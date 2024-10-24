package yahvya.implementation.multiagent.environment

import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.multiagent.interfaces.Box
import yahvya.implementation.multiagent.interfaces.Configurable
import yahvya.implementation.multiagent.interfaces.Exportable
import yahvya.implementation.plugins.loader.PluginsLoader

/**
 * @brief environment cell
 */
abstract class EnvironmentCell: Exportable, Configurable {
    /**
     * @brief the cell box
     */
    lateinit var box: Box

    /**
     * @brief cell configuration
     */
    var configuration: Map<*,*> = mapOf<String,String>()

    companion object{
        /**
         * @brief available cells classes from plugins
         */
        var AVAILABLE_CELLS_CLASSES: MutableList<Class<out EnvironmentCell>> = mutableListOf()

        init {
            this.loadAvailableCellsPlugins()
        }

        /**
         * @brief load available cells classes
         * @throws Nothing
         */
        fun loadAvailableCellsPlugins(){
            ApplicationConfig.LOGGER.info("Loading available cells from plugins")

            this.AVAILABLE_CELLS_CLASSES = PluginsLoader.loadPluginsClasses<EnvironmentCell>()
        }

        /**
         * @brief create a cell from the exported configuration
         * @param configuration exported configuration
         * @return the created cell
         * @throws Exception on error
         */
        fun createFromConfiguration(configuration: Map<*,*>): EnvironmentCell{
            ApplicationConfig.LOGGER.info("Creating environment cell from configuration")

            if(!Exportable.containKeys(configuration= configuration, ExportKeys.CLASS, ExportKeys.SPECIFIC_CONFIG,
                    ExportKeys.BOX, ExportKeys.EXTERNAL_CONFIGURATION))
                throw Exception("The provided configuration for the cell doesn't contain all expected keys")

            val cellClass = configuration[ExportKeys.CLASS] as String

            val foundedClass = this.AVAILABLE_CELLS_CLASSES.first{it.canonicalName == cellClass}
            val instance = foundedClass.getConstructor().newInstance() as EnvironmentCell

            instance.configuration = configuration[ExportKeys.EXTERNAL_CONFIGURATION] as Map<*,*>

            // load from configuration
            instance.box = Box.createFromConfiguration(configuration = configuration[ExportKeys.BOX] as Map<*,*>)

            if(!instance.loadFromExportedConfig(configuration = configuration[ExportKeys.SPECIFIC_CONFIG] as Map<*,*>))
                throw Exception("Fail to configure to cell")

            return instance
        }
    }

    /**
     * @throws Nothing
     */
    constructor()

    /**
     * @param box cell box
     * @throws Nothing
     */
    constructor(box: Box){
        this.box = box
    }

    /**
     * @brief provide the cell display name
     * @throws Nothing
     */
    abstract fun getDisplayName():String

    /**
     * @brief build the export configuration of this particular cell type
     * @return the configuration
     * @throws Nothing
     */
    abstract fun buildExportConfiguration(): Map<*,*>

    /**
     * @brief update the cell
     * @param environment parent environment
     * @return this
     * @throws Nothing
     */
    fun update(environment: Environment): EnvironmentCell = this

    override fun exportConfig(): Map<*, *> {
        return mapOf<String,Any>(
            ExportKeys.CLASS to this.javaClass.canonicalName,
            ExportKeys.BOX to this.box.exportConfig(),
            ExportKeys.SPECIFIC_CONFIG to this.buildExportConfiguration(),
            ExportKeys.EXTERNAL_CONFIGURATION to this.configuration
        )
    }

    override fun receiveConfiguration(configuration: Map<String, String>) {
        this.configuration = configuration
    }

    /**
     * @brief export keys
     */
    data object ExportKeys{
        /**
         * @brief cell class
         */
        const val CLASS:String = "class"

        /**
         * @brief box configuration
         */
        const val BOX: String = "box"

        /**
         * @brief cell specific configuration
         */
        const val SPECIFIC_CONFIG:String = "specificConfig"

        /**
         * @brief external configuration for configurable
         */
        const val EXTERNAL_CONFIGURATION:String = "externalConfiguration"
    }
}