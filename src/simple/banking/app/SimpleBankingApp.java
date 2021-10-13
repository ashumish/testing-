package simple.banking.app;

import java.io.IOException;
import java.util.Scanner;

import simple.banking.app.account.info.UserAccountInfo;
import simple.banking.app.account.info.UserTransactionsService;

/**
 * The Class SimpleBankingApp.
 */
public class SimpleBankingApp 
{

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException 
	{
		String defaultAccountsCsvPath = "account.csv";
		String defaultAccountStatementsCsvPath = "account_statement.csv";
		
		System.out.println("########### Welcome to Simple Banking app ################\n");
		Scanner consoleInputReciever = null;
	    try 
	    {
	    	consoleInputReciever = new Scanner(System.in);
	    	System.out.println("Enter accounts csv path:");
		    String accountsCsvPathInput = consoleInputReciever.nextLine();
		    if (accountsCsvPathInput==null || accountsCsvPathInput.equals("")) 
		    {
		    	accountsCsvPathInput = defaultAccountsCsvPath; //if value is not provided then setting here default
		    }
		    System.out.println("Enter account statements csv path:");
		    String accountStmtsCsvPathInput = consoleInputReciever.nextLine();
		    if (accountStmtsCsvPathInput==null || accountStmtsCsvPathInput.equals("")) 
		    {
		    	accountStmtsCsvPathInput = defaultAccountStatementsCsvPath; //if value is not provided then setting here default
		    }
		    System.out.println("Input accounts csv path: "+accountsCsvPathInput);
		    System.out.println("Input account statements csv path: "+accountStmtsCsvPathInput);
		    System.out.println("_________________________________________________________________\n");
		    System.out.println("-------- Select the banking operation you want to perform -------\n Enter '1' to CheckBalance \n Enter '2' to CreateAccount \n Enter '3' to AddAmount: \n Enter '4' to WithdrawAmount:");
		    int bankingOperationSelected = Integer.parseInt(consoleInputReciever.nextLine());
		    System.out.println("\nYou have selected: "+ bankingOperationSelected);
		    UserTransactionsService txService = new UserTransactionsService(accountsCsvPathInput, accountStmtsCsvPathInput);
		    switch (bankingOperationSelected) {
			case 1:
				System.out.println("Entering Check balance operation..\n");
				System.out.println("Enter account number:");
			    int accountNumberCheckBalance = Integer.parseInt(consoleInputReciever.nextLine());
			    System.out.println("Enter name:");
			    String nameCheckBalance = consoleInputReciever.nextLine();
			    double balance = txService.checkBalance(accountNumberCheckBalance, nameCheckBalance);
				System.out.println("Account balance for '"+nameCheckBalance+"' in account number: '"+accountNumberCheckBalance+"' is: "+ balance);
				break;
			case 2:
				System.out.println("Entering Create account operation..\n");
				System.out.println("Enter account number:");
			    int accountNumberCreateAcc = Integer.parseInt(consoleInputReciever.nextLine());
			    System.out.println("Enter name:");
			    String nameCreateAcc = consoleInputReciever.nextLine();
			    System.out.println("Enter balance:");
			    double balanceCreateAcc = Double.parseDouble(consoleInputReciever.nextLine());
			    
				UserAccountInfo newUserAccount = new UserAccountInfo(accountNumberCreateAcc, nameCreateAcc, balanceCreateAcc);
				String result = txService.createNewAccount(newUserAccount);
				System.out.println("Account creation result: "+result);
				break;
			case 3:
				System.out.println("Entering add amount operation..\n");
				System.out.println("Enter account number:");
			    int accountNumberAddAmount = Integer.parseInt(consoleInputReciever.nextLine());
			    System.out.println("Enter amount for credit:");
			    double balanceAddAmount = Double.parseDouble(consoleInputReciever.nextLine());
			    System.out.println("How do you want to add amount, select from the options---- \n Input as 'cash' for Cash transaction: \n Input as 'online' for Online transaction: ");
			    String mode = consoleInputReciever.nextLine();
			    String txResult = txService.addAmount(accountNumberAddAmount, balanceAddAmount, mode);
				System.out.println("Add amount result: "+txResult);
				break;
			case 4:
				System.out.println("Entering withdraw amount operation..");
				System.out.println("Enter account number:");
			    int accountNumberDebit = Integer.parseInt(consoleInputReciever.nextLine());
			    System.out.println("Enter amount for debit:");
			    double balanceDebit = Double.parseDouble(consoleInputReciever.nextLine());
			    System.out.println("How do you want to withdraw amount, select from the options---- \n Input as 'cash' for Cash transaction: \n Input as 'online' for Online transaction: \n Input as 'atm' for ATM transaction:");
			    String modeDebit = consoleInputReciever.nextLine();
			    String txDebitResult = txService.withdrawAmount(accountNumberDebit, balanceDebit, modeDebit);
				System.out.println("Withdraw amount result: "+txDebitResult);
				break;
			default:
				System.out.println("Please select a valid operation from the list!");
				break;
			}
	    }
	    catch (Exception e) 
	    {
			e.printStackTrace();
		}
	    finally 
	    {
	      //close the input output
		  if (consoleInputReciever!=null)
		  {
			  consoleInputReciever.close();
		  }
		}
	}
}
