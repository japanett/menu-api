package com.japanet.menuapi.controller.response.v1

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MenuResponse(
    var id: Long,

    var establishmentId: Long,

    var customerId: Long,

    var datUpdate: LocalDateTime?,

    var datCreation: LocalDateTime,
)
