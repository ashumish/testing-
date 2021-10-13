package simple.banking.app.account.info;

/**
 * The Class UserAccountStatement.
 */
public class UserAccountStatement implements Cloneable
{

	String transactionDate;
	String txnType;
	int accountNumber;
	double txnAmount;
	double balance;
	String mode;
	
	public UserAccountStatement(String transactionDate, String txnType, int accountNumber, double txnAmount, double balance, String mode) 
	{
		super();
		this.transactionDate = transactionDate;
		this.txnType = txnType;
		this.accountNumber = accountNumber;
		this.txnAmount = txnAmount;
		this.balance = balance;
		this.mode = mode;
	}

	public String getTransactionDate() 
	{
		return transactionDate;
	}

	public String getTxnType() 
	{
		return txnType;
	}

	public int getAccountNumber()
    {
		return accountNumber;
	}

	public double getTxnAmount() 
	{
		return txnAmount;
	}

	public double getBalance() 
	{
		return balance;
	}

	public String getMode() 
	{
		return mode;
	}

	public void setTransactionDate(String transactionDate) 
	{
		this.transactionDate = transactionDate;
	}

	public void setTxnType(String txnType) 
	{
		this.txnType = txnType;
	}

	public void setAccountNumber(int accountNumber) 
	{
		this.accountNumber = accountNumber;
	}

	public void setTxnAmount(double txnAmount) 
	{
		this.txnAmount = txnAmount;
	}

	public void setBalance(double balance) 
	{
		this.balance = balance;
	}

	public void setMode(String mode) 
	{
		this.mode = mode;
	}

	///////// Added clone method to keep a copy while resetting values
	/**
	 * Clone.
	 *
	 * @return the object
	 */
	@Override
	public Object clone() 
	{
	    try 
	    {
	        return (UserAccountStatement) super.clone();
	    } 
	    catch (CloneNotSupportedException e) 
	    {
	        return new UserAccountStatement(this.transactionDate, this.txnType, this.accountNumber, this.txnAmount, this.balance, this.mode);
	    }
	}

	@Override
	public String toString() {
		return "UserAccountStatement [transactionDate=" + transactionDate + ", txnType=" + txnType + ", accountNumber="
				+ accountNumber + ", txnAmount=" + txnAmount + ", balance=" + balance + ", mode=" + mode + "]";
	}

}
