<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>


<!-- -EMI changes -->
<div class="Emi Emi_wrapper">
<p onclick="" ><!-- id="emiStickerId" class="emi" style="display:none; width: 80%;" -->
	<spring:theme code="marketplace.emiavailable" />
	<%--  <a type="button" name="yes" id="prodEMI"
		data-target="#modalProd" onclick="openPopForBankEMI()"
		data-toggle="modal"><spring:theme code="marketplace.emiinfo"></spring:theme></a>  		 --%>		
						
		<input id="prodPrice" type="hidden" />
</p>
<!-- TPR-630 -->
<div id="EMImodal-content" class="modal-content">
<span class="Close">+</span>
  <div class="modal-header">   
    <h4 class="modal-title" id="myModalLabel"> <span class="Emi-tableTitle">EMI Details</span> <span class="Emi-subTitle">EMI for the product is provided by the below banks</span> </h4>
  </div>
  <div class="modal-body" id="modelId">
			<div class="SelectWrap">
				<select name="bankNameForEMI" id="bankNameForEMI"
					onchange="populateEMIDetailsForPDP()"></select>
			</div>
	<div id="emiTableDiv" class="other-sellers"> <span id="emiSelectBank" style="display:none">Please select a Bank again</span><span id="emiNoData" style="display:none">No data for the bank.</span>
      <table id="EMITermTable" class="other-sellers-table emi-table">
        <thead id="emiTableTHead" style="display:none">
			<th><b><spring:theme
						code="checkout.multi.paymentMethod.addPaymentDetails.terms" /></b></th>
			<th><b><spring:theme
						code="checkout.multi.paymentMethod.addPaymentDetails.interestRate" /></b></th>
			<th><b><spring:theme
						code="checkout.multi.paymentMethod.addPaymentDetails.monthlyInstallment" /></b></th>
			<th><b><spring:theme
						code="checkout.multi.paymentMethod.addPaymentDetails.interestPayable" /></b></th>
		</thead>
		
		<tbody id="emiTableTbody">
		<span id="emiSelectBank" style="display:none"><spring:theme code="emi.nobankselect"/></span>
		<span id="emiNoData" style="display:none"><spring:theme code="emi.nodata"/></span>
		</tbody>
      </table>
    </div>
  </div>
</div>
</div>