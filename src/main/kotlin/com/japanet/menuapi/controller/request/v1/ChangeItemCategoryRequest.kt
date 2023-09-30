package com.japanet.menuapi.controller.request.v1

import com.fasterxml.jackson.annotation.JsonProperty

data class ChangeItemCategoryRequest(

    @JsonProperty(required = true)
    val categoryId: Long,

    @JsonProperty(required = true)
    val menuId: Long
)
