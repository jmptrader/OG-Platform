/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.credit.isdastandardmodel;

import java.util.Map;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

/**
 *
 */
public class ISDACompliantCreditCurve extends ISDACompliantCurve implements Bean {

  /**
   * Flat credit (hazard) curve at hazard rate h
   * @param t (arbitrary) single knot point (t > 0)
   * @param h the level
   */
  public ISDACompliantCreditCurve(final double t, final double h) {
    super(t, h);
  }

  /**
   * credit (hazard) curve with knots at times, t, zero hazard rates, h, at the knots and piecewise constant
   * forward hazard rates between knots (i.e. linear interpolation of h*t or the -log(survival-probability)
   * @param t knot (node) times
   * @param h zero hazard rates
   */
  public ISDACompliantCreditCurve(final double[] t, final double[] h) {
    super(t, h);
  }

  /**
   * Copy constructor - can be used to down cast from ISDACompliantCurve
   * @param from a ISDACompliantCurve
   */
  public ISDACompliantCreditCurve(final ISDACompliantCurve from) {
    super(from);
  }

  /**
   * @param t Set of times that form the knots of the curve. Must be ascending with the first value >= 0.
   * @param r Set of zero rates
   * @param rt Set of rates at the knot times
   * @param df Set of discount factors at the knot times
   * @param offsetTime The offset to the base date
   * @param offsetRT The offset rate
   * @deprecated This constructor is deprecated
   */
  @Deprecated
  public ISDACompliantCreditCurve(final double[] t, final double[] r, final double[] rt, final double[] df,  final double offsetTime, final double offsetRT) {
    super(t, r, rt, df, offsetTime, offsetRT);
  }

  /**
   * Get the zero hazard rate at time t (note: this simply a pseudonym for getZeroRate)
   * @param t time
   * @return zero hazard rate at time t
   */
  public double getHazardRate(final double t) {
    return getZeroRate(t);
  }

  /**
   * Get the survival probability at time t (note: this simply a pseudonym for getDiscountFactor)
   * @param t time
   * @return survival probability at time t
   */
  public double getSurvivalProbability(final double t) {
    return getDiscountFactor(t);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ISDACompliantCreditCurve withRates(final double[] r) {
    return new ISDACompliantCreditCurve(super.withRates(r));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ISDACompliantCreditCurve withRate(final double rate, final int index) {
    return new ISDACompliantCreditCurve(super.withRate(rate, index));
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ISDACompliantCreditCurve}.
   * @return the meta-bean, not null
   */
  public static ISDACompliantCreditCurve.Meta meta() {
    return ISDACompliantCreditCurve.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ISDACompliantCreditCurve.Meta.INSTANCE);
  }

  @Override
  public ISDACompliantCreditCurve.Meta metaBean() {
    return ISDACompliantCreditCurve.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  @Override
  public ISDACompliantCreditCurve clone() {
    BeanBuilder<? extends ISDACompliantCreditCurve> builder = metaBean().builder();
    for (MetaProperty<?> mp : metaBean().metaPropertyIterable()) {
      if (mp.style().isBuildable()) {
        Object value = mp.get(this);
        if (value instanceof Bean) {
          value = ((Bean) value).clone();
        }
        builder.set(mp.name(), value);
      }
    }
    return builder.build();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(32);
    buf.append("ISDACompliantCreditCurve{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ISDACompliantCreditCurve}.
   */
  public static class Meta extends ISDACompliantCurve.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap());

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    public BeanBuilder<? extends ISDACompliantCreditCurve> builder() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Class<? extends ISDACompliantCreditCurve> beanType() {
      return ISDACompliantCreditCurve.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
