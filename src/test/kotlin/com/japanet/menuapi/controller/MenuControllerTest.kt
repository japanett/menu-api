package com.japanet.menuapi.controller

import com.japanet.menuapi.AbstractTest
import com.japanet.menuapi.controller.request.v1.CreateMenuRequest
import org.junit.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

class MenuControllerTest : AbstractTest() {

    companion object {
        private const val URI = "/menus"
        private const val MOCK_PATH = "/sql/controller/menus"
        private const val PAYLOAD_PATH = "menus"
    }

    @Test
    fun `create menu`() {
        val customerId = UUID.randomUUID()
        val establishmentId: Long = 3213
        val payload = super.mapper.writeValueAsBytes(CreateMenuRequest(
            establishmentId = establishmentId,
            customerId = customerId
        ))

        super.mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.establishmentId").value(establishmentId))
            .andExpect(jsonPath("$.customerId").value(customerId.toString()))
            .andExpect(jsonPath("$.datUpdate").exists())
            .andExpect(jsonPath("$.datCreation").exists())
            .andDo(print())
    }

    @Test
    @Sql("${MOCK_PATH}/create_establishment_constraint.sql")
    fun `create menu with establishment taken`() {
        val menuEntity = super.entitiesGenerator.createEmptyMenu()

        val payload = super.mapper.writeValueAsBytes(CreateMenuRequest(
            establishmentId = menuEntity.establishmentId!!,
            customerId = menuEntity.customerId!!
        ))

        super.mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }

    @Test
    fun `create menu with empty request`() {

        val payload = super.getContent("$PAYLOAD_PATH/create_menu_empty_parameters")

        super.mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }

    @Test
    fun `retrieve menu by id`() {
        val menuEntity = super.entitiesGenerator.createEmptyMenu()

        super.mockMvc.perform(MockMvcRequestBuilders.get("$URI?page=0&size=10&id=${menuEntity.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].id").exists())
            .andExpect(jsonPath("$.content[0].establishmentId").exists())
            .andExpect(jsonPath("$.content[0].customerId").exists())
            .andExpect(jsonPath("$.content[0].datUpdate").exists())
            .andExpect(jsonPath("$.content[0].datCreation").exists())
            .andDo(print())
    }

    @Test
    fun `retrieve menu by establishmentId`() {
        val menuEntity = super.entitiesGenerator.createEmptyMenu()

        super.mockMvc.perform(MockMvcRequestBuilders.get("$URI?page=0&size=10&establishmentId=${menuEntity.establishmentId}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].id").exists())
            .andExpect(jsonPath("$.content[0].establishmentId").exists())
            .andExpect(jsonPath("$.content[0].customerId").exists())
            .andExpect(jsonPath("$.content[0].datUpdate").exists())
            .andExpect(jsonPath("$.content[0].datCreation").exists())
            .andDo(print())
    }

    @Test
    fun `retrieve menu by customerId`() {
        val customerId = UUID.randomUUID()
        super.entitiesGenerator.createEmptyMenu(customerId)
        super.entitiesGenerator.createEmptyMenu(customerId)
        super.entitiesGenerator.createEmptyMenu(customerId)

        super.mockMvc.perform(MockMvcRequestBuilders.get("$URI?page=0&size=10&customerId=$customerId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content.length()").value(3))
            .andExpect(jsonPath("$.content[0].id").exists())
            .andExpect(jsonPath("$.content[0].establishmentId").exists())
            .andExpect(jsonPath("$.content[0].customerId").exists())
            .andExpect(jsonPath("$.content[0].datUpdate").exists())
            .andExpect(jsonPath("$.content[0].datCreation").exists())
            .andDo(print())
    }

    @Test
    fun `retrieve menu not found`() {

        super.mockMvc.perform(MockMvcRequestBuilders.get("$URI?page=0&size=10"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }
}
