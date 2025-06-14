package com.nezuko.users.grpc

import io.grpc.Status
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler
import org.slf4j.LoggerFactory


@GrpcAdvice
class GrpcExceptionHandler {
    private val log = LoggerFactory.getLogger(this::class.java)
    @GrpcExceptionHandler
    fun handleIllegalArgument(e: RuntimeException): Status? {
        log.error("Invalid argument error:", e)
        return Status.INVALID_ARGUMENT.withDescription(e.message).withCause(e)
    }
}