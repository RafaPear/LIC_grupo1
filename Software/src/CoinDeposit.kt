object CoinDeposit {
    private var coins = arrayOf(0, 0) // COIN0, COIN1

    fun init(){
        FileAccess.init()
    }

    fun updateTotal(coin: Int,value: Int) {
        when (coin) {
            0 -> coins[0] += value
            1 -> coins[1] += value
            else -> error("Invalid coin type")
        }
    }

    fun getTotal(coin: Int): Int {
        return when (coin) {
            0 -> coins[0]
            1 -> coins[1]
            else -> error("Invalid coin type")
        }
    }

    fun resetTotal(coin: Int) {
        when (coin) {
            0 -> coins[0] = 0
            1 -> coins[1] = 0
            else -> error("Invalid coin type")
        }
    }

    fun writeToFile() {
        FileAccess.writeToFileA(
            "Total of Coins: ${coins[0] + coins[1]}\n\nTotal of 2 Coins: ${coins[0]}\nTotal of 4 Coins: ${coins[1]}"
        )
    }

    fun closeFileA(){
        FileAccess.closeFileA()
    }
}