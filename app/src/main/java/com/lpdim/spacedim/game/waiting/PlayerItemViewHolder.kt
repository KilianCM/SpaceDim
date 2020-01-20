package com.lpdim.spacedim.game.waiting

import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.lpdim.spacedim.R
import com.lpdim.spacedim.databinding.PlayerItemViewBinding
import com.lpdim.spacedim.databinding.PlayerItemViewBindingImpl


class PlayerItemViewHolder(val viewDataBinding: PlayerItemViewBindingImpl) : RecyclerView.ViewHolder(viewDataBinding.root){
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.player_item_view
    }

}