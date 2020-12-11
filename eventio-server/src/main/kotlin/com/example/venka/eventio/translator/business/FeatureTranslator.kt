package com.example.venka.eventio.translator.business

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.dto.business.FeatureDto
import com.example.venka.eventio.data.model.business.Feature
import com.example.venka.eventio.translator.Translator
import org.springframework.stereotype.Service

/**
 * Feature Translator
 */
@Monitor
@Service
class FeatureTranslator : Translator<Feature, FeatureDto, String> {
    override fun fromDto(source: FeatureDto, id: String?): Feature {
        return Feature(
                id ?: source.id,
                source.name
        )
    }

    override fun toDto(source: Feature): FeatureDto {
        return FeatureDto(
                source.id,
                source.name
        )
    }
}
