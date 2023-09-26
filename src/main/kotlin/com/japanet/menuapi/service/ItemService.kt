package com.japanet.menuapi.service

import com.japanet.menuapi.controller.request.v1.AssignAdditionalItemRequest
import com.japanet.menuapi.controller.request.v1.ItemRequest
import com.japanet.menuapi.dto.ItemDTO
import com.japanet.menuapi.entity.ItemEntity
import com.japanet.menuapi.exception.AdditionalItemAlreadyAssignedException
import com.japanet.menuapi.exception.ItemNotFoundException
import com.japanet.menuapi.mapper.ItemMapper
import com.japanet.menuapi.repository.ItemRepository
import com.japanet.menuapi.utils.log.Logging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ItemService(
    private val mapper: ItemMapper,
    private val repository: ItemRepository,
    private val menuService: MenuService,
    private val additionalItemService: AdditionalItemService,
    private val categoryService: CategoryService
) {

    @Logging
    fun create(dto: ItemDTO): ItemDTO = dto
        .run { mapper.toEntity(
            this,
            menuService.retrieveById(this.menuId!!),
            categoryService.retrieveById(this.categoryId!!))
        }
        .run { repository.save(this) }
        .let { mapper.toDTO(it)}


    @Logging
    fun retrieveByFilter(request: ItemRequest, pageable: Pageable): Page<ItemEntity> {
        val example: Example<ItemEntity> = Example.of(mapper.toEntity(request))
        val pagedResult = repository.findAll(example, pageable)

        return if (pagedResult.content.isNotEmpty()) pagedResult
        else throw ItemNotFoundException("Item not found with parameters: $request")
    }

    @Logging
    fun assignAdditionalItem(id: Long, request: AssignAdditionalItemRequest): ItemDTO = runCatching {
        val item = repository.findByIdAndMenuId(id, request.menuId)
            .orElseThrow{ ItemNotFoundException("Item not found with id: $id and menuId: ${request.menuId}") }

        val additionalItem = additionalItemService.retrieveByIdAndMenuId(request.additionalItemId, request.menuId)

        item.additionalItems?.add(additionalItem)

        mapper.toDTO(repository.save(item))
    }.onFailure {
        if (it is DataIntegrityViolationException) throw AdditionalItemAlreadyAssignedException("Item with id: $id already has Additional Item with id: ${request.additionalItemId}")
        else throw it
    }.getOrThrow()

}
