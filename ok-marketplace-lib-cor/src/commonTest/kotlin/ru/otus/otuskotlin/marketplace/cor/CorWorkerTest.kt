package ru.otus.otuskotlin.marketplace.cor

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.cor.handlers.CorWorker
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class CorWorkerTest {
    @Test
    @JsName("worker_should_execute_handle")
    fun `worker should execute handle`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockHandle = { history += "w1; " }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("w1; ", ctx.history)
    }

    @Test
    @JsName("worker_should_not_execute_when_off")
    fun `worker should not execute when off`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockOn = { status == TestContext.CorStatuses.ERROR },
            blockHandle = { history += "w1; " }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("", ctx.history)
    }

    @Test
    @JsName("worker_should_handle_exception")
    fun `worker should handle exception`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockHandle = { throw RuntimeException("some error") },
            blockExcept = { e -> history += e.message }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("some error", ctx.history)
    }

}
