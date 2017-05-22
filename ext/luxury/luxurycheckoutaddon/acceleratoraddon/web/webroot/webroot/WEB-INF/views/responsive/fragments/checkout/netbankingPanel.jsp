<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<li class="item cardForm"><input type="hidden" id="juspayOrderId" />
	<div id="netbankingIssueError" class="error-message">
		<spring:theme
			code="checkout.multi.paymentMethod.netbankingIssue.Error" />
	</div> <c:if test="${not empty popularBankNames}">
		<p class="popular-netbanks"><spring:theme
				code="checkout.multi.paymentMethod.addPaymentDetails.paymentNetbanking.popularBanks.pick" /></p>
		<p class="popular-banks"><spring:theme
				code="checkout.multi.paymentMethod.addPaymentDetails.paymentNetbanking.popularBanks" /></p>
		
		<ul>
			<c:forEach var="bank" items="${popularBankNames}" varStatus="status">

				<li><input type="radio" class="NBBankCode"
					name="priority_banks" value="${bank.bankCode}"
					id="radioButton_bankCode${status.index}"
					onchange="deselectSelection()" /> <label
					for="radioButton_bankCode${status.index}"><img
						src='${bank.bankLogoUrl}'> <c:if
							test="${empty bank.bankLogoUrl}">
							<c:out value="${bank.bankName}" />
						</c:if></label> <input type="hidden" name="NBBankName"
					id="NBBankName${status.index}" value="${bank.bankName}" /></li>
			</c:forEach>
		</ul>
	</c:if> 
	<c:if test="${not empty otherBankNames}">
		<div class="bank-select">
			<label><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.otherNBBanks"/></label>
			<select name="NBBankCode" id="bankCodeSelection"
				onchange="deselectRadio()">
				<option value="select"><spring:theme
						code="checkout.multi.paymentMethod.addPaymentDetails.selectBank" /></option>
						<c:forEach var="bank" items="${popularBankNames}" varStatus="status">
						<option value="${bank.bankCode}" class="popular_bank_option">${bank.bankName}</option>
						</c:forEach>
						<optgroup label="---------------------" class="other_bank_label"></optgroup>
				<c:forEach var="bankMap" items="${otherBankNames}">
					<option value="${bankMap.value}">${bankMap.key}</option>
				</c:forEach>
			</select>

		</div>
	</c:if>
	<div id="netbankingError">
		<spring:theme code="checkout.multi.paymentMethod.netbanking.Error" />
	</div>
</li>