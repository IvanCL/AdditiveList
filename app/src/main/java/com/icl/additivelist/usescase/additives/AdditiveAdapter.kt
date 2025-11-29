package com.icl.additivelist.usescase.additives

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.icl.additivelist.R
import com.icl.additivelist.databinding.ItemAdditiveBinding
import com.icl.additivelist.models.Additive

class AdditiveAdapter(private val context: Context) : ListAdapter<Additive, AdditiveAdapter.AdditiveViewHolder>(AdditiveDiffCallback()) {

    inner class AdditiveViewHolder(val binding: ItemAdditiveBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Additive) {
            binding.additiveNameTxt.text = "${item.numb} - ${item.name}"
            binding.descriptionTxt.text = item.description
            binding.originAdditive.text = item.origin

            val (iconRes, colorRes) = getVisualsForOrigin(item.origin)
            binding.vegNoVegIcon.setImageResource(iconRes)
            binding.itemAdditive.setCardBackgroundColor(context.getColor(colorRes))

            binding.root.setOnClickListener {
                val intent = Intent(context, AdditiveDetailActivity::class.java).apply {
                    putExtra("additive", item)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdditiveViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAdditiveBinding.inflate(inflater, parent, false)
        return AdditiveViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdditiveViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        fun getVisualsForOrigin(origin: String): Pair<Int, Int> {
            return when (origin.trim()) {
                "No vegano" -> Pair(R.drawable.skull_icon, R.color.colorDangerous)
                "Dudoso" -> Pair(R.drawable.question_icon, R.color.colorDoubtful)
                else -> Pair(R.drawable.vegan_icon_ok, R.color.colorVegan)
            }
        }
    }
}

class AdditiveDiffCallback : DiffUtil.ItemCallback<Additive>() {
    override fun areItemsTheSame(oldItem: Additive, newItem: Additive): Boolean {
        return oldItem.numb == newItem.numb
    }

    override fun areContentsTheSame(oldItem: Additive, newItem: Additive): Boolean {
        return oldItem == newItem
    }
}
