package stepDefinitions;

import java.util.Arrays;

public class APIConnectionTest {

    static class Split{
        public static String split(String str){
            String[] raw = str.split(",")[1].split(":");
            return raw[1].substring(0, raw[1].indexOf("}"));
        };
    }

    public static void main(String[] args) {

        String rawBody = APIConnection.APIconnection("89445001021983048216");

        String[] raw = rawBody.split(",")[1].split(":");

        System.out.println("---------------API  Tests--------------");

        System.out.println("Body received: "+rawBody);

        System.out.println(">> "+ raw[1].substring(0, raw[1].indexOf("}")));

        System.out.println("Using new method: "+Split.split(rawBody));

        //APIConnection apiConnection = new APIConnection();
        //System.out.println(APIConnection.APIconnection("8944500102198304826"));
    }
}
