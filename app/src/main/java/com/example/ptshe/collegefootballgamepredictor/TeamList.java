package com.example.ptshe.collegefootballgamepredictor;

import java.util.ArrayList;
import java.util.Collections;

public class TeamList {

    public ArrayList<Team> allTeams;
    public ArrayList<Division> allDivisions;

    public TeamList(){

        allTeams = new ArrayList<Team>();
        allDivisions = new ArrayList<Division>();

    }

    public boolean contains(Team team){

        for (int i = 0; i < this.allTeams.size(); i++){
            if (team.equals(this.allTeams.get(i))){
                return true;
            }
        }
        return false;
    }

    public boolean contains(String name){

        for (int i = 0; i < this.allTeams.size(); i++){
            if (this.allTeams.get(i).equals(name)){
                return true;
            }
        }
        return false;
    }

    public Team get(String teamName){
        for (int i = 0; i < this.allTeams.size(); i++){
            if(this.allTeams.get(i).getTeamName().equals(teamName)){
                return this.allTeams.get(i);
            }
        }
        return null;
    }

    public void checkDivisions(){
        for (int i = 0; i < this.allTeams.size(); i++){
            if(!this.allDivisions.get(0).contains(this.allTeams.get(i)) &&
                    this.allTeams.get(i).getDivision().equals(this.allDivisions.get(0))){
                this.allDivisions.get(0).getListOfTeams().add(this.allTeams.get(i));
            }else if(!this.allDivisions.get(1).contains(this.allTeams.get(i)) &&
                    this.allTeams.get(i).getDivision().equals(this.allDivisions.get(1))){
                this.allDivisions.get(1).getListOfTeams().add(this.allTeams.get(i));
            }
        }
    }

    public void generateScoringMarginScore(Division division){
        double min = 0;
        for (int i = 0; i < division.getListOfTeams().size(); i++){
            if (division.getListOfTeams().get(i).getScoringMargin()<min){
                min = division.getListOfTeams().get(i).getScoringMargin();
            }
        }
        double max = 0;
        for (int i = 0; i < division.getListOfTeams().size(); i++){
            if (division.getListOfTeams().get(i).getScoringMargin()>max){
                max = division.getListOfTeams().get(i).getScoringMargin();
            }
        }
        double minDistance = Math.abs(min);
        for (int i = 0; i < division.getListOfTeams().size(); i++){
            division.getListOfTeams().get(i).setScoringMarginScore(((division.getListOfTeams().get(i).getScoringMargin()+minDistance)/(max+minDistance)));
        };
    }

    public void generateDriveMarginScore(Division division){
        for (int i = 0; i < division.getListOfTeams().size(); i++){
            division.getListOfTeams().get(i).calculateOffenseDriveScore();
            division.getListOfTeams().get(i).calculateDefenseDriveScore();
            division.getListOfTeams().get(i).calculateDriveMargin();
        }

        double min = 0;
        for (int i = 0; i < division.getListOfTeams().size(); i++){
            if (division.getListOfTeams().get(i).getDriveMargin()<min){
                min = division.getListOfTeams().get(i).getDriveMargin();
            }
        }
        double max = 0;
        for (int i = 0; i < division.getListOfTeams().size(); i++){
            if (division.getListOfTeams().get(i).getDriveMargin()>max){
                max = division.getListOfTeams().get(i).getDriveMargin();
            }
        }
        double minDistance = Math.abs(min);
        for (int i = 0; i < division.getListOfTeams().size(); i++){
            division.getListOfTeams().get(i).setDriveMarginScore((division.getListOfTeams().get(i).getDriveMargin()+minDistance)/(max+minDistance));
        };
    }

    public void generateStrengthOfScheduleScore(Division division){
        for (int i = 0; i < division.getListOfTeams().size(); i++){
            division.getListOfTeams().get(i).countWinsAndLosses();
        }
        for (int i = 0; i < division.getListOfTeams().size(); i++){
            division.getListOfTeams().get(i).calculateOppWinPercentage();
        }
    }

    public void generateOverallScore(Division division){
        for (int i = 0; i < division.getListOfTeams().size(); i++){
            division.getListOfTeams().get(i).generateScore();
        }
    }

    public void addTeamToConference(Division division, Team team){
        getDivision(team.getDivision()).getConference(team.getConferenceString()).getAllTeams().add(team);
    }

    public Division getDivision(String divisionName){
        for (int i = 0; i < this.allDivisions.size(); i++){
            if (this.allDivisions.get(i).getName().equalsIgnoreCase(divisionName)){
                return this.allDivisions.get(i);
            }
        }
        return null;
    }

    public Division getDivision(Division divisionName){
        for (int i = 0; i < this.allDivisions.size(); i++){
            if (this.allDivisions.get(i).getName().equals(divisionName)){
                return this.allDivisions.get(i);
            }
        }
        return null;
    }
}