package com.example.venka.eventio.service.person

import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.service.Service

/**
 * Service for {@link NaturalPerson}
 */
interface NaturalPersonService : Service<NaturalPerson, String> {

    /**
     * Removes all relationships by natural person id
     *
     * @param id natural person id
     */
    fun detach(id: String)
}
