import kotlin.math.max
import kotlin.math.min

private fun <A> listToTriple(list: List<A>): Triple<A, A, A> {
    check(list.size == 3)
    return Triple(list[0], list[1], list[2])
}

private class Mapper(lines: List<String>) {
    val mapSegments: List<Triple<Long, Long, Long>> = lines.map {
        listToTriple(it.split(" ").map { part -> part.toLong() })
    }.sortedBy { it.second }

    fun map(item: Long): Long {
        var l = 0
        var r = mapSegments.size
        while ((r - l) > 1) {
            val m = (l + r) / 2
            if (mapSegments[m].second <= item) {
                l = m
            } else {
                r = m
            }
        }
        if (item in mapSegments[l].second until mapSegments[l].second + mapSegments[l].third) {
            val delta = item - mapSegments[l].second
            return mapSegments[l].first + delta
        }
        return item
    }

    fun mapSegment(segment: LongRange): List<LongRange> {
        var currentSegment = segment
        val answer = mutableListOf<LongRange>()
        for (mapPart in mapSegments) {
            val mapFromSegment = mapPart.second until mapPart.second + mapPart.third
            val partTakenHere = intersectRanges(currentSegment, mapFromSegment)
            if (partTakenHere.isEmpty()) continue
            val partLeft = currentSegment.first until partTakenHere.first
            if (!partLeft.isEmpty()) {
                answer.add(partLeft)
            }
            answer.add(shiftRange(partTakenHere, mapPart.first - mapPart.second))
            if (partTakenHere == currentSegment) {
                currentSegment = LongRange.EMPTY
                break
            }
            currentSegment = (partTakenHere.last + 1)..currentSegment.last
        }
        if (!currentSegment.isEmpty()) {
            answer.add(currentSegment)
        }
        return answer
    }
}

fun shiftRange(range: LongRange, shift: Long): LongRange {
    return (range.first + shift)..(range.last + shift)
}

fun intersectRanges(first: LongRange, second: LongRange): LongRange {
    return max(first.first, second.first)..min(first.last, second.last)
}

fun main() {
    fun part1(input: List<String>): Long {
        var seeds: List<Long> = input[0].split(" ").drop(1).map { it.toLong() }
        for (part in input.drop(2).joinToString("\n").split("\n\n").map { it.split("\n") }) {
            val mapper = Mapper(part.drop(1))
            seeds = seeds.map { mapper.map(it) }
        }
        return seeds.min()
    }

    fun part2(input: List<String>): Long {
        var seedSegments: List<LongRange> =
            input[0].split(" ").drop(1).map { it.toLong() }.chunked(2)
                .map { it[0] until it[0] + it[1] }
        for (part in input.drop(2).joinToString("\n").split("\n\n").map { it.split("\n") }) {
            val mapper = Mapper(part.drop(1))
            seedSegments = seedSegments.map { mapper.mapSegment(it) }.flatten()
        }
        return seedSegments.minOf { it.first }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)

    val input = readInput("Day05")
    part1(input).println()

    val testInput2 = readInput("Day05_test")
    check(part2(testInput2) == 46L)
    part2(input).println()
}


