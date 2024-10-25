package yahvya.implementation.graphical.controllers

import javafx.fxml.FXML
import javafx.scene.input.MouseEvent
import javafx.stage.StageStyle
import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.configurations.ScreensConfig
import java.awt.Desktop
import java.net.URI

/**
 * @brief application welcome screen
 */
class WelcomeScreenController : ApplicationController() {
    override fun storeCurrentVersionOnSwitch(): Boolean = false

    override fun showAfter(): Boolean = true

    override fun getPageName(): String = "Accueil"

    override fun performActions(){
        val stage = this.navigationManager.mainStage

        stage.initStyle(StageStyle.UNDECORATED)
        stage.centerOnScreen()
    }

    @FXML
    fun createNewConfiguration(event: MouseEvent) {
        this.navigationManager.switchOnController(fxmlPath = ScreensConfig.NEW_CONFIGURATION_SCREEN)
    }

    @FXML
    fun loadConfiguration(event: MouseEvent) {

    }

    @FXML
    fun openAuthorGithub(event: MouseEvent) {
        try{
            ApplicationConfig.LOGGER.info("Opening author link")
            Desktop.getDesktop().browse(URI(ApplicationConfig.AUTHOR_GITHUB_LINK))
        }
        catch (e: Exception){
            ApplicationConfig.LOGGER.error("Fail to open author link ${e.message}")
        }
    }

    @FXML
    fun closeApplication(event: MouseEvent?) {
        this.navigationManager.mainStage.close()
    }
}