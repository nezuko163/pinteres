package com.nezuko.posts.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class NotFound(message: String) : ResponseStatusException(HttpStatus.NOT_FOUND, message)