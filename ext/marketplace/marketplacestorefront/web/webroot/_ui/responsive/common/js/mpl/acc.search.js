function constructDepartmentHierarchy(inputArray) {		
	var inputArray = unescape(inputArray);	
	var output = [];
		
		if(inputArray!=""){
		for (var i = 0; i < inputArray.length; i++) {				
			var categoryArray = inputArray[i].split("|");			
			var currentNode = output;
			//Construct 'All' tree node initially for search page
			if(i==0 && $('#isCategoryPage').val() == '') {
				output[0] = {label: "All", children: [], categoryCode: "", categoryType: "All", categoryName: ""};
			}
			//Other tree nodes are constructed here
			for (var j = 0; j < categoryArray.length; j++) {				
				if(categoryArray[j] != null && categoryArray[j].length > 0){
					var categoryDetails = categoryArray[j].split(":");
					var categoryCode = categoryDetails[0];
					var categoryName = categoryDetails[1];
					var facetCount = 0;
					
					if(categoryDetails[2] == "L3" || categoryDetails[2] == "L4")
					{
						//categoryName += "  (" +categoryDetails[5] + ")";
						facetCount = "  (" +categoryDetails[5] + ")";
												
					}
					
					var categoryType = "category";
					if(categoryDetails[3] == 'true') {
						categoryType = "department"
					}
					var lastNode = currentNode;
					for (var k = 0; k < currentNode.length; k++) {
						if (currentNode[k].categoryName == categoryName) {							
							currentNode = currentNode[k].children;								
							break;
						}						
					}
					if (lastNode == currentNode) {
						var newNode = currentNode[k] = {label: categoryName, children: [], categoryCode: categoryCode, categoryType: categoryType, categoryName: categoryName, facetCount: facetCount};
						currentNode = newNode.children;						
					}
				}
			}
		}
		}
	var expandTree = false;
	
	//TISCF-4 Start
	//The Department Hierarchy Tree should always remain Closed for Both PLP and SERP
//	if(output.length == 2) {
//		expandTree = true;
//	}
	//TISCF-4 End
	
	//TISPT-304 starts
	
	$( ".serpProduct" ).each(function( index ) {
		var product=$(this).closest('span').find('#productCode').val();
		 // console.log("prod"+product);
		  var categoryTypeValue=$(this).closest('span').find('#categoryType').val()
		 //  console.log("categoryTypeValue"+categoryTypeValue);
		  var productUrl=$(this).closest('span').find('#productUrl').val();
		 //console.log("productUrl"+productUrl);
		  var productPrice=$(this).closest('span').find('#productPrice').val();
		//  console.log("productPrice"+productPrice);
		  var list=$(this).closest('span').find('#list').val();
		//  console.log("list"+list);
		  var mrpPriceValue=$(this).closest('span').find('#mrpPriceValue').val();
		//  console.log("mrpPriceValue"+mrpPriceValue);
		  var sizeStockLevel=$(this).closest('span').find('#sizeStockLevel').val();
		 // console.log("sizeStockLevel"+sizeStockLevel);
		  var productPromotion=$(this).closest('span').find('#productPromotion').val();
		 // console.log("productPromotion"+productPromotion);
		  populateFacet();
		  if(typeof(serpSizeList)!= "undefined"){
			modifySERPDetailsByFilters(serpSizeList,product,categoryTypeValue,list,productUrl,productPrice,mrpPriceValue,sizeStockLevel,productPromotion);
		 } 
		});
	
	//TISPT-304 ends
	
	
	if($('#isCategoryPage').val() == 'true'){	
		// Assign tree object to category page
		$("#categoryPageDeptHierTree").tree({
			data: output,
			 openedIcon:'',
			 openedIcon: '',
			//TISCF-4 Start
			//autoOpen: true
			//The Department Hierarchy Tree should always remain Closed for Both PLP and SERP
			autoOpen: true
			//TISCF-4 End
	
		});
	}else {
		// Assign tree object to search page
		$("#searchPageDeptHierTree").tree({
			data: output,
			 closedIcon:'',
			 openedIcon:'',
			autoOpen: true
	
		});
		
		// persist search text in search text box
		 var isConceirge = $('#isConceirge').val();
			if(isConceirge!='true') {
			ACC.autocomplete.bindSearchText($('#text').val());
			}
	}
	
	$('#categoryPageDeptHierTree').bind(
			'tree.click',
			function(event) {
				var node = event.node;
				if(node.categoryType != 'All') {
					var actionText = ACC.config.contextPath;
					actionText = (actionText + '/Categories/' + node.name + '/c-' + node.categoryCode);
					$('#categoryPageDeptHierTreeForm').attr('action',actionText);
					
					$('#categoryPageDeptHierTreeForm').submit();
				}
			}
	);
	
	$('#searchPageDeptHierTree').bind(
			'tree.click',
			function(event) {
				var node = event.node;
				var searchQuery = document.getElementById("q").value;				
				if(node.categoryType == 'All') {
					$('#q').val($('#text').val() + ":relevance");
					$('#searchCategoryTree').val("all");
				}
				else{
					//Changes Added for TOR-488
					//$('#q').val($('#text').val() + ":relevance:category:" + node.categoryCode);
					//$('#searchCategoryTree').val(node.categoryCode);
					// alert($('#q').val());
					 //TISQAEE-14
					 if($('#q').val().indexOf(node.categoryCode)==-1){
						//INC_11754 start
						 if(node.categoryCode.indexOf($('#searchCategory').val())==-1){
							 $('#q').val(searchQuery +":category:" + node.categoryCode);
						 }else{			
						 	 $('#q').val($('#text').val() + ":relevance:category:" + node.categoryCode);
						 }
						 //INC_11754 end
					 }
					 $('#searchCategoryTree').val(node.categoryCode);
					
				} 
				
				$('#searchPageDeptHierTreeForm').submit();
				
			}
	);
		
	}


  //TPR-158 and TPR-413 starts here

$(document).ready(function(){	
	$("#displayAll").show();
	$("#clickToMore").hide();
	donotShowAll();	
	
	$("#displayAll").on("click",function(e){	
		showAll();		
		$("#displayAll").hide();
		$("#clickToMore").show();
		});

		//clicking on the clickToMore div will display limited department categories.
	$("#clickToMore").click(function(e){	
		donotShowAll();		
		$("#displayAll").show();
		$("#clickToMore").hide();
		});
});



//For department Hierarchy Expansion
function showAll()
{
	//alert("Hi in Show All");
	$(".jqtree-tree >li").each(function(e){		
	$(this).show();//show all		
	$(this).find("ul>li").each(function(e){
	$(this).show();//show all
	});		
	});
}


function donotShowAll()
{	//alert("Hi in donotShowAll");
	//below attributes can be made configurable through local.properties. The number of categories to be displayed are configurable for L1,L2 and L3 separately
	var l1ClassCount = -1;
	var l2ClassCount = -1;
	var l3ClassCount = -1;
	var l1displayLimit = $("#deptCountL1").val();
	var l2displayLimit = $("#deptCountL2").val();
	var l3displayLimit = $("#deptCountL3").val();
	var displayHideShowAll = false;
	
	//***************below first iteration for L1 categories
	$(".jqtree-tree >li").each(function(e){
	
	l1ClassCount= l1ClassCount+1;
	
	if(l1ClassCount>l1displayLimit)
	{
	$(this).hide();//hide L1 level
	displayHideShowAll = true;
	}	
	
	//l2ClassCount = -1 ;
	l2ClassCount = 0;
	//**********below iteration for L2 and L3 categories
	$(this).find("ul>li").each(function(e){	
	
	//if li has both class jqtree_common and jqtree-folder then it will be L2 category
	if($(this).hasClass('jqtree_common') && $(this).hasClass('jqtree-folder'))
	{
	l2ClassCount= l2ClassCount+1;	
	//l3ClassCount = -1;
	l3ClassCount = 0;
	if(l2ClassCount>l2displayLimit){
	$(this).hide();//hide L2 level
	displayHideShowAll = true;
	}
	}
	//if li has only class jqtree_common then it will be L3 category
	else if($(this).hasClass('jqtree_common'))
	{
	l3ClassCount= l3ClassCount+1;	
	if(l3ClassCount>l3displayLimit){
	$(this).hide();//hide L3 level
	displayHideShowAll = true;
	}
	}	
	else
	{
	//do nothing
	}
	
	});		//end ul>li iteration
		
	});		//end .jqtree-tree >li iteration	
	
	
	if(!displayHideShowAll)
	{	
	$("#displayAll").hide();
	}
}


	
		
//CR Changes End
//TPR-158 and TPR-413 ends here



	//change serp product details based on filters
	function modifySERPDetailsByFilters(serpSizeList,product,categoryTypeValue,list,productUrl,productPrice,mrpPriceValue,stockLevel,productPromotion,wishListUrl){
		if(mrpPriceValue!="" && productPrice!=""){
		}

		if(categoryTypeValue=='Apparel'||categoryTypeValue=='Footwear'){
		if(serpSizeList!=''){
		var sizeMatched = checkSizeCount(list, serpSizeList);
		var price1 = productPrice.replace("[[", "");
		var price2 = price1.replace("]]", "");
		var prcArr = new Array();
		prcArr = price2.split(',');
		var priceValueList = [];
		var countPrice = -1;
		var minPriceSize = "";
		var minPriceValue = "";
		for (k = 0; k < prcArr.length; k++) {
			if(prcArr[k]!=""){
			var x = JSON.parse(prcArr[k]);
			var priceArrayList = [];
			if (sizeMatched != "") {
				minPriceSize = sizeMatched;
				if(x[sizeMatched]!=undefined){
				minPriceValue = x[sizeMatched];
				/*console.log("size match for single variant"
						+ minPriceSize + "price"
						+ minPriceValue); */
				}
				 


			} else {
				for (h = 0; h < serpSizeList.length; h++) {
					var sizePrice = serpSizeList[h];
					if (x[sizePrice] != undefined) {
						minPriceSize = sizePrice;
						priceValueList[++countPrice] = parseInt(x[sizePrice]);

					}
				}
			}
		}
		}


		if (sizeMatched == "") {
			if(priceValueList!=""||priceValueList!=[]){
			priceValueList.sort(function(a, b) {
				return a - b
			});
			minPriceValue = priceValueList[0];
			minPriceSize = findSizeBasedOnMinPrice(
					minPriceValue, prcArr);
		}
		}
		



		 /*console.log("nminPrice for product" +product+"price"+minPriceValue + "minsize"
				+ minPriceSize); */
		if(minPriceValue!=undefined){ 
	    $("#price_"+product).html("");
		$("#price_"+product).html("&#8377;"+minPriceValue);  
		}

		//set product mrp
		updateProductMrp(mrpPriceValue,sizeMatched, serpSizeList,minPriceSize,minPriceValue,product);
		//update product stock

		updateProductStock(stockLevel,sizeMatched, serpSizeList,minPriceSize,product);
		//updtae sale price
		if(typeof(productPromotion)!='undefined' && productPromotion!=""){//PRDI-34 issue fix
		findOnSaleBasedOnMinPrice(productPromotion, list , serpSizeList,product);
		}

		//updating product url
		var url1 = productUrl.replace("[[", "");
		var url2 = url1.replace("]]", "");
		var arr = new Array();
		arr = url2.split(',');
		for (i = 0; i < arr.length; i++) {
			if(arr[i]!=""){
			var x = JSON.parse(arr[i]);
			if (sizeMatched != "") {
				if (x[sizeMatched] != undefined) {
					$(".thumb_" + product).attr(
							"href",
							ACC.config.encodedContextPath
									+ "/p" + x[sizeMatched]);
					$(".name_" + product).attr(
							"href",
							ACC.config.encodedContextPath
									+ "/p" + x[sizeMatched]);
					$("#quickview_" + product).attr(
							"href",
							ACC.config.encodedContextPath
									+ "/p" + x[sizeMatched]
									+ "/quickView");
				}

			} else {

				for (j = 0; j < serpSizeList.length; j++) {
					var sizeUrl = serpSizeList[j];
					if (x[sizeUrl] != undefined) {
						if (minPriceSize == serpSizeList[j]) {
							$(".thumb_" + product)
									.attr(
											"href",
											ACC.config.encodedContextPath
													+ "/p"

													+ x[minPriceSize]);
							$(".name_" + product)
									.attr(
											"href",
											ACC.config.encodedContextPath
													+ "/p"

													+ x[minPriceSize]);
							$("#quickview_" + product)
									.attr(
											"href",
											ACC.config.encodedContextPath
													+ "/p"

													+ x[sizeMatched]);
						}
					}
				}
			}
		}//
		}
		}

	}
	}


	//find Onsale product based on filters		
	function findOnSaleBasedOnMinPrice(productPromotion, list , serpSizeList,product) {		
		//Taking Promotion from minimum size minPriceSize		
		var sizeMatched = checkSizeCount(list, serpSizeList);		
		var promo1 = productPromotion.replace("[[", "");		
		var promo2 = promo1.replace("]]", "");		
		var arr = new Array();		
		arr = promo2.split(',');	
		
		if(arr!= undefined) {		
			
		for (i = 0; i < arr.length; i++) {		
					
			var temp1 = arr[i].replace("]", "");		
			var temp2 = temp1.replace("[", "");		
			var x = JSON.parse(temp2);		
					
			if (sizeMatched != "") {		
				if (x[sizeMatched] != undefined) {
					 $("#on-sale_" + product).show();//showing on_sale tag		
					break;		
				}		
				else {		
					continue;		
				}		

			}		
			else {		
				 $("#on-sale_"+ product).show();//showing on_sale tag		
				break;		
		}		
		}		


		}		





	}
	//get the minimum priced variant



	function findSizeBasedOnMinPrice(priceValue, priceArray) {
		for (pIndex = 0; pIndex < priceArray.length; pIndex++) {
			if(priceArray[pIndex]!=""){
			var proceJson = JSON.parse(priceArray[pIndex]);
			var minPriceSize = 0.0;
			for (l = 0; l < serpSizeList.length; l++) {
				var minsizePrice = serpSizeList[l];
				if (proceJson[minsizePrice] == priceValue) {
					minPriceSize = minsizePrice;
					return minPriceSize;
				}
			}
			}
		}
	}
	//update product stock


//	function updateProductStock(sizeStockLevel,sizeMatched, serpSizeList,minPriceSize,product) {
//		var stock1 = sizeStockLevel.replace("[[", "");
//		var stock2 = stock1.replace("]]", "");
//		var stockArray = new Array();
//		stockArray = stock2.split(',');
//		for (i = 0; i < stockArray.length; i++) {
//			if(stockArray[i]!=""){
//			var stckData = JSON.parse(stockArray[i]);
//			if (sizeMatched != "") {
//				if (stckData[sizeMatched] != undefined) {
//					$("#stockIdFiltered_" + product).val(stckData[sizeMatched]);
//				}
//
//			} else {
//				for (j = 0; j < serpSizeList.length; j++) {
//					var sizeUrl = serpSizeList[j];
//					if (stckData[sizeUrl] != undefined) {
//						if (minPriceSize == serpSizeList[j]) {
//							$("#stockIdFiltered_" + product).val(
//									stckData[sizeMatched]);
//						}
//					}
//				}
//			}
//		}
//		}
//	}
	
	//update product stock


	function updateProductStock(sizeStockLevel,sizeMatched, serpSizeList,minPriceSize,product) {
		var query="";
		var stock1 = sizeStockLevel.replace("[[", "");
		var stock2 = stock1.replace("]]", "");
		var stockArray = new Array();
		stockArray = stock2.split(',');
		/*$( ".facet-values js-facet-values js-facet-form  active" ).each(function( index ) {
	    var query=query+$(this).closest('li').find('#q').val();
		});	  
		console.log("logg"+query);*/
		for (i = 0; i < stockArray.length; i++) {
			if(stockArray[i]!=""){
			var stckData = JSON.parse(stockArray[i]);
			if (sizeMatched != "") {
				
				if (stckData[sizeMatched] != undefined) {
					
					if(stckData[sizeMatched]=='outOfStock'){
						
					  $(".AvailabilitySize").show();
					  $(".Availability").hide();
								//}
								
					$("#stockIdDefault_" + product).parents('div.image').find('a img').addClass('out-of-stock-product');		
					$("#stockIdDefault_" + product).html("OUT OF STOCK");
					$("#stockIdDefault_" + product).show();
					
//					$("#stockIdFilteredVariant_" + product).show();
//					$("#stockIdFilteredVariant_" + product).html("OUT OF STOCK");
					}
				}

			} else {
				for (j = 0; j < serpSizeList.length; j++) {
					var sizeUrl = serpSizeList[j];					
					if (stckData[sizeUrl] != undefined) {						
						if (minPriceSize == serpSizeList[j]) {							
							if(stckData[sizeMatched]=='outOfStock'){
								 $(".AvailabilitySize").show();
								 $(".Availability").hide();								
								 
								$("#stockIdDefault_" + product).parents('div.image').find('a img').addClass('out-of-stock-product');
								$("#stockIdDefault_" + product).html("OUT OF STOCK");
								$("#stockIdDefault_" + product).show();	
								
//								$("#stockIdFilteredVariant_" + product).show();
//								$("#stockIdFilteredVariant_" + product).html("OUT OF STOCK");
								
							}
							/*$("#stockIdFiltered_" + product).val(
									stckData[sizeMatched]);*/
						}
					}
				}
			}
		}
		}
	}


	//update product minimum price and mrp
	function updateProductMrp(mrpPriceValue,sizeMatched, serpSizeList,minPriceSize,minPriceValue,product) {
	//  var mrpPriceValue = '${product.displayMrp}';
		if(mrpPriceValue!=""){
	//	console.log("###"+mrpPriceValue);
	    var productCode='${product.code}';
		var mrpPrice1 = mrpPriceValue.replace("[[", "");
		var mrpPrice2 = mrpPrice1.replace("]]", "");
		var mrpPriceArray = new Array();
		mrpPriceArray = mrpPrice2.split(',');
		for (i = 0; i < mrpPriceArray.length; i++) {
			if(mrpPriceArray[i]!=""){
			var mrpPriceData = JSON.parse(mrpPriceArray[i]);
			if (sizeMatched != "") {
				if (mrpPriceData[sizeMatched] != undefined) {
					//console.log("mrp+"mrpPriceData[sizeMatched]+"minPrice"+minPriceValue);
					if(mrpPriceData[sizeMatched]<=minPriceValue){
						$("#priceEqual_"+product).html("");
						$("#priceEqual_"+product).html("&#8377;"+minPriceValue);
					}else if(mrpPriceData[sizeMatched]>minPriceValue){
						$("#mrpprice_"+product).html("");
						$("#mrpprice_"+product).html("&#8377;"+(mrpPriceData[sizeMatched])); 
						$("#price_"+product).html("");
						$("#price_"+product).html("&#8377;"+minPriceValue);
					}
					
				}


			} else {
				for (j = 0; j < serpSizeList.length; j++) {
					var sizeUrl = serpSizeList[j];
					if (mrpPriceData[sizeUrl] != undefined) {
						if (minPriceSize == serpSizeList[j]) {
						//	console.log("min price size"+minPriceSize+"mrp"+mrpPriceData[minPriceSize]+"minPrice"+minPriceValue);
							if(mrpPriceData[minPriceSize]<=minPriceValue){
								$("#priceEqual_"+product).html("");
								$("#priceEqual_"+product).html("&#8377;"+minPriceValue);
							}else if(mrpPriceData[minPriceSize]>minPriceValue){
								$("#mrpprice_"+product).html("");
								$("#mrpprice_"+product).html("&#8377;"+(mrpPriceData[minPriceSize])); 
								$("#price_"+product).html("");
								$("#price_"+product).html("&#8377;"+minPriceValue);
							}
						}
					}
				}
			}
		}
		}
		}
	}


	//finding the sizes of a product matched with the applied filters
	function checkSizeCount(list, serpSizeList) {
		var count = 0;
		var matchedSize = "";
		var productSizes = list.replace("[", "");
		var finalProductSizeArray = productSizes.replace("]", "")
//		console.log("arrays" + list + finalProductSizeArray);
		var temp = new Array();
		temp = finalProductSizeArray.split(",");
		for (var j = 0; j < serpSizeList.length; j++) {
			if (finalProductSizeArray.indexOf(serpSizeList[j]) != -1) {
				count++;
				matchedSize = serpSizeList[j];
			}
		}

		if (count > 1) {

			matchedSize = "";
		}
		//console.log("count" + matchedSize + "count" + count);

		return matchedSize;
	}
	
	$( "#brandNoFormSubmit" ).submit(function() {
		  event.preventDefault();
	});
	
	 $(".facet-name.js-facet-name h3").each(function(){
		 var facetStockSize=$("#facetStockSize").val();
		 if($("#stockStatusId").val()!= "true" && facetStockSize==1){
			 $(".Availability").hide();
		 }
		if($(this).hasClass("true")){
			$(this).addClass("active");
			$(this).parent().siblings('.facet-values.js-facet-values.js-facet-form').addClass("active");
	    	$(this).siblings('.brandSelectAllMain').addClass("active");
	    	$(this).parent().siblings('#searchPageDeptHierTreeForm').find("#searchPageDeptHierTree").addClass("active");
	    	$(this).parent().siblings('#categoryPageDeptHierTreeForm').find("#categoryPageDeptHierTree").addClass("active");
			
		}
	 }); 
	
	/*$(function() {
	    img = document.querySelectorAll('[data-searchimgsrc]');
	    for (var i = 0; i < img.length; i++) {
	        var self = img[i];
	          self.src = self.getAttribute('data-searchimgsrc');
	      }

	});*/

	 
	 $( document ).ready(function() {
		//Add to Wishlist PLP TPR-844 CR
		 $(".wishlist a").mouseover();
		var productCodePost = $("#productCode").val();
		getLastModifiedWishlistForPLP(productCodePost);
		//Ended here//
		}); 

		/*Wishlist In PLP changes*/
		function getLastModifiedWishlistForPLP(productCodePost) {
			var requiredUrl = ACC.config.encodedContextPath + "/search/"
					+ "getLastModifiedWishlistByPcode";	
			var dataString = 'pcode=' + productCodePost;	
			$.ajax({		
				contentType : "application/json; charset=utf-8",
				url : requiredUrl,
				data : dataString,
				dataType : "json",
				success : function(data) {
				if (data == true) {
					$('.product-info .picZoomer-pic-wp .zoom a,.product-image-container.device a.wishlist-icon').addClass("added");
					$("#add_to_wishlist").attr("disabled",true);
					$('.add_to_cart_form .out_of_stock #add_to_wishlist').addClass("wishDisabled");
				}
				else if(data == false) //UF-60
				{
					$('.product-info .picZoomer-pic-wp .zoom a,.product-image-container.device a.wishlist-icon').removeClass("added");
					$("#add_to_wishlist").attr("disabled",false);
					$('.add_to_cart_form .out_of_stock #add_to_wishlist').removeClass("wishDisabled");
				}
				
				},
				error : function(xhr, status, error) {		
					//alert("error"+error);
				}
			});
		}

$(document).on("click",".plp-wishlist",function(e){

	 /*TPR-250 changes*/
	var ussid=$(this).data("ussid");
	//addToWishlistForPLP($(this).data("product"),$(this).data("ussid"),this);
	/*TPR-250 changes*/
	if ( $( this ).hasClass( "added" ) ) {
		removeToWishlistForPLP($(this).data("product"),$(this).data("ussid"),this);

	} else {

		addToWishlistForPLP($(this).data("product"),$(this).data("ussid"),this);
	}
	return false;
})
		/*Changes for INC144313867*/


		function removeToWishlistForPLP(productURL,ussid,el) {
			var loggedIn=$("#loggedIn").val();

			var productCode=urlToProductCode(productURL);
			var wishName = "";
			var requiredUrl = ACC.config.encodedContextPath + "/search/"
					+ "removeFromWishListInPLP";	
		    var sizeSelected=true;
		    
		    if(!$('#variant li').hasClass('selected')) {
		    	sizeSelected=false;
		    }
		    if( $("#variant,#sizevariant option:selected").val()=="#"){
		    	sizeSelected=false;
		    }
		    var dataString = 'wish=' + wishName + '&product=' + productCode + '&ussid=' + ussid
			+ '&sizeSelected=' + sizeSelected;
			
		    //change for INC144314854 
			//if(loggedIn == 'false') {
		    if(!headerLoggedinStatus) {
				$(".wishAddLoginPlp").addClass("active");
				setTimeout(function(){
					$(".wishAddLoginPlp").removeClass("active")
				},3000)
				return false;
			}	
			else {	
				$.ajax({			
					contentType : "application/json; charset=utf-8",
					url : requiredUrl,
					data : dataString,
					dataType : "json",			
					success : function(data){
						if (data == true) {					
							$(".wishRemoveSucessPlp").addClass("active");
							setTimeout(function(){
								$(".wishRemoveSucessPlp").removeClass("active")
							},3000)
							$(el).removeClass("added");
						}
						else{
							$(".wishAlreadyAddedPlp").addClass("active");
							setTimeout(function(){
								$(".wishAlreadyAddedPlp").removeClass("active")
							},3000)
						}
						//TPR-6364
						dtmRemoveFromWishlist($('#pageType').val(),productCode,$('#categoryType').val());
						
					},
					error : function(xhr, status, error){
						alert(error);
					}
				});
				
				setTimeout(function() {
					$('a.wishlist#wishlist').popover('hide');
					$('input.wishlist#add_to_wishlist').popover('hide');

					}, 0);
			}
			return false;
		}



		function addToWishlistForPLP(productURL,ussid,el) {
			var loggedIn=$("#loggedIn").val();
			var productCode=urlToProductCode(productURL);
			var productarray=[];
			productarray.push(productCode);
			var wishName = "";
			var requiredUrl = ACC.config.encodedContextPath + "/search/"
					+ "addToWishListInPLP";	
		    var sizeSelected=true;
		    
		    if(!$('#variant li').hasClass('selected')) {
		    	sizeSelected=false;
		    }
		    if( $("#variant,#sizevariant option:selected").val()=="#"){
		    	sizeSelected=false;
		    }
		     var dataString = 'wish=' + wishName + '&product=' + productCode + '&ussid=' + ussid
					+ '&sizeSelected=' + sizeSelected;
			
		    // Change for INC144314854 
			//if(loggedIn == 'false') {
		    if(!headerLoggedinStatus) {
				$(".wishAddLoginPlp").addClass("active");
				setTimeout(function(){
					$(".wishAddLoginPlp").removeClass("active")
				},3000)
				return false;
			}	
			else {	
				$.ajax({			
					contentType : "application/json; charset=utf-8",
					url : requiredUrl,
					data : dataString,
					dataType : "json",			
					success : function(data){
						if (data == true) {					
							$(".wishAddSucessPlp").addClass("active");
							setTimeout(function(){
								$(".wishAddSucessPlp").removeClass("active")
							},3000)
							$(el).addClass("added");
						}
						else{
							$(".wishAlreadyAddedPlp").addClass("active");
							setTimeout(function(){
								$(".wishAlreadyAddedPlp").removeClass("active")
							},3000)
						}
						/*TPR-4723*//*TPR-4708*/
						if($('#pageType').val() == "productsearch"){
							if(typeof utag !="undefined"){
								utag.link({link_text: "add_to_wishlist_serp" , event_type : "add_to_wishlist_serp" ,product_sku_wishlist : productarray});
								}
						}
						
						if($('#pageType').val() == "category" || $('#pageType').val() == "electronics" ){
							if(typeof utag !="undefined"){
								utag.link({link_text: "add_to_wishlist_plp" , event_type : "add_to_wishlist_plp" ,product_sku_wishlist : productarray});
								}
						}
						//TPR-6364
						dtmAddToWishlist($('#pageType').val(),productCode,$('#categoryType').val());
						
					},
					error : function(xhr, status, error){
						alert(error);
						if(typeof utag !="undefined"){
							utag.link({error_type : 'wishlist_error'});
							}
					}
				});
				
				setTimeout(function() {
					$('a.wishlist#wishlist').popover('hide');
					$('input.wishlist#add_to_wishlist').popover('hide');

					}, 0);
			}
			return false;
		}
		
		function urlToProductCode(productURL) {
			var n = productURL.lastIndexOf("-");
			var productCode=productURL.substring(n+1, productURL.length);
			//CKD:TPR-250:Start : to discard msiteSellerId from URL when adding to Wishlist
			var ind = productCode.indexOf("?");
			if (ind != -1){
				productCode=productCode.substring(0,ind);
			}
			//CKD:TPR-250:End
		    return productCode.toUpperCase();
			
		}
		
