package com.mp08.ac02taulaperidica

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.Paris
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
            else -> holder.symbol.setTextAppearance(R.style.UnknownSymbol)
        }

        when(ItemsViewModel.category){
            "diatomic nonmetal" -> Paris.style(holder.item).apply(R.style.NonMetal)
            "noble gas" -> Paris.style(holder.item).apply(R.style.NobleGas)
            "alkali metal" -> Paris.style(holder.item).apply(R.style.Alkali)
            "alkaline earth metal" -> Paris.style(holder.item).apply(R.style.Alkaline)
            "metalloid" -> Paris.style(holder.item).apply(R.style.Metalloid)
            "polyatomic nonmetal" -> Paris.style(holder.item).apply(R.style.NonMetal)
            "post-transition metal" -> Paris.style(holder.item).apply(R.style.Post)
            "transition metal" -> Paris.style(holder.item).apply(R.style.Transition)
            "lanthanide" -> Paris.style(holder.item).apply(R.style.Lanthanoid)
            "actinide" -> Paris.style(holder.item).apply(R.style.Actinoid)
            else -> Paris.style(holder.item).apply(R.style.NoCategory)
        }


        holder.symbol.text = ItemsViewModel.symbol
        holder.number.text = ItemsViewModel.number.toString()
        holder.name.text = ItemsViewModel.name
        holder.atomicMass.text = ItemsViewModel.atomicMass.toString()

        when(ItemsViewModel.favorite){
            true -> holder.favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            false -> holder.favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }

        holder.favorite.setOnClickListener {
            if (ItemsViewModel.favorite) {
                holder.favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                ItemsViewModel.favorite = false
            } else {
                holder.favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                ItemsViewModel.favorite = true
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val favorite: ImageView = itemView.findViewById(R.id.IVFavorite)
        var item: ConstraintLayout = itemView.findViewById(R.id.CLItem)
        val symbol: TextView = itemView.findViewById(R.id.TVSymbol)
        val number: TextView = itemView.findViewById(R.id.TVNumberData)
        val name: TextView = itemView.findViewById(R.id.TVNameData)
        val atomicMass: TextView = itemView.findViewById(R.id.TVAtomicMassData)
    }
}
