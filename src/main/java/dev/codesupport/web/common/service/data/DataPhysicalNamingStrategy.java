package dev.codesupport.web.common.service.data;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Class used for defining a naming strategy for hibernate mappings
 *
 * <p>This is set in the application.yml property file via the property:
 * spring.jpa.hibernate.naming.physical-strategy</p>
 */
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

    /**
     * Defines a strategy for table names
     *
     * @param identifier      Identifier associated to the table name
     * @param jdbcEnvironment ?
     * @return The new table name conforming to the defined logic.
     */
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

    /**
     * Defines a strategy for column names
     *
     * @param identifier      Identifier associated to the column name
     * @param jdbcEnvironment ?
     * @return The new column name conforming to the defined logic.
     */
    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        String columnName = identifier.getText()
                .replaceFirst("Entity_id$", "Id"); // Remove Entity joiner from mapping table column names
        return convertToSnakeCase(Identifier.toIdentifier(columnName));
    }

    /**
     * Converts an identifier's string to Snake_Case, returning a new identifier.
     *
     * @param identifier The identifier to convert
     * @return A new identifier with the converted string.
     */
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
