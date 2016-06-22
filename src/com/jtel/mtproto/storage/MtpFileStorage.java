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

package com.jtel.mtproto.storage;

import com.jtel.common.io.FileStorage;
import com.jtel.common.io.Storage;
import com.jtel.mtproto.auth.AuthCredentials;
import com.jtel.mtproto.secure.TimeManager;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/12/16
 * Package : com.jtel.mtproto.auth
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class MtpFileStorage extends FileStorage implements Serializable {



    public MtpFileStorage() {
        super();
    }

    @Override
    public void initialItems() {
        setItem("auth_state",false);
        setItem("dcId", 1);
        setItem("session_id", TimeManager.getInstance().getSessionId());
        setItem("dcId_1_auth", new AuthCredentials());
        setItem("dcId_2_auth", new AuthCredentials());
        setItem("dcId_3_auth", new AuthCredentials());
        setItem("dcId_4_auth", new AuthCredentials());
        setItem("dcId_5_auth", new AuthCredentials());
    }

    @Override
    public void load() {
        try {
            FileInputStream   fis = new FileInputStream(getPath());
            ObjectInputStream ois = new ObjectInputStream(fis);
            Storage storage       = (Storage) ois.readObject();
            restore(storage);
            ois.close();
            fis.close();
        }
        catch (Exception e){
            //nothing
        }
    }

    @Override
    public String getPath(){
        Path currentRelativePath = Paths.get("");
        return currentRelativePath.toAbsolutePath().toString() +"/temp.dat";
    }

    @Override
    public void save() {
        try {
            FileOutputStream   fos = new FileOutputStream(getPath());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();

        }
        catch (IOException e){
            //nothing
        }
    }




}
