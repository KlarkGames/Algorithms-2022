package lesson4

import java.util.*
import kotlin.NoSuchElementException

/**
 * Префиксное дерево для строк
 */
class KtTrie : AbstractMutableSet<String>(), MutableSet<String> {

    private class Node {
        val children: SortedMap<Char, Node> = sortedMapOf()
    }

    private val root = Node()

    override var size: Int = 0
        private set

    override fun clear() {
        root.children.clear()
        size = 0
    }

    private fun String.withZero() = this + 0.toChar()

    private fun String.chop(): String {
        if (this.isEmpty()) {
            return ""
        }
        return this.substring(0, this.length - 1)
    }

    private fun findNode(element: String): Node? {
        var current = root
        for (char in element) {
            current = current.children[char] ?: return null
        }
        return current
    }

    override fun contains(element: String): Boolean =
        findNode(element.withZero()) != null

    override fun add(element: String): Boolean {
        var current = root
        var modified = false
        for (char in element.withZero()) {
            val child = current.children[char]
            if (child != null) {
                current = child
            } else {
                modified = true
                val newChild = Node()
                current.children[char] = newChild
                current = newChild
            }
        }
        if (modified) {
            size++
        }
        return modified
    }

    override fun remove(element: String): Boolean {
        val current = findNode(element) ?: return false
        if (current.children.remove(0.toChar()) != null) {
            size--
            return true
        }
        return false
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: [java.util.Iterator] (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    override fun iterator(): MutableIterator<String> {
        return TrieIterator()
    }

    inner class TrieIterator internal constructor() : MutableIterator<String> {
        private val startPoint = Node()
        private val pointStack = Stack<Pair<Node, Int>>()
        private var currentPoint = startPoint
        private var currentString = ""
        private var index = 0
        private var wordsCount = 0

        init {
            connectRoot(root)
        }

        private fun connectRoot(root: Node) {
            startPoint.children.putAll(root.children)
            pointStack.push(Pair(startPoint, 0))
        }

        override fun hasNext(): Boolean = wordsCount < size

        override fun next(): String {
            /*
            Время: O(N)
            Память: O(N)
            */
            while (hasNext()) {
                currentPoint = pointStack.peek().first
                index = pointStack.peek().second

                if (index == currentPoint.children.size) {
                    currentString = currentString.chop()
                    pointStack.pop()
                    val previousPoint = pointStack.peek().first
                    val previousIndex = pointStack.peek().second
                    pointStack.pop()
                    pointStack.push(Pair(previousPoint, previousIndex + 1))
                } else if (currentPoint.children.keys.toMutableList()[index] == 0.toChar()) {
                    wordsCount ++
                    pointStack.pop()
                    pointStack.push(Pair(currentPoint, index + 1))
                    return currentString
                } else {
                    pointStack.push(Pair(currentPoint.children.values.toMutableList()[index], 0))
                    currentString += currentPoint.children.keys.toMutableList()[index]
                }
            }
            throw NoSuchElementException()
        }

        override fun remove() {
            /*
            Время: O(N)
            Память: O(1)
            */
            if (wordsCount == 0 || !contains(currentString)) {
                throw IllegalStateException()
            }
            val previousPoint = pointStack.peek().first
            val previousIndex = pointStack.peek().second
            pointStack.pop()
            pointStack.push(Pair(previousPoint, previousIndex - 1))
            wordsCount--
            remove(currentString)
        }
    }


}