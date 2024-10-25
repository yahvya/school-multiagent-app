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
            if(stage.icons.size == 0){
                stage.icons.add(Image(ApplicationConfig.ROOT_CLASS.getResourceAsStream(PathsConfig.APPLICATION_ICON_PATH)))
                ApplicationConfig.LOGGER.info("Loading application icon succeed")
            }
        }
        catch(_: Exception){
            ApplicationConfig.LOGGER.error("Fail to load application icon")
        }
    }

    override fun addGlobalStylesheets(stage: Stage) {
        try{
            this.getGlobalStylesheets().forEach{stylesheetPath ->
                if(!stage.scene.stylesheets.contains(element = stylesheetPath))
                    stage.scene.stylesheets.add(element = stylesheetPath)
            }
        }
        catch(e: Exception){
            ApplicationConfig.LOGGER.error("Fail to load one global stylesheet due to ${e.message}")
        }
    }

    override fun getGlobalStylesheets(): List<String> {
        try{
            val globalStylesheets = arrayOf("app.css")

            return globalStylesheets.mapNotNull { stylesheetPathFormBaseDir ->
                ApplicationConfig.ROOT_CLASS.getResource("${PathsConfig.STYLESHEETS_DIRECTORY_PATH}/$stylesheetPathFormBaseDir")
                    ?.toExternalForm()
            }
        }
        catch(e: Exception){
            ApplicationConfig.LOGGER.error("Fail to load any stylesheet due to ${e.message}")
            return listOf()
        }
    }
}