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
import java.util.function.BiConsumer;

public class WarpService implements IWarpService<Warp> {

    private final Dao<Warp, UUID> warpDao;

    @SneakyThrows
    public WarpService(DatabaseService databaseService) {
        this.warpDao = DaoManager.createDao(databaseService.connection(), Warp.class);
        this.warpDao.setObjectCache(true);
        TableUtils.createTableIfNotExists(databaseService.connection(), Warp.class);
    }

    @Override
    public CompletableFuture<Optional<Warp>> warp(String name) {
        return CompletableFuture.supplyAsync(() -> {
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
    }

    @Override
    public void createOrUpdateWarp(Warp warp, BiConsumer<Boolean, Boolean> done) {
        this.warp(warp.name()).thenAcceptAsync((warpQuery) -> {
            try {
                // Search if home is present, then update or create

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
    public CompletableFuture<Boolean> deleteWarp(Warp warp) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.warpDao.delete(warp);
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<List<Warp>> warps() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return this.warpDao.queryBuilder()
                        .orderBy("name", true)
                        .query();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return new ArrayList<>();
            }
        });
    }
}
