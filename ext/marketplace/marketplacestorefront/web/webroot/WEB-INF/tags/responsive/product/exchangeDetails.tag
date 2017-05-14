<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<script>

	$(document).ready(function(){
		
		jQuery('#brandExchange').on('input', function() {
			 $("#brandExchangeParam").val($("#brandExchange").val());
			 $("#lbrand").text("Brand");
				document.getElementById('lbrand').style.color = "#999999";
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
		
		//!$('#buyNowButton').prop('disabled') && $('#serviceablePinExc').is(':visible') 
	    if(data1)
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
		 var prodCode = $('#productcode').val();
		var dataString = 'l3code=' + l3code;
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
				        	    	           catOptions += "<option value='"+data.l4categorylist[i]+"'>" + data.l4categorylist[i] + "</option>";
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
			error : function(resp,error) {
				alert("error:" + error);
			}
		});
	}
	
	
	
	function changeWorking(value) {
		document.getElementById('submit&Condition').style.display = "block";
		 document.getElementById('couponValue').style.display = "none";
		  $("#l4Exchange").val(value);
		  $("#ll4select").text("Type");
			document.getElementById('ll4select').style.color = "#999999";
		    if (value.length == 0) document.getElementById("activeselect").innerHTML = "<option></option>";
	    else {
	    	
	        var catOptions = "<option value= disabled selected>Select</option>";
	   
	        for (i = 0; i < activelist.length; i++) {
	           	          catOptions += "<option value='"+activelist[i].trim()+"'>" +activelist[i].trim()+ "</option>";
	        }
	        document.getElementById("activeselect").innerHTML = catOptions;
	    }
	}

	function changePrice(value) {
		document.getElementById('submit&Condition').style.display = "block";
		 document.getElementById('couponValue').style.display = "none";
		 $("#lactiveselect").text("Working Condition");
			document.getElementById('lactiveselect').style.color = "#999999";
		
		l4val=$('#l4select').val();
		l4wokinval=l4val+"|"+value;
		 $("#isWorkingExchange").val(l4wokinval);
		    var catOptions = "";
	        for (i = 0; i < pricelist.length; i++)
	        {
	        	var price=pricelist[i].split("-");
	        	
	        	 if(l4wokinval===price[0].trim())
	       	   	{
	        		 
	        		 document.getElementById('priceselect').innerHTML =  price[1];
	        		
	       	   	}
	        	
	       }
	     
	}
	
	function onSubmitExc()
	{
		var brand= $("#brandExchangeParam").val();
		var l4select=$('#l4select').val();
		var isError=false;
		var isWorking=$('#activeselect').val();
			if(!brand)
				{
				$("#lbrand").text("Please Enter Brand");
				document.getElementById('lbrand').style.color = "red";
				isError=true;
				}
			if(l4select==='disabled' ||!l4select )
				{
				$("#ll4select").text("Please Select Type");
				document.getElementById('ll4select').style.color = "red";
				isError=true;
				}
			if(isWorking==='disabled' ||!isWorking)
				{
				$("#lactiveselect").text("Please Select Working/Non Working");
				document.getElementById('lactiveselect').style.color = "red";
				isError=true;
				}
			if(!isError)
				{
		document.getElementById('couponValue').style.display = "block";
		document.getElementById('submit&Condition').style.display = "none";
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
    	<span class="Exchange-subTitle">Let's check your pincode if your area is serviceable for this offer.</span>	
    </h3>  	<!-- UF-48 -->	 
  </div>
  
  
   
  <div class="modal-body" id="modelId">

	<div class="inline-form">
	
		 <c:choose>
		 	<c:when test="${not empty pincode}">
			<input id="pinExc" type="text" value="${pincode}" maxlength="6" onkeypress="return isNum(event)"/>
		    </c:when>
		    <c:otherwise>
		    	<input id="pinExc" type="text" placeholder="Enter pincode" maxlength="6" onkeypress="return isNum(event)"/>
		    </c:otherwise>
		 </c:choose>
	   <!-- TISEE-6552 fix  -->
		<button class="orange submit" id="pdpPincodeCheckExchnage"><spring:theme code="text.submit"/></button>
		
		<button class="gray submitDList" id="pdpPincodeCheckDListExchange" style="display:none;"><spring:theme code="text.submit"/></button>
		    <div class="pincodeErrorMsg">
		<!-- <span class="pincodeErrorMsg"> -->
			<span id="emptyPinExc" style="display:none;color:#ff1c47"><spring:theme code="product.empty.pincode"/></span>
			<span id="wrongPinExc" style="display:none;color:#ff1c47"><spring:theme code="pincode.invalid"/></span>
			<span id="unsevisablePinExc" style="display:none;color:#ff1c47"><spring:theme code="pincode.unsevisable"/></span>
			<span id="serviceablePinExc" style="display:none;color:#339933"><spring:theme code="pincode.serviceable"/></span> <!-- Changes for TISPRM-20,65 -->
			<span id="unableprocessPinExc" style="display:none;color:#ff1c47"><spring:theme code="pincode.unableprocess"/></span>
		<!-- </span> -->
	</div>
	</div>
        
<div id="exchangeDetails" style="display:none">
  <div class="half">
  <label for="l3">Product Category</label>
  <input type="text" id="l3" value="${l3name}">
  </div>
   <div class="half">
   <label id="lbrand" for="brandExchange">Brand</label>
 <input type="text" id="brandExchange">
	</div>
	 <div class="half">
	 <label id="ll4select" for="l4select">Type</label>
 <select name="l4select" id="l4select" onchange="changeWorking(this.value);">
 <option value="" disabled selected>Select</option>
 </select>
 </div>
  <div class="half">
  <label id="lactiveselect" for="activeselect">Working Condition</label>
 <select name="activeselect" id="activeselect" onchange="changePrice(this.value);">
  <option value="" disabled selected>Select</option>
</select>
</div>

<div id="submit&Condition">
<input type="checkbox" name="terms&condition" id="exchange_tc" value="Bike">
<label for="exchange_tc"><span>I agree to the <span>terms and conditions</span></span></label>
	<cms:pageSlot position="Section2" var="component">
				<cms:component component="${component}" />
			</cms:pageSlot>	
			<br>
			<input type="button" id="submitExchange" onClick="onSubmitExc();" value="Submit">
</div>
</div>

<div id="couponValue" style="display:none">
<div class="couponMsg">
You are eligible for coupon against your successful exchange 
<br>
 Coupons Worth INR <span id="priceselect"></span>
 </div>
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
		<spring:theme code="basket.add.to.basket.exchange" />
	</button>
	</span>
	<span id="addToCartSizeGuideTitleSuccess"></span>
</form:form>

		  </div> 
    
  </div>
</div>
</div>
</c:if>