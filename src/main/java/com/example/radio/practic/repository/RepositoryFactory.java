package com.example.radio.practic.repository;

import com.example.radio.practic.config.Settings;
import com.example.radio.practic.domain.Entity;
import com.example.radio.practic.domain.EntityConverter;

public class RepositoryFactory {
    private final Settings settings;

    public RepositoryFactory(Settings settings) {
        this.settings = settings;
    }

    public <T extends Entity> Repository<T> createRepository(String key, EntityConverter<T> converter) {
        String repositoryType = settings.getProperty("Repository");

        switch (repositoryType.toLowerCase()) {
            case "sql":
                return createSQLRepository(key);
            case "binary":
                return new BinaryFileRepository<>(settings.getProperty(key));
            case "text":
                return new TextFileRepository<>(settings.getProperty(key), converter);
            default:
                throw new UnsupportedOperationException("Repository type not supported: " + repositoryType);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Entity> Repository<T> createSQLRepository(String tableName) {
        switch (tableName.toLowerCase()) {
            case "piese":
                return (Repository<T>) new SQLPiesaRepository();
            default:
                throw new IllegalArgumentException("Unknown table: " + tableName);
        }
    }
}
