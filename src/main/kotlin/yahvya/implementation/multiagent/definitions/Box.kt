package yahvya.implementation.multiagent.definitions

import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.configurations.DefaultPluginsConfig
import yahvya.implementation.pluginsmanager.loader.PluginsLoader
import kotlin.collections.get
import kotlin.reflect.KProperty

/**
 * @brief box
 */
abstract class Box : Exportable,Configurable{
    /**
     * @brief box configuration
     */
    var configuration: Map<*,*> = mapOf<String,String>()

    companion object{
        /**
         * @brief available box classes from plugins
         */
        val AVAILABLE_BOX_CLASSES: MutableList<Class<out Box>> by object{
            operator fun getValue(thisRef: Any?, property: KProperty<*>): MutableList<Class<out Box>> = loadAvailableBoxPlugins()
        }

        /**
         * @brief load available box classes
         * @throws Nothing
         * @return result
         */
        fun loadAvailableBoxPlugins():MutableList<Class<out Box>>{
            ApplicationConfig.LOGGER.info("Loading available box from plugins")

            val result = PluginsLoader.loadPluginsClasses<Box>()
            result.addAll(DefaultPluginsConfig.DEFAULT_BOXES)

            return result
        }

        /**
         * @brief create a box from configuration
         * @param configuration configuration
         * @return the created box
         * @throws Exception on error
         */
        fun createFromConfiguration(configuration: Map<*,*>): Box{
            ApplicationConfig.LOGGER.info("Creating box from configuration")

            if(!Exportable.containKeys(configuration= configuration, ExportKeys.CLASS, ExportKeys.SPECIFIC_CONFIGURATION,ExportKeys.EXTERNAL_CONFIGURATION))
                throw Exception("The provided configuration for the box doesn't contain all expected keys")

            val boxClass = configuration[ExportKeys.CLASS] as String
            val foundedClass = this.AVAILABLE_BOX_CLASSES.first{it.canonicalName == boxClass}
            val instance = foundedClass.getConstructor().newInstance() as Box

            instance.configuration = configuration[ExportKeys.EXTERNAL_CONFIGURATION] as Map<*,*>

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
     * @return box display name
     */
    abstract fun getDisplayName(): String

    /**
     * @brief build the specific box configuration
     * @return the configuration
     */
    abstract fun buildExportConfiguration():Map<*,*>

    override fun exportConfig(): Map<*, *> {
        return mapOf<String,Any>(
            ExportKeys.CLASS to this.javaClass.canonicalName,
            ExportKeys.SPECIFIC_CONFIGURATION to this.buildExportConfiguration(),
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
         * @brief specific box class
         */
        const val CLASS = "class"

        /**
         * @brief specific configuration
         */
        const val SPECIFIC_CONFIGURATION = "specificConfiguration"

        /**
         * @brief external configuration for configurable
         */
        const val EXTERNAL_CONFIGURATION:String = "externalConfiguration"
    }
}