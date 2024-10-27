package yahvya.implementation.graphical.components

import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.Tooltip
import javafx.scene.layout.VBox
import javafx.util.StringConverter
import yahvya.implementation.multiagent.definitions.Box
import yahvya.implementation.multiagent.environment.EnvironmentCell

/**
 * @brief environment cell configuration component
 */
open class EnvironmentCellConfigurationComponent(
    /**
     * @brief allow to print errors
     */
    var errorPrinter: (errorMessage:String) -> Unit,
    /**
     * @brief creation handler
     */
    var creationHandler: (
        cellInstance: EnvironmentCell,
        cellConfiguration: Map<String,String>,
        countOfCellToCreate: Int,
        boxInstance: Box,
        boxConfiguration: Map<String,String>
    ) -> Unit
) : ComponentBuilder{
    /**
     * @brief cell configuration zone
     */
    protected val cellConfigurationZone: VBox = VBox().apply{
        spacing = 20.0
    }

    /**
     * @brief cell configuration
     */
    protected lateinit var cellConfiguration: MutableMap<String,String>

    /**
     * @brief count of cell field
     */
    protected lateinit var countOfCellField: TextField

    override fun build(): Node {
        val container = VBox()

        container.spacing = 20.0
        container.children.apply{
            // create the list of the available environment cells types
            val cellTypeChoiceBox = ChoiceBox<EnvironmentCell>().apply{
                tooltip = Tooltip("Choisir le type de cellule")
                maxWidth = Double.MAX_VALUE
                converter = object : StringConverter<EnvironmentCell>() {
                    override fun toString(p0: EnvironmentCell?): String = if(p0 !== null) p0.getDisplayName() else "Choisir le type de cellule"

                    override fun fromString(p0: String?): EnvironmentCell = throw Exception("Appel non attendu")
                }

                // manage selection event
                selectionModel.selectedItemProperty().addListener { _, _, newValue -> showCellConfiguration(cellInstance= newValue)}

                EnvironmentCell.AVAILABLE_CELLS_CLASSES.forEach{environmentCellClass ->
                    items.add(environmentCellClass.getConstructor().newInstance())
                }
            }

            addAll(
                cellTypeChoiceBox,
                cellConfigurationZone
            )
        }

        return container
    }

    /**
     * @brief show the provided cell configuration zone
     * @param cellInstance the cell to show configuration
     */
    protected fun showCellConfiguration(cellInstance: EnvironmentCell){
        this.cellConfigurationZone.children.apply{
            clear()

            add(Label("Configuration de la cellule"))

            // count of cell with this configuration
            countOfCellField = TextField()

            countOfCellField.promptText = "Nombre de cellule de ce type à créer"
            countOfCellField.tooltip = Tooltip(countOfCellField.promptText)

            add(countOfCellField)

            // ask cell configuration
            val cellRequiredConfiguration = cellInstance.getToConfigure()

            cellConfiguration = mutableMapOf()

            // create configuration fields
            cellRequiredConfiguration.forEach{ configurationKey ->
                val textfield = TextField()

                textfield.promptText = configurationKey
                textfield.tooltip = Tooltip(configurationKey)
                textfield.textProperty().addListener { _, _, newValue ->
                    cellConfiguration[configurationKey] = newValue
                }

                add(textfield)
            }

            // ask box configuration

            val boxConfigurationComponent = BoxConfigurationComponent()

            addAll(
                Label("Box de la cellule"),
                boxConfigurationComponent.build()
            )

            // validation button
            val validationButton = Button("Valider").apply{
                cursor = Cursor.HAND
                tooltip = Tooltip("Valider la configuration")
                setOnMouseClicked {
                    validateConfigurationCreation(
                        cellInstance= cellInstance,
                        boxConfigurationComponent= boxConfigurationComponent
                    )
                }
            }

            add(validationButton)
        }
    }

    /**
     * @brief try to validate the configuration. if ok call the handler
     * @param cellInstance cell instance
     * @param boxConfigurationComponent box configuration component
     */
    protected fun validateConfigurationCreation(cellInstance: EnvironmentCell,boxConfigurationComponent: BoxConfigurationComponent){
        // check count of cell
        val numberMatcher = Regex("^[0-9]+$")

        if(!this.countOfCellField.text.matches(numberMatcher)){
            this.errorPrinter.invoke("Veuillez fournir un nombre de cellules valide")
            return
        }

        // check cell configuration
        if(!cellInstance.isConfigurationValid(configuration = cellConfiguration)){
            this.errorPrinter.invoke("Veuillez remplir les configurations requises de la cellule")
            return
        }

        // check box configuration
        if(!boxConfigurationComponent.isBoxConfigurationValid()){
            this.errorPrinter.invoke("Veuillez configurer la box lié à la cellule")
            return
        }

        this.creationHandler.invoke(
            cellInstance,
            this.cellConfiguration,
            this.countOfCellField.text.toInt(),
            boxConfigurationComponent.currentBoxInstance,
            boxConfigurationComponent.boxConfiguration
        )
    }
}