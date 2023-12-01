package com.master.tictactoe.controller.dto;

import com.master.tictactoe.model.Player;
import lombok.Data;

@Data
public class ConnectRequest {
    private Player player;
    private String matchID;

}
