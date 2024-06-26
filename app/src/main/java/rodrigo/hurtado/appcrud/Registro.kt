package rodrigo.hurtado.appcrud

import Modelo.ClaseConexion
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rodrigo.hurtado.appcrud.databinding.FragmentBienvenidaBinding
import rodrigo.hurtado.appcrud.databinding.FragmentRegistroBinding


class Registro : Fragment() {
   private lateinit var binding: FragmentRegistroBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =  FragmentRegistroBinding.inflate(inflater, container, false)

        val txtNombre = binding.txtNombre
        val txtApellido = binding.txtApellido
        val txtCorreo = binding.txtCorreo
        val txtClave = binding.txtClave
        val txtConfirmar = binding.txtConfirmar
        binding.btnRegistro.setOnClickListener {

            if(txtNombre.text.isEmpty() || txtApellido.text.isEmpty() || txtClave.text.isEmpty() || txtConfirmar.text.isEmpty()) {
                mostrarAlerta("Advertencia","Por favor, llena todos los campos")
            }
              else {
                  var clave = txtClave.text
                var correo = txtCorreo.text
                var confirmacion = txtConfirmar.text
                if(clave.length !in 8..12) { //Si la longitud de la contraseña NO esta entre 8 y 12 ...
                    mostrarAlerta("Advertencia","La constraseña debe contener entre 8 y 12 caracteres")
                } else {
                        if(!(clave.contains(Regex("[0-9]+")) && clave.contains(Regex(".*[A-Z].*")))) { //Si la contraseña NO contiene numeros NI mayúsculas
                            mostrarAlerta("Advertencia","La contraseña debe contener números y mayúsculas")
                        } else if(!(confirmacion.matches(Regex("$clave")))){ //si la confirmación NO coincide con la contraseña
                            mostrarAlerta("Advertencia","La constraseña no coincide")
                        } else { //RECIEN ACA TERMINA LA VALIDACION DE CONTRASEÑA
                         if(!(validarCorreo("$correo"))) { //OTRA VALIDACION NO DIOS TE LO SUPLICO
                             mostrarAlerta("Advertencia","Por favor, ingresa un correo válido")
                         } else { //RECIEN ACA EMPIEZA EL CRUD JSJSJS
                             CoroutineScope(Dispatchers.IO).launch {
                                 val objConexion = ClaseConexion().CadenaConexion() // creamos el objeto de la clase conexion
                                 val addUsuarios = objConexion?.prepareStatement("insert into ac_Usuarios(Nombre,Apellido,Correo,Clave) values(?,?,?,?)")!! //aqui se pone el comando sql
                                 addUsuarios.setString(1, txtNombre.text.toString()) //aqui se llena cada uno de los "?" de forma lineal
                                 addUsuarios.setString(2, txtApellido.text.toString())
                                 addUsuarios.setString(3, txtCorreo.text.toString())
                                 addUsuarios.setString(4, txtClave.text.toString())
                                 addUsuarios.executeUpdate()
                             }
                             mostrarAlerta("Usuario creado exitosamente","")
                             it.findNavController().navigate(R.id.action_registro_to_login) //nos vamos a la pantalla de login
                         }
                        }
                }
              }
        }
        return binding.root
    }

    public fun mostrarAlerta(Titulo:String, mensaje: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(Titulo)
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun validarCorreo(correo: String): Boolean {
        val patronCorreo = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return patronCorreo.matches(correo)
    }
}