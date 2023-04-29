package com.baymax104.bookmanager20.util

import com.baymax104.bookmanager20.architecture.interfaces.ObjectRange
import java.util.LinkedList

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/27 20:22
 *@Version 1
 */
abstract class TreeNode<T> {

    val children: LinkedList<Node<T>> = LinkedList()

    var index = -1

    operator fun get(i: Int) = children.getOrNull(i)

    operator fun iterator() = children.listIterator()

    inline fun forEachChild(action: (Node<T>) -> Unit) = children.forEach(action)

    fun refreshIndex(from: Int = 0, to: Int = children.size) {
        for (i in from until to) {
            children[i].index = i
        }
    }

    // insert into specific index, index is included in node
    operator fun plusAssign(node: Node<T>) {
        if (node.parent == this && node in children) return
        node.parent = this
        if (children.isEmpty() || node.index >= children.size || node.index < 0) {
            node.index = children.size
        }
        children.add(node.index, node)
        refreshIndex(from = node.index + 1)
    }

    operator fun minusAssign(node: Node<T>) {
        if (children.isEmpty() || !children.contains(node)) {
            throw IndexOutOfBoundsException("node is not contained in parent node")
        }
        // refresh children index and attach its children to its parent
        node.forEachChild {
            it.parent = this
            it.index = node.index++
            children.add(it.index, it)
        }

        // remove and refresh index
        node.parent = null
        node.children.clear()
        children -= node
        refreshIndex(from = node.index)
    }

    operator fun plusAssign(elements: Iterable<Node<T>>) {
        elements.forEach { this += it }
    }

    operator fun minusAssign(elements: Iterable<Node<T>>) {
        elements.forEach { this -= it }
    }
}

class Root<T> : TreeNode<T>() {
    override fun toString() = "Root"
}

data class Node<T>(val value: T) : TreeNode<T>() {

    var parent: TreeNode<T>? = null

    override fun toString(): String {
        return "Node(value=$value, parent=$parent, index=$index)"
    }
}

class IntervalTree<R : Comparable<R>, T : ObjectRange<R>>(
    val shrinkLeft: (leftEnd: R) -> R,
    val shrinkRight: (rightStart: R) -> R,
    val adjoin: (left: R, right: R) -> Boolean
) {

    val root: Root<T> = Root()

    // map parent node to its overlap child
    private val overlapMap: TreeMap<T> = HashMap()

    // map existed node to its duplicate node
    private val duplicateMap: TreeMap<T> = HashMap()

    fun create(list: List<T>) {
        list.forEach { this += it }
    }

    operator fun plusAssign(value: T) {
        val node = Node(value)
        val parent = find(value)

        // check situation and store intervals
        // previous and next must be only one
        var left: Node<T>? = null
        var right: Node<T>? = null
        val covers: MutableList<Node<T>> = mutableListOf()
        for (child in parent) {
            when {
                child.value leftOverlap node.value -> left = child
                child.value rightOverlap node.value -> right = child
                child.value in node.value -> covers += child
            }
        }

        // check overlap
        if (left != null && right != null && adjoin(left.value.end, right.value.start)) {
            addOverlap(left, node)
            return
        }

        // check duplicate
        if (parent is Node && parent.value equal value) {
            addDuplicate(parent, node)
            return
        }
        if (covers.size == 1 && covers.first().value equal value) {
            addDuplicate(covers.first(), node)
            return
        }

        // shrink its left boundary
        if (left != null) {
            node.value.start = shrinkLeft(left.value.end)
        }

        // shrink its right boundary
        if (right != null) {
            node.value.end = shrinkRight(right.value.start)
        }

        // attach overlap child to its children
        if (covers.isNotEmpty()) {
            parent.children -= covers.toSet()
            parent.refreshIndex()
            node += covers
        }

        // confirm node's index and insert it orderly
        if (left != null) {
            node.index = left.index + 1
        }
        if (left == null && right != null) {
            node.index = right.index
        }
        parent += node
    }

    operator fun minusAssign(value: T) {
        val node = find(value)

        // value does not exist in tree
        if (node is Root || (node is Node && value inner node.value)) {
            // search overlap and remove it, node is seen as parent
            if (node.children.isNotEmpty() && overlapMap[node] != null) {
                val overlap = overlapMap[node]!!.find { it.value equal value }
                overlap?.let {
                    it.parent = null
                    overlapMap[node]!!.remove(it)
                }
                if (overlapMap[node]!!.isEmpty()) overlapMap -= node
                if (overlap != null) return
            }
            throw AssertionError("value does not exist in tree")
        }

        node as Node
        val parent = node.parent!!

        // if children is empty, find overlap to fill it
        if (node.children.isEmpty()) {

            // if both overlap are null, remove it straightly
            val rightOverlap = overlapMap[parent]!!.find { it.index == node.index }
            val leftOverlap = overlapMap[parent]!!.find { it.index == node.index - 1 }

            // shrink node's boundary according to duplicated item
            val overlap = rightOverlap ?: leftOverlap
            if (overlap != null) {
                if (overlap === rightOverlap) {
                    node.value.start = overlap.value.start
                } else {
                    node.value.end = overlap.value.end
                }
                overlap.parent = null
                overlapMap[parent]!!.remove(overlap)
                if (overlapMap[parent]!!.isEmpty()) overlapMap -= parent
                return
            }
        }

        // remove node, if node has children, let its children fill its interval
        parent -= node
        // delete node's duplicate list
        duplicateMap -= node
    }

    /**
     * find node's parent or itself
     *
     * * if node does not exist in tree, it returns node's parent
     * * if node exists in tree, it returns node itself
     */
    private fun find(value: T): TreeNode<T> {
        var current: TreeNode<T> = root
        while (true) {
            val children = current.children
            if (children.isEmpty()) { break }
            var isContained = false
            for (child in children) {
                if (value in child.value) {
                    current = child
                    isContained = true
                    break
                }
            }
            // children are not empty, but it is not included in any child
            if (!isContained) { break }
        }
        return current
    }

    private fun addOverlap(host: Node<T>, node: Node<T>) {
        val parent = host.parent!!
        if (overlapMap[parent] == null) {
            overlapMap[parent] = LinkedList()
        }
        node.parent = parent
        node.index = host.index
        overlapMap[parent]!!.add(node)
    }

    private fun addDuplicate(host: Node<T>, node: Node<T>) {
        if (duplicateMap[host] == null) {
            duplicateMap[host] = LinkedList()
        }
        duplicateMap[host]!!.add(node)
    }



}