import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Transaction> transactionslist = FileManager.getProducts();

        outerLoop:
        // Had to research how to break out of a while loop without exiting the system and came across outerLoop
        while (true) {
            while (true) {
                System.out.println(RESET+"\n=== Home Screen ===");
                System.out.println("D. Add Deposit");
                System.out.println("P. Make Payment");
                System.out.println("L. Ledger Screen");
                System.out.println("X. Exit");
                System.out.print("Choose an option: ");
                String choice = scanner.nextLine();
                choice = choice.toUpperCase();
                switch (choice) {
                    case "D":
                    try{
                        addDeposit(scanner);
                        break;}
                    catch (NumberFormatException exception){
                        System.out.println(RED+"Can't put a letter in the deposit!"+RESET);
                        continue outerLoop;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    case "P":
                        try{
                            addPayment(scanner);
                            break;}
                        catch (NumberFormatException exception){
                            System.out.println(RED+"Can't put a letter in the payment!"+RESET);
                            continue outerLoop;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    case "L":
                        innerLoop:
                        while (true) {
                            while (true) {
                                List<Transaction> transactions = FileManager.getProducts();
                                System.out.println("\n=== Ledger Screen ===");
                                System.out.println("A. Display All Entires");
                                System.out.println("D. Display Only Deposit");
                                System.out.println("P. Display Only Payment");
                                System.out.println("R. Reports");
                                System.out.println("O. Back to Homepage");
                                String nextChoice = scanner.nextLine();
                                nextChoice = nextChoice.toUpperCase();
                                switch (nextChoice) {
                                    case "A":
                                        displayTransaction(transactions);
                                        break;
                                    case "D":
                                        displayDeposit(transactions);
                                        break;
                                    case "P":
                                        displayPayment(transactions);
                                        break;
                                    case "R":
                                        while (true) {
                                            List<Transaction> transaction = FileManager.getProducts();
                                            transaction.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime));
                                            System.out.println("\n=== Report Screen ===");
                                            System.out.println("1. Month to Date");
                                            System.out.println("2. Previous Month");
                                            System.out.println("3. Year to Date");
                                            System.out.println("4. Previous Year");
                                            System.out.println("5. Sreach by Vendor");
                                            System.out.println("0. Back to Ledger Screen");
                                            int LastChoice = Integer.parseInt(scanner.nextLine());
                                            switch (LastChoice) {
                                                case 1:
                                                    displayMonthtoDate(transaction);
                                                    break;
                                                case 2:
                                                    displayPerivousMonth(transaction);
                                                    break;
                                                case 3:
                                                    displayYearToDate(transaction);
                                                    break;
                                                case 4:
                                                    displayPerivousYear(transaction);
                                                    break;
                                                case 5:
                                                    try {
                                                        searchVendor(transaction, scanner);
                                                        break;
                                                    }catch (InputMismatchException e){
                                                        System.out.println(RED+"Must be a string!"+RESET);
                                                    }
                                                case 0:
                                                    continue innerLoop;
                                                default:
                                                    System.out.println(RED+"Choose a number between 1-5, 0 is to go back"+RESET);
                                            }
                                        }
                                    case "O":
                                        continue outerLoop;
                                    default:
                                        System.out.println(RED+"Choose a letter that was given"+RESET);
                                }
                            }
                        }
                    case "X":
                        System.out.println(WHITE+"Okay Goodbye!"+RESET);
                        System.exit(0);
                    default:
                        System.out.println(RED+"You have to choose the options that was given"+RESET);
                }
            }
        }
    }

    public static void addDeposit(Scanner scanner) throws InterruptedException {
        System.out.println("Do you want to enter the date manually?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int userInput = Integer.parseInt(scanner.nextLine());
        switch (userInput) {
            case 1:
                try {
                    System.out.print("Enter date (YYYY/MM/DD): ");
                    String dateInput = scanner.nextLine().trim();
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    LocalDate newDate = LocalDate.parse(dateInput, dateFormatter);

                    System.out.println("Enter time (HH:MM:SS): ");
                    String timeInput = scanner.nextLine().trim();
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalTime newTime = LocalTime.parse(timeInput, timeFormatter);

                    displayQuestion(newDate, newTime);
                } catch (DateTimeParseException e) {
                    System.out.println("Invaild");
                }
                break;
            case 2:
                System.out.println("Current date: ");
                LocalDate date = LocalDate.now();
                DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String FormattedDate = date.format(DateFormatter);
                System.out.println(FormattedDate);

                System.out.println("Current time: ");
                LocalTime time = LocalTime.now();
                DateTimeFormatter TimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String FormattedTime = time.format(TimeFormatter);
                System.out.println(FormattedTime);

                displayQuestion(date, time);
                break;
            default:
                System.out.println("Invalid Input");
        }
    }
    public static void displayQuestion(LocalDate date, LocalTime time) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is the description?");
        String description = scanner.nextLine();

        System.out.println("Who is the vendor?");
        String vendor = scanner.nextLine();

        System.out.println("What is the amount you want to deposit?");
        double amount = Float.parseFloat(scanner.nextLine());

        Transaction newTransaction = new Transaction(date, time, description, vendor, amount);
        System.out.println(WHITE + "Initializing....");
        for(int i=10; i>=0; i--) {
            Thread.sleep(500);
            if(i<10 && i>5){
                System.out.println(YELLOW+"Saving..."+i);
            }
            if (i<=5){
                System.out.println(YELLOW+"Writing..."+i);
            }
            if(i==0){
                Thread.sleep(1000);
                System.out.println(GREEN+"Success! You made your Deposit!"+RESET);
                FileManager.writeProduct(newTransaction);
            }

        }
    }
    public static void addPayment(Scanner scanner) throws InterruptedException {
        System.out.println("Do you want to enter the date manually?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int userInput = Integer.parseInt(scanner.nextLine());
        switch (userInput) {
            case 1:
                try {
                    System.out.print("Enter date (YYYY/MM/DD): ");
                    String dateInput = scanner.nextLine().trim();
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    LocalDate newDate = LocalDate.parse(dateInput, dateFormatter);

                    System.out.println("Enter time (HH:MM:SS): ");
                    String timeInput = scanner.nextLine().trim();
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalTime newTime = LocalTime.parse(timeInput, timeFormatter);

                    displayPaymentQuestion(newDate, newTime);
                } catch (DateTimeParseException e) {
                    System.out.println("Invaild");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 2:
                System.out.println("Current date: ");
                LocalDate date = LocalDate.now();
                DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String FormattedDate = date.format(DateFormatter);
                System.out.println(FormattedDate);

                System.out.println("Current time: ");
                LocalTime time = LocalTime.now();
                DateTimeFormatter TimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String FormattedTime = time.format(TimeFormatter);
                System.out.println(FormattedTime);

                displayPaymentQuestion(date, time);
                break;
            default:
                System.out.println(RED+"Invalid Input"+RESET);
        }
    }
    public static void displayPaymentQuestion(LocalDate date, LocalTime time) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is the description?");
        String description = scanner.nextLine();

        System.out.println("Credit or Debt Card?");
        System.out.println("1. Credit Card");
        System.out.println("2. Debit Card");
        int vendors = Integer.parseInt(scanner.nextLine());
        String vendor;
        if (vendors == 1) {
            vendor = "Credit";
        } else if (vendors == 2) {
            vendor = "Debit";
        } else{
            System.out.println(RED+"Wrong input!"+RESET);
            return;
        }

        System.out.println("How much payment do you want to add?");
        double amount = Float.parseFloat(scanner.nextLine());
        amount = amount * (-1);

        Transaction newTransaction = new Transaction(date, time, description, vendor, amount);
        System.out.println(WHITE + "Initializing....");
        for(int i=10; i>=0; i--) {
            Thread.sleep(500);
            if(i<10 && i>5){
                System.out.println(YELLOW+"Saving..."+i);
            }
            if (i<=5){
                System.out.println(YELLOW+"Writing..."+i);
            }
            if(i==0){
                Thread.sleep(1000);
                System.out.println(GREEN+"Success! You made your Deposit!"+RESET);
                FileManager.writeProduct(newTransaction);
            }

        }
    }

    public static void displayTransaction(List<Transaction> products) {
        for (Transaction product : products) {
            System.out.println(product.toString());
        }
    }

    public static void displayDeposit(List<Transaction> products) {
        for (Transaction product : products) {
            if (product.getAmount() > 0) {
                System.out.println(product.toString());
            }
        }
    }

    public static void displayPayment(List<Transaction> products) {
        for (Transaction product : products) {
            if (product.getAmount() < 0) {
                System.out.println(product.toString());
            }
        }
    }

    public static void displayPerivousMonth(List<Transaction> products) {
        LocalDate today = LocalDate.now();
        LocalDate oneMonthAgo = today.minusMonths(1);
        for (Transaction transaction : products) {
            if (transaction.getDate().getYear() == oneMonthAgo.getYear() && transaction.getDate().getMonth() == oneMonthAgo.getMonth()) {
                System.out.printf("Date: %tF Time: %tT Description: %s Vendor: %s Amount: %.2f %n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            }
        }
    }
    public static void displayPerivousYear(List<Transaction> products) {
        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);
        for (Transaction transaction : products) {
            if (transaction.getDate().getYear() == oneYearAgo.getYear() && transaction.getDate().getMonth() == oneYearAgo.getMonth()) {
                System.out.printf("Date: %tF Time: %tT Description: %s Vendor: %s Amount: %.2f %n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            }
        }
    }

    public static void displayMonthtoDate(List<Transaction> products) {
        LocalDate sameYear = LocalDate.now();
        for (Transaction transaction : products) {
            if (transaction.getDate().getMonth() == sameYear.getMonth() && transaction.getDate().getYear() == sameYear.getYear())  {
                System.out.printf("Date: %tF Time: %tT Description: %s Vendor: %s Amount: %.2f %n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            }
        }
    }

    public static void displayYearToDate(List<Transaction> products) {
        LocalDate sameYear = LocalDate.now();
        for (Transaction transaction : products) {
            if (transaction.getDate().getYear() == sameYear.getYear()) {
                System.out.printf(
                        "Date: %tF Time: %tT Description: %s Vendor: %s Amount: %.2f %n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount()
                );
            }
        }
    }
    public static void searchVendor(List<Transaction> products, Scanner scanner) {
        System.out.println("Enter the Vendor to search");
        String inputUser = scanner.nextLine();
        for (Transaction transaction : products) {
            if (Objects.equals(transaction.getVendor(), inputUser)) {
                System.out.printf("Date: %tF Time: %tT Description: %s Vendor: %s Amount: %.2f %n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            }
        }
    }
    public static final String WHITE = "\u001B[37m";
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
}