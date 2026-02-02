# Whack-a-Mole - Android Game App

**Whack-a-Mole** is an interactive Android game where players tap on bunnies as they pop up from holes to earn points. Built in **Java using Android Studio in 2024**, this project demonstrates Android UI development, animations, timers, and basic game logic.

---

## **Features**

### Gameplay
- **9 Random Holes**: Bunnies appear randomly in 9 designated holes.
- **Timed Challenge**: Game lasts **31 seconds**, counting down on a visible timer.
- **Score System**: Players earn points by tapping bunnies. Score is displayed as visual icons on screen.
- **Limited Pop-ups**: A maximum of 3 bunnies can appear at the same time for balanced gameplay.

### Visuals & Animations
- **Pop-up/Pop-down Animations**: Bunnies scale up when appearing and scale down when disappearing.
- **Splash Screen**: A 3-second splash screen is displayed at launch before the game starts, providing a polished first impression.
- **Dynamic Score Tally**: Each point adds a small bunny icon to the screen as a visual tally.

### Game Management
- **Restartable Game**: Players can restart at any time. Score and bunny positions are reset.
- **Thread-based Timer**: The game logic and timer run on a separate thread for smooth performance.

---

## **How It Works**

1. **SplashScreen Activity**
   - Displays a splash screen for 3 seconds.
   - Automatically launches `MainActivity` where the game starts.

2. **MainActivity**
   - Initializes 9 `ImageView` holes for bunnies.
   - Listens for taps on bunnies to increment score.
   - Uses a `Handler` and a separate thread to manage bunny appearances, disappearances, and countdown timer.
   - Animates bunnies popping up and down using `ScaleAnimation`.
   - Adds a dynamic score tally on-screen for every successful hit.

---

## **Technical Highlights**
- **Languages & Frameworks**: Java, Android Studio  
- **Android Components**: Activities, Views (`ImageView`, `TextView`, `Button`), `ConstraintLayout`  
- **Animations**: `ScaleAnimation` for pop-up/pop-down effects  
- **Game Logic**: Multi-threading with `Handler` for UI updates  
- **Splash Screen**: `CountDownTimer` for delay before game launch  

---

## **Project Status**
- Fully functional Whack-a-Mole game.
- Replayable with restart button.
- Timer, animations, and score tracking implemented.
- Can be extended with sound effects, difficulty levels, or high score persistence.

---

## **About This Project**
Created in **2024** as a learning project to explore Android UI, animations, multi-threading, and interactive game mechanics. Demonstrates experience with event handling, dynamic view manipulation, and game timers in mobile apps.

---

## **Skills Demonstrated**
- Android UI/UX design
- Animation and game mechanics
- Event handling and onClick logic
- Multi-threading for timer and game logic
- Activity lifecycle and splash screens
