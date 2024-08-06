import ca.jrvs.apps.jdbc.models.QuoteHttpHelper;
import controller.StockQuoteController;
import dao.PositionDao;
import dao.QuoteDao;
import okhttp3.OkHttpClient;
import service.PositionService;
import service.QuoteService;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main
{
    public static void main(String[] args)
    {
        Map<String, String> properties = new HashMap<>();
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("properties.txt")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: properties.txt");
            }
            Properties props = new Properties();
            props.load(inputStream);
            for (String name : props.stringPropertyNames()) {
                properties.put(name, props.getProperty(name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Class.forName(properties.get("db-class"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        OkHttpClient client = new OkHttpClient();
        String url = "jdbc:postgresql://" + properties.get("server") + ":" + properties.get("port") + "/" + properties.get("database");
        try (Connection c = DriverManager.getConnection(url, properties.get("username"), properties.get("password"))) {
            QuoteDao quoteRepo = new QuoteDao(c);
            PositionDao positionRepo = new PositionDao(c);
            QuoteHttpHelper httpHelper = new QuoteHttpHelper(properties.get("api-key"), client);
            QuoteService sQuote = new QuoteService(quoteRepo, httpHelper);
            PositionService sPos = new PositionService(positionRepo, quoteRepo);
            StockQuoteController con = new StockQuoteController(sQuote, sPos);
            con.initClient();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}