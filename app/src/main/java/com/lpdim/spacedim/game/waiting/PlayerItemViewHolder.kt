package com.lpdim.spacedim.game.waiting

import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.lpdim.spacedim.R
import com.lpdim.spacedim.databinding.PlayerItemViewBinding


class PlayerItemViewHolder(val viewDataBinding: PlayerItemViewBinding) : RecyclerView.ViewHolder(viewDataBinding.root){
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.player_item_view
    }

}