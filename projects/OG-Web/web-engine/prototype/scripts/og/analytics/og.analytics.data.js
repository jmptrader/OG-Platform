/*
 * Copyright 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * Please see distribution for license.
 */
$.register_module({
    name: 'og.analytics.Data',
    dependencies: ['og.api.rest'],
    obj: function () {
        var module = this, counter = 1, connections = {};
        $(window).on('unload', function () {
            Object.keys(connections).forEach(function (key) {try {connections[key].kill();} catch (error) {}});
        });
        var constructor = function (config) {
            var data = this, api = og.api.rest.views, id = 'data_' + counter++ + '_' + +new Date, meta, cols,
                fire = og.common.events.fire, viewport = null, view_id = config.view, graph_id, viewport_id,
                viewport_version, subscribed = false, ROOT = 'rootNode', SETS = 'columnSets', ROWS = 'rowCount',
                grid_type = config.type, depgraph = !!config.depgraph,
                fixed_set = {portfolio: 'Portfolio', primitives: 'Primitives'};
            var data_handler = function (result) {
                if (!result || result.error)
                    return og.dev.warn(module.name + ': ' + (result && result.message || 'reset connection'));
                if (!data.events.data.length || !result.data) return; // if a tree falls or there's no tree, etc.
                if (result.data.version === viewport_version) try {fire(data.events.data, result.data.data);}
                    catch (error) {return og.dev.warn(module.name + ': killed connection due to ', error), data.kill();}
            };
            var data_setup = function () {
                if (!view_id || !viewport) return;
                var viewports = (depgraph ? api[grid_type].depgraphs : api[grid_type]).viewports;
                subscribed = true;
                (viewport_id ? viewports.get({
                    view_id: view_id, graph_id: graph_id, viewport_id: viewport_id, update: data_setup
                }) : viewports
                    .put({
                        view_id: view_id, graph_id: graph_id,
                        rows: viewport.rows, columns: viewport.cols, expanded: viewport.expanded
                    })
                    .pipe(function (result) {
                        if (result.error) // goes to data_setup so take care
                            return (view_id = graph_id = viewport_id = subscribed = null);
                        (viewport_id = result.meta.id), (viewport_version = result.data.version);
                        return viewports
                            .get({view_id: view_id, graph_id: graph_id, viewport_id: viewport_id, update: data_setup});
                    })
                ).pipe(data_handler);
            };
            var grid_handler = function (result) {
                if (depgraph && !graph_id) return;
                if (!result || result.error) return (view_id = graph_id = viewport_id = subscribed = null),
                    og.dev.warn(module.name + ': ' + (result && result.message || 'reset connection')), initialize();
                if (!result.data[SETS].length) return;
                meta.data_rows = result.data[ROOT] ? result.data[ROOT][1] + 1 : result.data[ROWS];
                meta.structure = result.data[ROOT] || [];
                meta.columns.fixed = [{name: fixed_set[grid_type], columns: result.data[SETS][0].columns}];
                meta.columns.scroll = result.data[SETS].slice(1);
                try {fire(data.events.meta, meta);}
                catch (error) {return og.dev.warn(module.name + ': killed connection due to ', error), data.kill();}
                if (!subscribed) return data_setup();
            };
            var grid_setup = function () {
                if (!view_id) return;
                return depgraph ?
                    api[grid_type].grid.get({view_id: view_id, update: initialize}).pipe(function (result) {
                        if (result.error || !result.data[SETS].length) return; // goes to grid_handler so take care
                        if (graph_id) return api[grid_type].depgraphs.grid
                            .get({view_id: view_id, graph_id: graph_id, update: initialize});
                        return api[grid_type].depgraphs.put({row: config.row, col: config.col, view_id: view_id})
                            .pipe(function (result) {
                                return api[grid_type].depgraphs.grid
                                    .get({view_id: view_id, graph_id: (graph_id = result.meta.id), update: initialize});
                            })
                    })
                    : api[grid_type].grid.get({view_id: view_id, update: initialize});
            };
            var initialize = function () {
                var put_options = ['viewdefinition', 'aggregators', 'providers']
                    .reduce(function (acc, val) {return (acc[val] = config[val]), acc;}, {});
                (view_id ? grid_setup() : api.put(put_options).pipe(view_handler)).pipe(grid_handler);
            };
            var view_handler = function (result) {return (view_id = result.meta.id), grid_setup();};
            data.busy = (function (busy) {
                return function (value) {return busy = typeof value !== 'undefined' ? value : busy;};
            })(false);
            data.events = {meta: [], data: []};
            data.id = id;
            data.kill = function () {
                if (view_id) api.del({view_id: view_id}).pipe(function (result) {
                    view_id = null;
                    delete connections[data.id];
                });
            };
            data.meta = meta = {columns: {}};
            data.cols = cols = {};
            data.viewport = function (new_viewport) {
                var viewports = (depgraph ? api[grid_type].depgraphs : api[grid_type]).viewports;
                if (!new_viewport.rows.length || !new_viewport.cols.length)
                    return og.dev.warn(module.name + ': nonsensical viewport, ', new_viewport), data;
                viewport = new_viewport;
                if (!viewport_id) return data;
                data.busy(true);
                viewports.put({
                    view_id: view_id, graph_id: graph_id, viewport_id: viewport_id,
                    rows: viewport.rows, columns: viewport.cols, expanded: !!viewport.expanded
                }).pipe(function (result) {
                    if (result.error) return; else (viewport_version = result.data.version), data.busy(false);
                });
                return data;
            };
            connections[data.id] = data;
            initialize();
        };
        constructor.prototype.on = og.common.events.on;
        return constructor;
    }
});