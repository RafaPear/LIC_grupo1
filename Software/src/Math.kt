import kotlin.countOneBits

fun pow(base: Int, exponent: Int): Int{
    var temp = 1
    for (i in 1..exponent){
        temp = temp * base
    }
    return temp
}

fun Int.toBitPlace(): Int{
    return pow(2, this)
}

fun Int.isBit(index: Int) : Boolean{
    return and(1.shl(index)).countOneBits() == 1
}

fun Boolean.toInt(): Int{
    return if (this) 1 else 0
}