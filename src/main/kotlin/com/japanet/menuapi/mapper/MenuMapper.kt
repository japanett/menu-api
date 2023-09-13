package com.japanet.menuapi.mapper

import com.japanet.menuapi.controller.request.v1.CreateMenuRequest
import com.japanet.menuapi.controller.request.v1.MenuRequest
import com.japanet.menuapi.controller.response.v1.MenuResponse
import com.japanet.menuapi.entity.MenuEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.ReportingPolicy


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface MenuMapper {

    @Mappings(Mapping(target = "id", ignore = true))
    fun toEntity(request: CreateMenuRequest): MenuEntity

    fun toEntity(request: MenuRequest): MenuEntity

    fun toResponse(entity: MenuEntity): MenuResponse
}
