	function constructDepartmentHierarchy(inputArray) {
		var output = [];
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
						var newNode = currentNode[k] = {label: categoryName, children: [], categoryCode: categoryCode, categoryType: categoryType, categoryName: categoryName};
						currentNode = newNode.children;
					}
				}
			}
		}

	var expandTree = false;
	if(output.length == 2) {
		expandTree = true;
	}
	if($('#isCategoryPage').val() == 'true'){	
		// Assign tree object to category page
		$("#categoryPageDeptHierTree").tree({
			data: output,
			autoOpen: true
	
		});
	}else {
		// Assign tree object to search page
		$("#searchPageDeptHierTree").tree({
			data: output,
			autoOpen: expandTree
	
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
					actionText = (actionText + '/Categories/' + node.label + '/c/' + node.categoryCode);
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
	
	//find Onsale product based on filters

	function findOnSaleBasedOnMinPrice(productPromotion, list , serpSizeList,product) {
			
		if(serpSizeList!='' && productPromotion!="" ){ 
			if(categoryTypeValue=='Apparel'||categoryTypeValue=='Footwear'){
		
		//Taking Promotion from minimum size minPriceSize
		var sizeMatched = checkSizeCount(list, serpSizeList);

		var promo1 = productPromotion.replace("[[", "");
		var promo2 = promo1.replace("]]", "");
		var arr = new Array();
		arr = promo2.split(',');
		if(arr!= "") {

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
		}
		else {
	
			 $("#on-sale_"+ product).show();//showing on_sale tag
		}

	}
	
	//change serp product details based on filters
function modifySERPDetailsByFilters(serpSizeList,product,categoryTypeValue,list,productUrl,productPrice,mrpPriceValue,stockLevel){
	console.log("in search js..."+product+mrpPriceValue);
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
		var x = JSON.parse(prcArr[k]);
		var priceArrayList = [];
		if (sizeMatched != "") {
			minPriceSize = sizeMatched;
			if(x[sizeMatched]!=undefined){
			minPriceValue = x[sizeMatched];
			console.log("size match for single variant"
					+ minPriceSize + "price"
					+ minPriceValue); 
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
	if (sizeMatched == "") {
		priceValueList.sort(function(a, b) {
			return a - b
		});
		
		minPriceValue = priceValueList[0];
		minPriceSize = findSizeBasedOnMinPrice(
				minPriceValue, prcArr);
	}

	 console.log("nminPrice" + minPriceValue + "minsize"
			+ minPriceSize); 
	//updating product minimum price
     $("#price_"+product).html("");
	$("#price_"+product).html("&#8377;"+minPriceValue);  
	//set product mrp
	updateProductMrp(mrpPriceValue,sizeMatched, serpSizeList,minPriceSize,minPriceValue,product);
	//update product stock
	updateProductStock(stockLevel,sizeMatched, serpSizeList,minPriceSize,product);
	//updating product url
	var url1 = productUrl.replace("[[", "");
	var url2 = url1.replace("]]", "");
	var arr = new Array();
	arr = url2.split(',');
	for (i = 0; i < arr.length; i++) {
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
	}
	}
}
}
//get the minimum priced variant
function findSizeBasedOnMinPrice(priceValue, priceArray) {
	for (pIndex = 0; pIndex < priceArray.length; pIndex++) {
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
//update product stock
function updateProductStock(sizeStockLevel,sizeMatched, serpSizeList,minPriceSize,product) {
	//var sizeStockLevel = "${product.displayStock}";
	var stock1 = sizeStockLevel.replace("[[", "");
	var stock2 = stock1.replace("]]", "");
	var stockArray = new Array();
	stockArray = stock2.split(',');
	for (i = 0; i < stockArray.length; i++) {
		var stckData = JSON.parse(stockArray[i]);
		if (sizeMatched != "") {
			if (stckData[sizeMatched] != undefined) {
				$("#stockIdFiltered_" + product).val(stckData[sizeMatched]);
			}
		} else {
			for (j = 0; j < serpSizeList.length; j++) {
				var sizeUrl = serpSizeList[j];
				if (stckData[sizeUrl] != undefined) {
					if (minPriceSize == serpSizeList[j]) {
						$("#stockIdFiltered_" + product).val(
								stckData[sizeMatched]);
					}
				}
			}
		}
	}
}
//update product minimum price and mrp
function updateProductMrp(mrpPriceValue,sizeMatched, serpSizeList,minPriceSize,minPriceValue,product) {
//  var mrpPriceValue = '${product.displayMrp}';
	console.log("###"+mrpPriceValue);
    var productCode='${product.code}';
	var mrpPrice1 = mrpPriceValue.replace("[[", "");
	var mrpPrice2 = mrpPrice1.replace("]]", "");
	var mrpPriceArray = new Array();
	mrpPriceArray = mrpPrice2.split(',');
	for (i = 0; i < mrpPriceArray.length; i++) {
		var mrpPriceData = JSON.parse(mrpPriceArray[i]);
		if (sizeMatched != "") {
			
			if (mrpPriceData[sizeMatched] != undefined) {
				//console.log("mrp+"mrpPriceData[sizeMatched]+"minPrice"+minPriceValue);
				if(mrpPriceData[sizeMatched]<=minPriceValue){
					$("#priceEqual_"+product).html("");
					$("#priceEqual_"+product).html("&#8377;"+minPriceValue);
				}else if(mrpPriceData[sizeMatched]>minPriceValue){
					console.log("prices"+mrpPriceData[sizeMatched]);
					$("#mrpprice_"+product).html("");
					$("#mrpprice_"+product).html("&#8377;"+(mrpPriceData[sizeMatched])); 
					$("#price_"+product).html("");
					$("#price_"+product).html("&#8377;"+minPriceValue);
				}
				
			}
		} else {
			console.log("sizes matched for"+product+sizeMatched+mrpPriceData+mrpPriceData[minPriceSize]);
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

//finding the sizes of a product matched with the applied filters
function checkSizeCount(list, serpSizeList) {
	var count = 0;
	var matchedSize = "";
	var productSizes = list.replace("[", "");
	var finalProductSizeArray = productSizes.replace("]", "")
//	console.log("arrays" + list + finalProductSizeArray);
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

