package rodrigo.hurtado.appcrud

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

class main_menu : AppCompatActivity() {

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
        val correo = intent?.extras?.getString("correo") //así recibimos los valores del log in
        val clave = intent?.extras?.getString("clave")

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
                dialog.dismiss() // Cerrar el diálogo
            }

            buttonCreateTicket.setOnClickListener {
                val title = editTextTitle.text.toString()
                val description = editTextDescription.text.toString()

                // Aquí puedes hacer algo con los datos ingresados, como crear un ticket
                // Por ahora, solo cerraremos el diálogo
                dialog.dismiss() // Cerrar el diálogo
            }

            dialog.show()
        }
    }
    private suspend fun insertarTickets() {

    }
}