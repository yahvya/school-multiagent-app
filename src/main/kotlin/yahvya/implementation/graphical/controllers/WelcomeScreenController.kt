package yahvya.implementation.graphical.controllers

import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.input.MouseEvent
import javafx.stage.FileChooser
import javafx.stage.StageStyle
import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.configurations.ScreensConfig
import yahvya.implementation.multiagent.simulation.SimulationStorage
import java.awt.Desktop
import java.io.FileInputStream
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
        val fileChooser = FileChooser()

        fileChooser.title = "Choisir le fichier de configuration"
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Simulation",WelcomeScreenController.CONFIGURATION_FILE_EXTENSION))

        val chosenFile = fileChooser.showOpenDialog(this.navigationManager.mainStage)

        if(chosenFile === null)
            return

        try{
            FileInputStream(chosenFile).use{ inputStream ->
                val loadedSimulation = SimulationStorage.loadFrom(inputStream = inputStream)
                this.navigationManager.switchOnController(
                    fxmlPath = ScreensConfig.NEW_CONFIGURATION_SCREEN,
                    datas = loadedSimulation.configuration
                )
            }
        }
        catch (e: Exception){
            val alert = Alert(Alert.AlertType.ERROR)

            alert.apply {
                title = "Erreur"
                headerText = "Erreur d'ouverture"
                contentText = "Une erreur s'est produite à l'ouverture <${e.message}>"

                show()
            }
        }
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