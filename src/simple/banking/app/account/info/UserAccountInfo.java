package simple.banking.app.account.info;

/**
 * The Class UserAccountInfo.
 */
public class UserAccountInfo implements Cloneable
{
	int accountNumber;
	String name;
	double balance;

	public UserAccountInfo(int accountNumber, String name, double balance) 
	{
		super();
		this.accountNumber = accountNumber;
		this.name = name;
		this.balance = balance;
	}

	public int getAccountNumber() 
	{
		return accountNumber;
	}

	public String getName() 
	{
		return name;
	}

	public double getBalance() 
	{
		return balance;
	}

	public void setAccountNumber(int accountNumber) 
	{
		this.accountNumber = accountNumber;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setBalance(double balance)
	{
		this.balance = balance;
	}

	@Override
	public String toString() 
	{
		return "UserAccountInfo [accountNumber=" + accountNumber + ", name=" + name + ", balance=" + balance + "]";
	}

	//////////////////////// Added hascode and equals methods to keep this object as unique when adding/searching in list 
	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + accountNumber;
		long temp;
		temp = Double.doubleToLongBits(balance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAccountInfo other = (UserAccountInfo) obj;
		if (accountNumber != other.accountNumber)
			return false;
		if (Double.doubleToLongBits(balance) != Double.doubleToLongBits(other.balance))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
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
	        return (UserAccountInfo) super.clone();
	    } 
	    catch (CloneNotSupportedException e) 
	    {
	        return new UserAccountInfo(this.accountNumber, this.name, this.balance);
	    }
	}
}
