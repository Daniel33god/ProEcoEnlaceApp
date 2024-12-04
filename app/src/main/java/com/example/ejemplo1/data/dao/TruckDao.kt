package com.example.ejemplo1.data.dao

import com.example.ejemplo1.data.model.TruckModel


object TruckDao {
    fun listar_camiones(): List<TruckModel> {
        var lista = mutableListOf<TruckModel>()
        val statement = PostgresqlConexion.getConexion().createStatement()
        val resultSet = statement.executeQuery("SELECT * FROM \"truck\";")
        while (resultSet.next()) {
            lista.add(
                TruckModel(
                    resultSet.getString("matricula_truck"),
                    resultSet.getInt("id_user"),
                    resultSet.getString("model_truck"),
                    resultSet.getBoolean("is_certificated_truck"),
                    resultSet.getString("is_Active_truck"),
                    resultSet.getInt("max_weight_truck"),
                    resultSet.getInt("id_trucker")
                )
            )
        }
        return lista
    }
}