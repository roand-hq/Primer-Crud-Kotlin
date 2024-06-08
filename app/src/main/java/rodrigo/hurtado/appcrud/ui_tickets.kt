package rodrigo.hurtado.appcrud

import Modelo.ClaseConexion
import Modelo.tbTickets
import RecyclerViewHelpers.Adaptador
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rodrigo.hurtado.appcrud.databinding.FragmentUiTicketsBinding

class ui_tickets : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_ui_tickets, container, false)
        val correo = arguments?.getString("correo")
        val clave = arguments?.getString("clave")

        val rcvTickets = vista.findViewById<RecyclerView>(R.id.rcvTickets)
        rcvTickets.layoutManager = LinearLayoutManager(requireContext())
        fun obtenerTickets(): List<tbTickets>{
            val objConexion = ClaseConexion().CadenaConexion()
            val traerTickets = objConexion?.prepareStatement("select * from ac_tickets where email_autor = ?")!!
            traerTickets.setString(1, correo)
            val resultSet = traerTickets.executeQuery()
            val listaTickets = mutableListOf<tbTickets>()
            while(resultSet.next()){
                val numTicket = resultSet.getInt("NUM_TICKET")
                val titulo = resultSet.getString("TITULO")
                val descripcion = resultSet.getString("DESCRIPCION")
                val autor = resultSet.getString("AUTOR")
                val emailAutor = resultSet.getString("EMAIL_AUTOR")
                val fechaCreacion = resultSet.getString("FECHA_CREACION")
                val fechaCierre = resultSet.getString("FECHA_CIERRE")
                val estado = resultSet.getString("ESTADO")


                val valoresJuntos = tbTickets(numTicket, titulo, descripcion, autor, emailAutor, fechaCreacion, fechaCierre, estado)
                listaTickets.add(valoresJuntos)
            }
            return listaTickets
        }
        CoroutineScope(Dispatchers.IO).launch {
            val ticketsDB = obtenerTickets()
            withContext(Dispatchers.Main) {
                val adapter = Adaptador(ticketsDB)
                rcvTickets.adapter = adapter
            }
        }
        return vista
    }
}