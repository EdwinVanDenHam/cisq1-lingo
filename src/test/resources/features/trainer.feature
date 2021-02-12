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



