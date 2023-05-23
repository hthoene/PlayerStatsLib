package de.hthoene.playerstatslib;

import org.bukkit.configuration.file.YamlConfiguration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class StatsDatabase {
    protected Connection connection;

    public abstract void connect(String host, int port, String database, String username, String password) throws SQLException;

    public abstract void connect(YamlConfiguration configuration) throws SQLException;

    public void createTable(String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (uuid VARCHAR(36) PRIMARY KEY, p_value INT)";
        statement.executeUpdate(query);
        statement.close();
    }

    public void setPlayerValue(String tableName, String uuid, int value) throws SQLException {
        String query = "INSERT INTO " + tableName + " (uuid, p_value) VALUES (?, ?) ON DUPLICATE KEY UPDATE p_value = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, uuid);
        statement.setInt(2, value);
        statement.setInt(3, value);
        statement.executeUpdate();
        statement.close();
    }

    public int getPlayerValue(String tableName, String uuid) throws SQLException {
        String query = "SELECT p_value FROM " + tableName + " WHERE uuid = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, uuid);
        ResultSet resultSet = statement.executeQuery();

        int value = -1;

        if (resultSet.next()) {
            value = resultSet.getInt("p_value");
        }

        resultSet.close();
        statement.close();

        return value;
    }

    public ResultSet getTableData(String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM " + tableName;
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
    }

    public List<PlayerEntry> getTopPlayers(String tableName, int limit) throws SQLException {
        String query = "SELECT * FROM " + tableName + " ORDER BY p_value DESC LIMIT ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, limit);
        ResultSet resultSet = statement.executeQuery();

        List<PlayerEntry> topPlayers = new ArrayList<>();

        while (resultSet.next()) {
            String uuid = resultSet.getString("uuid");
            int value = resultSet.getInt("p_value");
            PlayerEntry entry = new PlayerEntry(uuid, value);
            topPlayers.add(entry);
        }

        resultSet.close();
        statement.close();

        return topPlayers;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}
