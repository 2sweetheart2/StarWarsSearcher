package me.sweetie.starwarssearcher.interfaces

import me.sweetie.starwarssearcher.objects.ObjStarship

fun interface IStarships {
    fun getStarships(starships: ArrayList<ObjStarship>)
}

fun interface IStarship{
    fun getStarship(starship: ObjStarship)
}