package com.jtel.common.db;

import sun.security.krb5.Credentials;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This file is part of jTelClient
 * IntelliJ idea.
 * Date     : 7/23/2016
 * Package : com.jtel.db
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class DbContext {
    Connection c = null;
    Statement stmt = null;
    ResultSet rs = null;

    public DbContext() {

    }

    public  synchronized void execute(String query, Object... params) {
        try

        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:conf.db");
            c.setAutoCommit(false);


            stmt = c.createStatement();


        } catch (Exception e)
        {
        }
        try {
            rs = stmt.executeQuery(String.format(query, params));
        }catch (Exception e){

        }


    }

    public synchronized void close(){
        try{
            if(rs != null) rs.close();
            if(stmt != null) stmt.close();
            if(c != null) c.close();

        }catch (Exception e){

        }
    }

    public ConfApi getApiDetails(){
        execute("select * from conf_api where api_id = 3751");
        try {
            while (rs.next()){
                return new ConfApi(
                        rs.getInt("api_id"),
                        rs.getString("api_hash"),
                        rs.getInt("api_schema_Layer"),
                        rs.getString("api_tl_schema_provider"));
            }
        }catch (Exception e){

        }
        close();
        return null;
    }

    public ConfCurrentState getCurrentState(){
        execute("select * from conf_current_state where session_name = 'current'");
        try {
            while (rs.next()){
                return new ConfCurrentState(
                        rs.getString("session_name"),
                        rs.getInt("dc_id"),
                        rs.getBytes("session_id"),
                        rs.getInt("authentication_required") == 1 ? true : false,
                        rs.getInt("signin_required") == 1 ? true : false
                );
            }
        }catch (Exception e){

        }
        close();
        return null;
    }

    public void updateApiState(ConfCurrentState state){
        try

        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:conf.db");
            c.setAutoCommit(false);


            PreparedStatement stmt = c.prepareStatement(
           "UPDATE conf_current_state SET dc_id  = ?, session_id =?, authentication_required = ?, signin_requiered=? where session_name = 'current';"
            );

            stmt.setInt(1,state.getDcId());
            stmt.setBytes(2,state.getSessionId());
            stmt.setInt(3,state.isAuthenticationRequired()? 1:0);
            stmt.setInt(4,state.isSignInRequired()?1:0);
            stmt.executeUpdate();
            close();


        } catch (Exception e)
        {
        }
    }

    public void updateCredentials(ConfCredentials credentials){
        try

        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:conf.db");
            c.setAutoCommit(false);


            PreparedStatement stmt = c.prepareStatement(
                    ";update credentials SET auth_key=?,auth_key_id=?,server_salt=?,server_time=? where dc_id = "+credentials.getDcId()+";"
            );

            stmt.setBytes(1,credentials.getAuthKey());
            stmt.setBytes(2,credentials.getAuthKeyId());
            stmt.setLong(4,credentials.getServerTime());
            stmt.setBytes(3,credentials.getServerSalt());
            stmt.executeUpdate();
            close();


        } catch (Exception e)
        {
        }
    }

    public ConfDc getDataCenter(int dc){
        execute("select * from conf_dc where dc_id = %s", dc);
        try {

            rs.next();
               return new ConfDc(rs.getInt("dc_id"),rs.getString("ip"),rs.getInt("port"));


        }catch (Exception e){

        }
        close();
        return null;
    }

    public ConfCredentials getCredential(int dc){
        execute("select * from credentials where dc_id = %s", dc);
        try {

            rs.next();
            return new ConfCredentials(rs.getInt("dc_id"),
                    rs.getString("auth_key").getBytes(),
                    rs.getString("auth_key_id").getBytes(),
                    rs.getLong("server_time"),
                    rs.getString("server_salt").getBytes()
            );


        }catch (Exception e){

        }
        close();
        return null;
    }

}

