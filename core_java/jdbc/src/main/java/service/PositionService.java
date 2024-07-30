package service;

import ca.jrvs.apps.jdbc.models.Position;
import ca.jrvs.apps.jdbc.models.Quote;
import dao.PositionDao;
import dao.QuoteDao;

import java.sql.SQLException;
import java.util.Optional;

public class PositionService
{
    private PositionDao positionDao;
    private QuoteDao quoteDao;

    public PositionDao getPositionDao()
    {
        return positionDao;
    }

    public void setPositionDao(PositionDao dao)
    {
        this.positionDao = dao;
    }

    public QuoteDao getQuoteDao()
    {
        return quoteDao;
    }

    public void setQuoteDao(QuoteDao quoteDao)
    {
        this.quoteDao = quoteDao;
    }

    /**
     * Processes a buy order and updates the database accordingly
     *
     * @param ticker
     * @param numberOfShares
     * @param price
     * @return The position in our database after processing the buy
     */
    public Position buy(String ticker, int numberOfShares, double price) throws SQLException
    {
        Optional<Quote> quote = quoteDao.findById(ticker);
        if (quote.isPresent()) {
            int availableShares = quote.get().getVolume();
            if (numberOfShares <= availableShares) {
                Position entity = new Position(ticker, numberOfShares, price);
                positionDao.save(entity);
                return entity;
            } else {
                throw new IllegalArgumentException("Not enough available shares to buy");
            }
        } else {
            throw new IllegalArgumentException("Ticker symbol does not exist");
        }
    }

    /**
     * Sells all shares of the given ticker symbol
     *
     * @param ticker
     */
    public void sell(String ticker) throws SQLException
    {
        Optional<Position> position = positionDao.findById(ticker);
        if (position.isPresent()) {
            Position entity = position.get();
            entity.setNumOfShares(0);
            positionDao.save(entity);
        } else {
            throw new IllegalArgumentException("Ticker does not exist");
        }
    }
}
