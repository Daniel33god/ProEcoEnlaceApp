package com.example.ejemplo1.data.dao

import java.sql.Connection
import java.sql.DriverManager

object PostgresqlConexion {
    fun getConexion(): Connection {
        return DriverManager.getConnection(
            "jdbc:postgresql://magallanes.inf.unap.cl:5432/lmartinez",
            "lmartinez",
            "3s88QLK3sK"
        )
    }
}