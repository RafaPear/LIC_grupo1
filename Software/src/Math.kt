fun pow(base: Int, exponent: Int): Int{
    var temp = 1
    for (i in 1..exponent){
        temp = temp * base
    }
    return temp
}