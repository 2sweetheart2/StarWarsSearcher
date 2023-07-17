package me.sweetie.starwarssearcher.objects

import me.sweetie.starwarssearcher.requests.SwAPI
import org.json.JSONObject

class ObjFilm(payload:JSONObject) {
    val characters: ArrayList<ObjPeople>
    val created: String
    val director: String
    val edited: String
    val episode_id: Int
    val opening_crawl: String
    val producer: String
    val release_date: String
    val title: String

    init{
        val charaterAr = payload.get("characters") as ArrayList<String>
        val charater_: ArrayList<ObjPeople> = ArrayList()
        for(character in charaterAr){
            SwAPI.getPeople(character.split('/')[5].toInt()) { people -> charater_.add(people) }
        }
        characters = charater_
        created = payload.getString("created")
        director = payload.getString("director")
        edited = payload.getString("edited")
        episode_id = payload.getInt("episode_id")
        opening_crawl = payload.getString("opening_crawl")
        producer = payload.getString("producer")
        release_date = payload.getString("release_date")
        title = payload.getString("title")

    }

}