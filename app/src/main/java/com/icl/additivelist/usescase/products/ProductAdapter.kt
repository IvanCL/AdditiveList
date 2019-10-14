package com.icl.additivelist.usescase.products

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*
import com.icl.additivelist.R
import com.icl.additivelist.models.Products
import kotlinx.android.synthetic.main.item_product.view.*


class ProductAdapter(private val items: ArrayList<Products>, private  val context: Context) : RecyclerView.Adapter<ViewProductHolder>() {


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewProductHolder {
        return ViewProductHolder(LayoutInflater.from(p0.context).inflate(com.icl.additivelist.R.layout.item_product, p0, false))
    }


    override fun onBindViewHolder(p0: ViewProductHolder, p1: Int) {
        p0.bindItems(items.get(p1), context)
    }

}


class ViewProductHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bindItems(item: Products, context: Context) {
        if (!item.name.isNullOrEmpty() && !item.uses.isNullOrEmpty() && !item.description.isNullOrEmpty() && !item.origin.isNullOrEmpty()) {
            itemView.productNameTxt.text = item.name
            itemView.productDescriptionTxt.text = item.description
            itemView.productOrigin.text = item.origin

            // Check type of additive
            var newIconImage: Drawable?
            newIconImage = ContextCompat.getDrawable(context, R.drawable.vegan_icon_ok)
            //typeIcon.setImageDrawable(newIconImage!!)
            itemView.itemProduct.setBackgroundColor(ContextCompat.getColor(context, R.color.colorVegan))

            // Check origin
            if (item.origin.trim().equals("No vegano", true)) {
                newIconImage = ContextCompat.getDrawable(context, R.drawable.skull_icon)

                //typeIcon.setImageDrawable(newIconImage!!)
                itemView.itemProduct.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDangerous))
            }else if (item.origin.trim().equals("Dudoso", true)){
                newIconImage = ContextCompat.getDrawable(context, R.drawable.question_icon)
                //typeIcon.setImageDrawable(newIconImage!!)
                itemView.itemProduct.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDoubtful))
            }

            itemView.setOnClickListener {

                var intent = Intent(context, ProductsDetailActivity::class.java)
                intent.putExtra("product", item)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent)
            }
        }
    }

}