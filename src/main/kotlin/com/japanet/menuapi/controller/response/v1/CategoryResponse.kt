package com.japanet.menuapi.controller.response.v1

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CategoryResponse(

    var id: Long,

    var menuId: Long,

    var name: String,

    var datUpdate: LocalDateTime,

    var datCreation: LocalDateTime
)
