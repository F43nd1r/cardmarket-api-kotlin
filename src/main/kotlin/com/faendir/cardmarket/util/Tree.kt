package com.faendir.cardmarket.util

/**
 * @author lukas
 * @since 15.10.19
 */
data class Tree<T : Any>(val data: T, val children: List<Tree<T>> = emptyList())

data class TreeBuilder<T : Any>(val children: MutableList<Tree<T>> = mutableListOf()) {
    infix fun node(tree: Tree<T>) {
        children += tree
    }

    infix fun node(data: T) = node(Tree(data))
}

fun <T : Any> tree(data: T, initialize: (TreeBuilder<T>.() -> Unit)? = null): Tree<T> {
    val builder = TreeBuilder<T>()
    initialize?.invoke(builder)
    return Tree(data, builder.children)
}

fun <T : Any> leafs(data: T, vararg leafs: T?) = Tree(data, leafs.filterNotNull().map { Tree(it) })