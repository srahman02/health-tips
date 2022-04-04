package com.sojibur.healthtips.service

import com.sojibur.healthtips.exception.InternalServerException
import com.sojibur.healthtips.repository.HealthTipsRepository
import com.sojibur.healthtips.exception.TipsNotFoundException
import com.sojibur.healthtips.model.HealthTips
import com.sojibur.healthtips.service.impl.HealthTipsServiceImpl
import spock.lang.Specification

class HealthTipsServiceSpec extends Specification{
    HealthTipsService mockHealthTipsService
    HealthTipsRepository mockHealthTipsRepository

    def setup(){
        mockHealthTipsRepository = Mock()
        mockHealthTipsService = new HealthTipsServiceImpl(mockHealthTipsRepository)
    }

    def "should return a health tips if the call to the repository findById() method is successful"(){
        given:
        def mockUserId = "0000"
        def tips = new ArrayList()
        tips.add("Drink Water")
        tips.add("Drink More Water")
        def mockTips = [id:mockUserId, tips:tips] as HealthTips

        when:
        HealthTips actualTips = mockHealthTipsService.getHealthTipsById(mockUserId)

        then:
        1 * mockHealthTipsRepository.findById(mockUserId) >> Optional.of(mockTips)
        actualTips == mockTips
    }

    def "should throw UserNotFoundException if the call to the repository findById() method is unsuccessful"(){
        given:
        def mockUserId = "0000"

        when:
        mockHealthTipsService.getHealthTipsById(mockUserId)

        then:
        1 * mockHealthTipsRepository.findById(mockUserId) >> Optional.empty()
        thrown TipsNotFoundException
    }

    def "should return created health tips if the call to the repository save() method is successful"(){
        given:
        def tips = new ArrayList()
        tips.add("Drink Water")
        tips.add("Drink More Water")
        def mockTips = [id:"0000", tips:tips] as HealthTips

        when:
        HealthTips actualTips = mockHealthTipsService.createTips(mockTips)

        then:
        1 * mockHealthTipsRepository.save(mockTips) >> mockTips
        actualTips == mockTips
    }

    def "should throw InternalServerException if the call to the repository save() method is unsuccessful"(){
        given:
        def tips = new ArrayList()
        tips.add("Drink Water")
        tips.add("Drink More Water")
        def mockTips = [id:"0000", tips:tips] as HealthTips

        when:
        mockHealthTipsService.createTips(mockTips)

        then:
        1 * mockHealthTipsRepository.save(mockTips) >> {throw new Exception()}
        thrown InternalServerException
    }
}
