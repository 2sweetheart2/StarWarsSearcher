package me.sweetie.starwarssearcher.interfaces

import me.sweetie.starwarssearcher.objects.ObjPlanet

fun interface IPlanets {
    fun getPlanets(planets: ArrayList<ObjPlanet>)
}

fun interface IPlanet{
    fun getPlanet(planet: ObjPlanet)
}