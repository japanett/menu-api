package com.japanet.menuapi.controller.request.v1

import javax.validation.constraints.NotNull

data class AssignAdditionalItemRequest(

    @field:NotNull
    val id: Long,

    @field:NotNull
    val menuId: Long
)
