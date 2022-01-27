package dev.masa.masuite.common.services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.TableUtils;
import dev.masa.masuite.api.services.IHomeService;
import dev.masa.masuite.common.models.home.Home;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class HomeService implements IHomeService<Home> {

    private final Dao<Home, UUID> homeDao;

    @SneakyThrows
    public HomeService(DatabaseService databaseService) {
        this.homeDao = DaoManager.createDao(databaseService.connection(), Home.class);
        this.homeDao.setObjectCache(true);
        TableUtils.createTableIfNotExists(databaseService.connection(), Home.class);
    }

    @Override
    public CompletableFuture<Optional<Home>> home(UUID ownerId, String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SelectArg homeName = new SelectArg(name);
                QueryBuilder<Home, UUID> queryBuilder = this.homeDao.queryBuilder()
                        .orderBy("name", true)
                        .where().in("owner", ownerId)
                        .and().in("name", homeName)
                        .queryBuilder();
                return Optional.ofNullable(homeDao.queryForFirst(queryBuilder.prepare()));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return Optional.empty();
        });
    }

    @Override
    public void createOrUpdateHome(Home home, BiConsumer<Boolean, Boolean> done) {
        this.home(home.owner(), home.name()).thenAcceptAsync((homeQuery) -> {
            try {
                if (homeQuery.isPresent()) {
                    homeQuery.get().location(home.location());
                    this.homeDao.update(homeQuery.get());
                    done.accept(true, false);
                } else {
                    this.homeDao.create(home);
                    done.accept(true, true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                done.accept(false, false);
            }
        });
    }


    @Override
    public CompletableFuture<Boolean> deleteHome(Home home) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.homeDao.delete(home);
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<List<Home>> homes(UUID ownerId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                QueryBuilder<Home, UUID> queryBuilder = this.homeDao.queryBuilder()
                        .orderBy("name", true)
                        .where().in("owner", ownerId).queryBuilder();
                return this.homeDao.query(queryBuilder.prepare()).stream().toList();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return new ArrayList<>();
            }
        });
    }
}
