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

package io.github.lasyard.code.calcite.mock;

import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.schema.FilterableTable;
import org.apache.calcite.schema.ProjectableFilterableTable;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeName;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class MockTable extends AbstractTable
    implements ScannableTable, FilterableTable, ProjectableFilterableTable {
    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        return typeFactory.createStructType(
            Arrays.asList(
                typeFactory.createSqlType(SqlTypeName.INTEGER),
                typeFactory.createSqlType(SqlTypeName.VARCHAR, 128)
            ),
            Arrays.asList(
                "id",
                "name"
            )
        );
    }

    @Override
    public Enumerable<Object[]> scan(DataContext dataContext) {
        log.info("{}.scan() called.", ScannableTable.class.getSimpleName());
        return new AbstractEnumerable<Object[]>() {
            @Override
            public Enumerator<Object[]> enumerator() {
                return new MockEnumerator();
            }
        };
    }

    @Override
    public Enumerable<Object[]> scan(DataContext dataContext, List<RexNode> list) {
        log.info("{}.scan() called.", FilterableTable.class.getSimpleName());
        // Filtering is not a must.
        return scan(dataContext);
    }

    @Override
    public Enumerable<Object[]> scan(DataContext dataContext, List<RexNode> list, final int[] integers) {
        log.info("{}.scan() called.", ProjectableFilterableTable.class.getSimpleName());
        log.info("columns = {}", integers);
        if (integers == null) {
            return scan(dataContext, list);
        }
        // Projecting is a must.
        return new AbstractEnumerable<Object[]>() {
            @Override
            public Enumerator<Object[]> enumerator() {
                return new MockEnumerator() {
                    @Override
                    public Object[] current() {
                        final Object[] result = super.current();
                        return Arrays.stream(integers).mapToObj(i -> result[i]).toArray();
                    }
                };
            }
        };
    }
}
