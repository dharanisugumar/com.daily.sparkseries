package com.akamai.nsecore.util

class ConfigUtils {
  /**
   * Utility class for {openApi Configuration} related helper functions.
   */

  /**
     * Get Application Id. This method will check the new app id
     * @param configuration the configuration object
     * @return the job application id.
     */

    def getApplicationId(configuration: Configuration): String = if (configuration.containsKey(AppOptions.APP_ID.key)) configuration.getString(AppOptions.APP_ID)
    else AppOptions.APP_ID.defaultValue()

    /**
     * Get Process Id. This method will check the new app id
     * @param configuration the configuration object
     * @return the job Process id.
     */

    def getProcessId(configuration: Configuration): String = if (configuration.containsKey(AppOptions.PROCESS_ID.key)) configuration.getString(AppOptions.PROCESS_ID)
    else AppOptions.PROCESS_ID.defaultValue()





}
