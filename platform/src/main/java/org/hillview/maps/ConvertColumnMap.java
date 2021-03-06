/*
 * Copyright (c) 2017 VMware Inc. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
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

package org.hillview.maps;

import org.hillview.table.api.*;

import java.io.Serializable;

/**
 * This map receives a column name of the input table, and returns a table with a new column,
 * containing the specified data converted to a new kind.
 */
public class ConvertColumnMap extends AppendColumnMap {
    static final long serialVersionUID = 1;

    public static class Info implements Serializable {
        String colName;
        String newColName;
        int columnIndex;
        ContentsKind newKind;

        public Info(String colName, String newColName, int columnIndex, ContentsKind kind) {
            this.colName = colName;
            this.newColName = newColName;
            this.columnIndex = columnIndex;
            this.newKind = kind;
        }
    }

    private final Info info;

    public ConvertColumnMap(Info info) {
        super(info.newColName, info.columnIndex);
        this.info = info;
    }

    @Override
    public IColumn createColumn(ITable table) {
        IColumn col = table.getLoadedColumn(this.info.colName);
        return col.convertKind(this.info.newKind, this.newColName, table.getMembershipSet());
    }
}
