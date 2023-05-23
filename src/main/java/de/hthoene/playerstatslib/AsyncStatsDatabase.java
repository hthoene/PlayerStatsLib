package de.hthoene.playerstatslib;

import org.bukkit.configuration.file.YamlConfiguration;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncStatsDatabase extends StatsDatabase {

    @Override
    public void connect(YamlConfiguration configuration) throws SQLException {
        connect(
                configuration.getString("database.host"),
                configuration.getInt("database.port"),
                configuration.getString("database.database"),
                configuration.getString("database.username"),
                configuration.getString("database.password")
        );
    }

    @Override
    public void connect(String host, int port, String database, String username, String password) throws SQLException {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        connection = DriverManager.getConnection(url, username, password);
    }

    public CompletableFuture<Void> createTableAsync(String tableName) {
        return CompletableFuture.runAsync(() -> {
            try {
                createTable(tableName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> setPlayerValueAsync(String tableName, String uuid, int value) {
        return CompletableFuture.runAsync(() -> {
            try {
                setPlayerValue(tableName, uuid, value);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Integer> getPlayerValueAsync(String tableName, String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getPlayerValue(tableName, uuid);
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        });
    }

    public CompletableFuture<ResultSet> getTableDataAsync(String tableName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getTableData(tableName);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public CompletableFuture<List<PlayerEntry>> getTopPlayersAsync(String tableName, int limit) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getTopPlayers(tableName, limit);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }


}
