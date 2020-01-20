package com.lpdim.spacedim.game.waiting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lpdim.spacedim.databinding.PlayerItemViewBinding
import com.lpdim.spacedim.game.model.User
import timber.log.Timber


class PlayerAdapter: RecyclerView.Adapter<PlayerItemViewHolder>() {

    var players: List<User> = emptyList()
        set(value) {
            field = value
            Timber.d(value.toString())
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
            it.player = players[position]
        }
    }

}