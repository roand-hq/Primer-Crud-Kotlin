package rodrigo.hurtado.appcrud

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import rodrigo.hurtado.appcrud.databinding.FragmentUiTicketsBinding

class ui_tickets : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_ui_tickets, container, false)

        val correo = arguments?.getString("correo")
        val clave = arguments?.getString("clave")

        val txtPruebaCorreo = vista.findViewById<TextView>(R.id.txtPruebaCorreo)
        val txtPruebaClave = vista.findViewById<TextView>(R.id.txtPruebaClave)

        txtPruebaCorreo.text = correo
        txtPruebaClave.text = clave
        return vista



    }

}