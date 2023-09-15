package com.japanet.menuapi.controller

import com.japanet.menuapi.AbstractTest
import org.junit.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class MenuControllerTest : AbstractTest() {

    companion object {
        private const val URI = "/menus"
        private const val MOCK_PATH = "/sql/controller/menus"
        private const val PAYLOAD_PATH = "menus"
    }

    @Test
    fun `create menu`() {
        val payload = super.getContent("$PAYLOAD_PATH/create_menu")

        super.mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.establishmentId").exists())
            .andExpect(jsonPath("$.customerId").exists())
            .andExpect(jsonPath("$.datUpdate").exists())
            .andExpect(jsonPath("$.datCreation").exists())
            .andDo(print())

    }
}
