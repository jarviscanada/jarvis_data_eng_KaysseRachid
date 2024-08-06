package ca.jrvs.apps.jdbc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ca.jrvs.apps.jdbc.models.Position;
import ca.jrvs.apps.jdbc.models.Quote;
import dao.PositionDao;
import dao.QuoteDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.PositionService;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

public class PositionService_UnitTest
{

    @Mock
    private PositionDao positionDao;
    @Mock
    private QuoteDao quoteDao;

    @InjectMocks
    private PositionService positionService;
    @Mock
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuy_success() throws SQLException
    {
        Position position = new Position("AAPL", 100, 150.00);
        Quote quote = new Quote(
                "AAPL",
                216.96,
                219.3,
                215.75,
                218.24,
                36311778,
                new Date(2024 - 1900, 6, 29), // Date(year, month, day) - months are 0-indexed, hence 6 for July
                217.96,
                0.28,
                "0.1285%",
                Timestamp.valueOf("2024-07-30 16:42:39")
        );

        when(quoteDao.findById(any(String.class))).thenReturn(Optional.of(quote));
        when(positionDao.findById("AAPL")).thenReturn(Optional.of(position));
        when(positionDao.save(any(Position.class))).thenReturn(position);
        Position result = positionService.buy("AAPL", 50, 150.00);
        assert (result != null);
        assert (result.getNumOfShares() == 50);
    }

    @Test
    public void testBuy_failure() throws SQLException
    {
        Position position = new Position("AAPL", 100, 150.00);
        Quote quote = new Quote(
                "AAPL",
                216.96,
                219.3,
                215.75,
                218.24,
                36311778,
                new Date(2024 - 1900, 6, 29), // Date(year, month, day) - months are 0-indexed, hence 6 for July
                217.96,
                0.28,
                "0.1285%",
                Timestamp.valueOf("2024-07-30 16:42:39")
        );

        when(quoteDao.findById(any(String.class))).thenReturn(Optional.of(quote));
        when(positionDao.findById("AAPL")).thenReturn(Optional.of(position));

        try {
            positionService.buy("AAPL", 150, 150.00);
        } catch (IllegalArgumentException e) {
            assert (e.getMessage().equals("Not enough available shares to buy"));
        }
    }

    @Test
    public void testSell_success() throws SQLException
    {
        Position position = new Position("AAPL", 100, 150.00);

        when(positionDao.findById("AAPL")).thenReturn(Optional.of(position));
        when(positionDao.save(any(Position.class))).thenReturn(position);

        positionService.sell("AAPL");
        verify(positionDao, times(1)).save(position);
        assert (position.getNumOfShares() == 0);
    }

    @Test
    public void testSell_failure() throws SQLException
    {
        when(positionDao.findById("INVALID")).thenReturn(Optional.empty());

        try {
            positionService.sell("INVALID");
        } catch (IllegalArgumentException e) {
            assert (e.getMessage().equals("Ticker does not exist"));
        }
    }
}
