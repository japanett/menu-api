package com.japanet.menuapi.repository

import com.japanet.menuapi.entity.ItemEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<ItemEntity, Long> {
}
