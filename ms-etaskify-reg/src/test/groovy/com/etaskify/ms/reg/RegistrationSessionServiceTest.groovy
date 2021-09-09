package com.etaskify.ms.reg

import com.etaskify.ms.reg.service.RegistrationSessionService
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.redisson.api.RBucket
import org.redisson.api.RedissonClient
import spock.lang.Specification

class RegistrationSessionServiceTest extends Specification {

    private RedissonClient redissonClient
    private RegistrationSessionService service

    private EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    void setup() {
        redissonClient = Mock()
        service = new RegistrationSessionService(redissonClient)
    }

    def "getSessionData bucket exists"() {
        given:
        def phoneNumber = "+9945055555555"
        def name = "organization name"
        def address = "organization address"
        def sessionKey = "key"

        when:
        service.getSessionData(sessionKey)

        then:
        def bucket = Mock(RBucket) {
            1 * it.exists >> true
            1 * it.get() >> new RegistrationSessionService.OrganizationCache(name, phoneNumber, address)
        }

        1 * redissonClient.getBucket(_ as String) >> {
            args ->
                String arg = args.get(0)
                assert arg.split(":")[1] == sessionKey
                return bucket
        }
    }

    def "getSessionData bucket not exists"() {
        given:
        def phoneNumber = "+9945055555555"
        def name = "organization name"
        def address = "organization address"
        def sessionKey = "key"

        when:
        service.getSessionData(sessionKey)

        then:
        def bucket = Mock(RBucket) {
            1 * it.exists >> false
            0 * it.get() >> new RegistrationSessionService.OrganizationCache(name, phoneNumber, address)
        }
        1 * redissonClient.getBucket(_ as String) >> {
            args ->
                String arg = args.get(0)
                assert arg.split(":")[1] == sessionKey
                return bucket
        }
        thrown(RuntimeException)
    }

    def "DeleteSessionDataByPhone"() {
        given:
        def sessionKey = "key"

        when:
        service.deleteRegistrationSession(sessionKey)

        then:
        def bucket = Mock(RBucket) {
            1 * it.delete()
        }
        1 * redissonClient.getBucket(_ as String) >> {
            args ->
                String arg = args.get(0)
                assert arg.split(":")[1] == sessionKey
                return bucket
        }
    }
}
