package yahvya.implementation.graphical.navigation

import javafx.scene.image.Image
import javafx.stage.Stage
import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.configurations.PathsConfig

/**
 * @brief application default interface configuration
 */
class DefaultInterfaceConfigurator : InterfaceConfigurator {
    override fun configureInterface(stage: Stage) {
        ApplicationConfig.LOGGER.info("Configuring interface")

        // set the icon
        try{
            stage.icons.clear()
            stage.icons.add(Image(javaClass.getResourceAsStream(PathsConfig.APPLICATION_ICON_PATH)))

            ApplicationConfig.LOGGER.info("Loading application icon succeed")
        }
        catch(_: Exception){
            ApplicationConfig.LOGGER.error("Fail to load application icon")
        }

        // add global stylesheet
        try{
            val globalStylesheetPath = javaClass.getResource("${PathsConfig.STYLESHEETS_DIRECTORY_PATH}/app.css")?.toExternalForm()

            if(globalStylesheetPath === null)
                throw Exception()

            if(!stage.scene.stylesheets.contains(element = globalStylesheetPath))
                stage.scene.stylesheets.add(element = globalStylesheetPath)
        }
        catch(_: Exception){
            ApplicationConfig.LOGGER.error("Fail to load global stylesheet")
        }
    }
}