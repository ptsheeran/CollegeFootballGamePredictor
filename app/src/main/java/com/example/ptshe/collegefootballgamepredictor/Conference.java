package com.example.ptshe.collegefootballgamepredictor;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Conference {

    private int id;
    private String shortName;
    private String longName;
    private String abbreviation;
    private ArrayList<Team> allTeams;
    private Division division;

    public Conference(Division division, JsonObject jsonObject){
        this.setId(jsonObject.get("id").getAsInt());
        this.setShortName(jsonObject.get("name").getAsString());
        this.setLongName(jsonObject.get("short_name").getAsString());
        this.setAbbreviation(jsonObject.get("abbreviation").getAsString());
        this.setDivision(division);
        this.setAllTeams(new ArrayList<Team>());
    }

    public boolean equals(Conference conference){
        if (this.getShortName() == conference.getShortName()){
            return true;
        }else{
            return false;
        }
    }

    public boolean equals(String conference){
        if (this.getShortName().equals(conference)){
            return true;
        }else{
            return false;
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public ArrayList<Team> getAllTeams() {
        return allTeams;
    }

    public void setAllTeams(ArrayList<Team> allTeams) {
        this.allTeams = allTeams;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public String toString(){
        return this.getAbbreviation();
    }
}