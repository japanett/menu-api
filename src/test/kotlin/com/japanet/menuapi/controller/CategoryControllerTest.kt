package com.japanet.menuapi.controller

import com.japanet.menuapi.AbstractTest
import com.japanet.menuapi.controller.request.v1.CreateCategoryRequest
import org.junit.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CategoryControllerTest : AbstractTest() {

    companion object {
        private const val URI = "/categories"
        private const val MOCK_PATH = "/sql/controller/categories"
        private const val PAYLOAD_PATH = "categories"
    }

    @Test
    fun `create category`() {
        val menuId: Long = super.entitiesGenerator.createEmptyMenu().id!!
        val payload = super.mapper.writeValueAsBytes(CreateCategoryRequest(
            menuId = menuId,
            name = "Comes e bebes"
        ))

        super.mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.menuId").value(menuId))
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.datUpdate").exists())
            .andExpect(jsonPath("$.datCreation").exists())
            .andDo(print())
    }

    @Test
    fun `create category with no menu`() {
        val payload = super.mapper.writeValueAsBytes(CreateCategoryRequest(
            menuId = 123,
            name = "Futrinca"
        ))

        super.mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }

    @Test
    fun `retrieve categories by menuId`() {
        val menuEntity = super.entitiesGenerator.createThreeCategories("Category")
        val menudId: Long = menuEntity.id!!

        super.mockMvc.perform(MockMvcRequestBuilders.get("$URI?page=0&size=10&menuId=$menudId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content.length()").value(3))
            .andExpect(jsonPath("$.content[0].id").exists())
            .andExpect(jsonPath("$.content[0].menuId").value(menudId))
            .andExpect(jsonPath("$.content[0].name").exists())
            .andExpect(jsonPath("$.content[0].datUpdate").exists())
            .andExpect(jsonPath("$.content[0].datCreation").exists())
            .andDo(print())
    }

    @Test
    fun `retrieve category when not found`() {
        val categoryId: Long = 213

        super.mockMvc.perform(MockMvcRequestBuilders.get("$URI?page=0&size=10&id=$categoryId"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }

    @Test
    fun `patch category`() {
        val name = "Comes e bebes"
        val entity = super.entitiesGenerator.createCategory(name)
        val patchedName = "Bebidas irlandesas"
        val payload = super.getContent("$PAYLOAD_PATH/patch_category")

        super.mockMvc.perform(MockMvcRequestBuilders.patch("$URI/${entity.id!!}").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.menuId").exists())
            .andExpect(jsonPath("$.name").value(patchedName))
            .andExpect(jsonPath("$.datUpdate").exists())
            .andExpect(jsonPath("$.datCreation").exists())
            .andDo(print())
    }

    @Test
    fun `patch category not found`() {
        val inexistentId: Long = 321321
        val payload = super.getContent("$PAYLOAD_PATH/patch_category")

        super.mockMvc.perform(MockMvcRequestBuilders.patch("$URI/$inexistentId").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }

    @Test
    fun `patch category with incorrect parameters`() {
        val inexistentId: Long = 321321
        val payload = super.getContent("$PAYLOAD_PATH/patch_category_invalid_parameters")

        super.mockMvc.perform(MockMvcRequestBuilders.patch("$URI/$inexistentId").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andDo(print())
    }


}
