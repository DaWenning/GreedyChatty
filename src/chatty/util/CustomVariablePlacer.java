package chatty.util;

import chatty.Chatty;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CustomVariablePlacer {

    String place(String s) {
        String returner = "";
        s = s.substring(1, s.length()-1);
        String[] arr = s.split(";");
        String code = arr[0];
        switch (code)
        {
            case "pick":
                returner = pick(arr);
                break;
            default:
                returner = s;
        }


        return returner;
    }

    private String pick(String[] parameters) {
        Random r = new Random();
        int t = r.nextInt(parameters.length - 1) + 1;
        return parameters[t];
    }




}
