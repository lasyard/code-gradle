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
import org.apache.calcite.adapter.enumerable.EnumerableConvention;
import org.apache.calcite.adapter.enumerable.EnumerableRel;
import org.apache.calcite.adapter.enumerable.EnumerableRelImplementor;
import org.apache.calcite.adapter.enumerable.PhysType;
import org.apache.calcite.adapter.enumerable.PhysTypeImpl;
import org.apache.calcite.linq4j.tree.Blocks;
import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.linq4j.tree.Expressions;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.core.TableScan;
import org.apache.calcite.rel.hint.RelHint;

import java.util.List;

@Slf4j
public class MockTableScan extends TableScan implements EnumerableRel {
    protected MockTableScan(
        RelOptCluster cluster,
        List<RelHint> hints,
        RelOptTable table
    ) {
        super(cluster, cluster.traitSetOf(EnumerableConvention.INSTANCE), hints, table);
    }

    @Override
    public void register(RelOptPlanner planner) {
        planner.addRule(MockTableScanRule.INSTANCE);
    }

    @Override
    public Result implement(EnumerableRelImplementor implementor, Prefer pref) {
        PhysType physType = PhysTypeImpl.of(
            implementor.getTypeFactory(),
            getRowType(),
            pref.preferArray()
        );
        final Expression expression = table.getExpression(MockTranslatableTable.class);
        return implementor.result(
            physType,
            Blocks.toBlock(
                Expressions.call(expression, "scan",
                    implementor.getRootExpression())
            )
        );
    }
}
