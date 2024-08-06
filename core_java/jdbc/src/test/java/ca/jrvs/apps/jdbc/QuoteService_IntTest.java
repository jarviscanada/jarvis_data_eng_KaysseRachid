package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.models.Quote;
import ca.jrvs.apps.jdbc.models.QuoteHttpHelper;
import dao.QuoteDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.QuoteService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class QuoteService_IntTest {

    private QuoteDao dao;
    private QuoteHttpHelper httpHelper;
    private QuoteService quoteService;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException
    {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/stock_quote", "postgres", "postgres");
        dao = new QuoteDao(connection);
        httpHelper = new QuoteHttpHelper();
        quoteService = new QuoteService();
        quoteService.setDao(dao);
        quoteService.setHttpHelper(httpHelper);
    }

    @Test
    public void testFetchQuoteDataFromAPI() throws SQLException {
        Optional<Quote> result = quoteService.fetchQuoteDataFromAPI("AAPL");
        assert(result.isPresent());
        assert(result.get().getTicker().equals("AAPL"));
    }
}


