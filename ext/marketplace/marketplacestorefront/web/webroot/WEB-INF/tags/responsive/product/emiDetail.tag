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
    <h3 class="modal-title" id="myModalLabel"> <span class="Emi-tableTitle">EMI T&C</span> <span class="Emi-subTitle">
	    <cms:pageSlot position="EMITermsandConditionsSlot" var="component">
			<cms:component component="${component}" />
		</cms:pageSlot>	
 
 </span> </h3>  	<!-- UF-48 -->	 
  </div>
</div>


</div>


</div>

<style>
.Emi-subTitle ul{
	text-align: left;
    list-style-type: disc;
    font-weight: normal;
    padding-left: 10px;
    padding-top: 10px;
}
.product-detail .Emi p.active.emi-tnc,
.product-detail .Emi p.emi-tnc{
	position: absolute;
    top: -38px;
    left: 131px;
    webkit-box-shadow: 0 -1px 3px rgba(138,134,138,1);
    -moz-box-shadow: 0 -1px 3px rgba(138,134,138,1);
   	box-shadow: 0 -1px 3px rgba(138,134,138,1);
}
.product-detail .Emi .modal-content p:before {
    width: 94px;
    background: #fff;
    z-index: 0;
    bottom: -5px;
}
.product-detail .Emi {
    margin: -9px 0 0 !important;
}
a.tab-emi{
	width: 50%;
    position: absolute;
   	top:0;
    padding: 8px 4px;
    text-align: center;
    font-size: 12px;
    text-transform: uppercase;
    background-color: #fff;
    color: #92002e;
    letter-spacing: 1px;
}
.showEmi.tab-emi{
	left:0;
	 margin-right: -3px;
}
.emi-tnc.tab-emi{
	right:0;
}
a.tab-emi.active{
    background-color: #b2b2b2;
    color: #fff;
}
.product-detail .Emi p+.modal-content {
    padding-top: 32px;
}
.product-detail .Emi .modal-content .Close {
    top: 32px;
}
</style>
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