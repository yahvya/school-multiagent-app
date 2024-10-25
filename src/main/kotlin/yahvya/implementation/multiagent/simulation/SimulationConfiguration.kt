package yahvya.implementation.multiagent.simulation

import yahvya.implementation.multiagent.agent.AppAgent
import yahvya.implementation.multiagent.agent.AppAgentBehaviour
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
    var agentsInitialConfig: MutableList<List<Class<out AppAgentBehaviour>>> = mutableListOf(),
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
    var toDoOnEnd: ((Simulation) -> Unit)? = null

    /**
     * @brief method to draw an agent
     */
    var drawAgent: ((AppAgent) -> Unit)? = null

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
}