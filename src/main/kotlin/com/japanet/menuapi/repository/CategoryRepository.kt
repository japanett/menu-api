package com.japanet.menuapi.repository

import com.japanet.menuapi.entity.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<CategoryEntity, Long> {

}
