<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>


<!-- CSS to be added to style.css starts here 27/04/2017  -->

<style >
/*Exchange*/
 .Exchange .SelectWrap,
.Exchange p:after,
.select-wrapper:after,
.Star .overall .Bottom > ul > li.active:after{font-family: "FontAwesome";}
 
.product-detail .Exchange {
	position: relative;
	margin: -15px 0 0;
}

.product-detail .Exchange p {
	font-size: 14px;
	letter-spacing: 1px;
	left: -10px;
	color: #a9143c;
	position: relative;
	display: inline-block;
	text-decoration: underline;
	cursor: pointer;
	padding: 12px 25px 12px 12px;
}

.product-detail .Exchange p:before {
	background: #fff;
	bottom: 0;
	content: "";
	height: 12px;
	position: absolute;
	right: 0;
	width: 145px;
	z-index: 9999;
}

.product-detail .Exchange p.active {
	-webkit-box-shadow: 0px 0px 10px 0px rgba(138, 134, 138, 1);
	-moz-box-shadow: 0px 0px 10px 0px rgba(138, 134, 138, 1);
	box-shadow: 0px 0px 10px 0px rgba(138, 134, 138, 1);
}

.product-detail .Exchange p:after {
	color: #a9183c;
	content: "\f107";
	display: inline-block;
	font-size: 18px;
	position: absolute;
	right: 8px;
	top: 10px;
}

.product-detail .Exchange p+.modal-content {
	display: none;
	position: absolute;
	left: -10px;
	top: 38px;
	width: 575px;
	background: #fff;
	z-index: 10;
	text-align: center;
	-webkit-box-shadow: 0px 0px 10px 0px rgba(138, 134, 138, 1);
	-moz-box-shadow: 0px 0px 10px 0px rgba(138, 134, 138, 1);
	box-shadow: 0px 0px 10px 0px rgba(138, 134, 138, 1);
}

.product-detail .Exchange p.active+.modal-content {
	display: block;
}

.product-detail .Exchange-tableTitle {
	font-size: 18px;
	font-weight: bold;
	text-transform: uppercase;
}

.product-detail .Exchange-subTitle {
	font-size: 13px;
	letter-spacing: 0.5px;
	margin: 0;
}

.modal-header {
	padding: 15px 15px 5px;
}

.product-detail .Exchange .SelectWrap {
	display: inline-block;
	position: relative;
}

/* .Exchange .SelectWrap:after {
	color: #000;
	content: "\f107";
	display: inline-block;
	font-size: 25px;
	position: absolute;
	right: 8px;
	top: 3px;
} */

.product-detail #bankNameForEMI {
	width: 250px;
	border: 2px solid #f5f5f5;
}

.product-detail .Exchange p.active.mobile {
	box-shadow: none;
}

.product-detail .Exchange p.active.mobile+.modal-content {
	position: fixed;
    width: 90%;
    left: 5%;
    top: 50px;
     z-index: 100001;
}

.product-detail .Exchange .modal-content .Close{
    position: absolute;
    right: 10px;
    top: 5px;
    transform: rotate(45deg);
    font-size: 26px;
    font-family: inherit;
    font-weight: 100;
    cursor: pointer;
    display: inline-block;
}

@media (min-width:791px){
	/* .Exchange .modal-content .Close {
		display: none;
	} */
}
</style>


<!-- CSS to be added to style.css ends here 27/04/2017  -->


<script>

	$(document).ready(function(){
		
		jQuery('#brandExchange').on('input', function() {
			 $("#brandExchangeParam").val($("#brandExchange").val());
		});
		
		
	
		var pdppin = document.getElementById("pin");
		var pinform=document.getElementById("pincodeExchangeParam");
		$("#pinExc").keyup(function() {
			document.getElementById("pdpPincodeCheck").className = "Check";
			pdppin.value = this.value;	
			pinform.value=this.value;
			
		});
		
	 

	$("#pdpPincodeCheckExchnage").on("click",function(){
		
		$( "#pdpPincodeCheck" ).trigger( "click" );
	   
	    
		
	  //ajax call to check greendust
		var dataString = 'pin=' + $("#pinExc").val();
	    var reversecheck=false;
	    var pinExc = $('#pinExc').val();
		var productCode =  $('#product_id').val();
		var productArray =[];
		productArray.push(productCode);
	var req1=$.ajax({
			url : ACC.config.encodedContextPath + "/p-checkReversePincode",
			data : dataString,
			/*data : {
				'selectedEMIBank' : selectedBank,
				'productVal' : productVal
			},*/
			type : "GET",
			cache : false,
			success : function(data) {
				if (data != null) {
					reversecheck=data;
					//alert("successs");
					//var pinExc = $('#pinExc').val();
					//alert("exchnge pin boss "+pinExc);
				} else {
					alert("no data");
				}
															
			},
			error : function(resp) {
				alert("error")
			}
		});
	$.when(req1).done(function(data1){
	    if(data1 && !$('#buyNowButton').prop('disabled'))
	    {
	    	   	populateExchangeDetails();
	    	if(typeof utag !="undefined"){
				utag.link({
					event_type : "exchange_pincode_true",
					pincode  : pinExc,
					product_id : productArray
				});
			}
	    }
	    else{
	    	if(typeof utag !="undefined"){
				utag.link({
					event_type : "exchange_pincode_false",
					pincode  : pinExc,
					product_id : productArray
				});
			}
	    }
	});
	  
	  return false;
	   
	    //check all ok
	    
	});

	});
	//end document ready
	
	
	function populateExchangeDetails()
	{
		
		$("#ussidExchange").val($("#ussid").val());
		   $("#exStock").val($("#stock").val());
		
		var l3code = $('#l3code').val();
		 var l3name = $('#l3name').val();
		 var prodCode = $('#productcode').val();
		var dataString = 'l3code=' + l3code +'&l3name='+l3name +'&productCode='+prodCode;
		var reversecheck=false;
	    var pinExc = $('#pinExc').val();
		var productCode =  $('#product_id').val();
		var productArray =[];
		productArray.push(productCode);
	$.ajax({
			url : ACC.config.encodedContextPath + "/p-exchange",
			data : dataString,
			type : "GET",
			cache : false,
			success : function(data) {
				if (data != null) {
					
					
					 var catOptions = "<option value= disabled selected>Select</option>";
					   
				        for (i = 0; i < data.l4categorylist.length; i++) {
				           	      	           catOptions += "<option value='"+data.l4categorylist+"'>" + data.l4categorylist + "</option>";
				        }
				        document.getElementById("l4select").innerHTML = catOptions;
				        
				       
				     activelist=data.isWorkinglist;
				     pricelist=data.priceList;
				        
				      
					$("#exchangeDetails").show();
					//alert("successs");
					//var pinExc = $('#pinExc').val();
					//alert("exchnge pin boss "+pinExc);
				} else {
					alert("no data");
				}
															
			},
			error : function(resp) {
				alert("error")
			}
		});
	}
	
	
	
	function changeWorking(value) {
		  $("#l4Exchange").val(value);
		  alert(activelist);
		    if (value.length == 0) document.getElementById("activeselect").innerHTML = "<option></option>";
	    else {
	    	
	        var catOptions = "<option value= disabled selected>Select</option>";
	   
	        for (i = 0; i < activelist.length; i++) {
	           	var res = activelist[i].split("|");
	           	
	           //	alert(res)
	        	           catOptions += "<option value='"+res[0].trim()+"|"+res[1].trim()+"'>" + res[1].trim() + "</option>";
	        }
	        document.getElementById("activeselect").innerHTML = catOptions;
	    }
	}

	function changePrice(value) {
		 $("#isWorkingExchange").val(value);
		    var catOptions = "";
	        for (i = 0; i < pricelist.length; i++)
	        {
	        	var price=pricelist[i].split("-");
	        	
	        	 if(value===price[0].trim())
	       	   	{
	        		 document.getElementById('couponValue').style.display = "block";
	        		 document.getElementById('priceselect').innerHTML =  price[1];
	        		
	       	   	}
	        	
	       }
	     
	}
	

</script>	
<c:set var="l3code" value="${product.level3CategoryCode}"/>
<c:set var="l3name" value="${product.level3CategoryName}"/>
<c:set var="isExchangeavailable" value="true"/>
<c:if test="${empty l3code || empty l3code}">
<c:set var="isExchangeavailable" value="false"/>
</c:if>

<c:if test="${isExchangeavailable ne false}">
<div class="Exchange Exchange_wrapper" id="exchangeStickerId">
<p onclick="" >
	<spring:theme code="marketplace.exchange" />
	
				
		<input id="l3code" type="hidden" value="${l3code}" />
		<input id="l3name" type="hidden" value="${l3name}" />
		<input id="productcode" type="hidden" value="${product.code}"/>
</p>
<div id="ExchangeModal-content" class="modal-content">
<span class="Close"></span>
  <div class="modal-header">   
    <h3 class="modal-title" id="myModalLabel"> 
    	<span class="Exchange-tableTitle">Exchange Offer</span>
    	<br>    
    	<span class="Exchange-subTitle">Let's check your pincode if your area is serviceable for this offer</span>	
    </h3>  	<!-- UF-48 -->	 
  </div>
  
  
   
  <div class="modal-body" id="modelId">
	
	<div class="inline-form">
		 <c:choose>
		 	<c:when test="${not empty pincode}">
			<input id="pinExc" type="text" value="${pincode}" maxlength="6" onkeypress="return isNum(event)"/>
		    </c:when>
		    <c:otherwise>
		    	<input id="pinExc" type="text" placeholder="Pincode" maxlength="6" onkeypress="return isNum(event)"/>
		    </c:otherwise>
		 </c:choose>
	   <!-- TISEE-6552 fix  -->
		<button class="orange submit" id="pdpPincodeCheckExchnage"><spring:theme code="text.submit"/></button>
		
		<button class="gray submitDList" id="pdpPincodeCheckDListExchange" style="display:none;"><spring:theme code="text.submit"/></button>
		    <div>
    	<span id="unsevisablePin" style="display:none;color:#ff1c47"><spring:theme code="pincode.unsevisable"/></span>
		<span class="pincodeErrorMsg">
			<span id="emptyPinExc" style="display:none;color:#ff1c47"><spring:theme code="product.empty.pincode"/></span>
			<span id="wrongPinExc" style="display:none;color:#ff1c47"><spring:theme code="pincode.invalid"/></span>
			<span id="unsevisablePinExc" style="display:none;color:#ff1c47"><spring:theme code="pincode.unsevisable"/></span>
			<span id="serviceablePinExc" style="display:none;color:#00994d"><spring:theme code="pincode.serviceable"/></span> <!-- Changes for TISPRM-20,65 -->
			<span id="unableprocessPinExc" style="display:none;color:#ff1c47"><spring:theme code="pincode.unableprocess"/></span>
		</span>
	</div>
	</div>
        
<div id="exchangeDetails" style="display:none">
  <input type="text" id="l3" value="${l3name}">
 <input type="text" id="brandExchange" placeholder="Brand Name">
<br>
<br>
 <select name="l4select" id="l4select" onchange="changeWorking(this.value);">
 <option value="" disabled selected>Select</option>
 </select>
 <select name="activeselect" id="activeselect" onchange="changePrice(this.value);">
  <option value="" disabled selected>Select</option>
</select>
<br>
</div>
<div id="couponValue" style="display:none">
You are eligible for coupon against your successful exchange 
<br>
 Coupons Worth INR <span id="priceselect"></span>
<!--   <input type="button" value="Add to Bag" onclick="generateExchnangId()"> -->
 
<form:form method="post" id="addToCartExchange" class="add_to_cart_form" action="#">
		
	<c:if test="${product.purchasable}">
	
	<input type="hidden" maxlength="3" size="1" name="qty" class="qty js-qty-selector-input" value="1" />
  	<!-- <input type="hidden" maxlength="3" size="1" id="pinCodeChecked"
		name="pinCodeChecked" value="false"> -->
	</c:if>
	<input type="hidden" maxlength="3" size="1" id="exStock" name="stock" value="" />
	<input type="hidden" name="productCodePost" id="productCode" value="${product.code}" /> 
	<input type="hidden" name="wishlistNamePost" id="wishlistNamePost" value="N" />
	<input type="hidden" maxlength="3" size=""  name="ussid" id="ussidExchange" value="" />
	<input type="hidden" maxlength="14" size=""  name="l3" id="l3Exchange" value="${product.level3CategoryCode}" />
	<input type="hidden" maxlength="50" size=""  name="exchangeParam" id="isWorkingExchange" value="" />
	<input type="hidden" maxlength="50" size=""  name="brandParam" id="brandExchangeParam" value="" />
	<input type="hidden" maxlength="6" size=""  name="pinParam" id="pincodeExchangeParam" value="" />
	
   		<span id="addToCartExchangenoInventorySize" style="display: none" class="no_inventory sizeGuide-message"><p class="inventory">
			<font color="#ff1c47"><spring:theme code="Product.outofinventory" /></font>
		</p></span>
		<span id="addToCartExchangeexcedeInventorySize" style="display: none" class="sizeGuide-message"><p class="inventory">
			<font color="#ff1c47">Please decrease the quantity</font>
		</p></span>
		
		<span id="addToCartExchangeTitleaddtobag" style="display: none" class="sizeGuide-message"><p class="inventory">
			<spring:theme code="product.addtocart.success"/>
		</p></span>
		<span id="addToCartExchangeTitleaddtobagerror" style="display: none" class="sizeGuide-message"><p class="inventory">
			<spring:theme code="product.error"/>
		</p></span>
		<span id="addToCartExchangeTitlebagtofull" style="display: none" class="sizeGuide-message"><p class="inventory">
			<spring:theme code="product.addtocart.aboutfull"/>
		</p></span>
		<span id="addToCartExchangeTitlebagfull" style="display: none" class="sizeGuide-message"><p class="inventory">
			<spring:theme code="product.bag"/>
		</p></span>
		<span id="pinNotServicableExchange" style="display: none" class="sizeGuide-message">
			<font color="#ff1c47">We're sorry. We don't service this pin code currently. Would you like to try entering another pin code that also works for you?</font>
		</span>
		<span id="addToCartExchangeTitleoutOfStockId" style="display: none"><p class="inventory">
			<span id="outOfStockText" class="sizeGuide-message">
			<spring:theme code="product.product.outOfStock" />
			</span>
		<input type="button" onClick="openPop_SizeGuide();" id="add_to_wishlist-sizeguide" class="wishlist" data-toggle="popover" data-placement="bottom" value="<spring:theme code="text.add.to.wishlist"/>"/>
			<!-- <font color="#ff1c47">Product is out of stock for the selected size</font> -->
		</p></span>
			
	<span id="sizeSelectedSizeGuide"   class="sizeGuide-message" style="display: none;color:#ff1c47"><spring:theme code="variant.pleaseselectsize"/></span>
	<span id="addToCartButtonIdExc">
	<!-- <span id="addToCartFormSizeTitleSuccess"></span> -->
	<button style="display: block;"
			id="addToCartButtonExc" type="button"
			class="btn-block js-add-to-cart">
		<spring:theme code="basket.add.to.basket" />
	</button>
	</span>
	<span id="addToCartSizeGuideTitleSuccess"></span>
</form:form>

		  </div> 
    
  </div>
</div>
</div>
</c:if>