package com.japanet.menuapi.controller.v1

import com.japanet.menuapi.controller.request.v1.CreateItemRequest
import com.japanet.menuapi.controller.response.v1.ItemResponse
import com.japanet.menuapi.mapper.ItemMapper
import com.japanet.menuapi.service.ItemService
import com.japanet.menuapi.utils.log.Logging
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/items")
class ItemController(
    private val service: ItemService,
    private val mapper: ItemMapper
) {

    @Logging
    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Cria um novo item p/ um menu")
    fun create(
        @RequestBody @Valid request: CreateItemRequest
    ): ItemResponse = request
        .let { mapper.toDTO(it) }
        .run { service.create(this) }
        .let { mapper.toResponse(it) }
}
