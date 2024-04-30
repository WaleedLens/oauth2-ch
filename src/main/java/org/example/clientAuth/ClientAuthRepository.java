package org.example.clientAuth;


import org.example.database.QueryBuilder;
import org.example.database.Repository;
import org.example.database.Table;

import java.util.List;

public class ClientAuthRepository implements Repository<ClientAuth> {
    private final QueryBuilder queryBuilder = QueryBuilder.getInstance();

    @Override
    public long save(ClientAuth object) {
        Table table = queryBuilder.objectToTable(object);
        return queryBuilder.insert(table);

    }

    @Override
    public long delete(ClientAuth object) {
        Table table = queryBuilder.objectToTable(object);
        return queryBuilder.delete(table);
    }

    @Override
    public long update(ClientAuth object) {
        Table table = queryBuilder.objectToTable(object);
        return queryBuilder.update(table);
    }

    @Override
    public ClientAuth find(Long id) {
        Table table = queryBuilder.objectToTable(new ClientAuth());
        return queryBuilder.find(table, ClientAuth.class);
    }

    @Override
    public List<ClientAuth> findAll() {
        Table table = queryBuilder.objectToTable(new ClientAuth());
        return queryBuilder.findAll(table, ClientAuth.class);
    }

    @Override
    public ClientAuth findByField(String field, Object value) {

        return null;
    }
}
