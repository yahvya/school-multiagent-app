package yahvya.implementation.multiagent.definitions

import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.plugins.loader.PluginsLoader
import kotlin.collections.get

/**
 * @brief box
 */
abstract class Box : Exportable{
    companion object{
        /**
         * @brief available box classes from plugins
         */
        var AVAILABLE_BOX_CLASSES: MutableList<Class<out Box>> = mutableListOf()

        init {
            this.loadAvailableBoxPlugins()
        }

        /**
         * @brief load available box classes
         * @throws Nothing
         */
        fun loadAvailableBoxPlugins(){
            ApplicationConfig.LOGGER.info("Loading available box from plugins")

            this.AVAILABLE_BOX_CLASSES = PluginsLoader.loadPluginsClasses<Box>()
        }

        /**
         * @brief create a box from configuration
         * @param configuration configuration
         * @return the created box
         * @throws Exception on error
         */
        fun createFromConfiguration(configuration: Map<*,*>): Box{
            ApplicationConfig.LOGGER.info("Creating box from configuration")

            if(!Exportable.containKeys(configuration= configuration, ExportKeys.CLASS, ExportKeys.SPECIFIC_CONFIGURATION))
                throw Exception("The provided configuration for the box doesn't contain all expected keys")

            val boxClass = configuration[ExportKeys.CLASS] as String
            val foundedClass = this.AVAILABLE_BOX_CLASSES.first{it.canonicalName == boxClass}
            val instance = foundedClass.getConstructor().newInstance() as Box

            // load from configuration
            if(!instance.loadFromExportedConfig(configuration = configuration[ExportKeys.SPECIFIC_CONFIGURATION] as Map<*,*>))
                throw Exception("Fail to configure to box")

            return instance
        }
    }

    constructor()

    /**
     * @brief check if the box collide with the given coords
     * @param x x pos
     * @param y y pos
     * @return if collide
     * @throws Nothing
     */
    abstract fun collideWith(x: Double, y: Double): Boolean

    /**
     * @brief build the specific box configuration
     * @return the configuration
     */
    abstract fun buildExportConfiguration():Map<*,*>

    override fun exportConfig(): Map<*, *> {
        return mapOf<String,Any>(
            ExportKeys.CLASS to this.javaClass.canonicalName,
            ExportKeys.SPECIFIC_CONFIGURATION to this.buildExportConfiguration()
        )
    }

    /**
     * @brief export keys
     */
    data object ExportKeys{
        /**
         * @brief specific box class
         */
        const val CLASS = "class"

        /**
         * @brief specific configuration
         */
        const val SPECIFIC_CONFIGURATION = "specificConfiguration"
    }
}