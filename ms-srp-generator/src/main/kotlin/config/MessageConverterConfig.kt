package com.etaskify.ms.srp.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.GsonHttpMessageConverter
import springfox.documentation.spring.web.json.Json
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Configuration
class MessageConverterConfig {
    @Bean
    fun gsonHttpMessageConverter(): GsonHttpMessageConverter {
        val converter = GsonHttpMessageConverter()
        converter.gson = gson
        return converter
    }

    companion object {
        private val swaggerSerializer = JsonSerializer { src: Json, _, _ -> JsonParser.parseString(src.value()) }

        private val localDateSerializer = JsonSerializer { date: LocalDate?, _, _ ->
            JsonPrimitive(date?.format(DateTimeFormatter.ISO_LOCAL_DATE))
        }
        private val localTimeSerializer = JsonSerializer { time: LocalTime?, _, _ ->
            JsonPrimitive(time?.format(DateTimeFormatter.ISO_LOCAL_TIME))
        }
        private val localDateTimeSerializer = JsonSerializer { dateTime: LocalDateTime?, _, _ ->
            JsonPrimitive(dateTime?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        }

        private val localDateDeserializer = JsonDeserializer { json, _, _ ->
            LocalDate.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE)
        }

        private val localDateTimeDeserializer = JsonDeserializer { json, _, _ ->
            LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }

        private val localTimeDeserializer = JsonDeserializer { json, _, _ ->
            LocalTime.parse(json.asString, DateTimeFormatter.ISO_LOCAL_TIME)
        }

        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(Json::class.java, swaggerSerializer)
            .registerTypeAdapter(LocalDate::class.java, localDateSerializer)
            .registerTypeAdapter(LocalTime::class.java, localTimeSerializer)
            .registerTypeAdapter(LocalDateTime::class.java, localDateTimeSerializer)
            .registerTypeAdapter(LocalDate::class.java, localDateDeserializer)
            .registerTypeAdapter(LocalTime::class.java, localTimeDeserializer)
            .registerTypeAdapter(LocalDateTime::class.java, localDateTimeDeserializer)
            .create()
    }
}
