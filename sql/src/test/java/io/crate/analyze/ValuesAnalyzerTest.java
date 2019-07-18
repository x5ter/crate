/*
 * Licensed to Crate under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Crate licenses this file
 * to you under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial
 * agreement.
 */

package io.crate.analyze;

import io.crate.analyze.relations.ValuesRelation;
import io.crate.test.integration.CrateDummyClusterServiceUnitTest;
import io.crate.testing.SQLExecutor;
import io.crate.testing.SymbolMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;

public class ValuesAnalyzerTest extends CrateDummyClusterServiceUnitTest {

    private SQLExecutor e;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        e = SQLExecutor.builder(clusterService).build();
    }

    @Test
    public void test_values_statement_can_be_analyzed() {
        ValuesRelation rel = e.analyze("VALUES (1, 1 + 2), (2, 1 * 1+ 1)");
        assertThat(rel.outputs(), contains(
            SymbolMatchers.isInputColumn(0),
            SymbolMatchers.isInputColumn(1)
        ));
    }

    @Test
    public void test_error_is_raised_if_number_of_items_within_rows_are_not_equal() {
        expectedException.expectMessage("VALUES lists must all be the same length");
        e.analyze("VALUES (1), (2, 3)");
    }

    @Test
    public void test_error_is_raised_if_the_types_of_the_rows_are_not_compatible() {
        expectedException.expectMessage(
            "The types of the columns within VALUES lists must match. Found `bigint` and `text` at position: 0");
        e.analyze("VALUES (1), ('foo')");
    }

    @Test
    public void test_values_used_in_sub_query_can_be_analyzed() {
        QueriedSelectRelation<ValuesRelation> rel = e.analyze("SELECT y, x FROM (VALUES (1, 2)) AS t (x, y)");
        assertThat(rel.fields(), contains(
            SymbolMatchers.isField("y"),
            SymbolMatchers.isField("x")
        ));
    }
}