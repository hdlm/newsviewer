/*
Copyright (c) 2022 Henry De la Mano

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
distribute, sublicense, create a derivative work, and/or sell copies of the
Software in any work that is designed, intended, or marketed for pedagogical or
instructional purposes related to programming, coding, application development,
or information technology.  Permission for such use, copying, modification,
merger, publication, distribution, sublicensing, creation of derivative works,
or sale is expressly withheld.

This project and source code may use libraries or frameworks that are
released under various Open-Source licenses. Use of those libraries and
frameworks are governed by their own individual licenses.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.shokworks.shokworksdemo.data.repository

import com.shokworks.shokworksdemo.commons.Utily
import com.shokworks.shokworksdemo.networking.model.ArticleModel
import com.shokworks.shokworksdemo.networking.DemoApiAdapter
import com.shokworks.shokworksdemo.networking.model.ResponseModel
import com.shokworks.shokworksdemo.networking.model.SourceModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

open class NewsRepositoryImpl : NewsRepository {

    private val coroutineJob = Job()
    private val coroutineScope = CoroutineScope(( Dispatchers.IO + coroutineJob) )

    protected val _newsAndroid : MutableList<ArticleModel> = mutableListOf()
    protected val _newsIphone : MutableList<ArticleModel> = mutableListOf()

    private val apiAdapter: DemoApiAdapter = DemoApiAdapter()

    override val newsAndroid: List<ArticleModel>
        get() = _newsAndroid

    override val newsIphone: List<ArticleModel>
        get() = _newsIphone

    /**
     * the method is responsible for querying the news to the REST API
     * and serve as data-binding to the ViewModel
     */
    override fun inquiryForLastestNews(keyWord: String, lastDays: Int) {
        coroutineScope.launch {
            val flowOfNewsFeed = getAllNewsfeedSync(keyWord, lastDays)
            flowOfNewsFeed.collect { value ->
                value?.let {
                    if (keyWord == "Android") {
                        if (_newsAndroid.isEmpty() ) _newsAndroid.addAll(it.articles)
                    }
                    else {
                        if (_newsIphone.isEmpty() ) _newsIphone.addAll(it.articles)
                    }
                    delay(100)
                } ?: run {
                    _newsAndroid.add( registerWhenNotFoundResult() )
                    _newsIphone.add( registerWhenNotFoundResult() )
                }
            }
        }
    }


    /**
     * If there are no results in the search, a single element will be returned to
     * indicate the result of the operation
     */
    private fun registerWhenNotFoundResult(): ArticleModel {
        val sourceModel = SourceModel("", "foo")
        return ArticleModel(
            sourceModel,
            "",
            "Haven't news yet",
            "", "", "", "", ""
        )
    }


    /**
     * The method is responsible to load the data from the API RESTFull
     * invoking _Search for articles on the web that mention a keyword_
     */
    protected open fun getAllNewsfeedSync(keyWord: String, lastDays: Int = 7) : Flow<ResponseModel?> = run {
        flow {
            val searchByKeyword = "everything?q=${keyWord}"
            val sDate = Utily.getCurrentDateMinusDays( lastDays ) // obtain news from the lastDays
            val response: ResponseModel? = apiAdapter.selectAllNews("${searchByKeyword}&from=${sDate}")
            emit(response)
        }
    }


}
