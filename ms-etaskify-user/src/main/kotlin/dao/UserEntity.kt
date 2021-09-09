package com.etaskify.ms.user.dao

import com.etaskify.ms.user.enums.UserStatus
import com.etaskify.ms.user.enums.UserType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "users")
data class UserEntity(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        var id: Long? = null,

        @Column(name = "name")
        var name: String,

        @Column(name = "surname")
        var surname: String,

        @Column(name = "email")
        val email: String,

        @Column(name = "salt")
        var salt: String,

        @Column(name = "login")
        var login: String,

        @Enumerated(EnumType.STRING)
        @Column(name = "status")
        var status: UserStatus?,

        @Column(name = "verifier")
        var verifier: String,

        @Enumerated(EnumType.STRING)
        @Column(name = "user_type")
        var userType: UserType?,

        @Column(name = "organization_id")
        var organizationId: Long,

        @CreationTimestamp
        var createdAt: LocalDateTime? = null,

        @UpdateTimestamp
        var updatedAt: LocalDateTime? = null
)