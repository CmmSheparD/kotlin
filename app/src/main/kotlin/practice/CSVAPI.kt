package practice

import java.io.FileInputStream
import org.apache.commons.csv.CSVFormat

fun readFromCSV(filename: String): Map<Address, Int> {
    val res = mutableMapOf<Address, Int>()
    CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
        setIgnoreSurroundingSpaces(true)
        setDelimiter(';')
    }.build().parse(FileInputStream(filename).reader())
        .drop(1)
        .forEach {
            val address = Address(it[0], it[1], it[2].toInt(), it[3].toInt())
            val i = res.putIfAbsent(address, 1)
            if (i != null) {
                res[address] = i + 1
            }
        }
    return res
}
