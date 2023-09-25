package com.japanet.menuapi.repository

import com.japanet.menuapi.entity.ItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ItemRepository : JpaRepository<ItemEntity, Long> {

    fun findByIdAndMenuId(id: Long, menuId: Long): Optional<ItemEntity>
}
