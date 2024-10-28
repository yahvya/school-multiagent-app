package yahvya.implementation.graphical.functionnalities

import javafx.scene.control.Alert
import javafx.stage.FileChooser
import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.configurations.ScreensConfig
import yahvya.implementation.graphical.controllers.WelcomeScreenController.Companion.CONFIGURATION_FILE_EXTENSION
import yahvya.implementation.multiagent.simulation.SimulationStorage
import java.io.FileInputStream

/**
 * @brief try to load a configuration file
 */
fun loadSimulationConfiguration(){
    val fileChooser = FileChooser()

    fileChooser.title = "Choisir le fichier de configuration"
    fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Simulation", CONFIGURATION_FILE_EXTENSION))

    val chosenFile = fileChooser.showOpenDialog(ApplicationConfig.NAVIGATION_MANAGER.mainStage)

    if(chosenFile === null)
        return

    try{
        FileInputStream(chosenFile).use{ inputStream ->
            val loadedSimulation = SimulationStorage.loadFrom(inputStream = inputStream)
            ApplicationConfig.NAVIGATION_MANAGER.switchOnController(
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