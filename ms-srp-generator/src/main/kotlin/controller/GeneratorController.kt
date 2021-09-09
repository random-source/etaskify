package com.etaskify.ms.srp.controller


import com.etaskify.ms.srp.service.GeneratorService
import com.etaskify.ms.srp.service.SaltVerifierGeneratorDto
import com.etaskify.ms.srp.service.SrpStep1Dto
import com.etaskify.ms.srp.service.SrpStep2Dto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/generator")
class GeneratorController(
        val generatorService: GeneratorService
) {

    @PostMapping("/sv/generate")
    fun generateSlatVerifier(@RequestBody req: SaltVerifierGeneratorDto) = generatorService.generateSaltAndVerifier(req)

    @PostMapping("/srp/step1")
    fun step1(@RequestBody req: SrpStep1Dto) = generatorService.srpStep1(req)

    @PostMapping("/srp/step2/generate")
    fun generateForSrpStep2(@RequestBody req: SrpStep2Dto) = generatorService.generateForSrpStep2(req)

}