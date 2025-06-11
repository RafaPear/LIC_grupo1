
import Statistics.toHexInt
import TUI.capture
import TUI.clearWrite
import TUI.createCustomChar
import TUI.writeCenterLine
import isel.leic.utils.Time
import kotlin.system.exitProcess

object APP {
    private val invalidBets = charArrayOf('*','#',TUI.NONE)
    private val validBets = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D'
    )

    private var BETS = mutableMapOf<Char, Int>()

    private var CREDS: Int = 0
    private var SORTED : Char = TUI.NONE
    private var WON: Int = 0

    private var BET_LIMIT = 9 // Limite de apostas por jogo

    private var lastCoin: Int = 0 // Guarda o último valor do coin acceptor

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

    private var COST = 1 // Custo de cada aposta

    private const val BONUSBETS = 3
    private const val TIMEBONUS = 5 // tem que ser < 10

    private var sudoMode = false
    private var shouldRun = true

    val pila = arrayOf(
        intArrayOf(0, 1, 0, 1, 0 ),
        intArrayOf(1, 1, 1, 1, 1 ),
        intArrayOf(0, 1, 1, 1, 0 ),
        intArrayOf(0, 1, 1, 1, 0 ),
        intArrayOf(0, 1, 1, 1, 0 ),
        intArrayOf(0, 1, 1, 1, 0 ),
        intArrayOf(0, 0, 1, 0, 0 ),
        intArrayOf(0, 0, 0, 0, 0 ),
    )

    // ⬆️  Seta para cima
    val arrowUp = arrayOf(
        intArrayOf(0,0,0,0,0),
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(0,1,1,1,0), // 0x0E
        intArrayOf(1,0,1,0,1), // 0x15
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(0,0,0,0,0) // 0x04
    )

    // ⬇️  Seta para baixo
    val arrowDown = arrayOf(
        intArrayOf(0,0,0,0,0), // 0x04
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(1,0,1,0,1), // 0x15
        intArrayOf(0,1,1,1,0), // 0x0E
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(0,0,0,0,0)  // 0x00
    )

    // 💰 Moeda “2 créditos” – 2 quadradinhos no interior
    val coin2 = arrayOf(
        intArrayOf(0,0,0,0,0), // 0x0E
        intArrayOf(0,0,0,0,0), // 0x11
        intArrayOf(1,1,1,1,1), // 0x1B
        intArrayOf(1,0,0,1,1), // 0x1F
        intArrayOf(1,0,0,0,1), // 0x19
        intArrayOf(1,1,0,0,1), // 0x11
        intArrayOf(1,1,1,1,1), // 0x0E
        intArrayOf(0,0,0,0,0)  // 0x00
    )

    val coin4BIG_A = arrayOf(
        intArrayOf(0,1,1,1,0), // 0x0E
        intArrayOf(1,0,0,0,1), // 0x11
        intArrayOf(1,1,0,1,1), // 0x1B
        intArrayOf(1,1,1,1,1), // 0x1F
        intArrayOf(1,1,0,0,1), // 0x19
        intArrayOf(1,0,0,0,1), // 0x11
        intArrayOf(0,1,1,1,0), // 0x0E
        intArrayOf(0,0,0,0,0)  // 0x00
    )

    val coin4BIG_B = arrayOf(
        intArrayOf(0,1,1,0,0), // 0x0E
        intArrayOf(1,0,0,1,0), // 0x11
        intArrayOf(1,0,1,0,1), // 0x1B
        intArrayOf(1,0,1,1,1), // 0x1F
        intArrayOf(1,0,0,1,1), // 0x19
        intArrayOf(0,1,0,0,1), // 0x11
        intArrayOf(0,0,1,1,0), // 0x0E
        intArrayOf(0,0,0,0,0)  // 0x00
    )

    val coin4BIG_C = arrayOf(
        intArrayOf(0,0,1,0,0), // 0x0E
        intArrayOf(0,0,1,0,0), // 0x11
        intArrayOf(0,0,1,0,0), // 0x1B
        intArrayOf(0,0,1,0,0), // 0x1F
        intArrayOf(0,0,1,0,0), // 0x19
        intArrayOf(0,0,1,0,0), // 0x11
        intArrayOf(0,0,1,0,0), // 0x0E
        intArrayOf(0,0,0,0,0)  // 0x00
    )

    val coin4BIG_D = arrayOf(
        intArrayOf(0,1,1,0,0).reversed().toIntArray(), // 0x0E
        intArrayOf(1,0,0,1,0).reversed().toIntArray(), // 0x11
        intArrayOf(1,0,1,0,1).reversed().toIntArray(), // 0x1B
        intArrayOf(1,0,1,1,1).reversed().toIntArray(), // 0x1F
        intArrayOf(1,0,0,1,1).reversed().toIntArray(), // 0x19
        intArrayOf(0,1,0,0,1).reversed().toIntArray(), // 0x11
        intArrayOf(0,0,1,1,0).reversed().toIntArray(), // 0x0E
        intArrayOf(0,0,0,0,0).reversed().toIntArray()  // 0x00
    )

    // 💰 Moeda “4 créditos” – 4 quadradinhos no interior
    val coin4_A = arrayOf(
        intArrayOf(0,0,0,0,0), // 0x0E
        intArrayOf(0,0,0,0,0), // 0x11
        intArrayOf(0,1,1,1,0), // 0x1B
        intArrayOf(1,0,1,0,1), // 0x1F
        intArrayOf(1,1,0,1,1), // 0x19
        intArrayOf(1,0,1,0,1), // 0x11
        intArrayOf(0,1,1,1,0), // 0x0E
        intArrayOf(0,0,0,0,0)  // 0x00
    )
    // 💰 Moeda “4 créditos” – 4 quadradinhos no interior
    val coin4_B = arrayOf(
        intArrayOf(0,0,0,0,0), // 0x0E
        intArrayOf(0,0,0,0,0), // 0x11
        intArrayOf(0,0,1,0,0), // 0x1B
        intArrayOf(0,1,1,1,0), // 0x1F
        intArrayOf(0,1,0,1,0), // 0x19
        intArrayOf(0,1,1,1,0), // 0x11
        intArrayOf(0,0,1,0,0), // 0x0E
        intArrayOf(0,0,0,0,0)  // 0x00
    )
    // 💰 Moeda “4 créditos” – 4 quadradinhos no interior
    val coin4_C = arrayOf(
        intArrayOf(0,0,0,0,0), // 0x0E
        intArrayOf(0,0,0,0,0), // 0x11
        intArrayOf(0,0,1,0,0), // 0x1B
        intArrayOf(0,0,1,0,0), // 0x1F
        intArrayOf(0,0,1,0,0), // 0x19
        intArrayOf(0,0,1,0,0), // 0x11
        intArrayOf(0,0,1,0,0), // 0x0E
        intArrayOf(0,0,0,0,0)  // 0x00
    )

    fun init() {
        CoinAcceptor.init()
        Statistics.init()
        CoinDeposit.init()
        M.init()

        RouletteDisplay.init()
        TUI.init()

        createCustomChar(0, arrowUp)
        createCustomChar(1, arrowDown)
        createCustomChar(2, coin2)
        createCustomChar(3, coin4_A)
        createCustomChar(4, coin4_B)
        createCustomChar(5, coin4_C)
    }

    fun run(){
        while (shouldRun) {
            lobby()
            game()
            updateStats()
        }
        writeAllStats()
    }

    fun animTest(){
        //val list = listOf(3, 4, 5, 4, 3)

        createCustomChar(3, coin4BIG_A)
        createCustomChar(4, coin4BIG_B)
        createCustomChar(5, coin4BIG_C)
        createCustomChar(6, coin4BIG_D)
        val list = listOf(3, 4, 5, 6, 3)
        while (true) {
            for (i in list) {
                Time.sleep(100)
                TUI.refreshPixel(i.toChar(), 0, 0)
            }
        }
    }

    fun lobby() {
        fun writeLobbyCreds(){
            val creds = " $$CREDS"
            TUI.refreshPixels(creds, 1, TUI.COLS-creds.length)
        }

        fun writeLobby(doCreds : Boolean = false) {
            writeCenterLine("Roulette Game!")
            if (doCreds) writeLobbyCreds()
        }
        TUI.clear()
        RouletteDisplay.clrAll()
        writeLobby(true)
        RouletteDisplay.clrAll()

        val delay = 5000L

        var endTime = Time.getTimeInMillis() + delay

        while (true){
            if (Time.getTimeInMillis() >= endTime) {
                RouletteDisplay.animationC()
                endTime = Time.getTimeInMillis() + delay
            }

            if (M.inM && !sudoMode){
                m()
                writeLobby(true)
            }

            if (updateCreds()) {
                RouletteDisplay.animationD(if (lastCoin == 0) 1 else 2)
                writeLobbyCreds()
            }

            if (capture() == CONFIRM_KEY) break

        }
        TUI.clear()
        doBets()
    }

    private fun doBets(){
        RouletteDisplay.clrAll()
        var tempLine2 = validBets.joinToString("")

        fun writeBets(writeBottom: Boolean = false){
            TUI.clear()
            for (bet in BETS) {
                TUI.refreshPixel(
                    if(bet.value != 0) bet.value.digitToChar() else ' ',
                    0, bet.key.toHexInt() + 1
                )
            }
            if (writeBottom) writeCenterLine(tempLine2, 1)
            RouletteDisplay.printIntList(CREDS.toString().toList().map { it.toHexInt() }.reversed())
        }

        fun writeBet(key: Char){
            val bet = BETS[key]
            TUI.refreshPixel(
                if(bet != 0 && bet != null) bet.digitToChar() else ' ',
                0, key.toHexInt()  + 1
            )
            RouletteDisplay.printIntList(CREDS.toString().toList().map { it.toHexInt() }.reversed())
        }

        RouletteDisplay.clrAll()
        writeBets(true)

        val delay = 5000L

        var endTime = Time.getTimeInMillis() + delay
        while (true){

            if (M.inM && !sudoMode){
                m()
                writeBets(true)
            }

            if (updateCreds()) {
                RouletteDisplay.animationD(if (lastCoin == 0) 1 else 2)
                RouletteDisplay.printIntList(CREDS.toString().toList().map { it.toHexInt() }.reversed())
            }

            val key = capture()
            when (key) {
                CONFIRM_KEY -> { if (canStartGame()) break; writeBets() }
                TUI.NONE -> continue
                else -> {
                    if(updateBets(key)) writeBet(key) else writeBets(true)
                }
            }
        }
        TUI.clear()
        doLobbyEndAnimation()
    }

    fun updateBets(key: Char): Boolean {
        if (sudoMode && canUpdateBets(key = key)) {
            BETS.computeIfPresent(key){k, v ->
                v + 1
            }
            BETS.computeIfAbsent(key){k -> 1 }
            return true
        }
        else if (canUpdateBets(key = key)) {
            BETS.computeIfPresent(key){k, v ->
                v + 1
            }
            BETS.computeIfAbsent(key){k -> 1 }
            CREDS -= COST
            return true
        }
        return false
    }

    fun doLobbyEndAnimation() {
        when (BETS.size) {
            1 -> {
                TUI.writeCenterLine("Really? 1?", 0)
                Time.sleep(2000)
                TUI.clear()
            }
            2 -> {
                TUI.writeCenterLine("Only That?", 0)
                RouletteDisplay.animationD(1)
                Time.sleep(2000)
                TUI.clear()
            }
            3 -> {
                TUI.writeCenterLine("Ok, 3 is fine", 0)
                RouletteDisplay.animationD(2)
                Time.sleep(2000)
                TUI.clear()
            }
            4 -> {
                TUI.writeCenterLine("Lets Go!", 0)
                RouletteDisplay.animationB()
                TUI.clear()
            }
            5 -> {
                TUI.writeCenterLine("OMG OMG!!", 0)
                RouletteDisplay.animationB()
                TUI.clear()
            }
            BET_LIMIT -> {
                TUI.writeCenterLine("HERE WE GO!", 0)
                RouletteDisplay.animationC()
                RouletteDisplay.animationB()
                TUI.clear()
            }
            else -> TUI.writeCenterLine("Ok Ok", 0)
        }
    }

    fun game() {
        RouletteDisplay.clrAll()
        for (i in bonusBets()){
            BETS.computeIfPresent(i) { k, v ->
                v + 1
            }
        }
        RouletteDisplay.clrAll()

        var time_roll = (3..9).random()
        TUI.refresh {
            writeCenterLine("ROLL!",0)
            writeCenterLine("${time_roll}s",1)
        }
        time_roll--
        val sleep = 1000
        var endTime = Time.getTimeInMillis() + sleep
        while (true) {
            if (time_roll <= 0) break
            if (Time.getTimeInMillis() >= endTime) {
                TUI.writeCenterLine("${time_roll}s",1)
                time_roll--
                endTime = Time.getTimeInMillis() + sleep
            }
            RouletteDisplay.animationA()
        }
        val sorted = validBets.random()
        var wonCredits = 0
        BETS.forEach { if(it.key == sorted) wonCredits += COST*2 }
        CREDS += wonCredits
        TUI.refresh {
            TUI.writeCenterLine("Sorted: $sorted",0)
            TUI.write("Won: $$wonCredits")
        }
        SORTED = sorted
        WON = wonCredits
        BETS.replaceAll { k, v -> 0 }
        for (i in 0 until 4) {
            RouletteDisplay.animationB()
        }
        RouletteDisplay.clrAll()
    }

    private fun bonusBets(): List<Char>{
        BET_LIMIT += BONUSBETS // Limite de apostas para o bonus
        var time_roll = TIMEBONUS
        writeGame(time_roll)
        val bonus = mutableListOf<Char>()
        val sleep = 1000
        TUI.showCursor(true)

        var endTime = Time.getTimeInMillis() + sleep
        time_roll--
        while(true){
            if (bonus.size >= BONUSBETS) break
            if (updateCreds()) TUI.refreshPixels("$$CREDS",1,TUI.COLS-"$$CREDS".length)
            val key = TUI.capture()

            if (key !in invalidBets && canUpdateBets(false, key)) {
                if (bonus.isNotEmpty()) {
                    TUI.write(',')
                    TUI.write(key, false, 1)
                }
                else TUI.write(key, false, 1)

                if (sudoMode) bonus += key
                else {
                    bonus += key; CREDS -= COST
                }

                TUI.writeRightLine(" $$CREDS",1)
            }
            else if (key == BACK_OR_CLEAR) {
                if (bonus.isNotEmpty()) {
                    bonus.removeLast()
                    CREDS += COST
                    TUI.writeRightLine(" $$CREDS",1)
                    if (bonus.isNotEmpty()) {
                        TUI.clearChar()
                        TUI.clearChar()
                    }
                    else TUI.clearChar()
                }
            }
            if (time_roll < 0) break
            if (Time.getTimeInMillis() >= endTime) {
                TUI.writeRightLine("${time_roll}s")
                time_roll--
                endTime = Time.getTimeInMillis() + sleep
            }
        }
        TUI.showCursor(false)
        doBonusEndAnimation(bonus)
        Time.sleep(1000)
        BET_LIMIT -= BONUSBETS // Reseta o limite de apostas
        return bonus
    }

    fun doBonusEndAnimation(list: List<Char>) {
        TUI.clear()
        when (list.size) {
            0 -> {
                TUI.writeCenterLine("Meh..", 0)
            }
            BONUSBETS -> {
                TUI.writeCenterLine("OH YEAH!", 0)
                RouletteDisplay.animationC()
                RouletteDisplay.animationC()
            }
            else -> {
                TUI.writeCenterLine("Ok Ok", 0)
                RouletteDisplay.animationB()
            }
        }
    }

    private fun writeGame(time_roll: Int) {
        line1 = "Bonus bets!"
        TUI.clear()
        TUI.write(line1,false,0)

        TUI.writeRightLine("${time_roll}s",0)

        TUI.write("bets:",false,1,0)
        TUI.writeRightLine("$$CREDS",1)
    }

    private fun m() {
        login_M()
        if (M.inM) writeM()
        while (M.inM){
            val key = TUI.capture()
            when(key){
                CONFIRM_KEY ->  { runMockGame() ; writeM() }
                KEY_COIN_M ->   { coinPage()    ; writeM() }
                SORTED_NUM_M -> { statsPage()   ; writeM() }
                GAME_OFF_M ->   { shutdown() }
            }
        }
        TUI.clear()
    }

    private fun runMockGame() {
        sudoMode = true
        val lastCreds = CREDS
        lobby()
        game()
        CREDS = lastCreds
        sudoMode = false
    }

    private fun shutdown() {
        TUI.clear()
        TUI.writeCenterLine("Shutting down...", 0)
        Time.sleep(1000)
        writeAllStats()
        resetAll()
        TUI.clear()
        exitProcess(0)
    }

    private fun statsPage(){
        val page = Page(Statistics.getSortedList())
        page.run {key, clear ->
            when (key) {
                CONFIRM_KEY -> {
                    if (TUI.confirmMenu("Reset All?")) {
                        Statistics.resetAll()
                        clearWrite("Reset Completed")
                        Time.sleep(1000)
                        page.lines = Statistics.getSortedList()
                    }
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

        page.run{ key, reset ->
            when (key) {
                CONFIRM_KEY -> {
                    if (TUI.confirmMenu("Reset All?")) {
                        resetAll()
                        Time.sleep(1000)
                        page.lines = listOf(
                            "2 Coins: ${CoinDeposit.getTotal(0)}",
                            "4 Coins: ${CoinDeposit.getTotal(1)}",
                            "Total: ${CoinDeposit.getTotal(0) + CoinDeposit.getTotal(1)}",
                            "Games: ${Statistics.getGames()}",
                            "# to exit",
                            "* to reset"
                        )
                    }
                    reset()
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
        if (!M.inM) return
        TUI.clear()
        TUI.writeCenterLine("Manager Mode", 0)
        TUI.writeCenterLine("Active", 1)
        RouletteDisplay.animationC()
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

        RouletteDisplay.animationC()

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
        BETS.replaceAll { k, v -> 0 }
        CREDS = 0
        CoinDeposit.resetTotal(0)
        CoinDeposit.resetTotal(1)
        Statistics.resetGames()
        clearWrite("Reset Completed")
    }

    private fun canStartGame(): Boolean{
        if (BETS.isEmpty()){
            clearWrite("You need at least 1 Bet")
            RouletteDisplay.animationB()
            return false
        }

        if(sudoMode) return true

        return true
    }

    private fun canUpdateBets(print : Boolean = true, key: Char): Boolean {
        if ((BETS[key] ?: 0) >= BET_LIMIT) {
            if (print) {
                clearWrite("Bet Limit reached")
                RouletteDisplay.animationB()
                TUI.clear()
            }
            return false
        }

        if(sudoMode) return true

        if(CREDS <= 0){
            if (print) {
                clearWrite("Not enough Credits")
                RouletteDisplay.animationB()
                TUI.clear()
            }
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
            lastCoin = coin
            return true
        }
        return false
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
        var lines: List<String>,
        val upKey : Char = 'A',
        val downKey : Char = 'B',
        val upSym : Int = 0,
        val downSym : Int = 1,
    ) {

        val line1MAX = TUI.COLS-3
        val line2MAX = TUI.COLS-3

        private fun write(idx: Int, idx2: Int){
            val line1 = lines[idx].padEnd(line1MAX, ' ')
            val line2 = lines[idx2].padEnd(line2MAX, ' ')
            if (line1.length <= line1MAX) TUI.refreshPixels (line1, 0, 0)
            else error("Very Big, line length should be less than $line1MAX")
            if (line2.length <= line2MAX) TUI.refreshPixels (line2, 1, 0)
            else error("Very Big, line length should be less than $line2MAX")

            TUI.refreshPixels(upSym.toChar()+upKey.toString(), 0, line1MAX)
            TUI.refreshPixels(downSym.toChar()+downKey.toString(), 1, line2MAX)
        }

        fun run(func: (key: Char, reset: () -> Unit)->Boolean) {

            TUI.clear()
            var idx = 0
            var idx2 = 1

            write(idx, idx2)

            RouletteDisplay.animationC()

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
