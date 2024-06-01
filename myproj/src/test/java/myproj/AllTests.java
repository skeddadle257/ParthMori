package myproj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.*;

import org.junit.*;

import models.*;
import services.*;
import exceptions.InsufficientFundsException;

public class AllTests {

    static List<Account> accounts = new ArrayList<>();
    static AccountService accountService = new AccountServiceImpl(accounts);

    @Before
    public void setUp() {
        accounts.clear();
    }

    // Test cases for getAllAccounts
    @Test
    public void getAllAccounts_Test_Success() {
        Account account = new Account(accounts.size(), AccountType.checkings, "Parth Mori", 90000);
        accountService.createAccount(account);
        List<Account> actualResult = accountService.getAllAccounts();
        assertEquals(1, actualResult.size());
    }

    @Test
    public void getAllAccounts_Test_Failure() {
        List<Account> actualResult = accountService.getAllAccounts();
        assertEquals(0, actualResult.size());
    }

    // Test cases for getAccountByID
    @Test
    public void getAccountByID_Test_Success() {
        Account expectedResult = new Account(accounts.size(), AccountType.checkings, "Parth Mori", 90000);
        accountService.createAccount(expectedResult);
        Account actualResult = accountService.getAccountByID(expectedResult.getAccountID());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getAccountByID_Test_Failure() {
        Account actualResult = accountService.getAccountByID(999);
        assertNull(actualResult);
    }

    // Test cases for getBalanceByAccountID
    @Test
    public void getBalanceByAccountID_Test_Success() {
        Account account = new Account(accounts.size(), AccountType.checkings, "Parth Mori", 90000);
        accountService.createAccount(account);
        double actualResult = accountService.getBalanceByAccountID(account.getAccountID());
        assertEquals(90000, actualResult, 0.0);
    }

    @Test
    public void getBalanceByAccountID_Test_Failure() {
        double actualResult = accountService.getBalanceByAccountID(999);
        assertEquals(0.0, actualResult, 0.0);
    }

    // Test cases for createAccount
    @Test
    public void createAccount_Test_Success() {
        Account expectedResult = new Account(accounts.size(), AccountType.checkings, "Parth Mori", 90000);
        Account actualResult = accountService.createAccount(expectedResult);
        assertEquals(expectedResult, actualResult);
        accounts.remove(expectedResult);
    }

    @Test
    public void createAccount_Test_Failure() {
        Account account = new Account(accounts.size(), null, "Parth Mori", 90000);
        assertThrows(NullPointerException.class, () -> {
            accountService.createAccount(account);
        });
    }

    // Test cases for deleteAccount
    @Test
    public void deleteAccount_Test_Success() {
        Account account = new Account(accounts.size(), AccountType.checkings, "Parth Mori", 90000);
        accountService.createAccount(account);
        Account actualResult = accountService.deleteAccount(account.getAccountID());
        assertEquals(account, actualResult);
    }

    @Test
    public void deleteAccount_Test_Failure() {
        Account actualResult = accountService.deleteAccount(999);
        assertNull(actualResult);
    }

    // Test cases for updateAccount
    @Test
    public void updateAccount_Test_Success() {
        Account account = new Account(accounts.size(), AccountType.checkings, "Parth Mori", 90000);
        accountService.createAccount(account);
        account.setBalance(100000);
        Account actualResult = accountService.updateAccount(account);
        assertEquals(100000, actualResult.getBalance(), 0.0);
    }

    @Test
    public void updateAccount_Test_Failure() {
        Account account = new Account(999, AccountType.checkings, "Parth Mori", 90000);
        Account actualResult = accountService.updateAccount(account);
        assertNull(actualResult);
    }

    // Test cases for withdraw
    @Test
    public void withdraw_Test_Success() {
        Account account = new Account(accounts.size(), AccountType.savings, "Het Patel", 110);
        accountService.createAccount(account);
        double initialBalance = account.getBalance(); // 110
        double amountToWithdraw = 100;
        double expectedResult = initialBalance - amountToWithdraw; // 110 - 100 = 10
        double actualResult = accountService.withdraw(account.getAccountID(), amountToWithdraw);
        assertEquals(expectedResult, actualResult, 0.0);
    }

    @Test
    public void withdraw_Test_Failure_InsufficientFunds() {
        Account account = new Account(accounts.size(), AccountType.savings, "Het Patel", 50);
        accountService.createAccount(account);
        assertThrows(InsufficientFundsException.class, () -> {
            accountService.withdraw(account.getAccountID(), 100);
        });
    }

    @Test
    public void withdraw_Test_Failure_AccountNotFound() {
        assertThrows(NullPointerException.class, () -> {
            accountService.withdraw(999, 50);
        });
    }
}
