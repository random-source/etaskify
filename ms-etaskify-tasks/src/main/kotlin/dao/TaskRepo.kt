package com.etaskify.ms.tasks.dao

import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepo: JpaRepository<TaskEntity, Long> {
    fun findAllByOrganizationId(organizationId: Long): List<TaskEntity>
    fun findAllByAssignedUserId(assignedUserId: Long): List<TaskEntity>
}