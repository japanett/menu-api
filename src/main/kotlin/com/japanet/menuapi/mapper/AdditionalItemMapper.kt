package com.japanet.menuapi.mapper

import com.japanet.menuapi.controller.request.v1.AdditionalItemRequest
import com.japanet.menuapi.controller.request.v1.CreateAdditionalItemRequest
import com.japanet.menuapi.controller.response.v1.AdditionalItemResponse
import com.japanet.menuapi.dto.AdditionalItemDTO
import com.japanet.menuapi.entity.AdditionalItemEntity
import com.japanet.menuapi.entity.MenuEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.ReportingPolicy

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AdditionalItemMapper {

    fun toDTO(request: CreateAdditionalItemRequest): AdditionalItemDTO

    @Mapping(source = "menu.id", target = "menuId")
    fun toDTO(entity: AdditionalItemEntity): AdditionalItemDTO

    @Mappings(
        Mapping(source = "menu", target = "menu"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "datUpdate", ignore = true),
        Mapping(target = "datCreation", ignore = true),
        Mapping(source = "dto.name", target = "name"),
        Mapping(source = "dto.description", target = "description"),
        Mapping(source = "dto.price", target = "price")
    )
    fun toEntity(dto: AdditionalItemDTO, menu: MenuEntity): AdditionalItemEntity

    @Mapping(source = "menuId", target = "menu.id")
    fun toEntity(request: AdditionalItemRequest): AdditionalItemEntity

    fun toResponse(dto: AdditionalItemDTO): AdditionalItemResponse

    @Mapping(source = "menu.id", target = "menuId")
    fun toResponse(entity: AdditionalItemEntity): AdditionalItemResponse
}
