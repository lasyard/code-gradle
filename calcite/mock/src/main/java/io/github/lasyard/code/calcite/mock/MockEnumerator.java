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

import org.apache.calcite.linq4j.Enumerator;

public class MockEnumerator implements Enumerator<Object[]> {
    private final Object[][] datum = {
        {1, "Alice"},
        {2, "Betty"},
    };

    private int pos = -1;

    @Override
    public Object[] current() {
        return datum[pos];
    }

    @Override
    public boolean moveNext() {
        pos++;
        return pos < datum.length;
    }

    @Override
    public void reset() {
        pos = -1;
    }

    @Override
    public void close() {
    }
}
