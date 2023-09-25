package com.japanet.menuapi.controller

import com.japanet.menuapi.AbstractTest
import com.japanet.menuapi.controller.request.v1.AssignAdditionalItemRequest
import com.japanet.menuapi.controller.request.v1.CreateItemRequest
import org.junit.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

class ItemControllerTest : AbstractTest() {

    companion object {
        private const val URI = "/items"
    }

    @Test
    fun `create item`() {
        val category = super.entitiesGenerator.createCategory()
        val menu = category.menu!!
        val itemName = "Hamburgao do bao"
        val itemPrice = BigDecimal("25.55")
        val itemDescription = "Melhor hamburgao da cidade"

        val payload = super.mapper.writeValueAsBytes(CreateItemRequest(
            menuId = menu.id!!,
            categoryId = category.id!!,
            name = itemName,
            description = itemDescription,
            price = itemPrice
        ))

        super.mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.menuId").value(menu.id))
            .andExpect(jsonPath("$.categoryId").value(category.id))
            .andExpect(jsonPath("$.category").value(category.name))
            .andExpect(jsonPath("$.name").value(itemName))
            .andExpect(jsonPath("$.description").value(itemDescription))
            .andExpect(jsonPath("$.price").value(itemPrice))
            .andExpect(jsonPath("$.datUpdate").exists())
            .andExpect(jsonPath("$.datCreation").exists())
            .andDo(print())
    }

    @Test
    fun `create item with invalid price`() {
        val itemPrice = BigDecimal("25.553")

        val payload = super.mapper.writeValueAsBytes(CreateItemRequest(
            menuId = 321,
            categoryId = 321,
            name = "blablabla",
            description = "bkblbalblalbal",
            price = itemPrice
        ))

        super.mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())

    }

    @Test
    fun `retrieve items by menuId`() {
        val item = super.entitiesGenerator.createItem()

        super.mockMvc.perform(get("$URI?page=0&size=10&menuId=${item.menu.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content.length()").value(1))
            .andExpect(jsonPath("$.content[0].id").exists())
            .andExpect(jsonPath("$.content[0].menuId").value(item.menu.id))
            .andExpect(jsonPath("$.content[0].categoryId").value(item.category!!.id))
            .andExpect(jsonPath("$.content[0].category").value(item.category!!.name))
            .andExpect(jsonPath("$.content[0].name").value(item.name))
            .andExpect(jsonPath("$.content[0].description").value(item.description))
            .andExpect(jsonPath("$.content[0].price").value(item.price))
            .andExpect(jsonPath("$.content[0].datUpdate").exists())
            .andExpect(jsonPath("$.content[0].datCreation").exists())
            .andDo(print())
    }

    @Test
    fun `assign additional item to item`() {
        val item = super.entitiesGenerator.createItem()
        val additionalItem = super.entitiesGenerator.createAdditionalItem(item.menu)

        val payload = super.mapper.writeValueAsBytes(AssignAdditionalItemRequest(
            id = additionalItem.id!!,
            menuId = item.menu.id!!
        ))

        super.mockMvc.perform(post("$URI/${item.id!!}/additional-item").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.menuId").value(item.menu.id))
            .andExpect(jsonPath("$.categoryId").value(item.category!!.id))
            .andExpect(jsonPath("$.category").value(item.category!!.name))
            .andExpect(jsonPath("$.name").value(item.name))
            .andExpect(jsonPath("$.description").value(item.description))
            .andExpect(jsonPath("$.price").value(item.price))
            .andExpect(jsonPath("$.additionalItems.length()").value(1))
            .andExpect(jsonPath("$.additionalItems[0].id").exists())
            .andExpect(jsonPath("$.additionalItems[0].name").exists())
            .andExpect(jsonPath("$.additionalItems[0].description").exists())
            .andExpect(jsonPath("$.additionalItems[0].price").exists())
            .andExpect(jsonPath("$.additionalItems[0].datUpdate").exists())
            .andExpect(jsonPath("$.additionalItems[0].datCreation").exists())
            .andExpect(jsonPath("$.datUpdate").exists())
            .andExpect(jsonPath("$.datCreation").exists())
            .andDo(print())
    }

}
