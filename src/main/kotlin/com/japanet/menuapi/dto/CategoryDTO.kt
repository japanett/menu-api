package com.japanet.menuapi.dto

import java.time.LocalDateTime

data class CategoryDTO(

    val id: Long?,

    val menuId: Long?,

    val name: String?,

    val datUpdate: LocalDateTime?,

    val datCreation: LocalDateTime?
)
