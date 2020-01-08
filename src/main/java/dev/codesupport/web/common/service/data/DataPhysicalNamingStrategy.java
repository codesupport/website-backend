package dev.codesupport.web.common.service.data;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Arrays;
import java.util.stream.Collectors;

//unused - This is called dynamically by spring boot, reference set by app property.
@SuppressWarnings("unused")
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
        String tableName = Arrays.stream(identifier.getText()
                .split("Entity_")) // Break up mapping table names
                .map(StringUtils::capitalize) // Set appropriate camelCase
                .collect(Collectors.joining("To")) // Add mapping table "To" connecting syntax
                .replaceFirst("Entity$", ""); // Remove any "Entity" suffix
        return convertToSnakeCase(Identifier.toIdentifier(tableName));
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return convertToSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        String columnName = identifier.getText()
                .replaceFirst("Entity_id$", "Id"); // Remove Entity joiner from mapping table column names
        return convertToSnakeCase(Identifier.toIdentifier(columnName));
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
