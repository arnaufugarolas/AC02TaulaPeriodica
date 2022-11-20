package com.mp08.ac02taulaperidica

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mp08.ac02taulaperidica.dataClass.Element

class ElementsAdapter(private val mList: MutableList<Element>) :
    RecyclerView.Adapter<ElementsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.element_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        when (ItemsViewModel.phase) {
            "Solid" -> holder.symbol.setTextAppearance(R.style.SolidSymbol)
            "Liquid" -> holder.symbol.setTextAppearance(R.style.LiquidSymbol)
            "Gas" -> holder.symbol.setTextAppearance(R.style.GasSymbol)
            "Unknown" -> holder.symbol.setTextAppearance(R.style.UnknownSymbol)
        }

        // var category = ItemsViewModel.category.toString()
        // if (category.matches(Regex("^*unknown*$"))) holder.item.style(R.style.NoCategory)
        // else if (category.matches(Regex("^*nonmetal*$")))  holder.item.style(R.style.NonMetal)
        // else if(category.matches(Regex("^*noble*$"))) holder.item.style(R.style.NobleGas)
        // else if (category.matches(Regex("^*alkaline*$"))) holder.item.style(R.style.Alkaline)
        // else if (category.matches(Regex("^*alkali*$"))) holder.item.style(R.style.Alkali)
        // else if (category.matches(Regex("^*metalloid*$"))) holder.item.style(R.style.Metalloid)
        // else if (category.matches(Regex("^*post-transition*$"))) holder.item.style(R.style.Post)
        // else if (category.matches(Regex("^*transition*$"))) holder.item.style(R.style.Transition)
        // else if (category.matches(Regex("^*lanthanide*$"))) holder.item.style(R.style.Lanthanoid)
        // else if (category.matches(Regex("^*actinide*$"))) holder.item.style(R.style.Actinoid)

        holder.symbol.text = ItemsViewModel.symbol
        holder.number.text = ItemsViewModel.number.toString()
        holder.name.text = ItemsViewModel.name
        holder.atomicMass.text = ItemsViewModel.atomicMass.toString()

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val item: ConstraintLayout = itemView.findViewById(R.id.CLItem)
        val symbol: TextView = itemView.findViewById(R.id.TVSymbol)
        val number: TextView = itemView.findViewById(R.id.TVNumberData)
        val name: TextView = itemView.findViewById(R.id.TVNameData)
        val atomicMass: TextView = itemView.findViewById(R.id.TVAtomicMassData)
    }
}