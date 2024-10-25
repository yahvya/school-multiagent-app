package yahvya.implementation.multiagent.definitions

/**
 * @brief describe an exportable element as json
 */
interface Exportable {
    /**
     * @brief export the element as pair
     * @return element configuration
     * @throws Nothing
     */
    fun exportConfig() : Map<*,*>

    /**
     * @brief load the instance from an exported configuration
     * @param configuration exported configuration
     * @return if successfully loaded
     * @throws Nothing
     */
    fun loadFromExportedConfig(configuration: Map<*,*>): Boolean

    companion object {
        /**
         * @brief check if the configuration contains all expected keys
         * @param configuration configuration to check
         * @param keys keys to check
         * @return if the configuration contains all given keys
         * @throws Nothing
         */
        fun containKeys(configuration: Map<*,*>,vararg keys: String):Boolean = keys.all{configuration.containsKey(it)}
    }
}