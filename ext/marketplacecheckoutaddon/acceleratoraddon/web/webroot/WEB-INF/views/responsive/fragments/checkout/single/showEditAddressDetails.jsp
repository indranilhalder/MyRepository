<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>

<script>
	maxL=120;
	var bName = navigator.appName;
	/* function taLimit(taObj) {
		if (taObj.value.length==maxL) return false;
		return true;
	} */
	
	function taCount(taObj,Cnt) { 
		objCnt=createObject(Cnt);
		objVal=taObj.value;
		if (objVal.length>maxL) objVal=objVal.substring(0,maxL);
		if (objCnt) {
			if(bName == "Netscape"){	
				objCnt.textContent=maxL-objVal.length;}
			else{objCnt.innerText=maxL-objVal.length;}
		}
	
		return true;
	}
	function createObject(objId) {
		if (document.getElementById) return document.getElementById(objId);
		else if (document.layers) return eval("document." + objId);
		else if (document.all) return eval("document.all." + objId);
		else return eval("document." + objId);
	}
	 /**************End of character count********/
	 
	 myLen=document.getElementById("address.line1").value.length;
		$("#myCounter").html((120 - myLen));
	//TISST-13010
	$(document).ready(function() {
		showPromotionTag();
	}); //TPR-1214
	$("#newAddressButton,#newAddressButtonUp").click(function() {
		ACC.singlePageCheckout.postEditAddress(this);
		return false;
	});
</script>
<div class="formaddress">
	<div class="heading-form">
		<h3>Edit Address</h3>
	</div>
	<div class="checkout-indent left-block address-form ">
		<ul class="product-block addresses new-form account-section">
			<li
				class="item account-section-content	 account-section-content-small ">
				<address:addressFormSelector supportedCountries="${countries}"
					regions="${regions}" cancelUrl="${currentStepUrl}"
					country="${country}" />
			</li>
		</ul>
	</div>
</div>