package com.example.ejemplo1.data.model

import java.sql.Date

data class UserModel(
    val id_user : Int,
    val name_user : String,
    val last_name_user: String,
    val dni_user: String,
    val email_user: String,
    val phone_user: String,
    val address_user: String,
    val password_user: String,
    val gender_user: String,
    val birthdate_user: Date
)