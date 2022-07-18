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
import com.google.gson.GsonBuilder

class DemoApiAdapterDummy : ApiAdapter {

    override suspend fun selectAllNews(query: String): ResponseModel? = run {
        val strJson = "{\"status\":\"ok\",\"totalResults\":7302,\"articles\":[{\"source\":{\"id\":null,\"name\":\"Lifehacker.com\"},\"author\":\"Khamosh Pathak\",\"title\":\"How to Unhide Your Wifi Password\",\"description\":\"Your cellular connection is constant, but your wifi connection is anything but. You probably have multiple locations where your phone automatically connects to the local network: your home, your office, the coffee shop you frequent, your friend’s place, or ev…\",\"url\":\"https://lifehacker.com/how-to-unhide-your-wifi-password-1849151134\",\"urlToImage\":\"https://i.kinja-img.com/gawker-media/image/upload/c_fill,f_auto,fl_progressive,g_center,h_675,pg_1,q_80,w_1200/2d6e66ffad177d0d5d9298759bcc7585.jpg\",\"publishedAt\":\"2022-07-12T15:30:00Z\",\"content\":\"Your cellular connection is constant, but your wifi connection is anything but. You probably have multiple locations where your phone automatically connects to the local network: your home, your offi… [+2026 chars]\"},{\"source\":{\"id\":\"engadget\",\"name\":\"Engadget\"},\"author\":\"Engadget\",\"title\":\"The best Amazon Prime Day wearable deals from Apple, Samsung, Fitbit and others\",\"description\":\"Smartwatches and fitness trackers keep track of your activity throughout the day, but they've also become some of the most popular accessories over the past few years. There are more options to choose from now than ever before, and Amazon Prime Day deals have…\",\"url\":\"https://www.engadget.com/best-wearable-deals-amazon-prime-day-2022-180023695.html\",\"urlToImage\":\"https://s.yimg.com/os/creatr-uploaded-images/2021-10/ed61c5f0-2c26-11ec-bfff-fc22ded728bb\",\"publishedAt\":\"2022-07-13T18:00:23Z\",\"content\":\"Smartwatches and fitness trackers keep track of your activity throughout the day, but they've also become some of the most popular accessories over the past few years. There are more options to choos… [+3401 chars]\"},{\"source\":{\"id\":\"engadget\",\"name\":\"Engadget\"},\"author\":\"Steve Dent\",\"title\":\"Google files a lawsuit that could kick Tinder out of the Play Store\",\"description\":\"Google has counter-sued Match seeking monetary damages and a judgement that would let it kick Tinder and the group's other dating apps out of the Play Store, Bloomberg has reported. Earlier this year, Match sued Google alleging antitrust violations over a dec…\",\"url\":\"https://www.engadget.com/google-files-a-lawsuit-to-kick-tinder-out-of-the-play-store-094540020.html\",\"urlToImage\":\"https://s.yimg.com/os/creatr-uploaded-images/2022-07/18f0db50-0284-11ed-b62f-0cb5a4c8bf4e\",\"publishedAt\":\"2022-07-13T09:45:40Z\",\"content\":\"Google has counter-sued Match seeking monetary damages and a judgement that would let it kick Tinder and the group's other dating apps out of the Play Store, Bloomberg has reported. Earlier this year… [+1845 chars]\"}]}"
        // return Dummy data
        val builder: GsonBuilder = GsonBuilder()
        val gson = builder.create()
        gson.fromJson(strJson, ResponseModel::class.java)
    }
}
