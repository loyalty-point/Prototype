package controllers;

import java.util.List;

/**
 * Created by 11120_000 on 22/05/15.
 */
public class Preconditions <T> {

    public static boolean checkNotNull(String param) {
        if(param == null || param.equals(""))
            return false;
        else
            return true;
    }

    public boolean checkNotNull(List<T> list) {
        if(list == null || list.size() == 0)
            return false;
        else
            return true;
    }

    public static boolean checkPositive(float value) {
        if(value < 0)
            return false;
        else
            return true;
    }

}
