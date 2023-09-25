package com.japanet.menuapi.controller.request.v1

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

data class CreateItemRequest(

    @JsonProperty(required = true)
    val menuId: Long,

    @JsonProperty(required = true)
    val categoryId: Long,

    @field:NotNull
    val name: String,

    val description: String?,

    @field:NotNull
    @field:Digits(integer = 9, fraction = 2)
    val price: BigDecimal

)
