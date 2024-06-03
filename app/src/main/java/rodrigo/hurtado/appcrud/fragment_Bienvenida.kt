package rodrigo.hurtado.appcrud

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import rodrigo.hurtado.appcrud.databinding.FragmentBienvenidaBinding


class fragment_Bienvenida : Fragment() {
    private lateinit var binding:FragmentBienvenidaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentBienvenidaBinding.inflate(inflater, container, false)

        binding.btnSignUp.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragment_Bienvenida_to_registro)
        }

        binding.btnLogIn.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragment_Bienvenida_to_login)
        }

        return binding.root
    }


}