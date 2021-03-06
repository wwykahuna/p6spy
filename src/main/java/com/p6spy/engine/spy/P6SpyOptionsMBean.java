package com.p6spy.engine.spy;

import java.util.Set;

public interface P6SpyOptionsMBean {

  /**
   * Reloads the whole configuration. 
   */
  void reload();
  
  void setAutoflush(boolean autoflush);

  boolean getAutoflush();

  String getDriverlist();

  void setDriverlist(String driverlist);

  Set<String> getDriverNames();

  boolean getReloadProperties();

  void setReloadProperties(boolean reloadproperties);

  long getReloadPropertiesInterval();

  void setReloadPropertiesInterval(long reloadpropertiesinterval);

  void setJNDIContextFactory(String jndicontextfactory);

  String getJNDIContextFactory();
  
  void unSetJNDIContextFactory();

  void setJNDIContextProviderURL(String jndicontextproviderurl);
  
  void unSetJNDIContextProviderURL();

  String getJNDIContextProviderURL();

  void setJNDIContextCustom(String jndicontextcustom);
  
  void unSetJNDIContextCustom();

  String getJNDIContextCustom();

  void setRealDataSource(String realdatasource);
  
  void unSetRealDataSource();

  String getRealDataSource();

  void setRealDataSourceClass(String realdatasourceclass);
  
  void unSetRealDataSourceClass();

  String getRealDataSourceClass();

  void setRealDataSourceProperties(String realdatasourceproperties);
  
  void unSetRealDataSourceProperties();

  String getRealDataSourceProperties();

  String getModulelist();

  void setModulelist(String modulelist);

  Set<String> getModuleNames();

  String getDatabaseDialectDateFormat();

  void setDatabaseDialectDateFormat(String databaseDialectDateFormat);

  String getDatabaseDialectTimestampFormat();

  void setDatabaseDialectTimestampFormat(String databaseDialectTimestampFormat);

  String getDatabaseDialectBooleanFormat();

  void setDatabaseDialectBooleanFormat(String databaseDialectBooleanFormat);
  
  /**
   * Gets the class name of the database dialect binary formatter.
   */
  String getDatabaseDialectBinaryFormat();

  /**
   * Sets the class name of the database dialect binary formatter.
   */
  void setDatabaseDialectBinaryFormat(String className);
  
  String getCustomLogMessageFormat();

  void setCustomLogMessageFormat(String customLogMessageFormat);

  void setAppend(boolean append);

  boolean getAppend();

  void setLogfile(String logfile);

  String getLogfile();

  String getAppender();

  void setAppender(String className);

  void setDateformat(String dateformat);

  String getDateformat();

  boolean getStackTrace();

  void setStackTrace(boolean stacktrace);
  
  String getStackTraceClass();

  void setStackTraceClass(String stacktraceclass);

  String getLogMessageFormat();

  void setLogMessageFormat(String logMessageFormatter);
  
  boolean getJmx();
  
  void setJmx(boolean jmx);
  
  String getJmxPrefix();
  
  void setJmxPrefix(String jmxPrefix);

}