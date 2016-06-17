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

package com.jtel.common.io;

import java.io.File;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/16/16
 * Package : com.jtel.common.io
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public abstract class FileStorage extends Storage {

    public FileStorage(){
        super();
        initialize();
    }

    @Override
    public void clear() {
        super.clear();
        initialItems();
        save();
    }

    protected void initialize() {

        File cache = new File(getPath());
        if (cache.exists()){
            load();
        }
        else {
            initialItems();
        }

    }

    public abstract void initialItems();

    public abstract String getPath();

    public abstract void save();

    public abstract void load();
}
