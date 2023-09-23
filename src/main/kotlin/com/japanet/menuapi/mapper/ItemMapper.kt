package com.japanet.menuapi.mapper

import com.japanet.menuapi.controller.request.v1.CreateItemRequest
import com.japanet.menuapi.controller.request.v1.ItemRequest
import com.japanet.menuapi.controller.response.v1.ItemResponse
import com.japanet.menuapi.dto.ItemDTO
import com.japanet.menuapi.entity.CategoryEntity
import com.japanet.menuapi.entity.ItemEntity
import com.japanet.menuapi.entity.MenuEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.ReportingPolicy

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface ItemMapper {

    fun toDTO(request: CreateItemRequest): ItemDTO

    @Mappings(
        Mapping(source = "entity.category.id", target = "categoryId"),
        Mapping(source = "entity.category.name", target = "categoryName"),
        Mapping(source = "entity.menu.id", target = "menuId")
    )
    fun toDTO(entity: ItemEntity): ItemDTO

    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(source = "menu", target = "menu"),
        Mapping(source = "category", target = "category"),
        Mapping(source = "dto.name", target = "name"),
        Mapping(source = "dto.description", target = "description"),
        Mapping(source = "dto.price", target = "price"),
        Mapping(target = "datUpdate", ignore = true),
        Mapping(target = "datCreation", ignore = true)
    )
    fun toEntity(dto: ItemDTO, menu: MenuEntity, category: CategoryEntity): ItemEntity

    @Mappings(
        Mapping(source = "menuId", target = "menu.id")
    )
    fun toEntity(request: ItemRequest): ItemEntity

    @Mapping(source = "categoryName", target = "category")
    fun toResponse(dto: ItemDTO): ItemResponse

    @Mappings(
        Mapping(source = "entity.menu.id", target = "menuId"),
        Mapping(source = "entity.category.id", target = "categoryId"),
        Mapping(source = "entity.category.name", target = "category")
    )
    fun toResponse(entity: ItemEntity): ItemResponse
}
