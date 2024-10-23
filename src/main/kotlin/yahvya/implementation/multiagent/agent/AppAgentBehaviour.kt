package yahvya.implementation.multiagent.agent

import jade.core.behaviours.Behaviour
import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.multiagent.interfaces.Exportable
import yahvya.implementation.plugins.loader.PluginsLoader

/**
 * @brief application agent behaviour
 */
abstract class AppAgentBehaviour : Behaviour(), Exportable {
    companion object{
        var AVAILABLE_AGENT_BEHAVIOURS_CLASSES: MutableList<Class<out AppAgentBehaviour>> = mutableListOf()

        init{
            this.loadAvailableAgentBehavioursPlugins()
        }

        /**
         * @brief load the available agent behaviours plugins
         * @throws Nothing
         */
        fun loadAvailableAgentBehavioursPlugins(){
            this.AVAILABLE_AGENT_BEHAVIOURS_CLASSES = PluginsLoader.loadPluginsClasses<AppAgentBehaviour>()
        }

        /**
         * @brief create a behaviour from the configuration
         * @param configuration configuration
         * @return the created behaviour
         * @throws Exception on error
         */
        fun createFromConfiguration(configuration: Map<*,*>): AppAgentBehaviour {
            ApplicationConfig.LOGGER.info("Creating agent behaviour <${this::class}> from configuration")

            if(!Exportable.containKeys(configuration = configuration, ExportKeys.CLASS, ExportKeys.SPECIFIC_CONFIGURATION))
                throw Exception("The provided configuration doesn't contain all expected keys")

            val behaviourClass = configuration[ExportKeys.CLASS] as String

            val foundedClass = this.AVAILABLE_AGENT_BEHAVIOURS_CLASSES.first{it.canonicalName == behaviourClass}
            val instance = foundedClass.getConstructor().newInstance() as AppAgentBehaviour

            if(!instance.loadFromExportedConfig(configuration = configuration[ExportKeys.SPECIFIC_CONFIGURATION] as Map<*,*>))
                throw Exception("Fail to load the behaviour")

            return instance
        }
    }

    /**
     * @return behaviour display name
     * @throws Nothing
     */
    abstract fun getDisplayName(): String

    /**
     * @brief build the save configuration
     * @throws Nothing
     */
    abstract fun buildConfiguration(): Map<*,*>

    override fun exportConfig(): Map<*, *> = mapOf<String,Any>(
        ExportKeys.CLASS to this.javaClass.canonicalName,
        ExportKeys.SPECIFIC_CONFIGURATION to this.buildConfiguration()
    )

    /**
     * @brief exports keys
     */
    data object ExportKeys{
        /**
         * @brief class
         */
        const val CLASS = "class"

        /**
         * @brief behaviour specific configuration
         */
        const val SPECIFIC_CONFIGURATION = "specificConfiguration"
    }
}