package yahvya.implementation.multiagent.simulation

import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.multiagent.agent.AppAgentBehaviour
import yahvya.implementation.multiagent.environment.Environment
import yahvya.implementation.multiagent.interfaces.Exportable

/**
 * @brief application simulation
 */
open class Simulation(
    /**
     * @brief simulation name
     */
    var name: String,
    /**
     * @brief simulation environment
     */
    var environment: Environment = Environment(),
    /**
     * @brief list of agents default behaviours
     */
    var agentsInitialConfig: MutableList<List<Class<out AppAgentBehaviour>>> = mutableListOf()
) : Exportable {
    /**
     * @brief environment last state save config
     */
    protected lateinit var environmentConfig: Map<*,*>

    companion object{
        /**
         * @brief create a simulation from the configuration
         * @param configuration the configuration
         * @return the created simulation
         * @throws Exception on error
         */
        fun createFromConfiguration(configuration: Map<*,*>): Simulation{
            ApplicationConfig.LOGGER.info("Creating simulation from exported configuration")

            val simulation = Simulation(name = "tmp")

            if(!simulation.loadFromExportedConfig(configuration = configuration))
                throw Exception("Fail to load simulation from exported configuration")

            return simulation
        }
    }

    init{
        this.saveState()
    }

    /**
     * @brief save the current state of the simulation for the exportation
     * @attention agents state isn't saved, only the initial configuration
     * @throws Nothing
     */
    fun saveState(){
        ApplicationConfig.LOGGER.info("Saving simulation $this state")
        this.environmentConfig = this.environment.exportConfig()
    }

    override fun exportConfig(): Map<*, *> = mapOf<String,Any>(
        ExportKeys.NAME to this.name,
        ExportKeys.ENVIRONMENT to this.environmentConfig,
        ExportKeys.AGENTS_INITIAL_CONFIGURATION to this.agentsInitialConfig.map{
            listOfBehavioursClass -> listOfBehavioursClass.map{it.canonicalName}
        }
    )

    override fun loadFromExportedConfig(configuration: Map<*, *>): Boolean {
        ApplicationConfig.LOGGER.info("Loading simulation <$this> from exported configuration")
        
        if(!Exportable.containKeys(configuration= configuration, ExportKeys.ENVIRONMENT, ExportKeys.NAME, ExportKeys.AGENTS_INITIAL_CONFIGURATION))
            return false

        try{
            val agentsInitialConfigSave = configuration[ExportKeys.AGENTS_INITIAL_CONFIGURATION] as List<*>
            this.name = configuration[ExportKeys.NAME] as String
            this.environment = Environment.createFromConfiguration(configuration = configuration[ExportKeys.ENVIRONMENT] as Map<*,*>)
            this.agentsInitialConfig = agentsInitialConfigSave.map{ listOfClassCanonicalNames ->
                (listOfClassCanonicalNames as List<*>).map{ canonicalName ->
                    AppAgentBehaviour.AVAILABLE_AGENT_BEHAVIOURS_CLASSES.first { it.canonicalName == canonicalName }
                }
            }.toMutableList()

            this.saveState()

            return true
        }
        catch(e:Exception){
            ApplicationConfig.LOGGER.error("Fail to load simulation <$this> from configuration due to <${e.message}>")
            return false
        }
    }

    /**
     * @brief export keys
     */
    data object ExportKeys{
        /**
         * @brief simulation name
         */
        const val NAME: String = "name"

        /**
         * @brief environment configuration
         */
        const val ENVIRONMENT:String = "environment"

        /**
         * @brief agents initial configuration
         */
        const val AGENTS_INITIAL_CONFIGURATION = "agentsInitialConfig"
    }
}