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

package com.jtel.common.log;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/9/16
 * Package : com.jtel.common
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class Logger implements ILogger {
    private static Logger ins;



    @Override
    public void log(Object... os) {
        context.log(os);
    }



    @Override
    public void warn(Object... os) {
        context.warn(os);
    }



    @Override
    public void error(Object... os) {
        context.error(os);
    }

    private ILogger context;


    private Logger(){
        setContext(new SystemLogger());
    }


    public void setContext(ILogger context) {
        this.context = context;
    }


    public static Logger getInstance() {
        if (ins == null) {
            ins = new Logger();
        }
        return ins;
    }
}
