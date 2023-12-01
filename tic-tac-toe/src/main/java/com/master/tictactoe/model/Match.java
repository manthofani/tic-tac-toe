package com.master.tictactoe.model;

import lombok.Data;

@Data
public class Match {

    private String matchID;
    private String player1;
    private String player2;
    private Integer Dimension;
    private MatchStatus status;
    private int [][] board;
    private TicToe winner;

    public Match() {
    }
}
