/*
 * Copyright (c) 2018 VMware Inc. All Rights Reserved.
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

package org.hillview.sketches;

import org.hillview.dataset.api.TableSketch;
import org.hillview.dataset.api.Empty;
import org.hillview.storage.CsvFileWriter;
import org.hillview.table.Schema;
import org.hillview.table.api.ITable;
import org.hillview.utils.Converters;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * This sketch saves a table into a set of CSV files in the specified folder.
 * TODO: Today the save can succeed on some machines, and fail on others. There
 * is no cleanup if that happens. This does not return anything really. If the
 * saving fails this will trigger an exception.
 */
public class SaveAsCSVSketch implements TableSketch<Empty> {
    static final long serialVersionUID = 1;

    private final String delimiter;
    private final String folder;
    @Nullable
    private final Schema schema;
    /**
     * If true a schema file will also be created.
     */
    private final boolean createSchema;
    /**
     * Map that describes how columns should be renamed.
     */
    @Nullable
    private final HashMap<String, String> renameMap;

    public SaveAsCSVSketch(final String delimiter, final String folder, @Nullable final Schema schema,
            @Nullable final HashMap<String, String> renameMap, boolean createSchema) {
        this.delimiter = delimiter;
        this.folder = folder;
        this.createSchema = createSchema;
        this.schema = schema;
        this.renameMap = renameMap;
    }

    @Override
    public Empty create(@Nullable ITable data) {
        Converters.checkNull(data);
        if (this.schema != null)
            data = data.project(this.schema);
        if (this.renameMap != null && this.renameMap.size() != 0)
            data = data.renameColumns(this.renameMap);

        data.getLoadedColumns(data.getSchema().getColumnNames());

        String tableFile = data.getSourceFile();
        if (tableFile == null)
            throw new RuntimeException("I don't know how to generate file names for the data");
        String sourceFile = data.getSourceFile();
        if (sourceFile.contains("/")) {
            String parentDir = sourceFile.substring(0, sourceFile.lastIndexOf('/'));
            String fileName = sourceFile.substring(sourceFile.lastIndexOf('/') + 1);

            File file = new File(parentDir, this.folder);
            boolean ignored = file.mkdir();
            System.out.println(ignored + " trying to create " + file.toString());
            // There is a race here: multiple workers may try to create the
            // folder at the same time, so we don't bother if the creation fails.
            // If the folder can't be created the writing below will fail.

            String fileOutput = Paths.get(parentDir, this.folder, fileName).toString();
            CsvFileWriter csvWriter = new CsvFileWriter(fileOutput);
            csvWriter.setSeparator(delimiter);
            csvWriter.setWriteHeaderRow(false);
            System.out.println("Writing to " + fileOutput);
            csvWriter.writeTable(data);
        }
        return Empty.getInstance();
    }

    @Nullable
    @Override
    public Empty zero() {
        return Empty.getInstance();
    }

    @Nullable
    @Override
    public Empty add(@Nullable Empty left, @Nullable Empty right) {
        return left;
    }
}
