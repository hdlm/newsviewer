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

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.shokworks.shokworksdemo.routing.BackButtonHandler
import com.shokworks.shokworksdemo.routing.DemoRouter
import com.shokworks.shokworksdemo.routing.Screen
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.shokworks.shokworksdemo.presentation.NewsViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun ShowWebview( viewModel : NewsViewModel = getViewModel()) {
    val setUrl : (String) -> String = { url ->
        if (url.isNotEmpty()) url
        else "https://shokworks.io/"
    }

    val isFavorites by remember { mutableStateOf(DemoRouter.isFavorite.value) }
    var sUrl by remember { mutableStateOf( setUrl.invoke(DemoRouter.urlSelected.value)) }
    var actionBtnDisable by remember { mutableStateOf (false) }

    /**
     * Choose the Image Vector for the icon to display (favorite or delete)
     */
    val onChooseImageVector : ( (Boolean ) -> ImageVector) = { isFavorite ->
        if (!isFavorite) {
            Icons.Default.Favorite
        } else {
            Icons.Default.Delete
        }
    }

    /**
     * The method is responsible for carrying out the operation of saving a news item in _Favorites_
     */
    val onSave : onDoAction = {  url ->
        viewModel.saveNewsAsFavorite(url)
    }

    /**
     * the method is responsible for carrying out the operation of deleting a news item in favorites
     */
    val onRemove : onDoAction = { id ->
        viewModel.removeFromFavorites( id )
    }

    val onDoNothing : onDismissType = { }

    /**
     * The method determines the action to be taken depending on whether it is a current favorite or not
     */
    val onChoosingAction :  onChooseAction = { id, isFavorite ->
        if (id.isNotEmpty()) {
            if (!isFavorite) {
                onSave.invoke(id)
            } else {
                onRemove.invoke(id)
            }
            actionBtnDisable = !actionBtnDisable
        }
    }

    if (DemoRouter.urlSelected.value.isNotEmpty())
        sUrl = DemoRouter.urlSelected.value

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.background)
        ) {
            // add back button
            Button(
                onClick = { DemoRouter.navigateTo(destination = Screen.Main) },
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
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )

            // add the action button
            ActionButton(
                sUrl = sUrl,
                isFavorite = isFavorites,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 5.dp),
                imageVector =  onChooseImageVector(isFavorites),
                onAction = onChoosingAction,
                disable = actionBtnDisable
            )
        }

        // a WebView inside AndroidView with layout as full screen
        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                loadUrl(sUrl)
            }
        }, update = {
            it.loadUrl(sUrl)
        })

        BackButtonHandler {
            DemoRouter.navigateTo(Screen.Main)
        }

    }

}

@Composable
fun ActionButton(sUrl:String,
                 isFavorite: Boolean,
                 modifier : Modifier,
                 imageVector: ImageVector,
                 onAction : onChooseAction,
                 disable : Boolean) {
    if (!disable) {
        Button(
            onClick = { onAction.invoke(sUrl, isFavorite) },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
            border = BorderStroke(1.dp, color = MaterialTheme.colors.background),
            modifier = modifier
        ) {
            Icon(
                imageVector = imageVector, contentDescription = null,
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShowWebview()
}

