package com.icl.additivelist.usescase.products

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.google.gson.Gson
import com.icl.additivelist.R
import com.icl.additivelist.config.ConfigProperties
import com.icl.additivelist.globals.GlobalActivity
import com.icl.additivelist.models.Products
import kotlinx.android.synthetic.main.activity_products.*
import org.json.JSONObject
import java.io.InputStream
import java.net.URL

class ProductsActivity : GlobalActivity() {

    var productsList : List<Products>? = null
    var foundsProducts: ArrayList<Products> = arrayListOf()
    var allProducts: ArrayList<Products> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        containerProducts.layoutManager = LinearLayoutManager(this)

        getProducts("")
        findProduct()
    }

    private fun getProducts(name:String){
        /* var obj = readJSONFromAsset()
         productsList = Gson().fromJson(obj, Array<Products>::class.java).toList()*/
        var url : String?

        if (name.isNullOrEmpty()){
            url =ConfigProperties.getValue(
                "url.products",
                this.applicationContext
            )
            callApiProducts(url, name)

        }else{
            url = ConfigProperties.getValue(
                "url.productsByName",
                this.applicationContext
            )+ name
            callApiProducts(url, name)
        }

    }

    private fun callApiProducts(url: String?, name: String) {
        var result: String
        Thread {
            result = URL(
                url
            ).readText()
            this.runOnUiThread {
                Log.d("PRODUCTOS RECIBIDOS", result)
                productsList = Gson().fromJson(result, Array<Products>::class.java).toList()

                productsList!!.forEach { product: Products ->
                    run {

                        if (name.isNullOrEmpty()){
                            allProducts.add(product)
                            val productAdapter =
                                ProductAdapter(allProducts, this@ProductsActivity)
                            containerProducts.adapter = productAdapter
                        }else if (product.name.toLowerCase().contains(name.toLowerCase())){
                            foundsProducts.add(product)
                            val productAdapter =
                                ProductAdapter(foundsProducts, this@ProductsActivity)
                            containerProducts.adapter = productAdapter
                        }
                    }
                }
            }
        }.start()
    }

    private fun readJSONFromAsset(): String? {
        var json: String? = null
        try {
            val  inputStream: InputStream = assets.open("products.json")
            json = inputStream.bufferedReader().use{it.readText()}
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun findProduct() {

        finderProductTxt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (!s.isBlank() && s.length >= 3) {
                    getProducts(s.toString())
/*                    if (!productsList.isNullOrEmpty()){
                        productsList!!.forEach { product: Products ->
                            run {
                                if (product.name.toLowerCase().contains(s.toString().toLowerCase()))
                                    foundsProducts.add(product)
                            }
                        }

                        val productAdapter =
                            ProductAdapter(foundsProducts, this@ProductsActivity)
                        containerProducts.adapter = productAdapter
                    }*/

                    /*if (productsList != null) {
                        productsList!!.forEach { product: Products ->
                            run {
                                Log.d("PRODUCTOS [BUSQUEDA]", product.toString())
                                *//*val array = product.split("|")
                                val item = Products(
                                    array[0],
                                    array[1],
                                    array[2],
                                    array[3],
                                    array[4]
                                )*//*
                                if (product.name.contains(s)) {

                                    foundsProducts.add(product)
                                    val productAdapter =
                                        ProductAdapter(foundsProducts, this@ProductsActivity)
                                    containerProducts.adapter = productAdapter
                                }

                            }
                        }
                    }*/
                } else if (s.isBlank() || s.isEmpty()) {
                    foundsProducts.clear()
                    val productAdapter = ProductAdapter(allProducts, this@ProductsActivity)
                    containerProducts.adapter = productAdapter
                }

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                foundsProducts.clear()
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })
    }
}
