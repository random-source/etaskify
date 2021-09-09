package com.etaskify.ms.user

import com.etaskify.ms.user.dao.OrganizationEntity
import com.etaskify.ms.user.dao.OrganizationRepo
import com.etaskify.ms.user.exception.OrganizationCreateException
import com.etaskify.ms.user.model.OrganizationDto
import com.etaskify.ms.user.service.OrganizationService
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class OrganizationServiceTest extends Specification{

    private OrganizationRepo repo

    private OrganizationService service

    private EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    void setup(){
        repo = Mock()

        service = new OrganizationService(repo)
    }

    def "createOrganization success"() {
        given:
        def req = random.nextObject(OrganizationDto)

        when:
        service.createOrganization(req)

        then:
        1 * repo.existsByPhoneNumber(req.phoneNumber) >> false
        1 * repo.save(_) >> {
            args ->
                def entity = args.get(0) as OrganizationEntity
                entity.id = 1
                assert entity.organizationName == req.organizationName
                assert entity.phoneNumber == req.phoneNumber
                assert entity.address == req.address
                return entity
        }
    }

    def "createOrganization exists case"() {
        given:
        def req = random.nextObject(OrganizationDto)

        when:
        service.createOrganization(req)

        then:
        1 * repo.existsByPhoneNumber(req.phoneNumber) >> true

        OrganizationCreateException ex = thrown()

        ex.code == "error.organizationCreate.exists"
    }

}
