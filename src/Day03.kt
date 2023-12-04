fun main() {
    fun safeSymbolCheck(field: List<String>, x: Int, y: Int): Char? {
        if (x !in field.indices || y !in field[0].indices) return null
        if (field[x][y] == '.' || field[x][y].isDigit()) return null
        return field[x][y]
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        for (i in input.indices) {
            var number = 0
            var foundSymbol = false
            for (j in input[i].indices) {
                if (!input[i][j].isDigit()) {
                    if (number != 0 && foundSymbol) {
                        sum += number
                    }
                    number = 0
                    foundSymbol = false
                } else {
                    number = number * 10 + input[i][j].digitToInt()
                    if (foundSymbol) {
                        continue
                    }
                    for (dx in -1..1) {
                        for (dy in -1..1) {
                            if (safeSymbolCheck(input, i + dx, j + dy) != null) {
                                foundSymbol = true
                            }
                        }
                    }
                }
            }
            if (number != 0 && foundSymbol) {
                sum += number
            }
        }
        return sum
    }

    fun getGearUpdateValue(
        gearsMatrix: MutableList<MutableList<Int?>>,
        position: Pair<Int, Int>,
        value: Int
    ): Int {
        return when (val gear = gearsMatrix[position.first][position.second]) {
            null -> -value                                  // 0 near
            in Int.MIN_VALUE..0 -> gear * (-value)   // 1 or 3+ near
            else -> 0                                       // 2 near already
        }
    }

    fun part2(input: List<String>): Int {
        val numbersInGears: MutableList<MutableList<Int?>> =
            MutableList(input.size) { MutableList(input[0].length) { null } }
        for (i in input.indices) {
            var number = 0
            val gearPositions: MutableSet<Pair<Int, Int>> = mutableSetOf()
            for (j in input[i].indices) {
                if (!input[i][j].isDigit()) {
                    if (number != 0) {
                        for (gearPosition in gearPositions) {
                            numbersInGears[gearPosition.first][gearPosition.second] =
                                getGearUpdateValue(numbersInGears, gearPosition, number)
                        }
                    }
                    number = 0
                    gearPositions.clear()
                } else {
                    number = number * 10 + input[i][j].digitToInt()
                    for (dx in -1..1) {
                        for (dy in -1..1) {
                            if (safeSymbolCheck(input, i + dx, j + dy) == '*') {
                                gearPositions.add(i + dx to j + dy)
                            }
                        }
                    }
                }
            }
            if (number != 0) {
                for (gearPosition in gearPositions) {
                    numbersInGears[gearPosition.first][gearPosition.second] =
                        getGearUpdateValue(numbersInGears, gearPosition, number)
                }
            }
        }
        return numbersInGears.flatten().sumOf { if (it == null || it < 0) 0 else it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()

    val testInput2 = readInput("Day03_test")
    check(part2(testInput2) == 467835)
    part2(input).println()
}
