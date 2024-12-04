package com.example.ejemplo1.data.model

import org.w3c.dom.Text

data class OrderModel(
    val id_order: Int,
    val id_user: Int,
    val tru_id_user: Int,
    val coordenates_x_order_start: Float,
    val coordenates_y_order_start: Float,
    val weight_order: Int,
    val is_recyclable: Boolean,
    val value_order: Int,
    val slice_value_order: Int,
    val payment_method_order: String,
    val comment_trucker_order: String,
    val ranking_trucker_order: Int,
    val status_order: String,
    val id_trucker: Int,
    val coordenates_x_order_end: Float,
    val coordenates_y_order_end: Float,
)