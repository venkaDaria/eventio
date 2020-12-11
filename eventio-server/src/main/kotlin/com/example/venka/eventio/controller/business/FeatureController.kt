package com.example.venka.eventio.controller.business

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.dto.business.FeatureDto
import com.example.venka.eventio.exception.NotFoundException
import com.example.venka.eventio.service.business.FeatureService
import com.example.venka.eventio.translator.business.FeatureTranslator
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for {@link Feature} entity
 */
@Monitor
@RestController
@RequestMapping("/feature")
class FeatureController(
        private val featureService: FeatureService,
        private val featureTranslator: FeatureTranslator
) : Logging by LoggingImpl<FeatureController>() {

    @GetMapping
    fun getAll(): List<FeatureDto> {
        log.debug("GET /feature => get all features")

        return featureService.getAll().map { featureTranslator.toDto(it) }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): FeatureDto? {
        log.debug("GET /feature/$id => get feature by id")

        val feature = featureService.getById(id) ?: throw NotFoundException()
        return featureTranslator.toDto(feature)
    }

    @PostMapping
    @PreAuthorize("hasRole('LEGAL_PERSON')")
    fun add(@RequestBody body: FeatureDto): FeatureDto? {
        log.debug("POST /feature => add feature with body data")
        log.trace(body.toString())

        val newFeature = featureTranslator.fromDto(body)
        featureService.save(newFeature)

        log.trace("Added feature: $newFeature")

        return featureTranslator.toDto(newFeature)
    }

    @PutMapping
    @PreAuthorize("hasRole('LEGAL_PERSON')")
    fun save(@RequestBody body: FeatureDto): FeatureDto? {
        log.debug("POST /feature => add feature with body data")
        log.trace(body.toString())

        val feature = featureService.getById(body.id!!) ?: throw NotFoundException()

        val newFeature = featureTranslator.fromDto(body, feature.id)
        featureService.save(newFeature)

        log.trace("Added feature: $newFeature")

        return featureTranslator.toDto(newFeature)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LEGAL_PERSON')")
    fun remove(@PathVariable id: String): FeatureDto? {
        log.debug("DELETE /feature/$id => delete feature by id")

        val feature = featureService.getById(id) ?: throw NotFoundException()
        featureService.deleteById(id)

        log.trace("Deleted feature: $feature")

        return featureTranslator.toDto(feature)
    }
}
