package yahvya.implementation.graphical.controllers

import javafx.fxml.FXML
import javafx.scene.input.MouseEvent


/**
 * @brief new configuration screen controller
 */
class NewConfigurationScreenController : ApplicationController(){
    override fun storeCurrentVersionOnSwitch(): Boolean = true

    override fun showAfter(): Boolean = true

    override fun getPageName(): String = "Nouvelle configuration"

    override fun performActions() {
        val mainStage = this.navigationManager.mainStage

        this.navigationManager.performStageAction {
            mainStage.isMaximized = true
        }
    }

    @FXML
    fun closeApplication(event: MouseEvent?) {
        this.navigationManager.mainStage.close()
    }
}