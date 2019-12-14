package dev.codesupport.web.common.service.data;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class DataPhysicalNamingStrategy implements PhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return convertToSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return convertToSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        String tableName = identifier.getText().replaceFirst("Entity$", "");
        return convertToSnakeCase(Identifier.toIdentifier(tableName));
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return convertToSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return convertToSnakeCase(identifier);
    }

    private Identifier convertToSnakeCase(final Identifier identifier) {
        Identifier returnIdentifier;
        if (identifier != null) {
            final String regex = "([a-z])([A-Z])";
            final String replacement = "$1_$2";
            final String newName = identifier.getText()
                    .replaceAll(regex, replacement)
                    .toLowerCase();
            returnIdentifier = Identifier.toIdentifier(newName);
        } else {
            returnIdentifier = null;
        }
        return returnIdentifier;
    }

}
