import java.io.File


fun main(args: Array<String>) {
    var lines = File(args[0]) .readLines()
    var dataLines = lines
            .filter {
                it.contains('#') || it.contains("\"id\"") || it.contains("contaminants")
            }

    val samples = mutableListOf<Reading>()
    var currentReading = Reading()
    for (dataLine in dataLines) {

        if (dataLine.contains("contaminants") && !currentReading.contaminants.isEmpty()){
            samples.add(currentReading)
            currentReading = Reading()
        }

        if (dataLine.contains("id") && currentReading.id != null) {
            samples.add(currentReading)
            currentReading = Reading()
        }

        if (dataLine.contains("id")) currentReading.id = dataLine

        if (dataLine.contains('#'))
            currentReading.contaminants.add(Anomaly(
                    """\".*\"""".toRegex().find(dataLine)?.value!!,
                    """: ([0-9]+)""".toRegex().find(dataLine)?.groups!![1]?.value?.toInt()!!
            ))

    }
    samples.add(currentReading)

samples.map {
    Pair(it.id, it.contaminants.map { c -> c.value }.sumBy { v -> v!! })
}
        .forEach{ println("${it.first}\t${it.second}")}


}

data class Reading(var id: String? = null, val contaminants: MutableList<Anomaly> = mutableListOf())

data class Sample(var date: String = "", var time: Int = -1, var id: String = "", var anomalies: MutableList<Anomaly> = mutableListOf()) {
    fun isComplete(): Boolean {
        return !date.isBlank() && time != -1 && !id.isBlank() && !anomalies.isEmpty()
    }
}

data class Anomaly(var color: String?, var value: Int?)


fun readFileToInts(args: Array<String>) =
    File(args[0])
            .readText()
            .split(" ")
            .map { Integer.parseInt(it, 2) }

