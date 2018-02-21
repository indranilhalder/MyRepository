/**
 * 
 */
package com.hybris.oms.tata.tship.exceltocsv.pojo;

/**
 * @author Tech
 *
 */
public class CilqCashWalletPojo
{
     
		
		private String customerFirstName;
      private String customerLastName;
      private String customerEmailId;
      private String amount;
      private String bucketName;
      private String transactionId;
      private String walletId;
      private String cardNumber;
      private String cardExpiry;
      private String batchNumber;
      private String comments;
      private String remarks;
      
      /**
   	 * @return the customerFirstName
   	 */
   	public String getCustomerFirstName()
   	{
   		return customerFirstName;
   	}
   	/**
   	 * @param customerFirstName the customerFirstName to set
   	 */
   	public void setCustomerFirstName(String customerFirstName)
   	{
   		this.customerFirstName = customerFirstName;
   	}
   	/**
   	 * @return the customerLastName
   	 */
   	public String getCustomerLastName()
   	{
   		return customerLastName;
   	}
   	/**
   	 * @param customerLastName the customerLastName to set
   	 */
   	public void setCustomerLastName(String customerLastName)
   	{
   		this.customerLastName = customerLastName;
   	}
		
		/**
		 * @return the customerEmailId
		 */
		public String getCustomerEmailId()
		{
			return customerEmailId;
		}
		/**
		 * @param customerEmailId the customerEmailId to set
		 */
		public void setCustomerEmailId(String customerEmailId)
		{
			this.customerEmailId = customerEmailId;
		}
		/**
		 * @return the amount
		 */
		public String getAmount()
		{
			return amount;
		}
		/**
		 * @param amount the amount to set
		 */
		public void setAmount(String amount)
		{
			this.amount = amount;
		}
		/**
		 * @return the bucketName
		 */
		public String getBucketName()
		{
			return bucketName;
		}
		/**
		 * @param bucketName the bucketName to set
		 */
		public void setBucketName(String bucketName)
		{
			this.bucketName = bucketName;
		}
		/**
		 * @return the transactionId
		 */
		public String getTransactionId()
		{
			return transactionId;
		}
		/**
		 * @param transactionId the transactionId to set
		 */
		public void setTransactionId(String transactionId)
		{
			this.transactionId = transactionId;
		}
		/**
		 * @return the walletId
		 */
		public String getWalletId()
		{
			return walletId;
		}
		/**
		 * @param walletId the walletId to set
		 */
		public void setWalletId(String walletId)
		{
			this.walletId = walletId;
		}
		/**
		 * @return the cardNumber
		 */
		public String getCardNumber()
		{
			return cardNumber;
		}
		/**
		 * @param cardNumber the cardNumber to set
		 */
		public void setCardNumber(String cardNumber)
		{
			this.cardNumber = cardNumber;
		}
		/**
		 * @return the cardExpiry
		 */
		public String getCardExpiry()
		{
			return cardExpiry;
		}
		/**
		 * @param cardExpiry the cardExpiry to set
		 */
		public void setCardExpiry(String cardExpiry)
		{
			this.cardExpiry = cardExpiry;
		}
		/**
		 * @return the batchNumber
		 */
		public String getBatchNumber()
		{
			return batchNumber;
		}
		/**
		 * @param batchNumber the batchNumber to set
		 */
		public void setBatchNumber(String batchNumber)
		{
			this.batchNumber = batchNumber;
		}
		/**
		 * @return the comments
		 */
		public String getComments()
		{
			return comments;
		}
		/**
		 * @param comments the comments to set
		 */
		public void setComments(String comments)
		{
			this.comments = comments;
		}
		/**
		 * @return the remarks
		 */
		public String getRemarks()
		{
			return remarks;
		}
		/**
		 * @param remarks the remarks to set
		 */
		public void setRemarks(String remarks)
		{
			this.remarks = remarks;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "CilqCashWalletPojo [customerFirstName=" + customerFirstName + ", customerLastName=" + customerLastName
					+ ", customerEmailId=" + customerEmailId + ", amount=" + amount + ", bucketName=" + bucketName + ", transactionId="
					+ transactionId + ", walletId=" + walletId + ", cardNumber=" + cardNumber + ", cardExpiry=" + cardExpiry
					+ ", batchNumber=" + batchNumber + ", comments=" + comments + ", remarks=" + remarks + "]";
		}
}
