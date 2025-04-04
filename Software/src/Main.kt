import isel.leic.utils.Time

fun main() {
	LCD.init()

	var canWrite = true
	var lastKey = KBD.NONE

	while (true){
		var key = KBD.getKey()
		println(key)

		if (key == '*') {
			LCD.clear()
		}
		else if (canWrite && key != KBD.NONE) {
			LCD.write(key)
			/*key = KBD.NONE*/
			canWrite = false
		}
		else if (key == KBD.NONE)
			canWrite = true

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
