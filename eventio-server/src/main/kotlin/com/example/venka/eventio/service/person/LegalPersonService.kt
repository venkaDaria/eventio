package com.example.venka.eventio.service.person

import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.service.Service

/**
 * Service for {@link LegalPerson}
 */
interface LegalPersonService : Service<LegalPerson, String> {

    /**
     * Returns legal person by company url
     *
     * @param url company url
     */
    fun getByCompanyUrl(url: String): LegalPerson?

    /**
     * Changes company url if it's not unique
     *
     * @param legalPerson legal person who's checking
     *
     * @return legal person (company url can be changed)
     */
    fun fixCompanyUrl(legalPerson: LegalPerson): LegalPerson
}
