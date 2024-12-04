package com.example.ejemplo1.data.model

data class TruckModel (
    val matricula_truck: String,
    val id_user: Int,
    val model_truck: String,
    val is_certificated_truck: Boolean,
    val is_Active_truck: String,
    val max_weight_truck: Int,
    val id_trucker: Int
)