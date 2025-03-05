import isel.leic.UsbPort

fun main() {
    while(true) {
        val value = UsbPort.read()
        UsbPort.write(value)
    }
}