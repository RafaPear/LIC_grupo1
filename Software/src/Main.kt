//fun main() {
//    TUI.init()
//    TUI.capture()
//}
fun main(){
    RouletteDisplay.init()
    RouletteDisplay.off(false)
    /*val test = Classe('F', 'D', 'D')
    *//*val test = Classe(1, 2, 3)*//*

    for (i in test) {
        println(i)
        RouletteDisplay.setValue(i)
        RouletteDisplay.cursor++
    }
    RouletteDisplay.update()*/
    while (true)
        RouletteDisplay.animation()

    TUI.init()
    TUI.writeWalkText("O ian e gay")
}
/*
var read = false

thread {
    readln()
    read = true
}
TUI.loadingScreen(500L) { read }
*/
