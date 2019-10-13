package com.icl.additivelist.usescase.additives

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.icl.additivelist.models.Additive
import kotlinx.android.synthetic.main.item_additive.view.*
import java.util.*
import android.support.v4.content.ContextCompat.getDrawable
import com.icl.additivelist.R


class AdditiveAdapter(private val items: ArrayList<Additive>, private  val context: Context) : RecyclerView.Adapter<ViewHolder>() {


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(com.icl.additivelist.R.layout.item_additive, p0, false))
    }


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindItems(items.get(p1), context, items)
    }

}


class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bindItems(item: Additive, context: Context, items: ArrayList<Additive>) {
        if (!item.name.isNullOrEmpty() && !item.numb.isNullOrEmpty() && !item.description.isNullOrEmpty() && !item.origin.isNullOrEmpty()) {
            itemView.additiveNameTxt.text = item.numb + " - " + item.name
            itemView.descriptionTxt.text = item.description

            // Check type of additive
            var newIconImage: Drawable?
            newIconImage = getDrawable(context, R.drawable.vegan_icon_ok)
            itemView.vegNoVegIcon.setImageDrawable(newIconImage!!)
            itemView.originAdditive.text = item.origin
            itemView.itemAdditive.setBackgroundColor(ContextCompat.getColor(context, R.color.colorVegan))

            // Check origin
            if (item.origin.trim().equals("No vegano", true)) {
                newIconImage = getDrawable(context, R.drawable.skull_icon)
                //itemView.vegNoVegIcon.setBackgroundDrawable(new_image)

                itemView.vegNoVegIcon.setImageDrawable(newIconImage!!)
                itemView.itemAdditive.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDangerous))
            }else if (item.origin.trim().equals("Dudoso", true)){
                newIconImage = getDrawable(context, R.drawable.question_icon)
                itemView.vegNoVegIcon.setImageDrawable(newIconImage!!)
                itemView.itemAdditive.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDoubtful))
            }
            itemView.setOnClickListener {

                var intent = Intent(context, AdditiveDetailActivity::class.java)
                intent.putExtra("additive", item)
                intent.putExtra("founds", items)
                context.startActivity(intent)
            }
        }
    }

}