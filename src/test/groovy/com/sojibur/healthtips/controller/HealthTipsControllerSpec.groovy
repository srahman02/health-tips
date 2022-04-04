package com.sojibur.healthtips.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.sojibur.healthtips.exception.ApiError
import com.sojibur.healthtips.exception.GlobalExceptionHandler
import com.sojibur.healthtips.exception.InternalServerException
import com.sojibur.healthtips.exception.TipsNotFoundException
import com.sojibur.healthtips.model.HealthTips
import com.sojibur.healthtips.service.HealthTipsService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class HealthTipsControllerSpec extends Specification{

    ObjectMapper objectMapper
    MockMvc mockMvc
    HealthTipsService mockHealthTipsService
    def mockHealthTipsController

    def setup() {
        objectMapper = new ObjectMapper()
        mockHealthTipsService = Mock()
        mockHealthTipsController = new HealthTipsController(mockHealthTipsService)
        mockMvc = MockMvcBuilders.standaloneSetup(mockHealthTipsController)
                .setControllerAdvice(new GlobalExceptionHandler()).build()
    }

    def "should return health tips for a userId when /api/tips is called with userId"(){
        def mockUserId = "0000"
        def tips = new ArrayList()
        tips.add("Drink Water")
        tips.add("Drink More Water")
        def mockTips = [id:mockUserId, tips:tips] as HealthTips

        when:
        def response = mockMvc.perform(get("/api/tips/"+mockUserId))
                .andExpect(status().isOk())
                .andReturn()
        def content = response.getResponse().getContentAsString()
        HealthTips actualTips = objectMapper.readValue(content, HealthTips.class)

        then:
        1 * mockHealthTipsService.getHealthTipsById(mockUserId) >> mockTips
        actualTips == mockTips
    }

    def "should throw TipsNotFoundException when /api/tips is called with userId and tips not present"(){
        given:
        def mockUserId = "0000"

        when:
        def response = mockMvc.perform(get("/api/tips/"+mockUserId))
                .andExpect(status().isNotFound())
                .andReturn()
        def content = response.getResponse().getContentAsString()
        ApiError apiError = objectMapper.readValue(content, ApiError.class)

        then:
        1 * mockHealthTipsService.getHealthTipsById(mockUserId) >> {throw new TipsNotFoundException("user-not-found")}
        apiError.status == HttpStatus.NOT_FOUND
        apiError.errors[0].message == "user-not-found"
    }

    def "should return health tips when a POST call to /api/tips is successful"() {
        given:
        def mockUserId = "0000"
        def tips = new ArrayList()
        tips.add("Drink Water")
        tips.add("Drink More Water")
        def mockTips = [id:mockUserId, tips:tips] as HealthTips

        when:
        def response = mockMvc.perform(post("/api/tips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockTips)))
                .andExpect(status().isCreated())
                .andReturn()
        def content = response.getResponse().getContentAsString()
        HealthTips actualTips = objectMapper.readValue(content, HealthTips.class)

        then:
        1 * mockHealthTipsService.createTips(mockTips) >> mockTips
        actualTips == mockTips
    }

    def "should throw InternalServerException if a POST call to /api/tips fails"() {
        given:
        def mockUserId = "0000"
        def tips = new ArrayList()
        tips.add("Drink Water")
        tips.add("Drink More Water")
        def mockTips = [id:mockUserId, tips:tips] as HealthTips

        when:
        def response = mockMvc.perform(post("/api/tips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockTips)))
                .andExpect(status().isInternalServerError())
                .andReturn()
        def content = response.getResponse().getContentAsString()
        ApiError apiError = objectMapper.readValue(content, ApiError.class)

        then:
        1 * mockHealthTipsService.createTips(mockTips) >> {throw new InternalServerException("internal-server-error")}
        apiError.status == HttpStatus.INTERNAL_SERVER_ERROR
        apiError.errors[0].message == "internal-server-error"
    }
}
