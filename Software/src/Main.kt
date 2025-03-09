import isel.leic.UsbPort

fun main(){
    HAL.init()
    while(true) {
//        Thread.sleep(1000)
        if (HAL.isBit(0b0000_1000))
            HAL.setBits(0b1111_1111)
        else HAL.clrBits(0b1111_1111)
    }
}