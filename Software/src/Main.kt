import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time
import isel.leic.utils.Time
import jdk.internal.org.jline.keymap.KeyMap.key

//fun main() {
//    TUI.init()
//    TUI.capture()
//}
fun main(){
    /*APP.init()*/
    TUI.init()


    // Coin test
    while(true) {
        if(!TUI.confirmMenu("Mais moedas?", "Sim(A)", "Nao(B)")) break
        TUI.loadingScreen { HAL.isBit(pow(2, 6)) }
        TUI.clear()
        val coin = HAL.isBit(pow(2, 5)).toInt()

        while (TUI.capture() != 'A') {
            TUI.write("Coin Detected $coin")
            Time.sleep(1000)
            TUI.clear()
            TUI.write("A to accept")
            Time.sleep(1000)
            TUI.clear()
        }
        HAL.setBits(pow(2, 6))
        while(HAL.isBit(pow(2, 6))){ }
        Time.sleep(1000)
        HAL.clrBits(pow(2, 6))
        TUI.clear()
        TUI.write("Coin Accepted")
        Time.sleep(2000)
        TUI.clear()
        println("coin: $coin")

    }
}
/*
var read = false

thread {
    readln()
    read = true
}
TUI.loadingScreen(500L) { read }
*/
