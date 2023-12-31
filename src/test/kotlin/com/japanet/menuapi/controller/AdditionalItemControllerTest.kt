package com.japanet.menuapi.controller

import com.japanet.menuapi.AbstractTest
import com.japanet.menuapi.controller.request.v1.CreateAdditionalItemRequest
import com.japanet.menuapi.repository.AdditionalItemRepository
import com.japanet.menuapi.repository.ItemRepository
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import kotlin.test.assertTrue

class AdditionalItemControllerTest : AbstractTest() {

    @Autowired
    private lateinit var repository: AdditionalItemRepository

    @Autowired
    private lateinit var itemRepository: ItemRepository

    companion object {
        private const val URI = "/additional-items"
        private const val PAYLOAD_PATH = "additional-items"
    }

    @Test
    fun `create additional item`() {
        val menu = super.entitiesGenerator.createEmptyMenu()
        val name = "Ketchup"
        val price = BigDecimal("1.99")
        val description = "Ketchupao da Heinzzz"

        val payload = super.mapper.writeValueAsBytes(CreateAdditionalItemRequest(
            menuId = menu.id!!,
            name = name,
            description = description,
            price = price
        ))

        super.mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.menuId").value(menu.id))
            .andExpect(jsonPath("$.name").value(name))
            .andExpect(jsonPath("$.description").value(description))
            .andExpect(jsonPath("$.price").value(price))
            .andExpect(jsonPath("$.datUpdate").exists())
            .andExpect(jsonPath("$.datCreation").exists())
            .andDo(print())
    }

    @Test
    fun `create additional item with invalid price`() {
        val price = BigDecimal("25.553")

        val payload = super.mapper.writeValueAsBytes(CreateAdditionalItemRequest(
            menuId = 321,
            name = "blablabla",
            description = "bkblbalblalbal",
            price = price
        ))

        super.mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())

    }

    @Test
    fun `create additional item with invalid request`() {
        val payload = super.getContent("$PAYLOAD_PATH/create_additional_item_invalid_request")

        super.mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }

    @Test
    fun `retrieve additional items by menuId`() {
        val additionalItem = super.entitiesGenerator.createAdditionalItem()

        super.mockMvc.perform(MockMvcRequestBuilders.get("$URI?page=0&size=10&menuId=${additionalItem.menu.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content.length()").value(1))
            .andExpect(jsonPath("$.content[0].id").exists())
            .andExpect(jsonPath("$.content[0].menuId").value(additionalItem.menu.id))
            .andExpect(jsonPath("$.content[0].name").value(additionalItem.name))
            .andExpect(jsonPath("$.content[0].description").value(additionalItem.description))
            .andExpect(jsonPath("$.content[0].price").value(additionalItem.price))
            .andExpect(jsonPath("$.content[0].datUpdate").exists())
            .andExpect(jsonPath("$.content[0].datCreation").exists())
            .andDo(print())
    }

    @Test
    fun `retrieve additional item not found`() {
        super.mockMvc.perform(MockMvcRequestBuilders.get("$URI?page=0&size=10&id=${321}"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }

    @Test
    fun `patch additional item`() {
        val additionalItem = super.entitiesGenerator.createAdditionalItem()
        val payload = super.getContent("$PAYLOAD_PATH/patch_additional_item")

        super.mockMvc.perform(MockMvcRequestBuilders.patch("$URI/${additionalItem.id}").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.menuId").exists())
            .andExpect(jsonPath("$.name").value("Ketchup"))
            .andExpect(jsonPath("$.description").value("Heinz braba"))
            .andExpect(jsonPath("$.price").value(10.55))
            .andExpect(jsonPath("$.datUpdate").exists())
            .andExpect(jsonPath("$.datCreation").exists())
            .andDo(print())
    }

    @Test
    fun `patch additional item description only`() {
        val additionalItem = super.entitiesGenerator.createAdditionalItem()
        val payload = super.getContent("$PAYLOAD_PATH/patch_additional_item_description")

        super.mockMvc.perform(MockMvcRequestBuilders.patch("$URI/${additionalItem.id}").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.menuId").exists())
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.description").value("mostardao"))
            .andExpect(jsonPath("$.price").exists())
            .andExpect(jsonPath("$.datUpdate").exists())
            .andExpect(jsonPath("$.datCreation").exists())
            .andDo(print())
    }

    @Test
    fun `patch additional item with invalid price`() {
        val additionalItem = super.entitiesGenerator.createAdditionalItem()
        val payload = super.getContent("$PAYLOAD_PATH/patch_invalid_price")

        super.mockMvc.perform(MockMvcRequestBuilders.patch("$URI/${additionalItem.id}").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }

    @Test
    fun `delete additional item`() {
        val additionalItem = super.entitiesGenerator.createAdditionalItem()

        super.mockMvc.perform(MockMvcRequestBuilders.delete("$URI/${additionalItem.id}"))
            .andExpect(status().isNoContent)
            .andDo(print())
    }

    @Test
    fun `delete additional item with items`() {
        val additionalItem = super.entitiesGenerator.createAdditionalItem()
        val item = super.entitiesGenerator.createItem(additionalItem.menu, mutableListOf(additionalItem))

        super.mockMvc.perform(MockMvcRequestBuilders.delete("$URI/${additionalItem.id}"))
            .andExpect(status().isNoContent)
            .andDo(print())

        val itemAfter = itemRepository.findById(item.id!!)
        val additionalItemAfter = repository.findById(additionalItem.id!!)

        assertTrue { itemAfter.get().additionalItems?.isEmpty()!! }
        assertTrue { additionalItemAfter.isEmpty }
    }

}
