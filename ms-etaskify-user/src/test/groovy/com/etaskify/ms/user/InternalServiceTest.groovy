package com.etaskify.ms.user

import com.etaskify.ms.user.dao.UserEntity
import com.etaskify.ms.user.dao.UserRepo
import com.etaskify.ms.user.exception.UserException
import com.etaskify.ms.user.mapper.UserMapperKt
import com.etaskify.ms.user.service.InternalService
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class InternalServiceTest extends Specification{

    private UserRepo repo

    private InternalService service
    private EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    void setup(){
        repo = Mock()
        service = new InternalService(repo)
    }

    def "getUserByLogin success"(){
        given:
        def userEntity = random.nextObject(UserEntity.class)
        def login = userEntity.login
        def userDto = use(UserMapperKt) {
            userEntity.toUserDto()
        }

        when:
        def actual = service.getUserByLogin(login)

        then:
        1 * repo.findByLogin(login) >> userEntity

        noExceptionThrown()
        actual == userDto
    }

    def "getUserById success"(){
        given:
        def userEntity = random.nextObject(UserEntity.class)
        def id = userEntity.id
        def userDto = use(UserMapperKt) {
            userEntity.toUserDto()
        }

        when:
        def actual = service.getUserById(id)

        then:
        1 * repo.findById(id) >> Optional.of(userEntity)

        noExceptionThrown()
        actual == userDto
    }

    def "patchUser successful case"() {
        given:
        def userEntity = random.nextObject(UserEntity.class)
        def userId = userEntity.id
        def userDto = use(UserMapperKt) {
            userEntity.toUserDto()
        }

        when:
        def actual = service.patchUser(userId, userDto)

        then:
        1 * repo.findById(userId) >> Optional.of(userEntity)
        1 * repo.save(userEntity) >> userEntity

        actual == userDto
    }

    def "patchUser user not found case"() {
        given:
        def userEntity = random.nextObject(UserEntity.class)
        def userId = userEntity.id
        def userDto = use(UserMapperKt) {
            userEntity.toUserDto()
        }

        when:
        service.patchUser(userId, userDto)

        then:
        1 * repo.findById(userId) >> Optional.empty()

        UserException ex = thrown()
        ex.code == "error.patchUser.userNotFound"
    }


}
