package dev.masa.masuite.common.services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.TableUtils;
import dev.masa.masuite.api.services.IHomeService;
import dev.masa.masuite.common.models.Home;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class HomeService implements IHomeService<Home> {

    private final Dao<Home, UUID> homeDao;

    @SneakyThrows
    public HomeService(DatabaseService databaseService) {
        this.homeDao = DaoManager.createDao(databaseService.connection(), Home.class);
        this.homeDao.setObjectCache(true);
        TableUtils.createTableIfNotExists(databaseService.connection(), Home.class);
    }

    @Override
    public Optional<Home> home(UUID ownerId, String name) {
        CompletableFuture<Optional<Home>> query = CompletableFuture.supplyAsync(() -> {
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

        try {
            return query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public void createOrUpdateHome(Home home, BiConsumer<Boolean, Boolean> done) {
        CompletableFuture.runAsync(() -> {
            try {
                // Search if home is present, then update or create
                Optional<Home> homeQuery = this.home(home.owner(), home.name());
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
    public void deleteHome(Home home, Consumer<Boolean> done) {
        CompletableFuture.runAsync(() -> {
            try {
                this.homeDao.delete(home);
                done.accept(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
                done.accept(false);
            }
        });
    }

    @Override
    public List<Home> homes(UUID ownerId) {
        CompletableFuture<List<Home>> homesQuery = CompletableFuture.supplyAsync(() -> {
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
        try {
            return homesQuery.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
