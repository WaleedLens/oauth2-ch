package org.example.client;

import org.example.database.QueryBuilder;
import org.example.database.Repository;
import org.example.database.Table;

import java.util.List;

public class ClientRepository implements Repository<Client> {
    private final QueryBuilder queryBuilder = QueryBuilder.getInstance();

    @Override
    public long save(Client object) {
        Table table = queryBuilder.objectToTable(object);
        return queryBuilder.insert(table);
    }

    @Override
    public long delete(Client object) {
        Table table = queryBuilder.objectToTable(object);
        return queryBuilder.delete(table);
    }

    @Override
    public long update(Client object) {
        Table table = queryBuilder.objectToTable(object);
        return queryBuilder.update(table);
    }

    @Override
    public Client find(Long id) {
        Client c = new Client();
        c.setId(id);
        Table table = queryBuilder.objectToTable(c);
        return queryBuilder.find(table, Client.class);
    }

    @Override
    public List<Client> findAll() {
        Table table = queryBuilder.objectToTable(new Client());
        return queryBuilder.findAll(table, Client.class);
    }

    @Override
    public Client findByField(String field, Object value) {
        Client c = new Client();
        Table table = queryBuilder.objectToTable(c);
        return queryBuilder.findByField(table, Client.class, field, value);
    }
}
