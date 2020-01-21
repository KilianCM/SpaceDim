package com.lpdim.spacedim.score

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lpdim.spacedim.databinding.PlayerItemScoreItemBinding
import com.lpdim.spacedim.game.model.User
import timber.log.Timber


class ScoreAdapter: RecyclerView.Adapter<ScoreItemViewHolder>() {

    var players: List<User> = emptyList()
        set(value) {
            field = value
            Timber.d(value.toString())
            notifyDataSetChanged()
        }

    override fun getItemCount() = players.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreItemViewHolder {
        val withDataBinding: PlayerItemScoreItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ScoreItemViewHolder.LAYOUT,
            parent,
            false)
        return ScoreItemViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ScoreItemViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.player = players[position]
        }
    }

}