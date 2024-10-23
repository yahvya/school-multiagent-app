package yahvya.implementation.plugins.loader

import yahvya.implementation.configurations.ApplicationConfig
import yahvya.implementation.configurations.PathsConfig
import java.io.File
import java.io.FileInputStream
import java.net.URLClassLoader
import java.util.jar.JarInputStream
import kotlin.collections.forEach
import kotlin.collections.toTypedArray

/**
 * @brief plugins loader
 */
object PluginsLoader {
    /**
     * @brief load the classes of the stored plugins
     * @param PluginsType plugins to load type
     * @return plugin classes
     * @throws Nothing
     */
    inline fun <reified PluginsType> loadPluginsClasses(): MutableList<Class<out PluginsType>>{
        ApplicationConfig.LOGGER.info("Loading plugins classes of type <${PluginsType::class}>")

        val result = mutableListOf<Class<out PluginsType>>()

        try{
            // loading directory
            val directoryResource = ApplicationConfig.ROOT_CLASS.getResource(PathsConfig.PLUGINS_DIRECTORY_PATH)

            if(directoryResource === null)
                throw Exception("Fail to load directory")

            val pluginsDirectoryReader = File(directoryResource.path)

            if(!pluginsDirectoryReader.isDirectory)
                throw Exception("The provided path isn't a directory")

            // loading jar url's list
            val jarUrlList = pluginsDirectoryReader
                .listFiles()
                ?.map{ it.toURI().toURL()}

            if(jarUrlList === null)
                throw Exception("Fail to load jar links")

            val classLoader = URLClassLoader(jarUrlList.toTypedArray())

            jarUrlList.forEach{ jarUrl ->
                JarInputStream(FileInputStream(jarUrl.file)).use{ inputStream ->
                    // loading classes from jar
                    while (true) {
                        val entry = inputStream.nextJarEntry ?: break

                        if(!entry.name.matches(Regex(".*${ApplicationConfig.PLUGINS_PARENT_DIRECTORY}.*\\.class")))
                            continue

                        // format file path to package format
                        val formattedClasspath = entry.name
                            .split(".class")
                            .first()
                            .replace("/",".")
                            .replace("\\",".")

                        result.add(classLoader.loadClass(formattedClasspath) as Class<PluginsType>)
                    }
                }
            }

            ApplicationConfig.LOGGER.info("Successfully loaded plugins")
        }
        catch(e:Exception){
            ApplicationConfig.LOGGER.error("Fail to load plugins <${e.message}>")
        }

        return result
    }
}