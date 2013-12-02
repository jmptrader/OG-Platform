/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.provider.description;

import static com.opengamma.util.money.Currency.EUR;
import static com.opengamma.util.money.Currency.GBP;
import static com.opengamma.util.money.Currency.USD;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.opengamma.analytics.financial.instrument.index.IborIndex;
import com.opengamma.analytics.financial.instrument.index.IndexIborMaster;
import com.opengamma.analytics.financial.legalentity.CreditRating;
import com.opengamma.analytics.financial.legalentity.LegalEntity;
import com.opengamma.analytics.financial.legalentity.LegalEntityCombinedMeta;
import com.opengamma.analytics.financial.legalentity.LegalEntityCreditRatings;
import com.opengamma.analytics.financial.legalentity.LegalEntityMeta;
import com.opengamma.analytics.financial.legalentity.LegalEntityRegion;
import com.opengamma.analytics.financial.legalentity.LegalEntityShortName;
import com.opengamma.analytics.financial.legalentity.Region;
import com.opengamma.analytics.financial.legalentity.Sector;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldAndDiscountCurve;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldCurve;
import com.opengamma.analytics.financial.provider.description.interestrate.IssuerProviderDiscount;
import com.opengamma.analytics.financial.provider.description.interestrate.MulticurveProviderDiscount;
import com.opengamma.analytics.math.curve.ConstantDoublesCurve;
import com.opengamma.analytics.math.curve.InterpolatedDoublesCurve;
import com.opengamma.analytics.math.interpolation.CombinedInterpolatorExtrapolatorFactory;
import com.opengamma.analytics.math.interpolation.Interpolator1D;
import com.opengamma.analytics.math.interpolation.Interpolator1DFactory;
import com.opengamma.util.i18n.Country;
import com.opengamma.util.money.Currency;
import com.opengamma.util.tuple.Pair;
import com.opengamma.util.tuple.Pairs;

/**
 * Sets of market data used in tests. With issuers.
 */
@SuppressWarnings("unchecked")
public class IssuerProviderDiscountDataSets {
  /** A linear interpolator with flat extrapolation */
  private static final Interpolator1D LINEAR_FLAT = CombinedInterpolatorExtrapolatorFactory.getInterpolator(Interpolator1DFactory.LINEAR, Interpolator1DFactory.FLAT_EXTRAPOLATOR,
      Interpolator1DFactory.FLAT_EXTRAPOLATOR);

  /** US government issuer name */
  private static final String US_NAME = "US GOVT";
  /** Belgian government issuer name */
  private static final String BEL_NAME = "BELGIUM GOVT";
  /** German government issuer name */
  private static final String GER_NAME = "GERMANY GOVT";
  /** UK government issuer name */
  private static final String UK_NAME = "UK GOVT";

  /** US government legal entity */
  private static final LegalEntity US_GOVT = new LegalEntity(US_NAME, US_NAME, Sets.newHashSet(CreditRating.of("AA", "S&P", true)), Sector.of("Government"), Region.of("United States", Country.US, Currency.USD));
  /** Belgian government legal entity */
  private static final LegalEntity BEL_GOVT = new LegalEntity(BEL_NAME, BEL_NAME, Sets.newHashSet(CreditRating.of("A", "S&P", true)), Sector.of("Government"), Region.of("Belgium", Country.BE, Currency.EUR));
  /** German government legal entity */
  private static final LegalEntity GER_GOVT = new LegalEntity(GER_NAME, GER_NAME, Sets.newHashSet(CreditRating.of("AAA", "S&P", true)), Sector.of("Government"), Region.of("Germany", Country.DE, Currency.EUR));
  /** UK government legal entity */
  private static final LegalEntity UK_GOVT = new LegalEntity(UK_NAME, UK_NAME, Sets.newHashSet(CreditRating.of("B", "S&P", true)), Sector.of("Government"), Region.of("Great Britain", Country.GB, Currency.GBP));

  private static final IndexIborMaster MASTER_IBOR_INDEX = IndexIborMaster.getInstance();
  private static final IborIndex EURIBOR3M = MASTER_IBOR_INDEX.getIndex("EURIBOR3M");

  private static final double[] USD_DSC_TIME = new double[] {0.0, 0.5, 1.0, 2.0, 5.0, 10.0 };
  private static final double[] USD_DSC_RATE = new double[] {0.0120, 0.0120, 0.0120, 0.0140, 0.0140, 0.0140 };
  private static final String USD_DSC_NAME = "USD Dsc";
  private static final YieldAndDiscountCurve USD_DSC = new YieldCurve(USD_DSC_NAME, new InterpolatedDoublesCurve(USD_DSC_TIME, USD_DSC_RATE, LINEAR_FLAT, true, USD_DSC_NAME));

  private static final double[] EUR_DSC_TIME = new double[] {0.0, 0.5, 1.0, 2.0, 5.0, 10.0 };
  private static final double[] EUR_DSC_RATE = new double[] {0.0150, 0.0125, 0.0150, 0.0175, 0.0150, 0.0150 };
  private static final String EUR_DSC_NAME = "EUR Dsc";
  private static final YieldAndDiscountCurve EUR_DSC = new YieldCurve(EUR_DSC_NAME, new InterpolatedDoublesCurve(EUR_DSC_TIME, EUR_DSC_RATE, LINEAR_FLAT, true, EUR_DSC_NAME));
  private static final double[] EUR_FWD3_TIME = new double[] {0.0, 0.5, 1.0, 2.0, 3.0, 4.0, 5.0, 10.0 };
  private static final double[] EUR_FWD3_RATE = new double[] {0.0150, 0.0125, 0.0150, 0.0175, 0.0175, 0.0190, 0.0200, 0.0210 };
  private static final String EUR_FWD3_NAME = "EUR EURIBOR 3M";
  private static final YieldAndDiscountCurve EUR_FWD3 = new YieldCurve(EUR_FWD3_NAME, new InterpolatedDoublesCurve(EUR_FWD3_TIME, EUR_FWD3_RATE, LINEAR_FLAT, true, EUR_FWD3_NAME));

  private static final double[] GBP_DSC_TIME = new double[] {0.0, 0.5, 1.0, 2.0, 5.0, 10.0 };
  private static final double[] GBP_DSC_RATE = new double[] {0.0150, 0.0125, 0.0150, 0.0175, 0.0150, 0.0150 };
  private static final String GBP_DSC_NAME = "GBP Dsc";
  private static final YieldAndDiscountCurve GBP_DSC = new YieldCurve(GBP_DSC_NAME, new InterpolatedDoublesCurve(GBP_DSC_TIME, GBP_DSC_RATE, LINEAR_FLAT, true, GBP_DSC_NAME));

  private static final double[] USD_US_TIME = new double[] {0.0, 0.5, 1.0, 2.0, 5.0, 10.0 };
  private static final double[] USD_US_RATE = new double[] {0.0100, 0.0100, 0.0100, 0.0120, 0.0120, 0.0120 };
  private static final String USD_US_CURVE_NAME = "USD " + US_NAME;
  private static final YieldAndDiscountCurve US_USD_CURVE = new YieldCurve(USD_US_CURVE_NAME, new InterpolatedDoublesCurve(USD_US_TIME, USD_US_RATE, LINEAR_FLAT, true, USD_US_CURVE_NAME));
  private static final YieldAndDiscountCurve US_USD_CURVE_6 = new YieldCurve(USD_US_CURVE_NAME, new ConstantDoublesCurve(0.06, USD_US_CURVE_NAME));

  private static final double[] EUR_BEL_TIME = new double[] {0.0, 0.5, 1.0, 2.0, 5.0, 10.0 };
  private static final double[] EUR_BEL_RATE = new double[] {0.0250, 0.0225, 0.0250, 0.0275, 0.0250, 0.0250 };
  private static final String EUR_BEL_CURVE_NAME = "EUR " + BEL_NAME;
  private static final YieldAndDiscountCurve BEL_EUR_CURVE = new YieldCurve(EUR_BEL_CURVE_NAME, new InterpolatedDoublesCurve(EUR_BEL_TIME, EUR_BEL_RATE, LINEAR_FLAT, true, EUR_BEL_CURVE_NAME));

  private static final double[] EUR_GER_TIME = new double[] {0.0, 0.5, 1.0, 2.0, 5.0, 10.0 };
  private static final double[] EUR_GER_RATE = new double[] {0.0250, 0.0225, 0.0250, 0.0275, 0.0250, 0.0250 };
  private static final String GER_EUR_CURVE_NAME = "EUR " + GER_NAME;
  private static final YieldAndDiscountCurve GER_EUR_CURVE = new YieldCurve(GER_EUR_CURVE_NAME, new InterpolatedDoublesCurve(EUR_GER_TIME, EUR_GER_RATE, LINEAR_FLAT, true, USD_US_CURVE_NAME));

  private static final double[] UK_GBP_TIME = new double[] {0.0, 0.5, 1.0, 2.0, 5.0, 10.0 };
  private static final double[] UK_GBP_RATE = new double[] {0.0250, 0.0225, 0.0250, 0.0275, 0.0250, 0.0250 };
  private static final String UK_GBP_CURVE_NAME = "GBP " + UK_NAME;
  private static final YieldAndDiscountCurve UK_GBP_CURVE = new YieldCurve(UK_GBP_CURVE_NAME, new InterpolatedDoublesCurve(UK_GBP_TIME, UK_GBP_RATE, LINEAR_FLAT, true, UK_GBP_CURVE_NAME));
  /** Extracts the short name (i.e. issuer name) from a legal entity */
  private static final LegalEntityMeta<LegalEntity> SHORT_NAME_META = LegalEntityShortName.builder().create();
  /** A set of discounting curves for EUR, USD and GBP */
  private static final MulticurveProviderDiscount DISCOUNTING_CURVES = new MulticurveProviderDiscount();
  static {
    DISCOUNTING_CURVES.setCurve(USD, USD_DSC);
    DISCOUNTING_CURVES.setCurve(EUR, EUR_DSC);
    DISCOUNTING_CURVES.setCurve(EURIBOR3M, EUR_FWD3);
    DISCOUNTING_CURVES.setCurve(GBP, GBP_DSC);
  }
  /** A set of issuer-specific curves for US GOVT, BELGIUM GOVT, GERMANY GOVT and UK GOVT */
  private static final Map<Pair<Object, LegalEntityMeta<LegalEntity>>, YieldAndDiscountCurve> ISSUER_SPECIFIC = new LinkedHashMap<>();
  static {
    ISSUER_SPECIFIC.put(Pairs.of((Object) US_NAME, SHORT_NAME_META), US_USD_CURVE);
    ISSUER_SPECIFIC.put(Pairs.of((Object) BEL_NAME, SHORT_NAME_META), BEL_EUR_CURVE);
    ISSUER_SPECIFIC.put(Pairs.of((Object) GER_NAME, SHORT_NAME_META), GER_EUR_CURVE);
    ISSUER_SPECIFIC.put(Pairs.of((Object) UK_NAME, SHORT_NAME_META), UK_GBP_CURVE);
  }
  /** Extracts the country from a legal entity */
  private static final LegalEntityMeta<LegalEntity> COUNTRY_META;
  /** A set of country-specific curves for US, DE, UK and GB */
  private static final Map<Pair<Object, LegalEntityMeta<LegalEntity>>, YieldAndDiscountCurve> COUNTRY_SPECIFIC = new LinkedHashMap<>();
  static {
    COUNTRY_META = LegalEntityRegion.builder().useCountries().create();
    COUNTRY_SPECIFIC.put(Pairs.of((Object) Collections.singleton(Country.US), COUNTRY_META), US_USD_CURVE);
    COUNTRY_SPECIFIC.put(Pairs.of((Object) Collections.singleton(Country.BE), COUNTRY_META), BEL_EUR_CURVE);
    COUNTRY_SPECIFIC.put(Pairs.of((Object) Collections.singleton(Country.DE), COUNTRY_META), GER_EUR_CURVE);
    COUNTRY_SPECIFIC.put(Pairs.of((Object) Collections.singleton(Country.GB), COUNTRY_META), UK_GBP_CURVE);
  }
  /** Extracts the currency from a legal entity */
  private static final LegalEntityMeta<LegalEntity> CURRENCY_META;
  /** A set of currency-specific curves for USD, EUR and GBP */
  private static final Map<Pair<Object, LegalEntityMeta<LegalEntity>>, YieldAndDiscountCurve> CURRENCY_SPECIFIC = new LinkedHashMap<>();
  static {
    CURRENCY_META = LegalEntityRegion.builder().useCurrencies().create();
    CURRENCY_SPECIFIC.put(Pairs.of((Object) Collections.singleton(USD), CURRENCY_META), US_USD_CURVE);
    CURRENCY_SPECIFIC.put(Pairs.of((Object) Collections.singleton(EUR), CURRENCY_META), GER_EUR_CURVE);
    CURRENCY_SPECIFIC.put(Pairs.of((Object) Collections.singleton(GBP), CURRENCY_META), UK_GBP_CURVE);
  }
  /** Extracts the country and rating from a legal entity */
  private static final LegalEntityMeta<LegalEntity> COUNTRY_RATING_META;
  /** A set of country and rating-specific curves for US, BE, DE and GB */
  private static final Map<Pair<Object, LegalEntityMeta<LegalEntity>>, YieldAndDiscountCurve> COUNTRY_RATING_SPECIFIC = new LinkedHashMap<>();
  static {
    COUNTRY_RATING_META = LegalEntityCombinedMeta.builder()
        .useMeta(COUNTRY_META)
        .useMeta(LegalEntityCreditRatings.builder().useRatings().create())
        .create();
    final Set<Object> us = new HashSet<>();
    us.add(Sets.newHashSet(Country.US));
    us.add(Sets.newHashSet(Pairs.of("S&P", "AA")));
    final Set<Object> be = new HashSet<>();
    be.add(Sets.newHashSet(Country.BE));
    be.add(Sets.newHashSet(Pairs.of("S&P", "A")));
    final Set<Object> de = new HashSet<>();
    de.add(Sets.newHashSet(Country.DE));
    de.add(Sets.newHashSet(Pairs.of("S&P", "AAA")));
    final Set<Object> gb = new HashSet<>();
    gb.add(Sets.newHashSet(Country.GB));
    gb.add(Sets.newHashSet(Pairs.of("S&P", "B")));
    COUNTRY_RATING_SPECIFIC.put(Pairs.of((Object) us, COUNTRY_RATING_META), US_USD_CURVE);
    COUNTRY_RATING_SPECIFIC.put(Pairs.of((Object) be, COUNTRY_RATING_META), BEL_EUR_CURVE);
    COUNTRY_RATING_SPECIFIC.put(Pairs.of((Object) de, COUNTRY_RATING_META), GER_EUR_CURVE);
    COUNTRY_RATING_SPECIFIC.put(Pairs.of((Object) gb, COUNTRY_RATING_META), UK_GBP_CURVE);
  }

  /** US GOVT curve with constant 6% rate */
  private static final Map<Pair<Object, LegalEntityMeta<LegalEntity>>, YieldAndDiscountCurve> USD_GOVT_6PC = new LinkedHashMap<>();
  static {
    USD_GOVT_6PC.put(Pairs.of((Object) US_NAME, SHORT_NAME_META), US_USD_CURVE_6);
  }
  /** Curves for pricing bonds with issuer-specific risky curves */
  private static final IssuerProviderDiscount ISSUER_SPECIFIC_MULTICURVE = new IssuerProviderDiscount(DISCOUNTING_CURVES, ISSUER_SPECIFIC);
  /** Curves for pricing bonds with country-specific risky curves */
  private static final IssuerProviderDiscount COUNTRY_SPECIFIC_MULTICURVE = new IssuerProviderDiscount(DISCOUNTING_CURVES, COUNTRY_SPECIFIC);
  /** Curves for pricing bonds with currency-specific risky curves */
  private static final IssuerProviderDiscount CURRENCY_SPECIFIC_MULTICURVE = new IssuerProviderDiscount(DISCOUNTING_CURVES, CURRENCY_SPECIFIC);
  /** Curves for pricing bonds with country and rating-specific risky curves */
  private static final IssuerProviderDiscount COUNTRY_RATING_SPECIFIC_MULTICURVE = new IssuerProviderDiscount(DISCOUNTING_CURVES, COUNTRY_RATING_SPECIFIC);

  /**
   * Returns a multi-curves provider with three discounting currencies (USD, EUR, GBP), one Ibor (EURIBOR3M) and four issuers
   * (US GOVT, BELGIUM GOVT, GERMAN GOVT, UK GOVT).
   * @return The provider.
   */
  public static IssuerProviderDiscount getIssuerSpecificProvider() {
    return ISSUER_SPECIFIC_MULTICURVE;
  }

  /**
   * Returns a multi-curves provider with three discounting currencies (USD, EUR, GBP), one Ibor (EURIBOR3M) and four countries
   * (US, BE, DE, UK)
   * @return The provider
   */
  public static IssuerProviderDiscount getCountrySpecificProvider() {
    return COUNTRY_SPECIFIC_MULTICURVE;
  }

  /**
   * Returns a multi-curves provider with three discounting currencies (USD, EUR, GBP) and one Ibor (EURIBOR3M)
   * @return The provider
   */
  public static IssuerProviderDiscount getCurrencySpecificProvider() {
    return CURRENCY_SPECIFIC_MULTICURVE;
  }

  /**
   * Returns a multi-curves provider with three discounting currencies (USD, EUR, GBP) and one Ibor (EURIBOR3M) and
   * a combined rating for S&P and Moody's of AA for each country.
   * @return The provider
   */
  public static IssuerProviderDiscount getCountryRatingSpecificProvider() {
    return COUNTRY_RATING_SPECIFIC_MULTICURVE;
  }

  /**
   * Returns a multi-curves provider with one currency (USD) and one issuer (US GOVT). The issuer curve is at 6% (useful for futures).
   * @return The provider.
   */
  public static IssuerProviderDiscount createIssuerProvider6() {
    return new IssuerProviderDiscount(DISCOUNTING_CURVES, USD_GOVT_6PC);
  }

  /**
   * Gets the issuer names.
   * @return The issuer names
   */
  public static String[] getIssuerNames() {
    return new String[] {US_NAME, BEL_NAME, GER_NAME, UK_NAME };
  }

  /**
   * Gets the issuers.
   * @return The issuers
   */
  public static LegalEntity[] getIssuers() {
    return new LegalEntity[] {US_GOVT, BEL_GOVT, GER_GOVT, UK_GOVT};
  }
}
