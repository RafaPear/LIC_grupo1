class Page(
    val lines: List<String>,
    val upKey : Char = 'A',
    val downKey : Char = 'B',
    val upSym : String = """/\""",
    val downSym : String = """\/""",
) {

    val line1MAX = TUI.COLS-(upSym.length+2)
    val line2MAX = TUI.COLS-(downSym.length+2)

    private fun write(idx: Int){
        val line1 = lines[idx].padEnd(line1MAX, ' ')
        val line2 = lines[idx + 1].padEnd(line2MAX, ' ')
        if (line1.length < line1MAX) TUI.refreshPixels (line1, 0, 0)
        else error("Very Big, line length should be less than $line1MAX")
        if (line2.length < line2MAX) TUI.refreshPixels (line2, 1, 0)
        else error("Very Big, line length should be less than $line2MAX")
    }

    fun run(func: (key: Char, reset: () -> Unit)->Boolean) {

        TUI.clear()
        val size = 2
        var idx = 0

        write(idx)

        TUI.refreshPixels(upSym+upKey, 0, line1MAX)
        TUI.refreshPixels(downSym+downKey, 1, line2MAX)

        while (true) {
            val key = TUI.capture()
            if (!func(key){ write(idx) }) break
            when(key){
                upKey -> {
                    idx = ((idx - 1) % (lines.size - size))
                    if (idx < 0) idx = lines.size - size
                    write(idx)
                }
                downKey -> {
                    idx = ((idx + 1) % (lines.size - (size - 1)))
                    write(idx)
                }
            }
        }
    }
}