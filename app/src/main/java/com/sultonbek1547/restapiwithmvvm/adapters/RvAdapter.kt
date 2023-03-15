package com.sultonbek1547.restapiwithmvvm.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sultonbek1547.restapiwithmvvm.databinding.RvItemBinding
import com.sultonbek1547.restapiwithmvvm.model.MyToDo

class RvAdapter(val rvClick: RvClick, var list: ArrayList<MyToDo> = ArrayList<MyToDo>()) :
    RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(private val itemRvBinding: RvItemBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(myToDo: MyToDo, position: Int) {
            itemRvBinding.apply {
                tvText.text = myToDo.sarlavha
                tvDate.text = myToDo.oxirgi_muddat
//                tvPriority.text = myToDo.matn.substring(5)


                itemRvBinding.btnMore.setOnClickListener {
                    rvClick.menuClick(itemRvBinding.btnMore, myToDo,position)
                }

                itemRvBinding.btnCheck.setOnCheckedChangeListener { p0, p1 ->
                    if (p1) {
                        itemRvBinding.tvText.paintFlags =
                            itemRvBinding.tvText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    } else {
                        itemRvBinding.tvText.paintFlags =
                            itemRvBinding.tvText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

                    }
                }


            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) =
        holder.onBind(list[position], position)


    override fun getItemCount(): Int = list.size


    interface RvClick {
        fun menuClick(imageView: ImageView, myToDo: MyToDo,position: Int)
    }

}