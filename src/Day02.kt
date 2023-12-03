import java.lang.IllegalArgumentException

fun main() {
    val limits = mapOf(0 to 12, 1 to 13, 2 to 14)
    val ids = mapOf('r' to 0, 'g' to 1, 'b' to 2)
    fun subGameRequired(subgame: String): Triple<Int, Int, Int> {
        var answer = Triple(0, 0, 0)
        for (part in subgame.split(", ")) {
            val (amount, color) = part.split(" ")
            check(color[0] in ids.keys)
            answer = answer.maxUpdate(ids[color[0]]!!, amount.toInt())
        }
        return answer
    }

    fun gameRequired(subgamesS: String): Triple<Int, Int, Int> {
        var answer = Triple(0, 0, 0)
        for (subgame in subgamesS.split("; ")) {
            val newUpdate = subGameRequired(subgame)
            for (i in 0..2) {
                answer = answer.maxUpdate(i, newUpdate[i])
            }
        }
        return answer
    }

    fun isGameValid(game: String): Int { // 0 if invalid, id otherwise
        val (gameIdS, subgamesS) = game.split(": ")
        val gameId = gameIdS.split(" ")[1].toInt()
        val required = gameRequired(subgamesS)
        for (id in 0..2) {
            if (required[id] > limits[id]!!) {
                return 0
            }
        }
        return gameId
    }

    fun gamePower(game: Triple<Int, Int, Int>): Int {
        var answer = 1
        for (id in 0..2) {
            answer *= game[id]
        }
        return answer
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { isGameValid(it) }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { gamePower(gameRequired(it.split(": ")[1])) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02")
    part1(input).println()

    val testInput2 = readInput("Day02_test")
    check(part2(testInput2) == 2286)
    part2(input).println()
}

fun Triple<Int, Int, Int>.updated(id: Int, value: Int): Triple<Int, Int, Int> {
    return when (id) {
        0 -> Triple(value, this.second, this.third)
        1 -> Triple(this.first, value, this.third)
        2 -> Triple(this.first, this.second, value)
        else -> throw IllegalArgumentException("Invalid index for a tuple of three: $id")
    }
}

fun Triple<Int, Int, Int>.maxUpdate(id: Int, value: Int): Triple<Int, Int, Int> {
    if (this[id] < value) {
        return this.updated(id, value)
    }
    return this
}

private operator fun <A> Triple<A, A, A>.get(id: Int): A {
    return when (id) {
        0 -> this.first
        1 -> this.second
        2 -> this.third
        else -> throw IllegalArgumentException("Invalid index for a tuple of three: $id")
    }
}
