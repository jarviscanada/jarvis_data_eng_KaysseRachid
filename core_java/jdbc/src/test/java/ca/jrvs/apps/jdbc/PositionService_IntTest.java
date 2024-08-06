package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.models.Position;
import dao.PositionDao;
import dao.QuoteDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.PositionService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class PositionService_IntTest {

    private PositionDao positionDao;
    private QuoteDao quoteDao;
    private PositionService positionService;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException{
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/stock_quote", "postgres", "postgres");
        positionDao = new PositionDao(connection);
        quoteDao = new QuoteDao(connection);
        positionService = new PositionService();
        positionService.setPositionDao(positionDao);
        positionService.setQuoteDao(quoteDao);
    }

    @Test
    public void testBuyAndSell() throws SQLException {
        Position boughtPosition = positionService.buy("AAPL", 50, 150.00);
        assert(boughtPosition.getNumOfShares() == 50);

        positionService.sell("AAPL");
        Optional<Position> soldPosition = positionDao.findById("AAPL");
        assert(soldPosition.isPresent());
        assert(soldPosition.get().getNumOfShares() == 0);
    }
}


