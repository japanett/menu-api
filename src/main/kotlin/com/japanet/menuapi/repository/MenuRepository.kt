package com.japanet.menuapi.repository

import com.japanet.menuapi.entity.MenuEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MenuRepository : JpaRepository<MenuEntity, Long> {
}
