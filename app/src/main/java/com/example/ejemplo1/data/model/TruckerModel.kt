package com.example.ejemplo1.data.model
import com.example.ejemplo1.data.model.UserModel
import java.sql.Date

data class TruckerModel (
    val id_user : Int,
    val id_trucker: Int,
    val name_user : String,
    val last_name_user: String,
    val dni_user: String,
    val email_user: String,
    val phone_user: String,
    val address_user: String,
    val password_user: String,
    val gender_user: String,
    val matricula_truck: String,
    val type_license_trucker: String,
    val income_trucker: Int,
    val description_trucker: String,
    val ranking_trucker: Int
)