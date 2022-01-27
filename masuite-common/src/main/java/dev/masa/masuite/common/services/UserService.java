package dev.masa.masuite.common.services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.TableUtils;
import dev.masa.masuite.api.services.IUserService;
import dev.masa.masuite.common.models.user.User;
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

    public CompletableFuture<Optional<User>> user(String username) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SelectArg name = new SelectArg(username);
                return userDao.queryForEq("username", name).stream().findFirst();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return Optional.empty();
        });
    }

    public CompletableFuture<Optional<User>> user(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return Optional.ofNullable(userDao.queryForId(uniqueId));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return Optional.empty();
        });
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
