package yahvya.implementation.graphical.controllers

import javafx.fxml.FXML
import javafx.stage.StageStyle
import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.configurations.ScreensConfig
import yahvya.implementation.graphical.functionnalities.addNewPlugin
import yahvya.implementation.graphical.functionnalities.loadSimulationConfiguration
import java.awt.Desktop
import java.net.URI

/**
 * @brief application welcome screen
 */
open class WelcomeScreenController : ApplicationController() {
    companion object{
        /**
         * @brief configuration file extension
         */
        const val CONFIGURATION_FILE_EXTENSION:String = "*.goat"
    }

    override fun storeCurrentVersionOnSwitch(): Boolean = false

    override fun showAfter(): Boolean = true

    override fun getPageName(): String = "Accueil"

    override fun performActions(){
        val stage = ApplicationConfig.NAVIGATION_MANAGER.mainStage

        stage.initStyle(StageStyle.UNDECORATED)
        stage.centerOnScreen()
    }

    @FXML
    fun createNewConfiguration() = ApplicationConfig.NAVIGATION_MANAGER.switchOnController(fxmlPath = ScreensConfig.CONFIGURATION_SCREEN)

    @FXML
    fun loadConfiguration() = loadSimulationConfiguration()

    @FXML
    fun openAuthorGithub() {
        try{
            ApplicationConfig.LOGGER.info("Opening author link")
            Desktop.getDesktop().browse(URI(ApplicationConfig.AUTHOR_GITHUB_LINK))
        }
        catch (e: Exception){
            ApplicationConfig.LOGGER.error("Fail to open author link ${e.message}")
        }
    }

    @FXML
    fun closeApplication() {
        ApplicationConfig.NAVIGATION_MANAGER.mainStage.close()
    }

    @FXML
    fun addPlugin() = addNewPlugin()
}