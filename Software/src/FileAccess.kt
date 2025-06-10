
import java.io.File
import java.io.PrintWriter

object FileAccess {
    const val FILE_A_NAME = "info.txt"
    const val FILE_B_NAME = "statistics.txt"

    lateinit var fileA: PrintWriter
    lateinit var fileB: PrintWriter

    fun init(){
        fileA = File(FILE_A_NAME).printWriter()
        fileB = File(FILE_B_NAME).printWriter()
    }

    fun writeToFileA(str: String) {
        fileA.println(str)
    }

    fun writeToFileB(str: String) {
        fileB.println(str)
    }

    fun closeFileA(){
        fileA.close()
    }

    fun closeFileB(){
        fileB.close()
    }
}