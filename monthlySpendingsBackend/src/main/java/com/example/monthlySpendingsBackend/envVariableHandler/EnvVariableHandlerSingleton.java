package com.example.monthlySpendingsBackend.envVariableHandler;

public class EnvVariableHandlerSingleton {
    private static EnvVariableHandlerSingleton envVars;
    private String timeZone;
    private String password;
    private String username;
    private String dataBaseURL;
    private EnvVariableHandlerSingleton(){
        timeZone = System.getenv("TIMEZONE");
        password = System.getenv("PASSWORD");
        username = System.getenv("USERNAME");
        dataBaseURL = System.getenv("DATABASEURL");
    }

    private static void initEnvVariableHandlerSingletonInstance(){
        if(envVars == null){
            envVars = new EnvVariableHandlerSingleton();
        }
    }

    public static String getTimeZone() {
        if(envVars == null){
            initEnvVariableHandlerSingletonInstance();
        }
        return envVars.timeZone;
    }

    public static String getPassword() {
        if(envVars == null){
            initEnvVariableHandlerSingletonInstance();
        }
        return envVars.password;
    }

    public static String getUsername() {
        if(envVars == null){
            initEnvVariableHandlerSingletonInstance();
        }
        return envVars.username;
    }

    public static String getDataBaseURL() {
        if(envVars == null){
            initEnvVariableHandlerSingletonInstance();
        }
        return envVars.dataBaseURL;
    }
}
