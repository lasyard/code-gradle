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
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.schema.ProjectableFilterableTable;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class MockProjectableFilterableTable extends MockTable implements ProjectableFilterableTable {
    @Override
    public Enumerable<Object[]> scan(DataContext dataContext, List<RexNode> list, final int[] integers) {
        log.info("scan() called.");
        log.info("columns = {}", integers);
        if (integers == null) {
            // It does not matter that filtering is not implemented here
            return new AbstractEnumerable<Object[]>() {
                @Override
                public Enumerator<Object[]> enumerator() {
                    return new MockEnumerator();
                }
            };
        }
        return new AbstractEnumerable<Object[]>() {
            @Override
            public Enumerator<Object[]> enumerator() {
                // Projecting is a must.
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
