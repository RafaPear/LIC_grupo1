import kotlin.concurrent.thread

fun main(){
    HAL.init()
    while (true){
        val key = KBD.waitKeyVal(1000)

        thread {
            if (key == -1) {
                HAL.setBits(0b1000_0000)
                Thread.sleep(50)
                HAL.clrBits(0b1000_0000)
                Thread.sleep(50)
            }
        }

        if (key != -1){
            HAL.writeBits(0b1111_1111, key)
        }
        else {
            HAL.writeBits(0b1111_1111, 0b1000_0000)
        }
    }
}


//HAL.init()
//var value = 0b0000_0001
//var isLeft = true
//while(true) {
////        Thread.sleep(1000)
//    if (HAL.isBit(0b0000_0001)) {
//        HAL.writeBits(0b1111_1111,value.inv())
//        Thread.sleep(100)
//        if (HAL.light == 0b0111_1111) {
//            isLeft = false
//        }
//        if (HAL.light == 0b1111_1110) {
//            isLeft = true
//        }
//        value = if (isLeft) {
//            value.shl(1)
//        } else
//            value.shr(1)
//    }
//    else {
//        HAL.clrBits(0b1111_1111)
//        value = 0b0000_0001
//        isLeft = true
//    }
//}