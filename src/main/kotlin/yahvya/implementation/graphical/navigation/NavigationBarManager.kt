package yahvya.implementation.graphical.navigation

import javafx.fxml.FXML

/**
 * @brief screen with navbar manager
 */
interface NavigationBarManager {
    @FXML
    fun closeApplication()

    @FXML
    fun loadConfiguration()

    @FXML
    fun createNewConfiguration()
}