package ru.itmo.vkusno_i_prosto.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.itmo.vkusno_i_prosto.model.user.VipUser

interface UserRepository : JpaRepository<VipUser, String> {
    fun existsByLogin(login: String): Boolean
    fun findByLogin(login: String): VipUser?
}