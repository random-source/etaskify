package com.etaskify.ms.user.dao

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "organizations")
data class OrganizationEntity(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        var id: Long? = null,

        @Column(name = "address")
        var address: String,

        @Column(name = "phone_number")
        var phoneNumber: String,

        @Column(name = "organization_name")
        var organizationName: String,

        @CreationTimestamp
        var createdAt: LocalDateTime? = null,

        @UpdateTimestamp
        var updatedAt: LocalDateTime? = null
)