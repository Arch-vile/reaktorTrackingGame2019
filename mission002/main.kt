import java.io.File


fun main(args: Array<String>) {
    var lines = File(args[0]) .readLines()

    var dataLines = lines
            .filter {
                it.contains('#') ||
                        it.contains("time") ||
                        it.contains("\"id\"") ||
                        it.contains("\"date\"")
            }

    val samples = mutableListOf<Sample>()
    var currentSample = Sample()
    for (dataLine in lines) {

        if (dataLine.contains("time")) {

            if (currentSample.time != -1) {
                samples.add(currentSample)
                currentSample = Sample()
            }

            currentSample.time = """[0-9]+""".toRegex().find(dataLine)?.value?.toInt()!!
        }
    }
    samples.add(currentSample)

    samples.forEach{println(it)}


}

data class Sample(var date: String = "", var time: Int = -1, var id: String = "", var anomalies: List<Anomaly> = listOf())
data class Anomaly(var color: String = "", var varue: Int = -1)


fun readFileToInts(args: Array<String>) =
    File(args[0])
            .readText()
            .split(" ")
            .map { Integer.parseInt(it, 2) }

