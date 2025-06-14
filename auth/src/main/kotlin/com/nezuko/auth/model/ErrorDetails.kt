package com.nezuko.auth.domain.model

import java.util.Date

data class ErrorDetails(val time: Date, val message: String, val details: String)
