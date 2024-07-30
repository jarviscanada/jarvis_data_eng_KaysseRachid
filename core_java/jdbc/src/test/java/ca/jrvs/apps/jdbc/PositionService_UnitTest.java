package ca.jrvs.apps.jdbc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ca.jrvs.apps.jdbc.models.Position;
import dao.PositionDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.PositionService;

import java.sql.SQLException;
import java.util.Optional;

public class PositionService_UnitTest
{

    @Mock
    private PositionDao dao;

    @InjectMocks
    private PositionService positionService;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuy_success() throws SQLException
    {
        Position position = new Position("AAPL", 100, 150.00);

        when(dao.findById("AAPL")).thenReturn(Optional.of(position));
        when(dao.save(any(Position.class))).thenReturn(position);
        Position result = positionService.buy("AAPL", 50, 150.00);
        assert (result != null);
        assert (result.getNumOfShares() == 50);
    }

    @Test
    public void testBuy_failure() throws SQLException
    {
        Position position = new Position("AAPL", 100, 150.00);

        when(dao.findById("AAPL")).thenReturn(Optional.of(position));

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

        when(dao.findById("AAPL")).thenReturn(Optional.of(position));
        when(dao.save(any(Position.class))).thenReturn(position);

        positionService.sell("AAPL");
        verify(dao, times(1)).save(position);
        assert (position.getNumOfShares() == 0);
    }

    @Test
    public void testSell_failure() throws SQLException
    {
        when(dao.findById("INVALID")).thenReturn(Optional.empty());

        try {
            positionService.sell("INVALID");
        } catch (IllegalArgumentException e) {
            assert (e.getMessage().equals("Ticker does not exist"));
        }
    }
}
