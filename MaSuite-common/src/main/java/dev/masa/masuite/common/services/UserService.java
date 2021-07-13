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
import java.util.concurrent.ExecutionException;

public class UserService implements IUserService<User> {

    private final Dao<User, UUID> userDao;

    @SneakyThrows
    public UserService(DatabaseService databaseService) {
        this.userDao = DaoManager.createDao(databaseService.connection(), User.class);
        this.userDao.setObjectCache(true);
        TableUtils.createTableIfNotExists(databaseService.connection(), User.class);
    }

    public Optional<User> user(UUID uniqueId) {
        CompletableFuture<Optional<User>> query = CompletableFuture.supplyAsync(() -> {
            try {
                return Optional.ofNullable(userDao.queryForId(uniqueId));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return Optional.empty();
        });


        try {
            return query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @SneakyThrows
    public void createOrUpdateUser(User user) {
        CompletableFuture.runAsync(() -> {
            try {
                userDao.createOrUpdate(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

}
