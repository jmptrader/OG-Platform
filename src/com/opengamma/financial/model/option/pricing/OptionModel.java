package com.opengamma.financial.model.option.pricing;

import java.util.List;

import com.opengamma.financial.greeks.Greek;
import com.opengamma.financial.greeks.GreekResultCollection;
import com.opengamma.financial.model.option.definition.OptionDefinition;
import com.opengamma.financial.model.option.definition.StandardOptionDataBundle;

/**
 * 
 * @author emcleod
 * 
 */

public interface OptionModel<T extends OptionDefinition, U extends StandardOptionDataBundle> {

  public GreekResultCollection getGreeks(T definition, U vars, List<Greek> requiredGreeks);

}
