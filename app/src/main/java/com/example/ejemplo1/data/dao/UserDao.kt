package com.example.ejemplo1.data.dao

import com.example.ejemplo1.data.model.UserModel

object UserDao {
    fun listar_usuario(): List<UserModel> {
        var lista = mutableListOf<UserModel>()
        val statement = PostgresqlConexion.getConexion().createStatement()
        val resultSet = statement.executeQuery("SELECT * FROM \"USER\";")
        while (resultSet.next()) {
            lista.add(
                UserModel(
                    resultSet.getInt("id_user"),
                    resultSet.getString("name_user"),
                    resultSet.getString("last_name_user"),
                    resultSet.getString("dni_user"),
                    resultSet.getString("email_user"),
                    resultSet.getString("phone_user"),
                    resultSet.getString("address_user"),
                    resultSet.getString("password_user"),
                    resultSet.getString("gender_user"),
                    resultSet.getDate("birthdate_user")
                )
            )
        }
        return lista
    }

    fun buscar_usuario(email:String,password:String): Int?{
        var lista = mutableListOf<UserModel>()
        PostgresqlConexion.getConexion().prepareStatement(
            "SELECT * FROM \"USER\" WHERE email_user = ? AND password_user = ?;"
        ).use { ps ->
            ps.setString(1, email)
            ps.setString(2, password)
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    return rs.getInt("id_user") // Devuelve el ID del usuario
                }
            }
        }
        return null // Si no se encuentra, devuelve null
    }

    fun buscar_conductor(email:String,password:String): Int?{
        PostgresqlConexion.getConexion().prepareStatement(
            "SELECT * FROM \"trucker\" WHERE email_user = ? AND password_user = ?;"
        ).use { ps ->
            ps.setString(1, email)
            ps.setString(2, password)
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    return rs.getInt("id_user") // Devuelve el ID del usuario
                }
            }
        }
        return null // Si no se encuentra, devuelve null
    }

    fun buscarStatusOrden(id_order:Int): String? {
        PostgresqlConexion.getConexion().prepareStatement(
            "SELECT * FROM \"ORDER\" WHERE id_order = ?;"
        ).use { ps ->
            ps.setInt(1, id_order)
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    return rs.getString("status_order") // Devuelve el ID de la orden
                }
            }
        }
        return null // Si no se encuentra, devuelve null
    }
    fun buscarStringOrden(id_order:Int, returnalVal: String): String? {
        PostgresqlConexion.getConexion().prepareStatement(
            "SELECT * FROM \"ORDER\" WHERE id_order = ?;"
        ).use { ps ->
            ps.setInt(1, id_order)
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    return rs.getString(returnalVal) // Devuelve el ID de la orden
                }
            }
        }
        return null // Si no se encuentra, devuelve null
    }
    fun buscarDoubleOrden(id_order:Int, returnalVal: String): Double? {
        PostgresqlConexion.getConexion().prepareStatement(
            "SELECT * FROM \"ORDER\" WHERE id_order = ?;"
        ).use { ps ->
            ps.setInt(1, id_order)
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    return rs.getDouble(returnalVal) // Devuelve el ID de la orden
                }
            }
        }
        return null // Si no se encuentra, devuelve null
    }

    fun obtenerNombrePorId(idUser: Int): String? {
        PostgresqlConexion.getConexion().prepareStatement(
            "SELECT name_user FROM \"USER\" WHERE id_user = ?;"
        ).use { ps ->
            ps.setInt(1, idUser)
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    return rs.getString("name_user") // Devuelve el nombre del usuario
                }
            }
        }
        return null // Si no se encuentra, devuelve null
    }

    fun insertarOrden(userId: Int, latitude: Double, longitude: Double, peso: Double, metodoPago: String, monto: Double, address: String): Int? {
        val query = """
        INSERT INTO "ORDER" (coordenates_y_order_start, coordenates_x_order_start, weight_order, payment_method_order, value_order, id_user, status_order, address_order_start) 
        VALUES (?, ?, ?, ?, ?, ?, 'Espera', ?) RETURNING id_order;
    """.trimIndent()

        PostgresqlConexion.getConexion().prepareStatement(query).use { ps ->
            ps.setDouble(1, latitude)
            ps.setDouble(2, longitude)
            ps.setDouble(3, peso)
            ps.setString(4, metodoPago)
            ps.setDouble(5, monto)
            ps.setInt(6, userId)
            ps.setString(7, address)

            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    return rs.getInt("id_order") // Devuelve el ID generado
                }
            }
        }
        return -1 // Devuelve -1 si no se generó un ID
    }
    fun eliminarOrdenPorId(idOrden: Int) {
        PostgresqlConexion.getConexion().prepareStatement(
            "DELETE FROM \"ORDER\" WHERE id_order = ?;"
        ).use { ps ->
            ps.setInt(1, idOrden)
            ps.executeUpdate()
        }
    }
    /*private fun registrar(producto: ProductoModel) {
        PostgresqlConexion.getConexion().prepareStatement(
            "INSERT INTO producto (descripcion, codigobarra, precio) VALUES (?, ?, ?);"
        ).use { ps ->

            ps.setString(1, producto.descripcion)
            ps.setString(2, producto.codigobarra)
            ps.setDouble(3, producto.precio)
            ps.executeUpdate()
        }
    }

    private fun actualizar(producto: ProductoModel) {
        PostgresqlConexion.getConexion().prepareStatement(
            "UPDATE producto SET descripcion = ?, codigobarra = ?, precio = ? WHERE id = ?;"
        ).use { ps ->

            ps.setString(1, producto.descripcion)
            ps.setString(2, producto.codigobarra)
            ps.setDouble(3, producto.precio)
            ps.setInt(4, producto.id)
            ps.executeUpdate()
        }
    }

    fun eliminar(producto: ProductoModel) {
        PostgresqlConexion.getConexion().prepareStatement(
            "DELETE FROM producto WHERE id = ?;"
        ).use { ps ->
            ps.setInt(1, producto.id)
            ps.executeUpdate()
        }
    }

    fun grabar(producto: ProductoModel) {
        if (producto.id == 0) {
            registrar(producto)
        } else {
            actualizar(producto)
        }
    }*/
}