package com.etaskify.ms.reg

import com.etaskify.ms.reg.client.user.OrganizationDto
import com.etaskify.ms.reg.client.user.UserClient
import com.etaskify.ms.reg.client.user.UserDto
import com.etaskify.ms.reg.client.user.UserStatus
import com.etaskify.ms.reg.client.user.UserType
import com.etaskify.ms.reg.exceptions.RegistrationException
import com.etaskify.ms.reg.model.OrganizationInfoDto
import com.etaskify.ms.reg.model.OrganizationInfoResponseDto
import com.etaskify.ms.reg.model.UserInfoDto
import com.etaskify.ms.reg.model.UserResponseDto
import com.etaskify.ms.reg.service.RegistrationService
import com.etaskify.ms.reg.service.RegistrationSessionService
import com.etaskify.ms.reg.util.RegistrationUtil
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class RegistrationServiceTest extends Specification {

    private UserClient userClient
    private RegistrationUtil util
    private RegistrationSessionService session

    private RegistrationService service

    private EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    void setup() {
        userClient = Mock()
        session = Mock()
        util = Mock()

        service = new RegistrationService(userClient, util, session)
    }

    def "createOrganization success" (){
        given:
        def req = random.nextObject(OrganizationInfoDto.class)
        def sessionKey = "sessionKey"

        when:
        service.createOrganization(req)

        then:
        1 * util.validateOrganizationInfo(req)
        1 * session.addSessionData(req) >> sessionKey

        noExceptionThrown()
    }

    def "createUser success case" (){
        given:
        def req = random.nextObject(UserInfoDto.class)
        def organizationCache = random.nextObject(RegistrationSessionService.OrganizationCache.class)
        def organizationDto = new OrganizationDto(null, organizationCache.address,
                organizationCache.phoneNumber, organizationCache.name )
        def createdOrganizationDto = random.nextObject(OrganizationDto.class)
        def resp = new UserResponseDto(req.login, req.email, req.name)

        when:
        def actual = service.createUser(req)

        then:
        1 * util.validateUserInfo(req)
        1 * session.getSessionData(req.sessionKey) >> organizationCache
        1 * userClient.createOrganization(organizationDto) >> createdOrganizationDto
        1 * userClient.createUser(_) >> {
            args ->
                def userReq = args.get(0) as UserDto
                assert userReq.organizationId == createdOrganizationDto.id
                assert userReq.name == req.name
                assert userReq.surname == req.surname
                assert userReq.salt == req.salt
                assert userReq.verifier == req.verifier
                assert userReq.login == req.login
                assert userReq.email == req.email
                assert userReq.status == UserStatus.ACTIVE
                assert userReq.userType == UserType.MANAGER
                return userReq
        }

        actual == resp
    }
}
