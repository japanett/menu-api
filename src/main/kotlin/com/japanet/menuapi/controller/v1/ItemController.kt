package com.japanet.menuapi.controller.v1

import com.japanet.menuapi.controller.request.v1.AssignAdditionalItemRequest
import com.japanet.menuapi.controller.request.v1.CreateItemRequest
import com.japanet.menuapi.controller.request.v1.ItemRequest
import com.japanet.menuapi.controller.response.v1.ItemResponse
import com.japanet.menuapi.mapper.ItemMapper
import com.japanet.menuapi.service.ItemService
import com.japanet.menuapi.utils.log.Logging
import io.swagger.annotations.ApiOperation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
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

    @Logging
    @GetMapping
    @ApiOperation("Consulta items pelos parametros informados")
    fun retrieve(
        request: ItemRequest,
        @PageableDefault pageable: Pageable
    ): Page<ItemResponse> = service.retrieveByFilter(request, pageable)
        .map { mapper.toResponse(it) }


    /*

        TODO PATCH item

        TODO DELETE item

     */


    @Logging
    @ResponseStatus(CREATED)
    @PostMapping(value = ["/{id}/additional-item"])
    @ApiOperation("Atribui um novo adicional para um item")
    fun assignAdditionalItem(
        @PathVariable id: Long,
        @RequestBody @Valid request: AssignAdditionalItemRequest
    ): ItemResponse = service.assignAdditionalItem(id, request)
        .let { mapper.toResponse(it) }


    // TODO unassignAdditionalItem()
}
