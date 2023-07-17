package me.sweetie.starwarssearcher

import android.content.Context
import android.content.SharedPreferences
import me.sweetie.starwarssearcher.objects.ObjPeople
import me.sweetie.starwarssearcher.objects.ObjStarship

/**
 * класс для сохранения кораблей или людей
 */
class Saves(context: Context) {
    companion object {
        const val APP_PREFERENCES = "favorite"
        var peopleCache = ArrayList<ObjPeople>()
        var starShipCache = ArrayList<ObjStarship>()
        lateinit var INSTANCE: Saves
    }

    private var mSettings: SharedPreferences
    var changed = true

    init {
        INSTANCE = this
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun savePeople(objPeople: ObjPeople) {
        changed = true
        val editor = mSettings.edit()
        val ar = getSaveObj("peoples")
        ar.add(objPeople.url)
        peopleCache.add(objPeople)
        if (getOneStr(ar) == null)
            editor.remove("peoples")
        else
            editor.putString("peoples", getOneStr(ar))
        editor.apply()
    }

    fun removePeople(people: ObjPeople) {
        changed = true
        val editor = mSettings.edit()
        val ar = getSaveObj("peoples")
        ar.remove(people.url)
        removeByUrl(people)
        if (getOneStr(ar) == null)
            editor.remove("peoples")
        else
            editor.putString("peoples", getOneStr(ar))
        editor.apply()
    }

    fun removeStarship(starship: ObjStarship) {
        changed = true
        val editor = mSettings.edit()
        val ar = getSaveObj("starships")
        ar.remove(starship.url)
        removeByUrl(starship)
        if (getOneStr(ar) == null)
            editor.remove("starships")
        else
            editor.putString("starships", getOneStr(ar))
        editor.apply()
    }

    private fun removeByUrl(objPeople: ObjPeople) {
        for (people in peopleCache) {
            if (people.url == objPeople.url) {
                peopleCache.remove(people)
                return
            }
        }
    }

    private fun removeByUrl(objStarship: ObjStarship) {
        for (starship in starShipCache) {
            if (starship.url == objStarship.url) {
                starShipCache.remove(starship)
                return
            }
        }
    }

    fun saveStarShip(starship: ObjStarship) {
        changed = true
        val editor = mSettings.edit()
        val ar = getSaveObj("starships")
        ar.add(starship.url)
        starShipCache.add(starship)
        if (getOneStr(ar) == null)
            editor.remove("starships")
        else
            editor.putString("starships", getOneStr(ar))
        editor.apply()
    }

    private fun getOneStr(ar: ArrayList<String>): String? {
        if (ar.size == 0)
            return null
        val sb = java.lang.StringBuilder()
        for (subStr in ar)
            sb.append(subStr).append(',')
        return sb.toString().substring(0, sb.toString().lastIndexOf(","))
    }

    fun getSaveObj(key: String): ArrayList<String> {
        return if (mSettings.contains(key)) {
            val ar = ArrayList<String>()
            val s = mSettings.getString(key, "")
            for (subStr in s!!.split(","))
                ar.add(subStr)
            ar
        } else
            ArrayList()
    }
}