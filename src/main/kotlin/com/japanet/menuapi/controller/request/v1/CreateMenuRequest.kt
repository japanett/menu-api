package com.japanet.menuapi.controller.request.v1

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.validation.constraints.NotNull

data class CreateMenuRequest(

    @JsonProperty(required = true)
    var establishmentId: Long,

    @field:NotNull
    var customerId: UUID

)
