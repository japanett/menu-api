package com.japanet.menuapi.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class AdditionalItemDTO(

    val id: Long?,

    val menuId: Long?,

    val name: String?,

    val description: String?,

    val price: BigDecimal?,

    val datUpdate: LocalDateTime?,

    val datCreation: LocalDateTime?

)
