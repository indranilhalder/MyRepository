function constructDepartmentHierarchy(inputArray) {		
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
					
					if(categoryDetails[2] == "L3")
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
		 // console.log("productUrl"+productUrl);
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
				if(node.categoryType == 'All') {
					$('#q').val($('#text').val() + ":relevance");
					$('#searchCategoryTree').val("all");
				}
				else{
					$('#q').val($('#text').val() + ":relevance:category:" + node.categoryCode);
					$('#searchCategoryTree').val(node.categoryCode);
				} 
				
				$('#searchPageDeptHierTreeForm').submit();
				
			}
	);
		
	}

	//change serp product details based on filters
	function modifySERPDetailsByFilters(serpSizeList,product,categoryTypeValue,list,productUrl,productPrice,mrpPriceValue,stockLevel,productPromotion){
		if(mrpPriceValue!="" && productPrice!=""){
	/*	console.log("in search js...for product"+product+"mrpPriceJSon"+mrpPriceValue+"price json"+productPrice);	
		console.log("original prices for "+product+$("#price_"+product).text()+$("#priceEqual_"+product).text());*/
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
		if(productPromotion!=""){
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
	
	 $(".facet-name.js-facet-name h4").each(function(){
		 
		 /*if($("#stockStatusId").val()!= "true"){
			 $(".Availability").hide();
		 }*/
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
