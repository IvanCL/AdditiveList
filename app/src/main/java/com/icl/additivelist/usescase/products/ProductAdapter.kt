package com.icl.additivelist.usescase.products

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.*
import com.icl.additivelist.R
import com.icl.additivelist.databinding.ItemProductBinding // Importa el binding
import com.icl.additivelist.models.Products

class ProductAdapter(private val items: ArrayList<Products>, private val context: Context) : RecyclerView.Adapter<ProductAdapter.ViewProductHolder>() {

    inner class ViewProductHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: Products, context: Context) {
            if (!item.name.isNullOrEmpty() && !item.uses.isNullOrEmpty() && !item.description.isNullOrEmpty() && !item.origin.isNullOrEmpty()) {
                binding.productNameTxt.text = item.name
                binding.productDescriptionTxt.text = item.description
                binding.productOrigin.text = item.origin

                // Check type of additive
                var newIconImage: Drawable?
                newIconImage = ContextCompat.getDrawable(context, R.drawable.vegan_icon_ok)
                binding.vegNoVegIcon.setImageDrawable(newIconImage!!)
                binding.itemProduct.setBackgroundColor(ContextCompat.getColor(context, R.color.colorVegan))

                // Check origin
                if (item.origin.trim().contains("No vegano", true)) {
                    newIconImage = ContextCompat.getDrawable(context, R.drawable.skull_icon)
                    binding.vegNoVegIcon.setImageDrawable(newIconImage!!)
                    binding.itemProduct.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDangerous))
                } else if (item.origin.trim().equals("Dudoso", true)) {
                    newIconImage = ContextCompat.getDrawable(context, R.drawable.question_icon)
                    binding.vegNoVegIcon.setImageDrawable(newIconImage!!)
                    binding.itemProduct.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDoubtful))
                }

                binding.root.setOnClickListener {
                    val intent = Intent(context, ProductsDetailActivity::class.java)
                    intent.putExtra("product", item)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewProductHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ViewProductHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewProductHolder, position: Int) {
        holder.bindItems(items[position], context)
    }
}