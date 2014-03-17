/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.sesame.config;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.annotation.Nullable;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.opengamma.sesame.function.Parameter;
import com.opengamma.util.ArgumentChecker;

/**
 * Basic function configuration implementation providing implementations and arguments.
 */
@BeanDefinition
public final class SimpleFunctionModelConfig implements FunctionModelConfig, ImmutableBean {

  /**
   * The function implementation classes keyed by function interface.
   * This only needs to be populated if the implementation is not the default.
   */
  @PropertyDefinition(validate = "notNull", get = "private")
  private final Map<Class<?>, Class<?>> _implementations;

  /** Implementation types that should only be used for satisfying a particular constructor parameter. */
  @PropertyDefinition(validate = "notNull", get = "private")
  private final Map<Parameter, Class<?>> _implementationsByParameter;

  /** The user-specified function arguments keyed by function implementation. */
  @PropertyDefinition(validate = "notNull", get = "private")
  private final Map<Class<?>, FunctionArguments> _arguments;

  public SimpleFunctionModelConfig(Map<Class<?>, Class<?>> implementations,
                                   Map<Class<?>, FunctionArguments> arguments,
                                   Set<Class<?>> decorators) {
    _arguments = ImmutableMap.copyOf(ArgumentChecker.notNull(arguments, "arguments"));
    _implementations = new HashMap<>(implementations);
    _implementationsByParameter = new HashMap<>();
    List<Class<?>> reversedDecorators = Lists.newArrayList(decorators);
    Collections.reverse(reversedDecorators);

    // TODO should this logic be somewhere else?
    for (Class<?> decorator : reversedDecorators) {
      Set<Class<?>> interfaces = EngineUtils.getInterfaces(decorator);

      if (interfaces.size() != 1) {
        throw new IllegalArgumentException("Decorator class " + decorator.getName() + " must implement exactly one interface");
      }
      Class<?> interfaceType = interfaces.iterator().next();
      Constructor<?> constructor = EngineUtils.getConstructor(decorator);
      Parameter delegateParameter = Parameter.ofType(interfaceType, constructor);

      Class<?> implementation = _implementations.get(interfaceType);

      if (implementation == null) {
        throw new IllegalArgumentException("No delegate available of type " + interfaceType.getName() + " for " +
                                               "decorator " + decorator.getName());
      }
      _implementations.put(interfaceType, decorator);
      _implementationsByParameter.put(delegateParameter, implementation);
    }
  }

  //-------------------------------------------------------------------------
  @Override
  public Class<?> getFunctionImplementation(Class<?> functionType, @Nullable Parameter parameter) {
    if (parameter == null) {
      return _implementations.get(functionType);
    }
    Class<?> typeForParameter = _implementationsByParameter.get(parameter);

    if (typeForParameter != null) {
      return typeForParameter;
    } else {
      return _implementations.get(functionType);
    }
  }

  @Override
  public FunctionArguments getFunctionArguments(Class<?> functionType) {
    FunctionArguments functionArguments = _arguments.get(functionType);
    return functionArguments == null ? FunctionArguments.EMPTY : functionArguments;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code SimpleFunctionModelConfig}.
   * @return the meta-bean, not null
   */
  public static SimpleFunctionModelConfig.Meta meta() {
    return SimpleFunctionModelConfig.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(SimpleFunctionModelConfig.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static SimpleFunctionModelConfig.Builder builder() {
    return new SimpleFunctionModelConfig.Builder();
  }

  private SimpleFunctionModelConfig(
      Map<Class<?>, Class<?>> implementations,
      Map<Parameter, Class<?>> implementationsByParameter,
      Map<Class<?>, FunctionArguments> arguments) {
    JodaBeanUtils.notNull(implementations, "implementations");
    JodaBeanUtils.notNull(implementationsByParameter, "implementationsByParameter");
    JodaBeanUtils.notNull(arguments, "arguments");
    this._implementations = ImmutableMap.copyOf(implementations);
    this._implementationsByParameter = ImmutableMap.copyOf(implementationsByParameter);
    this._arguments = ImmutableMap.copyOf(arguments);
  }

  @Override
  public SimpleFunctionModelConfig.Meta metaBean() {
    return SimpleFunctionModelConfig.Meta.INSTANCE;
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
  /**
   * Gets the function implementation classes keyed by function interface.
   * This only needs to be populated if the implementation is not the default.
   * @return the value of the property, not null
   */
  private Map<Class<?>, Class<?>> getImplementations() {
    return _implementations;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the implementationsByParameter.
   * @return the value of the property, not null
   */
  private Map<Parameter, Class<?>> getImplementationsByParameter() {
    return _implementationsByParameter;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the user-specified function arguments keyed by function implementation.
   * @return the value of the property, not null
   */
  private Map<Class<?>, FunctionArguments> getArguments() {
    return _arguments;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public SimpleFunctionModelConfig clone() {
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      SimpleFunctionModelConfig other = (SimpleFunctionModelConfig) obj;
      return JodaBeanUtils.equal(getImplementations(), other.getImplementations()) &&
          JodaBeanUtils.equal(getImplementationsByParameter(), other.getImplementationsByParameter()) &&
          JodaBeanUtils.equal(getArguments(), other.getArguments());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getImplementations());
    hash += hash * 31 + JodaBeanUtils.hashCode(getImplementationsByParameter());
    hash += hash * 31 + JodaBeanUtils.hashCode(getArguments());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("SimpleFunctionModelConfig{");
    buf.append("implementations").append('=').append(getImplementations()).append(',').append(' ');
    buf.append("implementationsByParameter").append('=').append(getImplementationsByParameter()).append(',').append(' ');
    buf.append("arguments").append('=').append(JodaBeanUtils.toString(getArguments()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SimpleFunctionModelConfig}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code implementations} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Map<Class<?>, Class<?>>> _implementations = DirectMetaProperty.ofImmutable(
        this, "implementations", SimpleFunctionModelConfig.class, (Class) Map.class);
    /**
     * The meta-property for the {@code implementationsByParameter} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Map<Parameter, Class<?>>> _implementationsByParameter = DirectMetaProperty.ofImmutable(
        this, "implementationsByParameter", SimpleFunctionModelConfig.class, (Class) Map.class);
    /**
     * The meta-property for the {@code arguments} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Map<Class<?>, FunctionArguments>> _arguments = DirectMetaProperty.ofImmutable(
        this, "arguments", SimpleFunctionModelConfig.class, (Class) Map.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "implementations",
        "implementationsByParameter",
        "arguments");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 643812097:  // implementations
          return _implementations;
        case 598891345:  // implementationsByParameter
          return _implementationsByParameter;
        case -2035517098:  // arguments
          return _arguments;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public SimpleFunctionModelConfig.Builder builder() {
      return new SimpleFunctionModelConfig.Builder();
    }

    @Override
    public Class<? extends SimpleFunctionModelConfig> beanType() {
      return SimpleFunctionModelConfig.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code implementations} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Map<Class<?>, Class<?>>> implementations() {
      return _implementations;
    }

    /**
     * The meta-property for the {@code implementationsByParameter} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Map<Parameter, Class<?>>> implementationsByParameter() {
      return _implementationsByParameter;
    }

    /**
     * The meta-property for the {@code arguments} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Map<Class<?>, FunctionArguments>> arguments() {
      return _arguments;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 643812097:  // implementations
          return ((SimpleFunctionModelConfig) bean).getImplementations();
        case 598891345:  // implementationsByParameter
          return ((SimpleFunctionModelConfig) bean).getImplementationsByParameter();
        case -2035517098:  // arguments
          return ((SimpleFunctionModelConfig) bean).getArguments();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code SimpleFunctionModelConfig}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<SimpleFunctionModelConfig> {

    private Map<Class<?>, Class<?>> _implementations = new HashMap<Class<?>, Class<?>>();
    private Map<Parameter, Class<?>> _implementationsByParameter = new HashMap<Parameter, Class<?>>();
    private Map<Class<?>, FunctionArguments> _arguments = new HashMap<Class<?>, FunctionArguments>();

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(SimpleFunctionModelConfig beanToCopy) {
      this._implementations = new HashMap<Class<?>, Class<?>>(beanToCopy.getImplementations());
      this._implementationsByParameter = new HashMap<Parameter, Class<?>>(beanToCopy.getImplementationsByParameter());
      this._arguments = new HashMap<Class<?>, FunctionArguments>(beanToCopy.getArguments());
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 643812097:  // implementations
          return _implementations;
        case 598891345:  // implementationsByParameter
          return _implementationsByParameter;
        case -2035517098:  // arguments
          return _arguments;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 643812097:  // implementations
          this._implementations = (Map<Class<?>, Class<?>>) newValue;
          break;
        case 598891345:  // implementationsByParameter
          this._implementationsByParameter = (Map<Parameter, Class<?>>) newValue;
          break;
        case -2035517098:  // arguments
          this._arguments = (Map<Class<?>, FunctionArguments>) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public SimpleFunctionModelConfig build() {
      return new SimpleFunctionModelConfig(
          _implementations,
          _implementationsByParameter,
          _arguments);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the {@code implementations} property in the builder.
     * @param implementations  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder implementations(Map<Class<?>, Class<?>> implementations) {
      JodaBeanUtils.notNull(implementations, "implementations");
      this._implementations = implementations;
      return this;
    }

    /**
     * Sets the {@code implementationsByParameter} property in the builder.
     * @param implementationsByParameter  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder implementationsByParameter(Map<Parameter, Class<?>> implementationsByParameter) {
      JodaBeanUtils.notNull(implementationsByParameter, "implementationsByParameter");
      this._implementationsByParameter = implementationsByParameter;
      return this;
    }

    /**
     * Sets the {@code arguments} property in the builder.
     * @param arguments  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder arguments(Map<Class<?>, FunctionArguments> arguments) {
      JodaBeanUtils.notNull(arguments, "arguments");
      this._arguments = arguments;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(128);
      buf.append("SimpleFunctionModelConfig.Builder{");
      buf.append("implementations").append('=').append(JodaBeanUtils.toString(_implementations)).append(',').append(' ');
      buf.append("implementationsByParameter").append('=').append(JodaBeanUtils.toString(_implementationsByParameter)).append(',').append(' ');
      buf.append("arguments").append('=').append(JodaBeanUtils.toString(_arguments));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
