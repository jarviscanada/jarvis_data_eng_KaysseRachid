package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ca.jrvs.apps.jdbc.models.Quote;
import ca.jrvs.apps.jdbc.models.QuoteHttpHelper;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuoteHttpHelperTest {

    @Mock
    private OkHttpClient client;

    @Mock
    private Response response;

    @Mock
    private Call call;

    @Mock
    private ResponseBody responseBody;

    @InjectMocks
    private QuoteHttpHelper quoteHttpHelper;

    private String apiKey = "6KSWVQPWVAC05091";

    private String jsonBodyResponse = "{\n" +
            "  \"Global Quote\": {\n" +
            "    \"01. symbol\": \"AAPL\",\n" +
            "    \"02. open\": \"145.67\",\n" +
            "    \"03. high\": \"146.00\",\n" +
            "    \"04. low\": \"144.50\",\n" +
            "    \"05. price\": \"145.30\",\n" +
            "    \"06. volume\": \"1000000\",\n" +
            "    \"07. latest trading day\": \"2023-07-23\",\n" +
            "    \"08. previous close\": \"144.00\",\n" +
            "    \"09. change\": \"1.30\",\n" +
            "    \"10. change percent\": \"0.90%\"\n" +
            "  }\n" +
            "}";

    @Test
    void testFetchQuoteInfo_Success() throws IOException {
        // Arrange
        String symbol = "AAPL";

        ResponseBody responseBody = mock(ResponseBody.class);
        when(responseBody.string()).thenReturn(jsonBodyResponse);

        Response response = mock(Response.class);
        when(response.isSuccessful()).thenReturn(true);
        when(response.body()).thenReturn(responseBody);

        Call call = mock(Call.class);
        when(call.execute()).thenReturn(response);
        when(client.newCall(any(Request.class))).thenReturn(call);

        // Act
        Quote quote = quoteHttpHelper.fetchQuoteInfo(symbol);

        // Assert
        assertNotNull(quote);
        assertEquals("AAPL", quote.getTicker());
        assertEquals(145.67, quote.getOpen());
        assertEquals(146.00, quote.getHigh());
        assertEquals(144.50, quote.getLow());
        assertEquals(145.30, quote.getPrice());
        assertEquals(1000000, quote.getVolume());
        assertEquals("2023-07-23", new SimpleDateFormat("yyyy-MM-dd").format(quote.getLatestTradingDay()));
        assertEquals(144.00, quote.getPreviousClose());
        assertEquals(1.30, quote.getChange());
        assertEquals("0.90%", quote.getChangePercent());
        assertNotNull(quote.getTimestamp());
    }

    @Test
    void testFetchQuoteInfo_NoData() throws IOException {
        // Arrange
        when(client.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);
        when(response.isSuccessful()).thenReturn(true);
        when(response.body()).thenReturn(responseBody);

        String jsonResponse = "{ \"Global Quote\": {} }";
        when(responseBody.string()).thenReturn(jsonResponse);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            quoteHttpHelper.fetchQuoteInfo("");
        });
    }
}

