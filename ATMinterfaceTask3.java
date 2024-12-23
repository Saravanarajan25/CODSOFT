import java.io.*;
import java.util.*;

class BankAccount {
    private String accountHolder;
    private double balance;

    public BankAccount(String accountHolder, double initialBalance) {
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit successful. Current balance: " + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawal successful. Current balance: " + balance);
        } else if (amount > balance) {
            System.out.println("Insufficient funds.");
        } else {
            System.out.println("Invalid withdrawal amount.");
        }
    }

    public void checkBalance() {
        System.out.println("Current balance: " + balance);
    }
}

public class ATMinterfaceTask3{
    private static final String DATA_FILE = "users.txt";
    private static Map<String, String> users = new HashMap<>();
    private static Map<String, BankAccount> accounts = new HashMap<>();

    public static void main(String[] args) {
        loadUserData();
        System.out.println("Welcome to the ATM Machine");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n1. Register\n2. Login\n3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        register(scanner);
                        break;
                    case 2:
                        login(scanner);
                        break;
                    case 3:
                        saveUserData();
                        System.out.println("Thank you for using the ATM. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }

    private static void register(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists.");
        } else {
            users.put(username, password);
            accounts.put(username, new BankAccount(username, 0.0));
            System.out.println("Registration successful.");
        }
    }

    private static void login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (users.containsKey(username) && users.get(username).equals(password)) {
            System.out.println("Login successful. Welcome, " + username + "!");
            BankAccount account = accounts.get(username);
            showMenu(scanner, account);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void showMenu(Scanner scanner, BankAccount account) {
        while (true) {
            System.out.println("\n1. Withdraw\n2. Deposit\n3. Check Balance\n4. Logout");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    account.withdraw(withdrawAmount);
                    break;
                case 2:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    break;
                case 3:
                    account.checkBalance();
                    break;
                case 4:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void loadUserData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    users.put(username, password);
                    accounts.put(username, new BankAccount(username, balance));
                }
            }
        } catch (IOException e) {
            System.out.println("No previous data found. Starting fresh.");
        }
    }

    private static void saveUserData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (String username : users.keySet()) {
                String password = users.get(username);
                double balance = accounts.get(username).getBalance();
                writer.write(username + "," + password + "," + balance);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }
}
