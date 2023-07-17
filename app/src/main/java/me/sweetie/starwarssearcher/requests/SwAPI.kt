package me.sweetie.starwarssearcher.requests

import me.sweetie.starwarssearcher.interfaces.IPeople
import me.sweetie.starwarssearcher.interfaces.IPeoples
import me.sweetie.starwarssearcher.interfaces.IStarships
import me.sweetie.starwarssearcher.objects.ObjPeople
import me.sweetie.starwarssearcher.objects.ObjStarship

/**
 * тут рассписывать особо нечего, просто все нужные запрос к [api](https://swapi.dev/api/) через [HTTPSRequests]
 */
class SwAPI {
    companion object {

        private const val url = "https://swapi.dev/api/"


        fun getPeople(id: Int, callback: IPeople) {
            HTTPSRequests.sendGet("people/$id", { json ->
                callback.getPeople(ObjPeople(json))
            }, "format=json")
        }

        fun searchStarships(name: String, callback: IStarships) {
            HTTPSRequests.sendGet(
                url + "starships", { json ->
                    val ar = ArrayList<ObjStarship>()
                    for (i in 0 until json.getJSONArray("results").length()) {
                        ar.add(ObjStarship(json.getJSONArray("results").getJSONObject(i)))
                    }
                    callback.getStarships(ar)
                }, "search=$name", "format=json"
            )
        }

        fun searchPeoples(name: String, callback: IPeoples) {
            HTTPSRequests.sendGet(
                url + "people",
                { json ->
                    val ar = ArrayList<ObjPeople>()
                    for (i in 0 until json.getJSONArray("results").length()) {
                        ar.add(ObjPeople(json.getJSONArray("results").getJSONObject(i)))
                    }
                    callback.getPeoples(ar)
                }, "search=$name", "format=json"
            )
        }
    }
}