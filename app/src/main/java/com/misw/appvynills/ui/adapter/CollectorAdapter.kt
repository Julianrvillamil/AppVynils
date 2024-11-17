import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misw.appvynills.R
import com.misw.appvynills.model.Collector

class CollectorAdapter(private val collectors: List<Collector>) :
    RecyclerView.Adapter<CollectorAdapter.CollectorViewHolder>() {

    class CollectorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.textViewCollectorName)
        val emailTextView: TextView = view.findViewById(R.id.textViewCollectorEmail)
        val phoneTextView: TextView = view.findViewById(R.id.textViewCollectorPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_collector, parent, false)
        return CollectorViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        val collector = collectors[position]
        holder.nameTextView.text = collector.name
        holder.emailTextView.text = collector.email
        holder.phoneTextView.text = collector.telephone
    }

    override fun getItemCount(): Int = collectors.size
}
