import isel.leic.utils.Time
import kotlin.concurrent.thread

object APP_outra {
    fun init(){
        TUI.init()
        TUI.clear()
        //lobby()
        game()
    }
    private val CONFIM_KEY = '*'
    private val KEY_COIN_M = 'A'
    private val SORTED_NUM_M = 'C'
    private val GAME_OFF_M = 'D'
    /**
     * Corresponde à primeira linha da tela LCD
     */
    private var line1 = "Roulette Game"

    /**
     * Corresponde à segunda linha da tela do LCD
     */
    private var line2 = "Start the Game"

    /**
     * Apostas validas
     */
    var validBets = charArrayOf('0','1','2','3','4','5','6','7','8','9','A','B','C','D')

    fun lobby() {
        TODO()
    }

    fun game() {
        var time_roll = (5..9).random()
        val timePixelPos = Pair(0,14)
        writeGame(time_roll,timePixelPos)

        var timing = 0
        val sleep = 1
        while (time_roll >= 0){
            val key = TUI.capture()
            if (key in validBets) {
                TUI.write(key, false, 1)
            }

            RouletteDisplay.animation()
            Time.sleep(sleep.toLong())
            TUI.refreshPixel(
                '0' + time_roll,
                timePixelPos.first,
                timePixelPos.second
            )

            timing++
            //animation dura cerca 500 ms para ter 1 s é 2*sleep + animation time
            if (timing >= 2*sleep) {
                time_roll--
                timing = 0
            }
        }
    }
    private fun writeGame(time_roll: Int,timePixelPos: Pair<Int,Int>) {
        line1 = "Bonus bets!"
        TUI.clear()
        TUI.write(line1)

        TUI.refreshPixel(
            '0' + time_roll,
            timePixelPos.first,
            timePixelPos.second
        )
        TUI.refreshPixel(
            's',
            timePixelPos.first,
            timePixelPos.second + 1
        )
        TUI.write("bets: ",false,1,0)
    }
    private fun m() {
        login_M()
        while (M.inM){
            val key = TUI.capture()
            when(key){
                CONFIM_KEY -> game()
                KEY_COIN_M -> TODO()
                SORTED_NUM_M -> TODO()
                GAME_OFF_M -> TODO()
            }
        }
    }

    private fun login_M(): Boolean{
        var input = ""
        var inputCripted = ""
        line1 = "Login"
        line2 = "Password: "
        TUI.refresh(
            { writeCenterLine(line1, 0) },
            { write(line2) }
        )
        var timer = 0
        var cursor = '_'
        while(input != M.password){
            if (!M.inM) return false

            val key = TUI.capture()

            if (key == '#' && input != ""){
                input = input.dropLast(1)
                inputCripted = inputCripted.dropLast(1)
                TUI.clearChar(true)
            }
            else if (TUI.isValid(key) && key in validBets ) {
                val temp_input = input + key
                if (temp_input.length <= M.password.length) {
                    input = temp_input
                    inputCripted += '*'
                    TUI.write('*')
                }
                if (input == M.password) break
            }
            //importante evita refresh desnecessários
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
                timer++
                timer = timer % 10
                if (timer == 0) cursor = TUI.cursorAnimation(cursor)
            }
            Time.sleep(10)
        }
        TUI.clear()
        return true
    }
}