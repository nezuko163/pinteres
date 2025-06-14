package com.nezuko.auth.controllers.advice

import com.nezuko.auth.domain.model.ErrorDetails
import com.nezuko.auth.exceptions.UserAlreadyExistsException
import io.jsonwebtoken.security.SignatureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.server.ServerWebExchange
import java.util.Date

@ControllerAdvice
class ExceptionHandlerController : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [(UserAlreadyExistsException::class)])
    fun handleUserAlreadyExists(ex: UserAlreadyExistsException, exchange: ServerWebExchange): ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            Date(),
            "Validation Failed",
            ex.message!!
        )
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [(BadCredentialsException::class)])
    fun handleInvalidCredentials(ex: BadCredentialsException): ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            Date(),
            "Invalid credentials",
            "Неправлиьные логин или пароль"
        )
        return ResponseEntity(errorDetails, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [(SignatureException::class)])
    fun handleInvalidToken(ex: SignatureException): ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            Date(),
            "Validation Failed",
            "Неправлиьная подпись токена"
        )
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }
}