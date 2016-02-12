  var sellerDetailsArray=[];
/*Display next list of sellers after clicking next link*/
  var pageCount=1;
function nextPage()
{
		++pageCount;
		if(pageCount>1)
		{
			$("#previousPageDev").show();	
		}
		setSellerLimits(pageCount);	
		focusOnElement();
}
/*Display previous list of sellers after clicking previous link*/	
function previousPage()
{
	pageCount= pageCount-1;
	if(pageCount<Math.ceil((sellerDetailsArray.length)/pageLimit))
	{
		$("#nextPageDev").show();	
	}
	setSellerLimits(pageCount);
	focusOnElement();
}
	
function focusOnElement() {
	/*window.scrollTo(0,$('#other-sellers-id').offset().top);*/
}


	/*Facebook share page*/
	(function(d, s, id) {
		var js, fjs = d.getElementsByTagName(s)[0];
		if (d.getElementById(id))
			return;
		js = d.createElement(s);
		js.id = id;
		js.src = "//connect.facebook.net/en_GB/sdk.js#xfbml=1&version=v2.3";
		fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));
	/*Twitter share page*/
	!function(d, s, id) {
		var js, fjs = d.getElementsByTagName(s)[0], p = /^http:/
				.test(d.location) ? 'http' : 'https';
		if (!d.getElementById(id)) {
			js = d.createElement(s);
			js.id = id;
			js.src = p + '://platform.twitter.com/widgets.js';
			fjs.parentNode.insertBefore(js, fjs);
		}
	}(document, 'script', 'twitter-wjs');
	
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
		//alert("seller page...");
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
	    if(isproductPage=='false'){
	    	sellerskuidList=$("#sellersSkuListId").val();
	    	ussidIdsForED=$("#skuIdForED").val();
	    	ussidIdsForHD=$("#skuIdForHD").val();
	    	ussidIdsForCNC=$("#skuIdForCNC").val();
	    	ussidIdsForCOD=$("#skuIdForCod").val();
	    	stockUssidIds=$("#skuIdsWithNoStock").val();
	    	stockUssidArray=$("#stockDataArray").val();
	    //	pincodeChecked=$("#pinCodeCheckedFlag").val();
	    }
	   
	    for (var i = 0; i < sellersArray.length; i++) {
		if(buyboxSeller!=sellersArray[i]['ussid']){	 
			var leadTime=0;
			if(null!=sellersArray[i]['leadTimeForHomeDelivery']){
				leadTime=sellersArray[i]['leadTimeForHomeDelivery'];
			}
		
		if(sellerskuidList.indexOf(sellersArray[i]['ussid'])==-1){
		sellerDetailsArray[++index]	=sellersArray[i];
	  	tbodycontent+="<tr id='tr"+index+"Id'>";  	
	 	stock = sellersArray[i]['availableStock'];
	 	ussid = sellersArray[i]['ussid'];
	 	var formStart = "<div id='addToCartFormId"+index+"Title' name='addToCartFormId"+index+"Title' class='addToCartTitle'>"+$('#addtocartid').text()+"</div>" + "<div id='addToCartFormId"+index+"Title' class='addToCartTitle sellerAddToBagTitle'>"+$('#addtocartid').text()+"</div>" +
		"<form method='post'  name='addToCartFormId"+index+"' id='addToCartFormId"+index+"' class='add_to_cart_form' action='#'>";
	  	//setting seller name
	  	tbodycontent+="<td width='278px'><h3>"; 
	  	tbodycontent+=sellersArray[i]['sellername'];
	  	tbodycontent+="</h3></td>"; 
		//setting price //TODO : price logic
	  	tbodycontent+="<td width='153px'>"; 
	  
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
		tbodycontent+="<div class='price price-line-height'>";
		if(originalPriceValue > 0 && originalPriceValue!=sellerPriceValue){
		tbodycontent+="<del>";	
		tbodycontent+=originalPrice.formattedValue;
		tbodycontent+="</del>";
		tbodycontent+=" ";
		}
		tbodycontent+="<font color=#ff1c47>"
		tbodycontent+=sellerPrice.formattedValue;
		tbodycontent+="</font>"
		tbodycontent+="</div>"
		if(promorestrictedSellers!=undefined){
			   if(promorestrictedSellers.indexOf(sellersArray[i]['sellerID'])!=-1){
				   tbodycontent+="<div class='tooltip_wrapper offer-tooltip-wrapper'><a name='yes' id='offer"+index+"' >Offer Available</a><span class='tooltip_pop offer-popover'>"+promoData.promoDescription+"<br><br>From&nbsp;"+promoStartDate+"<br>To&nbsp;"+promoEndDate+"<br>'</span></div>";
	     }
		}
		
	  	if (parseFloat(sellerPriceValue) > emiCuttOffAmount.value) {
	  		tbodycontent+="<div class='emi'>";
	  		tbodycontent+=$('#emiavailableid').text();
	  		tbodycontent+="</div>";
		}
	  	tbodycontent+="</td>"; 
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
			  /*for(var k in availableDeliveryATP){
				  if($("#isPinCodeChecked").val()!="true"){
			  		//if((ussidIdsForED=="") || (ussidIdsForHD=="")){
			  		  		if(isHomeDelivery && (availableDeliveryATP[k].toLowerCase().indexOf("home")!=-1)){
			  		  			deliveryModeMap+=availableDeliveryATP[k]+"<br/>";
			  		  		}
			  		  	   if(isExpressDelivery&&(availableDeliveryATP[k].toLowerCase().indexOf("express")!=-1)){
			  	           	 deliveryModeMap+=availableDeliveryATP[k]+"<br/>";
		  		  		}
			  			}
			  		else{
			  			if((availableDeliveryATP[k].toLowerCase().indexOf("express")!=-1) && (ussidIdsForED.indexOf(ussid)!=-1)){
				  			deliveryModeMap+=availableDeliveryATP[k]+"<br/>";
				  		}
				  		if((availableDeliveryATP[k].toLowerCase().indexOf("home")!=-1) && (ussidIdsForHD.indexOf(ussid)!=-1)){
				  			deliveryModeMap+=availableDeliveryATP[k]+"<br/>";
					  		}
			  		}
			     	}  */
	  	tbodycontent+="<td width='669px'><ul>";
	  		  
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
	  	tbodycontent+="</ul></td>";
	  	tbodycontent+="<td width='219px'>";
	  	
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
		  		tbodycontent+="<div id='addToCartFormId"+index+"excedeInventory' style='display:none;'>"+$('#addToCartFormexcedeInventory').text()+"</div>";
				tbodycontent+="<div id='addToCartFormId"+index+"noInventory' style='display:none;'>"+$('#addToCartFormnoInventory').text()+"</div>";
				tbodycontent+="<button id='addToCartButton"+index+"' type='button' class='button add-to-bag btn-block js-add-to-cart'>"+$('#addtobagid').text()+"</button>";
				tbodycontent+=$("#hiddenIdForStock").html();
		  	}
	    }
	    
	  	tbodycontent+=formEnd;
	  	
	  	tbodycontent+=formStartForWishList;
		tbodycontent+=$("#hiddenWishListId").html();
		    

		tbodycontent+="<a onClick='openPop(\""+ussid+"\");' id='wishlist' class='wishlist add-to-wishlist' data-toggle='popover' data-placement='bottom'>Add to Wishlist</a>";
		tbodycontent+=formEnd;
		
	  	tbodycontent+="</td>";  	  	
	  	tbodycontent+="</tr>";
	  	
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
	
}
	 function addToBag(index){
		
		$(document).on('click','#addToCartFormId'+index+' .js-add-to-cart',function(){
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

	 function fetchAllSellers(stockDataArrayList) {
		 var buyboxSeller = $("#ussid").val();
		    var modifiedData="";
			var isproductPage = $("#isproductPage").val();
			var productCode = $("#product").val();
			var requiredUrl = ACC.config.encodedContextPath + "/p" + "/" + productCode
					+ "/otherSellerDetails";
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
					fetchSellers(data,buyboxSeller);
					otherSellersCount = data.length;
					setSellerLimits(1);
					  var stock_id="stock";
					  var ussid = "ussid";
					  var input_name = "qty";
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
	 function sort(value)
	 {
		 if(value == 1)
	    	 sortPrice(pageCount);
	     else if(value ==2)
	    	 sortPriceDesc(pageCount);
	     else if(value ==3)
	    	 sellerNameAsc(pageCount);
	     else if(value == 4)
	    	 sellerNameDesc(pageCount);
		 /*pageCount=1;*/
	 }
	 function sortPrice(pageCount){
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
			  setSellerLimits(pageCount);
	 }
	
	 
	 function sortPriceDesc(pageCount){
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
		  setSellerLimits(pageCount);
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
