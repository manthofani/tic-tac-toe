package com.master.tictactoe.controller;

import com.master.tictactoe.controller.dto.ConnectRequest;
import com.master.tictactoe.exception.InvalidMatchException;
import com.master.tictactoe.exception.InvalidParamException;
import com.master.tictactoe.exception.NotFoundException;
import com.master.tictactoe.model.Game;
import com.master.tictactoe.model.Match;
import com.master.tictactoe.model.Player;
import com.master.tictactoe.service.MatchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @PostMapping("/start")
    public ResponseEntity<Match> start(@RequestBody Player player){
        log.info("Match request : {}", player);
        return ResponseEntity.ok(matchService.createMatch(player));
    }

    @PostMapping("/connect")
    public ResponseEntity<Match> connect(@RequestBody ConnectRequest request) throws InvalidParamException, InvalidMatchException {
        log.info("Connect Request : {}", request);
        return ResponseEntity.ok(matchService.connectPlayer(request.getPlayer(), request.getMatchID()));
    }

    @PostMapping("/random")
    public ResponseEntity<Match> random(@RequestBody Player player) throws NotFoundException{
        log.info("Random Request : {}", player);
        return ResponseEntity.ok(matchService.randomConnect(player));

    }

    @PostMapping("/play")
    public ResponseEntity<Match> playMatch(@RequestBody Game request) throws NotFoundException, InvalidMatchException {
        log.info("Playing : {}", request);
        Match match =   matchService.playMatch(request);
        simpMessagingTemplate.convertAndSend("/topic/in-game/" + match.getMatchID(), match);
        return ResponseEntity.ok(match);

    }
}
