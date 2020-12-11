package com.example.venka.eventio.service.business

import com.example.venka.eventio.data.model.business.Feature
import com.example.venka.eventio.service.Service
import java.lang.UnsupportedOperationException

/**
 * Service for {@link Feature}
 */
interface FeatureService : Service<Feature, String> {

    override fun getByParam(param: String) = throw UnsupportedOperationException()
}
