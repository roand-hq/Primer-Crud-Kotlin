package rodrigo.hurtado.appcrud

import Modelo.ClaseConexion
import Modelo.tbTickets
import RecyclerViewHelpers.Adaptador_Admins
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class menu_admins : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_admins)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val fabIrse = findViewById<FloatingActionButton>(R.id.fab_logoff)
        val rcvAdmins = findViewById<RecyclerView>(R.id.rcvAdmins)
        rcvAdmins.layoutManager = LinearLayoutManager(this)
        fun obtenerTickets(): List<tbTickets>{
            val objConexion = ClaseConexion().CadenaConexion()
            val traerTickets = objConexion?.prepareStatement("select * from ac_tickets order by num_ticket desc")!!
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
            val tickets = obtenerTickets()
            withContext(Dispatchers.Main) {
                val Adaptador = Adaptador_Admins(tickets)
                rcvAdmins.adapter = Adaptador
            }
        }
        fabIrse.setOnClickListener {
            val intento = Intent(this, MainActivity::class.java)
            startActivity(intento)
        }
    }
}