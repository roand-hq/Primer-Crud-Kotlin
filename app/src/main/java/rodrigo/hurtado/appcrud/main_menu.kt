package rodrigo.hurtado.appcrud

import Modelo.ClaseConexion
import android.icu.text.DateFormat
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class main_menu : AppCompatActivity() {
    val correo = intent?.extras?.getString("correo") //así recibimos los valores del log in
    val clave = intent?.extras?.getString("clave")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // configurar el navHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost_Menu) as NavHostFragment
        val navController = navHostFragment.navController
        //configurar el bottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        NavigationUI.setupWithNavController(bottomNavigationView,navController)

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.ui_tickets -> fabAdd.show()
                else -> fabAdd.hide()
            }
        } // hasta aca llega el addOnDestinationChangedListener


        val paquete = bundleOf("correo" to correo, "clave" to clave) //metemos todas las cosas en un paquete
        navController.navigate(R.id.ui_tickets,paquete) //navegamos a Tickets para que se muestre al inicio
        bottomNavigationView.setOnNavigationItemSelectedListener { item -> //así enviamos los valores a los fragments
            when(item.itemId) {
                R.id.ui_tickets -> {
                    navController.navigate(R.id.ui_tickets,paquete) //es necesario
                    //de otro modo los valores se pierden al ir ui_perfil y volver
                    true
                }
                R.id.ui_perfil -> {
                    navController.navigate(R.id.ui_perfil,paquete)
                    true
                } else -> false
            }
        }
        fabAdd.setOnClickListener {//Lo que pasa al oprimir el boton de agregar
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_layout, null)
            builder.setView(dialogLayout)
            val dialog = builder.create()

            val editTextTitle = dialogLayout.findViewById<EditText>(R.id.editTextTitle)
            val editTextDescription = dialogLayout.findViewById<EditText>(R.id.editTextDescription)
            val buttonCancel = dialogLayout.findViewById<Button>(R.id.buttonCancel)
            val buttonCreateTicket = dialogLayout.findViewById<Button>(R.id.buttonCreateTicket)

            buttonCancel.setOnClickListener {
                dialog.dismiss()
            }

            buttonCreateTicket.setOnClickListener {
                val titulo = editTextTitle.text.toString()
                val descripcion = editTextDescription.text.toString()
                CoroutineScope(Dispatchers.Main).launch {
                    insertarTickets(titulo, descripcion)
                }
                dialog.dismiss()
            }

            dialog.show()
        }
    }
    private suspend fun insertarTickets(titulo: String, descripcion: String) {
        withContext(Dispatchers.IO) {
                val fecha_actual = LocalDate.now() //como obtener la fecha actual
            val formato = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val fechaConFormato = fecha_actual.format(formato)

            val objConexion = ClaseConexion().CadenaConexion()
            val addTicket = objConexion?.prepareStatement("insert into ac_tickets values(?,?,?,?,?,?,?,?)")!!
            addTicket.setString(1,"default")
            addTicket.setString(2,titulo)
            addTicket.setString(3,descripcion)
            addTicket.setString(4, "(SELECT Nombre || ' ' || Apellido FROM ac_Usuarios WHERE Correo = '$correo')")
            addTicket.setString(5, correo)
            addTicket.setString(6, fechaConFormato)
            addTicket.setString(7, "null")
            addTicket.setString(8, "Activo")
            addTicket.executeUpdate()
        }
    }
}