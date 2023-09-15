package com.japanet.menuapi.service

import com.japanet.menuapi.controller.request.v1.CategoryRequest
import com.japanet.menuapi.dto.CategoryDTO
import com.japanet.menuapi.entity.CategoryEntity
import com.japanet.menuapi.exception.CategoryNotFoundException
import com.japanet.menuapi.mapper.CategoryMapper
import com.japanet.menuapi.repository.CategoryRepository
import com.japanet.menuapi.utils.log.Logging
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val mapper: CategoryMapper,
    private val menuService: MenuService,
    private val repository: CategoryRepository,
) {

    @Logging
    fun create(dto: CategoryDTO): CategoryDTO {
        val menu = menuService.retrieveById(dto.menuId!!)
        val entity = repository.save(mapper.toEntity(dto, menu))
        return mapper.toDTO(entity)
    }

    @Logging
    fun retrieveByFilter(request: CategoryRequest, pageable: Pageable): Page<CategoryEntity> {
        val example: Example<CategoryEntity> = Example.of(mapper.toEntity(request))
        val pagedResult = repository.findAll(example, pageable)

        return if (pagedResult.content.isNotEmpty()) pagedResult
            else throw CategoryNotFoundException("Category not found with parameters: $request")
    }
}
