package com.lpdim.spacedim.game.model

data class Action(
    val sentence: String,
    val uiElement: UIElement,
    val time: Long = 8000
)