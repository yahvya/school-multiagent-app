package yahvya.implementation

import javafx.application.Application
import javafx.stage.Stage
import mu.KotlinLogging
import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.graphical.navigation.NavigationManager

/**
 * @brief application
 */
open class MultiAgentApplication : Application() {
    override fun start(stage: Stage?) {
        this.configureApplication()
    }

    /**
     * @brief configure the application
     * @return this
     * @throws Nothing
     */
    protected fun configureApplication(): MultiAgentApplication{
        // configure application
        ApplicationConfig.init(
            rootClass = javaClass,
            navigationManager = NavigationManager(),
            pluginsParentDirectory = "plugins",
            logger =  KotlinLogging.logger {},
        )

        return this
    }
}

fun main() = Application.launch(MultiAgentApplication::class.java)