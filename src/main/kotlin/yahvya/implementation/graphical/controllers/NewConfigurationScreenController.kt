package yahvya.implementation.graphical.controllers

import javafx.fxml.FXML
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.stage.StageStyle
import yahvya.implementation.configurations.ScreensConfig
import yahvya.implementation.multiagent.simulation.SimulationConfiguration


/**
 * @brief new configuration screen controller
 */
open class NewConfigurationScreenController : ApplicationController(){
    @FXML
    private lateinit var environmentName: TextField

    @FXML
    private lateinit var simulationName: TextField

    @FXML
    private lateinit var configurationZone: VBox

    @FXML
    private lateinit var recapZone: VBox

    /**
     * @brief simulation configuration
     */
    protected lateinit var simulationConfiguration: SimulationConfiguration

    override fun storeCurrentVersionOnSwitch(): Boolean = true

    override fun showAfter(): Boolean = true

    override fun getPageName(): String = "Nouvelle configuration"

    override fun performActions() {
        // configure simulation

        this.simulationConfiguration = if(this.datas !== null) this.datas as SimulationConfiguration else SimulationConfiguration(name= "")

        // configure stage
        val mainStage = this.navigationManager.mainStage

        mainStage.initStyle(StageStyle.UNDECORATED)
        mainStage.isMaximized = true

        this.buildInterfaceFromSimulationConfig()
    }

    /**
     * @brief build the interface from the provided configuration
     */
    protected open fun buildInterfaceFromSimulationConfig(){

    }

    @FXML
    fun closeApplication(event: MouseEvent?) {
        this.navigationManager.mainStage.close()
    }

    @FXML
    fun addAgent(event: MouseEvent) {

    }

    @FXML
    fun addEnvironmentCell(event: MouseEvent) {

    }

    @FXML
    fun downloadConfiguration(event: MouseEvent) {

    }

    @FXML
    fun goBackToHome(event: MouseEvent) {
        this.navigationManager.switchOnController(fxmlPath = ScreensConfig.WELCOME_SCREEN)
    }
}