/*
 * Copyright 2020 lasyard@github.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Table;
import org.apache.calcite.sql.type.SqlTypeFactoryImpl;
import org.apache.calcite.sql.type.SqlTypeName;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class MockSchemaFactoryTest {
    private static Connection connection;

    @BeforeAll
    public static void setupAll() throws SQLException, ClassNotFoundException {
        Class.forName("org.apache.calcite.jdbc.Driver");
        Properties props = new Properties();
        props.put("model", MockSchemaFactoryTest.class.getResource("/models/model.yml").getPath());
        connection = DriverManager.getConnection("jdbc:calcite:", props);
    }

    @AfterAll
    public static void cleanUpAll() throws SQLException {
        connection.close();
    }

    @Test
    public void testGetTable() throws SQLException {
        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        assertThat(rootSchema.getName()).isEmpty();
        assertThat(rootSchema.getSubSchemaNames()).contains("mock");
        SchemaPlus schema = rootSchema.getSubSchema("mock");
        assertThat(schema.getTableNames()).contains("mock");
        Table table = schema.getTable("mock");
        assertThat(table.getJdbcTableType()).isEqualTo(Schema.TableType.TABLE);
        RelDataType dataType = table.getRowType(new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT));
        List<RelDataTypeField> fields = dataType.getFieldList();
        assertThat(fields).extracting("name")
            .contains("id", "name");
        assertThat(dataType.getField("id", true, false).getType().getSqlTypeName())
            .isEqualTo(SqlTypeName.INTEGER);
        assertThat(dataType.getField("name", true, false).getType().getSqlTypeName())
            .isEqualTo(SqlTypeName.VARCHAR);
        assertThat(dataType.getField("name", true, false).getType().getPrecision())
            .isEqualTo(128);
    }
}
