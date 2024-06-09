package RecyclerViewHelpers

import Modelo.ClaseConexion
import Modelo.tbTickets
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rodrigo.hurtado.appcrud.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
class Adaptador_Admins(var Datos: List<tbTickets>): RecyclerView.Adapter<ViewHolder_Admins>() {

    fun actualizarListaDespuesDeActualizarDatos(num_ticket: Int, nuevoEstado : String){
        val index=Datos.indexOfFirst { it.num_ticket == num_ticket}
        Datos[index].estado = nuevoEstado
        notifyItemChanged(index)
    }

    fun actualizarAdmins(num_ticket: Int){
        GlobalScope.launch(Dispatchers.IO) {
            val fechaCierre = LocalDate.now()
            val formato = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val fechaFormato = fechaCierre.format(formato)

            val objClaseConexion = ClaseConexion().CadenaConexion()
            val cerrarTicket = objClaseConexion?.prepareStatement("update ac_tickets set Estado = ?, fecha_cierre = ? where num_ticket = ?")!!
            cerrarTicket.setString(1, "CERRADO")
            cerrarTicket.setString(2, fechaFormato)
            cerrarTicket.setInt(3, num_ticket)
            cerrarTicket.executeUpdate()
            val commit = objClaseConexion.prepareStatement("commit")
            commit.executeUpdate()
            withContext(Dispatchers.Main){
                actualizarListaDespuesDeActualizarDatos(num_ticket,"CERRADO")
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder_Admins {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_admins, parent, false )
        return ViewHolder_Admins(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder_Admins, position: Int) {
        val item = Datos[position]
        holder.txtTituloAdmins.text = item.titulo
        holder.txtAutor.text = item.autor
        holder.estadoAdminsTexto.text = item.estado

        if(item.estado != "ACTIVO"){
            holder.estadoAdmins.setImageResource(R.drawable.ic_estado_cerrado)
        } else {
            holder.estadoAdmins.setImageResource(R.drawable.ic_estado_activo)
        }
        holder.btnCerrarTicket.setOnClickListener {
            actualizarAdmins(item.num_ticket)
        }

        holder.itemView.setOnClickListener {
            val contexto = holder.itemView.context
            val builder = AlertDialog.Builder(contexto)
            val dialogLayout = LayoutInflater.from(contexto).inflate(R.layout.dialog_details, null)
            builder.setView(dialogLayout)
            val dialog = builder.create()
            val editTextTitle = dialogLayout.findViewById<EditText>(R.id.editTextTitle)
            val editTextDescription = dialogLayout.findViewById<EditText>(R.id.editTextDescription)
            val buttonCreateTicket = dialogLayout.findViewById<Button>(R.id.buttonCreateTicket)
            buttonCreateTicket.text = "Cerrar"
            editTextTitle.setText(item.titulo)
            editTextDescription.setText(item.descripcion)

            buttonCreateTicket.setOnClickListener {

                dialog.dismiss()
            }
            dialog.show()
        }
    }


}