package com.baymax104.bookmanager20.architecture.interfaces

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/27 22:29
 *@Version 1
 */
interface ObjectRange<R : Comparable<R>> {

    var start: R

    var end: R

    operator fun contains(other: ObjectRange<R>) = start <= other.start && other.end <= end

    infix fun contained(other: ObjectRange<R>) = other.start <= start && end <= other.end

    infix fun inner(other: ObjectRange<R>) = other.start < start && end < other.end

    infix fun equal(other: ObjectRange<R>) = start == other.start && end == other.end

    infix fun leftOverlap(other: ObjectRange<R>): Boolean {
        if (other in this) return false
        if (this contained other) return false
        return start < other.start && other.start <= end && end < other.end
    }

    infix fun rightOverlap(other: ObjectRange<R>): Boolean {
        if (other in this) return false
        if (this contained other) return false
        return other.start < start && start <= other.end && other.end < end
    }
}
