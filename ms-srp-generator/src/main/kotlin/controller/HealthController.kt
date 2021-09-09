package com.etaskify.ms.srp.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health")
class HealthController {

    @GetMapping(path = ["/readiness"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun readiness() = ResponseEntity.ok(Response("Ok"))

    @GetMapping(path = ["/liveness"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun liveness() = ResponseEntity.ok(Response("Ok"))

}

data class Response(val result: String)
