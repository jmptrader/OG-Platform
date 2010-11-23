/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.equity;

import java.util.Set;

import javax.time.calendar.Clock;
import javax.time.calendar.LocalDate;

import org.apache.commons.lang.Validate;

import com.google.common.collect.Sets;
import com.opengamma.core.historicaldata.HistoricalDataSource;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.function.AbstractFunction;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.financial.OpenGammaExecutionContext;
import com.opengamma.financial.convention.ConventionBundle;
import com.opengamma.financial.convention.ConventionBundleSource;
import com.opengamma.financial.convention.InMemoryConventionBundleMaster;
import com.opengamma.financial.equity.CAPMBetaCalculator;
import com.opengamma.financial.timeseries.analysis.DoubleTimeSeriesStatisticsCalculator;
import com.opengamma.financial.timeseries.returns.TimeSeriesReturnCalculator;
import com.opengamma.financial.timeseries.returns.TimeSeriesReturnCalculatorFactory;
import com.opengamma.id.IdentificationScheme;
import com.opengamma.id.Identifier;
import com.opengamma.id.IdentifierBundle;
import com.opengamma.id.UniqueIdentifier;
import com.opengamma.math.statistics.descriptive.SampleCovarianceCalculator;
import com.opengamma.math.statistics.descriptive.SampleVarianceCalculator;
import com.opengamma.util.timeseries.DoubleTimeSeries;
import com.opengamma.util.timeseries.localdate.LocalDateDoubleTimeSeries;
import com.opengamma.util.tuple.Pair;

/**
 * 
 */
public abstract class CAPMBetaModelFunction extends AbstractFunction.NonCompiledInvoker {
  private static final DoubleTimeSeriesStatisticsCalculator COVARIANCE_CALCULATOR = new DoubleTimeSeriesStatisticsCalculator(new SampleCovarianceCalculator());
  private static final DoubleTimeSeriesStatisticsCalculator VARIANCE_CALCULATOR = new DoubleTimeSeriesStatisticsCalculator(new SampleVarianceCalculator());
  private final TimeSeriesReturnCalculator _returnCalculator;
  private final CAPMBetaCalculator _model;
  private final LocalDate _startDate;

  //TODO pass in covariance and variance calculator names
  //TODO need to use schedule for price series
  public CAPMBetaModelFunction(final String returnCalculatorName, final String startDate) {
    Validate.notNull(returnCalculatorName, "return calculator name");
    Validate.notNull(startDate, "startDate");
    _returnCalculator = TimeSeriesReturnCalculatorFactory.getReturnCalculator(returnCalculatorName);
    _model = new CAPMBetaCalculator(COVARIANCE_CALCULATOR, VARIANCE_CALCULATOR);
    _startDate = LocalDate.parse(startDate);
  }

  @Override
  public Set<ComputedValue> execute(final FunctionExecutionContext executionContext, final FunctionInputs inputs, final ComputationTarget target,
      final Set<ValueRequirement> desiredValues) {
    final Object positionOrNode = getTarget(target);
    final ConventionBundleSource conventionSource = OpenGammaExecutionContext.getConventionBundleSource(executionContext);
    final ConventionBundle bundle = conventionSource.getConventionBundle(Identifier.of(InMemoryConventionBundleMaster.SIMPLE_NAME_SCHEME, "USD_CAPM"));
    final Clock snapshotClock = executionContext.getSnapshotClock();
    final LocalDate now = snapshotClock.zonedDateTime().toLocalDate();
    final HistoricalDataSource historicalDataSource = OpenGammaExecutionContext.getHistoricalDataSource(executionContext);
    final Pair<UniqueIdentifier, LocalDateDoubleTimeSeries> marketTSObject = historicalDataSource.getHistoricalData(IdentifierBundle.of(Identifier.of(
        IdentificationScheme.BLOOMBERG_TICKER, bundle.getCAPMMarketName())), "BLOOMBERG", null, "PX_LAST", _startDate, true, now, false);
    final Object assetPnLObject = inputs.getValue(new ValueRequirement(ValueRequirementNames.PNL_SERIES, positionOrNode));
    final Object fairValueObject = inputs.getValue(new ValueRequirement(ValueRequirementNames.FAIR_VALUE, positionOrNode));
    if (marketTSObject != null && assetPnLObject != null && fairValueObject != null) {
      final double fairValue = (Double) fairValueObject;
      DoubleTimeSeries<?> marketReturn = _returnCalculator.evaluate(marketTSObject.getSecond());
      DoubleTimeSeries<?> assetReturn = ((DoubleTimeSeries<?>) assetPnLObject).divide(fairValue);
      assetReturn = assetReturn.intersectionFirstValue(marketReturn);
      marketReturn = marketReturn.intersectionFirstValue(assetReturn);
      final double beta = _model.evaluate(assetReturn, marketReturn);
      return Sets.newHashSet(new ComputedValue(new ValueSpecification(new ValueRequirement(ValueRequirementNames.CAPM_BETA, positionOrNode), getUniqueIdentifier()), beta));
    }
    throw new NullPointerException("Could not get both market time series, asset time series and fair value");
  }

  @Override
  public Set<ValueRequirement> getRequirements(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue) {
    if (canApplyTo(context, target)) {
      final Object positionOrNode = getTarget(target);
      return Sets.newHashSet(new ValueRequirement(ValueRequirementNames.PNL_SERIES, positionOrNode), new ValueRequirement(ValueRequirementNames.FAIR_VALUE, positionOrNode));
    }
    return null;
  }

  @Override
  public Set<ValueSpecification> getResults(final FunctionCompilationContext context, final ComputationTarget target) {
    if (canApplyTo(context, target)) {
      return Sets.newHashSet(new ValueSpecification(new ValueRequirement(ValueRequirementNames.CAPM_BETA, getTarget(target)), getUniqueIdentifier()));
    }
    return null;
  }

  public abstract Object getTarget(ComputationTarget target);

}
