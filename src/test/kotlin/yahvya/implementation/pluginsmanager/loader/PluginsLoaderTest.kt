package yahvya.implementation.pluginsmanager.loader

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import yahvya.implementation.multiagent.TestInit
import yahvya.implementation.multiagent.environment.EnvironmentCell

/**
 * @brief plugins loader test
 */
class PluginsLoaderTest {
    companion object{
        @BeforeAll
        @JvmStatic
        fun setup() = TestInit.initForTest()
    }

    @Test
    fun testPluginsLoading(){
        val plugins = PluginsLoader.loadPluginsClasses<EnvironmentCell>()

        assertTrue(plugins.isNotEmpty())
    }
}