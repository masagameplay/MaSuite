package dev.masa.masuite.common.services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.TableUtils;
import dev.masa.masuite.api.services.ISpawnService;
import dev.masa.masuite.common.models.teleport.Spawn;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SpawnService implements ISpawnService<Spawn> {

    private final Dao<Spawn, UUID> spawnDao;

    @SneakyThrows
    public SpawnService(DatabaseService databaseService) {
        this.spawnDao = DaoManager.createDao(databaseService.connection(), Spawn.class);
        this.spawnDao.setObjectCache(true);
        TableUtils.createTableIfNotExists(databaseService.connection(), Spawn.class);
    }

    @Override
    public Optional<Spawn> spawn(String serverName, boolean defaultSpawn) {
        CompletableFuture<Optional<Spawn>> query = CompletableFuture.supplyAsync(() -> {
            try {
                SelectArg server = new SelectArg(serverName);
                QueryBuilder<Spawn, UUID> queryBuilder = this.spawnDao.queryBuilder()
                        .where().in("server", server)
                        .and().in("defaultSpawn", defaultSpawn)
                        .queryBuilder();
                return Optional.ofNullable(spawnDao.queryForFirst(queryBuilder.prepare()));
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
    public Optional<Spawn> spawn(boolean defaultSpawn) {
        CompletableFuture<Optional<Spawn>> query = CompletableFuture.supplyAsync(() -> {
            try {
                QueryBuilder<Spawn, UUID> queryBuilder = this.spawnDao.queryBuilder()
                        .where().in("defaultSpawn", defaultSpawn)
                        .queryBuilder();
                return Optional.ofNullable(spawnDao.queryForFirst(queryBuilder.prepare()));
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
    public void createOrUpdateSpawn(Spawn spawn, BiConsumer<Boolean, Boolean> done) {
        CompletableFuture.runAsync(() -> {
            try {
                // Search if spawn is present, then update or create
                Optional<Spawn> spawnQuery = this.spawn(spawn.location().server(), spawn.isDefaultSpawn());
                if (spawnQuery.isPresent()) {
                    spawnQuery.get().location(spawn.location());
                    this.spawnDao.update(spawnQuery.get());
                    done.accept(true, false);
                } else {
                    this.spawnDao.create(spawn);
                    done.accept(true, true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                done.accept(false, false);
            }
        });
    }

    @Override
    public void deleteSpawn(Spawn spawn, Consumer<Boolean> done) {
        CompletableFuture.runAsync(() -> {
            try {
                this.spawnDao.delete(spawn);
                done.accept(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
                done.accept(false);
            }
        });
    }
}
