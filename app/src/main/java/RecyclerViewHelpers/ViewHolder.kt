package RecyclerViewHelpers
import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rodrigo.hurtado.appcrud.R

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val txtTituloCard = view.findViewById<TextView>(R.id.txtTituloCard)
    val iconEstado = view.findViewById<ImageView>(R.id.iconEstado)
    val btnElimCard = view.findViewById<ImageView>(R.id.btnElimCard)
    val txtEstado = view.findViewById<TextView>(R.id.txtEstado)


}