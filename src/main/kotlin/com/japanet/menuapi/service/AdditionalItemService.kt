package com.japanet.menuapi.service

import com.japanet.menuapi.controller.request.v1.AdditionalItemRequest
import com.japanet.menuapi.dto.AdditionalItemDTO
import com.japanet.menuapi.entity.AdditionalItemEntity
import com.japanet.menuapi.exception.AdditionalItemNotFoundException
import com.japanet.menuapi.mapper.AdditionalItemMapper
import com.japanet.menuapi.repository.AdditionalItemRepository
import com.japanet.menuapi.utils.log.Logging
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

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

    fun retrieveByIdAndMenuId(id: Long, menuId: Long) = repository.findByIdAndMenuId(id, menuId)
        .orElseThrow { AdditionalItemNotFoundException("AdditionalItem not found with id: $id and menuId: $menuId") }
}
