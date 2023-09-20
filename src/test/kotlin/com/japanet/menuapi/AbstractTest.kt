package com.japanet.menuapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.japanet.menuapi.component.EntitiesGenerator
import org.apache.http.HttpHeaders
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@AutoConfigureMockMvc
@EnableJpaRepositories
@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@EntityScan(basePackages = ["com.japanet.menuapi.entity"])
@AutoConfigureWireMock(port = 0)
@Sql(scripts = ["classpath:/sql/controller/clear_db.sql"], executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
abstract class AbstractTest {

    companion object {
        const val HEADER_V2 = "application/vnd.menus.menu-api.v2+json"
        const val CLEAR_DB_SCRIPT = "classpath:/sql/controller/clear_db.sql"
    }

    protected lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var context: WebApplicationContext

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var entitiesGenerator: EntitiesGenerator

    @Before
    fun init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
            .build()
    }

    protected fun getContent(fileName: String) = this.getResourceAsStream("request/$fileName.json").readBytes()

    private fun getResourceAsStream(resource: String) = Thread.currentThread().contextClassLoader.getResourceAsStream(resource)
        ?: resource::class.java.getResourceAsStream(resource)

    fun prepareRequestV2(url: String): MockHttpServletRequestBuilder = MockMvcRequestBuilders
        .get(url)
        .header(HttpHeaders.ACCEPT, HEADER_V2)
}
