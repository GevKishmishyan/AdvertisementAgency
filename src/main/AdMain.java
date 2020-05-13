package main;

import exception.ExistingModelException;
import model.Ad;
import model.Category;
import model.Gender;
import model.User;
import storage.Impl.StorageImpl;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class AdMain implements Commands {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final StorageImpl STORAGE = new StorageImpl();
    private static User currentUser = null;


    public static void main(String[] args) {
        STORAGE.initData();
        boolean isRun = true;
        int userCommand;
        while (isRun) {
            Commands.printMainCommands();
            try {
                userCommand = Integer.parseInt(SCANNER.nextLine());
            } catch (NumberFormatException e) {
                userCommand = -1;
            }
            switch (userCommand) {
                case EXIT:
                    isRun = false;
                    break;
                case LOGIN:
                    userLogin();
                    break;
                case REGISTER:
                    userRegister();
                    break;
                case PRINT_ALL_USERS:
                    STORAGE.printAllUsers();
                    break;
                default:
                    System.out.println("Incorrect Value!!! Please TRY AGAIN.");
            }
        }

    }

    private static void userLogin() {
        if (STORAGE.isUsersEmpty()) {
            System.out.println("Users list is EMPTY !!! Please registered FIRST.");
            return;
        }
        System.out.println("Please enter [phoneNumber, password].");
        try {
            String userInputStr = SCANNER.nextLine();
            String[] userInput = userInputStr.split(",");
            if (userInputStr.equals("")) {
                System.out.println("You forgot to fill all fields.");
                userLogin();
            } else if (userInput.length == 1) {
                System.out.println("You forgot to enter password.");
                userLogin();
            } else if (userInput.length > 2) {
                System.out.println("Incorrect value..!");
                userLogin();
            } else {
                User user = STORAGE.getUserByPhoneNumber(userInput[0]);
                if (user != null) {
                    currentUser = user;
                    System.out.println(String.format("Welcome %s %s. You are successfully logged in.", currentUser.getName(), currentUser.getSurname()));
                    System.out.println("------------------------------------------------------------------------------------");
                    System.out.println("Choose your command.");
                    userCommands();
                } else {
                    System.out.println(String.format("User with %s phoneNumber does not exist.", userInput[0]));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR.");
        }
    }

    private static void userCommands() {
        boolean isRun = true;
        int userCommand;
        while (isRun) {
            Commands.printUserCommands();
            try {
                userCommand = Integer.parseInt(SCANNER.nextLine());
            } catch (NumberFormatException e) {
                userCommand = -1;
            }
            switch (userCommand) {
                case LOGOUT:
                    isRun = false;
                    break;
                case ADD_NEW_AD:
                    addNewAd();
                    break;
                case PRINT_MY_ALL_ADS:
                    if (STORAGE.isAdsEmpty()) {
                        System.out.println("Ads list is EMPTY. Please, add AD first.");
                        return;
                    }
                    STORAGE.printMyAllAds(currentUser);
                    break;
                case PRINT_ALL_ADS:
                    if (STORAGE.isAdsEmpty()) {
                        System.out.println("Ads list is EMPTY. Please, add AD first.");
                        return;
                    }
                    STORAGE.printAllAds();
                    break;
                case PRINT_AD_BY_CATEGORY:
                    if (STORAGE.isAdsEmpty()) {
                        System.out.println("Ads list is EMPTY. Please, add AD first.");
                        return;
                    }
                    printAdByCategory();
                    break;
                case PRINT_ALL_ADS_BY_TITLE_SORT:
                    if (STORAGE.isAdsEmpty()) {
                        System.out.println("Ads list is EMPTY. Please, add AD first.");
                        return;
                    }
                    STORAGE.printAllAdsByTitleSort();
                    break;
                case PRINT_ALL_ADS_BY_DATE_SORT:
                    if (STORAGE.isAdsEmpty()) {
                        System.out.println("Ads list is EMPTY. Please, add AD first.");
                        return;
                    }
                    STORAGE.printAllAdsByDateSort();
                    break;
                case DELETE_MY_ALL_ADS:
                    if (STORAGE.isAdsEmpty()) {
                        System.out.println("Ads list is EMPTY. Please, add AD first.");
                        return;
                    }
                    STORAGE.deleteAllAdsByUser(currentUser);
                    break;
                case DELETE_AD_BY_TITLE:
                    if (STORAGE.isAdsEmpty()) {
                        System.out.println("Ads list is EMPTY. Please, add AD first.");
                        return;
                    }
                    deleteAdByTitle();
                    break;
                default:
                    System.out.println("Incorrect Value!!! Please TRY AGAIN.");
            }
        }
    }

    private static void deleteAdByTitle() {
        if (STORAGE.isAdsEmpty()) {
            System.out.println("Ads list is EMPTY. Please, add AD first.");
            return;
        }
        STORAGE.printMyAllAds(currentUser);
        System.out.println("Please, enter TITLE to delete the AD.");
        String title = SCANNER.nextLine();
        if (title.equals("")) {
            System.out.println("Please enter the ad's title, which you want to delete.");
            deleteAdByTitle();
        } else {
            STORAGE.deleteAdByTitle(title);
            System.out.println("Ad was deleted.");
        }
    }

    private static void printAdByCategory() {
        if (STORAGE.isAdsEmpty()) {
            System.out.println("Ads list is EMPTY. Please, add AD first.");
            return;
        }
        System.out.println("Please enter category [ART, SOCIAL, SPORT, MEDICINE].");
        try {
            String category = SCANNER.nextLine();
            if (category.equals("")) {
                System.out.println("Please enter category [ART, SOCIAL, SPORT, MEDICINE].");
                printAdByCategory();
            } else {
                STORAGE.printAdByCategory(Category.valueOf(category.toUpperCase()));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Please enter only ART, SOCIAL, SPORT, MEDICINE on category field.");
            printAdByCategory();
        }
    }

    private static void addNewAd() {
        System.out.println("Please enter [title, text, price, category(ART, SOCIAL, SPORT, MEDICINE)].");
        try {
            String adInfoStr = SCANNER.nextLine();
            String[] adInfo = adInfoStr.split(",");
            if (adInfoStr.equals("")) {
                System.out.println("You forgot to fill all fields.");
                addNewAd();
            } else if (adInfo.length < 4) {
                adFieldsErrors(adInfo.length);
                addNewAd();
            } else if (adInfo.length > 4) {
                System.out.println("Incorrect value..!");
                addNewAd();
            } else {
                Ad ad = new Ad();
                ad.setTitle(adInfo[0]);
                ad.setText(adInfo[1]);
                ad.setPrice(Double.parseDouble(adInfo[2]));
                ad.setDate(new Date());
                ad.setCategory(Category.valueOf(adInfo[3].toUpperCase()));
                ad.setAuthor(currentUser);
                STORAGE.add(ad);
                System.out.println("Ad successfully added.");
            }
        } catch (ExistingModelException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Please enter only DIGITS on price field");
            addNewAd();
        } catch (IllegalArgumentException e) {
            System.out.println("Please enter only ART, SOCIAL, SPORT, MEDICINE on category field.");
            addNewAd();
        }

    }

    private static void userRegister() {
        System.out.println("Please enter [name, surname, gender(MALE,FEMALE), age, phoneNumber, password].");
        try {
            String userInfoStr = SCANNER.nextLine();
            String[] userInfo = userInfoStr.split(",");
            if (userInfoStr.equals("")) {
                System.out.println("You forgot to fill all fields.");
                userRegister();
            } else if (userInfo.length < 6) {
                userFieldsErrors(userInfo.length);
                userRegister();
            } else if (userInfo.length > 6) {
                System.out.println("Incorrect value..!");
                userRegister();
            } else {
                User user = new User();
                user.setName(userInfo[0]);
                user.setSurname(userInfo[1]);
                user.setGender(Gender.valueOf(userInfo[2].toUpperCase()));
                user.setAge(Integer.parseInt(userInfo[3]));
                user.setPhoneNumber(userInfo[4]);
                user.setPassword(userInfo[5]);
                STORAGE.add(user);
                System.out.println("You are successfully registered.");
            }
        } catch (ExistingModelException e) {
            System.out.println(e.getMessage());
            userRegister();
        } catch (NumberFormatException e) {
            System.out.println("Please enter only DIGITS on age field");
            userRegister();
        } catch (IllegalArgumentException e) {
            System.out.println("Please enter only MALE or FEMALE on gender field.");
            userRegister();
        }
    }

    private static void userFieldsErrors(int length) {
        switch (length) {
            case 1:
                System.out.println("You forgot to enter [surname, gender(MALE,FEMALE), age, phoneNumber, password].");
                break;
            case 2:
                System.out.println("You forgot to enter [gender(MALE,FEMALE), age, phoneNumber, password].");
                break;
            case 3:
                System.out.println("You forgot to enter [age, phoneNumber, password].");
                break;
            case 4:
                System.out.println("You forgot to enter [phoneNumber, password].");
                break;
            case 5:
                System.out.println("You forgot to enter [password].");
                break;
        }
    }

    private static void adFieldsErrors(int length) {
        switch (length) {
            case 1:
                System.out.println("You forgot to enter [text, price, category(ART, SOCIAL, SPORT, MEDICINE)].");
                break;
            case 2:
                System.out.println("You forgot to enter [price, category(ART, SOCIAL, SPORT, MEDICINE)].");
                break;
            case 3:
                System.out.println("You forgot to enter [age, category(ART, SOCIAL, SPORT, MEDICINE)].");
                break;
        }
    }

}
