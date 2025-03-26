package com.example.goldbet;

import com.google.zxing.WriterException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketGenerator6 {
    private static final int TICKET_WIDTH = 300;
    private static final String CURRENCY = "€";

    private static Image loadImageResource(String imagePath) {
        InputStream stream = TicketGenerator6.class.getResourceAsStream(imagePath);
        if (stream == null) {
            throw new RuntimeException("Image not found: " + imagePath);
        }
        return new Image(stream);
    }

    public static VBox generateTicket(Bet bet, String ticketCode, String location,
                                      String timestamp, String tg, String date,
                                      String ib, String ns, String quotaType,
                                      String betType, String betChosen,
                                      double odds, double bonusAmount,double totalWinnings) {

        // Main ticket container
        VBox ticket = new VBox(3);
        ticket.setMaxWidth(TICKET_WIDTH);
        ticket.setMinWidth(TICKET_WIDTH);
        ticket.setPadding(new Insets(5, 5, 5, 5));
        ticket.setStyle("-fx-background-color: white; -fx-border-color: #ddd;");

        // 1. Header Section (Logo + Title + QR Code)
        HBox headerBox = createHeader(ticketCode);

        // 2. Ticket Details Section
        //TextFlow detailsFlow = createTicketDetails(ticketCode, location, tg, timestamp, date, ib, ns);
        Pane detailsFlow = createTicketDetails(ticketCode, location, tg, timestamp, date, ib, ns);

        // 3. Bet Information Section
        VBox betInfoBox = createBetInfoSection(bet, quotaType, betType, betChosen, odds);

        // 4. Amount Section
        Pane amountSection = createAmountSection(bet.getBetAmount(), bonusAmount, totalWinnings);

        // 5. Footer Section (Logos)
        HBox footerBox = createFooter();

        // 6. Barcode
        HBox barcodeContainer = createBarcode(ticketCode);

        // Assemble all sections
        ticket.getChildren().addAll(
                headerBox,
                detailsFlow,
                betInfoBox,
                amountSection,
                footerBox,
                barcodeContainer
        );

        return ticket;
    }

    private static HBox createHeader(String ticketCode) {
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 5, 0, 5));

        // Logo
        ImageView logoView = new ImageView(loadImageResource("/images/goldbet_logo.png"));
        logoView.setFitWidth(50);
        logoView.setPreserveRatio(true);

        // Title
        Text goldbetText = new Text("GoldBet");
        goldbetText.setStyle("-fx-font-size: 42; -fx-font-weight: bold;");

        // QR Code section
        VBox qrCodeBox = new VBox(2);
        qrCodeBox.setAlignment(Pos.CENTER);

        Text bonusText = new Text("BONUS BENVENUTO ONLI");
        bonusText.setStyle("-fx-font-size: 6; -fx-font-weight: bold;");
        bonusText.setTextAlignment(TextAlignment.LEFT);

        ImageView qrCodeView = new ImageView(loadImageResource("/images/qr_code.png"));
        qrCodeView.setFitWidth(70);
        qrCodeView.setPreserveRatio(true);

        qrCodeBox.getChildren().addAll(bonusText, qrCodeView);

        headerBox.getChildren().addAll(logoView, goldbetText, qrCodeBox);
        return headerBox;
    }

    private static Pane createTicketDetails(String ticketCode, String location,
                                            String tg, String timestamp,
                                            String date, String ib, String ns) {
        // Create GridPane for precise layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(2,2,0,0)); // Right padding removed

        // Column setup - minimal spacing
        ColumnConstraints lineCol = new ColumnConstraints();
        lineCol.setPrefWidth(1); // Vertical line column (just enough for the line)
        ColumnConstraints contentCol = new ColumnConstraints();
        contentCol.setPrefWidth(274); // Content column (280px total - 6px)
        grid.getColumnConstraints().addAll(lineCol, contentCol);

        // 1. Top horizontal line (spans both columns)
        Line topLine = createHorizontalLine(280);
        grid.add(topLine, 0, 0, 2, 1);

        // 2. Vertical line (full height)
        Line verticalLine = new Line(0, 0, 0, 0);
        verticalLine.setStroke(Color.BLACK);
        verticalLine.setStrokeWidth(1);
        grid.add(verticalLine, 0, 1);

        // 3. Content with minimal left padding
        VBox content = new VBox(0); // Reduced spacing between lines
        content.setPadding(new Insets(0, 0, 0, 2)); // Only 3px left padding
        content.getChildren().addAll(
                createText("CB-" + ticketCode),
                createText("CC-4098   NC-GBO Italy S.p.A   PV-14274   TM-4"),
                createText("NP-" + location + "   TG-" + tg),
                createText("GE-" + date + "      OE-" + timestamp + "      CG-2005376"),
                createText("IB-" + ib + " ".repeat(18) + "NS-" + ns) // Adjusted spacing
        );
        grid.add(content, 1, 1);

        // 4. Bottom horizontal line
        Line bottomLine = createHorizontalLine(280);
        grid.add(bottomLine, 0, 2, 2, 1);

        // Bind vertical line height
        verticalLine.endYProperty().bind(content.heightProperty());

        return grid;
    }

    private static Line createHorizontalLine(double length) {
        Line line = new Line(0, 0, length, 0);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(1.5);
        HBox.setHgrow(line, Priority.ALWAYS);
        return line;
    }

    private static Text createText(String content) {
        Text text = new Text(content);
        text.setStyle("-fx-font-size: 10; -fx-font-family: 'Courier New';");
        return text;
    }



    private static VBox createBetInfoSection(Bet bet, String quotaType,
                                             String betType, String betChosen,
                                             double odds) {
        VBox betInfoBox = new VBox(2);
        betInfoBox.setAlignment(Pos.CENTER_LEFT);

        // Quota Type (centered)
        Text quotaText = new Text(quotaType.toUpperCase());
        quotaText.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");
        HBox quotaBox = new HBox(quotaText);
        quotaBox.setAlignment(Pos.CENTER);
        betInfoBox.getChildren().add(quotaBox);

        // Group all matches by league first
        Map<String, List<Match>> matchesByLeague = new HashMap<>();
        for (Match match : bet.getMatches()) {
            String leagueKey = match.getLeagueName() != null ? match.getLeagueName() : "NO LEAGUE";
            if (!matchesByLeague.containsKey(leagueKey)) {
                matchesByLeague.put(leagueKey, new ArrayList<>());
            }
            matchesByLeague.get(leagueKey).add(match);
        }

        // Create one section per league
        for (Map.Entry<String, List<Match>> entry : matchesByLeague.entrySet()) {
            String currentLeague = entry.getKey();
            List<Match> leagueMatches = entry.getValue();

            if (!leagueMatches.isEmpty()) {
                // Create container for league header with lines
                VBox leagueHeaderContainer = new VBox();
                leagueHeaderContainer.setAlignment(Pos.CENTER);
                leagueHeaderContainer.setPadding(new Insets(5, 0, 5, 0)); // Add some vertical spacing

                // Top black line (7cm ≈ 265px at 96dpi)
                Pane topLine = new Pane();
                topLine.setMinHeight(2);
                topLine.setPrefHeight(2);
                topLine.setMaxHeight(2);
                topLine.setMinWidth(265);
                topLine.setPrefWidth(265);
                topLine.setStyle("-fx-background-color: black; -fx-border-color: black;");

                // League Name
                Text leagueText = new Text(currentLeague);
                leagueText.setStyle("-fx-font-weight: bold; -fx-font-size: 10;");
                HBox leagueBox = new HBox(leagueText);
                leagueBox.setAlignment(Pos.CENTER);
                leagueBox.setPadding(new Insets(2, 0, 2, 0)); // Tight padding around text

                // Bottom black line
                Pane bottomLine = new Pane();
                bottomLine.setMinHeight(2);
                bottomLine.setPrefHeight(2);
                bottomLine.setMaxHeight(2);
                bottomLine.setMinWidth(265);
                bottomLine.setPrefWidth(265);
                bottomLine.setStyle("-fx-background-color: black; -fx-border-color: black;");

                leagueHeaderContainer.getChildren().addAll(topLine, leagueBox, bottomLine);
                betInfoBox.getChildren().add(leagueHeaderContainer);

                // Add all matches for this league
                TextFlow leagueMatchesFlow = new TextFlow();
                leagueMatchesFlow.setStyle("-fx-font-size: 10; -fx-font-family: 'Courier New';");

                for (Match match : leagueMatches) {
                    int sportPadding = Math.max(15 - match.getSport().length(), 1);
                    int betTypePadding = Math.max(35 - match.getBetType().length(), 1);

                    // Create separate text nodes for each part
                    Text prefix = new Text("(" + match.getMatchId() + ") " + match.getCode() + " " + match.getSport() +
                            " ".repeat(sportPadding) + match.getDate() + " " + match.getTime() + "\n");
                    prefix.setStyle("-fx-font-size: 9; -fx-font-family: 'Courier New';");

                    Text team1 = new Text(match.getTeam1());
                    team1.setStyle("-fx-font-size: 10; -fx-font-family: 'Courier New'; -fx-font-weight: bold;");

                    Text separator = new Text(" - ");
                    separator.setStyle("-fx-font-size: 10; -fx-font-family: 'Courier New';");

                    Text team2 = new Text(match.getTeam2());
                    team2.setStyle("-fx-font-size: 10; -fx-font-family: 'Courier New'; -fx-font-weight: bold;");

                    Text suffix = new Text("\n" + match.getBetType() + " ".repeat(betTypePadding) +
                            match.getBetChosen() + " ".repeat(2) + formatNumber(match.getOdds()) + "\n\n");
                    suffix.setStyle("-fx-font-size: 10; -fx-font-family: 'Courier New';");

                    // Combine in TextFlow
                    TextFlow matchFlow = new TextFlow(prefix, team1, separator, team2, suffix);
                    leagueMatchesFlow.getChildren().add(matchFlow);
                    //leagueMatchesFlow.getChildren().add(matchText);
                }
                betInfoBox.getChildren().add(leagueMatchesFlow);
            }
        }
        return betInfoBox;
    }


    private static VBox createAmountSection(double betAmount, double bonusAmount,
                                            double potentialWinnings) {
        VBox container = new VBox(0); // Zero spacing container
        container.setPadding(new Insets(0));

        // 1. Importo Scommesso with precise lift
        HBox importoLine = new HBox();
        importoLine.setAlignment(Pos.CENTER_LEFT);
        importoLine.setPadding(new Insets(-5, 0, 0, 0)); // Negative top padding lifts it up

        Text labelText = new Text("IMPORTO SCOMMESSO".toUpperCase());
        labelText.setStyle("-fx-font-weight: bold;");

        // Right-aligned amount
        Text amountText = new Text(formatNumber(betAmount) +" "+ CURRENCY);
        amountText.setStyle("-fx-font-weight: bold;");
        HBox amountBox = new HBox(amountText);
        amountBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(amountBox, Priority.ALWAYS);

        importoLine.getChildren().addAll(labelText, amountBox);
        container.getChildren().add(importoLine);

        // 2. Bonus line (right-aligned if present)
        if (bonusAmount > 0) {
            HBox bonusLine = new HBox();
            Text bonusLabel = new Text("BONUS".toUpperCase());
            bonusLabel.setStyle("-fx-font-weight: bold;");

            Text bonusValue = new Text(formatNumber(bonusAmount) +" "+ CURRENCY);
            bonusValue.setStyle("-fx-font-weight: bold;");
            HBox bonusValueBox = new HBox(bonusValue);
            bonusValueBox.setAlignment(Pos.CENTER_RIGHT);
            HBox.setHgrow(bonusValueBox, Priority.ALWAYS);

            bonusLine.getChildren().addAll(bonusLabel, bonusValueBox);
            container.getChildren().add(bonusLine);
        }

        // 3. Vincita container - sticks directly underneath
        container.getChildren().add(createVincitaContainer(potentialWinnings));

        // 4. Disclaimer
        //container.getChildren().add(createDisclaimerText());
        Text disclaimer = createDisclaimerText();
        HBox disclaimerBox = new HBox(disclaimer);
        disclaimerBox.setAlignment(Pos.CENTER);
        container.getChildren().add(disclaimerBox);

        return container;
    }


    private static HBox createVincitaContainer(double potentialWinnings) {
        HBox container = new HBox();
        container.setPrefSize(272, 36); // Slightly reduced height
        container.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        //Left pattern (2.05cm = 77px) with vertical lines
        Pane leftPattern = createVerticalLinePattern(77, 35);

        // Middle text (118px)
        VBox middleText = new VBox(0); // No spacing
        middleText.setPrefSize(118, 36);
        middleText.setAlignment(Pos.CENTER);

        Text totalText = new Text("VINCITA TOTALE");
        totalText.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");

        Text amountText = new Text(formatNumber(potentialWinnings) + CURRENCY);
        amountText.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        middleText.getChildren().addAll(totalText, amountText);

        // Right pattern (2.05cm = 77px) with vertical lines
        Pane rightPattern = createVerticalLinePattern(77, 35);

        container.getChildren().addAll(leftPattern, middleText, rightPattern);
        return container;
    }

    private static Text createDisclaimerText() {
        Text disclaimer = new Text("\n"+
                "    Consulta le probabilità di vincita e i regolamenti di gioco su goldbet.it.\n" +
                        "Il gioco è vietato ai minori di 18 anni e può causare dipendenza patologica."
        );
        disclaimer.setStyle("-fx-font-size: 8; -fx-font-weight: bold;");
        VBox.setMargin(disclaimer, new Insets(3, 0, 0, 0)); // Small 3px top margin only
        return disclaimer;
    }

    private static Pane createVerticalLinePattern(double width, double height) {
        Pane patternPane = new Pane();
        patternPane.setPrefSize(width, height);

        // Create vertical lines (3px wide, 5px spacing)
        double lineWidth = 3;
        for (double x = 0; x < width; x += lineWidth + 5) {
            Line line = new Line(x, 0, x, height);
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(lineWidth);
            patternPane.getChildren().add(line);
        }
        return patternPane;
    }



    private static HBox createFooter() {
        HBox footerBox = new HBox(5);
        footerBox.setAlignment(Pos.CENTER);
        footerBox.setPadding(new Insets(2, 0, 2, 0)); // Reduced vertical padding

        ImageView gicLogo = new ImageView(loadImageResource("/images/gioco.jpeg"));
        gicLogo.setFitWidth(40);
        gicLogo.setPreserveRatio(true);

        ImageView plus18Logo = new ImageView(loadImageResource("/images/+18.jpg"));
        plus18Logo.setFitWidth(40);
        plus18Logo.setPreserveRatio(true);

        ImageView admLogo = new ImageView(loadImageResource("/images/amd_timone.png"));
        admLogo.setFitWidth(40);
        admLogo.setPreserveRatio(true);

        ImageView adm = new ImageView(loadImageResource("/images/adm.jpg"));
        adm.setFitWidth(80);
        adm.setPreserveRatio(true);

        footerBox.getChildren().addAll(gicLogo, plus18Logo, admLogo, adm);
        return footerBox;
    }




    private static HBox createBarcode(String ticketCode) {
        try {
            String barcodePath = "barcode.png";
            BarcodeGenerator.generateBarcodeImage(ticketCode, barcodePath);

            // Create ImageView with exact dimensions
            ImageView barcodeView = new ImageView(new Image("file:" + barcodePath));
            barcodeView.setFitWidth(260);  // Set exact width
            barcodeView.setFitHeight(60);  // Set exact height
            barcodeView.setPreserveRatio(false);  // Disable ratio preservation to use exact dimensions

            // Create centered container
            HBox barcodeContainer = new HBox();
            barcodeContainer.setAlignment(Pos.CENTER);
            barcodeContainer.setPadding(new Insets(2, 0, 2, -10));
            barcodeContainer.getChildren().add(barcodeView);

            return barcodeContainer;
        } catch (WriterException | IOException e) {
            System.err.println("Barcode error: " + e.getMessage());
            return new HBox();
        }
    }

    private static String formatNumber(double number) {
        return String.format("%.2f", number).replace(".", ",");
    }


    public static void configureForEpsonTMT20(VBox ticket) {
        // 1. Force monochrome
        ticket.setStyle("-fx-background-color: white; -fx-text-fill: black;");

        // 2. Optimal thermal printer font
        String epsonFont = "-fx-font-family: 'Courier New', monospace; -fx-font-size: 9pt;";
        ticket.lookupAll(".text").forEach(node ->
                ((Text)node).setStyle(epsonFont + "-fx-font-weight: bold;"));

        // 3. Barcode settings
        ImageView barcode = (ImageView)ticket.lookup("#barcode");
        if (barcode != null) {
            barcode.setFitWidth(300); // Optimal width for Epson
            barcode.setPreserveRatio(true);
        }

        // 4. Reduce padding
        ticket.setPadding(new Insets(2)); // 2px uniform
    }
}