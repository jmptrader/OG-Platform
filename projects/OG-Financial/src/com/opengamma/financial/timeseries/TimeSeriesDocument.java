/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.timeseries;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.beans.BeanDefinition;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.BasicMetaBean;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectMetaProperty;

import com.opengamma.id.IdentifierBundle;
import com.opengamma.id.UniqueIdentifiable;
import com.opengamma.id.UniqueIdentifier;
import com.opengamma.util.timeseries.DoubleTimeSeries;

/**
 * A document used to pass into and out of the Timeseries master.
 * 
 * @param <T> Type of time series 
 */
@BeanDefinition
public class TimeSeriesDocument<T> extends DirectBean implements UniqueIdentifiable {
  /**
   * The Timeseries unique identifier.
   */
  @PropertyDefinition
  private UniqueIdentifier _uniqueIdentifier;
  /**
   * The idenfifiers bundle
   */
  @PropertyDefinition
  private IdentifierBundle _identifiers;
  /**
   * The data source
   */
  @PropertyDefinition
  private String _dataSource;
  /**
   * The data provider
   */
  @PropertyDefinition
  private String _dataProvider;
  /**
   * The data field
   */
  @PropertyDefinition
  private String _dataField;
  /**
   * The observation time
   */
  @PropertyDefinition
  private String _observationTime;
  /**
   * The start date of timeseries
   */
  @PropertyDefinition
  private T _latest; 
  /**
   * The end date of timeseries
   */
  @PropertyDefinition
  private T _earliest;
  /**
   * The Timeseries.
   */
  @PropertyDefinition
  private DoubleTimeSeries<T> _timeSeries;

  /**
   * Creates an instance.
   */
  public TimeSeriesDocument() {
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code TimeSeriesDocument<T>}.
   * @param <R>  the bean's generic type
   * @return the meta-bean, not null
   */
  @SuppressWarnings("unchecked")
  public static <R> TimeSeriesDocument.Meta<R> meta() {
    return TimeSeriesDocument.Meta.INSTANCE;
  }

  @SuppressWarnings("unchecked")
  @Override
  public TimeSeriesDocument.Meta<T> metaBean() {
    return TimeSeriesDocument.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName) {
    switch (propertyName.hashCode()) {
      case -125484198:  // uniqueIdentifier
        return getUniqueIdentifier();
      case 1368189162:  // identifiers
        return getIdentifiers();
      case 1272470629:  // dataSource
        return getDataSource();
      case 339742651:  // dataProvider
        return getDataProvider();
      case -386794640:  // dataField
        return getDataField();
      case 951232793:  // observationTime
        return getObservationTime();
      case -1109880953:  // latest
        return getLatest();
      case -809579181:  // earliest
        return getEarliest();
      case 779431844:  // timeSeries
        return getTimeSeries();
    }
    return super.propertyGet(propertyName);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void propertySet(String propertyName, Object newValue) {
    switch (propertyName.hashCode()) {
      case -125484198:  // uniqueIdentifier
        setUniqueIdentifier((UniqueIdentifier) newValue);
        return;
      case 1368189162:  // identifiers
        setIdentifiers((IdentifierBundle) newValue);
        return;
      case 1272470629:  // dataSource
        setDataSource((String) newValue);
        return;
      case 339742651:  // dataProvider
        setDataProvider((String) newValue);
        return;
      case -386794640:  // dataField
        setDataField((String) newValue);
        return;
      case 951232793:  // observationTime
        setObservationTime((String) newValue);
        return;
      case -1109880953:  // latest
        setLatest((T) newValue);
        return;
      case -809579181:  // earliest
        setEarliest((T) newValue);
        return;
      case 779431844:  // timeSeries
        setTimeSeries((DoubleTimeSeries<T>) newValue);
        return;
    }
    super.propertySet(propertyName, newValue);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the Timeseries unique identifier.
   * @return the value of the property
   */
  public UniqueIdentifier getUniqueIdentifier() {
    return _uniqueIdentifier;
  }

  /**
   * Sets the Timeseries unique identifier.
   * @param uniqueIdentifier  the new value of the property
   */
  public void setUniqueIdentifier(UniqueIdentifier uniqueIdentifier) {
    this._uniqueIdentifier = uniqueIdentifier;
  }

  /**
   * Gets the the {@code uniqueIdentifier} property.
   * @return the property, not null
   */
  public final Property<UniqueIdentifier> uniqueIdentifier() {
    return metaBean().uniqueIdentifier().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the idenfifiers bundle
   * @return the value of the property
   */
  public IdentifierBundle getIdentifiers() {
    return _identifiers;
  }

  /**
   * Sets the idenfifiers bundle
   * @param identifiers  the new value of the property
   */
  public void setIdentifiers(IdentifierBundle identifiers) {
    this._identifiers = identifiers;
  }

  /**
   * Gets the the {@code identifiers} property.
   * @return the property, not null
   */
  public final Property<IdentifierBundle> identifiers() {
    return metaBean().identifiers().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the data source
   * @return the value of the property
   */
  public String getDataSource() {
    return _dataSource;
  }

  /**
   * Sets the data source
   * @param dataSource  the new value of the property
   */
  public void setDataSource(String dataSource) {
    this._dataSource = dataSource;
  }

  /**
   * Gets the the {@code dataSource} property.
   * @return the property, not null
   */
  public final Property<String> dataSource() {
    return metaBean().dataSource().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the data provider
   * @return the value of the property
   */
  public String getDataProvider() {
    return _dataProvider;
  }

  /**
   * Sets the data provider
   * @param dataProvider  the new value of the property
   */
  public void setDataProvider(String dataProvider) {
    this._dataProvider = dataProvider;
  }

  /**
   * Gets the the {@code dataProvider} property.
   * @return the property, not null
   */
  public final Property<String> dataProvider() {
    return metaBean().dataProvider().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the data field
   * @return the value of the property
   */
  public String getDataField() {
    return _dataField;
  }

  /**
   * Sets the data field
   * @param dataField  the new value of the property
   */
  public void setDataField(String dataField) {
    this._dataField = dataField;
  }

  /**
   * Gets the the {@code dataField} property.
   * @return the property, not null
   */
  public final Property<String> dataField() {
    return metaBean().dataField().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the observation time
   * @return the value of the property
   */
  public String getObservationTime() {
    return _observationTime;
  }

  /**
   * Sets the observation time
   * @param observationTime  the new value of the property
   */
  public void setObservationTime(String observationTime) {
    this._observationTime = observationTime;
  }

  /**
   * Gets the the {@code observationTime} property.
   * @return the property, not null
   */
  public final Property<String> observationTime() {
    return metaBean().observationTime().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the start date of timeseries
   * @return the value of the property
   */
  public T getLatest() {
    return _latest;
  }

  /**
   * Sets the start date of timeseries
   * @param latest  the new value of the property
   */
  public void setLatest(T latest) {
    this._latest = latest;
  }

  /**
   * Gets the the {@code latest} property.
   * @return the property, not null
   */
  public final Property<T> latest() {
    return metaBean().latest().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the end date of timeseries
   * @return the value of the property
   */
  public T getEarliest() {
    return _earliest;
  }

  /**
   * Sets the end date of timeseries
   * @param earliest  the new value of the property
   */
  public void setEarliest(T earliest) {
    this._earliest = earliest;
  }

  /**
   * Gets the the {@code earliest} property.
   * @return the property, not null
   */
  public final Property<T> earliest() {
    return metaBean().earliest().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the Timeseries.
   * @return the value of the property
   */
  public DoubleTimeSeries<T> getTimeSeries() {
    return _timeSeries;
  }

  /**
   * Sets the Timeseries.
   * @param timeSeries  the new value of the property
   */
  public void setTimeSeries(DoubleTimeSeries<T> timeSeries) {
    this._timeSeries = timeSeries;
  }

  /**
   * Gets the the {@code timeSeries} property.
   * @return the property, not null
   */
  public final Property<DoubleTimeSeries<T>> timeSeries() {
    return metaBean().timeSeries().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code TimeSeriesDocument}.
   */
  public static class Meta<T> extends BasicMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    @SuppressWarnings("rawtypes")
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code uniqueIdentifier} property.
     */
    private final MetaProperty<UniqueIdentifier> _uniqueIdentifier = DirectMetaProperty.ofReadWrite(this, "uniqueIdentifier", UniqueIdentifier.class);
    /**
     * The meta-property for the {@code identifiers} property.
     */
    private final MetaProperty<IdentifierBundle> _identifiers = DirectMetaProperty.ofReadWrite(this, "identifiers", IdentifierBundle.class);
    /**
     * The meta-property for the {@code dataSource} property.
     */
    private final MetaProperty<String> _dataSource = DirectMetaProperty.ofReadWrite(this, "dataSource", String.class);
    /**
     * The meta-property for the {@code dataProvider} property.
     */
    private final MetaProperty<String> _dataProvider = DirectMetaProperty.ofReadWrite(this, "dataProvider", String.class);
    /**
     * The meta-property for the {@code dataField} property.
     */
    private final MetaProperty<String> _dataField = DirectMetaProperty.ofReadWrite(this, "dataField", String.class);
    /**
     * The meta-property for the {@code observationTime} property.
     */
    private final MetaProperty<String> _observationTime = DirectMetaProperty.ofReadWrite(this, "observationTime", String.class);
    /**
     * The meta-property for the {@code latest} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<T> _latest = (DirectMetaProperty) DirectMetaProperty.ofReadWrite(this, "latest", Object.class);
    /**
     * The meta-property for the {@code earliest} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<T> _earliest = (DirectMetaProperty) DirectMetaProperty.ofReadWrite(this, "earliest", Object.class);
    /**
     * The meta-property for the {@code timeSeries} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<DoubleTimeSeries<T>> _timeSeries = DirectMetaProperty.ofReadWrite(this, "timeSeries", (Class) DoubleTimeSeries.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map;

    @SuppressWarnings({"unchecked", "rawtypes" })
    protected Meta() {
      LinkedHashMap temp = new LinkedHashMap();
      temp.put("uniqueIdentifier", _uniqueIdentifier);
      temp.put("identifiers", _identifiers);
      temp.put("dataSource", _dataSource);
      temp.put("dataProvider", _dataProvider);
      temp.put("dataField", _dataField);
      temp.put("observationTime", _observationTime);
      temp.put("latest", _latest);
      temp.put("earliest", _earliest);
      temp.put("timeSeries", _timeSeries);
      _map = Collections.unmodifiableMap(temp);
    }

    @Override
    public TimeSeriesDocument<T> createBean() {
      return new TimeSeriesDocument<T>();
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public Class<? extends TimeSeriesDocument<T>> beanType() {
      return (Class) TimeSeriesDocument.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code uniqueIdentifier} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<UniqueIdentifier> uniqueIdentifier() {
      return _uniqueIdentifier;
    }

    /**
     * The meta-property for the {@code identifiers} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<IdentifierBundle> identifiers() {
      return _identifiers;
    }

    /**
     * The meta-property for the {@code dataSource} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> dataSource() {
      return _dataSource;
    }

    /**
     * The meta-property for the {@code dataProvider} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> dataProvider() {
      return _dataProvider;
    }

    /**
     * The meta-property for the {@code dataField} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> dataField() {
      return _dataField;
    }

    /**
     * The meta-property for the {@code observationTime} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> observationTime() {
      return _observationTime;
    }

    /**
     * The meta-property for the {@code latest} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<T> latest() {
      return _latest;
    }

    /**
     * The meta-property for the {@code earliest} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<T> earliest() {
      return _earliest;
    }

    /**
     * The meta-property for the {@code timeSeries} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<DoubleTimeSeries<T>> timeSeries() {
      return _timeSeries;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
