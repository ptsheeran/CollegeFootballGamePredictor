package com.example.ptshe.collegefootballgamepredictor;

import com.google.gson.JsonObject;

public class Drive {

    public Team offense;
    public Team defense;
    public String offenseConference;
    public String defenseConference;
    public Game gameId;
    public String driveId;
    public boolean scoring;
    public int startPeriod;
    public int startYardLine;
    public int endYardLine;
    public int plays;
    public int yards;

    public Drive(TeamList teamList, GameList gameList, JsonObject jsonObject){
        offense = teamList.get(jsonObject.get("offense").getAsString());
        defense = teamList.get(jsonObject.get("defense").getAsString());
        gameId = gameList.get(jsonObject.get("game_id").getAsInt());
        gameId.addDrive(this);
        if(!jsonObject.get("offense_conference").isJsonNull()){
            offenseConference = jsonObject.get("offense_conference").getAsString();
        }
        if(!jsonObject.get("defense_conference").isJsonNull()){
            defenseConference = jsonObject.get("defense_conference").getAsString();
        }
        driveId = jsonObject.getAsJsonObject().get("id").getAsString();
        scoring = jsonObject.get("scoring").getAsBoolean();
        startPeriod = jsonObject.get("start_period").getAsInt();
        startYardLine = jsonObject.get("start_yardline").getAsInt();
        endYardLine = jsonObject.get("end_yardline").getAsInt();
        plays = jsonObject.get("plays").getAsInt();
        yards = jsonObject.get("yards").getAsInt();
    }

    public boolean isOffenseTeam(Team team){
        if (this.offense.equals(team)){
            return true;
        }else{
            return false;
        }
    }


}