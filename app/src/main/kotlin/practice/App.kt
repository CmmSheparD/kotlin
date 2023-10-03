package practice

import java.awt.Desktop
import java.net.URI
import java.util.Scanner

fun main() {
    val wikiAccess = WikiAPI()
    val topic = getTopicFromUser()
    val articles = wikiAccess.searchForArticles(topic)

    println("Results for \"$topic\":")
    for (i in articles.indices) {
        println("${i + 1}: ${articles[i]}")
    }
    val num = getArticleChoice(articles.size)
    openURI(wikiAccess.getArticleURI(articles[num - 1]))
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

fun openURI(article: URI) {
    Desktop.getDesktop().browse(article)
}
