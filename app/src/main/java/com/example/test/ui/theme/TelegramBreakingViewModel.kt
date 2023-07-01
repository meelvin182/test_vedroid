package com.example.test.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface TelegramService {
    @GET
    suspend fun getChannelContent(@Url url: String): ResponseBody
}

class TelegramBreakingViewModel : ViewModel() {
    private val _messages: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val messages: Flow<List<String>> = _messages.asStateFlow()

    init {
        viewModelScope.launch {
            fetchMessages()
        }
    }

    private suspend fun fetchMessages() {
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
        for (element in messageElements.take(20)) {
            val messageText = element.select(".tgme_widget_message_text").text()
            messages.add(messageText)
        }
        println("update?")
        _messages.value = messages
    }
}