package practice

import com.google.gson.JsonParser

import java.awt.Desktop
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.Scanner


fun main() {
    val topic = getTopicFromUser()
    val articles = searchForArticles(topic)

    println("Results for \"$topic\":")
    for (i in articles.indices) {
        println("${i + 1}: ${articles[i]}")
    }
    val num = getArticleChoice(articles.size)
    openArticle(articles[num - 1])
}

fun getTopicFromUser(): String {
    var topic = ""
    while (topic.isBlank()) {
        print("Enter a topic to search for: ")
        topic = readln()
    }
    return topic.trim()
}

fun getArticleChoice(maxNumber: Int): Int {
    var num: Int
    val scanner = Scanner(System.`in`)
    while (true) {
        print("Choose an article(1-$maxNumber): ")
        num = try {
            scanner.nextInt()
        } catch (e: java.util.InputMismatchException) {
            println("Input must be a number.")
            0
        }
        if (num in 1..maxNumber)
            break
        else
            println("Number must be in range 1-$maxNumber.")
    }
    return num
}

fun searchForArticles(topic: String) = parseResponse(requestArticlesAsJSON(topic))

fun requestArticlesAsJSON(topic: String): String {
    val requestText = "https://ru.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srsearch=$topic"
    val request = HttpRequest.newBuilder().uri(URI.create(requestText)).build()
    return HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString()).body()
}

fun parseResponse(jsonResponse: String): List<String> {
    return JsonParser.parseString(jsonResponse)
        .asJsonObject.get("query")
        .asJsonObject.get("search")
        .asJsonArray.map { it.asJsonObject.get("title").toString().trim { c -> c == '"' } }
}

fun openArticle(article: String) {
    val articleURI = "https://ru.wikipedia.org/wiki/${article.replace(' ', '_')}"
    Desktop.getDesktop().browse(URI.create(articleURI))
}
