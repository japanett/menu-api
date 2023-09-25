package com.japanet.menuapi.controller.request.v1

import java.math.BigDecimal
import javax.validation.constraints.Digits
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateAdditionalItemRequest(

    @field:NotNull
    val menuId: Long,

    @field:NotNull
    val name: String,

    @field:NotBlank
    val description: String?,

    @field:NotNull
    @field:Digits(integer = 9, fraction = 2)
    val price: BigDecimal
)
