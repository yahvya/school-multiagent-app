package yahvya.implementation.multiagent.definitions

/**
 * @brief configurable element
 */
interface Configurable {
    /**
     * @brief provide the elements to configure
     * @throws Nothing
     */
    fun getToConfigure(): List<String>

    /**
     * @brief allow to receive the configuration with the same key as getToConfigure
     * @param configuration configuration
     */
    fun receiveConfiguration(configuration: Map<String,String>)

    /**
     * @brief check if the configuration contain all the expected keys
     * @param configuration configuration
     * @return configuration is valid
     */
    fun isConfigurationValid(configuration: Map<String, String>): Boolean = this.getToConfigure().all{ configuration.containsKey(it) }
}