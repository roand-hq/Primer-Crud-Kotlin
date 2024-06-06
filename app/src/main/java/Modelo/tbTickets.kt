package Modelo

data class tbTickets(
    val num_ticket: Int,
    val titulo: String,
    val descripcion: String,
    val autor: String,
    val email_Autor: String,
    val fecha_Creacion: String,
    val fecha_Cierre: String,
    val estado: String
)
