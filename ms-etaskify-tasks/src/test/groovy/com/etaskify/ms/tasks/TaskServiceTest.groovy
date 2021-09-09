package com.etaskify.ms.tasks

import com.etaskify.ms.tasks.client.user.UserClient
import com.etaskify.ms.tasks.client.user.UserDto
import com.etaskify.ms.tasks.client.user.UserStatus
import com.etaskify.ms.tasks.client.user.UserType
import com.etaskify.ms.tasks.dao.TaskEntity
import com.etaskify.ms.tasks.dao.TaskRepo
import com.etaskify.ms.tasks.enums.TaskStatus
import com.etaskify.ms.tasks.exceptions.TaskCreationException
import com.etaskify.ms.tasks.exceptions.TaskException
import com.etaskify.ms.tasks.exceptions.UserAdminException
import com.etaskify.ms.tasks.exceptions.UserException
import com.etaskify.ms.tasks.mapper.TaskMapperKt
import com.etaskify.ms.tasks.model.TaskCreationDto
import com.etaskify.ms.tasks.service.NotifyService
import com.etaskify.ms.tasks.service.TaskService
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class TaskServiceTest extends Specification{

    private TaskRepo taskRepo
    private UserClient userClient
    private NotifyService notifyService

    private TaskService service

    private EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    void setup(){

        taskRepo = Mock()
        userClient = Mock()
        notifyService = Mock()

        service = new TaskService(taskRepo, userClient, notifyService)
    }

    def "createTask success" (){
        given:
        def userId = 1
        def req = random.nextObject(TaskCreationDto)
        def userDto = random.nextObject(UserDto)
        userDto.id = userId

        when:
        service.createTask(userId, req)

        then:
        1 * userClient.getUserById(userId) >> userDto
        1 * taskRepo.save(_) >> {
            args->
                def entity = args.get(0) as TaskEntity
                assert entity.createdUserId == userDto.id
                assert entity.createdUserName == userDto.name
                assert entity.createdUserSurname == userDto.surname
                assert entity.deadline == req.deadline
                assert entity.title == req.title
                assert entity.description == req.description
                assert entity.assignedUserId == 0
                assert entity.assignedUserName == null
                assert entity.assignedUserSurname == null
        }
    }

    def "createTask error" (){
        given:
        def userId = 1
        def req = new TaskCreationDto(0, null, TaskStatus.CREATED,"test",  new Date())
        def userDto = random.nextObject(UserDto)
        userDto.id = userId

        when:
        service.createTask(userId, req)

        then:

        TaskCreationException ex = thrown()
        ex.code == "error.taskCreate.EmptyTaskParam"
    }

    def "createTask user is not manager" (){
        given:
        def userId = 1
        def req = random.nextObject(TaskCreationDto)
        def userDto = random.nextObject(UserDto)
        userDto.id = userId
        userDto.userType = UserType.EMPLOYEE

        when:
        service.createTask(userId, req)

        then:
        1 * userClient.getUserById(userId) >> userDto

        UserAdminException ex = thrown()
        ex.code == "error.createUser.notAdmin"
    }

    def "getTasksList success"(){
        given:
        def userId = 1
        def userDto = random.nextObject(UserDto)
        def entity = random.nextObject(TaskEntity)
        List<TaskEntity> list = new ArrayList()
        list.add(entity)

        def response = use(TaskMapperKt) {
            list.toDtoList()
        }

        userDto.id = userId

        when:
        def actual = service.getTasksList(userId)

        then:
        1 * userClient.getUserById(userId) >> userDto
        1 * taskRepo.findAllByOrganizationId(userDto.organizationId) >> list

        noExceptionThrown()
        actual == response
    }

    def "getUserTasks success"(){
        given:
        def userId = 1
        def entity = random.nextObject(TaskEntity)
        List<TaskEntity> list = new ArrayList()
        list.add(entity)
        def response = use(TaskMapperKt) {
            list.toDtoList()
        }

        when:
        def actual = service.getUserTasks(userId)

        then:
        1 * taskRepo.findAllByAssignedUserId(userId) >> list

        noExceptionThrown()
        actual == response
    }

    def "assigneeTask success"(){
        given:
        def userId = 1
        def assignedUserId = 2

        def userDto = random.nextObject(UserDto)
        def assignedUserDto = random.nextObject(UserDto)
        def entity = random.nextObject(TaskEntity)
        def taskId = entity.id

        userDto.id = userId
        userDto.userType = UserType.MANAGER
        assignedUserDto.id = assignedUserId
        assignedUserDto.status = UserStatus.ACTIVE

        when:
        service.assigneeTask(userId, assignedUserId, taskId)

        then:
        1 * userClient.getUserById(userId) >> userDto
        1 * userClient.getUserById(assignedUserId) >> assignedUserDto
        1 * taskRepo.findById(taskId) >> Optional.of(entity)
        1 * taskRepo.save(_) >> {
            args->
                def taskEntity = args.get(0) as TaskEntity
                assert taskEntity.createdUserId == entity.createdUserId
                assert taskEntity.createdUserName == entity.createdUserName
                assert taskEntity.createdUserSurname == entity.createdUserSurname
                assert taskEntity.deadline == entity.deadline
                assert taskEntity.title == entity.title
                assert taskEntity.description == entity.description
                assert taskEntity.assignedUserId == assignedUserDto.id
                assert taskEntity.assignedUserName == assignedUserDto.name
                assert taskEntity.assignedUserSurname == assignedUserDto.surname
                return taskEntity
        }
        1 * notifyService.notifyUser(userDto.name, userDto.surname, entity.title)

    }

    def "assigneeTask error"(){
        given:
        def userId = 1
        def assignedUserId = 2

        def userDto = random.nextObject(UserDto)
        def assignedUserDto = random.nextObject(UserDto)
        def entity = random.nextObject(TaskEntity)
        def taskId = entity.id

        userDto.id = userId
        userDto.userType = UserType.EMPLOYEE
        assignedUserDto.id = assignedUserId
        assignedUserDto.status = UserStatus.ACTIVE

        when:
        service.assigneeTask(userId, assignedUserId, taskId)

        then:
        1 * userClient.getUserById(userId) >> userDto

        UserAdminException ex = thrown()
        ex.code == "error.createUser.notAdmin"

    }

    def "assigneeTask task not found"(){
        given:
        def userId = 1
        def assignedUserId = 2

        def userDto = random.nextObject(UserDto)
        def assignedUserDto = random.nextObject(UserDto)
        def entity = random.nextObject(TaskEntity)
        def taskId = entity.id

        userDto.id = userId
        userDto.userType = UserType.MANAGER
        assignedUserDto.id = assignedUserId
        assignedUserDto.status = UserStatus.ACTIVE

        when:
        service.assigneeTask(userId, assignedUserId, taskId)

        then:
        1 * userClient.getUserById(userId) >> userDto
        1 * userClient.getUserById(assignedUserId) >> assignedUserDto

        TaskException ex = thrown()
        ex.code == "error.assigneeTask.notFound"
    }
}
