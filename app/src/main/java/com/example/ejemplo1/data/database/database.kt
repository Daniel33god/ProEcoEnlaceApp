package com.example.ejemplo1.data.database

/*import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction*/

object DatabaseFactory
{
    /*fun init()
    {
        val database = Database.connect(
            url = "jdbc:postgresql://magallanes.inf.unap.cl:5432/lmartinez",
            driver = "org.postgresql.Driver",
            user = "lmartinez",
            password = "3s88QLK3sK"
        )
        transaction {
            SchemaUtils.create(Users)
        }
    }
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }*/
}