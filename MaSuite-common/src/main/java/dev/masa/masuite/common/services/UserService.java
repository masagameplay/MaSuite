package dev.masa.masuite.common.services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import dev.masa.masuite.api.services.IUserService;
import dev.masa.masuite.common.models.User;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserService implements IUserService<User> {

    private final Dao<User, UUID> userDao;

    @SneakyThrows
    public UserService(DatabaseService databaseService) {
        this.userDao = DaoManager.createDao(databaseService.connection(), User.class);
        this.userDao.setObjectCache(true);
        TableUtils.createTableIfNotExists(databaseService.connection(), User.class);
    }

    @SneakyThrows
    public Optional<User> user(UUID uniqueId) {
        CompletableFuture<Optional<User>> query = CompletableFuture.supplyAsync(() -> {
            try {
                return Optional.of(userDao.queryForId(uniqueId));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        });

        return query.get();
    }

    @SneakyThrows
    public void user(User user) {
        CompletableFuture.runAsync(() -> {
            try {
                userDao.createOrUpdate(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

}
