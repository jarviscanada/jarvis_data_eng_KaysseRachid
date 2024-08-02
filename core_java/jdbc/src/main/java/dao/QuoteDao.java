package dao;

import ca.jrvs.apps.jdbc.models.Quote;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String>
{
    private Connection connection;

    public QuoteDao() throws SQLException
    {

    }

    public QuoteDao(Connection connection)
    {
        this.connection = connection;
    }

    public Quote save(Quote entity) throws IllegalArgumentException, SQLException
    {
        String sql = "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading, previous_close, change, change_percent, timestamp) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getTicker());
            statement.setDouble(2, entity.getOpen());
            statement.setDouble(3, entity.getHigh());
            statement.setDouble(4, entity.getLow());
            statement.setDouble(5, entity.getPrice());
            statement.setInt(6, entity.getVolume());
            statement.setDate(7, entity.getLatestTradingDay());
            statement.setDouble(8, entity.getPreviousClose());
            statement.setDouble(9, entity.getChange());
            statement.setString(10, entity.getChangePercent());
            statement.setTimestamp(11, entity.getTimestamp());
            return entity;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to fetch data from dao", e);
        }
    }

    public Optional<Quote> findById(String id) throws IllegalArgumentException, SQLException
    {
        String sql = "SELECT * FROM quote WHERE symbol = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Quote quote = new Quote();
                quote.setTicker(result.getString("symbol"));
                quote.setOpen(result.getDouble("open"));
                quote.setHigh(result.getDouble("high"));
                quote.setLow(result.getDouble("low"));
                quote.setPrice(result.getDouble("price"));
                quote.setVolume(result.getInt("volume"));
                quote.setLatestTradingDay(result.getDate("latest_trading_day"));
                quote.setPreviousClose(result.getDouble("previous_close"));
                quote.setChange(result.getDouble("change"));
                quote.setChangePercent(result.getString("change_percent"));
                quote.setTimestamp(result.getTimestamp("timestamp"));
                return Optional.of(quote);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to fetch data from dao", e);
        }
    }

    public Iterable<Quote> findAll() throws SQLException
    {
        String sql = "SELECT * FROM quote";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ArrayList<Quote> quotes = new ArrayList<>();
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                quotes.add(new Quote(
                        result.getString(1),
                        result.getDouble(2),
                        result.getDouble(3),
                        result.getDouble(4),
                        result.getDouble(5),
                        result.getInt(6),
                        result.getDate(7),
                        result.getDouble(8),
                        result.getDouble(9),
                        result.getString(10),
                        result.getTimestamp(11)
                ));
            }
            return quotes;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to fetch data from dao", e);
        }
    }

    public void deleteById(String id) throws SQLException
    {
        String sql = "DELETE FROM quote WHERE symbol = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new IllegalArgumentException("No record found with ID: " + id);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to delete record with ID: " + id, e);
        }
    }

    public void deleteAll() throws SQLException
    {
        String sql = "DELETE FROM quote";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int rowsAffected = statement.executeUpdate();

            System.out.println(rowsAffected + " records deleted successfully.");
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to delete all records", e);
        }
    }

}
