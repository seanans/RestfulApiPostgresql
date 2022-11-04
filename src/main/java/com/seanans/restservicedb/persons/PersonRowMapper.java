/*package com.seanans.restservicedb.persons;

import javax.swing.tree.RowMapper;
import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public abstract class PersonRowMapper implements RowMapper {

    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Person(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("surname")
        );
    }
}
*/