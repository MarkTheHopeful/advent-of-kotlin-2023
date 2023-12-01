fun main() {
    fun getNumber(line: String): Int {
        val first: Int = line.first { it.isDigit() }.digitToInt()
        val last: Int = line.last { it.isDigit() }.digitToInt()
        return first * 10 + last
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { getNumber(it) }
    }

    val numbersRepr = listOf(
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine"
    ) + (0..9).map { it.toString() }
    val reprToValues = numbersRepr.zip((1..9) + (0..9)).toMap()

    fun getNumberByName(line: String): Int {
        val first: Int =
            line.findAnyOf(numbersRepr)?.second?.let { reprToValues[it] } ?: return 0
        val last: Int =
            line.findLastAnyOf(numbersRepr)?.second?.let { reprToValues[it] } ?: return 0
        return first * 10 + last
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { getNumberByName(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()

    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)
    part2(input).println()
}
