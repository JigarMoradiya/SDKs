package com.jigar.me.ui.view.dashboard.fragments.settings

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jigar.me.data.local.data.AbacusType
import com.jigar.me.databinding.RowDefaultThemeBinding
import com.jigar.me.utils.extensions.*

class AbacusThemePoligonAdapter(
    private var questions: List<AbacusType>,private val mListener: OnItemClickListener, private var currentPos : Int = 0
) : RecyclerView.Adapter<AbacusThemePoligonAdapter.FormViewHolder>() {
    interface OnItemClickListener {
        fun onThemePoligonItemClick(data: AbacusType)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): FormViewHolder {
        val binding = RowDefaultThemeBinding.inflate(parent.context.layoutInflater,parent,false)
        return FormViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val data = questions[position]
        with(holder.binding){
            imgAbacus.setImageResource(data.beadImage)
            if (currentPos == position){
                imgTick.show()
            }else{
                imgTick.invisible()
            }
            root.onClick {
                val previousPos = currentPos
                currentPos = position
                notifyItemChanged(previousPos)
                imgTick.show()
                mListener.onThemePoligonItemClick(data)
            }
        }

    }

    class FormViewHolder(
        itemBinding: RowDefaultThemeBinding
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var binding: RowDefaultThemeBinding = itemBinding
    }

    override fun getItemCount(): Int {
        return questions.size
    }
}