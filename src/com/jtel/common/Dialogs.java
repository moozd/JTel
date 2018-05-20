package com.jtel.common;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * This file is part of jTelClient
 * IntelliJ idea.
 * Date     : 7/22/2016
 * Package : com.jtel.common
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class Dialogs {

    public static void showError(Exception e){
        showErr(e.getMessage());

        // if(exit)System.exit(1);
    }

    public static void showError(String e){
        showErr(e);

        // if(exit)System.exit(1);
    }

    private static void showErr(String e) {
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("System Exception");
            alert.setContentText(e);
            alert.show();
        });
    }
}
