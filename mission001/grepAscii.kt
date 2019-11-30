import java.io.File

fun main(args: Array<String>) {

    File(args[0]).readBytes()
            .filter { it > 32 && it < 125 }
            .forEach {
                print( it.toChar() )
            }

}