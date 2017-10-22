package test;

import database.Utils;

public class Test1 {

    public static final void main(String[] args){
        Utils.getConnection();
        Utils.closeConnection();
    }
}
