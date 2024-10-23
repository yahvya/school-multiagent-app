package yahvya.implementation.multiagent.simulation

import com.google.gson.Gson
import yahvya.implementation.configurations.ApplicationConfig
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter

/**
 * @brief simulation storage manager
 */
object SimulationStorage {
    /**
     * @brief save the simulation configuration in the stream
     * @param simulation simulation
     * @param outputStream stream to write on
     * @return save success
     * @throws Nothing
     */
    fun storeIn(simulation: Simulation, outputStream: OutputStream): Boolean {
        ApplicationConfig.LOGGER.info("Storing simulation configuration <$simulation> into <$outputStream>")

        try{
            val simulationConfig = simulation.exportConfig()

            OutputStreamWriter(outputStream).use{ outputStreamWriter ->
                Gson().toJson(simulationConfig,outputStreamWriter)
            }

            return true
        }
        catch(e: Exception) {
            ApplicationConfig.LOGGER.error("Fail to store the simulation <$simulation> configuration due to <${e.message}>")
            return false
        }
    }

    /**
     * @brief load the simulation from the input stream
     * @param inputStream input stream
     * @return the created simulation
     * @attention the simulation must have been created using the storeIn method
     * @throws Exception on error
     */
    fun loadFrom(inputStream: InputStream): Simulation{
        ApplicationConfig.LOGGER.info("Loading simulation from input stream <$inputStream>")

        InputStreamReader(inputStream).use{ inputStreamReader ->
            val decodedConfig = Gson().fromJson(inputStreamReader,Map::class.java)
            return Simulation.createFromConfiguration(configuration = decodedConfig)
        }
    }
}