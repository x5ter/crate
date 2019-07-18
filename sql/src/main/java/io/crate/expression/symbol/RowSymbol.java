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

package io.crate.expression.symbol;

import io.crate.types.DataType;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

public final class RowSymbol extends Symbol {

    private List<Symbol> items;

    public RowSymbol(List<Symbol> items) {
        this.items = items;
    }

    @Override
    public SymbolType symbolType() {
        return null;
    }

    @Override
    public <C, R> R accept(SymbolVisitor<C, R> visitor, C context) {
        return null;
    }

    @Override
    public DataType valueType() {
        return null;
    }

    @Override
    public String representation() {
        StringJoiner joinOnComma = new StringJoiner(", ");
        for (Symbol item : items) {
            joinOnComma.add(item.representation());
        }
        return "ROW (" + joinOnComma.toString() + ')';
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
    }
}