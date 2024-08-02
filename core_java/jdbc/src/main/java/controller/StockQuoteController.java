package controller;

import ca.jrvs.apps.jdbc.models.Position;
import ca.jrvs.apps.jdbc.models.Quote;
import service.PositionService;
import service.QuoteService;

import java.sql.SQLException;
import java.util.*;

public class StockQuoteController
{

    private QuoteService quoteService;
    private PositionService positionService;

    public StockQuoteController(QuoteService quoteService, PositionService positionService)
    {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }

    /**
     * User interface for our application
     */

    private void displayMenu()
    {
        System.out.println();
        System.out.println("=====================");
        System.out.println("Stock Quote App Menu:");
        System.out.println("1. Buy Stock");
        System.out.println("2. Sell Stock");
        System.out.println("3. Get Stock Quote");
        System.out.println("4. View All Positions");
        System.out.println("5. Exit");
        System.out.print("Enter action: ");
    }

    private void buy(Scanner scanner)
    {
        try {
            System.out.print("\nEnter stock symbol: ");
            String symbol = scanner.nextLine();
            System.out.print("Enter number of shares: ");
            int numberOfShares = scanner.nextInt();
            System.out.print("Enter price: ");
            Double price = scanner.nextDouble();

            // clearing scanner to avoid extra line causing menu to jump to switch default case (see line:130)
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            positionService.buy(symbol, numberOfShares, price);
            System.out.println("Bought " + numberOfShares + " shares of " + symbol + " at $" + price + " each.");
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error while buying in menu: ", e);
        }
    }

    private void sell(Scanner scanner)
    {
        try {
            System.out.println("\nEnter stock symbol: ");
            String symbol = scanner.nextLine();
            positionService.sell(symbol);
            System.out.println("Sold all shares of " + symbol);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error while buying in menu: ", e);
        }
    }

    private void showQuote(Scanner scanner)
    {
        try {
            System.out.print("\nEnter stock symbol: ");
            String symbol = scanner.nextLine();
            Optional<Quote> quote = quoteService.fetchQuoteDataFromAPI(symbol);
            if (quote.isPresent()) {
                System.out.println("Stock Quote for " + symbol + ": " + quote.get());
            } else {
                System.out.println("No quote data found for " + symbol);
            }
        } catch (Exception e) {
            System.out.println("Error fetching stock quote: " + e.getMessage());
        }
    }

    private void showPositions()
    {
        try {
            List<Position> positions = new ArrayList<>();
            positionService.findAllPositions().forEach(positions::add);
            if (!positions.isEmpty()) {
                System.out.println("\nPositions:" + positions);
            } else {
                System.out.println("No quote data found");
            }
        } catch (Exception e) {
            System.out.println("showPositions() Error fetching stock quote: " + e.getMessage());
        }
    }

    public void initClient() throws SQLException
    {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);;
        while (true) {
            displayMenu();
            String action = scanner.nextLine();
            switch (action) {
                case "1":
                    System.out.println("=====================");
                    buy(scanner);
                    break;
                case "2":
                    System.out.println("=====================");
                    sell(scanner);
                    break;
                case "3":
                    System.out.println("=====================");
                    showQuote(scanner);
                    break;
                case "4":
                    System.out.println("=====================");
                    showPositions();
                    break;
                case "5":
                    System.out.println("=====================");
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid action. Please try again.");
            }
        }
    }
}
