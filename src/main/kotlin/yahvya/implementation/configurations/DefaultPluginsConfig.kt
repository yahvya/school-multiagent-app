package yahvya.implementation.configurations

import yahvya.implementation.multiagent.agent.AppAgentBehaviour
import yahvya.implementation.multiagent.definitions.Box
import yahvya.implementation.multiagent.environment.EnvironmentCell

/**
 * @brief default plugins
 */
data object DefaultPluginsConfig{
    /**
     * @val default boxes
     */
    val DEFAULT_BOXES: MutableList<Class<out Box>> = mutableListOf()

    /**
     * @val default cells
     */
    val DEFAULT_CELLS: MutableList<Class<out EnvironmentCell>> = mutableListOf()

    /**
     * @val default behaviours
     */
    val DEFAULT_BEHAVIOURS: MutableList<Class<out AppAgentBehaviour>> = mutableListOf()
}