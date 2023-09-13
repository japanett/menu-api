package com.japanet.menuapi.controller.v1

import com.japanet.menuapi.controller.request.v1.CreateMenuRequest
import com.japanet.menuapi.controller.request.v1.MenuRequest
import com.japanet.menuapi.controller.response.v1.MenuResponse
import com.japanet.menuapi.mapper.MenuMapper
import com.japanet.menuapi.service.MenuService
import io.swagger.annotations.ApiOperation
import mu.KLogger
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
@RequestMapping("/menus")
class MenuController(
    private val service: MenuService,
    private val mapper: MenuMapper
) {

    private val log: KLogger = KotlinLogging.logger {}

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Cria um novo menu")
    fun create(@RequestBody @Valid request: CreateMenuRequest): MenuResponse {
        log.info { "C=${this::class.simpleName}, M=${this::create.name}, request=${request}" }
        return request
            .run { service.create(this) }
            .let { mapper.toResponse(it) }
    }

    @GetMapping
    @ApiOperation(value = "Consulta menu pelos parametros informados")
    fun retrieve(
        request: MenuRequest,
        @PageableDefault pageable: Pageable
    ): Page<MenuResponse> {
        log.info { "C=${this::class.simpleName}, M=${this::retrieve.name}, request=${request}" }
        return service.retrieveByFilter(request, pageable)
            .map { mapper.toResponse(it) }
    }

}
