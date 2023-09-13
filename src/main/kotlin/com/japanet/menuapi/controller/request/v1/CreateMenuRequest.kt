package com.japanet.menuapi.controller.request.v1

import javax.validation.constraints.NotNull

data class CreateMenuRequest(

    @field:NotNull
    var establishmentId: Long,

    @field:NotNull
    var customerId: Long

)
