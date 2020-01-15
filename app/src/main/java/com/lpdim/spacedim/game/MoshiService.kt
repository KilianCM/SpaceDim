package com.lpdim.spacedim.game

import com.lpdim.spacedim.game.model.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Moshi instance with settings to deserialize sealed classes
 */
object MoshiService {
    val moshi: Moshi = Moshi.Builder()
        .add(
            // Declare each subtype of sealed class Event to deserialize correctly
            PolymorphicJsonAdapterFactory.of(Event::class.java, "type")
                .withSubtype(Event.WaitingForPlayer::class.java, EventType.WAITING_FOR_PLAYER.name)
                .withSubtype(Event.Ready::class.java, EventType.READY.name)
                .withSubtype(Event.NextAction::class.java, EventType.NEXT_ACTION.name)
                .withSubtype(Event.Error::class.java, EventType.ERROR.name)
                .withSubtype(Event.GameOver::class.java, EventType.GAME_OVER.name)
                .withSubtype(Event.GameStarted::class.java, EventType.GAME_STARTED.name)
                .withSubtype(Event.NextLevel::class.java, EventType.NEXT_LEVEL.name)
                .withSubtype(Event.PlayerAction::class.java, EventType.PLAYER_ACTION.name)
        )
        .add(
            // Declare each subtype of sealed class UIElement to deserialize correctly
            PolymorphicJsonAdapterFactory.of(UIElement::class.java, "type")
                .withSubtype(UIElement.Button::class.java, UIType.BUTTON.name)
                .withSubtype(UIElement.Shake::class.java, UIType.SHAKE.name)
                .withSubtype(UIElement.Switch::class.java, UIType.SWITCH.name)
        )
        .add(KotlinJsonAdapterFactory())
        .build()

    val eventAdapter: JsonAdapter<Event> = moshi.adapter(Event::class.java)
    val userAdapter: JsonAdapter<User> = moshi.adapter(User::class.java)
    val userPostAdapter: JsonAdapter<UserPost> = moshi.adapter(UserPost::class.java)

}