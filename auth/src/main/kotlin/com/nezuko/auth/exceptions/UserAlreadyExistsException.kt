package com.nezuko.auth.exceptions

class UserAlreadyExistsException(val login: String) : RuntimeException("User with name $login already exists")