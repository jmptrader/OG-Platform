/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.curve.exposure;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;

import java.util.List;

import org.testng.annotations.Test;

import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.financial.security.cash.CashSecurity;
import com.opengamma.financial.security.fra.FRASecurity;
import com.opengamma.financial.security.future.AgricultureFutureSecurity;
import com.opengamma.financial.security.future.BondFutureSecurity;
import com.opengamma.financial.security.future.EnergyFutureSecurity;
import com.opengamma.financial.security.future.EquityFutureSecurity;
import com.opengamma.financial.security.future.EquityIndexDividendFutureSecurity;
import com.opengamma.financial.security.future.FXFutureSecurity;
import com.opengamma.financial.security.future.IndexFutureSecurity;
import com.opengamma.financial.security.future.InterestRateFutureSecurity;
import com.opengamma.financial.security.future.MetalFutureSecurity;
import com.opengamma.financial.security.future.StockFutureSecurity;
import com.opengamma.id.ExternalId;
import com.opengamma.util.test.TestGroup;

/**
 * 
 */
@Test(groups = TestGroup.UNIT)
public class UnderlyingExposureFunctionTest {

  @Test
  public void testAgriculturalFutureSecurity() {
    final SecuritySource securitySource = ExposureFunctionTestHelper.getSecuritySource(null);
    final ExposureFunction exposureFunction = new UnderlyingExposureFunction(securitySource);
    final AgricultureFutureSecurity future = ExposureFunctionTestHelper.getAgricultureFutureSecurity();
    final List<ExternalId> ids = future.accept(exposureFunction);
    assertNull(ids);
  }

  @Test
  public void testBondFutureSecurity() {
    final SecuritySource securitySource = ExposureFunctionTestHelper.getSecuritySource(null);
    final ExposureFunction exposureFunction = new UnderlyingExposureFunction(securitySource);
    final BondFutureSecurity future = ExposureFunctionTestHelper.getBondFutureSecurity();
    final List<ExternalId> ids = future.accept(exposureFunction);
    assertNull(ids);
  }

  @Test
  public void testEnergyFutureSecurity() {
    final SecuritySource securitySource = ExposureFunctionTestHelper.getSecuritySource(null);
    final ExposureFunction exposureFunction = new UnderlyingExposureFunction(securitySource);
    final EnergyFutureSecurity future = ExposureFunctionTestHelper.getEnergyFutureSecurity();
    final List<ExternalId> ids = future.accept(exposureFunction);
    assertNull(ids);
  }

  @Test
  public void testEquityFutureSecurity() {
    final SecuritySource securitySource = ExposureFunctionTestHelper.getSecuritySource(null);
    final ExposureFunction exposureFunction = new UnderlyingExposureFunction(securitySource);
    final EquityFutureSecurity future = ExposureFunctionTestHelper.getEquityFutureSecurity();
    final List<ExternalId> ids = future.accept(exposureFunction);
    assertNull(ids);
  }

  @Test
  public void testCashSecurity() {
    final SecuritySource securitySource = ExposureFunctionTestHelper.getSecuritySource(null);
    final ExposureFunction exposureFunction = new UnderlyingExposureFunction(securitySource);
    final CashSecurity cash = ExposureFunctionTestHelper.getCashSecurity();
    final List<ExternalId> ids = cash.accept(exposureFunction);
    assertNull(ids);
  }

  @Test
  public void testEquityIndexDividendFutureSecurity() {
    final SecuritySource securitySource = ExposureFunctionTestHelper.getSecuritySource(null);
    final EquityIndexDividendFutureSecurity future = ExposureFunctionTestHelper.getEquityIndexDividendFutureSecurity();
    final ExposureFunction exposureFunction = new UnderlyingExposureFunction(securitySource);
    final List<ExternalId> ids = future.accept(exposureFunction);
    assertNull(ids);
  }

  @Test
  public void testFRASecurity() {
    final SecuritySource securitySource = ExposureFunctionTestHelper.getSecuritySource(null);
    final ExposureFunction exposureFunction = new UnderlyingExposureFunction(securitySource);
    final FRASecurity fra = ExposureFunctionTestHelper.getFRASecurity();
    final List<ExternalId> ids = fra.accept(exposureFunction);
    assertEquals(1, ids.size());
    assertEquals(fra.getUnderlyingId(), ids.get(0));
  }

  @Test
  public void testFXFutureSecurity() {
    final SecuritySource securitySource = ExposureFunctionTestHelper.getSecuritySource(null);
    final ExposureFunction exposureFunction = new UnderlyingExposureFunction(securitySource);
    final FXFutureSecurity future = ExposureFunctionTestHelper.getFXFutureSecurity();
    final List<ExternalId> ids = future.accept(exposureFunction);
    assertNull(ids);
  }

  @Test
  public void testIndexFutureSecurity() {
    final SecuritySource securitySource = ExposureFunctionTestHelper.getSecuritySource(null);
    final ExposureFunction exposureFunction = new UnderlyingExposureFunction(securitySource);
    final IndexFutureSecurity future = ExposureFunctionTestHelper.getIndexFutureSecurity();
    final List<ExternalId> ids = future.accept(exposureFunction);
    assertNull(ids);
  }

  @Test
  public void testInterestRateFutureSecurity() {
    final SecuritySource securitySource = ExposureFunctionTestHelper.getSecuritySource(null);
    final ExposureFunction exposureFunction = new UnderlyingExposureFunction(securitySource);
    final InterestRateFutureSecurity future = ExposureFunctionTestHelper.getInterestRateFutureSecurity();
    final List<ExternalId> ids = future.accept(exposureFunction);
    assertEquals(1, ids.size());
    assertEquals(ExternalSchemes.syntheticSecurityId("USD 3m Libor"), ids.get(0));
  }

  @Test
  public void testMetalFutureSecurity() {
    final SecuritySource securitySource = ExposureFunctionTestHelper.getSecuritySource(null);
    final ExposureFunction exposureFunction = new UnderlyingExposureFunction(securitySource);
    final MetalFutureSecurity future = ExposureFunctionTestHelper.getMetalFutureSecurity();
    final List<ExternalId> ids = future.accept(exposureFunction);
    assertNull(ids);
  }

  @Test
  public void testStockFutureSecurity() {
    final SecuritySource securitySource = ExposureFunctionTestHelper.getSecuritySource(null);
    final ExposureFunction exposureFunction = new UnderlyingExposureFunction(securitySource);
    final StockFutureSecurity future = ExposureFunctionTestHelper.getStockFutureSecurity();
    final List<ExternalId> ids = future.accept(exposureFunction);
    assertNull(ids);
  }
}
