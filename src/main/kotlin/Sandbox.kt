import io.opentelemetry.extension.annotations.WithSpan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class Sandbox {
    companion object {
        @WithSpan
        suspend fun breakingSpans() {
            kotlin.runCatching {
                blockingWork()
                usingIOPool()
            }
        }

        @WithSpan
        suspend fun blockingWork() {
            delay(2000L)
        }

        @WithSpan
        suspend fun usingIOPool() {
            return withContext(Dispatchers.IO) {
                fetchUpstreamResource()
                println("Finished getting upstream resource")
            }
        }

        @WithSpan
        suspend fun fetchUpstreamResource(){
            delay(2000L)
        }

        fun runit() {
            runBlocking {
                println("Starting the example")
                breakingSpans()
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            runit()
        }
    }
}