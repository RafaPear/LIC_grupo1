object Statistics {
    private var TOTAL_GAMES = 0

    private data class Entry(val id: Int, var total: Int = 0, var creds : Int = 0)

    private const val MAX_ID = 15

    private var SORTED = Array<Entry>(MAX_ID) { Entry(it) }

    fun init(){
        FileAccess.init()
    }

    fun Char.toHexInt(): Int {
        return when (this) {
            in '0'..'9' -> this.digitToInt()
            in 'A'..'F' -> this.code - 'A'.code + 10
            else -> error("Invalid character for hex conversion")
        }
    }

    fun getGames(): Int{
        return TOTAL_GAMES
    }

    fun resetGames(){
        TOTAL_GAMES = 0
    }

    fun updateEntry(entry: Char, addTotal : Int = 0, addCreds : Int = 0) {
        val id = entry.toHexInt()
        if (id <= MAX_ID) {
            SORTED[id].apply { this.total += addTotal ; this.creds += addCreds }
        }
    }

    fun addTotal(amount: Int) {
        TOTAL_GAMES += amount
    }

    fun writeToFile() {
        FileAccess.writeToFileA("Total Games: ${TOTAL_GAMES}\n")
        for (entry in SORTED) {
            FileAccess.writeToFileB("${entry.id};${entry.total};${entry.creds}")
        }
    }

    fun closeFileB(){
        FileAccess.closeFileB()
    }
}