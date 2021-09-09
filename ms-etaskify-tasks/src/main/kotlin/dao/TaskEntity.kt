package com.etaskify.ms.tasks.dao

import com.etaskify.ms.tasks.enums.TaskStatus
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tasks")
data class TaskEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        val id: Long? = null,

        @Column(name = "title")
        var title: String,

        @Column(name = "description")
        var description: String,

        @Enumerated(EnumType.STRING)
        @Column(name = "status")
        val status: TaskStatus = TaskStatus.BACKLOG,

        @Column(name = "deadline")
        var deadline:  Date? = null,

        @Column(name = "organization_id")
        var organizationId: Long = 0,

        @Column(name = "created_user_id")
        var createdUserId: Long = 0,

        @Column(name = "created_user_name")
        var createdUserName: String? = null,

        @Column(name = "created_user_surname")
        var createdUserSurname: String? = null,

        @Column(name = "assigned_user_id")
        var assignedUserId: Long = 0,

        @Column(name = "assigned_user_name")
        var assignedUserName: String? = null,

        @Column(name = "assigned_user_surname")
        var assignedUserSurname: String? = null,

        @CreationTimestamp
        var createdAt: LocalDateTime? = null,

        @UpdateTimestamp
        var updatedAt: LocalDateTime? = null
)