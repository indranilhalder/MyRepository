<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>


<!-- -EMI changes -->
<div class="Emi Emi_wrapper" id="emiStickerId" style="display:none">
<p onclick="" ><!-- id="emiStickerId" class="emi" style="display:none; width: 80%;" -->
	<spring:theme code="marketplace.emiavailable" />
	<%--  <a type="button" name="yes" id="prodEMI"
		data-target="#modalProd" onclick="openPopForBankEMI()"
		data-toggle="modal"><spring:theme code="marketplace.emiinfo"></spring:theme></a>  		 --%>		
						
		<input id="prodPrice" type="hidden" />
</p>
<!-- TPR-630 -->
<div id="EMImodal-content" class="modal-content">
<a class="showEmi tab-emi active"> EMI Details <!-- TPR-7417 -->
</a>
<a class="emi-tnc tab-emi"> EMI T&C
</a>
<div class="emi-availability" id="emiAvailability">
<span class="Close"></span>
  <div class="modal-header">   
    <h3 class="modal-title" id="myModalLabel"> <span class="Emi-tableTitle">EMI Details</span> <span class="Emi-subTitle">EMI for the product is provided by the following banks</span> </h3>  	<!-- UF-48 -->	 
  </div>
  <div class="modal-body" id="modelId">
			<div class="SelectWrap">
				<select name="bankNameForEMI" id="bankNameForEMI"
					onchange="populateEMIDetailsForPDP()"></select>
			</div>
	<div id="emiTableDiv" class="other-sellers"> <span id="emiSelectBank" style="display:none">Please select a Bank again</span><span id="emiNoData" style="display:none">No data for the bank.</span>
      <table id="EMITermTable" class="other-sellers-table emi-table">
        <thead id="emiTableTHead" style="display:none">
			<th width="5%"><spring:theme
						code="checkout.multi.paymentMethod.addPaymentDetails.terms" /></th>
			<th width="25%"><spring:theme
						code="checkout.multi.paymentMethod.addPaymentDetails.interestRate" /></th>
			<th width="35%"><spring:theme
						code="checkout.multi.paymentMethod.addPaymentDetails.monthlyInstallment" /></th>
			<th width="35%"><spring:theme
						code="checkout.multi.paymentMethod.addPaymentDetails.interestPayable" /></th>
		</thead>
		
		<tbody id="emiTableTbody">
		<span id="emiSelectBank" style="display:none"><spring:theme code="emi.nobankselect"/></span>
		<span id="emiNoData" style="display:none"><spring:theme code="emi.nodata"/></span>
		</tbody>
      </table>
    </div>
  </div>
</div>
<!-- TPR-7417 -->
<div class="" id="emiTnC" style="display:none">
<span class="Close"></span>
  <div class="modal-header">   
    <h3 class="modal-title" id="myModalLabel"> <span class="Emi-tableTitle">EMI T&C</span> <span class="Emi-subTitle tnc-subtitle">
	    <cms:pageSlot position="EMITermsandConditionsSlot" var="component">
			<cms:component component="${component}" />
		</cms:pageSlot>	
 
 </span> </h3>  	<!-- UF-48 -->	 
  </div>
</div>


</div>


</div>

<script>
$('.showEmi').click(function () {
    $('#emiTnC').hide();
    $('#emiAvailability').show();
    $('.emi-tnc.active').removeClass('active');
    $(this).addClass('active');
}),
$('.emi-tnc').click(function () {
    $('#emiTnC').show();
    $('#emiAvailability').hide();
    $('.showEmi.active').removeClass('active');
    $(this).addClass('active');
});
</script>