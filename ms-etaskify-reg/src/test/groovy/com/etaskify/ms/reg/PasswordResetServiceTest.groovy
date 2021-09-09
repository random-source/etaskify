package com.etaskify.ms.reg

import com.etaskify.ms.reg.client.user.UserClient
import com.etaskify.ms.reg.client.user.UserDto
import com.etaskify.ms.reg.model.PasswordResetReqDto
import com.etaskify.ms.reg.service.PasswordResetService
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class PasswordResetServiceTest extends Specification{
    private UserClient userClient

    private PasswordResetService service

    private EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    void setup() {
        userClient = Mock()

        service = new PasswordResetService(userClient)
    }


    def "reset password success"(){
        given:
        def request = random.nextObject(PasswordResetReqDto.class)
        def user = random.nextObject(UserDto.class)

        when:
        service.resetPassword(request)

        then:
        1 * userClient.getUserByLogin(request.login) >> user
        noExceptionThrown()

    }

}
