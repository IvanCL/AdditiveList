package com.icl.additivelist.usescase.additives

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.icl.additivelist.data.PreferencesUtils
import com.icl.additivelist.globals.ADDITIVES
import com.icl.additivelist.globals.GlobalActivity
import com.icl.additivelist.models.Additive
import com.icl.additivelist.R
import com.icl.additivelist.databinding.ActivityFindAdditivesBinding // Importa el binding

class AdditivesActivity : GlobalActivity() {

    private lateinit var binding: ActivityFindAdditivesBinding // Declara el binding
    var additiveList: MutableSet<String>? = null
    var foundsAdditives: ArrayList<Additive> = arrayListOf()

    // region LifeCircle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindAdditivesBinding.inflate(layoutInflater) // Infla el layout
        setContentView(binding.root) // Usa binding.root
        binding.containerAdditives.layoutManager = LinearLayoutManager(this) // Usa binding para acceder al RecyclerView
        findAdditive()
    }
    // endregion LifeCircle

    // region UI
    private fun findAdditive() {
        additiveList = PreferencesUtils(this.applicationContext).getPreference(ADDITIVES)

        binding.finderTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                foundsAdditives.clear() // Limpia la lista aquí al final de la búsqueda anterior
                if (!s.isBlank() && s.length >= 2) {
                    if (additiveList != null) {
                        additiveList!!.forEach { additive: String ->
                            Log.d("ADITIVOS [BUSQUEDA]", additive)
                            val array = additive.split("|")
                            val item = Additive(
                                array[0],
                                array[1],
                                array[2],
                                array[3],
                                array[4],
                                array[5],
                                array[6]
                            )
                            if (item.name.contains(s, true) || (item.numb.startsWith("E") && item.numb.contains(s))) {
                                foundsAdditives.add(item)
                            }
                        }
                    }
                }
                val additiveAdapter = AdditiveAdapter(foundsAdditives, this@AdditivesActivity)
                binding.containerAdditives.adapter = additiveAdapter
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                // No es necesario limpiar aquí, se hace en afterTextChanged al inicio de una nueva búsqueda
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                // No se necesita lógica específica aquí en este caso
            }
        })
        // Inicializa el adaptador con una lista vacía al inicio
        binding.containerAdditives.adapter = AdditiveAdapter(arrayListOf(), this@AdditivesActivity)
    }
    // endregion UI
}