import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File

//DEPS com.fasterxml.jackson.core:jackson-core:2.10.1, com.fasterxml.jackson.core:jackson-databind:2.10.1, com.fasterxml.jackson.module:jackson-module-kotlin:2.10.1

fun main(args: Array<String>) {

	var data =
			ObjectMapper()
					.registerModule(KotlinModule())
					.readValue(File(args[0]), FloodData::class.java)


	data.regions
			.map { it.regionID }
			.forEachIndexed { index, s ->   println("$index $s") }



}

data class FloodData(
		val regions: List<Region>
)

data class Region(
		val regionID: String,
		val readings: List<Reading>
)

data class Reading(
		val readingID: String,
		val date: String,
		val reading: List<Int>
)