package com.japanet.menuapi.repository

import com.japanet.menuapi.entity.AdditionalItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AdditionalItemRepository : JpaRepository<AdditionalItemEntity, Long> {

    fun findByIdAndMenuId(id: Long, menuId: Long): Optional<AdditionalItemEntity>
}
