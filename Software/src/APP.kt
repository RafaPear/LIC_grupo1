
import TUI.capture
import TUI.clear
import TUI.clearWrite
import TUI.writeCenterLine
import isel.leic.utils.Time
import kotlin.system.exitProcess

object APP {
    private const val ARROW_LEFT  = " <"
    private const val ARROW_RIGHT = "> "
    private const val KEY_LEFT    = "(1)"
    private const val KEY_RIGHT   = "(2)"

    var BETS = listOf<Char>()
    var CREDS: Int = 0
    var GAMES: Int = 0

    var COST = 1 // Custo de cada aposta

    val CONFIRM_KEY = '*'

    var isLoggedIn = false
    var doStats = true
    var sudoMode = false

    fun init() {
        TUI.init()
        CoinAcceptor.init()
        M.init()
    }

    fun run(){
        lobby()
        game()
    }

    fun lobby() {
        writeLobby()
        while (true){
            if (M.inM && !sudoMode){
                m()
                writeLobby()
            }

            if (updateCreds()) writeLobby()

            val key = capture()
            when (key) {
                CONFIRM_KEY -> { if (canStartGame()) break; writeLobby() }
                TUI.NONE -> continue
                else -> {
                    if (sudoMode && canUpdateBets()) BETS += key
                    else if (canUpdateBets()) {
                        BETS += key; CREDS--
                    }; writeLobby()
                }
            }
        }
    }

    fun m(){
        if (!isLoggedIn) {
            if (promptPassword() == M.password) {
                isLoggedIn = true
                clearWrite("LOGGED IN")
                Time.sleep(1000)
            }
            else{
                clearWrite("WRONG PASSWORD")
                Time.sleep(1000)
                if (M.inM)
                    m()
                else
                    isLoggedIn = false
            }
        }// TODO FIX THIS
        while (true){
            val keyA = TUI.waitForCapture(-1)
            clearWrite(keyA.toString())
            val keyB = TUI.waitForCapture(1000)
            TUI.write(keyB)
            var key = keyA.toString()
            key += if (keyB == TUI.NONE) "" else keyB.toString()
            Time.sleep(500)
            if (!M.inM) break
            when(key){
                CONFIRM_KEY.toString() -> { sudoMode = true ; run() ; sudoMode = false}
                "A" -> {
                    coinsMenu.show() ; clear()
                }
                "A*" -> {
                   BETS = emptyList() ; CREDS = 0 ; CoinDeposit.resetTotal(0) ; CoinDeposit.resetTotal(1) ; clearWrite("Reset Completed")
                }
                "D" -> exitProcess(0)
                else -> {}
            }
        }
        isLoggedIn = false
    }

    fun game(bets: List<Char> = BETS) {
        TODO()
    }

    private fun showTotal(total: Int) {
        clear()
        writeCenterLine("Total:")
        writeCenterLine(total.toString(), 1)
        Time.sleep(1000)
    }

    private val coinsMenu = Menu(
        "Coins",
        listOf(
            Page("Total") { showTotal(CoinDeposit.getTotal(0) + CoinDeposit.getTotal(1)) },
            Page("Coin1") { showTotal(CoinDeposit.getTotal(0)) },
            Page("Coin2") { showTotal(CoinDeposit.getTotal(1)) },
        ),
        loop = { M.inM }
    )

    private fun promptPassword():String{
        var password = ""
        var fake = ""
        refresh(
            { writeCenter("Password:") },
            { writeCenter(password) }
        )
        while (password.length < 4){
            val digit = capture()
            if (digit != TUI.NONE) {
                password += digit
                fake += '*'
                refresh(
                    { writeCenterLine("Password:", 0) },
                    { writeCenterLine(fake, 1) }
                )
            }
        }
        return password
    }

    private fun canStartGame(): Boolean{
        if (BETS.isEmpty()){
            clearWrite("You need at least 1 Bet")
            Time.sleep(2000)
            return false
        }

        if(sudoMode) return true

        if (CREDS<COST){
            clearWrite("You need at least 1 Credit")
            Time.sleep(2000)
            return false
        }
        return true
    }

    private fun canUpdateBets(): Boolean {
        if (BETS.size >= 7) {
            clearWrite("Bet Limit reached")
            Time.sleep(2000)
            return false
        }

        if(sudoMode) return true

        if(CREDS <= 0){
            clearWrite("Not enough Credits")
            Time.sleep(2000)
            return false
        }
        return true
    }

    private fun updateCreds(): Boolean {
        val coin = CoinAcceptor.getCoin()
        if (coin != -1) {
            CoinDeposit.updateTotal(coin, 1)
            CREDS += when (coin) {
                0 -> 2
                1 -> 4
                else -> error("Invalid coin type")
            }
            return true
        }
        return false
    }

    private fun writeLobby() {
        val bets = BETS.joinToString(",").trim()
        val creds = " $$CREDS"
        val full = bets + creds
        refresh(
            { writeCenterLine("Roulette Game!") },
            { writeRightLine(full, 1) }
        )
        /*if (full.length <= TUI.COLS){
            refresh(
                { write("Roulette Game!") },
                { writeRight(full) }
            )
        }
        else {
            val newA = bets.subSequence(0, TUI.COLS - creds.length).toString().trim() + creds
            val newB = bets.subSequence(TUI.COLS - creds.length, bets.length).toString().trim() + creds
            refresh(
                { write("Roulette Game!") },
                { writeRight(newA) }
            )
            Time.sleep(500)
            refresh(
                { write("Roulette Game!") },
                { writeRight(newB) }
            )
            Time.sleep(500)
        }*/
    }

    data class Page(val label: String, val action: () -> Unit = {})

    class Menu(
        private val title: String,
        private val pages: List<Page>,
        private val loop: () -> Boolean = { true },        // para sair de menus filhos
    ) {

        fun show() {
            var idx = 0

            fun paint() {
                val isFirst = idx == 0
                val isLast  = idx == pages.lastIndex

                val top  = navLine(title, KEY_LEFT,  KEY_RIGHT,  isFirst, isLast)
                val down = navLine(pages[idx].label, ARROW_LEFT, ARROW_RIGHT, isFirst, isLast)

                clear()
                writeCenterLine(top, 0)
                writeCenterLine(down, 1)
            }

            paint()
            while (loop()) {
                when (val k = capture()) {
                    '1' -> if (idx > 0) { idx--; paint() }
                    '2' -> if (idx < pages.lastIndex) { idx++; paint() }
                    'A' -> { pages[idx].action(); paint() }
                    'B' -> break
                    else -> continue
                }
            }
        }
    }

    /**
     * Limpa a tela LCD, e em seguinda executa o lambda [write1] para escrever na tela, mesma coisa para o [write2]
     * ambos em linhas diferentes
     * @param write1 uma função de extensão do TUI
     * @param write2 uma função de extensão do TUIa
     */
    private fun refresh(write1: TUI.()-> Unit = {},write2: TUI.()-> Unit = {}) {
        clear()
        TUI.write1()
        TUI.nextLine()
        TUI.write2()
    }
}
