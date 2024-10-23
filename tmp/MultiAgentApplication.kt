package yahvya.implementation.tmp

import javafx.application.Application
import javafx.scene.image.Image
import javafx.stage.Stage
import mu.KotlinLogging
import yahvya.implementation.tmp.appinterface.navigation.InterfaceConfigurator
import yahvya.implementation.tmp.appinterface.navigation.NavigationManager
import yahvya.implementation.tmp.pathconfig.PathConfig

/**
 * @brief multi agent application
 */
class MultiAgentApplication : Application(), InterfaceConfigurator {
    companion object{
        /**
         * @brief application logger
         */
        val logger = KotlinLogging.logger {}
    }

    /**
     * @brief application path config
     */
    val applicationPathConfig: PathConfig = PathConfig()

    /**
     * @brief application navigation manager
     */
    lateinit var navigationManager: NavigationManager

    override fun start(mainStage: Stage?) {
        if(mainStage === null)
            return

        this.navigationManager = NavigationManager(
            pathConfig = this.applicationPathConfig,
            mainStage = mainStage,
            interfaceConfigurator = this,
            rootClass = javaClass
        )
    }

    override fun configureInterface(stage: Stage) {
        logger.info("Configuring interface")

        // set the icon
        try{
            stage.icons.clear()
            stage.icons.add(Image(javaClass.getResourceAsStream("${this.applicationPathConfig.iconsBasePath}/icon.png")))

            logger.info("Loading application icon succeed")
        }
        catch(_: Exception){
            logger.error("Fail to load application icon")
        }

        // add global stylesheet
        try{
            val globalStylesheetPath = javaClass.getResource("${this.applicationPathConfig.stylesheets}/app.css")?.toExternalForm()

            if(globalStylesheetPath === null)
                throw Exception()

            if(!stage.scene.stylesheets.contains(element = globalStylesheetPath))
                stage.scene.stylesheets.add(element = globalStylesheetPath)
        }
        catch(_: Exception){
            logger.error("Fail to load global stylesheet")
        }
    }
}

fun main() = Application.launch(MultiAgentApplication::class.java)