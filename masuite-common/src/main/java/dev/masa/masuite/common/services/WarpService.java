package dev.masa.masuite.common.services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.TableUtils;
import dev.masa.masuite.api.services.IWarpService;
import dev.masa.masuite.common.models.warp.Warp;
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

public class WarpService implements IWarpService<Warp> {

    private final Dao<Warp, UUID> warpDao;

    @SneakyThrows
    public WarpService(DatabaseService databaseService) {
        this.warpDao = DaoManager.createDao(databaseService.connection(), Warp.class);
        this.warpDao.setObjectCache(true);
        TableUtils.createTableIfNotExists(databaseService.connection(), Warp.class);
    }

    @Override
    public Optional<Warp> warp(String name) {
        CompletableFuture<Optional<Warp>> query = CompletableFuture.supplyAsync(() -> {
            try {
                SelectArg warpName = new SelectArg(name);
                QueryBuilder<Warp, UUID> queryBuilder = this.warpDao.queryBuilder()
                        .orderBy("name", true)
                        .where().in("name", warpName)
                        .queryBuilder();
                return Optional.ofNullable(warpDao.queryForFirst(queryBuilder.prepare()));
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
    public void createOrUpdateWarp(Warp warp, BiConsumer<Boolean, Boolean> done) {
        CompletableFuture.runAsync(() -> {
            try {
                // Search if home is present, then update or create
                Optional<Warp> warpQuery = this.warp(warp.name());
                if (warpQuery.isPresent()) {
                    warpQuery.get().location(warp.location());
                    this.warpDao.update(warpQuery.get());
                    done.accept(true, false);
                } else {
                    this.warpDao.create(warp);
                    done.accept(true, true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                done.accept(false, false);
            }
        });
    }


    @Override
    public void deleteWarp(Warp warp, Consumer<Boolean> done) {
        CompletableFuture.runAsync(() -> {
            try {
                this.warpDao.delete(warp);
                done.accept(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
                done.accept(false);
            }
        });
    }

    @Override
    public List<Warp> warps() {
        CompletableFuture<List<Warp>> warpsQuery = CompletableFuture.supplyAsync(() -> {
            try {
                return this.warpDao.queryBuilder()
                        .orderBy("name", true)
                        .query();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return new ArrayList<>();
            }
        });
        try {
            return warpsQuery.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
