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

package com.jtel.mtproto;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/28/16
 * Package : com.jtel
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class MtpException extends Exception {


    MtpStates state;
    public MtpException(MtpStates state,String message) {
        super("Mtp client failed to complete the operation because a\\an " + state + " happened. " + message);
        this.state = state;
    }

    public MtpException(MtpStates state,String message, Throwable cause) {
        super("Mtp client failed to complete the operation because a\\an " + state + " happened. " +message, cause);
        this.state = state;
    }

    public MtpException(MtpStates state, Throwable cause) {
        super("Mtp client failed to complete the operation because a\\an " + state + " happened. see cause for more details", cause);
        this.state = state;
    }

    public MtpStates getState() {
        return state;
    }
}
