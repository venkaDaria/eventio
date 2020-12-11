package com.example.venka.eventio.service.business

import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.service.Service
import java.lang.UnsupportedOperationException
import java.util.Optional

/**
 * Service for {@link Room}
 */
interface RoomService : Service<Room, String> {

    override fun getByParam(param: String) = throw UnsupportedOperationException()
}
