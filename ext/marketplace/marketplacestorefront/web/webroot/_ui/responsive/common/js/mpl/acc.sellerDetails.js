  var sellerDetailsArray=[];
/*Display next list of sellers after clicking next link*/
  var sellerPageCount=1;
function nextPage()
{
		++sellerPageCount;
		if(sellerPageCount>1)
		{
			$("#previousPageDev").show();	
		}
		setSellerLimits(sellerPageCount);	
		focusOnElement();
}
/*Display previous list of sellers after clicking previous link*/	
function previousPage()
{
	sellerPageCount= sellerPageCount-1;
	if(sellerPageCount<Math.ceil((sellerDetailsArray.length)/pageLimit))
	{
		$("#nextPageDev").show();	
	}
	setSellerLimits(sellerPageCount);
	focusOnElement();
}
	
function focusOnElement() {
	/*window.scrollTo(0,$('#other-sellers-id').offset().top);*/
}

/*	
	Facebook share page
	(function(d, s, id) {
		var js, fjs = d.getElementsByTagName(s)[0];
		if (d.getElementById(id))
			return;
		js = d.createElement(s);
		js.id = id;
		js.src = "//connect.facebook.net/en_GB/sdk.js#xfbml=1&version=v2.3";
		fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));
	Twitter share page
	!function(d, s, id) {
		var js, fjs = d.getElementsByTagName(s)[0], p = /^http:/
				.test(d.location) ? 'http' : 'https';
		if (!d.getElementById(id)) {
			js = d.createElement(s);
			js.id = id;
			js.src = p + '://platform.twitter.com/widgets.js';
			fjs.parentNode.insertBefore(js, fjs);
		}
	}(document, 'script', 'twitter-wjs');*/
	
	/*Calculating the number of sellers to be displayed and hiding the next and previous link*/
	function setSellerLimits(index)
	{
		var endIndex = parseInt(pageLimit) * index;
	  	var startIndex = (endIndex - pageLimit);
	  	var maxPage=Math.ceil((sellerDetailsArray.length)/pageLimit);
	  	if(endIndex>sellerDetailsArray.length)
	  		{
	  		
	  		endIndex = sellerDetailsArray.length;
	  		}	  	
	  
	  	if(index==1)
	  	{
	  		$("#previousPageDev").hide();
	  		if(maxPage>1)
	  			$("#nextPageDev").show();
	  	}
	  	if(index==maxPage)
	  	{
	  		$("#nextPageDev").hide();
	  		if(maxPage>1)
	  			$("#previousPageDev").show();
	  	}
		
		for (var i = 0; i < sellerDetailsArray.length; i++) {  	  	
	  	
	  		if(i>=startIndex && i<endIndex)
	  		{
	  			
	  			$("#tr"+i+"Id").show();
	  			
	  		}
	  		else
	  			{
	  			
	  			$("#tr"+i+"Id").hide();
	  		
	  			}
	  		}
		

		//setting seller count
		var sellerCountString = (startIndex+1) +"-"+(endIndex) + " of " + sellerDetailsArray.length+" Sellers";
		$("#sellerCount .wrapper p").html(sellerCountString);
			  	
	}
	//declaring a new variable to hold ussid and final price
	var skuPriceMap="";
	var skuPriceArray="";
	/*Retrieving total number of sellers*/
	function fetchSellers(sellersArray,buyboxSeller)
	{
	//	alert(buyboxSeller+sellersArray);
		var promorestrictedSellers=$("#promotedSellerId").val();
		var isproductPage = $("#isproductPage").val();
		var pretext=$("#deliveryPretext").text();
		var posttext=$("#deliveryPosttext").text();
		var tbodycontent = "";
	  	var stock = 0;	
	  	var ussid="";	
	  	sellerDetailsArray = [];
	  	skuPriceArray=[];
	    var formEnd ="</form>";
	    var formStartForWishList = "<form id='wish'>";
	    var indx=-1;
	    var index=-1;
	    var sellerPrice = "";
	    var originalPrice = "";
	    var sellerPriceValue = 0;
	    var originalPriceValue = 0;
	 //   var deliveryModeMap="${deliveryModeMap}
	    if(isproductPage=='false' && null!=sessionStorage.getItem('pincodeChecked')){
	    	var dataVal='';
	    	var  allOosFlag='';
	    	var otherSellerCount='';
	    	
	    //	alert("hii"+sessionStorage.getItem('servicableList'));
	    	if(sessionStorage.getItem('servicableList')!=""){
	    	if(null!=sessionStorage.getItem('servicableList')){
				    dataVal=JSON.parse(sessionStorage.getItem("servicableList"));
				   // dataVal='123654098765485130011712';
			   }
	           if(null!=sessionStorage.getItem('allOosFlag')){
	        	    allOosFlag=sessionStorage.getItem('allOosFlag');
			   }
	           if(null!=sessionStorage.getItem('otherSellerCount')){
	        	   otherSellerCount=sessionStorage.getItem('otherSellerCount');
			   }
			populateBuyBoxData(dataVal,otherSellerCount,isproductPage,allOosFlag);
			var buyboxSeller = $("#ussid").val();
		//	var sellerskuidList = sessionStorage.getItem('notServicables');
            
            
//	    	sellerskuidList=sessionStorage.getItem('notServicables');
//	    	ussidIdsForED=sessionStorage.getItem('skuIdsForED');
//	    	ussidIdsForHD=sessionStorage.getItem('skuIdsForHD');
//	    	ussidIdsForCNC=sessionStorage.getItem('skuIdForCNC');
//	    	ussidIdsForCOD=sessionStorage.getItem('skuIdForCod');
//	    	stockUssidIds=sessionStorage.getItem('skuIdsWithNoStock');
//	    	stockUssidArray=sessionStorage.getItem('stockDataArrayList');
	    	}
	    	
	    //	pincodeChecked=$("#pinCodeCheckedFlag").val();
	    }
	   // console.log("sellers"+sessionStorage.getItem('notServicables'));
	    var sellerskuidList = sessionStorage.getItem('notServicables')== null ? "" : sessionStorage.getItem('notServicables');
        var ussidIdsForED = sessionStorage.getItem('ussidIdsForED')== null ? "" :   sessionStorage.getItem('ussidIdsForED');
        var ussidIdsForHD = sessionStorage.getItem('ussidIdsForHD')== null ? "" :   sessionStorage.getItem('ussidIdsForHD');
        var ussidIdsForCNC = sessionStorage.getItem('ussidIdsForCNC')== null ? "" :  sessionStorage.getItem('ussidIdsForCNC');
        var ussidIdsForCOD = sessionStorage.getItem('ussidIdsForCOD')== null ? "" :  sessionStorage.getItem('ussidIdsForCOD');
        var stockUssidIds = sessionStorage.getItem('stockUssidIds')== null ? "" :   sessionStorage.getItem('stockUssidIds');
        var stockUssidArray = sessionStorage.getItem('stockUssidArray')== null ? "" : sessionStorage.getItem('stockUssidArray');
	    //alert("non servicables"+  var sellerskuidList = sessionStorage.getItem('notServicables'))
	    for (var i = 0; i < sellersArray.length; i++) {
		if(buyboxSeller!=sellersArray[i]['ussid']){
			//alert(buyboxSeller+"##"+sellersArray[i]['ussid']);
			var leadTime=0;
			if(null!=sellersArray[i]['leadTimeForHomeDelivery']){
				leadTime=sellersArray[i]['leadTimeForHomeDelivery'];
			}
		
		if(sellerskuidList.indexOf(sellersArray[i]['ussid'])==-1){
		sellerDetailsArray[++index]	=sellersArray[i];
	  	tbodycontent+="<li id='tr"+index+"Id'><div class='Content'>";  	
	 	stock = sellersArray[i]['availableStock'];
	 	ussid = sellersArray[i]['ussid'];
	 	var formStart = "<div id='addToCartFormId"+index+"Title' name='addToCartFormId"+index+"Title' class='addToCartTitle'>"+$('#addtocartid').text()+"</div>" + "<div id='addToCartFormId"+index+"Title' class='addToCartTitle sellerAddToBagTitle'>"+$('#addtocartid').text()+"</div>" +
		"<form method='post'  name='addToCartFormId"+index+"' id='addToCartFormId"+index+"' class='add_to_cart_form' action='#'>";
	  	//setting seller name
	  	tbodycontent+="<div data='Seller Information' class='Seller'>"; 
	  	tbodycontent+=sellersArray[i]['sellername'];
	  	tbodycontent+="</div>"; 
		//setting price //TODO : price logic
	  	tbodycontent+="<div data='Price' class='Price'>"; 
	  
		if((null!=sellersArray[i]['spPrice'])&&(sellersArray[i]['spPrice']!='')&&(sellersArray[i]['spPrice'].value!=0))
	  	{  	  		
	  		sellerPrice = sellersArray[i]['spPrice'];
	  		originalPrice = sellersArray[i]['mrpPrice'];
	    	sellerPriceValue = sellerPrice.value;
	    	originalPriceValue = originalPrice.value;
	    	
	    	
	  	}
	  	else if((null!=sellersArray[i]['mopPrice'])&&(sellersArray[i]['mopPrice'].value!=''))
	  	{
	  	
	  		sellerPrice = sellersArray[i]['mopPrice'];
	  		originalPrice = sellersArray[i]['mrpPrice'];
		  	sellerPriceValue = sellerPrice.value;
		  	originalPriceValue = originalPrice.value;
	  	}
	  	else{
	  		sellerPrice = sellersArray[i]['mrpPrice'];
		  	sellerPriceValue = sellerPrice.value;
	  	}
		 
	
	    skuPriceMap=new Object();
	    skuPriceMap.key=sellersArray[i]['ussid'];
		skuPriceMap.value=sellerPriceValue;
		skuPriceArray[++indx]=skuPriceMap;
		if(originalPriceValue > 0 && originalPriceValue!=sellerPriceValue){
		tbodycontent+='<span class="sale strike" id="mrpPriceId">';	
		tbodycontent+=originalPrice.formattedValue;
		tbodycontent+="</span>";
		tbodycontent+=" ";
		}
		tbodycontent+='<span class="sale" id="mopPriceId">'
		tbodycontent+=sellerPrice.formattedValue;
		tbodycontent+="</span>"
		if(promorestrictedSellers!=undefined){
			   if(promorestrictedSellers.indexOf(sellersArray[i]['sellerID'])!=-1){
				   tbodycontent+="<div class='tooltip_wrapper offer-tooltip-wrapper'><a name='yes' id='offer"+index+"' >Offer Available</a><span class='tooltip_pop offer-popover'>"+promoData.promoDescription+"<br><br>From&nbsp;"+promoStartDate+"<br>To&nbsp;"+promoEndDate+"<br>'</span></div>";
	     }
		}
		
	  	if (parseFloat(sellerPriceValue) > emiCuttOffAmount.value) {
	  		tbodycontent+="<p>";
	  		tbodycontent+=$('#emiavailableid').text();
	  		tbodycontent+="</p>";
		}
	  	tbodycontent+="</div>"; 
	       var modes = sellersArray[i]['deliveryModes'];
	        var deliveryMap="";
	        var isHomeDelivery=false;
	        var isExpressDelivery=false;
	        var isClickDelivery=false;
		  	for(var j in modes){
		  		if(modes[j]['code'].toLowerCase().indexOf("home")!=-1){
		  			isHomeDelivery=true;
		  		}
		  		if(modes[j]['code'].toLowerCase().indexOf("express")!=-1){
		  			isExpressDelivery=true;
		  		}
		  		if(modes[j]['code'].toLowerCase().indexOf("collect")!=-1){
		  			isClickDelivery=true;
		  		}
		  		deliveryMap+=modes[j]['code']+"-"+modes[j]['deliveryCost'].formattedValue;
		  	
		  	}
		  	var deliveryModeMap="";
		  	if($("#isPinCodeChecked").val()!="true"){
		  		if(isHomeDelivery){
		  			 var start=parseInt($("#homeStartId").val())+leadTime;
		  			 var end=parseInt($("#homeEndId").val())+leadTime;
		  			 var deliveryValue=pretext+start+"-"+end+posttext;
		     var      atpMap=availableDeliveryATPForHD.concat("-").concat(deliveryValue);         
		  			 deliveryModeMap+=atpMap+"<br/>";
		  		}
		  		if(isExpressDelivery){
		  			
		  			deliveryModeMap+=availableDeliveryATPForED+"<br/>";
		  		}
		  		if(isClickDelivery){
		  			
		  			deliveryModeMap+=availableDeliveryATPForCNC+"<br/>";
		  		}
		  	}else{
		  		if(ussidIdsForED.indexOf(ussid)!=-1){
		  			 var start=parseInt($("#expressStartId").val())+leadTime;
		  			 var end=parseInt($("#expressEndId").val())+leadTime;
		  			 var deliveryValue=pretext+start+"-"+end+posttext;
		  			 availableDeliveryATPForED=availableDeliveryATPForED;
		  			 deliveryModeMap+=availableDeliveryATPForED+"<br/>";
		  		}
		  		if(ussidIdsForHD.indexOf(ussid)!=-1){
		  			 var start=parseInt($("#homeStartId").val())+leadTime;
		  			 var end=parseInt($("#homeEndId").val())+leadTime;
		  			 var deliveryValue=pretext+start+"-"+end+posttext;
		  			 if(atpMap!=""){
		  			 var atpMap=availableDeliveryATPForHD.concat("-").concat(deliveryValue);
		  			 }
		  			 deliveryModeMap+=atpMap+"<br/>";
			  		}
		  		if(ussidIdsForCNC.indexOf(ussid)!=-1){
		  			
		  			 var start=parseInt($("#clickStartId").val())+leadTime;
		  			 var end=parseInt($("#clickEndId").val())+leadTime;
		  			 var deliveryValue=pretext+start+"-"+end+posttext;
		  			 if(atpMap!=""){
		  			 var atpMap=availableDeliveryATPForCNC.concat("-").concat(deliveryValue);
		  			 }
		  			 deliveryModeMap+=atpMap+"<br/>";
			  	}
		  	}
			
	  	tbodycontent+='<div data="Delivery Information" class="Delivery"><ul>';
	  		  
	    tbodycontent+="<li>";
	    tbodycontent+=deliveryModeMap;
	    //console.log(JSON.stringfy(deliveryModeMap));
	    tbodycontent+="</li>";
	   // if(ussidIdsForCOD==""||ussidIdsForCOD==[]){
	    if($("#isPinCodeChecked").val()=="true"){
	    	if(ussidIdsForCOD.indexOf(ussid)!=-1){
	    		 tbodycontent+="<li>"+$('#cashondeliveryid').text()+"</li>";
	    	} 
	    	
	    }
	    else{
	    	if(sellersArray[i].isCod=='Y'){
    		    tbodycontent+="<li>"+$('#cashondeliveryid').text()+"</li>";
    		    }
	    }
	    tbodycontent+="<li>"; 
	    tbodycontent+=sellersArray[i]['replacement']+$('#replacementguranteeid').text();
	    tbodycontent+="</li>";
	    tbodycontent+="<li><span class='tooltip_wrapper'><a>"+$('#deliveryratesid').text()+"</a>"+"<span class='tooltip_pop'>"+ deliveryMap+"</span></span>"+" & ";
	 
	    tbodycontent+="<span class='tooltip_wrapper'><a>"+$('#returnpolicyid').text()+"</a>"+"<span class='tooltip_pop'>"+sellersArray[i]['returnPolicy']+"&nbsp;days</span></span></li>";
	  	tbodycontent+="</ul></div>";
	  	tbodycontent+='<div class="Buying">';
	  	
	  	tbodycontent+=formStart;
	  	if(($("#stockDataArray").val()!="")&&($("#stockDataArray").val()!=[])){
		  	   for (var si = 0; si < stockDataArray.length; si++) {
		  		   if(stockDataArray[si].ussid==sellersArray[i]['ussid'])
		  	 	    stock=stockDataArray[si].stock;
		  	    }	 
		  	    }
	  	tbodycontent+="<input type='hidden' size='1' id='stock' name='stock' value="+stock+">";
	  	tbodycontent+="<input type='hidden' size='1' id='ussid' name='ussid' value="+ussid+">";
	  
	    //if(stockUssidIds.indexOf(sellersArray[i]['ussid'])==-1){
	  	 if($("#isPinCodeChecked").val()!="true"){
	    	 if(stock<=0||sellersArray[i]['deliveryModes']==null){
	    			$("#addToCartButton"+index).hide();
					//tbodycontent+=$("#hiddenIdForNoStock").html();
					}else{
						tbodycontent+="<div id='addToCartFormIdOthersel"+index+"' class='OSErrorMsg' style='display:none;'></div>";
						tbodycontent+="<div id='addToCartFormId"+index+"excedeInventory' style='display:none;'>"+$('#addToCartFormexcedeInventory').text()+"</div>";
						tbodycontent+="<div id='addToCartFormId"+index+"noInventory' style='display:none;'>"+$('#addToCartFormnoInventory').text()+"</div>";
						tbodycontent+="<button id='addToCartButton"+index+"' type='button' class='button add-to-bag btn-block js-add-to-cart'>"+$('#addtobagid').text()+"</button>";
						tbodycontent+=$("#hiddenIdForStock").html();
					}
			  	}
	    else{
	    	if(stockUssidIds.indexOf(sellersArray[i]['ussid'])!=-1){
	    	$("#addToCartButton"+index).hide();
		  	}else{
		  		tbodycontent+="<div id='addToCartFormIdOthersel"+index+"' class='OSErrorMsg' style='display:none;'></div>";
		  		tbodycontent+="<div id='addToCartFormId"+index+"excedeInventory' style='display:none;'>"+$('#addToCartFormexcedeInventory').text()+"</div>";
				tbodycontent+="<div id='addToCartFormId"+index+"noInventory' style='display:none;'>"+$('#addToCartFormnoInventory').text()+"</div>";
				tbodycontent+="<button id='addToCartButton"+index+"' type='button' class='button add-to-bag btn-block js-add-to-cart'>"+$('#addtobagid').text()+"</button>";
				tbodycontent+=$("#hiddenIdForStock").html();
		  	}
	    }
	    
	  	tbodycontent+=formEnd;
	  	tbodycontent+="</div>";  	  	
	  	tbodycontent+="</div></li>";
	  	
	  	if($("#pinCodeChecked").val()=="false")//do not attach the click event for Add to Bag in next time seller refresh
  		{
  		addToBag(index);
  		} 
	  	}
	  	}	
	    }
	  	$("#sellerDetailTbdy").html(tbodycontent);
	  	var noOfSellers= (sellerDetailsArray.length);
	  	if(noOfSellers > 1){
	  		$('#sort').show();
	  	}
		$("#otherSellersCount").html(noOfSellers);
		$('a.add-to-wishlist#wishlist').popover({ 
		    html : true,
		    content: function() {
		      return $(this).parents('body').find('.add-to-wishlist-container').html();
		    }
		  });
		sessionStorage.removeItem('notServicables');
		sessionStorage.removeItem('servicableList');
		sessionStorage.removeItem('skuIdsForED');
		sessionStorage.removeItem('skuIdsForHD');
		sessionStorage.removeItem('skuIdForCNC');
		sessionStorage.removeItem('skuIdForCod');
		sessionStorage.removeItem('skuIdsWithNoStock');
		sessionStorage.removeItem('stockDataArrayList');
}
	 function addToBag(index){
		
		$(document).on('click','#addToCartFormId'+index+' .js-add-to-cart',function(){
			
        if(!$("#variant li ").hasClass("selected") && typeof($(".variantFormLabel").html())== 'undefined' && $("#ia_product_rootCategory_type").val()!='Electronics'){
		  		
		   		$("#addToCartFormIdOthersel"+index).html($('#selectSizeId').text());
				$("#addToCartFormIdOthersel"+index).show();
				 return false;
		   	 }
			ACC.product.sendAddToBag("addToCartFormId"+index);
		});
	
	}
	 function sendAddToBag(formId){
			var dataString=$('#'+formId).serialize();	
			
			
			$.ajax({
				url : ACC.config.encodedContextPath + "/cart/add",
				data : dataString,
				type : "POST",
				cache : false,
				success : function(data) {
					if(data==""){
						$("#"+formId+"Title").show();
						$("#"+formId+"Title").html("");
						$("#"+formId+"Title").html("<font color='blue'>"+$('#addtocartid').text()+"</font>");
						ACC.product.displayAddToCart(data,formId);
						}
						else if(data=="reachedMaxLimit") {
							$("#"+formId+"Title").show();
							$("#"+formId+"Title").html("");
							$("#"+formId+"Title").html("<br/><font color='red'>"+$('#addtocartaboutfullid').text()+"</font>");
						}
						else{
							$("#"+formId+"Title").show();
							$("#"+formId+"Title").html("");
							$("#"+formId+"Title").html("<font color='red'>"+$('#addtocartfullid').text()+"</font>");
						}
				},
				error : function(resp) {
					/*alert($('#addtocartfailedid').text());*/
				}
			});
	}

	 function fetchAllSellers() {
			
		  var buyboxSeller = $("#ussid").val();
		//  alert(buyboxSeller);
		    var modifiedData="";
			var isproductPage = $("#isproductPage").val();
			var productCode = $("#product").val();
			var requiredUrl = ACC.config.encodedContextPath + "/p-" + productCode
					+ "/otherSellerDetails";
			var dataString = 'productCode=' + productCode;
						
			var dataString = 'productCode=' + productCode;
			$.ajax({
				contentType : "application/json; charset=utf-8",
				url : requiredUrl,
				data : dataString,
				dataType : "json",
				success : function(data) {
					if(data==null||data==""||data.length==1){
						$("#sellerTable").hide();
						$("#other-sellers-id").hide();
					}else{
					var stockDataArrayList=
					fetchSellers(data,buyboxSeller);
					otherSellersCount = data.length;
					setSellerLimits(1);
					  var stock_id="stock";
					  var ussid = "ussid";
					  var input_name = "qty";
					  var stockDataArrayList=sessionStorage.getItem('stockDataArrayList');
					  //setting the stock value for other sellers after pincode servicability
					  if(stockDataArrayList!=undefined){
					
				 		$('form[name^="addToCartFormId"]').each(function() {
				 			 var input = $("#"+this.name+" :input[name='" + ussid +"']"); 
				 			 var stock_input = $("#"+this.name+" :input[name='" + stock_id +"']"); 
				 			   for (var si = 0; si < stockDataArrayList.length; si++) {
			 			  		   if($(input).val()!=undefined){
				 				   if(stockDataArrayList[si].ussid==$(input).val())
			 			  			  $(stock_input).val(stockDataArrayList[si].stock);
			 			  		   }
			 			  	    }	
				 			});
					  }
					}
				},
				error : function(xhr, status, error) {
					$("#sellerTable").hide();
					$("#other-sellers-id").hide();
				}
			
			
			});
		}
	 function sortSellers(value)
	 {
		 if(value == 1)
	    	 sortPrice(sellerPageCount);
	     else if(value ==2)
	    	 sortPriceDesc(sellerPageCount);
	     else if(value ==3)
	    	 sellerNameAsc(sellerPageCount);
	     else if(value == 4)
	    	 sellerNameDesc(sellerPageCount);
		 /*sellerPageCount=1;*/
	 }
	 function sortPrice(sellerPageCount){
		 var buyboxSeller = $("#ussid").val();
		     var aFinalPrice="";
		     var bFinalPrice="";
		
		      sellerDetailsArray.sort(function(a, b){
		    	  for (var p =0; p <skuPriceArray.length; p++) {  
		 	  		 if(skuPriceArray[p]['key']==a.ussid){
		 	  			aFinalPrice=skuPriceArray[p]['value'];
		 	  		 }
		 	  		 if(skuPriceArray[p]['key']==b.ussid){
			 	  			bFinalPrice=skuPriceArray[p]['value'];
			 	  		 }
		 			
		 		  }	  
		    
		    	  
			  return aFinalPrice - bFinalPrice;
		});
		      fetchSellers(sellerDetailsArray,buyboxSeller)
			  setSellerLimits(sellerPageCount);
	 }
	
	 
	 
	//Added for displaying Non HMC configurable offer messages , TPR-589
		
	    function populateOfferMessageTime(hour){
		var hours = hour.split(":")[0];
	    var am = true;
	    if (hours > 12) {
	       am = false;
	       hours =hours-12;
	    } else if(hours == 12) {
	       am = false;
	    } else if (hours == 0) {
	       hours = 12;
	    }
	    var offSatrtamorPm= am ? " AM" : " PM";   
	    return offSatrtamorPm;
		} 
	 
	//Added for displaying Non HMC configurable offer messages , TPR-589
		
	    function  populateOfferMsgWrapper(productCode, sellerId, divId ){
			var requiredUrl = ACC.config.encodedContextPath + "/p-" + productCode
			                  + "/getOfferMessage";		
			var dataString = 'productCode=' + productCode;	
			$.ajax({
				contentType : "application/json; charset=utf-8",
				url : requiredUrl,
				async: false,
				data : dataString,
				dataType : "json",
				success : function(data) {		
						var pagelevelOffer = "<div class='pdp-offer-title pdp-title'><b>OFFER: </b><span id='offerDetailId'></span></div>" ;	
						var modallevelOffer = "<div class='pdp-offerDesc pdp-promo right'><h3 class='product-name highlight desk-offer'><span id='message'></span></h3><h3 class='offer-price'></h3><div class='show-offerdate'><p><span id='messageDet'></span></p><div class='offer-date'><div class='from-date'><span class='from'>From:</span><span class='date-time' id='offerstartyearTime'></span><span class='date-time' id='offerstarthourTime'></span></div><div class='to-date'><span class='to'>To:</span><span class='date-time' id='offerendyearTime'></span><span class='date-time' id='offerendhourTime'></span></div></div></div></div>";
					   	if (data != null) {			
						    var offerMessageMap = data['offerMessageMap'];	
							var x=$('<div/>');	
							var message = null;
							var messageDet = null;
							var messageStartDate = null;
							var messageEndDate = null;		
							$(".pdp-promo-title-link").css("display","none");			
							if($("#promolist").val()!=""||!$.isEmptyObject(offerMessageMap)){
								$("#promolist").val(offerMessageMap);
								$(".pdp-promo-title-link").css("display", "block");		
							} 
							if(!$.isEmptyObject(offerMessageMap)){			
								if($(".pdp-promo-title").length > 0){
									$(pagelevelOffer).insertAfter(".pdp-promo-title");
									$(modallevelOffer).insertAfter(".show-date");
								}else{				
									$(".pdp-promo-block").append(pagelevelOffer);
									$(".offer-block").append(modallevelOffer);					
								}			
							}			
												
							$.each( offerMessageMap, function(key,value){		
								
								$.each(value, function(keyInternal,valueInternal){
									 if(keyInternal == 'message'){
										 message = valueInternal;
									 }else if(keyInternal == 'messageDet'){
										 messageDet = valueInternal;
									 }
									 if(keyInternal == 'startDate'){
										 messageStartDate = valueInternal;
									 }
									 if(keyInternal == 'endDate'){
										 messageEndDate = valueInternal;
									 }					 
								 });
								 
								 if(sellerId == key)
								 {						
									if(divId != null)
									{
										var offerMessageDiv="<div class='offerMessage-block' id='offerMessageId'>"+message+"</div>";
										var divSpecificId ='#'+divId;
										$(divSpecificId).html(offerMessageDiv);
									}
									else
									{
										$(".pdp-offer").html(message);						
									}
									$("#message").html(message);	
									$("#offerDetailId").html(messageDet);
									
									$("#messageDet").html(messageDet);
									var dateSplit = messageStartDate.split(" ");
				                   var firstpart = dateSplit[0];
				                   var secondpart = dateSplit[1];
				                   var thirdpart = secondpart.split(".")[0];                   
				                   var resfirstpart = firstpart.split("-").reverse().join("/");                  
				                   var offStartTime =  populateOfferMessageTime(thirdpart);                
				                   var offStart = thirdpart.concat(offStartTime);
				                $(".offerstartyear-time, #offerstartyearTime").html(resfirstpart);
				                $(".offerstarthour-time, #offerstarthourTime").html(offStart);                
								var enddateSplit = messageEndDate.split(" ");
				                var enddtfstpart = enddateSplit[0];
				                var enddtsecondpart = enddateSplit[1];
				                var edthirdpart = enddtsecondpart.split(".")[0];                  
				                var resEdfirstpart = enddtfstpart.split("-").reverse().join("/");                 
				                var offEndTime =  populateOfferMessageTime(edthirdpart);              
				                var offEnd = edthirdpart.concat(offEndTime);               
				              $(".offerendyear-time,#offerendyearTime").html(resEdfirstpart);
				              $(".offerendhour-time,#offerendhourTime").html(offEnd);            
								 }
								 else
								 {
									 x.append("<p>"+message+"</p>");								 
								 }				
								})	
								//Other Seller offer message details
								/* if($("#otherSellerInfoId").is(':visible'))
								 {					 
									 $('#otherSellerInfoId').append(x);						
								 }*/
							
							  	 /*var msg = $(".pdp-offer").text();
							     if(msg != "")
							     {
							    	 alert("*****");
							    	 $(".pdp-offer").css("display","inline-block");
							    	 $(".showOfferDetail").css("display","inline-block");
							     }*/
								}						
				}
			});	
		}		
		// End of TPR-589
		
	 
	 
	 
	 
	 
	 function sortPriceDesc(sellerPageCount){
		 var buyboxSeller = $("#ussid").val();
		 var aFinalPrice="";
	     var bFinalPrice="";
		 sellerDetailsArray.sort(function(a, b){
				
			 for (var p =0; p <skuPriceArray.length; p++) {  
	 	  		 if(skuPriceArray[p]['key']==a.ussid){
	 	  			aFinalPrice=skuPriceArray[p]['value'];
	 	  		 }
	 	  		 if(skuPriceArray[p]['key']==b.ussid){
		 	  			bFinalPrice=skuPriceArray[p]['value'];
		 	  		 }
	 			
	 		  }	  
			 
			 return bFinalPrice - aFinalPrice;
			 
			});
		  fetchSellers(sellerDetailsArray,buyboxSeller)
		  setSellerLimits(sellerPageCount);
		 }

	 $(document).ready(function(){
		 $(document).on("mouseenter","span.tooltip_wrapper",function(e){
			$(e.currentTarget).children().eq(1).css("visibility","visible");
		 });
		 $(document).on("mouseleave","span.tooltip_wrapper",function(e){
			$(e.currentTarget).children().eq(1).css("visibility","hidden");
		 });
		 
		 if($('#other-sellers-id').length)
			 {
			 window.scrollTo(0,$('#other-sellers-id').offset().top);	 
			 }
		
	});
	 
	 function firstToUpperCase( str ) {
		    return str.substr(0, 1).toUpperCase() + str.substr(1);
		}
	 
	 
	//TPR-1375 changes for pin code
	 function populateBuyBoxData(data,sellerCount,isproductPage,allOosFlag){
		   
			var stockInfo = +data['available'];
			availibility = stockInfo;
			$.each(stockInfo,function(key,value){

				$("#variant li a").each(function(){
					if(typeof($(this).attr("href"))!= 'undefined' && $(this).attr("href").toUpperCase().indexOf(key)!= -1 && value == 0){ 

					$(this).removeAttr("href");
					$(this).parent().addClass('strike');
				//$(this).parent().css("border-color","gray");
				$("#outOfStockId").hide();
				
					}
				});		
						$(".variant-select-sizeGuidePopUp option").each(function(){
							if(typeof($(this).attr("data-producturl"))!= 'undefined' && $(this).attr("data-producturl").indexOf(key)!= -1 && value == 0){
								$(this).attr("disabled","disabled");
								}
						});
					});
			if (data['sellerArticleSKU'] != undefined) {
				if (typeof(data['errMsg']) != "undefined" && data['errMsg'] != "") {
					return false;
				} else {
					var promorestrictedSellers = $("#promotedSellerId").val();
					if (promorestrictedSellers == null
							|| promorestrictedSellers == undefined
							|| promorestrictedSellers == "") {
						//TPR-772
						$(".promo-block").show();

					} else {
						if (promorestrictedSellers.length > 0
								&& !(promorestrictedSellers
										.indexOf(data['sellerId']) == -1))

							//TPR-772
							$(".promo-block").show();
					}
					var allStockZero = allOosFlag;
					// var codEnabled = data['isCod'];
					var sellerName = data['sellerName'];
					var sellerID = data['sellerId'];
					
					
					$("#sellerNameId").html(sellerName);
					$("#sellerSelId").val(sellerID);
					if (isOOS() && sellerCount>0) {
						$("#addToCartButton").hide();
						$("#outOfStockId").show();
						$("#buyNowButton").hide();
						$("#otherSellerInfoId").hide();
						$("#otherSellerLinkId").show();
						 $("#pdpPincodeCheck").hide();
						 $("#pin").attr("disabled",true);
						 $("#pdpPincodeCheckDList").show();
						 $("#buyNowButton").attr("disabled",true);
						
						
					}
					else if (isOOS() && sellerCount==0){
						$("#addToCartButton").hide();
						$("#buyNowButton").hide();
						$("#outOfStockId").show();
						$("#otherSellerInfoId").hide();
						$("#otherSellerLinkId").hide();
						 $("#pdpPincodeCheck").hide();
						 $("#pin").attr("disabled",true);
						 $("#pdpPincodeCheckDList").show();
						 $("#buyNowButton").attr("disabled",true);
						
						
					}else if (allStockZero == 'Y' && sellerCount>0 && $("#variant option").length == 0) {
						$("#addToCartButton").hide();
						$("#outOfStockId").show();
						$("#allVariantOutOfStock").show();
						$("#buyNowButton").hide();
						//}
						 $("#otherSellerInfoId").hide();
						 $("#otherSellerLinkId").show();
						 $("#pdpPincodeCheck").hide();
						 $("#pin").attr("disabled",true);
						 $("#pdpPincodeCheckDList").show();
						 $("#buyNowButton").attr("disabled",true);
						
					}
					else if (allStockZero == 'Y' && sellerCount==0 && $("#variant option").length == 0){
							$("#addToCartButton").hide();
							$("#buyNowButton").hide();
							$("#outOfStockId").show();
							$("#allVariantOutOfStock").show();
							 $("#otherSellerInfoId").hide();
							 $("#otherSellerLinkId").hide();
							 $("#pdpPincodeCheck").hide();
							 $("#pin").attr("disabled",true);
							 $("#pdpPincodeCheckDList").show();
							 $("#buyNowButton").attr("disabled",true);
					}
					else if (sellerCount == 0) {
						$("#otherSellerInfoId").hide();
						$("#otherSellerLinkId").hide();
					} 
					else if(sellerCount == -1) {
						$("#otherSellerInfoId").hide();
						$("#otherSellerLinkId").show();
					}
					else {
						$("#otherSellersId").html(sellerCount);
						$("#minPriceId").html(data['minPrice'].formattedValue);
					}

					$("#ussid").val(data['sellerArticleSKU']);
					//alert("ussid"+$("#ussid").val());
					$("#sellerSkuId").val(data['sellerArticleSKU']);
					var spPrice = data['specialPrice'];
					var mrpPrice = data['mrp'];
					var mop = data['price'];
					var savingsOnProduct= data['savingsOnProduct'];
					$("#stock").val(data['availablestock']);
					$(".selectQty").change(function() {
						$("#qty").val($(".selectQty :selected").val());
					});
					displayDeliveryDetails(sellerName);
					//TISPRM-33 savingsOnProduct added
					dispPrice(mrpPrice, mop, spPrice, savingsOnProduct);
					//Add to Wishlist PDP CR
					var ussIdWishlist = data['sellerArticleSKU'];
					getLastModifiedWishlist(ussIdWishlist);
					//Ended here//
					if (isproductPage == 'false') {
						//fetchAllSellers();
						$("#minPrice").html(data['minPrice'].formattedValue);
					}
				}
			} 
				else {
				 $(".reviews").hide(); 	
				 $('#addToCartButton-wrong').attr("disable",true);
				 $('#addToCartButton-wrong').show();
				 $('#addToCartButton').hide();
				 $("#otherSellerInfoId").hide();
				 $(".wish-share").hide();
				 $(".fullfilled-by").hide();
				// TISST-13959 fix
				 $("#dListedErrorMsg").show();
				 // TISEE-6552 fix
				 $("#pdpPincodeCheck").hide();
				 $("#pin").attr("disabled",true);
				 $("#pdpPincodeCheckDList").show();
				 $("#buyNowButton").attr("disabled",true);
			}
		}
