package com.sdk.ui.adapter

import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sdk.R
import com.sdk.data.Podcast
import com.sdk.databinding.RvPodcastHorizontalBinding
import com.sdk.ui.interfaces.PodcastClickListener
import com.sdk.utils.extensions.layoutInflater
import com.sdk.utils.extensions.onClick


class PodcastHorizontalRecycleAdapter(
        private val list: ArrayList<Podcast>,
        private val listener: PodcastClickListener? = null
) : RecyclerView.Adapter<PodcastHorizontalRecycleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        val binding = RvPodcastHorizontalBinding.inflate(parent.context.layoutInflater, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val context = root.context
            with(list[position]) {
                data = this
                this.cover_image?.text_hex_code?.let {
                    tvPodcastTitle.setTextColor(Color.parseColor(it))
                    tvSubText.setTextColor(Color.parseColor(it))
                }

                tvSubText.text = if (this.connectedminds_feed == 1){
                    context.getString(R.string.by_connectedminds)
                }else{
                    "By ".plus((this.expert.first_name + " " + this.expert.last_name)).also {
                        tvSubText.text = it
                    }
                }

                holder.itemView.onClick {
                    listener?.onPodcastClicked(position, this@with)
                }
            }
        }


    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: RvPodcastHorizontalBinding) :
            RecyclerView.ViewHolder(binding.root)
}