package com.jtel.common.log;

/**
 * This file is part of JTel
 * IntelliJ idea.
 * Date     : 6/9/16
 * Package : com.jtel.common
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public interface ILogger {

    void log(Object... os);

    void warn(Object... os);

    void error(Object... os);

}
