package yahvya.implementation.graphical.controllers

import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.StageStyle
import yahvya.implementation.configurations.ScreensConfig
import yahvya.implementation.graphical.components.EnvironmentCellConfigurationComponents
import yahvya.implementation.multiagent.definitions.Box
import yahvya.implementation.multiagent.environment.EnvironmentCell
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
     * @brief environment cells
     */
    protected val environmentCells: MutableList<CellConfig> = mutableListOf()

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
     * @throws Nothing
     */
    protected fun buildInterfaceFromSimulationConfig(){
        this.apply {
            environmentName.text = simulationConfiguration.environment.name
            simulationName.text = simulationConfiguration.name
            showGuiState.isSelected = simulationConfiguration.showGui
        }
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

    /**
     * @brief clear the configuration zone
     */
    protected fun clearConfigurationZone() = this.configurationZone.children.clear()

    /**
     * @brief register a new environment cell
     * @param cellInstance cell instance
     * @param cellConfiguration cell instance linked configuration
     * @param countOfCellToCreate count of cell with the same configuration to create
     * @param boxInstance cell linked box instance
     * @param boxConfiguration cell linked box instance configuration
     */
    protected fun registerNewEnvironmentCell(
        cellInstance: EnvironmentCell,
        cellConfiguration: Map<String,String>,
        countOfCellToCreate: Int,
        boxInstance: Box,
        boxConfiguration: Map<String,String>
    ){
        boxInstance.receiveConfiguration(configuration = boxConfiguration)
        cellInstance.receiveConfiguration(configuration = cellConfiguration)
        cellInstance.box = boxInstance

        val cellConfig = CellConfig(
            cellConfiguration = cellInstance.exportConfig(),
            countOfCellsToCreate= countOfCellToCreate
        )

        this.environmentCells.add(cellConfig)
    }

    @FXML
    fun closeApplication() = this.navigationManager.mainStage.close()

    @FXML
    fun addAgent() {

    }

    @FXML
    fun addEnvironmentCell() {
        try{
            this.configurationZone.children.apply{
                val environmentCellComponent = EnvironmentCellConfigurationComponents(
                    errorPrinter = ::showErrorMessage,
                    creationHandler = ::registerNewEnvironmentCell
                )

                clear()
                addAll(
                    Label("Ajouter une cellule à l'environnement"),
                    environmentCellComponent.build()
                )
            }
        }
        catch(e:Exception){
            this.showErrorMessage(errorMessage = "Une erreur s'est produite lors de la gestion de création de cellule <${e.message}>")
        }
    }

    @FXML
    fun downloadConfiguration() {
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

                environmentCells.forEach{ environmentCellConfig ->
                    repeat(environmentCellConfig.countOfCellsToCreate,{
                        cells.add(EnvironmentCell.createFromConfiguration(configuration = environmentCellConfig.cellConfiguration))
                    })
                }
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
    fun goBackToHome() {
        this.navigationManager.switchOnController(fxmlPath = ScreensConfig.WELCOME_SCREEN)
    }

    /**
     * @brief cell to register config
     */
    data class CellConfig(
        /**
         * @brief cell exported configuration
         */
        val cellConfiguration: Map<*,*>,
        /**
         * @brief count of cells to create
         */
        val countOfCellsToCreate: Int,
    )
}