package com.master.tictactoe.service;

import com.master.tictactoe.exception.InvalidMatchException;
import com.master.tictactoe.exception.InvalidParamException;
import com.master.tictactoe.exception.NotFoundException;
import com.master.tictactoe.model.*;
import com.master.tictactoe.storage.MatchStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class MatchService {

    public Match createMatch(Player player){
        Match match = new Match();
        match.setBoard(new int[player.getDimension()][player.getDimension()]);
        match.setMatchID(UUID.randomUUID().toString());
        match.setPlayer1(player.getLogin());
        match.setDimension(player.getDimension());
        match.setStatus(MatchStatus.NEW);
        MatchStorage.getInstance().setMatches(match);
        return match;
    }

    public Match connectPlayer(Player player2, String matchID) throws InvalidParamException, InvalidMatchException {
        if(!MatchStorage.getInstance().getMatches().containsKey(matchID)){
            throw new InvalidParamException("Match not found!");
        }

        Match match = MatchStorage.getInstance().getMatches().get(matchID);

        if(match.getPlayer2() != null){
            throw new InvalidMatchException("Match is in Progress, Can't Join!");
        }

        match.setPlayer2(player2.getLogin());
        match.setStatus(MatchStatus.IN_GAME);
        MatchStorage.getInstance().setMatches(match);
        return match;
    }

    public Match randomConnect(Player player2) throws NotFoundException{
        Match match = MatchStorage.getInstance().getMatches().values().stream().filter(it->it.getStatus().equals(MatchStatus.NEW)).
                        findFirst().orElseThrow(()-> new NotFoundException("All Match is in Progress!"));
        match.setPlayer2(player2.getLogin());
        match.setStatus(MatchStatus.IN_GAME);
        MatchStorage.getInstance().setMatches(match);
        return match;
    }

    public Match playMatch(Game game) throws NotFoundException, InvalidMatchException{
        if(!MatchStorage.getInstance().getMatches().containsKey(game.getMatchID())){
            throw new NotFoundException("Match Not Found!");
        }
        Match match = MatchStorage.getInstance().getMatches().get(game.getMatchID());
        if(match.getStatus().equals(MatchStatus.FINISHED)){
            throw  new InvalidMatchException("Game is already Finished");
        }

        int [][] board = match.getBoard();
        board[game.getCoordinateX()][game.getCoordinateY()] = game.getType().getValue();

        boolean xWinner = checkWinner(match.getBoard(), TicToe.X, match.getDimension());
        boolean oWinner = checkWinner(match.getBoard(), TicToe.O, match.getDimension());

        if(xWinner){
            match.setWinner(TicToe.X);
        } else if (oWinner){
            match.setWinner(TicToe.O);
        }

        MatchStorage.getInstance().setMatches(match);
        return match;
    }

    private Boolean checkWinner(int[][] board, TicToe tictoe, Integer dimension) {

        //Checking Vertical
        for(int i=0; i<dimension; i++){
            int score = 0;
            for(int j=0; j<dimension; j++){
                if (board[j][i] == tictoe.getValue()){
                    score++;
                    if (score == dimension){
                        return true;
                    }
                } else {
                    break;
                }
            }
        }
        //Checking Horizontal
        for(int i=0; i<dimension; i++){
            int score = 0;
            for(int j=0; j<dimension; j++){
                if (board[i][j] == tictoe.getValue()){
                    score++;
                    if (score == dimension){
                        return true;
                    }
                } else {
                    break;
                }
            }
        }

        //Checking Start Diagonally
        for(int i=0; i<1; i++){
            int score = 0;
            for(int j=0; j<dimension; j++){
                if (board[j][j] == tictoe.getValue()){
                    score++;
                    if (score == dimension){
                        return true;
                    }
                } else {
                    break;
                }
            }
        }

        //Checking END Diagonally
        for(int i=0; i<1; i++){
            int score = 0;
            for(int j=0; j < dimension ; j++){
                int x = j;
                int y = dimension - (1+j);

                if (board[x][y] == tictoe.getValue()){
                    score++;
                    if (score == dimension){
                        return true;
                    }
                } else {
                    break;
                }
            }
        }

        return false;
    }
}
