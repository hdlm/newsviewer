package com.shokworks.shokworksdemo

import com.shokworks.shokworksdemo.data.repository.NewsRepository
import com.shokworks.shokworksdemo.di.Modules.unitTestModule
import org.junit.After
import org.junit.Before
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class StartKoinForTest : KoinComponent {

    @Before
    fun startKoinForTest() {
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidLogger()
//                androidContext(this@StartKoinForTest)
                modules(unitTestModule)
                printLogger()

            }
        }
    }

    fun inyectandoKoin() {
        val repository by inject<NewsRepository>()

        fun sayHello() {
            val a = repository.inquiryForLastestNews("Android")
        }
    }


    @After
    fun stopKoinAfterTest() = stopKoin()
}