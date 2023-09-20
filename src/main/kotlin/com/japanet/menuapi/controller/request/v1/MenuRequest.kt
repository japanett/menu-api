package com.japanet.menuapi.controller.request.v1

import java.util.*

data class MenuRequest(

    val id: Long?,

    val customerId: UUID?,

    val establishmentId: Long?
)
