/*
 * This file is part of JTel.
 *
 *     JTel is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     JTel is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with JTel.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jtel.mtproto.tl.schema;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jtel.common.log.Logger;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/17/16
 * Package : com.jtel.mtproto.tl.schema
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class JSONTlSchemaProvider extends TlSchemaProvider {
    String mtpSchema;
    String apiSchema;
    protected JSONTlSchemaProvider() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();

        mtpSchema= s+"/mtp_schema.json";
         apiSchema= s+"/api_schema.json";
        initialize();
    }

    @Override
    protected TlSchema loadMtpSchema() {

        return load(mtpSchema);

    }

    @Override
    protected TlSchema loadApiSchema() {
       return load(apiSchema);
    }


    private TlSchema load(String path){
        try{
            Reader reader = new FileReader(path);
            Gson gson = new GsonBuilder().create();
            return  gson.fromJson(reader, TlSchema.class);
        } catch (Exception e) {
            Logger.getInstance().error(getClass().getSimpleName(),e.getMessage());
        }
        return null;
    }
}
