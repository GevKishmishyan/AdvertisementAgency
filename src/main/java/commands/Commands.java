package commands;

public interface Commands {

    // main commands
    int EXIT = 0;
    int LOGIN = 1;
    int REGISTER = 2;
    int PRINT_ALL_USERS = 3;
    int IMPORT_USERS = 4;

    // user commands
    int LOGOUT = 0;
    int ADD_NEW_AD = 1;
    int PRINT_MY_ALL_ADS = 2;
    int PRINT_ALL_ADS = 3;
    int PRINT_AD_BY_CATEGORY = 4;
    int PRINT_ALL_ADS_BY_TITLE_SORT = 5;
    int PRINT_ALL_ADS_BY_DATE_SORT = 6;
    int DELETE_MY_ALL_ADS = 7;
    int DELETE_AD_BY_TITLE = 8;
    int IMPORT_MY_ADS = 9;
    int EXPORT_MY_ADS = 10;


    static void printMainCommands() {
        System.out.println("Input " + EXIT + " for EXIT.");
        System.out.println("Input " + LOGIN + " for LOGIN.");
        System.out.println("Input " + REGISTER + " for REGISTER.");
        System.out.println("Input " + PRINT_ALL_USERS + " for PRINT_ALL_USERS.");
        System.out.println("Input " + IMPORT_USERS + " for IMPORT_USERS.");
    }

    static void printUserCommands() {
        System.out.println("Input " + LOGOUT + " for LOGOUT.");
        System.out.println("Input " + ADD_NEW_AD + " for ADD_NEW_AD.");
        System.out.println("Input " + PRINT_MY_ALL_ADS + " for PRINT_MY_ALL_ADS.");
        System.out.println("Input " + PRINT_ALL_ADS + " for PRINT_ALL_ADS.");
        System.out.println("Input " + PRINT_AD_BY_CATEGORY + " for PRINT_AD_BY_CATEGORY.");
        System.out.println("Input " + PRINT_ALL_ADS_BY_TITLE_SORT + " for PRINT_ALL_ADS_BY_TITLE_SORT.");
        System.out.println("Input " + PRINT_ALL_ADS_BY_DATE_SORT + " for PRINT_ALL_ADS_BY_DATE_SORT.");
        System.out.println("Input " + DELETE_MY_ALL_ADS + " for DELETE_MY_ALL_ADS.");
        System.out.println("Input " + DELETE_AD_BY_TITLE + " for DELETE_AD_BY_TITLE.");
        System.out.println("Input " + IMPORT_MY_ADS + " for IMPORT_MY_ADS.");
        System.out.println("Input " + EXPORT_MY_ADS + " for EXPORT_MY_ADS.");
    }

}
