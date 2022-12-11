package com.mp08.ac02taulaperidica

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.Paris
import com.google.gson.Gson
import com.mp08.ac02taulaperidica.dataClass.Element
import com.mp08.ac02taulaperidica.dataClass.PeriodicTable


class ElementsAdapter(private val context: Context, private val mList: MutableList<Element>) :
    RecyclerView.Adapter<ElementsAdapter.ViewHolder>() {
    private val gson: Gson = Gson()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.element_item, parent, false)
        val viewHolder = ViewHolder(view)

        viewHolder.itemView.setOnClickListener {
            val i = Intent(viewHolder.itemView.context, ElementDetails::class.java)
            i.putExtra(
                "element",
                gson.toJson(mList[viewHolder.adapterPosition], Element::class.java)
            )
            viewHolder.itemView.context.startActivity(i)
        }

        viewHolder.favorite.setOnClickListener {
            val position = viewHolder.adapterPosition
            val itemDetails = mList[position]

            itemDetails.favorite = !itemDetails.favorite
            this.notifyItemChanged(position)
            saveFavorite(viewHolder)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bind(holder, mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    private fun saveFavorite(viewHolder: ViewHolder) {
        val position = viewHolder.adapterPosition
        val itemDetails = mList[position]

        val sharedPreferences = context.getSharedPreferences(
            "PeriodicTablePreferences",
            MODE_PRIVATE
        )
        val persistentPeriodicTable = sharedPreferences.getString("periodicTable", null)
        val periodicTable = gson.fromJson(persistentPeriodicTable, PeriodicTable::class.java)
        periodicTable.elements[itemDetails.number!! - 1].favorite = itemDetails.favorite
        val editor = sharedPreferences.edit()
        editor.putString("periodicTable", gson.toJson(periodicTable, PeriodicTable::class.java))
        editor.apply()
    }

    private fun bind(holder: ViewHolder, itemsViewModel: Element) {
        when (itemsViewModel.phase) {
            "Solid" -> holder.symbol.setTextAppearance(R.style.ElementItemSolidSymbol)
            "Liquid" -> holder.symbol.setTextAppearance(R.style.ElementItemLiquidSymbol)
            "Gas" -> holder.symbol.setTextAppearance(R.style.ElementItemGasSymbol)
            else -> holder.symbol.setTextAppearance(R.style.ElementItemUnknownSymbol)
        }

        when (itemsViewModel.category) {
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

        when (itemsViewModel.favorite) {
            true -> holder.favorite.setImageResource(R.drawable.ic_baseline_bookmark_24)
            false -> holder.favorite.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
        }

        holder.symbol.text = itemsViewModel.symbol
        holder.number.text = itemsViewModel.number.toString()
        holder.name.text = itemsViewModel.name
        holder.atomicMass.text = itemsViewModel.atomicMass.toString()
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