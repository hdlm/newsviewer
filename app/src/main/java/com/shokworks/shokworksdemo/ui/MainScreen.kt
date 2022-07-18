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

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import com.google.accompanist.pager.ExperimentalPagerApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.google.accompanist.pager.*
import com.shokworks.shokworksdemo.presentation.NewsViewModel
import com.shokworks.shokworksdemo.routing.BackButtonHandler
import com.shokworks.shokworksdemo.routing.DemoRouter
import com.shokworks.shokworksdemo.routing.Screen
import com.shokworks.shokworksdemo.ui.theme.primaryColor
import com.shokworks.shokworksdemo.ui.theme.primaryTextColor
import com.shokworks.shokworksdemo.ui.theme.secondaryTextColor
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

/**
 * This class is responsible to show the Main Screen of the App
 */
@Composable
fun ShowMainScreen() {

    when (val screen = DemoRouter.currentScreen.value) {
        is Screen.Main -> {
            TabLayout()
        }
        is Screen.ReadNews -> {
            ShowWebview()
        }
    }
    BackButtonHandler {
        DemoRouter.goBack()
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout() {

    // variable for pager state
    val pagerState = rememberPagerState(pageCount = 2)

    // we are creating a column for our widgets.
    Column(
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {
        // we are specifying the top app bar and specifying background color for it.
        TopAppBar(backgroundColor = MaterialTheme.colors.background) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
                    border = BorderStroke(1.dp, color = MaterialTheme.colors.onSurface),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, contentDescription = null,
                    )
                }

                Text(
                    text = "Shokworks Demo",
                    style = typography.h1,
                    modifier = Modifier.align(Alignment.Center)
                )

                /*Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
                    border = BorderStroke(1.dp, color = MaterialTheme.colors.onSurface),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete, contentDescription = null,
                    )
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
                    border = BorderStroke(1.dp, color = MaterialTheme.colors.onSurface),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 70.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Save, contentDescription = null,
                    )
                }*/
            }
        }
        // calling tabs
        Tabs(pagerState = pagerState)
        // calling tabs content for displaying our page for each tab layout
        TabsContent(pagerState = pagerState)
    }

}

// creating a function for tabs
@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    // name of the tab and the icon for it
    val list = listOf(
        "News" to Icons.Default.Comment,
        "Favorite" to Icons.Default.Favorite,
    )
    val scope = rememberCoroutineScope()
    // creating a individual row for our tab layout.
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = primaryColor,

        // specifying the indicator for the tab
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = primaryColor
            )
        }
    ) {
        // specifying icon and text for the individual tab item
        list.forEachIndexed { index, _ ->
            // creating a tab
            Tab(
                icon = {
                    Icon(imageVector = list[index].second, contentDescription = null)
                },
                text = {
                    Text(
                        list[index].first,
                        // specifying the text color for the text in that tab
                        color = if (pagerState.currentPage == index) primaryTextColor else  secondaryTextColor
                    )
                },
                // the tab which is selected
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

// creating a tab content method in which we will be displaying the individual page of our tab
@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState, viewModel : NewsViewModel = getViewModel()) {

    with(viewModel) {
        retrieveNews("Android")
        retrieveNews("iPhone")
        retrieveFavorites()
    }

    HorizontalPager(state = pagerState) { page ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (page) {
                0 -> {
                    NewsView(viewModel = viewModel,
                    )
                }
                else -> {
                    NewsView(viewModel = viewModel, showFavorite = true)
                }
            }
        }
    }
}

// creating a Tab Content Screen for displaying a simple text message
@Composable
fun TabContentScreen(data: String) {
    Text(
        text = data,
        style = MaterialTheme.typography.h5,
        color = Color.Green,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}
