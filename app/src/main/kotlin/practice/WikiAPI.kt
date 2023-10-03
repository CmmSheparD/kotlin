package practice

import com.google.gson.JsonParser
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

internal class WikiAPI {
    fun searchForArticles(topic: String) = parseResponse(requestArticlesAsJSON(topic))

    fun getArticleURI(article: String): URI = URI.create(
        "https://ru.wikipedia.org/wiki/${article.replace(' ', '_')}"
    )

    private fun requestArticlesAsJSON(topic: String): String {
        val requestText =
            "https://ru.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srsearch=$topic"
        val request = HttpRequest.newBuilder().uri(URI.create(requestText)).build()
        return HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString()).body()
    }

    private fun parseResponse(jsonResponse: String): List<String> {
        return JsonParser.parseString(jsonResponse)
            .asJsonObject.get("query")
            .asJsonObject.get("search")
            .asJsonArray.map { it.asJsonObject.get("title").toString().trim { c -> c == '"' } }
    }
}
