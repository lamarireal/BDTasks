package com.example.a00ex

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import kotlin.math.log

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private val consultas = TaskManager()
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.a00ex", appContext.packageName)
    }

    @Test
    fun fun1() = runBlocking {
        val nombre = consultas.fun1("111A")
        println(nombre)
        assertEquals("Mikel", nombre)
    }

    @Test
    fun fun2() = runBlocking {
        val list = consultas.fun2("Ane")
        println(list)
        assertTrue(list.first().contains("Ane Lopez"))
    }
}