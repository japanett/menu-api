package com.japanet.menuapi.component

import com.japanet.menuapi.entity.AdditionalItemEntity
import com.japanet.menuapi.entity.CategoryEntity
import com.japanet.menuapi.entity.ItemEntity
import com.japanet.menuapi.entity.MenuEntity
import com.japanet.menuapi.repository.AdditionalItemRepository
import com.japanet.menuapi.repository.CategoryRepository
import com.japanet.menuapi.repository.ItemRepository
import com.japanet.menuapi.repository.MenuRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

@Service
class EntitiesGenerator {

    @Autowired
    private lateinit var menuRepository: MenuRepository

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var additionalItemRepository: AdditionalItemRepository

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

    fun createCategory(name: String? = null, menu: MenuEntity? = null): CategoryEntity = run {
            categoryRepository.save(
                CategoryEntity(
                    name = name ?: "Category number: ${Random.nextLong(50)}",
                    menu = menu ?: createEmptyMenu()
                ))
        }

    fun createItem(menu: MenuEntity? = null,
                   additionalItem: MutableList<AdditionalItemEntity>? = null
    ): ItemEntity = createCategory(menu = menu)
        .run {
            itemRepository.save(
                ItemEntity(
                    name = "Item number: ${Random.nextLong(50)}",
                    description = "Description number: ${Random.nextLong(50)}",
                    price = BigDecimal("50.0"),
                    category = this,
                    menu = this.menu!!,
                    additionalItems = additionalItem
                ))
        }

    fun createAdditionalItem(menu: MenuEntity? = null): AdditionalItemEntity =
        additionalItemRepository.save(
            AdditionalItemEntity(
                menu = menu?: createEmptyMenu(),
                name = "Additional item ${Random.nextLong(50)}",
                description = "Description ${Random.nextLong(50)}",
                price = BigDecimal("1.99")
            )
        )

}

