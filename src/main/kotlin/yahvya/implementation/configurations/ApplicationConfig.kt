package yahvya.implementation.configurations

import javafx.application.Application
import mu.KotlinLogging
import yahvya.implementation.graphical.navigation.NavigationManager

/**
 *  @brief application config
 */
data object ApplicationConfig {
    /**
     * @brief application root class
     */
    lateinit var ROOT_CLASS: Class<Application>

    /**
     * @brief application navigation manager
     */
    lateinit var NAVIGATION_MANAGER: NavigationManager

    /**
     * @brief plugins parent directory in the JAR
     */
    lateinit var PLUGINS_PARENT_DIRECTORY: String

    /**
     * @brief logger
     */
    lateinit var LOGGER: mu.KLogger

    /**
     * @brief initialize the application configuration
     * @param rootClass application root class
     * @param navigationManager application navigation manager
     * @param pluginsParentDirectory plugins parent directory in the JAR
     * @param logger logger
     * @throws Nothing
     */
    fun init(
        rootClass: Class<Application>,
        navigationManager: NavigationManager,
        pluginsParentDirectory: String,
        logger: mu.KLogger
    ){
        ApplicationConfig.apply{
            ROOT_CLASS = rootClass
            NAVIGATION_MANAGER = navigationManager
            PLUGINS_PARENT_DIRECTORY = pluginsParentDirectory
            LOGGER = logger
        }
    }
}