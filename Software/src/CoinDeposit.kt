object CoinDeposit {
    private var coins = arrayOf(0, 0) // COIN0, COIN1

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
}