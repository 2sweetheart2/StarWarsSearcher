package me.sweetie.starwarssearcher.objects

import me.sweetie.starwarssearcher.requests.HTTPSRequests
import me.sweetie.starwarssearcher.interfaces.IPlanet
import org.json.JSONObject

class ObjPeople(payload:JSONObject) {
    val data:JSONObject
    var birth_year:String
    var eye_color:String
    var gender:String
    var hair_color:String
    var height:String
    var mass:String
    var name:String
    var skin_color:String
    var created:String
    var edited:String
    val films:ArrayList<String> = ArrayList()
    val species:ArrayList<String> = ArrayList()
    val starships:ArrayList<String> = ArrayList()
    val vehicles:ArrayList<String> = ArrayList()
    var homeworld:ObjPlanet? = null
    private val homeworldLink:String
    var favorite = false
    val url:String
    init{
        data = payload
        birth_year = payload.getString("birth_year")
        eye_color = payload.getString("eye_color")
        gender = payload.getString("gender")
        hair_color = payload.getString("hair_color")
        height = payload.getString("height")
        mass = payload.getString("mass")
        name = payload.getString("name")
        created = payload.getString("created")
        edited = payload.getString("edited")
        skin_color = payload.getString("skin_color")
        for(i in 0 until payload.getJSONArray("films").length()){
            val l = payload.getJSONArray("films").getString(i)
            films.add(l)
        }
        for(i in 0 until payload.getJSONArray("species").length()){
            val l = payload.getJSONArray("species").getString(i)
            species.add(l)
        }
        for(i in 0 until payload.getJSONArray("starships").length()){
            val l = payload.getJSONArray("starships").getString(i)
            starships.add(l)
        }
        for(i in 0 until payload.getJSONArray("vehicles").length()){
            val l = payload.getJSONArray("vehicles").getString(i)
            vehicles.add(l)
        }
        homeworldLink = payload.getString("homeworld")
        url = payload.getString("url")
    }

    fun loadHome(callback:IPlanet){
        if(homeworld!=null)
            callback.getPlanet(homeworld!!)
        HTTPSRequests.sendGet(homeworldLink,{ json->
            this.homeworld = ObjPlanet(json)
            callback.getPlanet(homeworld!!)
        },"format=json")
    }

}