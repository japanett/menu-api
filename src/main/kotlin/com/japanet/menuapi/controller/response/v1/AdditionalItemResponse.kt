package com.japanet.menuapi.controller.response.v1

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AdditionalItemResponse(
    val id: Long?,

    val menuId: Long?,

    val name: String?,

    val description: String?,

    val price: BigDecimal?,

    val datUpdate: LocalDateTime?,

    val datCreation: LocalDateTime?
)
