package com.master.tictactoe.model;

import lombok.Data;

@Data
public class Game {

    private TicToe type;
    private Integer coordinateX;
    private Integer coordinateY;
    private String matchID;
}
