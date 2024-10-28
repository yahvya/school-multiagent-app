package yahvya.implementation.multiagent.simulation

import yahvya.implementation.graphical.painter.Painter
import yahvya.implementation.multiagent.agent.AppAgentBehaviour
import yahvya.implementation.multiagent.definitions.Box
import yahvya.implementation.multiagent.definitions.Exportable
import yahvya.implementation.multiagent.environment.Environment
import java.net.ServerSocket

/**
 * @brief simulation configuration object
 */
open class SimulationConfiguration(
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
    var agentsInitialConfig: MutableList<AgentInitialConfig> = mutableListOf(),
    /**
     * @brief show jade gui
     */
    var showGui: Boolean = false,
) {
    /**
     * @brief host
     */
    var host:String = "127.0.0.1"

    /**
     * @brief port
     */
    var port: String = "no_port"

    /**
     * @brief function to call of the end
     */
    var toDoOnEnd: ((simulation: Simulation) -> Unit)? = null

    /**
     * @brief method to draw an agent
     */
    var painter: Painter? = null

    /**
     * @return the port to use
     */
    fun buildPort():String{
        if(this.port == "no_port"){
            // load a free port
            ServerSocket(0).use{
                return it.localPort.toString()
            }
        }

        return this.port
    }

    /**
     * @brief agent initial configs
     */
    open class AgentInitialConfig : Exportable{
        companion object{
            /**
             * @brief create an instance from configuration
             * @param configuration configuration
             * @return instance
             * @throws Exception on error
             */
            fun createFromConfiguration(configuration: Map<*,*>):AgentInitialConfig{
                val initialConfig = AgentInitialConfig()

                if(!initialConfig.loadFromExportedConfig(configuration = configuration))
                    throw Exception("Fail to load initial config from exported config")

                return initialConfig
            }
        }

        /**
         * @brief agent box
         */
        lateinit var box:Box

        /**
         * @brief agent default behaviours
         */
        lateinit var behaviours: List<AppAgentBehaviour>

        constructor()

        constructor(box: Box,behaviours: List<AppAgentBehaviour>){
            this.box = box
            this.behaviours = behaviours
        }

        override fun exportConfig(): Map<*, *> = mapOf(
            "box" to box.exportConfig(),
            "behaviours" to behaviours.map{it.exportConfig()}
        )

        override fun loadFromExportedConfig(configuration: Map<*, *>): Boolean {
            if(!Exportable.containKeys(configuration= configuration,"box","behaviours"))
                return false

            this.box = Box.createFromConfiguration(configuration = configuration["box"] as Map<*,*>)
            this.behaviours = (configuration["behaviours"] as List<*>).map{
                AppAgentBehaviour.createFromConfiguration(configuration = it as Map<*, *>)
            }

            return true
        }
    }
}