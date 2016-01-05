<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



	 <p class="title">  ${order.mplPaymentInfo.paymentOption} Information:</p>
   <address>
   <c:if test="${not empty order.mplPaymentInfo.paymentOption && order.mplPaymentInfo.paymentOption eq 'COD'}">
    
   ${order.mplPaymentInfo.cardAccountHolderName}<br>
   </c:if>
   <c:if test="${not empty order.mplPaymentInfo.paymentOption && order.mplPaymentInfo.paymentOption eq 'Credit Card'}">

   		${order.mplPaymentInfo.cardAccountHolderName}<br>
     	${fn:escapeXml(order.mplPaymentInfo.cardCardType)}&nbsp;<spring:theme code="text.card.number"/>&nbsp; ${fn:escapeXml(order.mplPaymentInfo.cardIssueNumber)} <br>
       <spring:theme code="paymentMethod.paymentDetails.expires" arguments="${fn:escapeXml(order.mplPaymentInfo.cardExpirationMonth)},${fn:escapeXml(order.mplPaymentInfo.cardExpirationYear)}"/><br>
    </c:if>
     <c:if test="${not empty order.mplPaymentInfo.paymentOption && order.mplPaymentInfo.paymentOption eq 'Debit Card'}">
 
  		${order.mplPaymentInfo.cardAccountHolderName}<br>
    	${fn:escapeXml(order.mplPaymentInfo.cardCardType)}&nbsp;<spring:theme code="text.card.number"/>&nbsp; ${fn:escapeXml(order.mplPaymentInfo.cardIssueNumber)} <br>
       <spring:theme code="paymentMethod.paymentDetails.expires" arguments="${fn:escapeXml(order.mplPaymentInfo.cardExpirationMonth)},${fn:escapeXml(order.mplPaymentInfo.cardExpirationYear)}"/><br>
    </c:if>
     <c:if test="${not empty order.mplPaymentInfo.paymentOption && order.mplPaymentInfo.paymentOption eq 'Netbanking'}">
  
<%--    ${order.mplPaymentInfo.cardAccountHolderName}<br> --%>
   	Bank- ${order.mplPaymentInfo.bank}
    </c:if>
     <c:if test="${not empty order.mplPaymentInfo.paymentOption && order.mplPaymentInfo.paymentOption eq 'EMI'}">
      
   		${order.mplPaymentInfo.cardAccountHolderName}<br>
    	${fn:escapeXml(order.mplPaymentInfo.cardCardType)}&nbsp;<spring:theme code="text.card.number"/>&nbsp; ${fn:escapeXml(order.mplPaymentInfo.cardIssueNumber)} <br>
    	<spring:theme code="paymentMethod.paymentDetails.expires" arguments="${fn:escapeXml(order.mplPaymentInfo.cardExpirationMonth)},${fn:escapeXml(order.mplPaymentInfo.cardExpirationYear)}"/><br>
    Bank&nbsp;-&nbsp;${order.mplPaymentInfo.bank}<br>
    Term&nbsp;-&nbsp; ${order.mplPaymentInfo.emiInfo.term}<br>
    Interest Rate&nbsp;-&nbsp; ${order.mplPaymentInfo.emiInfo.interestRate}<br>
    Monthly Installment&nbsp;-&nbsp; ${order.mplPaymentInfo.emiInfo.monthlyInstallment}<br>
    Total Interest Payable&nbsp;-&nbsp;  ${order.mplPaymentInfo.emiInfo.interestPayable}
    </c:if>
   </address>   
   