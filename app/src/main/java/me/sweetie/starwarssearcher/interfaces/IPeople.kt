package me.sweetie.starwarssearcher.interfaces

import me.sweetie.starwarssearcher.objects.ObjPeople

fun interface IPeoples {
    fun getPeoples(peoples: ArrayList<ObjPeople>)
}
fun interface IPeople{
    fun getPeople(people: ObjPeople)
}