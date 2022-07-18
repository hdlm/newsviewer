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

package com.shokworks.shokworksdemo.presentation

import androidx.lifecycle.ViewModel
import com.shokworks.shokworksdemo.data.database.mapper.MapperFavorite.toFavoriteDb
import com.shokworks.shokworksdemo.networking.model.ArticleModel
import com.shokworks.shokworksdemo.data.repository.NewsRepositoryImpl
import com.shokworks.shokworksdemo.domain.Favorite
import com.shokworks.shokworksdemo.data.repository.FavoriteRepositoryImpl
import com.shokworks.shokworksdemo.networking.model.SourceModel

class NewsViewModel(
    private val repository: NewsRepositoryImpl,
    private val repositoryFavorite: FavoriteRepositoryImpl
) : ViewModel() {
    /**
     * The attribute is used to determine in which of the lists the _selected news item_ is found
     * when navigating in the webview.
     */
    var isFromAndroidList: Boolean = false
    var isFirstLoading: Boolean = true

    val newsAndroid: List<ArticleModel>
        get() = repository.newsAndroid

    val newsIphone: List<ArticleModel>
        get() = repository.newsIphone

    val newsFavorite: List<Favorite>
        get() = repositoryFavorite.newsFavorite

    /**
     * Data-binding [NewsViewModel]ViewModel - [NewsView] View
     */
    var onNewsAndroidUpdated: ( (List<ArticleModel>) -> Unit )? = null
        set(value) {
            field = value
            onNewsAndroidUpdated?.invoke(newsAndroid)
        }

    /**
     * Data-binding [NewsViewModel]ViewModel - [NewsView] View
     */
    var onNewsIphoneUpdated: ( (List<ArticleModel>) -> Unit )? = null
        set(value) {
            field = value
            onNewsIphoneUpdated?.invoke(newsIphone)
        }

    /**
     * Data-binding [NewsViewModel] - [NewsView]
     */
    var onFavoriteUpdated: ( (List<Favorite>) -> Unit )? = null
        set(value) {
            field = value
            onFavoriteUpdated?.invoke(newsFavorite)
        }

    /**
     * The method is responsible for retrieve the lastest News of the
     * _keyword_ selected, and keep the origin of the list to which the news item belongs
     */
    fun retrieveNews(phrase: String) {
        when(phrase) {
            "Android" -> {
                isFromAndroidList = true
                repository.inquiryForLastestNews(keyWord = phrase)
                onNewsAndroidUpdated?.invoke(newsAndroid)
            }
            "iPhone" -> {
                isFromAndroidList = false
                repository.inquiryForLastestNews(keyWord = phrase)
                onNewsIphoneUpdated?.invoke(newsIphone)
            }
        }
    }

    /**
     * The method is responsible for retrieve the lastest Favorites of the
     */
    fun retrieveFavorites() {
        repositoryFavorite.getAllFavorites()
        /*onFavoriteUpdated?.invoke(newsFavorite) */
    }

    /**
     * The function is responsible for saving the news into favorites
     */
    fun saveNewsAsFavorite(sUrl: String) {
        var favorite: Favorite? = null

        // map the object
        val newsItem = if (isFromAndroidList) {
            newsAndroid.first { it.url == sUrl }
        } else {
            newsIphone.first { it.url == sUrl }
        }
        with(newsItem) {
            favorite = Favorite(
                source = SourceModel(source.id, source.name),
                author = author,
                title = title,
                description = description,
                url = url,
                urlToImage = urlToImage,
                publishedAt = publishedAt,
                content = content
            )
        }
        repositoryFavorite.savedToFavorites(favorite!!.toFavoriteDb()
        ) { /*onFavoriteUpdated!!.invoke(newsFavorite)*/ }
    }


    /**
     * The function removes the specified record from favorites
     */
    fun removeFromFavorites(id: String) {
        val favorite : Favorite = newsFavorite.first { it.url == id }
        repositoryFavorite.removeFromFavorites(favorite.toFavoriteDb()
        ) { /*onFavoriteUpdated!!.invoke(newsFavorite)*/}
    }
}
