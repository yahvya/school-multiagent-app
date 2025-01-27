package yahvya.implementation

import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import mu.KotlinLogging
import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.configurations.ScreensConfig
import yahvya.implementation.graphical.navigation.DefaultInterfaceConfigurator
import yahvya.implementation.graphical.navigation.InterfaceConfigurator
import yahvya.implementation.graphical.navigation.NavigationManager

/**
 * @brief application
 */
open class MultiAgentApplication : Application(), InterfaceConfigurator by DefaultInterfaceConfigurator() {
    companion object{
        /**
         * @brief restart the application
         * @param onCloseFinished action on close finished
         * @param onCloseError action on close error
         * @throws Nothing
         */
        fun restartApp(onCloseFinished: () -> Unit = {},onCloseError: (Exception) -> Unit = {}){
            Platform.runLater{
                try{
                    ApplicationConfig.NAVIGATION_MANAGER.mainStage.close()
                    onCloseFinished()
                    MultiAgentApplication().start(Stage())
                }
                catch (e:Exception){
                    onCloseError(e)
                }
            }
        }
    }

    override fun start(stage: Stage?) {
        if(stage == null) {
            println("Fail to launch app")
            return
        }

        this.configureApplication(mainStage = stage)
        ApplicationConfig.NAVIGATION_MANAGER.switchOnController(fxmlPath = ScreensConfig.WELCOME_SCREEN)
    }

    /**
     * @brief configure the application
     * @param mainStage application main Stage
     * @return this
     * @throws Nothing
     */
    protected fun configureApplication(mainStage: Stage): MultiAgentApplication{
        ApplicationConfig.init(
            rootClass = javaClass,
            navigationManager = NavigationManager(
                mainStage = mainStage,
                interfaceConfigurator = this
            ),
            pluginsParentDirectory = "plugins",
            logger =  KotlinLogging.logger {},
            applicationName = "Simulation",
            authorGithubLink = "https://github.com/yahvya"
        )

        return this
    }
}

fun main() = Application.launch(MultiAgentApplication::class.java)