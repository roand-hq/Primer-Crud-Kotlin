package RecyclerViewHelpers

import Modelo.ClaseConexion
import Modelo.tbTickets
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rodrigo.hurtado.appcrud.R

class Adaptador(var Datos: List<tbTickets>): RecyclerView.Adapter<ViewHolder>() {

    fun actualizarTickets(nuevaLista:List<tbTickets>){
        Datos=nuevaLista
        notifyDataSetChanged()
    }

    fun elimiarTickets(num_ticket: Int, position: Int) {
        val listaDatos = Datos .toMutableList()
        listaDatos.removeAt(position)
        CoroutineScope(Dispatchers.IO).launch {
            val objConexion = ClaseConexion().CadenaConexion()
            val borrarTicket = objConexion?.prepareStatement("delete from ac_tickets where num_ticket = ?")!!
            borrarTicket.setInt(1, num_ticket)
            borrarTicket.executeUpdate()
            val commit = objConexion.prepareStatement( "commit")!!
            commit.executeUpdate()
        }
        Datos=listaDatos.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Unir RecyclerView con la Card
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)


        return ViewHolder(vista)
    }
    //Devolver la cantidad de datos que hay
    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = Datos[position]
        holder.txtTituloCard.text = item.titulo

        holder.btnElimCard.setOnClickListener {
            val contexto = holder.itemView.context
            val builder = AlertDialog.Builder(contexto)
            builder.setTitle("Eliminar ticket?")
            builder.setPositiveButton("Eliminar"){dialog,wich ->
                elimiarTickets(item.num_ticket, position)
            }
            builder.setNegativeButton("Cancelar"){dialog,wich ->

            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

}