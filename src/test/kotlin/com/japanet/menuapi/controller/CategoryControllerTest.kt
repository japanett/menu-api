package com.japanet.menuapi.controller

import com.japanet.menuapi.AbstractTest
import org.junit.Test
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CategoryControllerTest : AbstractTest() {

    companion object {
        private const val URI = "/categories"
        private const val MOCK_PATH = "/sql/controller/categories"
        private const val PAYLOAD_PATH = "categories"
    }

    @Test
    @Sql("$MOCK_PATH/create_menu.sql")
    fun `create category`() {
        val menuId: Long = 911
        val payload = super.getContent("${PAYLOAD_PATH}/create_category")

        super.mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON).content(payload))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.menuId").value(menuId))
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.datUpdate").exists())
            .andExpect(jsonPath("$.datCreation").exists())
            .andDo(MockMvcResultHandlers.print())
    }

}
