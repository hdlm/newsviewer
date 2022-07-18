package com.shokworks.shokworksdemo.data.repository

import com.shokworks.shokworksdemo.di.Modules
import org.junit.After
import org.junit.Before
import org.koin.core.component.inject
import org.junit.Test
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class NewsRepositoryImplTest : KoinComponent {
    @Before
    fun startKoinForTest() {
        if (GlobalContext.getOrNull() == null) {
            startKoin {
//                androidLogger()
//                androidContext(this@StartKoinForTest)
                modules(Modules.unitTestModule)
                printLogger()

            }
        }
    }

    @Test
    fun manyinquiryForLastestNewsTest() {
        val repository by inject<NewsRepository>()
        repository.inquiryForLastestNews("Android")
        val lst = repository.newsAndroid
        assertTrue(lst.isNotEmpty())
        assertEquals(100, lst.size)
    }
    @After
    fun stopKoinAfterTest() = stopKoin()
}