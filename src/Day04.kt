import kotlin.math.min

fun main() {
    fun getMatches(card: String): Set<Int> {
        val winningNumbers: MutableSet<Int> = mutableSetOf()
        val cardNumbers: MutableSet<Int> = mutableSetOf()
        var encounteredPipe = false
        for (token in card.split(regex = " +".toRegex()).drop(2)) {
            if (token == "|") {
                encounteredPipe = true
            } else {
                if (!encounteredPipe) {
                    winningNumbers.add(token.toInt())
                } else {
                    cardNumbers.add(token.toInt())
                }
            }
        }
        return cardNumbers.intersect(winningNumbers)
    }

    fun part1(input: List<String>): Int {
        return input.map { getMatches(it).size }.sumOf { if (it > 0) 1 shl (it - 1) else 0 }
    }

    fun part2(input: List<String>): Int {
        val dp = MutableList(input.size) { 1 }
        var answer = 0
        for ((id, line) in input.withIndex()) {
            val step = getMatches(line).size
            for (i in (id + 1)..min(id + step, input.size - 1)) {
                dp[i] += dp[id]
            }
            answer += dp[id]
        }
        return answer
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("Day04")
    part1(input).println()

    val testInput2 = readInput("Day04_test")
    check(part2(testInput2) == 30)
    part2(input).println()
}
