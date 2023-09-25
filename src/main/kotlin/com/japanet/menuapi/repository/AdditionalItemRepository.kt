package com.japanet.menuapi.repository

import com.japanet.menuapi.entity.AdditionalItemEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AdditionalItemRepository : JpaRepository<AdditionalItemEntity, Long> {
}
