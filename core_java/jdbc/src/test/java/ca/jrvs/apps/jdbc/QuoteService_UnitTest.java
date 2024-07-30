package ca.jrvs.apps.jdbc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ca.jrvs.apps.jdbc.models.Quote;
import ca.jrvs.apps.jdbc.models.QuoteHttpHelper;
import dao.QuoteDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.QuoteService;

import java.sql.SQLException;
import java.util.Optional;

public class QuoteService_UnitTest
{

    @Mock
    private QuoteDao dao;

    @Mock
    private QuoteHttpHelper httpHelper;

    @InjectMocks
    private QuoteService quoteService;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchQuoteDataFromAPI_success() throws SQLException
    {
        Quote quote = new Quote();
        quote.setTicker("AAPL");

        when(httpHelper.fetchQuoteInfo("AAPL")).thenReturn(quote);
        when(dao.save(any(Quote.class))).thenReturn(quote);

        Optional<Quote> result = quoteService.fetchQuoteDataFromAPI("AAPL");
        assert (result.isPresent());
        assert (result.get().getTicker().equals("AAPL"));
    }

    @Test
    public void testFetchQuoteDataFromAPI_failure() throws SQLException
    {
        when(httpHelper.fetchQuoteInfo("INVALID")).thenReturn(null);

        Optional<Quote> result = quoteService.fetchQuoteDataFromAPI("INVALID");
        assert (!result.isPresent());
    }
}

