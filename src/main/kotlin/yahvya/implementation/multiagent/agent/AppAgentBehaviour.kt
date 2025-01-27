package yahvya.implementation.multiagent.agent

import jade.core.behaviours.Behaviour
import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.configurations.DefaultPluginsConfig
import yahvya.implementation.multiagent.definitions.Configurable
import yahvya.implementation.multiagent.definitions.Exportable
import yahvya.implementation.pluginsmanager.loader.PluginsLoader
import kotlin.reflect.KProperty

/**
 * @brief application agent behaviour
 */
abstract class AppAgentBehaviour : Behaviour(), Exportable, Configurable {
    /**
     * @brief behaviour configuration
     */
    var configuration: Map<*,*> = mapOf<String,String>()

    companion object{
        val AVAILABLE_AGENT_BEHAVIOURS_CLASSES: MutableList<Class<out AppAgentBehaviour>> by object {
            operator fun getValue(thisRef: Any?, property: KProperty<*>): MutableList<Class<out AppAgentBehaviour>> = loadAvailableAgentBehavioursPlugins()
        }

        /**
         * @brief load the available agent behaviours plugins
         * @throws Nothing
         * @return plugins list
         */
        fun loadAvailableAgentBehavioursPlugins():MutableList<Class<out AppAgentBehaviour>>{
            ApplicationConfig.LOGGER.info { "Loading available agent behaviours plugins" }

            val result: MutableList<Class<out AppAgentBehaviour>> = PluginsLoader.loadPluginsClasses<AppAgentBehaviour>()
            result.addAll(DefaultPluginsConfig.DEFAULT_BEHAVIOURS)

            return result
        }

        /**
         * @brief create a behaviour from the configuration
         * @param configuration configuration
         * @return the created behaviour
         * @throws Exception on error
         */
        fun createFromConfiguration(configuration: Map<*,*>): AppAgentBehaviour {
            ApplicationConfig.LOGGER.info("Creating agent behaviour <${this::class}> from configuration")

            if(!Exportable.containKeys(configuration = configuration, ExportKeys.CLASS, ExportKeys.SPECIFIC_CONFIGURATION,
                    ExportKeys.EXTERNAL_CONFIGURATION))
                throw Exception("The provided configuration doesn't contain all expected keys")

            val behaviourClass = configuration[ExportKeys.CLASS] as String

            val foundedClass = this.AVAILABLE_AGENT_BEHAVIOURS_CLASSES.first{it.canonicalName == behaviourClass}
            val instance = foundedClass.getConstructor().newInstance() as AppAgentBehaviour

            instance.configuration = configuration[ExportKeys.EXTERNAL_CONFIGURATION] as Map<*,*>

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
        ExportKeys.SPECIFIC_CONFIGURATION to this.buildConfiguration(),
        ExportKeys.EXTERNAL_CONFIGURATION to this.configuration
    )

    override fun receiveConfiguration(configuration: Map<String, String>) {
        this.configuration = configuration
    }

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

        /**
         * @brief external configuration for configurable
         */
        const val EXTERNAL_CONFIGURATION:String = "externalConfiguration"
    }
}