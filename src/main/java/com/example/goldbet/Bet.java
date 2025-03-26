package com.example.goldbet;

import java.util.List;

public class Bet {
    private List<Match> matches;
    private double betAmount;
    private double totalOdds;
    private double potentialWinnings;

    public Bet(List<Match> matches, double betAmount) {
        this.matches = matches;
        this.betAmount = betAmount;
        this.totalOdds = calculateTotalOdds();
        this.potentialWinnings = betAmount * totalOdds;
    }

    private double calculateTotalOdds() {
        double total = 1.0;
        for (Match match : matches) {
            total *= match.getOdds();
        }
        return total;
    }

    // Getters
    public List<Match> getMatches() { return matches; }
    public double getBetAmount() { return betAmount; }
    public double getTotalOdds() { return totalOdds; }
    public double getPotentialWinnings() { return potentialWinnings; }
}