
import TUI.capture
import TUI.clear
import TUI.clearWrite
import isel.leic.utils.Time
import kotlin.system.exitProcess

object APP {
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
                        BETS += key; CREDS -= COST
                    }; writeLobby()
                }
            }
        }
    }

    fun game(bets: List<Char> = BETS) {
        TODO()
    }

    fun m(){
        doLogIn()
        while (isLoggedIn){
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
                    TODO() ; clear()
                }
                "A*" -> {
                   resetAll()
                }
                "D" -> exitProcess(0)
                else -> {}
            }
        }
        isLoggedIn = false
    }

    private fun resetAll(){
        BETS = emptyList()
        CREDS = 0
        CoinDeposit.resetTotal(0)
        CoinDeposit.resetTotal(1)
        clearWrite("Reset Completed")
    }

    private fun doLogIn(){
        if (!isLoggedIn) {
            if (promptPassword() == M.password) {
                isLoggedIn = true
                clearWrite("LOGGED IN")
                Time.sleep(1000)
            }
            else{
                clearWrite("WRONG PASSWORD")
                Time.sleep(1000)
                if (!M.inM)
                    isLoggedIn = false
            }
        }
    }

    private fun promptPassword():String{
        var password = ""
        var fake = ""
        clearWrite("Pass: $fake")
        while (password.length < 4){
            val digit = capture()
            if (digit != TUI.NONE) {
                password += digit
                fake += '*'
                clearWrite("Pass: $fake")
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

        return true
    }

    private fun canUpdateBets(): Boolean {
        if (BETS.size >= 6) {
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
