package org.example.token;

import org.example.database.QueryBuilder;
import org.example.database.Repository;
import org.example.database.Table;

import java.util.List;

public class TokenRepository implements Repository<Token> {
    private final QueryBuilder queryBuilder = QueryBuilder.getInstance();

    @Override
    public long save(Token object) {
        Table table = queryBuilder.objectToTable(object);
        return queryBuilder.insert(table);
    }

    @Override
    public long delete(Token object) {
        Table table = queryBuilder.objectToTable(object);
        return queryBuilder.delete(table);
    }

    @Override
    public long update(Token object) {
        Table table = queryBuilder.objectToTable(object);
        return queryBuilder.update(table);
    }

    @Override
    public Token find(Long id) {
        Token c = new Token();
        c.setId(id);
        Table table = queryBuilder.objectToTable(c);
        return queryBuilder.find(table, Token.class);
    }

    @Override
    public List<Token> findAll() {
        Table table = queryBuilder.objectToTable(new Token());
        return queryBuilder.findAll(table, Token.class);
    }

    @Override
    public Token findByField(String field, Object value) {
        Token token = new Token();
        Table table = queryBuilder.objectToTable(token);

        return queryBuilder.findByField(table, Token.class, field, value);
    }
}
