package dev.masa.masuite.common.services;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class DatabaseService {

    @Getter
    private final JdbcPooledConnectionSource connection;

    @SneakyThrows
    public DatabaseService(String address, int port, String databaseName, String username, String password) {
        this.connection = new JdbcPooledConnectionSource("jdbc:mysql://" + address + ":" + port + "/" + databaseName, username, password);
    }

}
