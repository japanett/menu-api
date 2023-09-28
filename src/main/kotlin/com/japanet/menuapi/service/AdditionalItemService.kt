package com.japanet.menuapi.service

import com.japanet.menuapi.controller.request.v1.AdditionalItemRequest
import com.japanet.menuapi.controller.request.v1.PatchAdditionalItemRequest
import com.japanet.menuapi.dto.AdditionalItemDTO
import com.japanet.menuapi.entity.AdditionalItemEntity
import com.japanet.menuapi.exception.AdditionalItemNotFoundException
import com.japanet.menuapi.mapper.AdditionalItemMapper
import com.japanet.menuapi.repository.AdditionalItemRepository
import com.japanet.menuapi.utils.FormatValueUtils.Companion.convertPrice
import com.japanet.menuapi.utils.log.Logging
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdditionalItemService(
    private val mapper: AdditionalItemMapper,
    private val menuService: MenuService,
    private val repository: AdditionalItemRepository
) {

    @Logging
    fun create(dto: AdditionalItemDTO): AdditionalItemDTO = dto
        .run { mapper.toEntity(this, menuService.retrieveById(this.menuId!!)) }
        .run { repository.save(this) }
        .let { mapper.toDTO(it) }

    @Logging
    fun retrieveByFilter(request: AdditionalItemRequest, pageable: Pageable): Page<AdditionalItemEntity> {
        val example: Example<AdditionalItemEntity> = Example.of(mapper.toEntity(request))
        val pagedResult = repository.findAll(example, pageable)

        return if (pagedResult.content.isNotEmpty()) pagedResult
        else throw AdditionalItemNotFoundException("AdditionalItem not found with parameters: $request")
    }

    @Logging
    fun patch(request: PatchAdditionalItemRequest, id: Long): AdditionalItemDTO = run {
        val entity = repository.findById(id)
            .orElseThrow { AdditionalItemNotFoundException("AdditionalItem not found with id: $id") }

        request.changes.forEach { entry ->
            run {
                when (entry.key) {
                    "name" -> entity.name = entry.value.toString()
                    "description" -> entity.description = entry.value.toString()
                    "price" -> entity.price = convertPrice(request.price)
                }
            }
        }
        repository.save(entity)
    }.let { mapper.toDTO(it) }

    @Logging
    @Transactional
    fun delete(id: Long) = run {
        val entity = repository.findById(id)
            .orElseThrow { AdditionalItemNotFoundException("AdditionalItem not found with id: $id") }

        // Removes the relationship without deleting parent
        for (item in entity.items!!) { item.additionalItems?.remove(entity) }

        repository.deleteById(id)
    }

    fun retrieveByIdAndMenuId(id: Long, menuId: Long): AdditionalItemEntity = repository.findByIdAndMenuId(id, menuId)
        .orElseThrow { AdditionalItemNotFoundException("AdditionalItem not found with id: $id and menuId: $menuId") }

}
