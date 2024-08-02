package dao;

import ca.jrvs.apps.jdbc.models.Position;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String>
{
    private Connection connection;

    public PositionDao() throws SQLException
    {
        connect();
    }

    public PositionDao(Connection connection)
    {
        this.connection = connection;
    }

    private void connect() throws SQLException
    {

    }

    public Position save(Position entity) throws IllegalArgumentException, SQLException
    {
        String sql = "INSERT INTO position (symbol, number_of_shares, value_paid) VALUES (?, ?, ?)" +
                "ON CONFLICT (symbol) DO UPDATE SET number_of_shares = EXCLUDED.number_of_shares, value_paid = EXCLUDED.value_paid";;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getTicker());
            statement.setInt(2, entity.getNumOfShares());
            statement.setDouble(3, entity.getValuePaid());
            statement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to fetch data", e);
        }
    }

    public Optional<Position> findById(String id) throws IllegalArgumentException, SQLException
    {
        String sql = "SELECT * FROM position WHERE symbol = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Position position = new Position();
                position.setTicker(result.getString("symbol"));
                position.setNumOfShares(result.getInt("number_of_shares"));
                position.setValuePaid(result.getDouble("value_paid"));
                return Optional.of(position);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to fetch data", e);
        }
    }

    public Iterable<Position> findAll() throws SQLException
    {
        String sql = "SELECT * FROM position";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ArrayList<Position> positions = new ArrayList<>();
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                positions.add(new Position(
                        result.getString(1),
                        result.getInt(2),
                        result.getDouble(3)
                ));
            }
            return positions;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to fetch data", e);
        }
    }

    public void deleteById(String id) throws SQLException
    {
        String sql = "DELETE FROM position WHERE symbol = ?";

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
        String sql = "DELETE * FROM position";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int rowsAffected = statement.executeUpdate();

            System.out.println(rowsAffected + " records deleted successfully.");
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to delete all records", e);
        }
    }
}
