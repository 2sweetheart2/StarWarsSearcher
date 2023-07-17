package me.sweetie.starwarssearcher.objects

import org.json.JSONObject

class ObjStarship(payload:JSONObject) {
    val name:String
    val model:String
    val manufacturer:String
    val passengers:String
    var favorite:Boolean = false
    val url:String

    init{
        name = payload.getString("name")
        model = payload.getString("model")
        manufacturer = payload.getString("manufacturer")
        passengers = payload.getString("passengers")
        url = payload.getString("url")
    }

}