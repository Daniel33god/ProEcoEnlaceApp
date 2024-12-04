package com.example.ejemplo1.data.dao

object TruckerDao {
    fun insertarOrden(id_order: Int, tru_id_user: Int, latitude: Double, longitude: Double): Boolean {
        val query = """
            UPDATE "ORDER" 
            SET tru_id_user = ?, 
            status_order = ?, 
            id_trucker = ?, 
            coordenates_y_order_end = ?, 
            coordenates_y_order_end = ? 
            WHERE id_order = ?;
        """.trimIndent()
        /*id_order, tru_id_user, status_order, id_trucker, x, y*/
        PostgresqlConexion.getConexion().prepareStatement(query).use { ps ->
            ps.setInt(1, tru_id_user)
            ps.setString(2, "IN PROGRESS")
            ps.setInt(3, tru_id_user)
            ps.setDouble(4, latitude)
            ps.setDouble(5, longitude)
            ps.setInt(6, id_order)

            val result = ps.executeUpdate()
            return result > 0 // Devuelve true si se insertó una fila
        }
    }
    fun buscarOrden(): Int?{
        PostgresqlConexion.getConexion().prepareStatement(
            "SELECT * FROM \"ORDER\" WHERE status_order = \'Espera\';"
        ).use { ps ->
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    return rs.getInt("id_order") // Devuelve el ID de la orden
                }
            }
        }
        return null // Si no se encuentra, devuelve null
    }
}