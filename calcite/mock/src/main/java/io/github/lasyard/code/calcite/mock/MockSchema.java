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

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.util.Map;

@RequiredArgsConstructor
public class MockSchema extends AbstractSchema {
    private final TableFlavor flavor;

    @Override
    protected Map<String, Table> getTableMap() {
        MockTable table;
        switch (flavor) {
            case SCANNABLE:
            default:
                table = new MockScannableTable();
                break;
            case FILTERABLE:
                table = new MockFilterableTable();
                break;
            case PROJECTABLE_FILTERABLE:
                table = new MockProjectableFilterableTable();
                break;
            case TRANSLATABLE:
                table = new MockTranslatableTable();
                break;
        }
        return ImmutableMap.<String, Table>builder()
            .put("mock", table)
            .build();
    }
}
