package com.japanet.menuapi.service

import com.japanet.menuapi.controller.request.v1.CreateMenuRequest
import com.japanet.menuapi.controller.request.v1.MenuRequest
import com.japanet.menuapi.entity.MenuEntity
import com.japanet.menuapi.exception.DuplicateEstablishmentException
import com.japanet.menuapi.exception.MenuNotFoundException
import com.japanet.menuapi.mapper.MenuMapper
import com.japanet.menuapi.repository.MenuRepository
import com.japanet.menuapi.utils.log.Logging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class MenuService(
    private val mapper: MenuMapper,
    private val repository: MenuRepository
) {

    @Logging
    fun create(request: CreateMenuRequest): MenuEntity = runCatching {
        val entity = mapper.toEntity(request)
        repository.save(entity)
    }.onFailure {
        if (it is DataIntegrityViolationException) throw DuplicateEstablishmentException("Establishment {${request.establishmentId}} already has a menu")
        else throw it
    }.getOrThrow()

    @Logging
    fun retrieveByFilter(request: MenuRequest, pageable: Pageable): Page<MenuEntity> {
        val example: Example<MenuEntity> = Example.of(mapper.toEntity(request))
        val pagedResult = repository.findAll(example, pageable)

        return if (pagedResult.content.isNotEmpty()) pagedResult
            else throw MenuNotFoundException("Menu not found with parameters: $request")
    }

    fun retrieveById(menuId: Long): MenuEntity = repository.findById(menuId)
        .orElseThrow { MenuNotFoundException("Menu not found with id: $menuId") }
}
