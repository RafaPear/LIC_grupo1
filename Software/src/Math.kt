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