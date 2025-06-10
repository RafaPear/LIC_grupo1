
import TUI.capture
import TUI.clearWrite
import isel.leic.utils.Time
import kotlin.system.exitProcess

object APP {
    private var BETS = listOf<Char>()
    private var CREDS: Int = 0
    private var SORTED : Char = TUI.NONE
    private var WON: Int = 0

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
    private const val BACK_OR_CLEAR = '#'

    private val invalidBets = charArrayOf('*','#',TUI.NONE)
    private val validBets = charArrayOf(
        '1', '2', '3', 'A', '4', '5', '6', 'B', '7', '8', '9', 'C', '0', 'D'
    )
    private var COST = 1 // Custo de cada aposta

    private const val BONUSBETS = 3
    private const val TIMEBONUS = 5 // tem que ser < 10

    private var sudoMode = false
    private var shouldRun = true

    fun init() {
        CoinAcceptor.init()
        Statistics.init()
        M.init()
        TUI.init()
    }

    fun run(){
        while (shouldRun) {
            lobby()
            game()
            updateStats()
        }
        writeAllStats()
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
                TUI.writeCenterLine("${timer}s ",1)
                timer--
                timing = 0
            }
        }
        val sorted = '5'//validBets.random()
        var wonCredits = 0
        BETS.forEach { if(it == sorted) wonCredits += COST*2 }
        CREDS += wonCredits
        TUI.refresh {
            TUI.writeCenterLine("Sorted: $sorted",0)
            TUI.write("Won: $$wonCredits")
        }
        SORTED = sorted
        WON = wonCredits
        BETS = emptyList()
        RouletteDisplay.clrAll()
        Time.sleep(2000)
    }

    private fun bonusBets(): List<Char>{
        var time_roll = TIMEBONUS
        writeGame(time_roll)
        val bonus = mutableListOf<Char>()
        var timing = 0
        val sleep = 1
        TUI.showCursor(true)
        time_roll--

        while(time_roll >= 0){
            if (bonus.size >= BONUSBETS) break
            if (updateCreds()) TUI.refreshPixels("$CREDS",1,15)
            val key = TUI.capture()

            if (key !in invalidBets && canUpdateBets()) {
                TUI.write(key, false, 1)
                TUI.write(',')

                if (sudoMode && canUpdateBets()) bonus += key
                else if (canUpdateBets()) {
                    bonus += key; CREDS -= COST
                }

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
        writeM()
        while (M.inM){
            val key = TUI.capture()
            when(key){
                CONFIRM_KEY -> {sudoMode = true ; lobby() ; game() ; writeM() ; sudoMode = false}
                KEY_COIN_M -> { coinPage(); writeM() }
                SORTED_NUM_M -> { statsPage() ; writeM() }
                GAME_OFF_M -> {writeAllStats() ; resetAll() ; TUI.clear() ; exitProcess(0) }
            }
        }
    }

    private fun statsPage(){
        val list = Statistics.getSortedList()
        val page = Page(list)
        page.run {key, clear ->
            when (key) {
                CONFIRM_KEY -> {
                    Statistics.resetAll()
                    clear()
                    M.inM
                }
                BACK_OR_CLEAR -> {
                    false
                }
                else -> M.inM
            }
        }
    }

    private fun coinPage() {
        val lines = listOf(
            "2 Coins: ${CoinDeposit.getTotal(0)}",
            "4 Coins: ${CoinDeposit.getTotal(1)}",
            "Total: ${CoinDeposit.getTotal(0) + CoinDeposit.getTotal(1)}",
            "Games: ${Statistics.getGames()}",
            "# to exit",
            "* to reset"
        )

        val page = Page(lines)

        page.run{ key, func ->
            when (key) {
                CONFIRM_KEY -> {
                    resetAll()
                    Time.sleep(1000)
                    func()
                    M.inM
                }
                BACK_OR_CLEAR -> {
                    false
                }
                else -> M.inM
            }
        }
    }

    private fun writeM(){
        TUI.clear()
        TUI.writeCenterLine("Manager Mode", 0)
        TUI.writeCenterLine("Active", 1)
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
        Statistics.resetGames()
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


    private fun updateStats(){
        if (sudoMode) return
        Statistics.addTotal(1)
        if (SORTED != TUI.NONE) {
            Statistics.updateEntry(SORTED, 1, WON)
        }
        SORTED = TUI.NONE
        WON = 0
    }

    private fun writeAllStats(){
        Statistics.writeToFile()
        CoinDeposit.writeToFile()
        Statistics.closeFileB()
        CoinDeposit.closeFileA()
    }

    private class Page(
        val lines: List<String>,
        val upKey : Char = 'A',
        val downKey : Char = 'B',
        val upSym : String = """/\""",
        val downSym : String = """\/""",
    ) {

        val line1MAX = TUI.COLS-(upSym.length+2)
        val line2MAX = TUI.COLS-(downSym.length+2)

        private fun write(idx: Int, idx2: Int){
            val line1 = lines[idx].padEnd(line1MAX, ' ')
            val line2 = lines[idx2].padEnd(line2MAX, ' ')
            if (line1.length <= line1MAX) TUI.refreshPixels (line1, 0, 0)
            else error("Very Big, line length should be less than $line1MAX")
            if (line2.length <= line2MAX) TUI.refreshPixels (line2, 1, 0)
            else error("Very Big, line length should be less than $line2MAX")
        }

        fun run(func: (key: Char, reset: () -> Unit)->Boolean) {

            TUI.clear()
            val size = 2
            var idx = 0
            var idx2 = 1

            write(idx, idx2)

            TUI.refreshPixels(upSym+upKey, 0, line1MAX)
            TUI.refreshPixels(downSym+downKey, 1, line2MAX)

            while (true) {
                val key = TUI.capture()
                if (!func(key){ write(idx, idx2) }) break
                when(key){
                    upKey -> {
                        idx2 = idx
                        idx = capInside(idx-1, 0, lines.size - 1)

                        write(idx, idx2)
                    }
                    downKey -> {
                        idx = idx2
                        idx2 = capInside(idx+1, 0, lines.size - 1)
                        write(idx, idx2)
                    }
                }
            }
        }
    }
}
