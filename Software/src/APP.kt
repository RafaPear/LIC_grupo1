
import TUI.capture
import TUI.clear
import TUI.clearWrite
import isel.leic.utils.Time
import kotlin.system.exitProcess

object APP {
    var BETS = listOf<Char>()
    var CREDS: Int = 0
    var GAMES: Int = 0
    var SORTED = listOf<Char>()
    /**
     * Corresponde à primeira linha da tela LCD
     */
    private var line1 = "Roulette Game"

    /**
     * Corresponde à segunda linha da tela do LCD
     */
    private var line2 = "Start the Game"

    private const val CONFIRM_KEY = '*'
    private const val KEY_COIN_M = 'A'
    private const val SORTED_NUM_M = 'C'
    private const val GAME_OFF_M = 'D'

    private val invalidBets = charArrayOf('*','#',TUI.NONE)
    private val validBets = charArrayOf(
        '1', '2', '3', 'A', '4', '5', '6', 'B', '7', '8', '9', 'C', '0', 'D'
    )
    var COST = 1 // Custo de cada aposta

    private const val BONUSBETS = 3
    private const val TIMEBONUS = 5 // tem que ser < 10
    var doStats = true
    var sudoMode = false

    fun init() {
        CoinAcceptor.init()
        Statistics.init()
        M.init()
        TUI.init()
        TUI.clear()
    }

    fun run(){
        lobby()
        game()
        updateStats()
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

    fun game() {
        BETS += bonusBets()

        var timer = (3..10).random()
        TUI.refresh {
            writeCenterLine("ROLL!",0)
            writeCenterLine("${timer}s",1)
        }
        timer--
        var timing = 0
        val sleep = 1
        while (timer >= 0) {
            RouletteDisplay.animation()
            timing++
            //animation dura cerca 500 ms para ter 1 s é 2*sleep + animation time
            if (timing >= 2*sleep) {
                TUI.writeCenterLine("${timer}s",1)
                timer--
                timing = 0
            }
        }
        GAMES++
        val sorted = '5'//validBets.random()
        var wonCredits = 0
        BETS.forEach { if(it == sorted) wonCredits += COST*2 }
        CREDS += wonCredits
        TUI.refresh {
            TUI.writeCenterLine("Sorted: $sorted",0)
            TUI.write("Won: $$wonCredits")
        }
        RouletteDisplay.clrAll()
        Time.sleep(2000)
    }

    private fun bonusBets(): List<Char>{
        var time_roll = TIMEBONUS
        writeGame(time_roll)
        var bonus = listOf<Char>()
        var timing = 0
        val sleep = 1
        TUI.showCursor(true)
        time_roll--

        while(time_roll >= 0){
            if (bonus.size >= BONUSBETS) break
            if (updateCreds()) TUI.refreshPixels("$CREDS",1,15)
            val key = TUI.capture()

            if (key !in invalidBets && CREDS != 0) {
                TUI.write(key, false, 1)
                TUI.write(',')
                bonus += key
                CREDS -= COST
                TUI.writeRightLine(" $$CREDS",1)
            }

            Time.sleep(sleep.toLong())

            timing++
            if (timing >= 1000*sleep) {
                TUI.writeRightLine("${time_roll}s")
                time_roll--
                timing = 0
            }
        }
        TUI.showCursor(false)
        return bonus
    }
    private fun writeGame(time_roll: Int) {
        line1 = "Bonus bets!"
        TUI.clear()
        TUI.write(line1)

        TUI.writeRightLine("${time_roll}s",0)

        TUI.write("bets:",false,1,0)
        TUI.writeRightLine("$$CREDS",1)
    }

    private fun m() {
        login_M()
        TUI
        while (M.inM){
            val key = TUI.capture()
            when(key){
                CONFIRM_KEY -> game()
                KEY_COIN_M -> TODO()
                SORTED_NUM_M -> TODO()
                GAME_OFF_M -> TODO()
            }
        }
    }
    private fun login_M() {
        var input = ""
        var inputCripted = ""
        line1 = "Login"
        line2 = "Password: "

        TUI.refresh(
            { writeCenterLine(line1, 0) },
            { write(line2) }
        )

        TUI.showCursor(true)

        while(input != M.password){
            if (!M.inM) break

            val key = TUI.capture()

            if (key == '#' && input != ""){
                input = input.dropLast(1)
                inputCripted = inputCripted.dropLast(1)
                TUI.clearChar()
            }
            else if (TUI.isValid(key) && key !in invalidBets ) {
                val temp_input = input + key
                if (temp_input.length <= M.password.length) {
                    input = temp_input
                    inputCripted += '*'
                    TUI.write('*')
                }
                if (input == M.password) break
            }
            //importante evita TUI.refresh desnecessários
            if (input.length == M.password.length) {
                if (line1 == "Login"){
                    line1 = "Invalid password"
                    TUI.refresh(
                        { writeCenterLine(line1, 0) },
                        { write(line2 + inputCripted) }
                    )
                }
            } else {
                if (line1 != "Login"){
                    line1 = "Login"
                    TUI.refresh(
                        { writeCenterLine(line1, 0) },
                        { write(line2 + inputCripted) }
                    )
                }
            }
            Time.sleep(10)
        }
        TUI.clear()
        TUI.showCursor(false)
    }
    private fun resetAll(){
        BETS = emptyList()
        CREDS = 0
        CoinDeposit.resetTotal(0)
        CoinDeposit.resetTotal(1)
        clearWrite("Reset Completed")
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
        TUI.refresh(
            { writeCenterLine("Roulette Game!") },
            { writeRightLine(full, 1) }
        )
    }

    fun updateStats(){
        if (sudoMode || !doStats) return
        Statistics.addTotal(1)
    }

    fun writeAllStats(){
        Statistics.writeToFile()
        CoinDeposit.writeToFile()
        Statistics.closeFileB()
        CoinDeposit.closeFileA()
    }

    /**
     * Limpa a tela LCD, e em seguida executa o lambda [write1] para escrever na tela, mesma coisa para o [write2]
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
