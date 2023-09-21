package com.japanet.menuapi.service

import com.japanet.menuapi.controller.request.v1.ItemRequest
import com.japanet.menuapi.dto.ItemDTO
import com.japanet.menuapi.mapper.ItemMapper
import com.japanet.menuapi.repository.ItemRepository
import com.japanet.menuapi.utils.log.Logging
import org.springframework.stereotype.Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import com.japanet.menuapi.exception.ItemNotFoundException
import com.japanet.menuapi.entity.ItemEntity
import org.springframework.data.domain.Example

@Service
class ItemService(
    private val mapper: ItemMapper,
    private val repository: ItemRepository,
    private val menuService: MenuService,
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

}
