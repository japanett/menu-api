package com.japanet.menuapi.controller.v1

import com.japanet.menuapi.controller.request.v1.CreateMenuRequest
import com.japanet.menuapi.controller.request.v1.MenuRequest
import com.japanet.menuapi.controller.response.v1.MenuResponse
import com.japanet.menuapi.mapper.MenuMapper
import com.japanet.menuapi.service.MenuService
import com.japanet.menuapi.utils.log.Logging
import io.swagger.annotations.ApiOperation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping("/menus")
class MenuController(
    private val service: MenuService,
    private val mapper: MenuMapper
) {

    @Logging
    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Cria um novo menu")
    fun create(@RequestBody @Valid request: CreateMenuRequest): MenuResponse = request
        .run { service.create(this) }
        .let { mapper.toResponse(it) }

    @Logging
    @GetMapping
    @ApiOperation(value = "Consulta menu pelos parametros informados")
    fun retrieve(
        request: MenuRequest,
        @PageableDefault pageable: Pageable
    ): Page<MenuResponse> = service.retrieveByFilter(request, pageable)
        .map { mapper.toResponse(it) }

}
