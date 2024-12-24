# Texas Hold 'Em Simulator

## Overview

The Texas Hold 'Em Simulator is a comprehensive project developed as part of the Object-Oriented Analysis and Design (OOAD) course. It provides a simulated environment for the popular poker game, implementing advanced programming principles and design patterns.

### Contributors
- Bowman Russell
- Ryan Oros
- Clement Canel

---

## Features and Functionality

### Core Components
- **Card, Dealer, Deck, Game, GameConfigurator, Player, and Table Classes**: These classes form the core structure of the simulator.
- **Event Handling**: The `EventBus` and `EventType` classes manage game events using the Observer pattern.
- **Observer and Observable Interfaces**: Designed for event-driven programming.
- **Audible Notifications**: The `AudibleObserver` class provides sound-based notifications for game events.
- **Game Flow**: Features for orchestrating game flow, including dealing cards, betting rounds, and evaluating hands.
- **Player Actions**: Methods for player interactions such as folding, betting, and evaluating hand strength.

### Design Patterns Used
- Observer Pattern
- Singleton Pattern
- Factory Pattern
- Builder Pattern

---

## Implementation Details

### BDD Scenarios
The simulator was developed using Behavior-Driven Development (BDD). Key scenarios include:
1. **Dealing Players**: Ensures correct distribution of cards.
2. **Evaluating Hands**: Determines winners based on poker hand rankings.
3. **Betting Rounds**: Simulates player bets based on hand strength.
4. **Game Completion**: Verifies game termination conditions.

---

## Testing
Comprehensive testing was conducted to ensure robust functionality. Test reports and results are available in the `Coverage` directory.

---

## How to Run
1. Clone the repository.
2. Open the project in your preferred IDE.
3. Run the `Game` class to start the simulator.

