package rodrigo.hurtado.appcrud

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView



class ui_perfil : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista =  inflater.inflate(R.layout.fragment_ui_perfil, container, false)

        val correo = arguments?.getString("correo")
        val clave = arguments?.getString("clave")

        val txtPerfilCorreo = vista.findViewById<TextView>(R.id.txtPerfilCorreo)
        val txtPerfilClave = vista.findViewById<TextView>(R.id.txtPerfilClave)


        return vista
    }

}