package com.example.ptshe.collegefootballgamepredictor;

import com.google.gson.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Game {

    public int gameId;
    public int season;
    public int week;
    public String seasonType;
    public String gameDate;
    public boolean neutralSite;
    public boolean conferenceGame;
    public int attendance;
    public int venueId;
    public String venueName;
    public Team homeTeam;
    public String homeConference;
    public int homePoints;
    public Team awayTeam;
    public String awayConference;
    public int awayPoints;
    public boolean gamePlayed;
    public ArrayList<Drive> listOfDrives;

    public Game(TeamList listOfTeams, JsonObject game) throws ParseException {

        SimpleDateFormat formatter=new SimpleDateFormat("E, d MMM yyyy HH:mm:ss Z");
        gameId = game.getAsJsonObject().get("id").getAsInt();
        gamePlayed = true;
        season = game.getAsJsonObject().get("season").getAsInt();
        week = game.getAsJsonObject().get("week").getAsInt();
        seasonType = game.getAsJsonObject().get("season_type").getAsString();
        gameDate = game.getAsJsonObject().get("start_date").getAsString();
        neutralSite = game.getAsJsonObject().get("neutral_site").getAsBoolean();
        conferenceGame = game.getAsJsonObject().get("conference_game").getAsBoolean();
        //attendance = game.getAsJsonObject().get("attendance").getAsInt().;
        venueId = game.getAsJsonObject().get("venue_id").getAsInt();
        venueName = game.getAsJsonObject().get("venue").getAsString();
        homeTeam = listOfTeams.get(game.getAsJsonObject().get("home_team").getAsString());
        homeTeam.addGameToGamesList(this);
        awayTeam = listOfTeams.get(game.getAsJsonObject().get("away_team").getAsString());
        awayTeam.addGameToGamesList(this);
        homeConference = game.getAsJsonObject().get("home_conference").getAsString();
        if (game.getAsJsonObject().get("home_points").isJsonNull()){
            homePoints = 0;
            gamePlayed = false;
        }else{
            homePoints = game.getAsJsonObject().get("home_points").getAsInt();
        }
        //TODO: Fix NPE when using data from File
        if (game.getAsJsonObject().get("away_conference").isJsonNull()){
            awayConference = "FCS";
        }else{
            awayConference = game.getAsJsonObject().get("away_conference").getAsString();
        }
        if (game.getAsJsonObject().get("away_points").isJsonNull()){
            awayPoints = 0;
            gamePlayed = false;
        }else{
            awayPoints = game.getAsJsonObject().get("away_points").getAsInt();
        }
        listOfDrives = new ArrayList<Drive>();

    }

    public int getPoints(Team team){
        if (homeTeam==team) {
            return homePoints;
        }else if (awayTeam==team){
            return awayPoints;
        }else {
            return 0;
        }
    }

    public boolean equals(int passedGameId){
        if (this.gameId == passedGameId){
            return true;
        }
        return false;
    }

    public void addDrive(Drive drive){
        listOfDrives.add(drive);
    }

    public boolean isHomeTeam(Team team){
        if (this.homeTeam.equals(team)){
            return true;
        }else{
            return false;
        }
    }
}
