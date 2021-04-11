package nl.hu.cisq1.lingo.trainer.domain;

import java.io.Serializable;

public enum GameStatus implements Serializable {
    ONGOING,
    WAITING_FOR_NEW_ROUND,
    GAME_OVER
}
