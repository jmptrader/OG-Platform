/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.opengamma.financial.Currency;
import com.opengamma.financial.GICSCode;
import com.opengamma.id.IdentificationScheme;
import com.opengamma.id.Identifier;

/**
 * A {@code Security} used to model equities.
 */
public class EquitySecurity extends FinancialSecurity {

  /**
   * The security type of equity.
   */
  public static final String EQUITY_TYPE = "EQUITY";

  /**
   * The ticker symbol.
   */
  private String _ticker;
  /**
   * The exchange.
   */
  private String _exchange;
  /**
   * The exchange code.
   */
  private String _exchangeCode;
  /**
   * The company name.
   */
  private String _companyName;
  /**
   * The currency.
   */
  private Currency _currency;
  /**
   * The GICS code.
   */
  private GICSCode _gicsCode;

  // Identifiers that might be valid for equities:
  // - Bloomberg ticker (in BbgId)
  // - CUSIP (in CUSIP)
  // - ISIN (in ISIN)
  // - Bloomberg Unique ID (in BbgUniqueId)

  /**
   * Creates an equity security.
   */
  public EquitySecurity() {
    super(EQUITY_TYPE);
  }

  /**
   * This should be removed after the demo is fully Bloomberg modified.
   * 
   * @param scheme
   * @param value  the value
   */
  public EquitySecurity(String scheme, String value) {
    this();
    addIdentifier(new Identifier(new IdentificationScheme(scheme), value));
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the ticker.
   * @return the ticker
   */
  public String getTicker() {
    return _ticker;
  }

  /**
   * Sets the ticker.
   * @param ticker  the ticker to set
   */
  public void setTicker(String ticker) {
    _ticker = ticker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the exchange.
   * @return the exchange
   */
  public String getExchange() {
    return _exchange;
  }

  /**
   * Sets the exchange.
   * @param exchange  the exchange to set
   */
  public void setExchange(String exchange) {
    _exchange = exchange;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the exchange code.
   * @return the exchange code
   */
  public String getExchangeCode() {
    return _exchangeCode;
  }

  /**
   * Sets the exchange code.
   * @param exchangeCode  the exchange code to set
   */
  public void setExchangeCode(String exchangeCode) {
    _exchangeCode = exchangeCode;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the company name.
   * @return the company name
   */
  public String getCompanyName() {
    return _companyName;
  }

  /**
   * Sets the company name.
   * @param companyName  the company name to set
   */
  public void setCompanyName(String companyName) {
    _companyName = companyName;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the currency.
   * @return the currency
   */
  public Currency getCurrency() {
    return _currency;
  }

  /**
   * Sets the currency.
   * @param currency  the currency to set
   */
  public void setCurrency(Currency currency) {
    _currency = currency;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the GICS code.
   * @return the GICS Code
   */
  public GICSCode getGICSCode() {
    return _gicsCode;
  }

  /**
   * Sets the GICS code.
   * @param gicsCode  the GICS code to set
   */
  public void setGICSCode(GICSCode gicsCode) {
    _gicsCode = gicsCode;
  }

  //-------------------------------------------------------------------------
  /**
   * Override to use the company name as the display name.
   * @return the display name, not null
   */
  @Override
  protected String buildDefaultDisplayName() {
    if (getCompanyName() != null) {
      return getCompanyName();
    } else {
      return super.buildDefaultDisplayName();
    }
  }

  //-------------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  //-------------------------------------------------------------------------
  @Override
  public <T> T accept(FinancialSecurityVisitor<T> visitor) {
    return visitor.visitEquitySecurity(this);
  }

}
