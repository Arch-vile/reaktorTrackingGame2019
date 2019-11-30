import java.io.File

fun main(args: Array<String>) {

    File(args[0]).readBytes()
            .toList()
            .chunked(16)
            .map {
                it.map{  if(it > 32 && it < 125) it else '.'.toByte() }
            }
            .forEach {
                println(String(it.toByteArray()))
            }


}
