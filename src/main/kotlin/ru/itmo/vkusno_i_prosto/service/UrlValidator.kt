package ru.itmo.vkusno_i_prosto.service

import org.springframework.stereotype.Service
import java.net.URL

@Service
class UrlValidator {

    fun validate(url: String): Boolean {
        try {
            URL(url)
            return true
        } catch (e: Exception) {
            if (url.isEmpty()) return true
            return false
        }
    }
}