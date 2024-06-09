package RecyclerViewHelpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rodrigo.hurtado.appcrud.R

class ViewHolder_Admins(view: View): RecyclerView.ViewHolder(view) {
    val txtTituloAdmins = view.findViewById<TextView>(R.id.txtTituloAdmins)
    val estadoAdminsTexto = view.findViewById<TextView>(R.id.estadoAdminTexto)
    val estadoAdmins = view.findViewById<ImageView>(R.id.estadoAdmin)
    val txtAutor = view.findViewById<TextView>(R.id.txtAutor)
    val btnCerrarTicket = view.findViewById<ImageView>(R.id.btnCerrarTicket)
}
