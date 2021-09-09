package com.etaskify.ms.user.dao

import org.springframework.data.jpa.repository.JpaRepository

interface OrganizationRepo: JpaRepository<OrganizationEntity, Long> {

    fun existsByPhoneNumber(phoneNumber: String): Boolean


}