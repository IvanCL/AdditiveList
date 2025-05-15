package com.icl.additivelist.usescase.products

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.google.gson.Gson
import com.icl.additivelist.R
import com.icl.additivelist.config.ConfigProperties
import com.icl.additivelist.globals.GlobalActivity
import com.icl.additivelist.models.Products
import com.icl.additivelist.databinding.ActivityProductsBinding // Importa el binding
import java.net.URL

class ProductsActivity : GlobalActivity() {

    private lateinit var binding: ActivityProductsBinding // Declara el binding
    var productsList: List<Products>? = null
    var foundsProducts: ArrayList<Products> = arrayListOf()
    var allProducts: ArrayList<Products> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater) // Infla el layout
        setContentView(binding.root) // Usa binding.root
        binding.containerProducts.layoutManager = LinearLayoutManager(this) // Usa binding
        //getProducts("")
        //findProduct()
    }

    private fun getProducts(name: String) {
        val url: String? = if (name.isNullOrEmpty()) {
            ConfigProperties.getValue("url.products", this.applicationContext)
        } else {
            ConfigProperties.getValue("url.productsByName", this.applicationContext) + name
        }
        callApiProducts(url, name)
    }

    private fun callApiProducts(url: String?, name: String) {
        Thread {
            val result = URL(url).readText()
            runOnUiThread {
                Log.d("PRODUCTOS RECIBIDOS", result)
                productsList = Gson().fromJson(result, Array<Products>::class.java).toList()

                productsList?.forEach { product ->
                    if (name.isNullOrEmpty()) {
                        allProducts.add(product)
                        val productAdapter = ProductAdapter(allProducts, this@ProductsActivity)
                        binding.containerProducts.adapter = productAdapter // Usa binding
                    } else if (product.name.toLowerCase().contains(name.toLowerCase())) {
                        foundsProducts.add(product)
                        val productAdapter = ProductAdapter(foundsProducts, this@ProductsActivity)
                        binding.containerProducts.adapter = productAdapter // Usa binding
                    }
                }
                if (name.isNullOrEmpty() && allProducts.isNotEmpty()) {
                    binding.containerProducts.adapter = ProductAdapter(allProducts, this@ProductsActivity) // Asegura que el adaptador se inicialice al cargar todos los productos
                } else if (!name.isNullOrEmpty() && foundsProducts.isNotEmpty()) {
                    binding.containerProducts.adapter = ProductAdapter(foundsProducts, this@ProductsActivity) // Asegura que el adaptador se actualice con los resultados de la búsqueda
                } else if (productsList.isNullOrEmpty()) {
                    binding.containerProducts.adapter = ProductAdapter(arrayListOf(), this@ProductsActivity) // Muestra una lista vacía si no hay productos
                }
            }
        }.start()
    }

    private fun findProduct() {
        binding.finderProductTxt.addTextChangedListener(object : TextWatcher { // Usa binding
            override fun afterTextChanged(s: Editable) {
                if (!s.isBlank() && s.length >= 3) {
                    getProducts(s.toString())
                } else {
                    getProducts("") // Si el texto está vacío o tiene menos de 3 caracteres, muestra todos los productos
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                foundsProducts.clear() // Limpia los resultados previos antes de una nueva búsqueda
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                // No se necesita lógica específica aquí
            }
        })
        // Inicializa el RecyclerView con un adaptador vacío o con todos los productos al inicio
        binding.containerProducts.adapter = ProductAdapter(allProducts, this@ProductsActivity)
    }
}