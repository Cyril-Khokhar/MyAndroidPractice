package com.example.userpermissiondemo.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.userpermissiondemo.R
import kotlinx.android.synthetic.main.row_image_adapter.view.*

class AdapterImage (var mContext : Context, var mlist : ArrayList<Bitmap>) : RecyclerView.Adapter<AdapterImage.MyViewHolder>(){

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bindView(image : Bitmap){
            itemView.imageView_show.setImageBitmap(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_image_adapter, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var image1 = mlist[position]
        holder.bindView(image1)
    }
}