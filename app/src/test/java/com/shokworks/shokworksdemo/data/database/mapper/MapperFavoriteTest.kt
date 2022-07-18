package com.shokworks.shokworksdemo.data.database.mapper

import com.shokworks.shokworksdemo.domain.Favorite
import com.shokworks.shokworksdemo.networking.model.SourceModel
import com.shokworks.shokworksdemo.data.database.mapper.MapperFavorite.toFavoriteDb
import com.shokworks.shokworksdemo.data.database.model.FavoriteDbModel
import com.shokworks.shokworksdemo.networking.model.ArticleModel

import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class MapperFavoriteTest {
    private lateinit var favorite : Favorite
    private lateinit var article: ArticleModel

    @Before
    fun setUp() {
        favorite = Favorite(
            id = UUID.randomUUID().toString(),
            source = SourceModel("1", "foo"),
            author = "bar",
            title = "baz",
            description =  "quz",
            url = "https://theconversation.com/mafalda-la-filosofa-que-ama-a-los-beatles-y-odia-la-sopa-147271",
            urlToImage = "https://images.theconversation.com/files/361009/original/file-20201001-17-o3xhrl.jpg?ixlib=rb-1.1.0&rect=6%2C0%2C4432%2C2216&q=45&auto=format&w=1356&h=668&fit=crop",
            publishedAt = "2022-01-02",
            content = "quux"
        )

        article = ArticleModel(
            SourceModel("bar", "foo"),
            author = "barz",
            title = "qux",
            description = "quuz",
            url = "https://www.starlab.io",
            urlToImage = "none",
            publishedAt = "corge",
            content = "grault"
        )
    }

    @Test
    fun mapperFavoriteToFavoriteDbTest() {
        val favoriteDb : FavoriteDbModel = favorite.toFavoriteDb()
        assertEquals(favorite.id, favoriteDb.uid, "aren't the same")
    }

    @Test
    fun mapperFavoriteDbToFavoriteTest() {
        val favoriteDb : FavoriteDbModel = favorite.toFavoriteDb()
        val otherFavorite = MapperFavorite.fromFavoriteDbModel(favoriteDb)
        assertEquals(favorite.id, otherFavorite.id, "aren't the same")
    }

    @Test
    fun mapperFavoriteToArticleModelTest()
    {
        val favorite2 = MapperFavorite.fromArticleModel(article)
        assertEquals(favorite2.url, article.url)
        assertEquals(favorite2.title, article.title)
        assertNotEquals("megan", article.author)
    }
}