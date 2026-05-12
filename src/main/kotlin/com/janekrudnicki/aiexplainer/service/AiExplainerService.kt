package com.janekrudnicki.aiexplainer.service

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class AiExplainerService {

    private val apiUrl = "https://api.groq.com/openai/v1/chat/completions"
    private val httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build()
    private val gson = Gson()

    fun getExplanation(apiKey: String, prompt: String): String {
        val root = JsonObject().apply {
            addProperty("model", "llama-3.3-70b-versatile")
            add("messages", gson.toJsonTree(listOf(mapOf("role" to "user", "content" to prompt))))
        }

        val request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $apiKey")
            .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(root)))
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            throw Exception("API Error: ${response.statusCode()} - ${response.body()}")
        }

        return parseResponse(response.body())
    }

    private fun parseResponse(responseBody: String): String {
        return try {
            val jsonResponse = gson.fromJson(responseBody, JsonObject::class.java)
            val choices = jsonResponse.getAsJsonArray("choices")
            if (choices != null && !choices.isEmpty) {
                val message = choices[0].asJsonObject.getAsJsonObject("message")
                message?.get("content")?.asString ?: "No explanation found in API response."
            } else {
                "No explanation found in API response."
            }
        } catch (e: Exception) {
            throw Exception("Failed to parse AI response: ${e.message}")
        }
    }
}