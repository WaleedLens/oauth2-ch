package org.example.authorization;

import org.example.database.QueryBuilder;
import org.example.database.Repository;
import org.example.database.Table;

import java.util.List;

public class AuthorizationRepository implements Repository<AuthorizationCode> {
    private final QueryBuilder queryBuilder = QueryBuilder.getInstance();

    @Override
    public long save(AuthorizationCode object) {
        Table table = queryBuilder.objectToTable(object);
        return queryBuilder.insert(table);
    }

    @Override
    public long delete(AuthorizationCode object) {
        Table table = queryBuilder.objectToTable(object);
        return queryBuilder.delete(table);
    }

    @Override
    public long update(AuthorizationCode object) {
        Table table = queryBuilder.objectToTable(object);
        return queryBuilder.update(table);
    }

    @Override
    public AuthorizationCode find(Long id) {
        AuthorizationCode authorizationCode = new AuthorizationCode();
        authorizationCode.setId(id);
        Table table = queryBuilder.objectToTable(authorizationCode);
        return queryBuilder.find(table, AuthorizationCode.class);
    }

    @Override
    public List<AuthorizationCode> findAll() {
        Table table = queryBuilder.objectToTable(new AuthorizationCode());
        return queryBuilder.findAll(table, AuthorizationCode.class);
    }

    @Override
    public AuthorizationCode findByField(String field, Object value) {
        AuthorizationCode authorizationCode = new AuthorizationCode();
        Table table = queryBuilder.objectToTable(authorizationCode);

        return queryBuilder.findByField(table, AuthorizationCode.class, field, value);
    }
}
