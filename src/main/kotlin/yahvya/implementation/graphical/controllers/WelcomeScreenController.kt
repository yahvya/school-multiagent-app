package yahvya.implementation.graphical.controllers

import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.stage.FileChooser
import javafx.stage.StageStyle
import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.configurations.PathsConfig
import yahvya.implementation.configurations.ScreensConfig
import yahvya.implementation.multiagent.simulation.SimulationStorage
import java.awt.Desktop
import java.io.File
import java.io.FileInputStream
import java.net.URI
import java.util.Date

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
    fun createNewConfiguration() {
        this.navigationManager.switchOnController(fxmlPath = ScreensConfig.NEW_CONFIGURATION_SCREEN)
    }

    @FXML
    fun loadConfiguration() {
        val fileChooser = FileChooser()

        fileChooser.title = "Choisir le fichier de configuration"
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Simulation",CONFIGURATION_FILE_EXTENSION))

        val chosenFile = fileChooser.showOpenDialog(this.navigationManager.mainStage)

        if(chosenFile === null)
            return

        try{
            FileInputStream(chosenFile).use{ inputStream ->
                val loadedSimulation = SimulationStorage.loadFrom(inputStream = inputStream)
                this.navigationManager.switchOnController(
                    fxmlPath = ScreensConfig.SIMULATION_SCREEN,
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
        this.navigationManager.mainStage.close()
    }

    @FXML
    fun addPlugin() {
        val alert = Alert(Alert.AlertType.WARNING).apply {
            title = "Conseils"
            headerText = "Ajout d'un plugin"
            contentText = "Pensez à n'ajouter que des plugins vérifiés, sans contenu malicieux à défaut de corrompre l'application."
        }

        alert.showAndWait()

        val fileChooser = FileChooser().apply{
            title = "Choisir le fichier JAR"
            extensionFilters.add(FileChooser.ExtensionFilter("Simulation","*.jar"))
        }

        try{
            ApplicationConfig.LOGGER.info("Adding plugin")

            val jarFile = fileChooser.showOpenDialog(this.navigationManager.mainStage)

            jarFile?.let {
                ApplicationConfig.ROOT_CLASS.getResource(PathsConfig.PLUGINS_DIRECTORY_PATH)?.path?.let { path ->
                    it.copyTo(File("$path/${Date().time.toInt()}.jar"))

                    ApplicationConfig.LOGGER.info("Plugin added")

                    Alert(Alert.AlertType.INFORMATION).apply {
                        title = "Confirmation"
                        headerText = "Le plugin a bien été ajouté"
                        contentText = "Le plugin a bien été ajouté. Pour charger les modifications, veuillez relancer l'application."

                        show()
                    }
                }
            }
        }
        catch (_: Exception){}
    }
}