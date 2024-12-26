package yahvya.implementation.graphical.functionnalities

import javafx.scene.control.Alert
import javafx.stage.FileChooser
import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.configurations.PathsConfig
import java.io.File
import java.util.*

/**
 * @brief add a new plugin
 */
fun addNewPlugin(){
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

        val jarFile = fileChooser.showOpenDialog(ApplicationConfig.NAVIGATION_MANAGER.mainStage)

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