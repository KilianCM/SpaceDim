package com.lpdim.spacedim.score

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.lpdim.spacedim.R
import com.lpdim.spacedim.databinding.PlayerItemScoreItemBinding


class ScoreItemViewHolder(val viewDataBinding: PlayerItemScoreItemBinding) : RecyclerView.ViewHolder(viewDataBinding.root){
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.player_item_score_item
    }

}