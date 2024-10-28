package yahvya.implementation.graphical.controllers

import javafx.fxml.FXML
import javafx.scene.layout.HBox
import yahvya.implementation.graphical.navigation.DefaultNavigationBarManager
import yahvya.implementation.graphical.navigation.NavigationBarManager

/**
 * @brief controller with navbar
 */
abstract class ApplicationNavbarController:
    ApplicationController(),
    NavigationBarManager by DefaultNavigationBarManager()
{
        @FXML
        protected lateinit var navigationElements: HBox
}