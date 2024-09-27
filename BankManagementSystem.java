import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Represents a bank account
class Account {
    private String accountHolderName;
    private int accountNumber;
    private String password;
    private double balance;

    public Account(String accountHolderName, int accountNumber, String password) {
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = 0.0; // Initial balance set to 0
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getPassword() {
        return password;
    }

    // Deposit funds into the account
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive!");
            return;
        }
        balance += amount;
        System.out.printf("Successfully deposited: $%.2f%n", amount);
        System.out.printf("Updated balance: $%.2f%n", balance);
    }

    // Withdraw funds from the account
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive!");
            return;
        }
        if (balance >= amount) {
            balance -= amount;
            System.out.printf("Successfully withdrew: $%.2f%n", amount);
            System.out.printf("Updated balance: $%.2f%n", balance);
        } else {
            System.out.println("Insufficient balance!");
        }
    }

    // Transfer funds to another account
    public void transfer(Account recipient, double amount) {
        if (recipient == null) {
            System.out.println("Recipient account does not exist!");
            return;
        }
        if (amount <= 0) {
            System.out.println("Transfer amount must be positive!");
            return;
        }
        if (balance >= amount) {
            balance -= amount;
            recipient.balance += amount;
            System.out.printf("Successfully transferred $%.2f to %s (Account Number: %d)%n",
                    amount, recipient.getAccountHolderName(), recipient.getAccountNumber());
            System.out.printf("Your new balance: $%.2f%n", balance);
        } else {
            System.out.println("Insufficient balance for transfer!");
        }
    }

    // Display account details excluding the password
    public void displayAccountDetails() {
        System.out.println("\n--- Account Details ---");
        System.out.println("Account Holder Name: " + accountHolderName);
        System.out.println("Account Number     : " + accountNumber);
        System.out.printf("Current Balance    : $%.2f%n", balance);
        System.out.println("------------------------");
    }
}

// Manages all bank accounts
class Bank {
    // Maps account holder names to their accounts (case-insensitive)
    private Map<String, Account> accountsByName;
    // Maps account numbers to their accounts
    private Map<Integer, Account> accountsByNumber;
    // Keeps track of the next account number to assign
    private int nextAccountNumber;

    public Bank() {
        accountsByName = new HashMap<>();
        accountsByNumber = new HashMap<>();
        nextAccountNumber = 10001; // Starting account number
    }

    // Adds a new account to the bank
    public void addAccount(Account account) {
        accountsByName.put(account.getAccountHolderName().toLowerCase(), account);
        accountsByNumber.put(account.getAccountNumber(), account);
    }

    // Authenticates a user using name and password
    public Account authenticate(String name, String password) {
        Account account = accountsByName.get(name.toLowerCase());
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null;
    }

    // Retrieves an account by account number
    public Account getAccountByNumber(int accountNumber) {
        return accountsByNumber.get(accountNumber);
    }

    // Retrieves an account by name (case-insensitive)
    public Account getAccountByName(String name) {
        return accountsByName.get(name.toLowerCase());
    }

    // Generates a unique, sequential account number
    public int generateAccountNumber() {
        return nextAccountNumber++;
    }

    // Checks if an account with the given name already exists
    public boolean accountExists(String name) {
        return accountsByName.containsKey(name.toLowerCase());
    }
}

public class BankManagementSystem {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner scanner = new Scanner(System.in);

        boolean exitMainMenu = false;

        while (!exitMainMenu) {
            System.out.println("\n--- Bank Management System ---");
            System.out.println("1. Create New Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Select an option (1-3): ");

            String mainChoiceInput = scanner.nextLine();
            int mainChoice;

            try {
                mainChoice = Integer.parseInt(mainChoiceInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 3.");
                continue;
            }

            switch (mainChoice) {
                case 1: // Create New Account
                    createNewAccount(bank, scanner);
                    break;

                case 2: // Login
                    login(bank, scanner);
                    break;

                case 3: // Exit
                    exitMainMenu = true;
                    System.out.println("Thank you for using the Bank Management System!");
                    break;

                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }

        scanner.close();
    }

    // Handles the creation of a new account
    private static void createNewAccount(Bank bank, Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }

        if (bank.accountExists(name)) {
            System.out.println("An account with this name already exists. Please choose a different name or login.");
            return;
        }

        String password;
        String confirmPassword;

        while (true) {
            System.out.print("Enter a password: ");
            password = scanner.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty!");
                continue;
            }

            System.out.print("Re-enter your password: ");
            confirmPassword = scanner.nextLine().trim();

            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match! Please try again.");
            } else {
                break;
            }
        }

        int accountNumber = bank.generateAccountNumber();
        Account newAccount = new Account(name, accountNumber, password);
        bank.addAccount(newAccount);
        System.out.println("\nAccount created successfully!");
        System.out.println("Your account number is: " + accountNumber);
        System.out.println("Please keep your account number safe for future transactions.");
    }

    // Handles user login
    private static void login(Bank bank, Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine().trim();
        if (password.isEmpty()) {
            System.out.println("Password cannot be empty!");
            return;
        }

        Account userAccount = bank.authenticate(name, password);
        if (userAccount == null) {
            System.out.println("Authentication failed! Invalid name or password.");
            System.out.print("Would you like to create a new account? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                createNewAccount(bank, scanner);
            }
            return;
        }

        System.out.println("\nLogin successful! Welcome " + userAccount.getAccountHolderName());
        accountMenu(bank, scanner, userAccount);
    }

    // Displays the account menu after successful login
    private static void accountMenu(Bank bank, Scanner scanner, Account userAccount) {
        boolean exitAccountMenu = false;

        while (!exitAccountMenu) {
            System.out.println("\n--- Account Menu ---");
            System.out.println("1. Add Funds");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer");
            System.out.println("4. Account Details");
            System.out.println("5. Logout");
            System.out.print("Select an option (1-5): ");

            String accountChoiceInput = scanner.nextLine();
            int accountChoice;

            try {
                accountChoice = Integer.parseInt(accountChoiceInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 5.");
                continue;
            }

            switch (accountChoice) {
                case 1: // Add Funds
                    addFunds(userAccount, scanner);
                    break;

                case 2: // Withdraw
                    withdrawFunds(userAccount, scanner);
                    break;

                case 3: // Transfer
                    transferFunds(bank, userAccount, scanner);
                    break;

                case 4: // Account Details
                    userAccount.displayAccountDetails();
                    break;

                case 5: // Logout
                    exitAccountMenu = true;
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }
    }

    // Handles adding funds to the account
    private static void addFunds(Account account, Scanner scanner) {
        System.out.print("Enter deposit amount: ");
        String depositInput = scanner.nextLine();
        double depositAmount;

        try {
            depositAmount = Double.parseDouble(depositInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount! Please enter a numeric value.");
            return;
        }

        account.deposit(depositAmount);
    }

    // Handles withdrawing funds from the account
    private static void withdrawFunds(Account account, Scanner scanner) {
        System.out.print("Enter withdrawal amount: ");
        String withdrawInput = scanner.nextLine();
        double withdrawAmount;

        try {
            withdrawAmount = Double.parseDouble(withdrawInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount! Please enter a numeric value.");
            return;
        }

        account.withdraw(withdrawAmount);
    }

    // Handles transferring funds to another account
    private static void transferFunds(Bank bank, Account userAccount, Scanner scanner) {
        System.out.print("Enter recipient's name: ");
        String recipientName = scanner.nextLine().trim();
        if (recipientName.isEmpty()) {
            System.out.println("Recipient name cannot be empty!");
            return;
        }

        System.out.print("Enter recipient's account number: ");
        String recipientAccountInput = scanner.nextLine().trim();
        int recipientAccountNumber;

        try {
            recipientAccountNumber = Integer.parseInt(recipientAccountInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid account number! Please enter a numeric value.");
            return;
        }

        if (recipientAccountNumber == userAccount.getAccountNumber()) {
            System.out.println("You cannot transfer funds to your own account!");
            return;
        }

        Account recipient = bank.getAccountByNumber(recipientAccountNumber);
        if (recipient == null) {
            System.out.println("Recipient account not found!");
            return;
        }

        // Verify that the recipient's name matches the account number
        if (!recipient.getAccountHolderName().equalsIgnoreCase(recipientName)) {
            System.out.println("Recipient name does not match the account number provided!");
            return;
        }

        System.out.print("Enter transfer amount: ");
        String transferInput = scanner.nextLine();
        double transferAmount;

        try {
            transferAmount = Double.parseDouble(transferInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount! Please enter a numeric value.");
            return;
        }

        accountTransferProcess(userAccount, recipient, transferAmount);
    }

    // Processes the transfer between accounts
    private static void accountTransferProcess(Account sender, Account recipient, double amount) {
        sender.transfer(recipient, amount);
    }
}
