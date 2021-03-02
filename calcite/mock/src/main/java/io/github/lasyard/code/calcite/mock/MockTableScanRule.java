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
import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.plan.RelRule;

@Slf4j
public class MockTableScanRule extends RelRule<MockTableScanRule.Config> {
    public static final MockTableScanRule INSTANCE = MockTableScanRule.Config.DEFAULT.toRule();

    public MockTableScanRule(Config config) {
        super(config);
    }

    @Override
    public void onMatch(RelOptRuleCall call) {
        MockTableScan scan = call.rel(0);
        // Actually nothing changed.
        call.transformTo(scan);
        log.info("Transformed.");
    }

    public interface Config extends RelRule.Config {
        Config DEFAULT = EMPTY
            .withOperandSupplier(b0 -> b0.operand(MockTableScan.class).noInputs())
            .as(Config.class);

        @Override
        default MockTableScanRule toRule() {
            return new MockTableScanRule(this);
        }
    }
}
