package simple.banking.app.account.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import simple.banking.app.account.info.UserAccountInfo;
import simple.banking.app.account.info.UserAccountStatement;

/**
 * The Class ReadWriteAccountInfo.
 */
public class ReadWriteAccountInfo 
{
    
    /** The Constant COMMA. */
    public static final String COMMA = ",";
    
    /** The Constant NEWLINE. */
    public static final String NEWLINE = "\n";
    
    /** The Constant EMPTY. */
    public static final String EMPTY = "";

    /** The Constant ACCOUNTS_HEADER. */
    public static final String ACCOUNTS_HEADER = "account_number,name,balance";
    
    /** The Constant ACCOUNT_STMTS_HEADER. */
    public static final String ACCOUNT_STMTS_HEADER = "date,txn_type,account_number,txn_amount,balance,mode";

    
	/**
	 * Read user accounts.
	 *
	 * @param accountCsvInputPath the account csv input path
	 * @return the set of user accounts
	 */
	public static Set<UserAccountInfo> readUserAccounts(String accountCsvInputPath) throws IOException 
	{
		File inputAccountsCsvFile = new File(accountCsvInputPath);
		Set<UserAccountInfo> userAccountsInfo = null;
		//Checking if file is present
		if (inputAccountsCsvFile.isFile()) 
		{
			//to keep the entries in the order they are in csv as we are using set to keep things unique
			userAccountsInfo = new LinkedHashSet<UserAccountInfo>();
			BufferedReader buffReaderForCsv = null;
			FileReader csvFileReader = null;
			try 
			{
				csvFileReader = new FileReader(inputAccountsCsvFile);
				buffReaderForCsv = new BufferedReader(csvFileReader);
				String rowInCsv = EMPTY;
				int loopCounter = 0;
				while ((rowInCsv = buffReaderForCsv.readLine()) != null)
				{
					//System.out.println("Eachrow: "+rowInCsv);
					String[] data = rowInCsv.split(COMMA);// splitting to get each row
					if (loopCounter == 0) 
					{
						//Skip the first row as it is containing header information 
						//System.out.println("Skipping header: "+ data[0] + "|" + data[1]+"|"+data[2]);
						loopCounter++;
						continue;
					}
					else if (loopCounter > 0 && data.length > 0) 
					{
						//split the string so that all the information can be gathered
						UserAccountInfo userAcInfo = new UserAccountInfo(Integer.parseInt(data[0]), cleanUpString(data[1]), Double.parseDouble(data[2]));
						userAccountsInfo.add(userAcInfo);
						loopCounter++;
					}
				}
			} 
			catch (IOException e) 
			{
				System.err.println("Failed to read csv file located at: " + accountCsvInputPath);
				e.printStackTrace();
			} 
			finally 
			{
				if (buffReaderForCsv!=null) 
				{
					buffReaderForCsv.close();
				}
				if (csvFileReader!=null) 
				{
					csvFileReader.close();
				}
			}
		}
		return userAccountsInfo;
	}
	
	/**
	 * Read user account statement.
	 *
	 * @param accountStmCsvInputPath the account stm csv input path
	 * @return the set of user account statements
	 */
	public static List<UserAccountStatement> readUserAccountStatement(String accountStmCsvInputPath) throws IOException 
	{	
		File accountsStmtCsvFile = new File(accountStmCsvInputPath);
		List<UserAccountStatement> userAccountStatements = null;
		//Checking if file is present
		if (accountsStmtCsvFile.isFile()) 
		{
			//to keep the entries in the order they are in csv as we are using set to keep things unique
			userAccountStatements = new ArrayList<UserAccountStatement>();
			BufferedReader buffReaderForCsv = null;
			FileReader csvFileReader = null;
			try 
			{
				csvFileReader = new FileReader(accountsStmtCsvFile);
				buffReaderForCsv = new BufferedReader(csvFileReader);
				String rowInCsv = EMPTY;
				int loopCounter = 0;
				while ((rowInCsv = buffReaderForCsv.readLine()) != null)
				{
					//System.out.println("Eachrow: "+rowInCsv);
					String[] data = rowInCsv.split(COMMA);// splitting to get each row
					if (loopCounter == 0) 
					{
						//Skip the first row as it is containing header information 
						//System.out.println("Skipping header: " + data[0] + "|" + data[1] + "|" + data[2] +"|"+ data[3] + "|" + data[4] + "|" + data[5]);
						loopCounter++;
						continue;
					}
					else if (loopCounter > 0 && data.length > 0)	
					{
						//split the string so that all the information can be gathered
						UserAccountStatement userAccStmtInfo = new UserAccountStatement(
								cleanUpString(data[0]), cleanUpString(data[1]), Integer.parseInt(data[2]),
								Double.parseDouble(data[3]), Double.parseDouble(data[4]), cleanUpString(data[5]));
						userAccountStatements.add(userAccStmtInfo);
						loopCounter++;
					}
				}
			} 
			catch (IOException ioe) 
			{
				System.err.println("Failed to read csv file located at: " + accountStmCsvInputPath);
				ioe.printStackTrace();
			} 
			catch (Exception e)
			{
				System.err.println("Failed to process csv "+ e.getMessage());
				e.printStackTrace();
			}
			finally 
			{
				if (buffReaderForCsv != null) 
				{
					buffReaderForCsv.close();
				}
				if (csvFileReader!=null) 
				{
					csvFileReader.close();
				}
			}
		}
		return userAccountStatements;
	}
	
	/**
	 * Convert string date to date.
	 *
	 * @param date the date
	 * @return the date
	 */
	public static Date convertStringDateToDate(String date) throws ParseException 
	{
		//input format i am getting: 2020-10-10 14:06:42
		//System.out.println("Input date: "+date);
		//we are getting additional double quotes from csv for some reason, so delete it
		String finalDate = cleanUpString(date);
		Date outputDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(finalDate);
		return outputDate;
	}

	/**
	 * Clean up string.
	 *
	 * @param inputString the input string
	 * @return the string
	 */
	public static String cleanUpString(String inputString) {
		if (inputString.contains("\"")) {
			//Remove additional double quotes for a string which is coming from csv for some reason
			String firstPart = inputString.substring(inputString.indexOf("\"")+1);
			String finalPart = firstPart.substring(0, firstPart.indexOf("\""));
			return finalPart;
		}
		//return the string as is as quote is not preset
		return inputString;
	}
	
	/**
	 * Convert date to string date.
	 *
	 * @param date the date
	 * @return the string
	 */
	public static String convertDateToStringDate(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String resultDate = sdf.format(date); 
		return resultDate;
	}
	
	/**
	 * Append to new account csv.
	 *
	 * @param accountCsvPath the account csv path
	 * @param userAccountInfo the user account info
	 * @return true or false
	 */
	public static boolean appendToNewAccountCsv(String accountCsvPath, UserAccountInfo userAccountInfo) throws IOException 
	{
		boolean success = false;
		File inputAccountsCsvFile = new File(accountCsvPath);
		//Checking if file is present
		if (inputAccountsCsvFile.isFile()) 
		{
			FileWriter accountsCsvFileWriter = null;
	        try 
	        {
	            accountsCsvFileWriter = new FileWriter(inputAccountsCsvFile, true);	 //open csv file to append is true           
	            //Write a new account to the CSV file
                accountsCsvFileWriter.append(String.valueOf(userAccountInfo.getAccountNumber()));
                accountsCsvFileWriter.append(COMMA);
                accountsCsvFileWriter.append(userAccountInfo.getName());
                accountsCsvFileWriter.append(COMMA);
                accountsCsvFileWriter.append(String.valueOf(userAccountInfo.getBalance()));
                accountsCsvFileWriter.append(NEWLINE);
	            success = true;
	        } 
	        catch (IOException e) 
	        {
	        	System.err.println("Failed to update "+accountCsvPath);
	        	e.printStackTrace();
	        } 
	        finally 
	        {
	            if(accountsCsvFileWriter !=null) 
	            {
	                accountsCsvFileWriter.flush();
	                accountsCsvFileWriter.close();
	            }
	        }
		}
		return success;
	}
	
	/**
	 * Write to new account csv.
	 *
	 * @param accountCsvPath the account csv path
	 * @param userAccountsInfo the user accounts info
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean writeToNewAccountCsv(String accountCsvPath, Set<UserAccountInfo> userAccountsInfo) throws IOException 
	{
		boolean success = false;
		File inputAccountsCsvFile = new File(accountCsvPath);
		//Checking if file is present
		if (inputAccountsCsvFile.isFile()) 
		{
			FileWriter accountsCsvFileWriter = null;
	        try 
	        {
	            accountsCsvFileWriter = new FileWriter(inputAccountsCsvFile);
	            //setup header
	            accountsCsvFileWriter.append(ACCOUNTS_HEADER.toString());
	            accountsCsvFileWriter.append(NEWLINE);
	            
	            for (UserAccountInfo userAccountInfo : userAccountsInfo) {
	            	accountsCsvFileWriter.append(String.valueOf(userAccountInfo.getAccountNumber()));
	                accountsCsvFileWriter.append(COMMA);
	                accountsCsvFileWriter.append(userAccountInfo.getName());
	                accountsCsvFileWriter.append(COMMA);
	                accountsCsvFileWriter.append(String.valueOf(userAccountInfo.getBalance()));
	                accountsCsvFileWriter.append(NEWLINE);
				}
	            success = true;
	        } 
	        catch (IOException e) 
	        {
	        	System.err.println("Failed to update "+accountCsvPath);
	        	e.printStackTrace();
	        } 
	        finally 
	        {
	            if(accountsCsvFileWriter !=null) 
	            {
	                accountsCsvFileWriter.flush();
	                accountsCsvFileWriter.close();
	            }
	        }
		}
		return success;
	}
	
	/**
	 * Append to new account stmt csv.
	 *
	 * @param accountStmtCsvPath the account stmt csv path
	 * @param userAccountStatement the user account statement
	 * @return true, if successful
	 */
	public static boolean appendToNewAccountStmtCsv(String accountStmtCsvPath, UserAccountStatement userAccountStatement) throws IOException 
	{
		boolean success = false;
		File inputAccountStmtsCsvFile = new File(accountStmtCsvPath);
		//Checking if file is present
		if (inputAccountStmtsCsvFile.isFile()) 
		{
			FileWriter accountsStmtCsvFileWriter = null;
	        try 
	        {
	        	accountsStmtCsvFileWriter = new FileWriter(inputAccountStmtsCsvFile, true);	 //open csv file to append is true           
	            //Write a new account to the CSV file
	        	accountsStmtCsvFileWriter.append(userAccountStatement.getTransactionDate());
                accountsStmtCsvFileWriter.append(COMMA);
                accountsStmtCsvFileWriter.append(userAccountStatement.getTxnType());
                accountsStmtCsvFileWriter.append(COMMA);
                accountsStmtCsvFileWriter.append(String.valueOf(userAccountStatement.getAccountNumber()));
                accountsStmtCsvFileWriter.append(COMMA);
                accountsStmtCsvFileWriter.append(String.valueOf(userAccountStatement.getTxnAmount()));
                accountsStmtCsvFileWriter.append(COMMA);
                accountsStmtCsvFileWriter.append(String.valueOf(userAccountStatement.getBalance()));
                accountsStmtCsvFileWriter.append(COMMA);
                accountsStmtCsvFileWriter.append(userAccountStatement.getMode());
                accountsStmtCsvFileWriter.append(NEWLINE);
	            success = true;
	        } 
	        catch (IOException e) 
	        {
	        	System.err.println("Failed to update "+accountStmtCsvPath);
	        	e.printStackTrace();
	        } 
	        finally 
	        {
	            if(accountsStmtCsvFileWriter !=null) 
	            {
	            	accountsStmtCsvFileWriter.flush();
	            	accountsStmtCsvFileWriter.close();
	            }
	        }
		}
		return success;
	}
	
	/**
	 * Write to new account stmt csv.
	 *
	 * @param accountStmtCsvPath the account stmt csv path
	 * @param userAccountStatements the user account statements
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean writeToNewAccountStmtCsv(String accountStmtCsvPath, List<UserAccountStatement> userAccountStatements) throws IOException 
	{
		boolean success = false;
		File inputAccountStmtsCsvFile = new File(accountStmtCsvPath);
		//Checking if file is present
		if (inputAccountStmtsCsvFile.isFile()) 
		{
			FileWriter accountsStmtCsvFileWriter = null;
	        try 
	        {
	        	accountsStmtCsvFileWriter = new FileWriter(inputAccountStmtsCsvFile);
	        	//setup header
	        	accountsStmtCsvFileWriter.append(ACCOUNT_STMTS_HEADER.toString());
	        	accountsStmtCsvFileWriter.append(NEWLINE);
	        	for (UserAccountStatement userAccountStatement : userAccountStatements) {
	        		accountsStmtCsvFileWriter.append(userAccountStatement.getTransactionDate());
	                accountsStmtCsvFileWriter.append(COMMA);
	                accountsStmtCsvFileWriter.append(userAccountStatement.getTxnType());
	                accountsStmtCsvFileWriter.append(COMMA);
	                accountsStmtCsvFileWriter.append(String.valueOf(userAccountStatement.getAccountNumber()));
	                accountsStmtCsvFileWriter.append(COMMA);
	                accountsStmtCsvFileWriter.append(String.valueOf(userAccountStatement.getTxnAmount()));
	                accountsStmtCsvFileWriter.append(COMMA);
	                accountsStmtCsvFileWriter.append(String.valueOf(userAccountStatement.getBalance()));
	                accountsStmtCsvFileWriter.append(COMMA);
	                accountsStmtCsvFileWriter.append(userAccountStatement.getMode());
	                accountsStmtCsvFileWriter.append(NEWLINE);
				}
	            success = true;
	        } 
	        catch (IOException e) 
	        {
	        	System.err.println("Failed to update "+accountStmtCsvPath);
	        	e.printStackTrace();
	        } 
	        finally 
	        {
	            if(accountsStmtCsvFileWriter !=null) 
	            {
	            	accountsStmtCsvFileWriter.flush();
	            	accountsStmtCsvFileWriter.close();
	            }
	        }
		}
		return success;
	}
}
