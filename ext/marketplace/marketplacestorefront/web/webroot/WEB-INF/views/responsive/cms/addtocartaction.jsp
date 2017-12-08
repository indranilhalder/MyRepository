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
	
	else {
		$("#selectedSizeVariant").val(code);
	
	}
	 
    $("#addToCartButton").click(function(){  	
     $("#selectSizeId").hide();
     
   	 var stock=$("#stock").val();
   	 var quantity= $("#qty").val();
  	 var isShowSize= $("#showSize").val();
  	 
  	 
   	 //Changes for pdp CR

   	if(!$("#variant li ").hasClass("selected") && typeof($(".variantFormLabel").html())== 'undefined' && $("#ia_product_rootCategory_type").val()!='Electronics' && $("#ia_product_rootCategory_type").val()!='HomeFurnishing'&& $("#ia_product_rootCategory_type").val()!='Watches' && isShowSize=='true'){
  		/* alert("please select size !"+isShowSize); */
   		$("#addToCartFormTitle").html("<font color='#fff'>" + $('#selectSizeId').text() + "</font>");
		$("#addToCartFormTitle").show();
		if ($(window).width() < 768) {
			setTimeout(function(){
				$('#addToCartFormTitle').fadeOut(2000);		
			 },2000);
			//UF-390
			$('html, body').animate({
	              scrollTop: $('.product-info .product-image-container.device').height()
	        }, 1000);
		}	
		//For pdp analytics changes
		utag.link({"error_type":"size_not_selected"});
 	    return false;

   /* 	 }     	 
   	} 	
   		  	
    	utag.link({
			link_obj: this,
			link_text: 'addtobag' ,
			event_type : 'addtobag_winner_seller' ,
			product_sku : productCodeArray              // Product code passed as an array for Web Analytics - INC_11511  fix
		}); */

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
<span id="exchangeRestriction" style="display:none"><spring:theme code="product.addtocart.exchange.qty.error"/></span>

<%--TPR-5346 --%>
 <span id="bagfullproduct" style="display:none"><spring:theme code="product.bag.maxlimit"/></span>
 <%--TPR-5346 --%>

<form:form method="post" name="addToCartFormId" id="addToCartForm" class="add_to_cart_form"
	action="#">
	<%-- <c:if test="${product.purchasable}"> --%>
	<input type="hidden" maxlength="3" size="1" id="qty" name="qty"
		class="qty js-qty-selector-input" value="1" />
	<input type="hidden" maxlength="3" size="1" id="stock" name="stock"
		value="" />
			<!-- SDI-1023 -->
		<input type="hidden" maxlength="3" size="1" id="winning_product_stock" name="winning_product_stock" value="">
		
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
	
	<!-- TPR-4966 -->
	
    <%-- <span id="addToCartFormnoInventory" style="display: none" class="no_inventory"><p class="inventory">
			<font color="#ff1c47"><spring:theme code="Product.outofinventory" /></font>
		</p></span>
	<span id="addToCartFormexcedeInventory" style="display: none"><p class="inventory">
			<font color="#ff1c47">Please decrease the quantity</font>
		</p></span> --%>
		
		<!-- TPR-4966 -->
		
	 <!-- TISST-13959 fix  -->
<%-- 	<span id="dListedErrorMsg" style="display: none"  class="dlist_message">
		<spring:theme code="pdp.delisted.message" />
	</span> --%>
	<span id="outOfStockId" style="display: none"  class="out_of_stock">
<%-- 		<spring:theme code="product.product.outOfStock" /> --%>
		<%-- <input type="button" id="add_to_wishlist" onClick="openPop();" id="wishlist" class="wishlist" data-toggle="popover" data-placement="bottom" value="<spring:theme code="text.add.to.wishlist"/>"/> --%>
		<input type="button" id="add_to_wishlist" onClick="addToWishlist();" id="wishlist" class="wishlist" data-toggle="popover" value="<spring:theme code="text.add.to.wishlist"/>"/>
	</span>
	<%-- <span id="selectSizeId" style="display: none;color:#ff1c47"><spring:theme code="variant.pleaseselectsize"/></span> --%>
	
	<c:choose> 
	<c:when test="${ product.rootCategory =='FineJewellery' || product.rootCategory =='FashionJewellery'}">
	    <spring:eval expression="T(de.hybris.platform.util.Config).getParameter('mpl.jewellery.category')" var="lengthVariant"/>
     	<c:set var = "categoryListArray" value = "${fn:split(lengthVariant, ',')}" />
		<c:forEach items="${product.categories}" var="categories">
   			<c:forEach items = "${categoryListArray}" var="lengthVariantArray">
   				<c:if test="${categories.code eq lengthVariantArray}">
   				 	<c:set var="lengthSize" value="true"/>
   				</c:if> 
   			</c:forEach>
   		</c:forEach>	  
   		<c:choose>
   			<c:when test="${true eq lengthSize}">
   				<span id="selectSizeId" style="display: none;color:#ff1c47"><spring:theme code="variant.pleaseselectlength"/></span>
   			</c:when>
   			<c:otherwise>
				<span id="selectSizeId" style="display: none;color:#ff1c47"><spring:theme code="variant.pleaseselectsize"/></span> 
   			</c:otherwise>
   		</c:choose>
	</c:when>
	<c:otherwise>
    	<span id="selectSizeId" style="display: none;color:#ff1c47"><spring:theme code="variant.pleaseselectsize"/></span>
	</c:otherwise>
	</c:choose>	
	
	<!-- UF-160 -->
	<span id="addToCartLargeAppliance" style="display: none;color:#ff1c47"><spring:theme code="product.addToCart.largeAppliance.error"/></span>

	<span id="addToCartButtonId">
		<span id="addToCartFormTitleSuccess"></span>
		<button style="display: none;"
				id="addToCartButton" type="button"
				class="btn-block js-add-to-cart">
			<spring:theme code="basket.add.to.basket" />
		</button>
		<button
				id="addToCartButton-wrong" type="button"
				class="btn-block">
			<spring:theme code="basket.add.to.basket" />
		</button>
	</span>
</form:form>

