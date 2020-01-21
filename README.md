## SpaceDim 🚀

SpaceDim is a school project based on the real game *SpaceTeam*.

### Concept

A server push event to the app using websocket. Each event represent a state of the game.
A game of SpaceDim is composed by multiple event :

- `WAITING_FOR_PLAYER`, users are in a waiting room until there are unless 2 ready users.
- `GAME_STARTED`, beginning of the game with UI elements list to generate (buttons, switch, shake).
- `NEXT_ACTION` represent the action that the user must accomplished until the end of countdown clicking the correct UI element.
- `NEXT_LEVEL` a new UI elements to generate
- `GAME_OVER` end of the game (win or loose) with score and reached level

### Application

3 activities
- `HomeActivity` : the home page with sign in and sign up
- `GameActivity` splitted into 3 fragments
    - `WaitingRoomFragment` the waiting room with user list and button to be ready
    - `PlayFragment` where UI elements are generated
    - `FinishFragment` with score and level reached at the end of game
- `ScoreActivity` : top 5 users

Websocket connexion is handled in `WebSocketManager`.
`GameActivity` uses a ViewModel which hold the LiveData (event)

#### Tools

- **OkHttp** for websocket and HTTP requests
- **Moshi** as content negociator
- **Sensey** lib for ShakeListener
- **Timber** for easy logging
