package com.example.controller

import com.example.common.ApplicationEndpoints.TEST_ENDPOINT
import com.example.config.SecurityConfiguration
import com.example.common.jwtString
import com.example.common.randomJwt
import com.example.common.testJwt
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
@Import(value = [SecurityConfiguration::class, TestController::class])
class TestControllerTest {

    @TestConfiguration
    class Configuration {
        @Bean
        fun jwtDecoder() = mockk<JwtDecoder>()
    }

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var jwtDecoder: JwtDecoder

    @Test
    fun `Authorized user gets a 200`() {
        every { jwtDecoder.decode(jwtString) } returns testJwt

        mvc.get(TEST_ENDPOINT) {
            header(HttpHeaders.AUTHORIZATION, "bearer $jwtString")
        }.andExpect {
            status { isOk() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `Unauthorized user gets a 403`() {
        every { jwtDecoder.decode(jwtString) } returns randomJwt

        mvc.get(TEST_ENDPOINT) {
            header(HttpHeaders.AUTHORIZATION, "bearer $jwtString")
        }.andExpect {
            status { isForbidden() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `Unauthenticated user gets a 401`() {
        every { jwtDecoder.decode(jwtString) } throws JwtException("Invalid JWT token!")

        mvc.get(TEST_ENDPOINT) {
        }.andExpect {
            status { isUnauthorized() }
        }.andDo {
            print()
        }
    }
}
