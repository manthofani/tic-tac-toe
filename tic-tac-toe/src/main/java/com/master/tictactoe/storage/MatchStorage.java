package com.master.tictactoe.storage;

import com.master.tictactoe.model.Match;

import java.util.HashMap;
import java.util.Map;

public class MatchStorage {

    private static Map<String, Match> matches;
    private static MatchStorage instance;

    private MatchStorage(){
        matches = new HashMap<>();
    }

    public static synchronized MatchStorage getInstance(){
      if (instance == null) {
          instance = new MatchStorage();
      }
      return instance;
    }

    public Map<String, Match> getMatches(){
        return matches;
    }

    public void setMatches(Match match){
        matches.put(match.getMatchID(), match);
    }

}
