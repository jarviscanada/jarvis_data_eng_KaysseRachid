package service;

import ca.jrvs.apps.jdbc.models.QuoteHttpHelper;
import ca.jrvs.apps.jdbc.models.Quote;
import dao.QuoteDao;

import java.sql.SQLException;
import java.util.Optional;

public class QuoteService
{
    private QuoteDao dao;
    private QuoteHttpHelper httpHelper;

    public QuoteDao getDao()
    {
        return dao;
    }

    public QuoteHttpHelper getHttpHelper()
    {
        return httpHelper;
    }

    public void setDao(QuoteDao dao)
    {
        this.dao = dao;
    }

    public void setHttpHelper(QuoteHttpHelper httpHelper)
    {
        this.httpHelper = httpHelper;
    }

    /**
     * Fetches latest quote data from endpoint
     *
     * @param ticker
     * @return Latest quote information or empty optional if ticker symbol not found
     */
    public Optional<Quote> fetchQuoteDataFromAPI(String ticker)
    {
        try {
            Optional<Quote> fetchedQuote = Optional.ofNullable(httpHelper.fetchQuoteInfo(ticker));

            if (fetchedQuote.isPresent()) {
                Quote quote = fetchedQuote.get();

                if (quote.getTicker().equalsIgnoreCase(ticker)) {
                    dao.save(quote);
                    return Optional.of(quote);
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to fetch data from service: ", e);
        }
        return Optional.empty();
    }
}
