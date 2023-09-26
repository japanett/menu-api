package com.japanet.menuapi.controller.v1

import com.japanet.menuapi.controller.request.v1.AdditionalItemRequest
import com.japanet.menuapi.controller.request.v1.CreateAdditionalItemRequest
import com.japanet.menuapi.controller.request.v1.PatchAdditionalItemRequest
import com.japanet.menuapi.controller.response.v1.AdditionalItemResponse
import com.japanet.menuapi.mapper.AdditionalItemMapper
import com.japanet.menuapi.service.AdditionalItemService
import com.japanet.menuapi.utils.log.Logging
import io.swagger.annotations.ApiOperation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/additional-items")
class AdditionalItemController(
    private val mapper: AdditionalItemMapper,
    private val service: AdditionalItemService
) {

    @Logging
    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Cria um novo item adicional")
    fun create(
        @RequestBody @Valid request: CreateAdditionalItemRequest
    ): AdditionalItemResponse = request
        .let { mapper.toDTO(request) }
        .run { service.create(this) }
        .let { mapper.toResponse(it) }

    @Logging
    @GetMapping
    @ApiOperation("Consulta additional items pelos parametros informados")
    fun retrieve(
        request: AdditionalItemRequest,
        @PageableDefault pageable: Pageable
    ): Page<AdditionalItemResponse> = service.retrieveByFilter(request, pageable)
        .map { mapper.toResponse(it) }

    @Logging
    @ResponseStatus(OK)
    @PatchMapping(value = ["/{id}"])
    @ApiOperation("Atualiza additional item")
    fun patch(
        @RequestBody @Valid request: PatchAdditionalItemRequest,
        @PathVariable id: Long
    ): AdditionalItemResponse = service.patch(request, id)
        .let { mapper.toResponse(it) }

    @Logging
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = ["/{id}"])
    @ApiOperation("Remove additional item pelo id")
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }

}
