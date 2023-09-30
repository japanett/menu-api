package com.japanet.menuapi.repository

import com.japanet.menuapi.entity.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CategoryRepository : JpaRepository<CategoryEntity, Long> {

    fun findByIdAndMenuId(id: Long, menuId: Long): Optional<CategoryEntity>
}
