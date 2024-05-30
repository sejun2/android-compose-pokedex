package com.origogi.pokedex.extenstion
fun <T> List<T>.sample(n: Int): List<T> {
    return this.shuffled().take(n)
}

fun String.capitalizeFirstChar(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}

fun Int.PokedexIdString(): String {
    return "Nº${this.toString().padStart(3, '0')}"
}