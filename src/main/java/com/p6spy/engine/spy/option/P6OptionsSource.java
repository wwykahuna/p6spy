package com.p6spy.engine.spy.option;

import java.util.Map;

import com.p6spy.engine.spy.P6ModuleManager;

public interface P6OptionsSource {

  public Map<String, String> getOptions();

  void postInit(P6ModuleManager p6moduleManager);

  void preDestroy(P6ModuleManager p6moduleManager);
}