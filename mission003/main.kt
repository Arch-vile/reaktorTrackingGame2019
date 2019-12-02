import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File

//DEPS com.fasterxml.jackson.core:jackson-core:2.10.1, com.fasterxml.jackson.core:jackson-databind:2.10.1, com.fasterxml.jackson.module:jackson-module-kotlin:2.10.1

fun main(args: Array<String>) {


    val mapper = ObjectMapper().registerModule(KotlinModule())
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)


    var readValue = mapper.readValue(File(args[0]), FloodData::class.java)
    readValue.regions.forEach { println(it.regionID) }


}

data class FloodData(val regions: List<Region>)
data class Region(val regionID: String)
