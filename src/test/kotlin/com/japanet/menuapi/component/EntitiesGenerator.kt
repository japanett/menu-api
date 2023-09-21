package com.japanet.menuapi.component

import com.japanet.menuapi.entity.CategoryEntity
import com.japanet.menuapi.entity.MenuEntity
import com.japanet.menuapi.repository.CategoryRepository
import com.japanet.menuapi.repository.MenuRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.random.Random

@Service
class EntitiesGenerator {

    @Autowired
    private lateinit var menuRepository: MenuRepository

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    fun createEmptyMenu(customerId: UUID? = null): MenuEntity = menuRepository.save(
        MenuEntity(
            customerId = customerId ?: UUID.randomUUID(),
            establishmentId = Random.nextLong(50),
            items = null,
            categories = null
        ))

    fun createThreeCategories(name: String): MenuEntity = createEmptyMenu()
        .also {
            categoryRepository.save(
                CategoryEntity(
                    name = "$name first",
                    menu = it
                ))
            categoryRepository.save(
                CategoryEntity(
                    name = "$name second",
                    menu = it
                ))
            categoryRepository.save(
                CategoryEntity(
                    name = "$name third",
                    menu = it
                ))
        }

    fun createCategory(name: String? = null): CategoryEntity = createEmptyMenu()
        .run {
            categoryRepository.save(
                CategoryEntity(
                    name = name ?: "Category number: ${Random.nextLong(50)}",
                    menu = this
                ))
        }
}
