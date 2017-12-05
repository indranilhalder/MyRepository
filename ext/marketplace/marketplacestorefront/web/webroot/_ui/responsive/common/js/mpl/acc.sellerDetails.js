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
		var promorestrictedSellers=$("#promotedSellerId").val();
		var isproductPage = $("#isproductPage").val();
		//TISSTRT-1580
		var pretext=$.trim($("#deliveryPretext").text()) + "&nbsp;";
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
	    if(isproductPage=='false' && null!=sessionStorage.getItem('pincodeChecked')){
	    	var dataVal='';
	    	var  allOosFlag='';
	    	var otherSellerCount='';
	    	if(sessionStorage.getItem('servicableList')!=""){
	    	if(null!=sessionStorage.getItem('servicableList')){
				    dataVal=JSON.parse(sessionStorage.getItem("servicableList"));
			   }
	           if(null!=sessionStorage.getItem('allOosFlag')){
	        	    allOosFlag=sessionStorage.getItem('allOosFlag');
			   }
	           if(null!=sessionStorage.getItem('otherSellerCount')){
	        	   otherSellerCount=sessionStorage.getItem('otherSellerCount');
			   }
			populateBuyBoxData(dataVal,otherSellerCount,isproductPage,allOosFlag);
			var buyboxSeller = $("#ussid").val();
	    	}
	    	
	    //	pincodeChecked=$("#pinCodeCheckedFlag").val();
	    }
	    var sellerskuidList = sessionStorage.getItem('notServicables')== null ? "" : sessionStorage.getItem('notServicables');
        var ussidIdsForED = sessionStorage.getItem('ussidIdsForED')== null ? "" :   sessionStorage.getItem('ussidIdsForED');
        var ussidIdsForHD = sessionStorage.getItem('ussidIdsForHD')== null ? "" :   sessionStorage.getItem('ussidIdsForHD');
        var ussidIdsForCNC = sessionStorage.getItem('ussidIdsForCNC')== null ? "" :  sessionStorage.getItem('ussidIdsForCNC');
        var ussidIdsForCOD = sessionStorage.getItem('ussidIdsForCOD')== null ? "" :  sessionStorage.getItem('ussidIdsForCOD');
        var stockUssidIds = sessionStorage.getItem('stockUssidIds')== null ? "" :   sessionStorage.getItem('stockUssidIds');
        var stockUssidArray = sessionStorage.getItem('stockUssidArray')== null ? "" : sessionStorage.getItem('stockUssidArray');
	    for (var i = 0; i < sellersArray.length; i++) {
		if(buyboxSeller!=sellersArray[i]['ussid']){
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
		tbodycontent+=originalPrice.formattedValueNoDecimal;
		tbodycontent+="</span>";
		tbodycontent+=" ";
		}
		tbodycontent+='<span class="sale" id="mopPriceId">'
		tbodycontent+=sellerPrice.formattedValueNoDecimal;
		tbodycontent+="</span>"
		//CAR-327 starts here	
			
			tbodycontent+='<span class="offerstart"></span>'; 	
				
				
			/*if(promorestrictedSellers!=undefined){
				   if(promorestrictedSellers.indexOf(sellersArray[i]['sellerID'])!=-1){		
					   tbodycontent+="<div class='tooltip_wrapper offer-tooltip-wrapper'><a name='yes' id='offer"+index+"' >Offer Available</a><span class='tooltip_pop offer-popover'>"+promoData.promoDescription+"<br><br>From&nbsp;"+promoStartDate+"<br>To&nbsp;"+promoEndDate+"<br>'</span></div>";
		     }
			    
			}*/
		
		//CAR-327 ends here
		
	  	if (parseFloat(sellerPriceValue) > emiCuttOffAmount.value) {
	  		tbodycontent+="<p>";
	  		tbodycontent+=$('#emiavailableid').text();
	  		tbodycontent+="</p>";
		}
	  //TPR-6907
	    if($("#isPinCodeChecked").val()=="true"){
	    	if(ussidIdsForCOD.indexOf(ussid)!=-1){
	    		 tbodycontent+='<p class="cod" id="codEligible">'
	    	     tbodycontent+=$('#cashondeliveryid').text();
	    		 tbodycontent+='</p>'
	    	} 
	    	
	    }
	    else{
	    	if(sellersArray[i].isCod=='Y'){
	    		tbodycontent+='<p class="cod" id="codEligible">'
    		    tbodycontent+=$('#cashondeliveryid').text();
	    		tbodycontent+='</p>'
    		    }
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
		  		//UF-306 Starts here
		  		if(modes[j]['code']=='home-delivery')
	  			{
		  			deliveryMap+='standard-shipping'+"-"+modes[j]['deliveryCost'].formattedValue;
	  			}
		  		else if(modes[j]['code']=='express-delivery')
	  			{
		  			deliveryMap+='express-shipping'+"-"+modes[j]['deliveryCost'].formattedValue;
	  			}
		  		else if(modes[j]['code']=='click-and-collect')
	  			{
		  			deliveryMap+='CLiQ AND PiQ'+"-"+modes[j]['deliveryCost'].formattedValue;
	  			}
		  		//UF-306 Ends here
		  	
		  	}
		  	var deliveryModeMap="";
		  	if($("#isPinCodeChecked").val()!="true"){
		  		if(isHomeDelivery){
		  			 var start=parseInt($("#homeStartId").val())+leadTime;
		  			 var end=parseInt($("#homeEndId").val())+leadTime;
		  			 var deliveryValue=pretext+start+"-"+end+posttext;
		  			 var atpMap=availableDeliveryATPForHD.concat("-").concat(deliveryValue);         
		  			 deliveryModeMap+= "<li>" + atpMap + "</li>";
		  		}
		  		if(isExpressDelivery){
		  			deliveryModeMap+= "<li>" + availableDeliveryATPForED + "</li>";
		  		}
		  		if(isClickDelivery){
		  			
		  			deliveryModeMap+= "<li>" + availableDeliveryATPForCNC + "</li>";
		  		}
		  	}else{
		  		if(ussidIdsForED.indexOf(ussid)!=-1){
		  			 var start=parseInt($("#expressStartId").val())+leadTime;
		  			 var end=parseInt($("#expressEndId").val())+leadTime;
		  			 var deliveryValue=pretext+start+"-"+end+posttext;
		  			 availableDeliveryATPForED=availableDeliveryATPForED;
		  			 deliveryModeMap+= "<li>" + availableDeliveryATPForED + "</li>";
		  		}
		  		if(ussidIdsForHD.indexOf(ussid)!=-1){
		  			 var start=parseInt($("#homeStartId").val())+leadTime;
		  			 var end=parseInt($("#homeEndId").val())+leadTime;
		  			 var deliveryValue=pretext+start+"-"+end+posttext;
		  			 if(atpMap!=""){
		  			 var atpMap=availableDeliveryATPForHD.concat("-").concat(deliveryValue);
		  			 }
		  			 deliveryModeMap+= "<li>" + atpMap + "</li>";
			  		}
		  		if(ussidIdsForCNC.indexOf(ussid)!=-1){
		  			
		  			 var start=parseInt($("#clickStartId").val())+leadTime;
		  			 var end=parseInt($("#clickEndId").val())+leadTime;
		  			 var deliveryValue=pretext+start+"-"+end+posttext;
		  			 if(atpMap!=""){
		  			 var atpMap=availableDeliveryATPForCNC.concat("-").concat(deliveryValue);
		  			 }
		  			 deliveryModeMap+= "<li>" + atpMap + "</li>";
			  	}
		  	}
			
	  	tbodycontent+='<div data="Delivery Information" class="Delivery"><ul>';
	  		  
	    //tbodycontent+="<li>";
	    tbodycontent+=deliveryModeMap;
	    //console.log(JSON.stringfy(deliveryModeMap));
	    //tbodycontent+="</li>";
	   // if(ussidIdsForCOD==""||ussidIdsForCOD==[]){
	    //TPR-6907
//	    if($("#isPinCodeChecked").val()=="true"){
//	    	if(ussidIdsForCOD.indexOf(ussid)!=-1){
//	    		 tbodycontent+="<li>"+$('#cashondeliveryid').text()+"</li>";
//	    	} 
//	    	
//	    }
//	    else{
//	    	if(sellersArray[i].isCod=='Y'){
//    		    tbodycontent+="<li>"+$('#cashondeliveryid').text()+"</li>";
//    		    }
//	    }
	  //UF-250(0 Day replacement guarantee text to be removed.)
	    //tbodycontent+="<li>"; 
	   // tbodycontent+=sellersArray[i]['replacement']+$('#replacementguranteeid').text();
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
	    			
	    			tbodycontent+="<span class='outOfStock"+index+"'>"+$('#otherselleroutofstock').text()+"</span>";
					tbodycontent+="<div class='btn-buy-now oosOtherSeller'><button id='buyNowButton"+index+"' type='button' class='button add-to-bag btn-block js-add-to-cart buy-now' disabled='disabled'>"+$('#buynowid').text()+"</button>";
					tbodycontent+="<span id='addToCartButtonIds' class='btn-buy-now-holder'><button id='addToCartButton"+index+"' type='button' class='button add-to-bag btn-block js-add-to-cart add-to-bag-seller' disabled='disabled'>"+$('#addtobagid').text()+"</button></span></div>";
					
					
					//tbodycontent+=$("#hiddenIdForNoStock").html();
					}else{
						//TPR-887
						tbodycontent+="<div id='buyNowFormIdOthersel"+index+"' class='OSErrorMsg' style='display:none;'></div>";
						tbodycontent+="<div id='buyNowFormId"+index+"excedeInventory' style='display:none;'>"+$('#buyNowFormexcedeInventory').text()+"</div>";
						tbodycontent+="<div id='buyNowFormId"+index+"noInventory' style='display:none;'>"+$('#buyNowFormnoInventory').text()+"</div>";
						tbodycontent+="<div class='btn-buy-now'><button id='buyNowButton"+index+"' type='button' class='button add-to-bag btn-block js-add-to-cart buy-now'>"+$('#buynowid').text()+"</button>";
						tbodycontent+="<span id='addToCartButtonId' class='btn-buy-now-holder'><button id='addToCartButton"+index+"' type='button' class='button add-to-bag btn-block js-add-to-cart add-to-bag-seller'>"+$('#addtobagid').text()+"</button></span></div>";
						
						tbodycontent+="<div id='addToCartFormIdOthersel"+index+"' class='OSErrorMsg' style='display:none;'></div>";
						tbodycontent+="<div id='addToCartFormId"+index+"excedeInventory' style='display:none;'>"+$('#addToCartFormexcedeInventory').text()+"</div>";
						tbodycontent+="<div id='addToCartFormId"+index+"noInventory' style='display:none;'>"+$('#addToCartFormnoInventory').text()+"</div>";
						
						tbodycontent+=$("#hiddenIdForStock").html();
					}
			  	}
	    else{
	    	if(stockUssidIds.indexOf(sellersArray[i]['ussid'])!=-1){
	    		 
	    	$("#addToCartButton"+index).hide();
	    	tbodycontent+="<span class='outOfStock"+index+"'>"+$('#otherselleroutofstock').text()+"</span>";
	    	tbodycontent+="<div class='btn-buy-now oosOtherSeller'><button id='buyNowButton"+index+"' type='button' class='button add-to-bag btn-block js-add-to-cart buy-now' disabled='disabled'>"+$('#buynowid').text()+"</button>";
			tbodycontent+="<span id='addToCartButtonId' class='btn-buy-now-holder'><button id='addToCartButton"+index+"' type='button' class='button add-to-bag btn-block js-add-to-cart add-to-bag-seller' disabled='disabled'>"+$('#addtobagid').text()+"</button></span></div>";
			
	    	}else{
		  	//TPR-887
				tbodycontent+="<div id='buyNowFormIdOthersel"+index+"' class='OSErrorMsg' style='display:none;'></div>";
				tbodycontent+="<div id='buyNowFormId"+index+"excedeInventory' style='display:none;'>"+$('#buyNowFormexcedeInventory').text()+"</div>";
				tbodycontent+="<div id='buyNowFormId"+index+"noInventory' style='display:none;'>"+$('#buyNowFormnoInventory').text()+"</div>";
				tbodycontent+="<div class='btn-buy-now'><button id='buyNowButton"+index+"' type='button' class='button add-to-bag btn-block js-add-to-cart buy-now'>"+$('#buynowid').text()+"</button>";
				tbodycontent+="<span id='addToCartButtonId' class='btn-buy-now-holder'><button id='addToCartButton"+index+"' type='button' class='button add-to-bag btn-block js-add-to-cart add-to-bag-seller'>"+$('#addtobagid').text()+"</button></span></div>";
				
		  		tbodycontent+="<div id='addToCartFormIdOthersel"+index+"' class='OSErrorMsg' style='display:none;'></div>";
		  		tbodycontent+="<div id='addToCartFormId"+index+"excedeInventory' style='display:none;'>"+$('#addToCartFormexcedeInventory').text()+"</div>";
				tbodycontent+="<div id='addToCartFormId"+index+"noInventory' style='display:none;'>"+$('#addToCartFormnoInventory').text()+"</div>";
				
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
	  	
	    //CAR-327 starts here
	    for (var i = 0; i < sellersArray.length; i++) {
        	
			var sellerIdValue = sellersArray[i]['sellerID'];
		    var productCode = $("#product").val();
		    populatePrimaryOfrMsgWrapperOtherSellers(productCode, sellerIdValue, null);
        }
	    //CAR-327 ends here
	  	
	  	var noOfSellers= (sellerDetailsArray.length)-$('.oosOtherSeller').length;
	  	if(noOfSellers ==0){
	  	
	  		$('#other-sellers-id .Padd h3').hide();
	  		
	  	}
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
		sessionStorage.removeItem('pincodeChecked');
}
	 function addToBag(index){
		
		//$(document).on('click','#addToCartFormId'+index+' .js-add-to-cart',function(){ //Changed for TPR-887
		 $(document).off('click', '#addToCartFormId'+index+' #addToCartButton'+index).on('click', '#addToCartFormId'+index+' #addToCartButton'+index, function(event) {
		 //$(document).on('click','#addToCartFormId'+index+' #addToCartButton'+index,function(){
			 
			 
			 
			
        if(!$("#variant li ").hasClass("selected") && typeof($(".variantFormLabel").html())== 'undefined' && $("#ia_product_rootCategory_type").val()!='Electronics'&& $("#ia_product_rootCategory_type").val()!='Watches' && $("#ia_product_rootCategory_type").val()!='Accessories' && $("#ia_product_rootCategory_type").val()!='HomeFurnishing' && $("#showSize").val()=='true'){
		  		
		   		/*$("#addToCartFormIdOthersel"+index).html($('#selectSizeId').text());
				$("#addToCartFormIdOthersel"+index).show();*/
        		$("#buyNowFormIdOthersel"+index).html($('#selectSizeId').text());
        		$("#buyNowFormIdOthersel"+index).show();
				return false;
		   	 }
        //TISQAEE-64
        var productCode= $('#productCode').val();
        var productCodeArray=[];
        productCodeArray.push(productCode);    
	        utag.link({
				link_obj: this,
				link_text: 'add_to_bag' ,
				event_type : 'add_to_bag_pdp' ,
				product_sku : productCodeArray                     // Product code passed as an array for Web Analytics   -- INC_11511  fix
			});
			ACC.product.sendAddToBag("addToCartFormId"+index);
		});
		 
		//TPR-887 //INC144313255
		 $(document).off('click', '#addToCartFormId'+index+' #buyNowButton'+index).on('click', '#addToCartFormId'+index+' #buyNowButton'+index, function(event) {
		 //$(document).on('click','#addToCartFormId'+index+' #buyNowButton'+index,function(){
	        if(!$("#variant li ").hasClass("selected") && typeof($(".variantFormLabel").html())== 'undefined' && $("#ia_product_rootCategory_type").val()!='Electronics' && $("#ia_product_rootCategory_type").val()!='Accessories' && $("#ia_product_rootCategory_type").val()!='Watches' && $("#ia_product_rootCategory_type").val()!='HomeFurnishing' && $("#showSize").val()=='true'){
			  		
	        		$("#buyNowFormIdOthersel"+index).html($('#selectSizeId').text());
					$("#buyNowFormIdOthersel"+index).show();
					return false;
			   	 }
				ACC.product.sendAddToBag("addToCartFormId"+index,true);
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
					//UF-34 Default Sorting as Price Low to High at the time of loading
					sortSellers("1");
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
	     //UF-34
	     var oosSellers = [];
	
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
	      
	     //UF-34
	      for (var sel =0; sel <sellerDetailsArray.length; sel++) { 
				 if(sellerDetailsArray[sel].availableStock < 1) {
					 oosSellers.push(sellerDetailsArray[sel]);
				 }
			 }

	      sellerDetailsArray = sellerDetailsArray.filter(function(val) {
	    	  return oosSellers.indexOf(val) == -1;
	      });

	      sellerDetailsArray = sellerDetailsArray.concat(oosSellers);

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
				cache : false,
				data : dataString,
				dataType : "json",
				success : function(data) {		
						var pagelevelOffer = "<div class='pdp-offer-title pdp-title' id='offerDetailIdSecondDiv'><b>OFFER: </b><span id='offerDetailId'></span></div>" ;	

						var modallevelOffer = "<div class='pdp-offerDesc pdp-promo offercalloutdesc' style='margin-top:10px'><h3 class='product-name highlight desk-offer'><span id='message'></span></h3><h3 class='offer-price'></h3><div class='show-offerdate'><p><span id='messageDet'></span></p><div class='offer-date'><div class='from-date'><span class='from'>From:</span><span class='date-time' id='offerstartyearTime'></span><span class='date-time' id='offerstarthourTime'></span></div><div class='to-date'><span class='to'>To:</span><span class='date-time' id='offerendyearTime'></span><span class='date-time' id='offerendhourTime'></span></div></div></div></div>";
						if (data['offerMessageMap'] != null) {			

						    var offerMessageMap = data['offerMessageMap'];	
							var x=$('<div/>');	
							var message = null;
							var messageDet = null;
							var messageStartDate = null;
							var messageEndDate = null;		

							$(".pdp-promo-title-link").css("display","none");			
							//TISSQAUAT-472 starts here
							if($("#promolist").val()=="All" || $("#promolist").val()=="Web" ||!$.isEmptyObject(offerMessageMap)){
								$("#promolist").val(offerMessageMap);
								$(".pdp-promo-title-link").css("display", "block");		
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
								 
								//message[sellerid] =
								//if(){
									//$("otherseller_"+key).val(message);
									
								//}
								 if(sellerId == key)
								 {	
									if(!$.isEmptyObject(offerMessageMap)){	
										    $(".pdp-promo-title-link").show();
											if($(".pdp-promo-title").length > 0){
												$(pagelevelOffer).insertAfter(".pdp-promo-block");
												$(modallevelOffer).insertAfter(".pdp-promoDesc");
											}else{	
												//$(".pdp-promo-block").append(pagelevelOffer);
												$(pagelevelOffer).insertAfter(".pdp-promo-block");
												$(".offer-block").append(modallevelOffer);					
											}			
									}	
									if(divId != null)
									{
										//var offerMessageDiv="<div class='offerMessage-block' id='offerMessageId'>"+message+"</div>";
										var offerMessageDiv="<div class='offerMessage-block' id='offerMessageId'>"+messageDet+"</div>";
										var divSpecificId ='#'+divId;
										$(divSpecificId).html(offerMessageDiv);
									}
									else
									{
										$(".pdp-offer").html(message);						
									}
									
									//INC144316032
									$("#message").html(message);
									$("#offerDetailId").html(message);
									$("#messageDet").html(messageDet);
									
//									$("#message").html(message);	
//									$("#offerDetailId").html(messageDet);								
//									$("#messageDet").html(messageDet);
									
									//$("#message").html(messageDet);	
									//$("#offerDetailId").html(message);									
									//$("#messageDet").html(message);
									
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
									// if($(".pdp-promo-title").length == 0) {
										// $(".pdp-promo-title-link").css("display","none");		
									// }
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
	    
	    //Start of CAR-327
	    function populatePrimaryOfrMsgWrapperOtherSellers(productCode, sellerId, divId )
	    {
	    	var index = -1;
	    	var requiredUrl = ACC.config.encodedContextPath + "/p-" + productCode
			                  + "/getPrimaryCalloutOfferMessage";
			var dataString = 'productCode=' + productCode + '&sellerId=' + sellerId;	
			$.ajax({
				contentType : "application/json; charset=utf-8",
				url : requiredUrl,
				async: false,
				cache : false,
				data : dataString,
				dataType : "json",
				success : function(data) {		
						if (data['primaryOfferMessageMap'] != null) {		
							
						    var offerMessageMap = data['primaryOfferMessageMap'];	
							var message = null;
							var messageDet = null;
							var messageStartDate = null;
							var messageEndDate = null;
							var termsAndConditions = null;
							var termsAndConditionsHeading = "Terms And Conditions :";
							var tbodycontent = "";
																															
												
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
									 if(keyInternal == 'termsAndConditions'){
										 termsAndConditions = valueInternal;
									 }	
								 });
																
								if(sellerId == key)
									 {	
										if(!$.isEmptyObject(offerMessageMap)){	
											index ++;
											tbodycontent+="<div class='tooltip_wrapper offer-tooltip-wrapper' id='car327'><a name='yes' id='offer"+index+"'>Offer Available</a><span class='tooltip_pop offer-popover'>"+message+"<br><br>From&nbsp;"+messageStartDate+"<br>To&nbsp;"+messageEndDate+"<br></span></div>";
																					
										}
										if($("#car327").length < 1){
											$("#sellerDetailTbdy .Price .offerstart").append(tbodycontent);
										}
									 }	
									
								})							
								
						}						
				}
			});	
		}		
	       
	     
	    	    
	    function  populatePrimaryCalloutOfferMsgWrapper(productCode, sellerId, divId ){
	    	var requiredUrl = ACC.config.encodedContextPath + "/p-" + productCode
			                  + "/getPrimaryCalloutOfferMessage";
			var dataString = 'productCode=' + productCode + '&sellerId=' + sellerId;	
			$.ajax({
				contentType : "application/json; charset=utf-8",
				url : requiredUrl,
				async: false,
				cache : false,
				data : dataString,
				dataType : "json",
				success : function(data) {		
						var pagelevelOffer = "<div class='pdp-offer-title pdp-title' id='offerDetailIdDiv'><b>OFFER: </b><span id='offerDetailIdPromotion'></span></div>" ;	

						var modallevelOffer = "<div class='pdp-offerDesc pdp-promo offercalloutdesc'><h3 class='product-name highlight desk-offer'><span id='messagePrimary'></span></h3><h3 class='offer-price'></h3><div class='show-offerdate'><p><span id='messageDetPrimary'></span></p><p id='termsP'><span id='termsAndConditionsheading'></span><br><span id='termsAndConditions'></span></p><p id='primaryPromoUrl'><span id='promoUrl'></span></p><div class='offer-date'><div class='from-date'><span class='from'>From:</span><span class='date-time' id='offerstartyearTimePrimary'></span><span class='date-time' id='offerstarthourTimePrimary'></span></div><div class='to-date'><span class='to'>To:</span><span class='date-time' id='offerendyearTimePrimary'></span><span class='date-time' id='offerendhourTimePrimary'></span></div></div></div></div>";
						if (data['primaryOfferMessageMap'] != null) {			

						    var offerMessageMap = data['primaryOfferMessageMap'];	
							var x=$('<div/>');	
							var message = null;
							var messageDet = null;
							var messageStartDate = null;
							var messageEndDate = null;
							var termsAndConditions = null;
							var termsAndConditionsHeading = "Terms And Conditions :";
							var promoUrl = null;
							var bundlePromoLinkText = null;

							$(".pdp-promo-title-link").css("display","none");		
							if($("#promolist").val()=="All" || $("#promolist").val()=="Web" ||!$.isEmptyObject(offerMessageMap)){
								$("#promolist").val(offerMessageMap);
								//$(".pdp-promo-title-link").css("display", "block");
								if($(".pdp-promoDesc").text() == "")
									{									
									$(".pdp-promo-title-link").css("display", "none");
									}
								else
									{									
									$(".pdp-promo-title-link").css("display", "block");
									}
							} 
									
												
							$.each( offerMessageMap, function(key,value){							
							
								 if(sellerId == key)
								 {	
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
										 if(keyInternal == 'termsAndConditions'){
											 termsAndConditions = valueInternal;
										 }	
										 if(keyInternal == 'promoUrl'){
											 promoUrl = valueInternal;
										 }	
										 if(keyInternal == 'bundlePromoLinkText'){
											 bundlePromoLinkText = valueInternal;
										 }	
									 });
									 
									 
									if(!$.isEmptyObject(offerMessageMap)){	
										    $(".pdp-promo-title-link").show();
										    if($("#offerDetailIdPromotion").text() != $("#offerDetailIdPromotion").html(message)){
										    	if($("#offerDetailIdDiv").length < 1){
										    		if($(".pdp-promo-title").length > 0){
														$(pagelevelOffer).insertAfter(".pdp-promo-block");
														$(modallevelOffer).insertAfter(".pdp-promoDesc");
										    		}else{	
														$(pagelevelOffer).insertAfter(".pdp-promo-block");
														$(".offer-block").append(modallevelOffer);					
										    		}	
										    	}
										    }
									}	
									if(divId != null)
									{
										var offerMessageDiv="<div class='offerMessage-block' id='offerMessageId'>"+messageDet+"</div>";
										var divSpecificId ='#'+divId;
										$(divSpecificId).html(offerMessageDiv);
									}
									else
									{
										$(".pdp-offer").html(message);						
									}
									$("#messagePrimary").html(message);
									$("#offerDetailIdPromotion").html(message);
									$("#messageDetPrimary").html(messageDet);
									if(termsAndConditions != null)
									{
										$("#termsAndConditionsheading").html(termsAndConditionsHeading);
										$("#termsAndConditions").html(termsAndConditions);
									}
									else{
										$("#termsAndConditionsheading").remove();
										$("#termsAndConditions").remove();
										$("#termsP").remove();
									}
									
									if(promoUrl != null && bundlePromoLinkText != null)
									{
									var link = document.createElement('a');
									link.textContent = bundlePromoLinkText;
									link.href = promoUrl;
									document.getElementById('promoUrl').appendChild(link);
									}
									else
										{
										$("#promoUrl").remove();
										$("#primaryPromoUrl").remove();
										}
									
							       var dateSplit = messageStartDate.split(" ");
				                   var firstpart = dateSplit[0];
				                   var secondpart = dateSplit[1];
				                   var thirdpart = secondpart.split(".")[0];                   
				                   var resfirstpart = firstpart.split("-").reverse().join("/");                  
				                   var offStartTime =  populateOfferMessageTime(thirdpart);                
				                   var offStart = thirdpart.concat(offStartTime);
				                $(".offerstartyear-time, #offerstartyearTimePrimary").html(resfirstpart);
				                $(".offerstarthour-time, #offerstarthourTimePrimary").html(offStart);                
								var enddateSplit = messageEndDate.split(" ");
				                var enddtfstpart = enddateSplit[0];
				                var enddtsecondpart = enddateSplit[1];
				                var edthirdpart = enddtsecondpart.split(".")[0];                  
				                var resEdfirstpart = enddtfstpart.split("-").reverse().join("/");                 
				                var offEndTime =  populateOfferMessageTime(edthirdpart);              
				                var offEnd = edthirdpart.concat(offEndTime);               
				              $(".offerendyear-time,#offerendyearTimePrimary").html(resEdfirstpart);
				              $(".offerendhour-time,#offerendhourTimePrimary").html(offEnd);            
								 }
								 /*else
								 {									
									 x.append("<p>"+message+"</p>");								 
								 }	*/			
								})	
														
								}						
				}
			});	
		}		
		// End of CAR-327
	 
	 
	 
	 
	 function sortPriceDesc(sellerPageCount){
		 var buyboxSeller = $("#ussid").val();
		 var aFinalPrice="";
	     var bFinalPrice="";
	     //UF-34
	     var oosSellers = [];
	     
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
		  
		//UF-34
	      
	      for (var sel =0; sel <sellerDetailsArray.length; sel++) { 
				 if(sellerDetailsArray[sel].availableStock < 1) {
					 oosSellers.push(sellerDetailsArray[sel]);
				 }
			 }
	      
	      sellerDetailsArray = sellerDetailsArray.filter(function(val) {
	    	  return oosSellers.indexOf(val) == -1;
	    	});
	      
	      sellerDetailsArray = sellerDetailsArray.concat(oosSellers);
	      
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
						$("#minPriceId").html(data['minPrice'].formattedValueNoDecimal);
					}

					$("#ussid").val(data['sellerArticleSKU']);
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
						$("#minPrice").html(data['minPrice'].formattedValueNoDecimal);
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
