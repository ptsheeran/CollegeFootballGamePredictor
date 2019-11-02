package com.example.ptshe.collegefootballgamepredictor;

//import android.telecom.Conference;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import java.lang.Override;
import java.lang.Comparable;
import java.lang.Double;
import java.util.ArrayList;

public class Team implements Comparable< Team >{

    //public

    //private
    private String teamName;
    private double score;
    private Division division;
    private ArrayList<Game> teamListOfGames;
    private Conference conference;
    private String conferenceString;
    private double teamAvgScore;
    private double scoringMargin;
    private double driveOffenseScore;
    private double driveDefenseScore;
    private double driveMargin;
    private double scoringMarginScore;
    private double driveMarginScore;
    private double wins;
    private double losses;
    private double winPercentage;
    private double oppWinPercentage;

    public Team(String passedName){
        setTeamName(passedName);
        setTeamListOfGames(new ArrayList<Game>());
    }

    public Team(JsonObject jsonObject){
        setTeamName(jsonObject.get("school").getAsString());
        setTeamListOfGames(new ArrayList<Game>());
        setConferenceString(jsonObject.get("conference").getAsString());
    }

    @Override
    public String toString() {
        return getTeamName();
    }

    public int compareTo(@NotNull Team o) {
        Double thisScore = this.getScore();
        Double thatScore = o.getScore();
        return thisScore.compareTo(thatScore);
    }

    public boolean equals(Team passedTeam){
        if (this.getTeamName().equals(passedTeam.getTeamName())){
            return true;
        }
        return false;
    }

    public boolean equals(String passedTeamName){
        if (this.getTeamName().equals(passedTeamName)){
            return true;
        }
        return false;
    }

    public double getScore() { return score; }

    public void setScore(double score) {
        this.score = score;
    }

    public void generateScore(){
        score = (getDriveMarginScore() + getScoringMarginScore() + getWinPercentage() + getWinPercentage() + getWinPercentage() + getOppWinPercentage())/6;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
        division.getListOfTeams().add(this);
    }

    public ArrayList<Game> getTeamListOfGames() {
        return teamListOfGames;
    }

    public void setTeamListOfGames(ArrayList<Game> teamListOfGames) {
        this.teamListOfGames = teamListOfGames;
    }

    public double getTeamAvgScore() {
        double tmpSum = 0;
        double count = 0;

        for (int i = 0; i < teamListOfGames.size(); i++){
            if (teamListOfGames.get(i).awayPoints!=0 && teamListOfGames.get(i).homePoints!=0
                    && !teamListOfGames.get(i).awayTeam.getDivision().getName().equalsIgnoreCase("FCS")
                    && !teamListOfGames.get(i).homeTeam.getDivision().getName().equalsIgnoreCase("FCS")){
                tmpSum+=teamListOfGames.get(i).getPoints(this);
                count++;
            }
        }

        return tmpSum/count;
    }

    public double getTeamAvgOppScore(){

        double tmpSum = 0;
        double count = 0;

        for (int i = 0; i < teamListOfGames.size(); i++){
            if (teamListOfGames.get(i).homePoints!=0 && teamListOfGames.get(i).awayPoints!=0
                    && !teamListOfGames.get(i).awayTeam.getDivision().getName().equalsIgnoreCase("FCS")
                    && !teamListOfGames.get(i).homeTeam.getDivision().getName().equalsIgnoreCase("FCS")){
                if (teamListOfGames.get(i).homeTeam.equals(this)) {
                    tmpSum += teamListOfGames.get(i).awayPoints;
                    count++;
                }else if (teamListOfGames.get(i).awayTeam.equals(this)){
                    tmpSum += teamListOfGames.get(i).homePoints;
                    count++;
                }
            }
        }

        return tmpSum/count;

    }

    public void setTeamAvgScore(double teamAvgScore) {
        this.teamAvgScore = teamAvgScore;
    }

    public double scoringMargin(){
        return this.getTeamAvgScore()-this.getTeamAvgOppScore();
    }

    public void setScoringMargin(){
        this.scoringMargin = scoringMargin();
    }

    public double getScoringMargin(){
        return this.scoringMargin;
    }

    public int getGamesPlayed(){
        int count = 0;
        for (int i = 0; i < teamListOfGames.size(); i++){
            if (teamListOfGames.get(i).awayPoints!=0 && teamListOfGames.get(i).homePoints!=0){
                count++;
            }
        }
        return count;
    }

    public void addGameToGamesList(Game game){
        teamListOfGames.add(game);
        this.setScoringMargin();
    }

    public void calculateOffenseDriveScore(){
        double tmp = 0;
        double tmpSum = 0;
        int offenseDriveCount = 0;
        for (int i = 0; i < teamListOfGames.size(); i++){
            for (int j = 0; j < teamListOfGames.get(i).listOfDrives.size(); j++){
                if (teamListOfGames.get(i).homeTeam.equals(this)
                        && teamListOfGames.get(i).listOfDrives.get(j).offense.equals(this)
                        && !teamListOfGames.get(i).awayTeam.getDivision().getName().equalsIgnoreCase("FCS")){
                    offenseDriveCount++;
                    tmpSum+=teamListOfGames.get(i).listOfDrives.get(j).endYardLine;
                }else if (teamListOfGames.get(i).awayTeam.equals(this)
                        && teamListOfGames.get(i).listOfDrives.get(j).offense.equals(this)
                        && !teamListOfGames.get(i).homeTeam.getDivision().getName().equalsIgnoreCase("FCS")){
                    offenseDriveCount++;
                    tmpSum+= (100-teamListOfGames.get(i).listOfDrives.get(j).endYardLine);
                }
            }
        }
        driveOffenseScore = tmpSum/offenseDriveCount;
    }

    public void calculateDefenseDriveScore(){
        double tmp = 0;
        double tmpSum = 0;
        int defenseDriveCount = 0;
        for (int i = 0; i < teamListOfGames.size(); i++){
            for (int j = 0; j < teamListOfGames.get(i).listOfDrives.size(); j++){
                if (teamListOfGames.get(i).homeTeam.equals(this)
                        && teamListOfGames.get(i).listOfDrives.get(j).defense.equals(this)
                        && !teamListOfGames.get(i).awayTeam.getDivision().getName().equalsIgnoreCase("FCS")){
                    defenseDriveCount++;
                    tmpSum+=(100-teamListOfGames.get(i).listOfDrives.get(j).endYardLine);
                }else if (teamListOfGames.get(i).awayTeam.equals(this)
                        && teamListOfGames.get(i).listOfDrives.get(j).offense.equals(this)
                        && !teamListOfGames.get(i).homeTeam.getDivision().getName().equalsIgnoreCase("FCS")){
                    defenseDriveCount++;
                    tmpSum+=teamListOfGames.get(i).listOfDrives.get(j).endYardLine;
                }
            }
        }
        driveDefenseScore = tmpSum/defenseDriveCount;
    }

    public double getDriveOffenseScore() {
        return driveOffenseScore;
    }

    public void calculateDriveMargin(){
        this.driveMargin = this.driveOffenseScore - this.getDriveDefenseScore();
    }

    public double getDriveDefenseScore() {
        return driveDefenseScore;
    }

    public double getDriveMargin() {
        return driveMargin;
    }

    public double getScoringMarginScore() {
        return scoringMarginScore;
    }

    public void setScoringMarginScore(double scoringMarginScore) {
        this.scoringMarginScore = scoringMarginScore;
    }

    public double getDriveMarginScore() {
        return driveMarginScore;
    }

    public void setDriveMarginScore(double driveMarginScore) {
        this.driveMarginScore = driveMarginScore;
    }

    public void countWinsAndLosses(){
        for (int i = 0; i < teamListOfGames.size(); i++){
            if (teamListOfGames.get(i).homeTeam.equals(this) && teamListOfGames.get(i).homePoints>teamListOfGames.get(i).awayPoints){
                wins = getWins() + 1;
            }else if (teamListOfGames.get(i).homeTeam.equals(this) && teamListOfGames.get(i).homePoints<teamListOfGames.get(i).awayPoints){
                losses = getLosses() + 1;
            }else if (teamListOfGames.get(i).awayTeam.equals(this) && teamListOfGames.get(i).homePoints<teamListOfGames.get(i).awayPoints){
                wins = getWins() + 1;
            }else if (teamListOfGames.get(i).awayTeam.equals(this) && teamListOfGames.get(i).homePoints>teamListOfGames.get(i).awayPoints){
                losses = getLosses() + 1;
            }
        }
        if(getLosses() == 0 && getWins() > 0){
            winPercentage = 1.000f;
        }else if ( getLosses() == 0 && getWins() == 0){
            winPercentage = 0.000f;
        }else{
            winPercentage = getWins() / (getLosses()+getWins());
        }
    }

    public void calculateOppWinPercentage(){
        double count = 0;
        double sumPercentage = 0;

        for (int i = 0; i < teamListOfGames.size(); i++){
            if (teamListOfGames.get(i).homeTeam.equals(this)){
                count++;
                sumPercentage += teamListOfGames.get(i).awayTeam.winPercentage;
            }else{
                count++;
                sumPercentage += teamListOfGames.get(i).homeTeam.winPercentage;
            }
        }
        oppWinPercentage = sumPercentage/count;
    }

    public double getWins() {
        return wins;
    }

    public double getLosses() {
        return losses;
    }

    public double getWinPercentage() {
        return winPercentage;
    }

    public double getOppWinPercentage() {
        return oppWinPercentage;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public String getConferenceString() {
        return conferenceString;
    }

    public void setConferenceString(String conferenceString) {
        this.conferenceString = conferenceString;
    }
}
