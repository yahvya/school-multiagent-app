package yahvya.implementation.graphical.controllers

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.StageStyle
import yahvya.implementation.configurations.ScreensConfig
import yahvya.implementation.graphical.painter.Painter
import yahvya.implementation.multiagent.simulation.Simulation
import yahvya.implementation.multiagent.simulation.SimulationConfiguration

/**
 * @brief simulation screen
 */
open class SimulationScreenController : ApplicationController(), Painter{
    @FXML
    private lateinit var recapZone: HBox

    @FXML
    private lateinit var simulationNameLabel: Label

    @FXML
    private lateinit var simulationZone: AnchorPane

    @FXML
    private lateinit var simulationStateButton: Button

    @FXML
    private lateinit var page: VBox

    /**
     * @brief simulation configuration
     */
    protected lateinit var simulationConfiguration: SimulationConfiguration

    /**
     * @brief current simulation
     */
    protected lateinit var currentSimulation: Simulation

    /**
     * @brief simulation thread
     */
    protected var simulationThread: Thread? = null

    override fun storeCurrentVersionOnSwitch(): Boolean = false

    override fun showAfter(): Boolean = true

    override fun getPageName(): String = "Simulation"

    override fun performActions() {
        // configure simulation

        if(this.datas !== null)
            this.simulationConfiguration = this.datas as SimulationConfiguration

        // configure stage
        val mainStage = this.navigationManager.mainStage

        mainStage.initStyle(StageStyle.UNDECORATED)
        mainStage.isMaximized = true

        // configure default elements
        this.simulationNameLabel.text = if(this::simulationConfiguration.isInitialized) this.simulationConfiguration.name.uppercase() else "Echec de chargement de la configuration"
    }

    override fun beforeSwitch() {
        try{
            if(this.simulationThread !== null){
                this.currentSimulation.stop.set(true)
                this.simulationThread = null
            }
        }
        catch(_:Exception){}
    }

    override fun applyOnCanvas(toApply: (drawZone: AnchorPane) -> Unit) = Platform.runLater{
        toApply.invoke(this.simulationZone)
    }

    /**
     * @brief show an error message in alert
     * @param errorMessage error message
     * @throws Nothing
     */
    protected fun showErrorMessage(errorMessage: String){
        Alert(Alert.AlertType.ERROR).apply {
            title = "Erreur"
            headerText = "Une erreur s'est produite"
            contentText = errorMessage

            show()
        }
    }

    @FXML
    fun backToHome() = this.navigationManager.switchOnController(
        fxmlPath = ScreensConfig.WELCOME_SCREEN
    )

    @FXML
    fun closeApplication(){
        this.beforeSwitch()
        this.navigationManager.mainStage.close()
    }

    @FXML
    fun setSimulationState() {
        try{
            if(!this::simulationConfiguration.isInitialized){
                this.showErrorMessage("La configuration de la simulation n'a pas été chargée")
                return
            }

            if(!this::currentSimulation.isInitialized || this.currentSimulation.stop.get()){
                // must start simulation
                this.currentSimulation = Simulation(configuration = this.simulationConfiguration)
                this.currentSimulation.configuration.toDoOnEnd = {
                    Platform.runLater{
                        this.simulationStateButton.text = "Lancer la simulation"
                        this.navigationManager.mainStage.onCloseRequest = null
                        this.simulationThread = null
                    }
                }
                this.currentSimulation.configuration.painter = this
                this.simulationThread = Thread(this.currentSimulation)
                this.simulationThread!!.start()
                this.navigationManager.mainStage.setOnCloseRequest {
                    this.beforeSwitch()
                }
                this.simulationStateButton.text = "Stopper la simulation"
            }
            else{
                // must stop
                this.currentSimulation.stop.set(true)
                this.simulationThread = null
            }
        }
        catch (e: Exception){
            this.showErrorMessage(errorMessage = "Une erreur s'est produite <${e.message}>")
        }
    }

    @FXML
    fun modifySimulation() = this.navigationManager.switchOnController(
        fxmlPath = ScreensConfig.NEW_CONFIGURATION_SCREEN,
        datas= this.simulationConfiguration
    )
}