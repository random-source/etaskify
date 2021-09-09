package com.etaskify.ms.user.dao

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepo: JpaRepository<UserEntity, Long> {
    fun findByLogin(login: String): UserEntity?
    fun existsByEmail(email: String): Boolean
    fun existsByLogin(login: String): Boolean
}