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

public class SpawnService implements ISpawnService<Spawn> {

    private final Dao<Spawn, UUID> spawnDao;

    @SneakyThrows
    public SpawnService(DatabaseService databaseService) {
        this.spawnDao = DaoManager.createDao(databaseService.connection(), Spawn.class);
        this.spawnDao.setObjectCache(true);
        TableUtils.createTableIfNotExists(databaseService.connection(), Spawn.class);
    }

    @Override
    public CompletableFuture<Optional<Spawn>> spawn(String serverName, boolean defaultSpawn) {

        return CompletableFuture.supplyAsync(() -> {
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
    }

    @Override
    public CompletableFuture<Optional<Spawn>> spawn(boolean defaultSpawn) {
        return CompletableFuture.supplyAsync(() -> {
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
    }

    @Override
    public void createOrUpdateSpawn(Spawn spawn, BiConsumer<Boolean, Boolean> done) {
        this.spawn(spawn.location().server(), spawn.isDefaultSpawn()).thenAcceptAsync((spawnQuery) -> {
            try {
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
    public CompletableFuture<Boolean> deleteSpawn(Spawn spawn) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.spawnDao.delete(spawn);
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        });
    }
}
