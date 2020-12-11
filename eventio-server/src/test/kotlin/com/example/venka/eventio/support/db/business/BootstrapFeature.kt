package com.example.venka.eventio.support.db.business

import com.example.venka.eventio.data.db.business.FeatureRepository
import com.example.venka.eventio.data.model.business.Feature
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

val feature: Feature = Feature("1", "hello")

val feature2: Feature = Feature("2", "hello 2")

/**
 * Bootstrap support class for Feature testing
 */
@Component
class BootstrapFeature(private val featureRepository: FeatureRepository) : CommandLineRunner {

    override fun run(vararg args: String) {
        featureRepository.save(feature)
        featureRepository.save(feature2)
    }
}
