package com.japanet.menuapi.service

import com.japanet.menuapi.controller.request.v1.CategoryRequest
import com.japanet.menuapi.controller.request.v1.PatchCategoryRequest
import com.japanet.menuapi.dto.CategoryDTO
import com.japanet.menuapi.entity.CategoryEntity
import com.japanet.menuapi.exception.CategoryHasItemAssignedException
import com.japanet.menuapi.exception.CategoryNotFoundException
import com.japanet.menuapi.mapper.CategoryMapper
import com.japanet.menuapi.repository.CategoryRepository
import com.japanet.menuapi.utils.log.Logging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val mapper: CategoryMapper,
    private val menuService: MenuService,
    private val repository: CategoryRepository
) {

    @Logging
    fun create(dto: CategoryDTO): CategoryDTO = run {
        val menu = menuService.retrieveById(dto.menuId!!)
        repository.save(mapper.toEntity(dto, menu))
    }.let { mapper.toDTO(it) }

    @Logging
    fun retrieveByFilter(request: CategoryRequest, pageable: Pageable): Page<CategoryEntity> {
        val example: Example<CategoryEntity> = Example.of(mapper.toEntity(request))
        val pagedResult = repository.findAll(example, pageable)

        return if (pagedResult.content.isNotEmpty()) pagedResult
        else throw CategoryNotFoundException("Category not found with parameters: $request")
    }

    @Logging
    fun patch(request: PatchCategoryRequest, id: Long): CategoryDTO = run {
        val entity = repository.findById(id)
            .orElseThrow { CategoryNotFoundException("Category not found with id: $id") }

        request.changes.forEach { entry ->
            run {
                when (entry.key) {
                    "name" -> entity.name = entry.value.toString()
                }
            }
        }
        repository.save(entity)
    }.let { mapper.toDTO(it) }

    @Logging
    fun delete(id: Long) = runCatching {
        repository.deleteById(id)
    }.onFailure {
        if (it is EmptyResultDataAccessException) throw CategoryNotFoundException("Category not found with id: $id")
        if (it is DataIntegrityViolationException) throw CategoryHasItemAssignedException("Category with id: $id has items assigned")
        else throw it
    }

    fun retrieveByIdAndMenuId(id: Long, menuId: Long): CategoryEntity = repository.findByIdAndMenuId(id, menuId)
        .orElseThrow { CategoryNotFoundException("Category not found with id: $id and menuId: $menuId") }

}
