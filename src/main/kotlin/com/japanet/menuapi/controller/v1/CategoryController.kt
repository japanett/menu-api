package com.japanet.menuapi.controller.v1

import com.japanet.menuapi.controller.request.v1.CategoryRequest
import com.japanet.menuapi.controller.request.v1.CreateCategoryRequest
import com.japanet.menuapi.controller.request.v1.PatchCategoryRequest
import com.japanet.menuapi.controller.response.v1.CategoryResponse
import com.japanet.menuapi.mapper.CategoryMapper
import com.japanet.menuapi.service.CategoryService
import com.japanet.menuapi.utils.log.Logging
import io.swagger.annotations.ApiOperation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/categories")
class CategoryController(
    private val service: CategoryService,
    private val mapper: CategoryMapper
){

    @Logging
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Cria uma nova categoria")
    fun create(@RequestBody @Valid request: CreateCategoryRequest): CategoryResponse = request
        .let { mapper.toDTO(it) }
        .run { service.create(this) }
        .let { mapper.toResponse(it) }

    @Logging
    @GetMapping
    @ApiOperation("Consulta categorias pelos parametros informados")
    fun retrieve(
        request: CategoryRequest,
        @PageableDefault pageable: Pageable
    ): Page<CategoryResponse> = service.retrieveByFilter(request, pageable)
        .map { mapper.toResponse(it) }

    @Logging
    @PatchMapping(value = ["/{id}"])
    @ResponseStatus(OK)
    @ApiOperation("Atualiza categoria pelos parametros informados")
    fun patch(
        @RequestBody @Valid request: PatchCategoryRequest,
        @PathVariable id: Long
    ): CategoryResponse = service.patch(request, id)
        .let { mapper.toResponse(it) }
}
