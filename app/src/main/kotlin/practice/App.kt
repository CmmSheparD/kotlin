package practice

import java.nio.file.Path
import java.rmi.UnexpectedException
import java.time.Duration.between
import java.time.Instant
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.notExists

fun main() {
    while (true) {
        val path = getPathOrExit() ?: break

        val startMoment = Instant.now()
        val entries = when (path.extension) {
            "csv" -> readFromCSV(path.toString())
            "xml" -> readFromXML(path.toString())
            else -> throw UnexpectedException("How did you even manage to slip this in?")
        }
        val fileReadMoment = Instant.now()
        val citiesStats = mutableMapOf<String, MutableList<Int>>()
        val repeats = mutableListOf<Map.Entry<Address, Int>>()
        processData(entries, citiesStats, repeats)
        val finalMoment = Instant.now()

        println("Repeating entries:")
        for (rep in repeats) {
            println("${rep.key} was met ${rep.value} times.")
        }
        if (repeats.isEmpty())
            println(" [No entries]")

        println()
        println("Cities stats:")
        for (city in citiesStats) {
            println("${city.key} has:")
            println(" One-story buildings:   ${city.value[0]}")
            println(" Two-story buildings:   ${city.value[1]}")
            println(" Three-story buildings: ${city.value[1]}")
            println(" Four-story buildings:  ${city.value[1]}")
            println(" Five-story buildings:  ${city.value[1]}")
        }
        println("File read in ${between(startMoment, fileReadMoment).toMillis()} ms.")
        println("Data processed in ${between(fileReadMoment, finalMoment).toMillis()}ms.")
        println("Total time worked: ${between(startMoment, finalMoment).toMillis()}ms.")
    }
}

fun getPathOrExit(): Path? {
    var path: Path? = null
    while (path == null) {
        print("Enter path or type \"exit\" to quit the program: ")
        val input = readln()

        if (input == "exit") {
            break
        } else if (!input.endsWith(".csv", true) && !input.endsWith(".xml", true)) {
            println("Wrong file format: only CSV and XML are supported.")
            continue
        }
        path = Path(input).toAbsolutePath().normalize()

        if (path.notExists()) {
            println("No such path exists!")
            path = null
        }
    }
    return path
}

fun processData(
    entries: Map<Address, Int>,
    citiesStats: MutableMap<String, MutableList<Int>>,
    repeats: MutableList<Map.Entry<Address, Int>>
) {
    for (address in entries) {
        if (address.value > 1)
            repeats += address
        if (address.key.city !in citiesStats) {
            citiesStats[address.key.city] = MutableList(5) {
                if (it == address.key.floor - 1)
                    1
                else
                    0
            }
        } else {
            ++citiesStats[address.key.city]!![address.key.floor - 1]
        }
    }
}
