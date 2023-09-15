package com.japanet.menuapi.mapper

import com.japanet.menuapi.controller.request.v1.CategoryRequest
import com.japanet.menuapi.controller.request.v1.CreateCategoryRequest
import com.japanet.menuapi.controller.response.v1.CategoryResponse
import com.japanet.menuapi.dto.CategoryDTO
import com.japanet.menuapi.entity.CategoryEntity
import com.japanet.menuapi.entity.MenuEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.ReportingPolicy

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface CategoryMapper {

    fun toDTO(request: CreateCategoryRequest): CategoryDTO

    @Mapping(source = "menu.id", target = "menuId")
    fun toDTO(entity: CategoryEntity): CategoryDTO


    @Mappings(
        Mapping(source = "menu", target = "menu"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "datUpdate", ignore = true),
        Mapping(target = "datCreation", ignore = true)
    )
    fun toEntity(dto: CategoryDTO, menu: MenuEntity): CategoryEntity

    @Mapping(source = "menuId", target = "menu.id")
    fun toEntity(request: CategoryRequest): CategoryEntity


    fun toResponse(dto: CategoryDTO): CategoryResponse

    @Mapping(source = "menu.id", target = "menuId")
    fun toResponse(entity: CategoryEntity): CategoryResponse
}
