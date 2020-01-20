package com.lpdim.spacedim.game.waiting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lpdim.spacedim.databinding.PlayerItemViewBinding
import com.lpdim.spacedim.game.model.User


class PlayerAdapter: RecyclerView.Adapter<PlayerItemViewHolder>() {

    var players: List<User> = emptyList()
        set(value) {
            field = value
            // For an extra challenge, update this to use the paging library.

            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    override fun getItemCount() = players.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerItemViewHolder {
        val withDataBinding: PlayerItemViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            PlayerItemViewHolder.LAYOUT,
            parent,
            false)
        return PlayerItemViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: PlayerItemViewHolder, position: Int) {
        holder.viewDataBinding.also {
            
        }
    }

}