package me.sweetie.starwarssearcher.interfaces

fun interface IObj<T> {
    fun getObj(obj: Any): T
}