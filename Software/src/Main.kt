import isel.leic.utils.Time
import kotlin.concurrent.thread

fun main(){
    LCD.init()

    var read = ""

    thread {
        while (read.isEmpty()){
            read = readln()
        }
    }

    LCD.loadingScreen(500L) { read != "exit" }
}