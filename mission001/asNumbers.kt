import java.io.File

fun main(args: Array<String>) {

    File(args[0]).readBytes()
            .forEach {
                println( it )
            }

}