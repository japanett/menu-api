package com.japanet.menuapi.controller.request.v1

import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotEmpty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PatchCategoryRequest(@field:NotEmpty val changes: Map<String, Any?> = mapOf()) {

    val name: String? by changes

}
