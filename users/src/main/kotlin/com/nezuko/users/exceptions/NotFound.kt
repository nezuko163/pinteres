package com.nezuko.users.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class NotFound(reasonn: String) : ResponseStatusException(HttpStatus.NOT_FOUND, reasonn)