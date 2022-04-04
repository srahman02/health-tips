package com.sojibur.healthtips.exception


import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class GlobalExceptionHandlerSpec extends Specification{
    GlobalExceptionHandler globalExceptionHandler

    def setup(){
        globalExceptionHandler = new GlobalExceptionHandler()
    }

    def "should handle TipsNotFoundException when thrown"() {
        given:
        def expectedMessage = "user-not-found"
        def mockException = new TipsNotFoundException(expectedMessage)
        when:
        ResponseEntity<ApiError> responseEntity = globalExceptionHandler.handleTipsNotFoundException(mockException)
        then:
        responseEntity.getStatusCode() == HttpStatus.NOT_FOUND
        responseEntity.getBody().status == HttpStatus.NOT_FOUND
        responseEntity.getBody().errors[0].message == expectedMessage
    }

    def "should handle InternalServerError when thrown"() {
        given:
        def expectedMessage = "internal-server-error"
        def mockException = new InternalServerException(expectedMessage)
        when:
        ResponseEntity<ApiError> responseEntity = globalExceptionHandler.handleInternalServerException(mockException)
        then:
        responseEntity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
        responseEntity.getBody().status == HttpStatus.INTERNAL_SERVER_ERROR
        responseEntity.getBody().errors[0].message == expectedMessage
    }

    def "should handle all other Exception when thrown"() {
        given:
        def expectedMessage = "generic-exception"
        def mockException = new Exception(expectedMessage)
        when:
        ResponseEntity<ApiError> responseEntity = globalExceptionHandler.handleGenericException(mockException)
        then:
        responseEntity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
        responseEntity.getBody().errors[0].message == expectedMessage
    }

    def "should build a response entity based on given status code and exception"() {
        given:
        def status = HttpStatus.INTERNAL_SERVER_ERROR
        def expectedMessage = "internal-server-error"
        def mockException = new InternalServerException(expectedMessage)

        when:
        ResponseEntity<ApiError> responseEntity = globalExceptionHandler.buildResponseEntity(mockException, status)

        then:
        responseEntity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
        responseEntity.getBody().errors[0].message == expectedMessage
    }
}
