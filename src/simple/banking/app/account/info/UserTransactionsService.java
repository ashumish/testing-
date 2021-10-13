package simple.banking.app.account.info;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import simple.banking.app.account.csv.ReadWriteAccountInfo;

/**
 * The Class UserTransactionsService.
 */
public class UserTransactionsService {
	
	/** The Constant SUCCESS. */
	public static final String SUCCESS = "SUCCESS";
	
	/** The Constant ERROR. */
	public static final String ERROR = "ERROR";
	
	/** The acounts csv path. */
	public String acountsCsvPath;
	
	/** The account statements csv path. */
	public String accountStatementsCsvPath;
		
	/**
	 * Instantiates a new user transactions service.
	 *
	 * @param acountsCsvPath the acounts csv path
	 * @param accountStatementsCsvPath the account statements csv path
	 */
	public UserTransactionsService(String acountsCsvPath, String accountStatementsCsvPath) {
		super();
		this.acountsCsvPath = acountsCsvPath;
		this.accountStatementsCsvPath = accountStatementsCsvPath;
	}

	/**
	 * Creates the new account.
	 *
	 * @param newUserAccountsInfo the new user accounts info
	 * @return the SUCCESS/ERROR
	 */
	public String createNewAccount(UserAccountInfo newUserAccountsInfo)
	{
		String accountAdded = ERROR;
		try
		{
			//Get the existing accounts and to check whether same user account already present
			//considering account number and name should be unique
			Set<UserAccountInfo> userAccountsInfo = ReadWriteAccountInfo.readUserAccounts(this.acountsCsvPath);
			boolean valid = true;
			for (UserAccountInfo userAccountInfo : userAccountsInfo) 
			{
				//validate account number
				if (userAccountInfo.getAccountNumber() == newUserAccountsInfo.getAccountNumber()) 
				{
					System.err.println("A account number already exist!");
					valid = false;
				} 
				//validate name
				if (userAccountInfo.getName().equals(newUserAccountsInfo.getName()))
				{
					System.err.println("A name already exist!");
					valid = false;
				}
				//If validation failed at any point above, break the loop as input account was validated by now
				if (!valid)
				{
					break;
				}
			}
			
			if (valid) 
			{
				boolean resultNewAccount = ReadWriteAccountInfo.appendToNewAccountCsv(this.acountsCsvPath, newUserAccountsInfo);
				if (resultNewAccount) 
				{
					accountAdded=SUCCESS;
				}
			}
			else
			{
				System.err.println("Account validation failed, new account can not be added at this time.");
				accountAdded=ERROR;
			}
			
		} 
		catch (IOException e) 
		{
			accountAdded=ERROR;
			e.printStackTrace();
		}
		return accountAdded;
	}

	/**
	 * Adds the amount.
	 *
	 * @param accountNumber the account number
	 * @param amount the amount
	 * @param mode the mode
	 * @return the SUCCESS/ERROR
	 */
	public String addAmount(int accountNumber, double amount, String mode) 
	{
		String amountAdded=ERROR;
		boolean amountCredit=false;
		boolean statementUpdated=false;
		try
		{
			Set<UserAccountInfo> userAccountsInfo = ReadWriteAccountInfo.readUserAccounts(this.acountsCsvPath);
			List<UserAccountStatement> userAccStmtsInfo = ReadWriteAccountInfo.readUserAccountStatement(this.accountStatementsCsvPath);
			boolean accountFound = false;
			for (UserAccountInfo userAccountInfo : userAccountsInfo) 
			{
				// if account number found then proceed
				if (userAccountInfo.getAccountNumber()==accountNumber) 
				{
					System.out.println("A account number exists in the bank, processing credit transaction..");
					accountFound = true;
					//Copy exiting useraccount info
					UserAccountInfo copyOfUserAccountInfo = (UserAccountInfo) userAccountInfo.clone();
					copyOfUserAccountInfo.setBalance(copyOfUserAccountInfo.getBalance()+amount);
					userAccountsInfo.remove(userAccountInfo); //Remove old account info
					userAccountsInfo.add(copyOfUserAccountInfo); //Remove old account info
					amountCredit=true;
					
					UserAccountStatement userAccountLatestStatement = new UserAccountStatement(ReadWriteAccountInfo.convertDateToStringDate(new Date()), "Cr", accountNumber, amount, copyOfUserAccountInfo.getBalance(), mode);
					userAccStmtsInfo.add(userAccountLatestStatement); // add info
					statementUpdated=true;
					break; // break the loop as account already matched so no further check needed
				}
			}
			if (!accountFound) 
			{
				//send error and exit as no further operation can be done
				throw new IllegalArgumentException("No account info found, please provide correct inputs!");
			}			
			//Calculate result
			if (amountCredit && statementUpdated)
			{
				amountAdded=SUCCESS;
			}

			//System.out.println("Updated user accounts: "+ userAccountsInfo);
			//Write to CSV with latest accounts info
			ReadWriteAccountInfo.writeToNewAccountCsv(this.acountsCsvPath, userAccountsInfo);
			//System.out.println("Updated user account statements: "+userAccStmtsInfo);
			//Write to CSV with latest accounts info
			ReadWriteAccountInfo.writeToNewAccountStmtCsv(this.accountStatementsCsvPath, userAccStmtsInfo);
		}
		catch (IOException e) 
		{
			amountAdded=ERROR;
			e.printStackTrace();
		}
		return amountAdded;
	}
	
	/**
	 * Withdraw amount.
	 *
	 * @param accountNumber the account number
	 * @param amount the amount
	 * @param mode the mode
	 * @return the SUCCESS/ERROR
	 */
	public String withdrawAmount(int accountNumber, double amount, String mode) 
	{
		String amountWithdrawn=ERROR;
		boolean amountDebit=false;
		boolean statementUpdated=false;
		try
		{
			Set<UserAccountInfo> userAccountsInfo = ReadWriteAccountInfo.readUserAccounts(this.acountsCsvPath);
			List<UserAccountStatement> userAccStmtsInfo = ReadWriteAccountInfo.readUserAccountStatement(this.accountStatementsCsvPath);
			boolean accountFound = false;
			for (UserAccountInfo userAccountInfo : userAccountsInfo) 
			{
				// if account number found then proceed
				if (userAccountInfo.getAccountNumber()==accountNumber) 
				{
					System.out.println("A account number exists in the bank, processing debit transaction..");
					accountFound = true;
					//Copy exiting useraccount info
					UserAccountInfo copyOfUserAccountInfo = (UserAccountInfo) userAccountInfo.clone();
					copyOfUserAccountInfo.setBalance(copyOfUserAccountInfo.getBalance()-amount);//debit
					userAccountsInfo.remove(userAccountInfo); //Remove old account info
					userAccountsInfo.add(copyOfUserAccountInfo); //Remove old account info
					amountDebit=true;
					//Create new row in csv file as for newly created accounts a statement info will not be present initially
					UserAccountStatement userAccountLatestStatement = new UserAccountStatement(
							ReadWriteAccountInfo.convertDateToStringDate(new Date()), "Dr", accountNumber, amount,
							copyOfUserAccountInfo.getBalance(), mode);
					userAccStmtsInfo.add(userAccountLatestStatement); // add info
					statementUpdated=true;
					break; // break the loop as account already matched so no further check needed
				}
			}
			if (!accountFound) 
			{
				//send error and exit as no further operation can be done
				throw new IllegalArgumentException("No account info found, please provide correct inputs!");
			}
			
			//Calculate result
			if (amountDebit && statementUpdated)
			{
				amountWithdrawn=SUCCESS;
			}
			
			//System.out.println("Updated user accounts: "+ userAccountsInfo);
			//Write to CSV with latest accounts info
			ReadWriteAccountInfo.writeToNewAccountCsv(this.acountsCsvPath, userAccountsInfo);
			//System.out.println("Updated user account statements: "+userAccStmtsInfo);
			//Write to CSV with latest accounts info
			ReadWriteAccountInfo.writeToNewAccountStmtCsv(this.accountStatementsCsvPath, userAccStmtsInfo);
		}
		catch (IOException e) 
		{
			amountWithdrawn=ERROR;
			e.printStackTrace();
		}
		return amountWithdrawn;
	}
	
	/**
	 * Check balance based on account number and name.
	 *
	 * @param accountNumber the account number
	 * @param name the name
	 * @return the balance
	 */
	public double checkBalance(int accountNumber, String name) 
	{
		double balance=0.0;
		try 
		{
			Set<UserAccountInfo> userAccountsInfo = ReadWriteAccountInfo.readUserAccounts(this.acountsCsvPath);
			boolean accountFound = false;
			for (UserAccountInfo userAccountInfo : userAccountsInfo) 
			{
				// if account number and name found then get the balance
				if (userAccountInfo.getAccountNumber()==accountNumber && userAccountInfo.getName().equals(name.trim())) 
				{
					System.out.println("A account number exists in the bank, checking balance..");
					accountFound = true;
					balance = userAccountInfo.getBalance();
					break; // break the loop as account already matched so no further check needed
				}
			}
			
			if (!accountFound) 
			{
				throw new IllegalArgumentException("No account info found, please provide correct inputs!");
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		//Return the final balance
		return balance;
	}
}
