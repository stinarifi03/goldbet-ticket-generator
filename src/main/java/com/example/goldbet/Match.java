package com.example.goldbet;

public class Match {
    private String team1;
    private String team2;
    private String matchDate;
    private double odds;
    private String betType;
    private String betChosen;
    private String leagueName;
    private String matchId;
    private String code;
    private String sport;
    private String date;
    private String time;

    public Match(String team1, String team2, String matchDate, double odds, String betType, String betChosen, String leagueName,
                 String matchId, String code, String sport, String date, String time) {
        this.team1 = team1;
        this.team2 = team2;
        this.matchDate = matchDate;
        this.odds = odds;
        this.betType = betType;
        this.betChosen = betChosen;
        this.leagueName = leagueName;
        this.matchId = matchId;
        this.code = code;
        this.sport = sport;
        this.date = date;
        this.time = time;

    }

    // Getters
    public String getTeam1() { return team1; }
    public String getTeam2() { return team2; }
    public String getMatchDate() { return matchDate; }
    public double getOdds() { return odds; }
    public String getBetType() { return betType; }
    public String getBetChosen() { return betChosen; }
    public String getLeagueName() {return leagueName; }
    public String getMatchId() { return matchId; }
    public String getCode() { return code; }
    public String getSport() { return sport; }
    public String getDate() { return date; }
    public String getTime() { return time; }
}