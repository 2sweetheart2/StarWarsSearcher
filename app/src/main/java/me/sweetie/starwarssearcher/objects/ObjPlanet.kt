package me.sweetie.starwarssearcher.objects

import org.json.JSONObject

class ObjPlanet(payload:JSONObject) {
    val climate:String
    val diameter:String
    val gravity:String
    val name:String
    val orbital_period:String
    val population:String

    init{
        climate=payload.getString("climate")
        diameter=payload.getString("diameter")
        gravity=payload.getString("gravity")
        name=payload.getString("name")
        orbital_period=payload.getString("orbital_period")
        population=payload.getString("population")
    }
}