package rodrigo.hurtado.appcrud

import Modelo.ClaseConexion
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rodrigo.hurtado.appcrud.databinding.FragmentLoginBinding


class Login : Fragment() {
  private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)

        val Correo= binding.txtCorreo.text
        val Clave = binding.txtClave.text
        binding.btnIniciar.setOnClickListener {
           CoroutineScope(Dispatchers.Main).launch{
               if(inicioSesion(Correo.toString(), Clave.toString(), findNavController() )) {
                   //Podemos llamara inicioSesion ya que estamos en la Corrutina Main
                   val paquete = Bundle().apply { //aqui pondremos los datos que nos llevaremos al main menu
                       putString("correo", Correo.toString())
                       putString("clave", Clave.toString())
                   }
                   it.findNavController().navigate(R.id.action_login_to_main_menu, paquete) //y lo ponemos aqui para que se los envíe a la actividad
                   binding.txtCorreo.setText("")
                   binding.txtClave.setText("")
               } else {
                   if(Correo.toString() != "admin" && Clave.toString() != "admin"){
                       Toast.makeText(requireContext(), "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                   }
               }
           }
        }
        return binding.root
    }
    private suspend fun inicioSesion(correo: String, clave:String, navController: NavController): Boolean{ //recibimos el navController para poder hacer el cambio a la pantalla admin
        //Las funciones suspend se pueden llamar desde otras corrutinas u otras funciones de suspension
        return withContext(Dispatchers.IO) {//Significa que se ejecutará en el hilo IO
            try {
                val objConexion = ClaseConexion().CadenaConexion()
                val buscarUsuario = objConexion?.prepareStatement("select * from ac_Usuarios where Correo = ? and Clave = ?")!!
                buscarUsuario.setString(1, correo)
                buscarUsuario.setString(2,clave)
                val filas = buscarUsuario.executeQuery() //Filas es igual al numero de filas que el select encuentre, idealmente será solo 1
                if(correo == "admin" && clave== "admin"){
                    requireActivity().runOnUiThread {
                        navController.navigate(R.id.action_login_to_menu_admins)
                    }
                }
                filas.next()//si filas tiene un valor, retornara true
            } catch (e: Exception) {
                println(e)
                false//Si el executeQuery falla y por lo tanto no se encuentran filas, retorna false
            }
        }
    }

}