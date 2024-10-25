package yahvya.implementation.graphical.navigation

import javafx.stage.Stage

/**
 * @brief describe a stage configurator
 */
interface InterfaceConfigurator {
    /**
     * @brief configure the provided stage
     */
    fun configureInterface(stage: Stage)

    /**
     * @brief add the global stylesheets to the stage
     * @param stage stage
     */
    fun addGlobalStylesheets(stage: Stage)

    /**
     * @return get the global stylesheets
     */
    fun getGlobalStylesheets(): List<String>
}