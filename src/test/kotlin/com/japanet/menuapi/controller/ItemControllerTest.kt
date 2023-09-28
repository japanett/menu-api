package com.japanet.menuapi.controller

import com.japanet.menuapi.AbstractTest
import com.japanet.menuapi.controller.request.v1.AssignAdditionalItemRequest
import com.japanet.menuapi.controller.request.v1.CreateItemRequest
import org.junit.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

class ItemControllerTest : AbstractTest() {

    companion object {
        private const val URI = "/items"
        private const val MOCK_PATH = "/sql/controller/items"
        private const val PAYLOAD_PATH = "items"
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
    fun `create item with nonexistent menu`() {
        val payload = super.mapper.writeValueAsBytes(CreateItemRequest(
            menuId = 321,
            categoryId = 1232,
            name = "Bla",
            description = null,
            price = BigDecimal(10)
        ))

        super.mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }

    @Test
    fun `create item with nonexistent category`() {
        val menu = super.entitiesGenerator.createEmptyMenu()
        val payload = super.mapper.writeValueAsBytes(CreateItemRequest(
            menuId = menu.id!!,
            categoryId = 1232,
            name = "Bla",
            description = null,
            price = BigDecimal(10)
        ))

        super.mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }

    @Test
    fun `create item with invalid request`() {
        val payload = super.getContent("$PAYLOAD_PATH/create_item_invalid_request")

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
    fun `retrieve items by id not found`() {
        super.mockMvc.perform(get("$URI?page=0&size=10&id=${321}"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }

    @Test
    fun `assign additional item to item`() {
        val item = super.entitiesGenerator.createItem()
        val additionalItem = super.entitiesGenerator.createAdditionalItem(item.menu)

        val payload = super.mapper.writeValueAsBytes(AssignAdditionalItemRequest(
            additionalItemId = additionalItem.id!!,
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

    @Test
    @Sql("$MOCK_PATH/create_item_add_item_constraint.sql")
    fun `already assigned additional item to item`() {
        val additionalItem = super.entitiesGenerator.createAdditionalItem()
        val item = super.entitiesGenerator.createItem(additionalItem.menu, mutableListOf(additionalItem))

        val payload = super.mapper.writeValueAsBytes(AssignAdditionalItemRequest(
            additionalItemId = additionalItem.id!!,
            menuId = item.menu.id!!
        ))

        super.mockMvc.perform(post("$URI/${item.id!!}/additional-item").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }

    @Test
    fun `assign additional item with nonexistent item`() {
        val item = super.entitiesGenerator.createItem()
        val payload = super.mapper.writeValueAsBytes(AssignAdditionalItemRequest(
            additionalItemId = 321,
            menuId = 321
        ))

        super.mockMvc.perform(post("$URI/${item.id!!}/additional-item").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }

    @Test
    fun `assign additional item with nonexistent additional item`() {
        val item = super.entitiesGenerator.createItem()
        val payload = super.mapper.writeValueAsBytes(AssignAdditionalItemRequest(
            additionalItemId = 312,
            menuId = item.menu.id!!
        ))

        super.mockMvc.perform(post("$URI/${item.id!!}/additional-item").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }

    @Test
    fun `assign additional item with invalid request`() {
        val payload = super.getContent("$PAYLOAD_PATH/assign_additional_items_invalid_request")

        super.mockMvc.perform(post("$URI/32/additional-item").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }

    @Test
    fun `unassign additional item from item`() {
        val menu = super.entitiesGenerator.createEmptyMenu()
        val additionalItem = super.entitiesGenerator.createAdditionalItem(menu)
        val additionalItem2 = super.entitiesGenerator.createAdditionalItem(menu)
        val item = super.entitiesGenerator.createItem(menu, mutableListOf(additionalItem, additionalItem2))

        val payload = super.mapper.writeValueAsBytes(AssignAdditionalItemRequest(
            additionalItemId = additionalItem.id!!,
            menuId = menu.id!!
        ))

        super.mockMvc.perform(delete("$URI/${item.id!!}/additional-item").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.menuId").value(menu.id))
            .andExpect(jsonPath("$.categoryId").value(item.category!!.id))
            .andExpect(jsonPath("$.category").value(item.category!!.name))
            .andExpect(jsonPath("$.name").value(item.name))
            .andExpect(jsonPath("$.description").value(item.description))
            .andExpect(jsonPath("$.price").value(item.price))
            .andExpect(jsonPath("$.additionalItems.length()").value(1))
            .andExpect(jsonPath("$.additionalItems[0].id").value(additionalItem2.id))
            .andExpect(jsonPath("$.additionalItems[0].name").value(additionalItem2.name))
            .andExpect(jsonPath("$.additionalItems[0].description").value(additionalItem2.description))
            .andExpect(jsonPath("$.additionalItems[0].price").value(additionalItem2.price))
            .andExpect(jsonPath("$.additionalItems[0].datUpdate").exists())
            .andExpect(jsonPath("$.additionalItems[0].datCreation").exists())
            .andExpect(jsonPath("$.datUpdate").exists())
            .andExpect(jsonPath("$.datCreation").exists())
            .andDo(print())
    }

    @Test
    fun `unassign nonexistent additional item from item`() {
        val menu = super.entitiesGenerator.createEmptyMenu()
        val additionalItem = super.entitiesGenerator.createAdditionalItem(menu)
        val additionalItem2 = super.entitiesGenerator.createAdditionalItem(menu)
        val item = super.entitiesGenerator.createItem(menu, mutableListOf(additionalItem))

        val payload = super.mapper.writeValueAsBytes(AssignAdditionalItemRequest(
            additionalItemId = additionalItem2.id!!,
            menuId = menu.id!!
        ))

        super.mockMvc.perform(delete("$URI/${item.id!!}/additional-item").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }
}
