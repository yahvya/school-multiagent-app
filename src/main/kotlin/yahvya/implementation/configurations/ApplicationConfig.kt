package yahvya.implementation.configurations

import javafx.application.Application
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
     * @brief application name
     */
    lateinit var APPLICATION_NAME:String

    /**
     * @brief author GitHub link
     */
    lateinit var AUTHOR_GITHUB_LINK:String

    /**
     * @brief initialize the application configuration
     * @param rootClass application root class
     * @param navigationManager application navigation manager
     * @param pluginsParentDirectory plugins parent directory in the JAR
     * @param logger logger
     * @param applicationName application name
     * @param authorGithubLink github link
     * @throws Nothing
     */
    fun init(
        rootClass: Class<Application>,
        navigationManager: NavigationManager,
        pluginsParentDirectory: String,
        logger: mu.KLogger,
        applicationName: String,
        authorGithubLink: String
    ){
        ApplicationConfig.apply{
            ROOT_CLASS = rootClass
            NAVIGATION_MANAGER = navigationManager
            PLUGINS_PARENT_DIRECTORY = pluginsParentDirectory
            LOGGER = logger
            APPLICATION_NAME = applicationName
            AUTHOR_GITHUB_LINK = authorGithubLink
        }
    }
}