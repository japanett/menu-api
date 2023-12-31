package com.japanet.menuapi.service

import com.japanet.menuapi.controller.request.v1.AssignAdditionalItemRequest
import com.japanet.menuapi.controller.request.v1.ChangeItemCategoryRequest
import com.japanet.menuapi.controller.request.v1.ItemRequest
import com.japanet.menuapi.controller.request.v1.PatchItemRequest
import com.japanet.menuapi.dto.ItemDTO
import com.japanet.menuapi.entity.ItemEntity
import com.japanet.menuapi.exception.AdditionalItemAlreadyAssignedException
import com.japanet.menuapi.exception.ItemNotFoundException
import com.japanet.menuapi.exception.UnassignAdditionalItemException
import com.japanet.menuapi.mapper.ItemMapper
import com.japanet.menuapi.repository.ItemRepository
import com.japanet.menuapi.utils.FormatValueUtils
import com.japanet.menuapi.utils.log.Logging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
            categoryService.retrieveByIdAndMenuId(this.categoryId!!, this.menuId))
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
    fun patch(request: PatchItemRequest, id: Long): ItemDTO  = run {
        val entity = repository.findById(id)
            .orElseThrow { ItemNotFoundException("Item not found with id: $id") }

        request.changes.forEach { entry ->
            run {
                when (entry.key) {
                    "name" -> entity.name = entry.value.toString()
                    "description" -> entity.description = entry.value.toString()
                    "price" -> entity.price = FormatValueUtils.convertPrice(request.price)
                }
            }
        }
        repository.save(entity)
    }.let { mapper.toDTO(it) }

    @Logging
    @Transactional
    fun assignAdditionalItem(id: Long, request: AssignAdditionalItemRequest): ItemDTO = runCatching {
        val item = retrieveItemByIdAndMenuId(id, request.menuId)
        val additionalItem = additionalItemService.retrieveByIdAndMenuId(request.additionalItemId, request.menuId)

        item.additionalItems?.add(additionalItem)

        mapper.toDTO(repository.saveAndFlush(item))
    }.onFailure {
        if (it is DataIntegrityViolationException) throw AdditionalItemAlreadyAssignedException("Item with id: $id already has Additional Item with id: ${request.additionalItemId}")
        else throw it
    }.getOrThrow()

    @Logging
    @Transactional
    fun unassignAdditionalItem(id: Long, request: AssignAdditionalItemRequest): ItemDTO = run {
        val item = retrieveItemByIdAndMenuId(id, request.menuId)
        val additionalItem = additionalItemService.retrieveByIdAndMenuId(request.additionalItemId, request.menuId)

        if (item.additionalItems?.contains(additionalItem) == false) throw UnassignAdditionalItemException("Item with id: $id does not contain additional item with id: ${request.additionalItemId}")

        item.additionalItems?.remove(additionalItem)

        repository.saveAndFlush(item)
    }.let { mapper.toDTO(it) }

    @Logging
    fun changeCategory(id: Long, request: ChangeItemCategoryRequest): ItemDTO = retrieveItemByIdAndMenuId(id, request.menuId)
        .apply { this.category = categoryService.retrieveByIdAndMenuId(request.categoryId, request.menuId) }
        .run { repository.save(this) }
        .let { mapper.toDTO(it) }

    @Logging
    fun delete(id: Long) {
        runCatching {
            repository.deleteById(id)
        }.onFailure {
            if (it is EmptyResultDataAccessException) throw ItemNotFoundException("Item not found with id: $id")
            else throw it
        }
    }

    private fun retrieveItemByIdAndMenuId(id: Long, menuId: Long): ItemEntity = repository.findByIdAndMenuId(id, menuId)
        .orElseThrow { ItemNotFoundException("Item not found with id: $id and menuId: $menuId") }
}
