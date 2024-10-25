package yahvya.implementation.multiagent.environment

import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.multiagent.definitions.Exportable

/**
 * @brief environment configuration
 */
open class Environment : Exportable{
    /**
     * @brief environment name
     */
    var name: String = ""

    /**
     * @brief environment cells
     */
    var cells: MutableList<EnvironmentCell> = mutableListOf()

    companion object{
        /**
         * @brief create an environment from environment
         * @param configuration configuration
         * @return the created environment
         * @throws Exception on error
         */
        fun createFromConfiguration(configuration: Map<*,*>): Environment{
            ApplicationConfig.LOGGER.info("Creating environment from configuration")

            val environment = Environment()

            if(!environment.loadFromExportedConfig(configuration = configuration))
                throw Exception("Fail to load environment from configuration")

            return environment
        }
    }

    /**
     * @throws Nothing
     */
    constructor()

    /**
     * @brief search the cells of the given type
     * @param Type type
     * @return cells of this type
     * @throws Nothing
     */
    inline fun <reified Type> getCellsOfType():List<EnvironmentCell> = this.cells.filter{ it is Type }

    /**
     * @brief update the environment
     * @return this
     * @throws Nothing
     */
    fun update(): Environment{
        ApplicationConfig.LOGGER.info("Updating environment")

        this.cells.forEach{ it.update(environment = this) }

        ApplicationConfig.LOGGER.info("Environment updated")

        return this
    }

    override fun exportConfig(): Map<*,*> = mapOf(
        ExportKeys.NAME to this.name,
        ExportKeys.CELLS to this.cells.map{ it.exportConfig() }
    )

    override fun loadFromExportedConfig(configuration: Map<*, *>): Boolean {
        try{
            ApplicationConfig.LOGGER.info("Loading environment from configuration")

            if(!Exportable.containKeys(configuration = configuration, ExportKeys.CELLS, ExportKeys.NAME)){
                ApplicationConfig.LOGGER.error("The provided environment configuration doesn't contain all expected keys")
                return false
            }

            // loading from configuration
            val cellsConfig = configuration[ExportKeys.CELLS] as MutableList<*>

            this.name = configuration[ExportKeys.NAME] as String
            this.cells = cellsConfig.map{
                EnvironmentCell.createFromConfiguration(configuration = it as Map<*,*>)
            } as MutableList<EnvironmentCell>

            return true
        }
        catch(e: Exception){
            ApplicationConfig.LOGGER.error("Fail to load environment due to <${e.message}>")
            return false
        }
    }

    /**
     * @brief export keys
     */
    data object ExportKeys{
        /**
         * @brief environment name
         */
        const val NAME:String = "name"

        /**
         * @brief environment cells
         */
        const val CELLS:String = "cells"
    }
}