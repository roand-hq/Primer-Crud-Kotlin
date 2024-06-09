package RecyclerViewHelpers

import Modelo.ClaseConexion
import Modelo.tbTickets
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rodrigo.hurtado.appcrud.R

class Adaptador(
    var Datos: List<tbTickets>
): RecyclerView.Adapter<ViewHolder>() {

    fun actualizarListaDespuesDeActualizarDatos(num_ticket: Int, nuevoTitulo: String, nuevaDesc: String){
        val index=Datos.indexOfFirst { it.num_ticket == num_ticket}
        Datos[index].titulo=nuevoTitulo
        Datos[index].descripcion =nuevaDesc
        notifyItemChanged(index)
    }
    fun actualizarTickets(titulo: String, desc: String, num_ticket: Int){
        GlobalScope.launch(Dispatchers.IO) {
            val objClaseConexion = ClaseConexion().CadenaConexion()
            val updTicket = objClaseConexion?.prepareStatement("update ac_tickets set Titulo = ?, descripcion = ? where num_ticket = ?")!!
            updTicket.setString(1, titulo)
            updTicket.setString(2, desc)
            updTicket.setInt(3, num_ticket)
            updTicket.executeUpdate()
            val commit = objClaseConexion.prepareStatement("commit")
            commit.executeUpdate()
            withContext(Dispatchers.Main) {
                actualizarListaDespuesDeActualizarDatos(num_ticket,titulo,desc)
            }
        }
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
        holder.txtEstado.text = item.estado

        if(item.estado != "ACTIVO"){
            holder.iconEstado.setImageResource(R.drawable.ic_estado_cerrado)
            holder.btnUpdateCard.visibility = View.GONE
        }

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
        holder.btnUpdateCard.setOnClickListener {
            val contexto = holder.itemView.context
            val builder = AlertDialog.Builder(contexto)
            val dialogLayout = LayoutInflater.from(contexto).inflate(R.layout.dialog_layout, null)
            //ESTA LINEA DE ARRIBA HIZO QUE ME TARDARA 2 DIAS EN LOGRAR ESTO, PERO AL FIN FUNCIONA
            builder.setView(dialogLayout)
            val dialog = builder.create()
            val editTextTitle = dialogLayout.findViewById<EditText>(R.id.editTextTitle)
            val editTextDescription = dialogLayout.findViewById<EditText>(R.id.editTextDescription)
            val buttonCancel = dialogLayout.findViewById<Button>(R.id.buttonCancel)
            val buttonCreateTicket = dialogLayout.findViewById<Button>(R.id.buttonCreateTicket)
            editTextTitle.setHint(item.titulo)
            editTextDescription.setHint(item.descripcion)
            buttonCreateTicket.text = "Actualizar ticket"

            buttonCreateTicket.setOnClickListener {
                val nuevoTitulo = editTextTitle.text.toString()
                val nuevaDescripcion = editTextDescription.text.toString()
                actualizarTickets(nuevoTitulo,nuevaDescripcion,item.num_ticket)

                dialog.dismiss()
            }
            buttonCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
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