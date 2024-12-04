package com.example.ejemplo1.data.model
import com.example.ejemplo1.data.model.UserModel

data class TruckerModel (
    val User: UserModel,
    val id_trucker: Int,
    val matricula_truck: String,
    val type_license_trucker: String,
    val income_trucker: Int,
    val description_trucker: String,
    val ranking_trucker: Int
)