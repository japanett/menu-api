package com.japanet.menuapi.service

import com.japanet.menuapi.dto.ItemDTO
import com.japanet.menuapi.mapper.ItemMapper
import com.japanet.menuapi.repository.ItemRepository
import com.japanet.menuapi.utils.log.Logging
import org.springframework.stereotype.Service

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


}
