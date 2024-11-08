# WOF and Mastermind with Inheritance Project

This project refactors the Wheel of Fortune game using inheritance, polymorphism, interfaces, and abstract classes to create a robust game framework that supports multiple game types, including Wheel of Fortune and Mastermind. The project is divided into two parts, each with distinct objectives, and provides a foundation for designing games that share similar mechanics.

## Learning Objectives

- Develop experience designing software using inheritance, polymorphism, interfaces, and abstract classes.
- Structure game logic to be reusable across different games by encapsulating shared behaviors in a superclass.
- Refactor existing code to improve design, readability, and scalability.

## Project Overview

### Part 1: Refactoring Wheel of Fortune

In Part 1, you will refactor an existing Wheel of Fortune game to use inheritance and polymorphism. This refactor involves creating various classes and interfaces to handle game recording, scoring, and player interaction.

#### Key Components

- **GameRecord**: Represents a single play of a game, tracking the score and player ID. Implements `Comparable` to allow easy sorting of game records.
- **AllGamesRecord**: Manages a list of all game records and provides methods for calculating averages, finding top scores, and filtering scores by player ID.

#### Classes and Interfaces

- **Game (Abstract Class)**: Encapsulates code for looping through a set of games and recording results. Defines:
  - `playAll()` - Plays a set of games and returns an `AllGamesRecord` object for the session.
  - Abstract methods `play()` and `playNext()` - Play a single game and check if the player wants to continue.

- **WheelOfFortune (Abstract Class)**: Extends `Game` and contains shared code specific to Wheel of Fortune gameplay, including:
  - `readPhrases()`, `randomPhrase()`, `getHiddenPhrase()`, and `processGuess()`.
  - Abstract method `getGuess(String previousGuesses)` - Returns the next guessed letter, implemented differently by subclasses.

- **WheelOfFortuneUserGame (Concrete Class)**: Implements a user-based Wheel of Fortune game, allowing the player to make guesses and decide when to end the game.

- **WheelOfFortuneAIGame (Concrete Class)**: Plays Wheel of Fortune using AI players, running a game for each AI and each phrase.

- **WheelOfFortunePlayer (Interface)**: Defines methods for all AI players:
  - `nextGuess()` - Get the next guess.
  - `playerId()` - Return the player's ID.
  - `reset()` - Reset the player to start a new game.

- **WheelOfFortunePlayer Implementations**: At least three concrete classes implementing different AI strategies.

#### Main Programs

Each game class (`WheelOfFortuneUserGame`, `WheelOfFortuneAIGame`) has a main program that:
- Calls `playAll()` to run a set of games.
- Allows users or AI players to play through phrases until they run out or choose to stop.
- Demonstrates the use of `GamesRecord` to display results.

### Part 2: Integrating Mastermind with Wheel of Fortune

In Part 2, you’ll design a Mastermind game and refactor Wheel of Fortune to reuse code by creating a common superclass, `GuessingGame`. This structure captures shared logic between Mastermind and Wheel of Fortune, streamlining the code and making it easier to maintain.

#### Key Objectives for Part 2

- **Mastermind Game**: Develop a command-line Mastermind game for user interaction.
- **GuessingGame Superclass**: Encapsulate shared behaviors (e.g., guess processing, score tracking) to allow code reuse in both Mastermind and Wheel of Fortune.
  
## Usage and Execution

Each game provides a main program to initialize the game and run multiple sessions. The main program demonstrates how to use `GamesRecord` methods to display high scores, averages, and player-specific results.

### Running Wheel of Fortune

1. Initialize the `WOFUserGame` or `WOFAIGame` class and call `playAll()` to run the game.
2. Review results by calling specific `GamesRecord` methods to view scores and averages.

### Running Mastermind

1. Initialize the `Mastermind` class and call `playAll()` to play a set of games.
2. Review scores and results for the Mastermind game sessions using `GamesRecord`.

## Methods in AllGamesRecord

- **add(GameRecord)** - Adds a new game record.
- **average()** - Calculates the average score across all games.
- **average(playerId)** - Calculates the average score for a specific player.
- **highGameList(n)** - Returns the top `n` scores.
- **highGameList(playerId, n)** - Returns the top `n` scores for a specific player.

## Design and Communication

This project emphasizes not only functional coding but also clear, structured design as specified. Communicate with your client (Professor Wolber) for clarification on requirements to ensure the final code meets design and functionality expectations.

## Resources

- [Project Rubric](https://docs.google.com/spreadsheets/d/1LXqTcoQ2DWKJW9METt6XJ4bg3ZLl3-gbDq6sT7MnoAw/edit#gid=0) - Refer to the project rubric for grading criteria and detailed requirements.
  
