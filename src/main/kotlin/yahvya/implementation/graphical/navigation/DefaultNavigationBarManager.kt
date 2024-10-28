package yahvya.implementation.graphical.navigation

import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.configurations.ScreensConfig
import yahvya.implementation.graphical.functionnalities.loadSimulationConfiguration

/**
 * @brief application default navigation bar manager
 */
class DefaultNavigationBarManager : NavigationBarManager {
    override fun closeApplication() = ApplicationConfig.NAVIGATION_MANAGER.mainStage.close()

    override fun loadConfiguration() = loadSimulationConfiguration()

    override fun createNewConfiguration(){
        ApplicationConfig.NAVIGATION_MANAGER.switchOnController(fxmlPath = ScreensConfig.CONFIGURATION_SCREEN)
    }
}