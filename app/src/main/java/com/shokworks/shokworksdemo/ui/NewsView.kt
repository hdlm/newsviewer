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

package com.shokworks.shokworksdemo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import com.shokworks.shokworksdemo.presentation.NewsViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shokworks.shokworksdemo.domain.Favorite
import com.shokworks.shokworksdemo.networking.model.ArticleModel
import com.shokworks.shokworksdemo.routing.DemoRouter.navigateTo
import com.shokworks.shokworksdemo.routing.Screen
import com.shokworks.shokworksdemo.ui.theme.*
import org.koin.androidx.compose.getViewModel

@Composable
fun NewsView( viewModel : NewsViewModel,
              showFavorite : Boolean = false) {

    var newsAndroid by remember {
        mutableStateOf(listOf<ArticleModel>(), policy = neverEqualPolicy())
    }
    var newsIphone by remember {
        mutableStateOf(listOf<ArticleModel>(), policy = neverEqualPolicy())
    }
    var newsFavorite by remember {
        mutableStateOf(listOf<Favorite>(), policy = neverEqualPolicy())
    }

    if (viewModel.isFirstLoading) { // load the list of favorites at the beginning for first time
        viewModel.retrieveFavorites()
        viewModel.isFirstLoading = false
    }

    // connect the changes of news in viewModel to this function
    viewModel.onNewsAndroidUpdated = {
        newsAndroid = it
    }
    viewModel.onNewsIphoneUpdated = {
        newsIphone = it
    }
    viewModel.onFavoriteUpdated = {
        newsFavorite = it
    }

    if (showFavorite) {
        ShowListOfFavorites(viewModel, items = newsFavorite)
    } else {
        FilterRadioGroup(
            viewModel = viewModel,
            newsAndroid = newsAndroid,
            newsIphone = newsIphone
        )
    }
}


@Composable
fun ShowListOfFavorites (viewModel : NewsViewModel, items : List<Favorite>) {
    val focusManager = LocalFocusManager.current

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items) { item ->
            val onItemClick = {
                // go to the selected news
                focusManager.clearFocus()
                navigateTo(
                    destination = Screen.ReadNews,
                    urlSite = item.url,
                    isFavorit = true,   // we are in the list of favorites
                    isInAndroidLst = false
                )
            }
            NewsItem(
                author = item.author,
                title = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = true, onClick = onItemClick)
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun FilterRadioGroup(viewModel: NewsViewModel, newsAndroid : List<ArticleModel>, newsIphone : List<ArticleModel>) {
    val radioButtons = listOf("Android", "iPhone")
    val selectedButton = remember { mutableStateOf(radioButtons.first()) }

    Spacer(modifier = Modifier.padding(top = 8.dp))
    Text(
        text = "See the most recently news about",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h2.copy(
            fontStyle = FontStyle.Normal,
            color = primaryTextColor
        ),
    )
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        radioButtons.forEach { index ->
            val isSelected = index == selectedButton.value
            val colors = RadioButtonDefaults.colors(
                selectedColor = selectedButtonColor,
                unselectedColor = unselectedButtonColor,
                disabledColor = disableButtonColor
            )
            Row( verticalAlignment = Alignment.CenterVertically ) {
                RadioButton(
                    colors = colors,
                    selected = isSelected,
                    onClick = {
                        selectedButton.value = index
                        // keep the keyword and the list which the news belongs
                        viewModel.retrieveNews(index)
                    }
                )
                Text(
                    text = index,
                    style = MaterialTheme.typography.h3.copy(
                        fontStyle = FontStyle.Normal,
                        color = primaryTextColor
                    )
                )
            }
        }
    }

    Divider(
        color = primaryColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        thickness = 2.dp
    )

    val onSelectedNews : ( (String) -> List<ArticleModel> ) = {
        if (it == "Android") {
            newsAndroid
        } else {
            newsIphone
        }
    }

    ShowListOfNews(
        viewModel = viewModel,
        news =   onSelectedNews(selectedButton.value)
    )
}


@Composable
private fun ShowListOfNews(viewModel: NewsViewModel, news: List<ArticleModel>) {
    val focusManager = LocalFocusManager.current

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items (news) {  item ->
            val onItemClick = {
                // visit the url of the selected news and keep in which of the list belongs
                focusManager.clearFocus()
                navigateTo(
                    destination = Screen.ReadNews,
                    urlSite = item.url,
                    isFavorit = false,
                    isInAndroidLst = viewModel.isFromAndroidList
                )
            }
            NewsItem(
                author = item.author,
                title = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = true, onClick = onItemClick)
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}


@Composable
private fun NewsItem(
    modifier: Modifier = Modifier,
    author: String,
    title: String,
    isFavorite: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
    ) {

        RadioButton(
            selected = isFavorite,
            onClick = null
        )

        Text(
            modifier = Modifier.padding(8.dp),
            text = "$title by $author",
            style = if (isFavorite) {
                // style when the news have been selected
                MaterialTheme.typography.h3.copy(
                    fontStyle = FontStyle.Italic,
                    color = secondaryTextColor
                )
            } else {
                // style by default for a news
                MaterialTheme.typography.h3
            },
        )
    }
}



