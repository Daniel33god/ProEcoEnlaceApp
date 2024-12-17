package com.example.ejemplo1.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.EditText
import com.example.ejemplo1.data.model.UserModel
import java.sql.Date
import java.sql.SQLException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

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

    // Método para insertar una cuenta de usuario en la tabla "USER"
    fun insertarCuentaUsuario(
        name: String, lastName: String, dni: String, email: String, phone: String,
        address: String, password: String, gender: String, birthDate: String
    ): Int? {
        // Consulta SQL para insertar un usuario en la base de datos
        val query = """
        INSERT INTO "USER" (name_user, last_name_user, dni_user, email_user, phone_user, address_user, password_user, gender_user, birthdate_user)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_user;
    """.trimIndent()

        try {
            // Preparamos la sentencia SQL
            PostgresqlConexion.getConexion().prepareStatement(query).use { ps ->
                // Asignación de los parámetros al PreparedStatement
                ps.setString(1, name)
                ps.setString(2, lastName)
                ps.setString(3, dni)
                ps.setString(4, email)
                ps.setString(5, phone)
                ps.setString(6, address)
                ps.setString(7, password)
                ps.setString(8, gender)
                try {
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val utilDate = sdf.parse(birthDate)
                    val sqlDate = java.sql.Date(utilDate.time)
                    ps.setDate(9, sqlDate)
                } catch (e: ParseException) {
                    // Log the error or handle the invalid date
                    Log.e("DateParsing", "Invalid date format: $birthDate", e)
                    // Optionally, show an error to the user
                }

                val resultSet = ps.executeQuery()
                if (resultSet.next()) {
                    return resultSet.getInt("id_user")
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    fun insertarCuentaConductor(
        idUser: Int, name: String, lastName: String, dni: String, email: String,
        phone: String, address: String, password: String, gender: String,
        patente: String, licencia: String, descripcion: String
    ): Int? {
        val query = """
        INSERT INTO trucker (id_user, name_user, last_name_user, dni_user, email_user, phone_user, address_user, password_user, gender_user, type_license_trucker, description_trucker)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_trucker;
    """.trimIndent()

        try {
            PostgresqlConexion.getConexion().prepareStatement(query).use { ps ->
                ps.setInt(1, idUser)
                ps.setString(2, name)
                ps.setString(3, lastName)
                ps.setString(4, dni)
                ps.setString(5, email)
                ps.setString(6, phone)
                ps.setString(7, address)
                ps.setString(8, password)
                ps.setString(9, gender)
                //ps.setString(10, patente)
                ps.setString(10, licencia)
                ps.setString(11, descripcion)

                val resultSet = ps.executeQuery()
                if (resultSet.next()) {
                    return resultSet.getInt("id_trucker")
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    fun insertarCamion(
        idUser: Int, matricula: String, modelo: String, pesoMax: Int, idTrucker: Int
    ): Boolean {
        val query = """
        INSERT INTO truck (id_user, matricula_truck, model_truck, is_certificated_truck, is_active_truck, max_weight_truck, id_trucker)
        VALUES (?, ?, ?, true, 'Activo', ?, ?);
    """.trimIndent()

        try {
            PostgresqlConexion.getConexion().prepareStatement(query).use { ps ->
                ps.setInt(1, idUser)
                ps.setString(2, matricula)
                ps.setString(3, modelo)
                ps.setInt(4, pesoMax)
                ps.setInt(5, idTrucker)

                val rowsAffected = ps.executeUpdate()
                return rowsAffected > 0
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    fun cuentaConductor(
        name: String, lastName: String, dni: String, email: String, phone: String,
        address: String, password: String, gender: String, birthDate: String,
        patente: String, modelo: String, pesoMax: Int, licencia: String, descripcion: String
    ): Boolean {
        // Crear usuario
        val idUser = insertarCuentaUsuario(name, lastName, dni, email, phone, address, password, gender, birthDate)
        if (idUser == null) {
            return false // Si no se pudo crear el usuario, retorna false
        }

        // Crear conductor
        val idTrucker = insertarCuentaConductor(idUser, name, lastName, dni, email, phone, address, password, gender, patente, licencia, descripcion)
        if (idTrucker == null) {
            return false // Si no se pudo crear el conductor, retorna false
        }

        // Crear camión
        insertarCamion(idUser, patente, modelo, pesoMax, idTrucker)
        return actualizarCamioneroMatricula(idTrucker, patente)
    }


    fun actualizarCamioneroMatricula(idTrucker: Int, matricula: String): Boolean {
        PostgresqlConexion.getConexion().prepareStatement(
            """
        UPDATE "trucker"
        SET matricula_truck = ?
        WHERE id_trucker = ?;
        """
        ).use { ps ->
            ps.setString(1, matricula) // Cambia el estado a "Progreso"
            ps.setInt(2, idTrucker)     // Asigna el ID del trucker
            ps.executeUpdate()          // Ejecuta la actualización
        }
        return true
    }

    fun buscar_usuario(email:String,password:String): Int?{
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

    fun obtenerIdTrucker(idUser: Int): Int? {
        PostgresqlConexion.getConexion().prepareStatement(
            """
        SELECT t.id_trucker 
        FROM "USER" u
        INNER JOIN trucker t ON u.id_user = t.id_user
        WHERE u.id_user = ?;
        """
        ).use { ps ->
            ps.setInt(1, idUser)
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    return rs.getInt("id_trucker") // Devuelve el ID del trucker
                }
            }
        }
        return null // Si no se encuentra, devuelve null
    }

    fun obtenerNameTrucker2(idUser: Int): String? {
        PostgresqlConexion.getConexion().prepareStatement(
            """
        SELECT o.id_trucker, t.name_user 
		FROM "ORDER" o 
		JOIN trucker t on o.id_trucker = t.id_trucker 
		WHERE id_order = ?;
        """
        ).use { ps ->
            ps.setInt(1, idUser)
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    return rs.getString("name_user") // Devuelve el ID del trucker
                }
            }
        }
        return null // Si no se encuentra, devuelve null
    }

    fun aceptarSolicitud(idTrucker: Int, orderId: Int) {
        PostgresqlConexion.getConexion().prepareStatement(
            """
        UPDATE "ORDER"
        SET status_order = ?, id_trucker = ?, coordenates_x_order_end = -70.14129193434847, coordenates_y_order_end = -20.219767752933535
        WHERE id_order = ?;
        """
        ).use { ps ->
            ps.setString(1, "Progreso") // Cambia el estado a "Progreso"
            ps.setInt(2, idTrucker)     // Asigna el ID del trucker
            ps.setInt(3, orderId)       // Filtra por el ID de la orden
            ps.executeUpdate()          // Ejecuta la actualización
        }
    }



    fun insertarOrden(userId: Int, latitude: Double, longitude: Double, peso: Double, metodoPago: String, monto: Double, address: String, tajo_monto : Double, is_recyclable : Boolean): Int? {
        val query = """
        INSERT INTO "ORDER" (coordenates_y_order_start, coordenates_x_order_start, weight_order, payment_method_order, value_order, id_user, status_order, address_order_start, slice_value_order, is_recyclable) 
        VALUES (?, ?, ?, ?, ?, ?, 'Espera', ?, ?, ?) RETURNING id_order;
    """.trimIndent()

        PostgresqlConexion.getConexion().prepareStatement(query).use { ps ->
            ps.setDouble(1, latitude)
            ps.setDouble(2, longitude)
            ps.setDouble(3, peso)
            ps.setString(4, metodoPago)
            ps.setDouble(5, monto)
            ps.setInt(6, userId)
            ps.setString(7, address)
            ps.setDouble(8, tajo_monto)
            ps.setBoolean(9, is_recyclable)

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


    fun obtenerOrdenes(): List<Map<String, String>> {
        val query = """
     SELECT u.id_user, u.name_user, o.weight_order, o.value_order, o.address_order_start, o.value_order, o.id_order, o.payment_method_order
         FROM "USER" u
         JOIN "ORDER" o ON u.id_user = o.id_user WHERE o.status_order = 'Espera';
 """.trimIndent()

        val listaOrdenes = mutableListOf<Map<String, String>>()

        PostgresqlConexion.getConexion().prepareStatement(query).use { ps ->
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    listaOrdenes.add(
                        mapOf(
                            "name_user" to rs.getString("name_user"),
                            "weight_order" to rs.getString("weight_order"),
                            "value_order" to rs.getString("value_order"),
                            "address_order_start" to rs.getString("address_order_start"),
                            "payment_method_order" to rs.getString("payment_method_order"),
                            "id_order" to rs.getString("id_order")
                        )
                    )
                }
            }
        }
        return listaOrdenes
    }

    fun obtenerOrdenesRealizadas(): List<Map<String, String>> {
        val query = """
     SELECT u.id_user, u.name_user, o.weight_order, o.value_order, o.address_order_start, o.value_order, o.ranking_trucker_order, o.comment_trucker_order, o.id_order
         FROM "USER" u
         JOIN "ORDER" o ON u.id_user = o.id_user WHERE o.status_order = 'Completado';
 """.trimIndent()

        val listaOrdenes = mutableListOf<Map<String, String>>()

        PostgresqlConexion.getConexion().prepareStatement(query).use { ps ->
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    listaOrdenes.add(
                        mapOf(
                            "name_user" to rs.getString("name_user"),
                            "weight_order" to rs.getString("weight_order"),
                            "value_order" to rs.getString("value_order"),
                            "ranking_trucker_order" to rs.getString("ranking_trucker_order"),
                            "comment_trucker_order" to rs.getString("comment_trucker_order"),
                            "address_order_start" to rs.getString("address_order_start"),
                            "id_order" to rs.getString("id_order")
                        )
                    )
                }
            }
        }
        return listaOrdenes
    }

    fun calificarConductor(idOrder: Int, rating: Float, comentario: String) {
        val query = """
        UPDATE "ORDER"
        SET ranking_trucker_order = ?, comment_trucker_order = ?, status_order = 'Completado'
        WHERE id_order = ?;
    """.trimIndent()

        PostgresqlConexion.getConexion().prepareStatement(query).use { ps ->
            ps.setFloat(1, rating)
            ps.setString(2, comentario)
            ps.setInt(3, idOrder)
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