package com.mp08.ac02taulaperidica

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.airbnb.paris.extensions.style
import com.google.gson.Gson
import com.mp08.ac02taulaperidica.dataClass.Element

class GameLostDialog(private val element: Element) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_game_lost, container, false)
        return setupDialog(view)
    }

    private fun setupDialog(view: View): View {
        val symbol = view.findViewById<TextView>(R.id.TVDialogSymbol)
        val message = view.findViewById<TextView>(R.id.TVMessage)

        when (element.category) {
            "diatomic nonmetal" -> symbol.style { add(R.style.NonMetal) }
            "noble gas" -> symbol.style { add(R.style.NobleGas) }
            "alkali metal" -> symbol.style { add(R.style.Alkali) }
            "alkaline earth metal" -> symbol.style { add(R.style.Alkaline) }
            "metalloid" -> symbol.style { add(R.style.Metalloid) }
            "polyatomic nonmetal" -> symbol.style { add(R.style.NonMetal) }
            "post-transition metal" -> symbol.style { add(R.style.Post) }
            "transition metal" -> symbol.style { add(R.style.Transition) }
            "lanthanide" -> symbol.style { add(R.style.Lanthanoid) }
            "actinide" -> symbol.style { add(R.style.Actinoid) }
            else -> symbol.style { add(R.style.NoCategory) }
        }

        when (element.phase) {
            "Solid" -> symbol.setTextAppearance(R.style.ElementItemSolidSymbol)
            "Liquid" -> symbol.setTextAppearance(R.style.ElementItemLiquidSymbol)
            "Gas" -> symbol.setTextAppearance(R.style.ElementItemGasSymbol)
            else -> symbol.setTextAppearance(R.style.ElementItemUnknownSymbol)
        }

        symbol.setOnClickListener {
            val i = Intent(context, ElementDetails::class.java)
            val gson = Gson()
            i.putExtra("element", gson.toJson(element, Element::class.java))
            startActivity(i)
        }

        symbol.text = element.symbol
        message.text = getString(R.string.lost_message, element.name, element.phase)

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}