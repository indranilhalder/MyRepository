<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>


<style>
.out_of_stock {
	color: #FE2E2E;
}
.dlist_message {
	color: #FE2E2E;
}
.addToCartTitle{
	display:none;
}
.no_inventory{
	float: left;
}
</style>
<script> 

$(document).ready(function(){
	var code='${product.code}';
	if( $("#variant,#sizevariant option:selected").val()=="#"){
		$("#selectedSizeVariant").val("");
	}
	else{
		$("#selectedSizeVariant").val(code);
	}
	
    $("#addToCartButton").click(function(){  	
     $("#selectSizeId").hide();
   	 var stock=$("#stock").val();
   	 var quantity= $("#qty").val();
  	 var isShowSize= $("#showSize").val();
  	 
  	 
   	 //Changes for pdp CR
   	if(!$("#variant li ").hasClass("selected") && typeof($(".variantFormLabel").html())== 'undefined' && $("#ia_product_rootCategory_type").val()!='Electronics' && $("#ia_product_rootCategory_type").val()!='Watches' && isShowSize=='true'){
  		/* alert("please select size !"+isShowSize); */
   		//$("#addToCartFormTitle").html("<font color='#ff1c47'>" + $('#selectSizeId').text() + "</font>");
		//$("#addToCartFormTitle").show();
		//For pdp analytics changes
		//utag.link({"error_type":"size_not_selected"});
		$("#sizeGuideAction").val('#addToCartButton')
		$(".size-guide").click();
 	    return false;
   	 }
   	 
   	/* if( $("#variant,#sizevariant option:selected").val()=="#")
 	  {
 		$("#addToCartFormTitle").html("<font color='#ff1c47'>" + $('#selectSizeId').text() + "</font>");
		$("#addToCartFormTitle").show();
 	 return false;
 	  }	 */ 
   	 /* var pincodecheked=$("#pinCodeChecked").val();
   	
   	 if(pincodecheked=="true"){ 
   		
   		 if(parseInt(stock)<parseInt(quantity)){
   			$("#addToCartFormTitle").html("<font color='#ff1c47'>" + $('#inventory').text() + "</font>");
   			$("#addToCartFormTitle").show().fadeOut(6000);
			 return false;
   	 }
   	 else{
   		 $("#inventory").hide();
   		 $("#addToCartButton").show();
   	 }
   	 }
   	 else{
   		 if(parseInt(stock)<parseInt(quantity)){
   			$("#addToCartFormTitle").html("<font color='#ff1c47'>" + $('#inventory').text() + "</font>");
   			$("#addToCartFormTitle").show().fadeOut(6000);
   			 return false;
       	 }
       	 else{
       		 $("#inventory").hide();
       		 $("#addToCartButton").show();
       	 }

   	 } */
   	   
   }); 
}); 

</script> 
<%-- <c:url value="${url}" var="addToCartUrl" /> --%>
<c:url value="/cart/addBag" var="addToCartUrl" />

<%-- <div id="addToCartFormTitle" class="addToCartTitle">
	<spring:theme code="basket.added.to.basket" />
	<spring:theme code="product.addtocart.success" />
</div> --%>
<span id="addtobag" style="display:none"><spring:theme code="product.addtocart.success"/></span>
<span id="addtobagerror" style="display:none"><spring:theme code="product.wishlist.outOfStock"/></span>
<span id="bagtofull" style="display:none"><spring:theme code="product.addtocart.aboutfull"/></span>
<span id="bagfull" style="display:none"><spring:theme code="product.bag"/></span>
<form:form method="post" name="addToCartFormId" id="addToCartForm" class="add_to_cart_form"
	action="#">
	<input type="hidden" id="sizeGuideAction"/>
	<%-- <c:if test="${product.purchasable}"> --%>
	<input type="hidden" maxlength="3" size="1" id="qty" name="qty"
		class="qty js-qty-selector-input" value="1" />
	<input type="hidden" maxlength="3" size="1" id="stock" name="stock"
		value="" />

	<input type="hidden" maxlength="3" size="1" id="pinCodeChecked"
		name="pinCodeChecked" value="false">
	<%-- </c:if> --%>
	<input type="hidden" name="productCodePost" id="productCodePost"
		value="${product.code}" />
	<input type="hidden" name="wishlistNamePost" id="wishlistNamePost"
		value="N" />
	<input type="hidden" maxlength="3" size="" id="ussid" name="ussid" class="ussidPdp"
		value="" />
	<input type="hidden" id="showSize" name="showSize" value="${showSizeGuideForFA}" />
	<%-- <span id="inventory" style="display: none"><p class="inventory">
			<font color="#ff1c47"><spring:theme code="Product.outofinventory" /></font>
		</p></span>
	<span id="noinventory" style="display: none"><p class="noinventory">
			<font color="#ff1c47">You are about to exceede maximum inventory</font>
		</p></span> --%>
	
    <span id="addToCartFormnoInventory" style="display: none" class="no_inventory"><p class="inventory">
			<font color="#ff1c47"><spring:theme code="Product.outofinventory" /></font>
		</p></span>
	<span id="addToCartFormexcedeInventory" style="display: none"><p class="inventory">
			<font color="#ff1c47">Please decrease the quantity</font>
		</p></span>
	 <!-- TISST-13959 fix  -->
<%-- 	<span id="dListedErrorMsg" style="display: none"  class="dlist_message">
		<spring:theme code="pdp.delisted.message" />
	</span> --%>
	<span id="outOfStockId" style="display: none"  class="out_of_stock">
<%-- 		<spring:theme code="product.product.outOfStock" /> --%>
		<%-- <input type="button" id="add_to_wishlist" onClick="openPop();" id="wishlist" class="wishlist" data-toggle="popover" data-placement="bottom" value="<spring:theme code="text.add.to.wishlist"/>"/> --%>
		<%--<input type="button" id="add_to_wishlist" onClick="addToWishlist();" id="wishlist" class="wishlist" data-toggle="popover" value="<spring:theme code="text.add.to.wishlist"/>"/>--%>
		<div id="OutOfStockAddToWishList" class="btn-block btn btn-default btn-lg btn-block">
			<spring:theme code="text.add.to.wishlist"/></div>
	</span>
	<span id="selectSizeId" style="display: none;color:#ff1c47"><spring:theme code="variant.pleaseselectsize"/></span>
	<span id="addToCartButtonId">
		<span id="addToCartFormTitleSuccess"></span>
		<button id="addToCartButton" type="button" class="btn-block js-add-to-cart btn btn-default btn-lg btn-block">
			<spring:theme code="basket.add.to.basket" />
		</button>
		<button
				id="addToCartButton-wrong" type="button"
				class="btn-block">
			<spring:theme code="basket.add.to.basket" />
		</button>
	</span>
</form:form>

