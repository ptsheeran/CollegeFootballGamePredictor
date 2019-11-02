package com.example.ptshe.collegefootballgamepredictor;

import java.util.ArrayList;

public class GameList {

    public ArrayList<Game> allGames;

    public GameList(){
        allGames = new ArrayList<Game>();
    }

    public Game get(int passedGameId){
        for (int i = 0; i < this.allGames.size(); i++){
            if(this.allGames.get(i).equals(passedGameId)){
                return this.allGames.get(i);
            }
        }
        return null;
    }

}