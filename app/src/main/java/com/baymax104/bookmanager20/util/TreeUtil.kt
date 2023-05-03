package com.baymax104.bookmanager20.util

import com.baymax104.bookmanager20.architecture.interfaces.ObjectRange
import java.util.*
import kotlin.collections.HashSet

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

    val hash: Int
        get() = System.identityHashCode(this)

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

interface IntervalCallback<R : Comparable<R>> {
    fun shrinkLeft(leftEnd: R): R
    fun shrinkRight(rightStart: R): R
    fun adjoin(left: R, right: R): Boolean
}

open class IntervalTree<R : Comparable<R>, T : ObjectRange<R>>(
    val callback: IntervalCallback<R>
) {

    private val root: Root<T> = Root()

    val firstSet: HashSet<T>
        get() = root.children.asSequence()
            .map { it.value }
            .toHashSet()

    // map parent node to its overlap child
    private val overlapMap: TreeMap<T> = HashMap()

    // map existed node to its duplicate node
    private val duplicateMap: TreeMap<T> = HashMap()

    fun create(list: List<T>) = apply {
        list.forEach { this += it }
    }

    operator fun plusAssign(value: T) {
        val node = Node(value)
        val parent = find(value)

        if (addParentDuplicate(parent, node)) return

        val (left, right, covers) = checkChildCondition(parent, node)

        // check overlap
        if (addOverlap(left, right, node)) return

        // shrink boundary
        if (left != null) {
            node.value.start = callback.shrinkLeft(left.value.end)
        }
        if (right != null) {
            node.value.end = callback.shrinkRight(right.value.start)
        }

        // check duplicate
        if (addCoverDuplicate(covers, node)) return

        // attach overlap child to its children
        if (covers.isNotEmpty()) {
            parent.children -= covers.toSet()
            parent.refreshIndex()
            node += covers
        }

        // confirm node's index in order to insert it orderly
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

        if (!assertNodeExistInTree(value, node)) {
            // search overlap and remove it, node is seen as parent
            val overlap = overlapMap[node.hash]!!.find { it.value equal value }
            overlap?.let {
                it.parent = null
                overlapMap[node.hash]!!.remove(it)
            }
            if (overlapMap[node.hash]!!.isEmpty()) overlapMap -= node.hash
            if (overlap != null) return
        }

        node as Node
        val parent = node.parent!!

        // if children is empty, find overlap to fill it
        if (node.children.isEmpty()) {

            // if both overlap are null, remove it straightly
            val rightOverlap = overlapMap[parent.hash]?.find { it.index == node.index }
            val leftOverlap = overlapMap[parent.hash]?.find { it.index == node.index - 1 }

            // shrink node's boundary according to duplicated item
            val overlap = rightOverlap ?: leftOverlap
            if (overlap != null) {
                if (overlap === rightOverlap) {
                    node.value.start = overlap.value.start
                } else {
                    node.value.end = overlap.value.end
                }
                overlap.parent = null
                overlapMap[parent.hash]!!.remove(overlap)
                if (overlapMap[parent.hash]!!.isEmpty()) overlapMap -= parent.hash
                return
            }
        }

        // remove node, if node has children, let its children fill its interval
        parent -= node
        // delete node's duplicate list
        duplicateMap -= node.hash
    }

    fun clear() {
        val current: TreeNode<T> = root
        val queue: Queue<TreeNode<T>> = LinkedList()
        queue.offer(current)
        while (queue.isNotEmpty()) {
            val node = queue.poll()!!
            node.forEachChild { queue.offer(it.apply { parent = null }) }
            node.children.clear()
        }
    }

    private fun assertNodeExistInTree(value: T, node: TreeNode<T>): Boolean {
        // node does not exist in tree, may exist in overlap map
        if (node is Root || (node is Node && value inner node.value)) {
            // node does not exist in overlap map, throw AssertionError
            if (node.children.isEmpty() || overlapMap[node.hash] == null) {
                throw AssertionError("value does not exist in tree or overlap map")
            }
            return false  // node exists in overlap map
        }
        return true  // node exists in tree
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
            if (!isContained) {
                break
            }
        }
        return current
    }

    private fun checkChildCondition(
        parent: TreeNode<T>,
        node: Node<T>
    ): Triple<Node<T>?, Node<T>?, List<Node<T>>> {
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
        return Triple(left, right, covers)
    }

    private fun addOverlap(left: Node<T>?, right: Node<T>?, node: Node<T>): Boolean {
        if (left != null && right != null && callback.adjoin(left.value.end, right.value.start)) {
            addOverlapInternal(left, node)
            return true
        }
        return false
    }

    private fun addParentDuplicate(parent: TreeNode<T>, node: Node<T>): Boolean {
        if (parent is Node && parent.value equal node.value) {
            addDuplicateInternal(parent, node)
            return true
        }
        return false
    }

    private fun addCoverDuplicate(covers: List<Node<T>>, node: Node<T>): Boolean {
        if (covers.size == 1 && covers.first().value equal node.value) {
            addDuplicateInternal(covers.first(), node)
            return true
        }
        return false
    }

    private fun addOverlapInternal(host: Node<T>, node: Node<T>) {
        val parent = host.parent!!
        if (overlapMap[parent.hash] == null) {
            overlapMap[parent.hash] = LinkedList()
        }
        node.parent = parent
        node.index = host.index
        overlapMap[parent.hash]!!.add(node)
    }

    private fun addDuplicateInternal(host: Node<T>, node: Node<T>) {
        if (duplicateMap[host.hash] == null) {
            duplicateMap[host.hash] = LinkedList()
        }
        duplicateMap[host.hash]!!.add(node)
    }

}