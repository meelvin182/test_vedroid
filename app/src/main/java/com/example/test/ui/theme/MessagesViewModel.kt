package com.example.test.ui.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface TelegramService {
    @GET
    fun getChannelContent(@Url url: String): ResponseBody
}

class MessagesViewModel : ViewModel() {
    private val _messages: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<String>> = _messages.asStateFlow()

    init {
        fetchMessages()
    }

    private fun fetchMessages() {
        val channelUrl = "https://t.me/s/bbbreaking"

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.telegram.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val telegramService = retrofit.create(TelegramService::class.java)
        val responseBody = telegramService.getChannelContent(channelUrl)

        val htmlContent = responseBody.string()
        val document = Jsoup.parse(htmlContent)

        val messages = arrayListOf<String>()

        val messageElements = document.select(".tgme_widget_message_bubble")
        for (element in messageElements.take(5)) {
            val messageText = element.select(".tgme_widget_message_text").text()

            messages.add(messageText)
        }
        _messages.value = messages
    }
}