package yahvya.implementation.graphical.controllers

import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.StageStyle
import yahvya.implementation.configurations.ScreensConfig
import yahvya.implementation.multiagent.simulation.Simulation
import yahvya.implementation.multiagent.simulation.SimulationConfiguration
import yahvya.implementation.multiagent.simulation.SimulationStorage
import java.io.FileOutputStream


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

    @FXML
    private lateinit var showGuiState: CheckBox

    /**
     * @brief simulation configuration
     */
    protected lateinit var simulationConfiguration: SimulationConfiguration

    override fun storeCurrentVersionOnSwitch(): Boolean = false

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
        this.apply {
            environmentName.text = simulationConfiguration.environment.name
            simulationName.text = simulationConfiguration.name
            showGuiState.isSelected = simulationConfiguration.showGui
        }
    }

    /**
     * @brief show an error message in alert
     * @param errorMessage error message
     */
    protected open fun showErrorMessage(errorMessage: String){
        Alert(Alert.AlertType.ERROR).apply {
            title = "Erreur"
            headerText = "Une erreur s'est produite"
            contentText = errorMessage

            show()
        }
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
        // verify configuration validity
        if(this.environmentName.text.isEmpty()){
            this.showErrorMessage(errorMessage = "Veuillez donner un nom à l'environnement")
            return
        }

        if(this.simulationName.text.isEmpty()){
            this.showErrorMessage(errorMessage = "Veuillez donner un nom à la simulation")
            return
        }

        // load the file to save in
        val fileChooser = FileChooser()

        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Simulation",WelcomeScreenController.CONFIGURATION_FILE_EXTENSION))

        val chosenFile = fileChooser.showSaveDialog(this.navigationManager.mainStage)

        if(chosenFile === null)
            return

        try{
            // save configuration in instance

            this.simulationConfiguration.apply{
                name = simulationName.text
                showGui = showGuiState.isSelected
            }

            this.simulationConfiguration.environment.apply {
                name = environmentName.text
            }

            FileOutputStream(chosenFile).use{ outputStream ->
                if(!SimulationStorage.storeIn(
                    simulation = Simulation(configuration = this.simulationConfiguration),
                    outputStream = outputStream
                ))
                    this.showErrorMessage(errorMessage = "Une erreur s'est produite lors du téléchargement de la configuration")
            }
        }
        catch(e:Exception){
            this.showErrorMessage(errorMessage = "Une erreur s'est produite lors du téléchargement <${e.message}>")
        }
    }

    @FXML
    fun goBackToHome(event: MouseEvent) {
        this.navigationManager.switchOnController(fxmlPath = ScreensConfig.WELCOME_SCREEN)
    }
}