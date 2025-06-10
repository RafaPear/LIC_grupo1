object Statistics {
    private var TOTAL_GAMES = 0

    private data class Entry(val id: Int, var total: Int = 0, var creds : Int = 0)

    private const val MAX_ID = 15

    private var SORTED = Array<Entry>(MAX_ID) { Entry(it) }

    fun init(){
        FileAccess.init()
    }

    fun updateEntry(entry: Int, total: Int = SORTED[entry].total, creds: Int = SORTED[entry].creds) {
        if (entry <= MAX_ID) {
            SORTED[entry].apply { this.total = total ; this.creds = creds }
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