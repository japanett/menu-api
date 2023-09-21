package com.japanet.menuapi.controller.response.v1

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ItemResponse(
    val id: Long?,

    val menuId: Long?,

    val categoryId: Long?,

    val category: String?,

    val name: String?,

    val description: String?,

    val price: BigDecimal?,

    val datUpdate: LocalDateTime?,

    val datCreation: LocalDateTime?
)
