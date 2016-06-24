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

import com.jtel.mtproto.tl.TlMethod;
import com.jtel.mtproto.tl.TlObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/7/16
 * Package : com.jtel.mtproto.services
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public abstract class TlSchemaProvider {

    private TlSchema mtpSchema;
    private TlSchema apiSchema;

    private static TlSchemaProvider instance;

    public static TlSchemaProvider getInstance() {
        if (instance == null) {
            instance = TlSchemaFactory.createDefault();
        }
        return instance ;
    }



    protected abstract TlSchema loadMtpSchema();

    protected abstract TlSchema loadApiSchema();


    protected void initialize(){
            apiSchema = loadMtpSchema();
            mtpSchema = loadApiSchema();
    }

    public TlSchema getApiSchema() {
        return apiSchema;
    }

    public TlSchema getMtpSchema() {

        return mtpSchema;
    }


    public TlObject getConstructor(String predicate, boolean mtp) {
        initialize();
        List<TlObject> constructors;
        if(mtp) {
            constructors = mtpSchema.constructors;
        }
        else {
            constructors = apiSchema.constructors;
        }

        for (TlObject o : constructors) {
            if (o.getPredicate().equals(predicate)) {
                //System.out.println(o);
                return o;
            }
        }
        return null;
    }
    public TlObject getConstructor(String predicate) {
        initialize();
        TlObject tlObject = getConstructor(predicate,false);
        if(tlObject == null) {
            tlObject = getConstructor(predicate,true);
        }
        return tlObject;
    }
    public TlObject getConstructor(int id) {
        initialize();

        for (TlObject o : mtpSchema.constructors) {
            if (o.getId() == id) {
               // System.out.println(o);
                return o;
            }
        }
        for (TlObject o : apiSchema.constructors) {
            if (o.getId() == id) {
                //System.out.println(o);
                return o;
            }
        }
        return null;
    }
    public List<TlObject> getDefinitions(String type, boolean mtp) {
        initialize();
        List<TlObject> constructors;
        List<TlObject> definitions = new ArrayList<>();
        if(mtp) {
            constructors = mtpSchema.constructors;
        }
        else {
            constructors = apiSchema.constructors;
        }

        for (TlObject o : constructors) {
            if (o.getType().equals(type)) {
                definitions.add(o);
            }
        }
        return definitions;
    }

    public TlObject getFirstDefinition(String type){
        initialize();
        return apiSchema.constructors
                .stream()
                .filter(a -> a.getType().equals(type))
                .findFirst()
                .orElse(
                        mtpSchema.constructors
                                .stream()
                                .filter(b-> b.getType().equals(type))
                                .findFirst()
                                .orElse(null)
                );


    }

    public TlMethod getMethod(String method){
        initialize();
        for (TlMethod o : mtpSchema.methods) {
            if (o.getMethodName().equals(method)) {
                return o;
            }
        }
        for (TlMethod o : apiSchema.methods) {
            if (o.getMethodName().equals(method)) {
                return o;
            }
        }
        return new TlMethod();
    }

}
