package com.japanet.menuapi.controller.request.v1

import com.fasterxml.jackson.annotation.JsonProperty

data class AssignAdditionalItemRequest(

    @JsonProperty(required = true)
    val additionalItemId: Long,

    @JsonProperty(required = true)
    val menuId: Long
)
