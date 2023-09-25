package com.japanet.menuapi.controller.request.v1

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

data class CreateCategoryRequest(

    @JsonProperty(required = true)
    var menuId: Long,

    @field:NotNull
    var name: String
)
