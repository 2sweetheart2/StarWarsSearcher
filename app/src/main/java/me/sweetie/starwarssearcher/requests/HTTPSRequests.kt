package me.sweetie.starwarssearcher.requests

import me.sweetie.starwarssearcher.interfaces.IJson

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * класс для запросов через [HttpsURLConnection]
 */
class HTTPSRequests {
    companion object {

        fun sendGet(url: String, callback: IJson, vararg params: String) {
            Thread {
                try {
                    var url = url
                    val str = StringBuilder()
                    if (params.isNotEmpty()) {
                        for (s in params) {
                            str.append(s).append("&")
                        }
                        url = url + "?" + str.toString().substring(0, str.lastIndexOf("&"))
                    }
                    val httpClient: HttpsURLConnection =
                        URL(url).openConnection() as HttpsURLConnection
                    httpClient.requestMethod = "GET"
                    httpClient.connectTimeout = 10000
                    httpClient.readTimeout = 10000
                    httpClient.setRequestProperty(
                        "Accept",
                        "application/json"
                    )
                    val br: BufferedReader =
                        if (httpClient.responseCode < HttpURLConnection.HTTP_BAD_REQUEST) BufferedReader(
                            InputStreamReader(httpClient.inputStream)
                        ) else BufferedReader(InputStreamReader(httpClient.errorStream))
                    try {
                        var line: String?
                        val response = java.lang.StringBuilder()
                        while (br.readLine().also { line = it } != null) {
                            response.append(line)
                        }
                        callback.getJson(JSONObject(response.toString()))
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                        callback.getJson(JSONObject("results=[]"))
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    callback.getJson(JSONObject("{'results':[]}"))

                }
            }.start()
        }

    }
}