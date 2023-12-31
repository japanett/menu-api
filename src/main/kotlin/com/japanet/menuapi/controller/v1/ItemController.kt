package com.japanet.menuapi.controller.v1

import com.japanet.menuapi.controller.request.v1.*
import com.japanet.menuapi.controller.response.v1.ItemResponse
import com.japanet.menuapi.mapper.ItemMapper
import com.japanet.menuapi.service.ItemService
import com.japanet.menuapi.utils.log.Logging
import io.swagger.annotations.ApiOperation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus.*
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

    @Logging
    @ResponseStatus(OK)
    @PatchMapping(value = ["/{id}"])
    @ApiOperation("Atualiza item pelos parametros informados")
    fun patch(
        @PathVariable id: Long,
        @RequestBody @Valid request: PatchItemRequest
    ): ItemResponse = service.patch(request, id)
        .let { mapper.toResponse(it) }

    @Logging
    @ResponseStatus(OK)
    @PutMapping(value = ["/{id}/category"])
    @ApiOperation("Atualiza categoria do item pelo id")
    fun changeCategory(
        @PathVariable id: Long,
        @RequestBody @Valid request: ChangeItemCategoryRequest
    ): ItemResponse = service.changeCategory(id, request)
        .let { mapper.toResponse(it) }

    @Logging
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = ["/{id}"])
    @ApiOperation("Remove um item pelo id")
    fun delete(@PathVariable id: Long) = service.delete(id)

    @Logging
    @ResponseStatus(CREATED)
    @PostMapping(value = ["/{id}/additional-item"])
    @ApiOperation("Atribui um novo adicional para um item")
    fun assignAdditionalItem(
        @PathVariable id: Long,
        @RequestBody @Valid request: AssignAdditionalItemRequest
    ): ItemResponse = service.assignAdditionalItem(id, request)
        .let { mapper.toResponse(it) }

    @Logging
    @ResponseStatus(OK)
    @DeleteMapping(value = ["/{id}/additional-item"])
    @ApiOperation("Atribui um novo adicional para um item")
    fun unassignAdditionalItem(
        @PathVariable id: Long,
        @RequestBody @Valid request: AssignAdditionalItemRequest
    ): ItemResponse = service.unassignAdditionalItem(id, request)
        .let { mapper.toResponse(it) }
}
