package uz.xabardor.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import uz.xabardor.R
import uz.xabardor.extensions.language.Language
import uz.xabardor.extensions.language.LanguageManager
import uz.xabardor.rest.models.weather.Viloyat

class WeatherAdapter(val context: Context, var dataSource: List<Viloyat>) : BaseAdapter() {

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.simple_spinner_item, parent, false)

            
            vh = ItemHolder(view)
            context.let {
                var language: Language = LanguageManager(it).currentLanguage
//               var textSizes: TextSizes = TextSizes(it)
                Language.getNameByLanguage(dataSource.get(position).uz,dataSource.get(position).uz_cyrl, language)?.let {
                    vh.label.text = it
                }
                }
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
       
        return view
    }

    private class ItemHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.spinner_name) as TextView
    
    
    }
}
