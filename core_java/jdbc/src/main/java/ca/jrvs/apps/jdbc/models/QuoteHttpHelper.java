package ca.jrvs.apps.jdbc.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class QuoteHttpHelper
{

    private final String apiKey = "6KSWVQPWVAC05091";
    private OkHttpClient client;

    public QuoteHttpHelper()
    {
        this.client = new OkHttpClient();
    }

    /**
     * Fetch latest quote data from Alpha Vantage endpoint
     *
     * @param symbol
     * @return Quote with latest data
     * @throws IllegalArgumentException - if no data was found for the given symbol
     */
    public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException
    {
        String url = String.format("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s", symbol, apiKey);
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Error code " + response);

            ResponseBody responseBody = response.body();
            if (responseBody == null) throw new IllegalArgumentException("No data found for symbol: " + symbol);

            String responseData = responseBody.string();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseData).get("Global Quote");
            if (jsonNode == null || jsonNode.isEmpty())
                throw new ParseException("No data found for symbol: " + symbol, 0);

            Quote quote = new Quote();
            quote.setTicker(jsonNode.get("01. symbol").asText());
            quote.setOpen(jsonNode.get("02. open").asDouble());
            quote.setHigh(jsonNode.get("03. high").asDouble());
            quote.setLow(jsonNode.get("04. low").asDouble());
            quote.setPrice(jsonNode.get("05. price").asDouble());
            quote.setVolume(jsonNode.get("06. volume").asInt());
            quote.setLatestTradingDay(parseDate(jsonNode.get("07. latest trading day").asText()));
            quote.setPreviousClose(jsonNode.get("08. previous close").asDouble());
            quote.setChange(jsonNode.get("09. change").asDouble());
            quote.setChangePercent(jsonNode.get("10. change percent").asText());
            quote.setTimestamp(new Timestamp(System.currentTimeMillis()));

            return quote;
        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException("Failed to fetch data from httpHelper", e);
        }
    }

    private java.sql.Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = simpleDate.parse(dateString);
        return new java.sql.Date(utilDate.getTime());
    }
}
