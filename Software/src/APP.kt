import isel.leic.utils.Time
import kotlin.concurrent.thread

/**
 * Aplicação principal do Jogo
 */
object APP {
    /**
     * Guarda a tecla sorteada
     */
    private var keyWinner = '1'

    /**
     * O tempo em segundos do processo de girar a roleta
     */
    private const val timeRoulette = 5

    /**
     * Tecla responsavel por concluir as apostas
     */
    private const val sendBets = '#'

    /**
     * Inicia o jogo
     */
    private const val startGame = '*'

    /**
     * Total de apostas disponíveis
     */
    private var totalBets = maxBets
    /**
     * Número de apostas máximas
     */
    private const val maxBets = 6
    /**
     *Número de apostas bonus
     */
    private const val bonusBets = 3
    /**
     * Apostas feitas
     */
    private var bets = ""

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

    /**
     * Inicia a aplicação
     */
    fun init() {
        TUI.init()
        RouletteDisplay.init()
        lobby(300L)
        game()
    }

    /**
     * Responsável por construir a tela inicial do jogo
     * @param time o tempo que irá demorar o [TUI.writeWalkText]
     * @param breakKey tecla que quebra o loop
     */
    fun lobby(time: Long = 200, breakKey: Char = startGame) {
        var window = ""

        for (i in 0 until LCD.COLS - 1) {
            window += " "
        }
        window += line2

        for (i in 0 until LCD.COLS - 1) {
            window += " "
        }
        var l = 0
        var r = LCD.COLS - 1

        while (true) {
            if (KBD.getKey() == breakKey) return
            TUI.writeCenter(line1)
            TUI.nextLine()
            if (r == window.length) {
                r = LCD.COLS - 1
                l = 0
            }
            for (i in l..r) {
                TUI.write(window[i], false)
            }
            l++
            r++
            Time.sleep(time)
            TUI.clear()
        }
    }

    /**
     * Loop principal do jogo
     */
    fun game(){
        while(true) {
            bets = ""
            line1 = "Roulette Game"
            totalBets = maxBets
            line2 = "$maxBets bets!"

            refresh(
                { writeCenterLine(line1, 0) },
                { writeCenterLine(line2, 1) }
            )

            RouletteDisplay.set(totalBets)

            Time.sleep(1000L)

            line2 = "Bets:"

            refresh(
                { writeCenterLine(line1, 0) }, { write(line2) }
            )

            bets()

            totalBets = 3
            line1 = "$bonusBets bonus bets!"
            line2 = timeRoulette.toString() + "s"

            refresh(
                { writeCenterLine(line1, 0) }, { writeCenterLine(line2, 1) }
            )

            bonusBets()

            line1 = "You guessed: " + result().toString()
            line2 = "Winner Key: $keyWinner"

            refresh(
                { writeCenterLine(line1, 0)},
                { writeCenterLine(line2, 1)}
            )
            RouletteDisplay.clrAll()

            while (true) {
                if (TUI.capture() == startGame) break
            }
        }
    }

    /**
     * Responsável por efetuas as apostas principais
     */
    fun bets() {
        while(true){
            val temp = TUI.capture()
            if (temp == sendBets) return

            if (totalBets > 0 && temp in validBets) {
                totalBets--
                RouletteDisplay.set(totalBets)
                bets += temp
                TUI.write("$temp,")
            }
        }
    }

    /**
     * Efetua as apostas bonus, estas que acontecem durante o processo de rodar a roleta
     */
    fun bonusBets() {
        var count = timeRoulette

        thread {
            for (i in 1..timeRoulette) {
                Time.sleep(1000L)
                count--
            }
        }
        while (count > 0) {
            val temp = TUI.capture()
            if (temp in validBets && totalBets > 0) {
                totalBets--
                bets += temp
            }
            RouletteDisplay.animation()

            line1 = "$bonusBets bonus bets!"
            line2 = count.toString() + "s"

            refresh(
                {writeCenterLine(line1,0)},
                {writeCenterLine(line2,1)}
            )

        }
    }

    /**
     * Define aleatoriamente a tecla vencedora, e calcula a pontuação
     * @return [Int] retorna a pontuação
     */
    fun result(): Int{
        var sum = 0
        keyWinner = validBets.random()
        for (i in bets) if (i == keyWinner) sum++
        return sum
    }

    /**
     * Limpa a tela LCD, e em seguinda executa o lambda [write1] para escrever na tela, mesma coisa para o [write2]
     * ambos em linhas diferentes
     * @param write1 uma função de extensão do TUI
     * @param write2 uma função de extensão do TUIa
     */
    fun refresh(write1: TUI.()-> Unit,write2: TUI.()-> Unit) {
        TUI.clear()
        TUI.write1()
        TUI.nextLine()
        TUI.write2()
    }

}
