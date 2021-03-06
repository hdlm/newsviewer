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

package com.shokworks.shokworksdemo.networking

import com.shokworks.shokworksdemo.networking.model.ResponseModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * The class it is responsible to interact with the APIResful calls
 */
class DemoApiAdapter : ApiAdapter {

    override suspend fun selectAllNews(query: String): ResponseModel? {
        val url = "${BASE_URL}${query}&apiKey=${Companion.API_KEY}"
        val call = DemoApiAdapter.getRetrofit().create(DemoApiService::class.java).getNews(url)
        if (!call.isSuccessful) {
            return null
        }
        return call.body() as ResponseModel?
    }

    companion object {
        private const val BASE_URL = "https://newsapi.org/v2/"
        private const val API_KEY = "6bd473946d3a451abc5648e04b9266b8"

        fun getRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
