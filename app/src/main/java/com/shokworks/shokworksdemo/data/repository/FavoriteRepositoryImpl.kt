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

import com.shokworks.shokworksdemo.MainActivity
import com.shokworks.shokworksdemo.data.database.AppDatabase
import com.shokworks.shokworksdemo.data.database.dao.FavoriteDao
import com.shokworks.shokworksdemo.data.database.mapper.MapperFavorite
import com.shokworks.shokworksdemo.data.database.model.FavoriteDbModel
import com.shokworks.shokworksdemo.domain.Favorite
import com.shokworks.shokworksdemo.ui.onDismissType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

open class FavoriteRepositoryImpl : FavoriteRepository {
    protected val mNewsFavorite : MutableList<Favorite> = mutableListOf()

    private val coroutineJob = Job()
    private val coroutineScope = CoroutineScope(( Dispatchers.IO + coroutineJob) )

    private val favoriteDao: FavoriteDao = AppDatabase.getInstance(MainActivity.getAppContext())
        .favoriteDao()

    override val newsFavorite: MutableList<Favorite>
        get() = mNewsFavorite


    // <editor-fold defaultstate="collapsed" desc="DAO methods"
    override fun getAllFavorites() {
        coroutineScope.launch {
            val flowOfFavorites = favoriteDao?.getAllFavorites()
            flowOfFavorites?.collect { value ->
                value.forEach {
                    var favorite = MapperFavorite.fromFavoriteDbModel(it)
                    if (mNewsFavorite.indexOf(favorite) == -1 ) { // el registro no existe en la lista, agregarlo
                        mNewsFavorite.add(favorite)
                    }
                }
            }
        }
    }

    override fun getFavoriteById(favId: String) {
        coroutineScope.launch {
            val flowOfFavorite = favoriteDao?.getFavoriteById(favId)
            flowOfFavorite?.collect { value ->
                mNewsFavorite.add(MapperFavorite.fromFavoriteDbModel(value))
            }
        }
    }

    /**
     * The method insert a new register of favorite into the database
     */
    private suspend fun insert(favorite: FavoriteDbModel) {
        favoriteDao?.insert(favorite)
    }

    private suspend fun delete(favorite: FavoriteDbModel) {
        favoriteDao?.delete(favorite)
    }
    // </editor-fold>

    /**
     * The function is responsible for saving favorite news in the database
     */
    override fun savedToFavorites (favorite: FavoriteDbModel, onDone : onDismissType ) {
        coroutineScope.launch {
            insert(favorite)
            delay(200)
            /*onDone.invoke()*/
        }
    }

    /**
     * The function is responsible for removing a favorite news in the database
     */
    override fun removeFromFavorites (favoriteDbModel: FavoriteDbModel, onDone: onDismissType ) {
        coroutineScope.launch {
            delete(favoriteDbModel)
            delay(200)
            var favorite = MapperFavorite.fromFavoriteDbModel(favoriteDbModel)
            mNewsFavorite.remove(favorite)
            /*onDone.invoke()*/
        }
    }
}
