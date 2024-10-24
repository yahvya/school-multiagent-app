package yahvya.implementation.multiagent.simulation

import jade.core.Profile
import jade.core.ProfileImpl
import jade.core.Runtime
import jade.wrapper.AgentContainer
import jade.wrapper.AgentController
import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.multiagent.agent.AppAgent
import yahvya.implementation.multiagent.agent.AppAgentBehaviour
import yahvya.implementation.multiagent.environment.Environment
import yahvya.implementation.multiagent.interfaces.Exportable
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * @brief application simulation
 */
open class Simulation(
    /**
     * @brief simulation configuration
     */
    val configuration : SimulationConfiguration
) : Exportable, Runnable {
    /**
     * @brief environment last state save config
     */
    protected lateinit var environmentConfig: Map<*,*>

    /**
     * @brief jade execution container
     */
    lateinit var mainContainer: AgentContainer

    /**
     * @brief simulation stop state
     */
    lateinit var stop: AtomicBoolean

    /**
     * @brief agents index
     */
    lateinit var agentsIndex: AtomicInteger

    companion object{
        /**
         * @brief create a simulation from the configuration
         * @param configuration the configuration
         * @return the created simulation
         * @throws Exception on error
         */
        fun createFromConfiguration(configuration: Map<*,*>): Simulation{
            ApplicationConfig.LOGGER.info("Creating simulation from exported configuration")

            val simulation = Simulation(configuration = SimulationConfiguration(name = "tmp"))

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
        ApplicationConfig.LOGGER.info("Saving simulation ${this.configuration.name} state")
        this.environmentConfig = this.configuration.environment.exportConfig()
    }

    /**
     * @brief add a new agent to the main container
     * @param defaultBehaviours agent default behaviours
     * @return the created controller
     * @throws Exception on error
     */
    fun addAgentWithDefaultBehaviours(defaultBehaviours: List<Class<out AppAgentBehaviour>>): AgentController{
        return this.mainContainer.createNewAgent(
            this.agentsIndex.incrementAndGet().toString(),
            AppAgent::class.java.name,
            arrayOf(
                this,
                defaultBehaviours
            )
        )
    }

    override fun exportConfig(): Map<*, *> = mapOf<String,Any>(
        ExportKeys.NAME to this.configuration.name,
        ExportKeys.ENVIRONMENT to this.environmentConfig,
        ExportKeys.HOST to this.configuration.host,
        ExportKeys.PORT to this.configuration.port,
        ExportKeys.AGENTS_INITIAL_CONFIGURATION to this.configuration.agentsInitialConfig.map{
            listOfBehavioursClass -> listOfBehavioursClass.map{it.canonicalName}
        }
    )

    override fun loadFromExportedConfig(configuration: Map<*, *>): Boolean {
        ApplicationConfig.LOGGER.info("Loading simulation <${this.configuration.name}> from exported configuration")
        
        if(!Exportable.containKeys(configuration= configuration, ExportKeys.ENVIRONMENT, ExportKeys.NAME, ExportKeys.AGENTS_INITIAL_CONFIGURATION))
            return false

        try{
            val agentsInitialConfigSave = configuration[ExportKeys.AGENTS_INITIAL_CONFIGURATION] as List<*>
            this.configuration.name = configuration[ExportKeys.NAME] as String
            this.configuration.environment = Environment.createFromConfiguration(configuration = configuration[ExportKeys.ENVIRONMENT] as Map<*,*>)
            this.configuration.port = configuration[ExportKeys.PORT] as String
            this.configuration.host = configuration[ExportKeys.HOST] as String
            this.configuration.agentsInitialConfig = agentsInitialConfigSave.map{ listOfClassCanonicalNames ->
                (listOfClassCanonicalNames as List<*>).map{ canonicalName ->
                    AppAgentBehaviour.AVAILABLE_AGENT_BEHAVIOURS_CLASSES.first { it.canonicalName == canonicalName }
                }
            }.toMutableList()

            this.saveState()

            return true
        }
        catch(e:Exception){
            ApplicationConfig.LOGGER.error("Fail to load simulation <${this.configuration.name}> from configuration due to <${e.message}>")
            return false
        }
    }

    override fun run(){
        ApplicationConfig.LOGGER.info("Launching simulation <${this.configuration.name}>")

        try{
            // setup jade

            val jadeRuntime = Runtime.instance()
            val jadeProfile = ProfileImpl()

            jadeProfile.setParameter(Profile.MAIN_HOST,this.configuration.host)
            jadeProfile.setParameter(Profile.MAIN_PORT,this.configuration.buildPort())
            jadeProfile.setParameter(Profile.GUI,this.configuration.showGui.toString())

            this.mainContainer = jadeRuntime.createMainContainer(jadeProfile).also{
                // listen to the end event
                it.addPlatformListener(CustomListener(simulation = this))
            }
            this.stop = AtomicBoolean(false)
            this.agentsIndex = AtomicInteger(0)

            // creating agents
            this.configuration.agentsInitialConfig.forEach { agentInitialBehaviours ->
                val agentContainer = this.addAgentWithDefaultBehaviours(defaultBehaviours = agentInitialBehaviours)

                agentContainer.start()
            }

            while(!this.stop.get()){
                Thread.sleep(1000)
                this.configuration.environment.update()
            }

            this.manageEnd()
        }
        catch(e: Exception){
            ApplicationConfig.LOGGER.error("Fail to launch simulation <${this.configuration.name}> due to <${e.message}>")
        }
    }

    /**
     * @brief manage the end of the simulation
     */
    protected fun manageEnd(){
        this.configuration.toDoOnEnd?.invoke(this)
        this.stop.set(false)
        this.agentsIndex.set(0)
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
        const val AGENTS_INITIAL_CONFIGURATION:String = "agentsInitialConfig"

        /**
         * @brief host
         */
        const val HOST:String = "host"

        /**
         * @brief port
         */
        const val PORT:String = "port"
    }
}