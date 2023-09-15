package com.japanet.menuapi.controller.request.v1

import javax.validation.constraints.NotNull

data class CreateCategoryRequest(

    @field:NotNull
    var menuId: Long,

    @field:NotNull
    var name: String
)
