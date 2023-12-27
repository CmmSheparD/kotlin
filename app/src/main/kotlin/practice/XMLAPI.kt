package practice

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import java.io.FileInputStream
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants.START_ELEMENT

fun readFromXML(filename: String): Map<Address, Int> {
    val res = mutableMapOf<Address, Int>()
    val reader = XMLInputFactory.newFactory().createXMLStreamReader(FileInputStream(filename))
    val mapper = XmlMapper.builder()
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
        .build()
    mapper.registerModule(
        KotlinModule.Builder()
            .enable(KotlinFeature.StrictNullChecks)
            .build()
    )
    reader.next()
    while (reader.hasNext()) {
        reader.next()
        if (reader.eventType == START_ELEMENT) {
            val address = mapper.readValue(reader, Address::class.java)
            val i = res.putIfAbsent(address, 1)
            if (i != null) {
                res[address] = i + 1
            }
        }
    }
    reader.close()
    return res
}
