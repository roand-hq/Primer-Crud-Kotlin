package Modelo

data class tbTickets(
    val num_ticket: Int,
    var titulo: String,
    var descripcion: String,
    val autor: String,
    val email_Autor: String,
    val fecha_Creacion: String,
    var fecha_Cierre: String,
    var estado: String
)
