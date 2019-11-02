package com.example.ptshe.collegefootballgamepredictor;

//import android.telecom.Conference;

import java.util.ArrayList;

public class Division {

    private ArrayList<Team> listOfTeams;
    private String name;
    private ArrayList<Conference> allDivisionConferences;

    public Division(String passedName){
        this.setName(passedName);
        this.listOfTeams = new ArrayList<Team>();
        this.setAllDivisionConferences(new ArrayList<Conference>());
    }

    public Division(String passedName, ArrayList<Team> teamArrayList){
        this.setListOfTeams(teamArrayList);
        this.setName(passedName);
    }

    public ArrayList<Team> getListOfTeams() {
        return listOfTeams;
    }

    public void setListOfTeams(ArrayList<Team> listOfTeams) {
        this.listOfTeams = listOfTeams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean contains(Team team){
        for (int i = 0; i < this.listOfTeams.size(); i++){
            if(this.listOfTeams.get(i).equals(team)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Conference> getAllDivisionConferences() {
        return allDivisionConferences;
    }

    public void setAllDivisionConferences(ArrayList<Conference> allDivisionConferences) {
        this.allDivisionConferences = allDivisionConferences;
    }

    public Conference getConference(String conferenceName){
        for (int i = 0; i < getAllDivisionConferences().size(); i++){
            if (getAllDivisionConferences().get(i).getShortName().equalsIgnoreCase(conferenceName)){
                return getAllDivisionConferences().get(i);
            }
        }
        return null;
    }
}