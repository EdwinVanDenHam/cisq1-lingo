  Feature: Lingo Trainer
    As a player
    I want to guess 5, 6 and 7 letter words
    In order to prepare for Lingo

  Feature: Creating a game
    As a User,
    I want to create a game
    In order to start guessing 5, 6 or 7 letter words

  Feature: Guessing a 5, 6 or 7 letter word
    As a User,
    I want to guess a 5, 6 or 7 letter word
    In order to make an attempt to guess the correct word

  Feature: Feedback
    As a player
    I want to get feedback from my attempted guess
    In order to see what letters are correct/ present/ absent

  Feature: Requesting the state of the game
    As a User,
    I want to request the state of a game
    In order to see the progress of the game

  Feature: Resume game
    As a player
    I want to resume an old game
    In order to finish the old game without having lost the progress

  Feature: Keep score
    As a player
    I want the score of the game
    In order to see if I get better

  Scenario Start new game
    When I start a new game
    Then I should see the first letter of a 5 letter word
    And My score is 0


  Scenario Outline: Start a new round
    Given I am playing a game
    And the round was won
    And the last word had "<previous length>" letters
    When I start a new round
    Then the word to guess has "<next length>" letters

    Examples:
      | previous length | next length |
      | 5               | 6           |
      | 6               | 7           |
      | 7               | 5           |

  # Failure path
    Given I am playing a game
    And the round was lost
    Then I cannot start a new round


  Scenario Outline: Guessing a word
    Given I am playing a game
    And A new round has started
    And The word to guess is "<word>"
    When I attempt to guess the word with the following guess: <guess>
    Then I will get the following feedback: <feedback>

    Examples:
      | word  | guess  | feedback                                             |
      | BAARD | BERGEN | INVALID, INVALID, INVALID, INVALID, INVALID, INVALID |
      | BAARD | BONJE  | CORRECT, ABSENT, ABSENT, ABSENT, ABSENT              |
      | BAARD | BARST  | CORRECT, CORRECT, PRESENT, ABSENT, ABSENT            |
      | BAARD | DRAAD  | ABSENT, PRESENT, CORRECT, PRESENT, CORRECT           |
      | BAARD | BAARD  | CORRECT, CORRECT, CORRECT, CORRECT, CORRECT          |

  # Failure path
    Given I am playing a game
    And I did not guess the word within 5 turns
    Then I cannot guess a word







