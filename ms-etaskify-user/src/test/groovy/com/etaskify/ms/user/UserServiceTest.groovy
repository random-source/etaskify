package com.etaskify.ms.user

import com.etaskify.ms.user.client.auth.AuthClient
import com.etaskify.ms.user.client.auth.PasswordGenerationReqDto
import com.etaskify.ms.user.client.auth.PasswordGenerationRespDto
import com.etaskify.ms.user.dao.OrganizationRepo
import com.etaskify.ms.user.dao.UserEntity
import com.etaskify.ms.user.dao.UserRepo
import com.etaskify.ms.user.enums.UserStatus
import com.etaskify.ms.user.enums.UserType
import com.etaskify.ms.user.exception.UserAdminException
import com.etaskify.ms.user.exception.UserCreateException
import com.etaskify.ms.user.model.UserCreationDto
import com.etaskify.ms.user.model.UserDto
import com.etaskify.ms.user.service.UserService
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class UserServiceTest extends Specification{

    private UserRepo repo
    private AuthClient authClient
    private OrganizationRepo organizationRepo

    private UserService service

    private EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    void setup(){
        repo = Mock()
        authClient = Mock()
        organizationRepo = Mock()
        service = new UserService("test", repo, authClient, organizationRepo)
    }

    def "createUser success"() {
        given:
        def req = random.nextObject(UserDto)
        req.status = UserStatus.ACTIVE
        req.userType = UserType.MANAGER

        when:
        service.createUser(req)

        then:
        1 * repo.existsByLogin(req.login) >> false
        1 * organizationRepo.existsById(req.organizationId) >> true
        1 * repo.save(_) >> {
            args ->
                def entity = args.get(0) as UserEntity
                entity.id = 1
                assert entity.salt == req.salt
                assert entity.verifier == req.verifier
                assert entity.name == req.name
                assert entity.surname == req.surname
                assert entity.email == req.email
                assert entity.organizationId == req.organizationId
                assert entity.userType == UserType.MANAGER
                assert entity.status == UserStatus.ACTIVE
                return entity
        }
    }

    def "createUser user exists case"() {
        given:
        def req = random.nextObject(UserDto)

        when:
        service.createUser(req)

        then:
        1 * repo.existsByLogin(req.login) >> true

        UserCreateException ex = thrown()

        ex.code == "error.userCreate.exists"
    }

    def "createUser organization not exist case"() {
        given:
        def req = random.nextObject(UserDto)

        when:
        service.createUser(req)

        then:
        1 * repo.existsByLogin(req.login) >> false
        1 * organizationRepo.existsById(req.organizationId) >> false

        UserCreateException ex = thrown()

        ex.code == "error.userCreate.notExists"
    }

    def "createUserByAdmin success"(){
        given:
        def userId = 1
        def req = random.nextObject(UserCreationDto)
        def adminUser = random.nextObject(UserEntity)
        def resp = random.nextObject(PasswordGenerationRespDto)

        adminUser.id = 1
        adminUser.userType = UserType.MANAGER

        when:
        service.createUserByAdmin(userId, req)

        then:
        1 * repo.findById(userId) >> Optional.of(adminUser)
        1 * repo.existsByEmail(req.email) >> false
        1 * authClient.generatePassword(_) >> resp
        1 * repo.save(_) >> {
            args ->
                def entity = args.get(0) as UserEntity
                entity.id = 1
                assert entity.salt == resp.salt
                assert entity.verifier == resp.verifier
                assert entity.name == req.name
                assert entity.surname == req.surname
                assert entity.email == req.email
                assert entity.organizationId == adminUser.organizationId
                assert entity.userType == UserType.EMPLOYEE
                assert entity.status == UserStatus.RESET_PASSWORD
                return entity
        }
    }

    def "createUserByAdmin user type error"(){
        given:
        def userId = 1
        def req = random.nextObject(UserCreationDto)
        def adminUser = random.nextObject(UserEntity)

        adminUser.id = 1
        adminUser.userType = UserType.EMPLOYEE

        when:
        service.createUserByAdmin(userId, req)

        then:
        1 * repo.findById(userId) >> Optional.of(adminUser)

        UserAdminException ex = thrown()
        ex.code == "error.createUser.notAdmin"
    }

    def "createUserByAdmin user empty param error"(){
        given:
        def userId = 1
        def req = new UserCreationDto(null, "email", "surname")
        def adminUser = random.nextObject(UserEntity)

        adminUser.id = 1
        adminUser.userType = UserType.MANAGER

        when:
        service.createUserByAdmin(userId, req)

        then:
        1 * repo.findById(userId) >> Optional.of(adminUser)

        UserAdminException ex = thrown()
        ex.code == "error.createUser.emptyUserParam"
    }

    def "createUserByAdmin user is exist error"(){
        given:
        def userId = 1
        def req = random.nextObject(UserCreationDto)
        def adminUser = random.nextObject(UserEntity)

        adminUser.id = 1
        adminUser.userType = UserType.MANAGER

        when:
        service.createUserByAdmin(userId, req)

        then:
        1 * repo.findById(userId) >> Optional.of(adminUser)
        1 * repo.existsByEmail(req.email) >> true

        UserCreateException ex = thrown()
        ex.code == "error.createUser.exists"
    }

}
