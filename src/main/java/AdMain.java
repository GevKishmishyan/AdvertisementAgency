
import commands.Commands;
import exception.ExistingModelException;
import model.Ad;
import model.Category;
import model.Gender;
import model.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import storage.Impl.StorageImpl;
import util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
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
                case IMPORT_USERS:
                    importUsersFromXlsx();
                    break;
                case PRINT_ALL_USERS:
                    STORAGE.printAllUsers();
                    break;
                case EXPORT_ADS:
                    if (STORAGE.isAdsEmpty()) {
                        System.out.println("Ads list is EMPTY. Please, add AD first.");
                        break;
                    }
                    exportAdsToXlsx();
                    break;
                default:
                    System.out.println("Incorrect Value!!! Please TRY AGAIN.");
            }
        }

    }

    private static void importUsersFromXlsx() {
        System.out.println("Please select xlsx path.");
        String xlsxPath = SCANNER.nextLine();
        Runnable run = new Runnable() {
            public void run() {
                try {
                    XSSFWorkbook workbook = new XSSFWorkbook(xlsxPath);
                    Sheet sheet = workbook.getSheetAt(0);
                    int lastRowNum = sheet.getLastRowNum();
                    for (int i = 1; i <= lastRowNum; i++) {
                        Row row = sheet.getRow(i);
                        String name = row.getCell(0).getStringCellValue();
                        String surname = row.getCell(1).getStringCellValue();
                        Double age = row.getCell(2).getNumericCellValue();
                        Gender gender = Gender.valueOf(row.getCell(3).getStringCellValue());
                        Cell phoneNumber = row.getCell(4);
                        String phoneNumberStr = phoneNumber.getCellType() == CellType.NUMERIC ?
                                String.valueOf(Double.valueOf(phoneNumber.getNumericCellValue()).intValue()) : phoneNumber.getStringCellValue();
                        Cell password = row.getCell(5);
                        String passwordStr = password.getCellType() == CellType.NUMERIC ?
                                String.valueOf(Double.valueOf(password.getNumericCellValue()).intValue()) : password.getStringCellValue();

                        User user = new User();
                        user.setName(name);
                        user.setSurname(surname);
                        user.setAge(age.intValue());
                        user.setGender(gender);
                        user.setPhoneNumber(phoneNumberStr);
                        user.setPassword(passwordStr);
                        System.out.println(user);
                        if (user.equals(STORAGE.getUserByPhoneNumber(phoneNumberStr))) {
                            System.out.println("User already exist.");
                            Thread.sleep(1000);
                            continue;
                        }
                        STORAGE.add(user);
                        System.out.println("Import was succeed.");
                        Thread.sleep(1000);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("Error while importing users.");
                } catch (ExistingModelException e) {
                    System.out.println(e.getMessage());
                }
            }
        };

        Thread thread = new Thread(run, "import users");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
                    System.out.println(user);
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
                        break;
                    }
                    STORAGE.printMyAllAds(currentUser);
                    break;
                case PRINT_ALL_ADS:
                    if (STORAGE.isAdsEmpty()) {
                        System.out.println("Ads list is EMPTY. Please, add AD first.");
                        break;
                    }
                    STORAGE.printAllAds();
                    break;
                case PRINT_AD_BY_CATEGORY:
                    if (STORAGE.isAdsEmpty()) {
                        System.out.println("Ads list is EMPTY. Please, add AD first.");
                        break;
                    }
                    printAdByCategory();
                    break;
                case PRINT_ALL_ADS_BY_TITLE_SORT:
                    if (STORAGE.isAdsEmpty()) {
                        System.out.println("Ads list is EMPTY. Please, add AD first.");
                        break;
                    }
                    STORAGE.printAllAdsByTitleSort();
                    break;
                case PRINT_ALL_ADS_BY_DATE_SORT:
                    if (STORAGE.isAdsEmpty()) {
                        System.out.println("Ads list is EMPTY. Please, add AD first.");
                        break;
                    }
                    STORAGE.printAllAdsByDateSort();
                    break;
                case DELETE_MY_ALL_ADS:
                    if (STORAGE.isAdsEmpty()) {
                        System.out.println("Ads list is EMPTY. Please, add AD first.");
                        break;
                    }
                    STORAGE.deleteAllAdsByUser(currentUser);
                    break;
                case DELETE_AD_BY_TITLE:
                    if (STORAGE.isAdsEmpty()) {
                        System.out.println("Ads list is EMPTY. Please, add AD first.");
                        break;
                    }
                    deleteAdByTitle();
                    break;
                case IMPORT_MY_ADS:
                    importAdsFromXlsx();
                    break;
                default:
                    System.out.println("Incorrect Value!!! Please TRY AGAIN.");
            }
        }
    }

    private static void exportAdsToXlsx() {
        Runnable run = new Runnable() {
            public void run() {
                List<Ad> exportedAds = FileUtil.deserializeAdList();

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("exported ads");

                CreationHelper creationHelper = workbook.getCreationHelper();
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/mm/yyyy"));

                Row mainRow = sheet.createRow(0);
                mainRow.createCell(0).setCellValue("title");
                mainRow.createCell(1).setCellValue("text");
                mainRow.createCell(2).setCellValue("price");
                mainRow.createCell(3).setCellValue("date");
                mainRow.createCell(4).setCellValue("category");
                int rowIndex = 1;
                for (Ad ad : exportedAds) {
                    Row row = sheet.createRow(rowIndex);
                    row.createCell(0).setCellValue(ad.getTitle());
                    row.createCell(1).setCellValue(ad.getText());
                    row.createCell(2).setCellValue(ad.getPrice());
                    Cell cell = row.createCell(3);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(ad.getDate());
                    row.createCell(4).setCellValue(String.valueOf(ad.getCategory()));
                    rowIndex++;
                    System.out.println(ad);
                    System.out.println("Export was succeed.");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                final String FILE_PATH = "src\\main\\resources\\exported_ads.xlsx";
                File exportedFile = new File(FILE_PATH);
                try {
                    if (!exportedFile.exists()) {
                        exportedFile.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(new File(FILE_PATH));
                    workbook.write(out);
                    out.close();
                    System.out.println("exported_ads.xlsx was successfully exported.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(run, "export ads");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void importAdsFromXlsx() {
        System.out.println("Please select xlsx path.");
        String xlsxPath = SCANNER.nextLine();
        Runnable run = new Runnable() {
            public void run() {
                try {
                    XSSFWorkbook workbook = new XSSFWorkbook(xlsxPath);
                    Sheet sheet = workbook.getSheetAt(0);
                    int lastRowNum = sheet.getLastRowNum();
                    for (int i = 1; i <= lastRowNum; i++) {
                        Row row = sheet.getRow(i);
                        String title = row.getCell(0).getStringCellValue();
                        String text = row.getCell(1).getStringCellValue();
                        Double price = row.getCell(2).getNumericCellValue();
                        Date date = row.getCell(3).getDateCellValue();
                        Category category = Category.valueOf(row.getCell(4).getStringCellValue());

                        Ad ad = new Ad();
                        ad.setTitle(title);
                        ad.setText(text);
                        ad.setPrice(price);
                        ad.setDate(date);
                        ad.setCategory(category);
                        ad.setAuthor(currentUser);

                        System.out.println(ad);
                        if (STORAGE.getAdByTitleStr(title) == null) {
                            STORAGE.add(ad);
                            System.out.println("Import was succeed.");
                            Thread.sleep(1000);
                        } else {
                            System.out.println("Ad already exist.");
                            Thread.sleep(1000);
                            continue;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error while importing users.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExistingModelException e) {
                    System.out.println(e.getMessage());
                }
            }
        };

        Thread thread = new Thread(run, "import ads");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            STORAGE.deleteAdByTitle(title, currentUser);
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
