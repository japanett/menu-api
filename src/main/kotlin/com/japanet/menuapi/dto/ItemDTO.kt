package com.japanet.menuapi.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class ItemDTO(

    val id: Long?,

    val menuId: Long?,

    val categoryId: Long?,

    val categoryName: String?,

    val name: String?,

    val description: String?,

    val price: BigDecimal?,

    val datUpdate: LocalDateTime?,

    val datCreation: LocalDateTime?

)
