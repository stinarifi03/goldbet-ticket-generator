package com.example.goldbet;

import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainController2 {
    @FXML private TextField team1Field;
    @FXML private TextField team2Field;
    @FXML private DatePicker matchDatePicker;
    @FXML private TextField oddsField;
    @FXML private TextField betAmountField;
    @FXML private TextField ticketCodeField;
    @FXML private TextField timestampField;
    @FXML private TextArea ticketPreviewArea;
    @FXML private ScrollPane ticketScrollPane;
    @FXML private VBox ticketContainer;
    @FXML private TextField tgField;
    @FXML private TextField ibField;
    @FXML private TextField nsField;
    @FXML private TextField quotaTypeField;
    @FXML private TextField leagueNameField;
    @FXML private TextField betTypeField;
    @FXML private TextField betChosenField;
    @FXML private TextField matchIdField;
    @FXML private TextField codeField;
    @FXML private TextField sportField;
    @FXML private TextField dateField;
    @FXML private TextField timeField;
    @FXML private TextField bonusField;
    @FXML private Label bonusLabel;

    private List<Match> matches = new ArrayList<>();
    private double bonusAmount = 0.0;

    @FXML
    private void addMatch() {
        try {
            String team1 = team1Field.getText();
            String team2 = team2Field.getText();
            String matchDate = matchDatePicker.getValue().toString();
            double odds = Double.parseDouble(oddsField.getText());
            String betType = betTypeField.getText();
            String betChosen = betChosenField.getText();
            String leagueName = leagueNameField.getText();
            String matchId = matchIdField.getText();
            String code = codeField.getText();
            String sport = sportField.getText();
            String date = dateField.getText();
            String time = timeField.getText();

            Match match = new Match(team1, team2, matchDate, odds, betType, betChosen,
                    leagueName, matchId, code, sport, date, time);
            matches.add(match);

            // Show bonus field if 5 or more matches are added
            if (matches.size() >= 5) {
                bonusField.setVisible(true);
                bonusLabel.setVisible(true);
            }

            // Clear fields for next input
            team1Field.clear();
            team2Field.clear();
            oddsField.clear();
            betTypeField.clear();
            betChosenField.clear();
            leagueNameField.clear();
            matchIdField.clear();
            codeField.clear();
            sportField.clear();
            dateField.clear();
            timeField.clear();

        } catch (NumberFormatException e) {
            ticketPreviewArea.setText("Error: Invalid input. Please check your odds.");
        }
    }

    private boolean validateBetAmount() {
        String betAmountText = betAmountField.getText();
        if (betAmountText == null || betAmountText.trim().isEmpty()) {
            ticketContainer.getChildren().clear();
            ticketContainer.getChildren().add(new Label("Error: Bet amount cannot be empty."));
            return false;
        }
        try {
            Double.parseDouble(betAmountText);
            return true;
        } catch (NumberFormatException e) {
            ticketContainer.getChildren().clear();
            ticketContainer.getChildren().add(new Label("Error: Invalid bet amount."));
            return false;
        }
    }

    @FXML
    private void generateTicket() {
        if (!validateBetAmount()) {
            return;
        }
        try {
            // Format the date from DatePicker
            String formattedDate = matchDatePicker.getValue()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            // Generate ticket
            Bet bet = new Bet(matches, Double.parseDouble(betAmountField.getText()));
            double bonus = matches.size() >= 5 && !bonusField.getText().isEmpty() ?
                    Double.parseDouble(bonusField.getText().replace(",", ".")) : 0.0;

            // Calculate TOTAL winnings (potential + bonus)
            double totalWinnings = bet.getPotentialWinnings() + bonus;

            VBox ticket = TicketGenerator6.generateTicket(
                    bet,
                    ticketCodeField.getText(),
                    "Lignano Sabbiadoro, Via Arcobalen",
                    timestampField.getText(),  // OE value from timestamp field
                    tgField.getText(),
                    formattedDate,  // GE value from date picker (formatted)
                    ibField.getText(),
                    nsField.getText(),
                    quotaTypeField.getText(),
                    betTypeField.getText(),
                    betChosenField.getText(),
                    Double.parseDouble(oddsField.getText()),
                    bonus,
                    totalWinnings
            );

            ticketContainer.getChildren().clear();
            ticketContainer.getChildren().add(ticket);
        } catch (NumberFormatException e) {
            ticketContainer.getChildren().clear();
            ticketContainer.getChildren().add(new Label("Error: Invalid input values."));
        } catch (Exception e) {
            ticketContainer.getChildren().clear();
            ticketContainer.getChildren().add(new Label("Error: Unable to generate ticket."));
        }
    }

    @FXML
    private void printTicketButtonClicked() {
        try {
            // Format the date from DatePicker
            String formattedDate = matchDatePicker.getValue()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            Bet bet = new Bet(matches, Double.parseDouble(betAmountField.getText()));
            double bonus = matches.size() >= 5 && !bonusField.getText().isEmpty() ?
                    Double.parseDouble(bonusField.getText().replace(",", ".")) : 0.0;

            // Calculate TOTAL winnings (potential + bonus)
            double totalWinnings = bet.getPotentialWinnings() + bonus;

            VBox ticket = TicketGenerator6.generateTicket(
                    bet,
                    ticketCodeField.getText(),
                    "Lignano Sabbiadoro, Via Arcobalen",
                    timestampField.getText(),
                    tgField.getText(),
                    formattedDate,
                    ibField.getText(),
                    nsField.getText(),
                    quotaTypeField.getText(),
                    betTypeField.getText(),
                    betChosenField.getText(),
                    Double.parseDouble(oddsField.getText()),
                    bonus,
                    totalWinnings
            );

            TicketGenerator6.configureForEpsonTMT20(ticket); // <-- ADD THIS LINE
            printTicket(ticket);
        } catch (Exception e) {
            ticketContainer.getChildren().clear();
            ticketContainer.getChildren().add(new Label("Error: Unable to print ticket."));
        }
    }

    private void printTicket(VBox ticket) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(ticket.getScene().getWindow())) {
            // Set EPSON-specific parameters
            Printer printer = job.getPrinter();
            PageLayout layout = printer.createPageLayout(
                    Paper.A4,
                    PageOrientation.PORTRAIT,
                    Printer.MarginType.HARDWARE_MINIMUM
            );

            // TM-T20III optimal scaling (80mm width)
            double paperWidthMM = 80;
            double pixelsPerMM = 3.78; // JavaFX px-to-mm ratio
            double scaleX = (paperWidthMM * pixelsPerMM) / ticket.getBoundsInParent().getWidth();

            // Apply scaling
            Scale scale = new Scale(scaleX, scaleX * 1.05); // 5% taller to prevent cutoff
            ticket.getTransforms().add(scale);

            // Force black-and-white printing
            job.getJobSettings().setPrintQuality(PrintQuality.HIGH);

            // Print with status check
            if (job.printPage(layout, ticket)) {
                job.endJob();
                System.out.println("Sent to Epson TM-T20III successfully!");
            } else {
                System.out.println("Printing failed: " + job.getJobStatus());
            }
            ticket.getTransforms().remove(scale);
        }
    }

    @FXML
    private void testPrinter() {
        Text testPage = new Text("PRINTER TEST\n" +
                "----------------\n" +
                "EPSON TM-T20III\n" +
                new Date().toString());

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job.printPage(testPage)) {
            job.endJob();
        }
    }
}