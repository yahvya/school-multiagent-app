package yahvya.implementation.graphical.components

import javafx.scene.Node
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.scene.control.Tooltip
import javafx.scene.layout.VBox
import javafx.util.StringConverter
import yahvya.implementation.multiagent.definitions.Box

open class BoxConfigurationComponent : ComponentBuilder{
    /**
     * @brief box configuration zone
     */
    protected val boxConfigurationZone: VBox = VBox().apply{
        spacing = 20.0
    }

    /**
     * @brief box configuration
     */
    lateinit var boxConfiguration: MutableMap<String,String>

    /*
    @brief current box instance
     */
    lateinit var currentBoxInstance: Box

    override fun build(): Node {
        val container = VBox()

        container.spacing = 20.0
        container.children.apply {
            val boxTypeChoiceBox = ChoiceBox<Box>().apply{
                tooltip = Tooltip("Choisir le type de box")
                maxWidth = Double.MAX_VALUE
                converter = object : StringConverter<Box>() {
                    override fun toString(p0: Box?): String = if(p0 === null) "Choisir le type de box" else p0.getDisplayName()
                    override fun fromString(p0: String?): Box = throw Exception("Appel inattendu")
                }

                // manage selection event
                selectionModel.selectedItemProperty().addListener { _, _, newValue -> showBoxConfiguration(boxInstance = newValue) }

                Box.AVAILABLE_BOX_CLASSES.forEach { boxClass ->
                    items.add(boxClass.getConstructor().newInstance())
                }
            }

            addAll(
                boxTypeChoiceBox,
                boxConfigurationZone
            )
        }

        return container
    }

    /**
     * @brief show the box configuration
     * @param boxInstance the box to show configuration
     */
    fun showBoxConfiguration(boxInstance: Box){
        this.currentBoxInstance = boxInstance
        this.boxConfiguration = mutableMapOf()

        this.boxConfigurationZone.children.apply{
            clear()

            boxInstance.getToConfigure().forEach { configurationKey ->
                val textfield = TextField()

                textfield.promptText = configurationKey
                textfield.tooltip = Tooltip(configurationKey)
                textfield.textProperty().addListener { _, _, newValue ->
                    boxConfiguration[configurationKey] = newValue
                }

                add(textfield)
            }
        }
    }

    fun isBoxConfigurationValid():Boolean = this::currentBoxInstance.isInitialized && this.currentBoxInstance.isConfigurationValid(configuration = this.boxConfiguration)
}