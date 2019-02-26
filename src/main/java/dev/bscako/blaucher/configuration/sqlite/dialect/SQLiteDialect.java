package dev.bscako.blaucher.configuration.sqlite.dialect;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;

import java.sql.Types;

public class SQLiteDialect extends Dialect {
    public SQLiteDialect() {
        this.registerColumnType(Types.BIT, "integer");
        this.registerColumnType(Types.TINYINT, "tinyint");
        this.registerColumnType(Types.SMALLINT, "smallint");
        this.registerColumnType(Types.INTEGER, "integer");
        this.registerColumnType(Types.BIGINT, "integer");
        this.registerColumnType(Types.FLOAT, "float");
        this.registerColumnType(Types.REAL, "real");
        this.registerColumnType(Types.DOUBLE, "double");
        this.registerColumnType(Types.NUMERIC, "numeric");
        this.registerColumnType(Types.DECIMAL, "decimal");
        this.registerColumnType(Types.CHAR, "char");
        this.registerColumnType(Types.VARCHAR, "varchar");
        this.registerColumnType(Types.LONGVARCHAR, "longvarchar");
        this.registerColumnType(Types.DATE, "date");
        this.registerColumnType(Types.TIME, "time");
        this.registerColumnType(Types.TIMESTAMP, "timestamp");
        this.registerColumnType(Types.BINARY, "blob");
        this.registerColumnType(Types.VARBINARY, "blob");
        this.registerColumnType(Types.LONGVARBINARY, "blob");
        this.registerColumnType(Types.BLOB, "blob");
        this.registerColumnType(Types.CLOB, "clob");
        this.registerColumnType(Types.BOOLEAN, "integer");
    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new SQLiteIdentityColumnSupport();
    }

    @Override
    public boolean hasAlterTable() {
        return true;
    }

    @Override
    public boolean dropConstraints() {
        return false;
    }

    @Override
    public String getDropForeignKeyString() {
        return "";
    }

    @Override
    public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey, String referencedTable, String[] primaryKey, boolean referencesPrimaryKey) {
        return "";
    }

    @Override
    public String getAddPrimaryKeyConstraintString(String constraintName) {
        return "";
    }

    @Override
    public String getForUpdateString() {
        return "";
    }

    @Override
    public String getAddColumnString() {
        return "add column";
    }

    @Override
    public boolean supportsOuterJoinForUpdate() {
        return false;
    }

    @Override
    public boolean supportsIfExistsBeforeTableName() {
        return true;
    }

    @Override
    public boolean supportsCascadeDelete() {
        return false;
    }
}
