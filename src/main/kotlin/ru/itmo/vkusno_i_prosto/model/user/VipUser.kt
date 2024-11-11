package ru.itmo.vkusno_i_prosto.model.user

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class VipUser(
    @Id
    val login: String,
    val password: String,
    val username: String,
)