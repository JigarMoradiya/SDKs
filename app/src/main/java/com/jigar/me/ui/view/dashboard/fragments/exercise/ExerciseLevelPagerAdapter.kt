package com.jigar.me.ui.view.dashboard.fragments.exercise

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.jigar.me.R
import com.jigar.me.data.local.data.ExerciseLevel
import com.jigar.me.data.local.data.ExerciseLevelDetail
import com.jigar.me.databinding.RawExerciseLevelBinding
import com.jigar.me.databinding.RawExerciseLevelPagerBinding
import com.jigar.me.utils.extensions.layoutInflater
import com.jigar.me.utils.extensions.onClick

class ExerciseLevelPagerAdapter(var listData: ArrayList<ExerciseLevel>, private val mListener: OnItemClickListener) : PagerAdapter() {
    var selectedParentPosition: Int = 0
    var selectedChildPosition : Int = 0
    interface OnItemClickListener {
        fun onExerciseStartClick(data: ExerciseLevel,child: ExerciseLevelDetail?)
    }
    interface OnChildItemClickListener {
        fun onExerciseLevelClick(parentPosition : Int,childPosition : Int, child: ExerciseLevelDetail)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = RawExerciseLevelPagerBinding.inflate(container.context.layoutInflater,container,false)

        with(listData[position]){
            binding.data = this
            val childData = this.list[0]
            when (this.id) {
                "1" -> {
                    binding.desc = "${childData.digits} Digits ${childData.queLines} Lines X ${childData.totalQue} Questions in ${childData.totalTime} minute"
                }
                "2" -> {
                    binding.desc = "The answer is ${childData.digits} Digits X ${childData.totalQue} Questions in ${childData.totalTime} minute"
                }
                "3" -> {
                    binding.desc = "The dividend is MAX ${childData.digits} Digits X ${childData.totalQue} Questions in ${childData.totalTime} minute"
                }
            }

            val adapter = ExerciseLevelAdapter(this.list,position, object : OnChildItemClickListener{
                override fun onExerciseLevelClick(parentPosition: Int,childPosition : Int, child: ExerciseLevelDetail) {
                    selectedParentPosition = parentPosition
                    selectedChildPosition = childPosition
                    when (listData[parentPosition].id) {
                        "1" -> {
                            binding.desc = "${child.digits} Digits ${child.queLines} Lines X ${child.totalQue} Questions in ${child.totalTime} minute"
                        }
                        "2" -> {
                            binding.desc = "The answer is ${child.digits} Digits X ${child.totalQue} Questions in ${child.totalTime} minute"
                        }
                        "3" -> {
                            binding.desc = "The dividend is MAX ${child.digits} Digits X ${child.totalQue} Questions in ${child.totalTime} minute"
                        }
                    }
                }
            })
            binding.recyclerviewExercise.adapter = adapter
            binding.btnYes.onClick {
                mListener.onExerciseStartClick(listData[selectedParentPosition],listData[selectedParentPosition].list[selectedChildPosition])
            }
        }
        container.addView(binding.root)
        return binding.root
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as ConstraintLayout
    }

    override fun getCount(): Int {
        return listData.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }


    class ExerciseLevelAdapter(private var listData: ArrayList<ExerciseLevelDetail>,val parentPosition: Int, private val mListener: OnChildItemClickListener) :
        RecyclerView.Adapter<ExerciseLevelAdapter.FormViewHolder>() {
        var selectedPosition = 0
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
            val binding = RawExerciseLevelBinding.inflate(parent.context.layoutInflater,parent,false)
            return FormViewHolder(binding)
        }

        override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
            val data: ExerciseLevelDetail = listData[position]
            val context = holder.binding.root.context
            with(holder.binding){
                txtSubTitle.text = (position + 1).toString()
                if (selectedPosition == position){
                    txtSubTitle.setTextColor(ContextCompat.getColor(context, R.color.white))
                    txtSubTitle.setBackgroundResource(R.drawable.circle_primary)
                }else {
                    txtSubTitle.setBackgroundResource(R.drawable.circle_primary_border)
                    txtSubTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                }
            }

            holder.binding.root.setOnClickListener {
                val previousPos = selectedPosition
                selectedPosition = position
                notifyItemChanged(selectedPosition)
                notifyItemChanged(previousPos)
                this.mListener.onExerciseLevelClick(parentPosition, position, data)
            }
        }

        class FormViewHolder(itemBinding: RawExerciseLevelBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
            var binding: RawExerciseLevelBinding = itemBinding
        }

        override fun getItemCount(): Int {
            return listData.size
        }

    }
}