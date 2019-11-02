package com.example.ptshe.collegefootballgamepredictor;



import android.support.v7.app.AppCompatActivity;

import com.google.gson.*;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;


public class Main  extends AppCompatActivity {

    int week;

    public static void main(String[] args) throws IOException, ParseException {
        //Date Objects
        Date date = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Scanner in = new Scanner(System.in);

        // Downloads JSON Data from URL
        System.out.print("Enter the Week: ");
        SetWeek(in.nextInt());
        in.nextLine();
        System.out.println();
        System.out.print("Download fresh data? (Y/N): ");
        String tmpData = in.nextLine();

        JsonArray rootTeamObj;
        JsonArray rootGameObj;
        JsonArray rootDriveObj;
        JsonArray rootConfObj;

        Gson gson = new GsonBuilder().serializeNulls().create();

        if (tmpData.equalsIgnoreCase("Y")){
            rootTeamObj = InputController.downloadAPIData("https://api.collegefootballdata.com/teams/fbs?year=2019");
            rootGameObj = InputController.downloadAPIData("https://api.collegefootballdata.com/games?year=2019&seasonType=both");
            rootDriveObj = InputController.downloadAPIData("https://api.collegefootballdata.com/drives?seasonType=both&year=2019");
            rootConfObj = InputController.downloadAPIData("https://api.collegefootballdata.com/conferences");

            FileWriter teams = new FileWriter("data/teams.json", false);
            FileWriter games = new FileWriter("data/games.json", false);
            FileWriter drives = new FileWriter("data/drives.json", false);
            FileWriter conferences = new FileWriter("data/conferences.json", false);

            teams.write(gson.toJson(rootTeamObj));
            teams.flush();
            teams.close();

            games.write(gson.toJson(rootGameObj));
            games.flush();
            games.close();

            drives.write(gson.toJson(rootDriveObj));
            drives.flush();
            drives.close();

            conferences.write(gson.toJson(rootConfObj));
            conferences.flush();
            conferences.close();

        }else{
            rootTeamObj = InputController.readFileData("data/teams.json");
            rootGameObj = InputController.readFileData("data/games.json");
            rootDriveObj = InputController.readFileData("data/drives.json");
            rootConfObj = InputController.readFileData("data/conferences.json");
        }

        TeamList theTeamList = new TeamList();
        GameList theGameList = new GameList();
        DriveList theDriveList = new DriveList();
        Division fbs = new Division("FBS");
        Division fcs = new Division("FCS");
        theTeamList.allDivisions.add(fbs);
        theTeamList.allDivisions.add(fcs);

        for (int i = 0; i < rootConfObj.size(); i++){
            theTeamList.getDivision("FBS").getAllDivisionConferences().add( new Conference(fbs, rootConfObj.get(i).getAsJsonObject()));
        }

        for(int i = 0; i < rootTeamObj.size(); i++) {
            Team tmp = new Team(rootTeamObj.get(i).getAsJsonObject());
            theTeamList.allTeams.add(tmp);
            tmp.setDivision(fbs);
            fbs.getConference(tmp.getConferenceString()).getAllTeams().add(tmp);
            tmp.setConference(fbs.getConference(tmp.getConferenceString()));
        }

        for(int i = 0; i < rootGameObj.size(); i++){
            if (theTeamList.contains(rootGameObj.get(i).getAsJsonObject().get("home_team").getAsString())){

            }else{
                theTeamList.allTeams.add(new Team(rootGameObj.get(i).getAsJsonObject().get("home_team").getAsString()));
                theTeamList.get(rootGameObj.get(i).getAsJsonObject().get("home_team").getAsString()).setDivision(fcs);
            }
            if (theTeamList.contains(rootGameObj.get(i).getAsJsonObject().get("away_team").getAsString())){

            }else{
                theTeamList.allTeams.add(new Team(rootGameObj.get(i).getAsJsonObject().get("away_team").getAsString()));
                theTeamList.get(rootGameObj.get(i).getAsJsonObject().get("away_team").getAsString()).setDivision(fcs);
            }
            theGameList.allGames.add(new Game(theTeamList, rootGameObj.get(i).getAsJsonObject()));
        }

        for (int i = 0; i < rootDriveObj.size(); i++){
            theDriveList.allDrives.add(new Drive(theTeamList, theGameList, rootDriveObj.get(i).getAsJsonObject()));
        }

        theTeamList.checkDivisions();
        theTeamList.generateScoringMarginScore(fbs);
        theTeamList.generateDriveMarginScore(fbs);
        theTeamList.generateStrengthOfScheduleScore(fbs);
        theTeamList.generateOverallScore(fbs);
        Collections.sort(theTeamList.allTeams, Collections.reverseOrder());

        System.out.println();
        System.out.println("Jorge's CFB Ratings (Runtime: "+sdf.format(date)+")");

        System.out.print("Request Results: ");
        String tmp = in.nextLine();
        Conference tmpC = fbs.getConference(tmp);
        System.out.print("Print to CSV? (Y/N): ");
        String csv = in.nextLine();

        if (csv.equalsIgnoreCase("y")){
            if (tmpC!=null && !tmp.equalsIgnoreCase("FBS")){
                Collections.sort(fbs.getConference(tmp).getAllTeams(), Collections.reverseOrder());
                PrintConferenceRankingsCSV(fbs.getConference(tmp));
            }else if(tmp.equalsIgnoreCase("FBS")){
                Collections.sort(fbs.getListOfTeams(), Collections.reverseOrder());
                PrintRankingsCSV(fbs);
            }else if (tmp.equalsIgnoreCase("ALL")){
                for (Conference conference : fbs.getAllDivisionConferences()){
                    Collections.sort(conference.getAllTeams(), Collections.reverseOrder());
                    PrintConferenceRankingsCSV(conference);
                }
            } else{
                System.out.println("No matching conference or division.  Exiting...");
                System.exit(0);
            }
        }else {
            if (tmpC!=null && !tmp.equalsIgnoreCase("FBS")){
                Collections.sort(fbs.getConference(tmp).getAllTeams(), Collections.reverseOrder());
                PrintConferenceRankings(fbs.getConference(tmp));
            }else if(tmp.equalsIgnoreCase("FBS")){
                PrintRankings(theTeamList, theTeamList.allDivisions.get(0));
            }else{
                System.out.println("No matching conference or division.  Exiting...");
                System.exit(0);
            }
        }
        System.out.println();
        System.out.println("Output complete.  Exiting...");
    }

    public static void PrintRankings(TeamList thePassedTeamList, Division division){
        int rank = 0;
        boolean first = true;
        for(int i = 0; i < thePassedTeamList.allTeams.size(); i++){
            if (thePassedTeamList.allTeams.get(i).getDivision().equals(division) && first==true){
                rank++;
                System.out.println("Rank, Team, ScoringMarginScore, DriveMarginScore, Winning%, SoS, Score, Conference");
                System.out.print(""+rank+", "+thePassedTeamList.allTeams.get(i).getTeamName()+", ");
                System.out.printf("%4.3f", thePassedTeamList.allTeams.get(i).getScoringMarginScore());
                System.out.print(", ");
                System.out.printf("%4.3f", thePassedTeamList.allTeams.get(i).getDriveMarginScore());
                System.out.print(", ");
                System.out.printf("%4.3f", thePassedTeamList.allTeams.get(i).getWinPercentage());
                System.out.print(", ");
                System.out.printf("%4.3f", thePassedTeamList.allTeams.get(i).getOppWinPercentage());
                System.out.print(", ");
                System.out.printf("%4.3f", thePassedTeamList.allTeams.get(i).getScore());
                System.out.print(", "+thePassedTeamList.allTeams.get(i).getConference());
                first=false;
            }else{
                if (thePassedTeamList.allTeams.get(i).getDivision().equals(division)){
                    rank++;
                    System.out.print("\n"+rank+", "+thePassedTeamList.allTeams.get(i).getTeamName()+", ");
                    System.out.printf("%4.3f", thePassedTeamList.allTeams.get(i).getScoringMarginScore());
                    System.out.print(", ");
                    System.out.printf("%4.3f", thePassedTeamList.allTeams.get(i).getDriveMarginScore());
                    System.out.print(", ");
                    System.out.printf("%4.3f", thePassedTeamList.allTeams.get(i).getWinPercentage());
                    System.out.print(", ");
                    System.out.printf("%4.3f", thePassedTeamList.allTeams.get(i).getOppWinPercentage());
                    System.out.print(", ");
                    System.out.printf("%4.3f", thePassedTeamList.allTeams.get(i).getScore());
                    System.out.print(", "+thePassedTeamList.allTeams.get(i).getConference());
                }
            }
        }
    }

    public static void PrintConferenceRankings(Conference conference){
        int rank = 0;
        boolean first = true;
        for(int i = 0; i < conference.getAllTeams().size(); i++){
            if (first==true){
                rank++;
                System.out.println("Rank, Team, ScoringMarginScore, DriveMarginScore, Winning%, SoS, Score, Conference");
                System.out.print(""+rank+", "+conference.getAllTeams().get(i).getTeamName()+", ");
                System.out.printf("%4.3f", conference.getAllTeams().get(i).getScoringMarginScore());
                System.out.print(", ");
                System.out.printf("%4.3f", conference.getAllTeams().get(i).getDriveMarginScore());
                System.out.print(", ");
                System.out.printf("%4.3f", conference.getAllTeams().get(i).getWinPercentage());
                System.out.print(", ");
                System.out.printf("%4.3f", conference.getAllTeams().get(i).getOppWinPercentage());
                System.out.print(", ");
                System.out.printf("%4.3f", conference.getAllTeams().get(i).getScore());
                System.out.print(", "+conference.getAllTeams().get(i).getConference());
                first=false;
            }else{
                rank++;
                System.out.print("\n"+rank+", "+conference.getAllTeams().get(i).getTeamName()+", ");
                System.out.printf("%4.3f", conference.getAllTeams().get(i).getScoringMarginScore());
                System.out.print(", ");
                System.out.printf("%4.3f", conference.getAllTeams().get(i).getDriveMarginScore());
                System.out.print(", ");
                System.out.printf("%4.3f", conference.getAllTeams().get(i).getWinPercentage());
                System.out.print(", ");
                System.out.printf("%4.3f", conference.getAllTeams().get(i).getOppWinPercentage());
                System.out.print(", ");
                System.out.printf("%4.3f", conference.getAllTeams().get(i).getScore());
                System.out.print(", "+conference.getAllTeams().get(i).getConference());
            }
        }
    }

    public static void PrintRankingsCSV(Division division) throws IOException {
        FileWriter csvWriter = new FileWriter("results.csv", false);
        csvWriter.append("Rank, Team, ScoringMarginScore, DriveMarginScore, Winning%, SoS, Score, Conference");
        csvWriter.append("\n");
        int rank = 1;
        for (int i = 0; i < division.getListOfTeams().size(); i++){
            csvWriter.append(rank+", "+division.getListOfTeams().get(i).getTeamName());
            csvWriter.append(", "+String.format("%4.3f", division.getListOfTeams().get(i).getScoringMarginScore()));
            csvWriter.append(", "+String.format("%4.3f", division.getListOfTeams().get(i).getDriveMarginScore()));
            csvWriter.append(", "+String.format("%4.3f", division.getListOfTeams().get(i).getWinPercentage()));
            csvWriter.append(", "+String.format("%4.3f", division.getListOfTeams().get(i).getOppWinPercentage()));
            csvWriter.append(", "+String.format("%4.3f", division.getListOfTeams().get(i).getScore()));
            csvWriter.append(", "+division.getListOfTeams().get(i).getConference());
            csvWriter.append("\n");
            rank++;
            csvWriter.flush();
        }
        csvWriter.flush();
        csvWriter.close();
    }

    public static void PrintConferenceRankingsCSV(Conference conference) throws IOException {
        FileWriter csvWriter = new FileWriter(conference.getAbbreviation()+".csv", false);
        csvWriter.append("Rank, Team, ScoringMarginScore, DriveMarginScore, Winning%, SoS, Score, Conference");
        csvWriter.append("\n");
        int rank = 1;
        for (int i = 0; i < conference.getAllTeams().size(); i++){
            csvWriter.append(rank+", "+conference.getAllTeams().get(i).getTeamName());
            csvWriter.append(", "+String.format("%4.3f", conference.getAllTeams().get(i).getScoringMarginScore()));
            csvWriter.append(", "+String.format("%4.3f", conference.getAllTeams().get(i).getDriveMarginScore()));
            csvWriter.append(", "+String.format("%4.3f", conference.getAllTeams().get(i).getWinPercentage()));
            csvWriter.append(", "+String.format("%4.3f", conference.getAllTeams().get(i).getOppWinPercentage()));
            csvWriter.append(", "+String.format("%4.3f", conference.getAllTeams().get(i).getScore()));
            csvWriter.append(", "+conference.getAllTeams().get(i).getConference());
            csvWriter.append("\n");
            rank++;
            csvWriter.flush();
        }
        csvWriter.flush();
        csvWriter.close();
    }

    public static int GetWeek(){
        return 9;
    }

    public static void SetWeek(int num){

    }
}