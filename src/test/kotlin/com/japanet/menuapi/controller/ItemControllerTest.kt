package com.japanet.menuapi.controller

import com.japanet.menuapi.AbstractTest
import com.japanet.menuapi.controller.request.v1.CreateItemRequest
import org.junit.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
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

        super.mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(APPLICATION_JSON).content(payload))
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

        super.mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())

    }

}
