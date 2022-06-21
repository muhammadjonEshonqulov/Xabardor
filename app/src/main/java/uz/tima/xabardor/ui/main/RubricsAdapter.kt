package uz.tima.xabardor.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.tima.xabardor.R
import uz.tima.xabardor.extensions.language.Krill
import uz.tima.xabardor.extensions.language.Language
import uz.tima.xabardor.extensions.language.Uzbek
import uz.tima.xabardor.extensions.lg
import uz.tima.xabardor.rest.models.rubric.RubricsData

class RubricsAdapter : RecyclerView.Adapter<RubricsAdapter.RubricsHolder>() {

    var itemClickListener: ((data: RubricsData) -> Unit)? = null
    var data = ArrayList<RubricsData>()
    lateinit var language: Language

    fun setOnClickListener(lis: (data: RubricsData) -> Unit) {
        itemClickListener = lis
    }

    inner class RubricsHolder(view: View) : RecyclerView.ViewHolder(view) {

        var text1: TextView = view.findViewById(R.id.text1)

        fun bind(elem: RubricsData) {
            if (language.id == Uzbek().id){
                text1.text = elem.title

            } else if (language.id == Krill().id){
                text1.text = elem.title_cyrl
            }

            text1.setOnClickListener {
                itemClickListener?.invoke(elem)
            }
        }
    }

    override fun onBindViewHolder(holder: RubricsHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RubricsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.simple_rubrics_item, parent, false)
        return RubricsHolder(view)
    }
}