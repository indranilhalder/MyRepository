/**
 *
 */
package com.tisl.mpl.util;

/**
 * @author TCS
 *
 */
public class MplEMICalculator
{


	/**
	 * This method takes termInMonths, interestRatePerMonth and totalAmount as parameters and returns the emi calculated
	 * using the input params. The formula is: E = P×r×(1 + r)^n/((1 + r)^n - 1) , where E is EMI where P is Principal
	 * Loan Amount, r is rate of interest calculated in monthly(interestRatePerMonth), n is the tenure(termInMonths)
	 *
	 * @param termInMonths
	 * @param interestRatePerMonth
	 * @param totalAmount
	 * @return emi(Double)
	 */
	public static Double emiCalculator(final Double termInMonths, final Double interestRatePerMonth, final Double totalAmount)
	{
		//Calculating (1 + r)^n
		final Double powerValue = Double.valueOf(Math.pow((1 + interestRatePerMonth.doubleValue()), termInMonths.doubleValue()));

		//Calculating the denominator, ie. ((1 + r)^n - 1)
		final Double denominator = Double.valueOf(powerValue.doubleValue() - 1);

		//Calculating the numerator, ie. P×r×(1 + r)^n
		final Double numerator = Double.valueOf(totalAmount.doubleValue() * interestRatePerMonth.doubleValue()
				* powerValue.doubleValue());

		//Calculating EMI, ie. E = P×r×(1 + r)^n/((1 + r)^n - 1)
		final Double emi = Double.valueOf(numerator.doubleValue() / denominator.doubleValue());
		return emi;
	}


	/**
	 * This method calculate and returns the total interest payable when emi is applied against the order amount
	 *
	 * @param emi
	 * @param termInMonths
	 * @param totalAmount
	 * @return interestPayable(Double)
	 */
	public static Double interestPayable(final Double emi, final Double termInMonths, final Double totalAmount)
	{
		//Calculating interestPayable=(emi*termInMonths)-totalAmount
		final Double interestPayable = Double.valueOf((emi.doubleValue() * termInMonths.doubleValue()) - totalAmount.doubleValue());
		return interestPayable;
	}
}
