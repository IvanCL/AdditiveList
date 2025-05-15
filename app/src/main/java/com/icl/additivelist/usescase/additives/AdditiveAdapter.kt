package com.icl.additivelist.usescase.additives

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import com.icl.additivelist.R
import com.icl.additivelist.databinding.ItemAdditiveBinding // Importa el binding generado
import com.icl.additivelist.models.Additive
import com.icl.additivelist.usescase.additives.AdditiveDetailActivity
import java.util.*

class AdditiveAdapter(private val items: ArrayList<Additive>, private val context: Context) : RecyclerView.Adapter<AdditiveAdapter.AdditiveViewHolder>() {

    inner class AdditiveViewHolder(val binding: ItemAdditiveBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: Additive, context: Context, items: ArrayList<Additive>) {
            if (!item.name.isNullOrEmpty() && !item.numb.isNullOrEmpty() && !item.description.isNullOrEmpty() && !item.origin.isNullOrEmpty()) {
                binding.additiveNameTxt.text = "${item.numb} - ${item.name}"
                binding.descriptionTxt.text = item.description

                // Check type of additive
                var newIconImage: Drawable?
                newIconImage = getDrawable(context, R.drawable.vegan_icon_ok)
                binding.vegNoVegIcon.setImageDrawable(newIconImage!!)
                binding.originAdditive.text = item.origin
                binding.itemAdditive.setBackgroundColor(ContextCompat.getColor(context, R.color.colorVegan))

                // Check origin
                if (item.origin.trim().equals("No vegano", true)) {
                    newIconImage = getDrawable(context, R.drawable.skull_icon)
                    binding.vegNoVegIcon.setImageDrawable(newIconImage!!)
                    binding.itemAdditive.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDangerous))
                } else if (item.origin.trim().equals("Dudoso", true)) {
                    newIconImage = getDrawable(context, R.drawable.question_icon)
                    binding.vegNoVegIcon.setImageDrawable(newIconImage!!)
                    binding.itemAdditive.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDoubtful))
                }
                binding.root.setOnClickListener {
                    val intent = Intent(context, AdditiveDetailActivity::class.java)
                    intent.putExtra("additive", item)
                    intent.putExtra("founds", items)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdditiveViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAdditiveBinding.inflate(inflater, parent, false)
        return AdditiveViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdditiveViewHolder, position: Int) {
        holder.bindItems(items[position], context, items)
    }
}