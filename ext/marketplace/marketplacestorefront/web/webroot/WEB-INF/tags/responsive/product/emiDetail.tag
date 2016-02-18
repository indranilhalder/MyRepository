<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>


<!-- -EMI changes -->
<div id="emiStickerId" class="emi" style="display:none; width: 80%;">
	<spring:theme code="marketplace.emiavailable" />
	 <a type="button" name="yes" id="prodEMI"
		data-target="#modalProd" onclick="openPopForBankEMI()"
		data-toggle="modal"><spring:theme code="marketplace.emiinfo"></spring:theme></a>  				
						
		<input id="prodPrice" type="hidden" />
</div>

<div class="modal fade" id="modalProd" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	
	<div class="content" style="width:50%">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">
					<span class="Emi-tableTitle">EMI Details</span>
					<span class="Emi-subTitle">EMI for the product is provided by the below banks</span>
				</h4>
			</div>
			<div class="modal-body" id="modelId">

				<select name="bankNameForEMI" id="bankNameForEMI"
	onchange="getSelectedEMIBankForPDP()">
</select>

<div id="emiTableDiv" class="other-sellers">
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
			<div class="modal-footer">
				<!--   <button type="button" class="btn btn-default" 
               data-dismiss="modal">Close
            </button> -->
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
	<div class="overlay" data-dismiss="modal"></div>
</div>