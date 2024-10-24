package yahvya.implementation.multiagent.simulation

import jade.wrapper.PlatformController
import jade.wrapper.PlatformEvent
import java.util.concurrent.atomic.AtomicInteger

/**
 * @brief simulation jade event listener
 */
open class CustomListener(
    /**
     * @brief parent simulation
     */
    val simulation: Simulation
) : PlatformController.Listener{
    /**
     * @brief count of agents
     */
    val agentsCount = AtomicInteger(0)

    override fun bornAgent(p0: PlatformEvent?){
        this.agentsCount.incrementAndGet()
    }

    override fun deadAgent(p0: PlatformEvent?){
        if(this.agentsCount.decrementAndGet() <= 0)
            this.simulation.stop.set(true)
    }

    override fun startedPlatform(p0: PlatformEvent?){}

    override fun suspendedPlatform(p0: PlatformEvent?){}

    override fun resumedPlatform(p0: PlatformEvent?){}

    override fun killedPlatform(p0: PlatformEvent?){}
}