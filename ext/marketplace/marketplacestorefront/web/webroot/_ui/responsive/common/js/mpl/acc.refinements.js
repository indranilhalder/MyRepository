var updatedsearchQuery = "";
var dummyForm ;
var lessBrands = [];
var facetAjaxUrl = '';
var productItemArray = [];
var res;
var filterValue = "";
var filterName = "";
var filterChecked = "";
var countBrand=1;
var countCustomPrice=0;	//TISPRDT-1645


ACC.refinements = {

	_autoload: [
		["bindMoreLessToggles", $(".js-facet-form").length != 0],
		["bindMoreStoresToggles", $(".js-facet-form").length != 0],
		["init", $(".js-facet-form").length != 0],
		["bindSearch", $(".js-facet-form").length != 0]
	],


	coords:{},
	storeSearchData:{},


	init:function(){
		navigator.geolocation.getCurrentPosition(
			function (position){
				ACC.refinements.coords = position.coords;
			},
			function (error)
			{
				console.log("An error occurred... The error code and message are: " + error.code + "/" + error.message);
			}
		);

	},


	bindSearch:function(){

		$(document).on("submit",'#user_location_form', function(e){
			e.preventDefault()
			var q = $(".js-shop-stores-facet .js-shop-store-search-input").val();
			 if(q.length>0){
				 ACC.refinements.getInitStoreData(q);				
			 }
		})


		$(document).on("click",'#findStoresNearMeAjax', function(e){
			e.preventDefault()
			ACC.refinements.getInitStoreData(null,ACC.refinements.coords.latitude,ACC.refinements.coords.longitude);
		})


	},


	getInitStoreData: function(q,latitude,longitude){
		$(".alert").remove();
		data ={
			"q":"" ,
			"page":"0"
		}

		if(q != null){
			data.q = q;
		}
		

		if(latitude != null){
			data.latitude = latitude;
		}

		if(longitude != null){
			data.longitude = longitude;
		}

		ACC.refinements.storeSearchData = data;
		ACC.refinements.getStoreData();
	},


	getStoreData: function(){
		url= $(".js-facet-form").data("url");
		$.ajax({
			url: url,
			data: ACC.refinements.storeSearchData,
			type: "get",
			success: function (response){
				window.location.reload();
			}
		});
	},
	
	bindMoreLessToggles: function (){

		$(document).on("click",".js-shop-stores-facet .js-facet-change-link",function(e){
			e.preventDefault();
			$(".js-shop-stores-facet .js-facet-container").hide();
			$(".js-shop-stores-facet .js-facet-form").show();
		})
// Fix For TISPRO-194(Including Loader)
		
		/* TPR-198 : AJAX Call in SERP and PDP START */
		
		var browserURL = window.location.href.split('?');		
		// AJAX for checkbox
		$(document).on("change",".js-product-facet .facet_desktop .js-facet-checkbox",function(){
			var staticHost=$('#staticHost').val();
			$("body").append("<div id='no-click' style='opacity:0.60; background:black; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
			$("body").append('<div class="loaderDiv" style="position: fixed; left: 50%;top: 50%;"><img src="'+staticHost+'/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>');
			
			var dataString = null;
			var nonEmptyDataString= null;
			
			// generating datastring and postAjaxURL
			$(this).parents("form").find('input[type="hidden"]').each(function(){
				if(dataString == null){
					dataString = $(this).attr('name')+"="+$(this).val();
				}
				else{
					dataString = dataString + ("&"+$(this).attr('name')+"="+$(this).val());
				}
				
				if($(this).val().length >0){
					if(nonEmptyDataString == null){
						nonEmptyDataString = $(this).attr('name')+"="+$(this).val();
					}
					else{
						nonEmptyDataString = nonEmptyDataString + ("&"+$(this).attr('name')+"="+$(this).val());
					}
				}
			})
			/*TPR-1283 CHANGES--Starts*/
			   if(browserURL[0].includes("/b")){
					var indx1=browserURL[0].indexOf("/b");
					var res = browserURL[0].substring(0, indx1);
					browserURL[0]=res;
					//window.location.href=browserURL[0];
				}
			/*TPR-1283 CHANGES--Ends*/
			//UF-254 later page push is not considered . Putting page 1 for default.
			var browserUrlLazy = appendPageNo(browserURL[0]);
			// generating postAjaxURL
			var pageURL = browserUrlLazy +'?'+nonEmptyDataString.replace(/%/g,"%25").replace(/ - /g,"+-+").replace(/:/g,"%3A");
			var requiredUrl="";
			var action = $(this).parents("form").attr('action');
			
			// generating request mapping URL
			if($("#isCategoryPage").val() == 'true'){
				if ($("input[name=customSku]").val()) {
					var collectionId = $("input[name=customSkuCollectionId]").val();
					requiredUrl = '/CustomSkuCollection/'+collectionId+'/getFacetData';
				}
				else {
					action = action.split('/c-');
					action = action[1].split('/');
					requiredUrl = "/c-"+action[0];
					requiredUrl += "/getFacetData";
				}
			} else {
				if(action.indexOf("/getFacetData") == -1){
					if(action.indexOf("offer") > -1 || action.indexOf("viewOnlineProducts") > -1 || action.indexOf('/s/') > -1) {
						requiredUrl = action.concat("/getFacetData");
					}
					else if ($("input[name=customSku]").val()) {
						var collectionId = $("input[name=customSkuCollectionId]").val();
						requiredUrl = '/CustomSkuCollection/'+collectionId+'/getFacetData';
					}
					else{
						//requiredUrl = action.concat("getFacetData");
						// INC_11277 start
						if ((/.*\/$/g).test(action)) {
							requiredUrl = action.concat("getFacetData");
						}
						else{
							requiredUrl = action.concat("/getFacetData");
						}
						// INC_11277 end
					}
				}
				else{
					requiredUrl = action;
				}
			}			

			
			// TPR-645 Start  INC_11511  fix--h3 tag done
			filterValue = $(this).parent().find('span.facet-text').text().trim();
			filterName = $(this).parents('li.facet.js-facet').find('div.facet-name.js-facet-name h3').text().trim();
			if($(this).attr('checked')){
				filterChecked = true;
				//onFilterRemoveAnalytics(filterName,filterValue);
			}
			else{
				filterChecked = false;
				//onFilterAddAnalytics(filterName,filterValue);
			}
			// TPR-645 End
			// AJAX call
			filterDataAjax(requiredUrl,encodeURI(dataString),pageURL);
		})
		
		// TPR-845
		$(document).on("change",".js-product-facet .js-facet-checkbox",function(){
			var filterMobileQuery = $(this).parents("form").find('input[name="q"]').val();
			dummyForm = $(this).parents("form");
			if(updatedsearchQuery==''){
				updatedsearchQuery=filterMobileQuery;
			}else{
				
				//var newFilter=createSearchQuery(filterMobileQuery);
				var ownVal = $(this).parents("form").find('input[name="facetValue"]').val();
				var ownIdentifier = $(this).parents("li").attr('class').replace('filter-',"");
				var newFilter = ':' + ownIdentifier + ':' + ownVal;
				//if(updatedsearchQuery.includes(newFilter))
				if(updatedsearchQuery.indexOf(newFilter) > -1)
				{
					updatedsearchQuery=updatedsearchQuery.replace(newFilter,"");
				}
				else{
					updatedsearchQuery+=newFilter;
				}
			}
		});
		
		$(document).on("change",".facet_mobile .js-facet-checkbox-price",function(){
			var filterMobileQuery = $(this).parents("form").find('input[name="q"]').val();
			//console.log("form query:"+filterMobileQuery)
			dummyForm = $(this).parents("form");
			if(updatedsearchQuery==''){
				updatedsearchQuery=filterMobileQuery;
			}else{
				//TISQAUATS-27 starts 
				//var newFilter=createSearchQuery(filterMobileQuery);
				var ownVal = $(this).parents("form").find('input[name="facetValue"]').val();
				//var ownIdentifier = $(this).parents("li").attr('class').replace('filter-',"");
				var ownIdentifier = $(this).parents("li").attr('class').replace(/^(\S*).*/, '$1').replace('filter-',"");
				var newFilter = ':' + ownIdentifier + ':' + ownVal;
				//TISQAUATS-27 ends 
				//if(updatedsearchQuery.includes(newFilter))
				if(updatedsearchQuery.indexOf(newFilter) > -1)
				{
					updatedsearchQuery=updatedsearchQuery.replace(newFilter,"");
				}
				else{
					updatedsearchQuery+=newFilter;
				}
			}
			
			//console.log("Mobile View : updatedsearchQuery : "+updatedsearchQuery);	
			// hiding
			loadMobilePriceRange();
			
		});
		
				
		// AJAX for Colourbutton and sizebuttons
		$(document).on("click",".js-product-facet .facet_desktop .js-facet-colourbutton , .js-product-facet .facet_desktop .js-facet-sizebutton",function(){
			
			$("body").append("<div id='no-click' style='opacity:0.60; background:black; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
			$("body").append('<div class="loaderDiv" style="position: fixed; left: 50%;top: 50%;"><img src="/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>');
			
			var dataString = null;
			var nonEmptyDataString= null;
			
			// generating datastring and postAjaxURL
			$(this).parents("form").find('input[type="hidden"]').each(function(){
				if(dataString == null){
					dataString = $(this).attr('name')+"="+$(this).val();
				}
				else{
					dataString = dataString + ("&"+$(this).attr('name')+"="+$(this).val());
				}
				
				
				if($(this).val().length >0){
					if(nonEmptyDataString == null){
						nonEmptyDataString = $(this).attr('name')+"="+$(this).val();
					}
					else{
						nonEmptyDataString = nonEmptyDataString + ("&"+$(this).attr('name')+"="+$(this).val());
					}
				}
			})
			var browserUrlLazy = appendPageNo(browserURL[0]);
			// generating postAjaxURL
			var pageURL = browserUrlLazy +'?'+nonEmptyDataString.replace(/%/g,"%25").replace(/ - /g,"+-+").replace(/:/g,"%3A");
			var requiredUrl="";
			var action = $(this).parents("form").attr('action');
			
			// generating request mapping URL
			if($("#isCategoryPage").val() == 'true'){
				if ($("input[name=customSku]").val()) {
					var collectionId = $("input[name=customSkuCollectionId]").val();
					requiredUrl = '/CustomSkuCollection/'+collectionId+'/getFacetData';
				}
				else {
					action = action.split('/c-');
					action = action[1].split('/');
					requiredUrl = "/c-"+action[0];
					requiredUrl += "/getFacetData";
				}
			} else {
				if(action.indexOf("/getFacetData") == -1){
					if(action.indexOf("offer") > -1 || action.indexOf("viewOnlineProducts") > -1 || action.indexOf('/s/') > -1){
						requiredUrl = action.concat("/getFacetData");
					}
					else if ($("input[name=customSku]").val()) {
						var collectionId = $("input[name=customSkuCollectionId]").val();
						requiredUrl = '/CustomSkuCollection/'+collectionId+'/getFacetData';
					}
					else{
						//requiredUrl = action.concat("getFacetData");
						// INC_11277 start
						if ((/.*\/$/g).test(action)) {
							requiredUrl = action.concat("getFacetData");
						}
						else{
							requiredUrl = action.concat("/getFacetData");
						}
						 // INC_11277 end
					}
				}
				else{
					requiredUrl = action;
				}
			}

			// TPR-645 Start INC_11511  fix--h3 tag done
			var filterValue = '';
			var filterName = $(this).parents('li.facet.js-facet').find('div.facet-name.js-facet-name h3').text().trim();
			if($(this).attr('class').indexOf('size') > -1){
				filterValue = $(this).attr('value');
			}
			else{
				// console.log($(this).attr('class').text());
				filterValue = $(this).parent().find('span > span').text();
			}

			if($(this).attr('checked')){
				filterChecked = true;
				//onFilterRemoveAnalytics(filterName,filterValue);
			}
			else{
				filterChecked = false;
				//onFilterAddAnalytics(filterName,filterValue);
			}
			// TPR-645 End
			// AJAX call
			filterDataAjax(requiredUrl,encodeURI(dataString),pageURL);

		});
		
		// TPR-845
		$(document).on("click",".js-product-facet .facet_mobile .js-facet-colourbutton , .js-product-facet .facet_mobile .js-facet-sizebutton",function(){
			var filterMobileQuery = $(this).parents("form").find('input[name="q"]').val();
			dummyForm = $(this).parents("form");
			if(updatedsearchQuery==''){
				updatedsearchQuery=filterMobileQuery;
				
			}else{
				//var newFilter=createSearchQuery(filterMobileQuery);
				//TISQAUATS-27 starts 
				var ownVal = $(this).parents("form").find('input[name="facetValue"]').val();
				var ownIdentifier = $(this).parents("li").attr('class').replace(/^(\S*).*/, '$1').replace('filter-',"");
				var newFilter = ':' + ownIdentifier + ':' + ownVal;
				//TISQAUATS-27 ends 
				//if(updatedsearchQuery.includes(newFilter))
				if(updatedsearchQuery.indexOf(newFilter) > -1)
				{
					updatedsearchQuery=updatedsearchQuery.replace(newFilter,"");
				}
				else{
					updatedsearchQuery+=newFilter;
				}			
			}
			//console.log("updatedsearchQuery : "+updatedsearchQuery);
		});
		
		// AJAX for removal of filters
		$(document).on("click",".facet-list.filter-opt .remove_filter , .any_price",function(e){
			$("body").append("<div id='no-click' style='opacity:0.60; background:black; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
			$("body").append('<div class="loaderDiv"  style="position: fixed; left: 50%;top: 50%;"><img src="/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>');
			
			// generating postAjaxURL
			var pageURL = $(this).parent().attr('href');
			var action = pageURL.split('?');
			
			var dataString;
			// generating datastring
			if($(action).length > 1)
			{
				dataString = action[1].replace(/%3A/g,':');
			}
			else{
				dataString = action[0].replace(/%3A/g,':');
			}
			
			var requiredUrl="";
			
			// generating request mapping URL
			if($("#isCategoryPage").val() == 'true'){
				if ($("input[name=customSku]").val()) {
					var collectionId = $("input[name=customSkuCollectionId]").val();
					requiredUrl = '/CustomSkuCollection/'+collectionId+'/getFacetData';
				}
				else {
					action = action[0].split('/c-');
					action = action[1].split('/');
					requiredUrl = "/c-"+action[0];
					requiredUrl += "/getFacetData";
				}
			}			
			else {
				requiredUrl = action[0].concat("/getFacetData");
			}

			
			//Utag Fire on remove filter or any price start
			if($(this).attr('class') == "remove_filter"){
				filterName=$(this).parents('li').children().eq(0).attr('class');
				filterValue=$(this).parents('li').children().eq(1).attr('value');
				filterChecked = true;
				//onFilterRemoveAnalytics(filterName,filterValue);
			}
			else{
				filterName="price"
				filterValue=$('.facet-list.filter-opt .price').siblings('.applied-color').val();
				filterChecked = true;
				//onFilterRemoveAnalytics(filterName,filterValue);
			}
			//TISSPTEN-130 starts
			if ($("input[name=customSku]").val()) {
				pageURL =  window.location.href;
				if (pageURL.indexOf("?") > -1) {
					pageURL = pageURL.split("?")[0];
					pageURL = pageURL.replace(/page-[0-9]+/, 'page-1');
				}
				if (action[1] != "") {
					pageURL = pageURL + '?' + action[1]; 
				}
			}
			//TISSPTEN-130 ends
			// AJAX call
			filterDataAjax(requiredUrl,dataString,pageURL);
			
			//utag firing on remove filter or any price end
			return false;
		});
		
		/* TPR-198 : AJAX Call in SERP and PDP END */
		
		$(document).on("change",".facet_mobile ul.facet-list.js-facet-top-values.active.selectedMobile input[type=checkbox]",function(){
			//$('ul.facet-list.js-facet-top-values.active').first().find('input[type=checkbox]').change(function(){
				var brandNode = $(this).parent().find('span.facet-text').text().trim();
				//alert(brandNode);
				if($(this).is(':checked')){
					$('.facet_mobile ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden.selectedMobile').first().find("span.facet-text:contains('"+brandNode+"')").closest('label').find('input[type=checkbox]').prop('checked',true);	
				}else{
					$('.facet_mobile ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden.selectedMobile').first().find("span.facet-text:contains('"+brandNode+"')").closest('label').find('input[type=checkbox]').prop('checked',false);
				}
		});
			
		$(document).on("change",".facet_mobile ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden.selectedMobile input[type=checkbox]",function(){
			//$('ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').first().find('input[type=checkbox]').change(function(){
				var brandNode = $(this).parent().find('span.facet-text').text().trim();
				if($(this).is(':checked')){
					$('.facet_mobile ul.facet-list.js-facet-top-values.active.selectedMobile').first().find("span.facet-text:contains('"+brandNode+"')").closest('label').find('input[type=checkbox]').prop('checked',true);	
				}else{
					$('.facet_mobile ul.facet-list.js-facet-top-values.active.selectedMobile').first().find("span.facet-text:contains('"+brandNode+"')").closest('label').find('input[type=checkbox]').prop('checked',false);
				}
		});
		
		
		
		$(document).on("click",".js-product-facet .js-more-facet-values-link",function(e){
			e.preventDefault();
			if($('.brandSearchTxt').val()=="")
			{
				$(this).parents(".js-facet").find(".js-facet-top-values").hide();
				$(this).parents(".js-facet").find(".js-facet-list-hidden").show();
				
				$(this).parents(".js-facet").find(".js-facet-list-hidden span.facet-label").show();
			}
			else
			{
				$('.marked').css("display","block");				
			}
			$(this).parents(".js-facet").find(".js-more-facet-values").hide();
			$(this).parents(".js-facet").find(".js-less-facet-values").show();
		});
		
			$(document).on("click",".js-less-facet-values-link",function(e){
			e.preventDefault();
			//var brandFacet = [];
			if($('.brandSearchTxt').val()=="")
			{
				$(this).parents(".js-facet").find(".js-facet-top-values").show();
				$(this).parents(".js-facet").find(".js-facet-list-hidden").hide();
			}
			else
			{
				$('.marked').css("display","none");				
			}
			$(this).parents(".js-facet").find(".js-more-facet-values").show();
			$(this).parents(".js-facet").find(".js-less-facet-values").hide();
		});

		// AJAX for removal of filters
		$(document).on("click",".filter-apply",function(e){
			// TPR-1507
			var filterCount=0;
			var url= window.location.href;	//INC144316162
			$(".facet_mobile .facet.js-facet").each(function(){
				//INC144316162 fix start
				if (url.indexOf("isFacet") >= 0)
				{
					filterCount++;
				}
				//INC144316162 fix ends
				filterCount+=$(this).find(".facet-list.js-facet-list li").find("input[type=checkbox]:checked").length;
				filterCount+=$(".facet_mobile .filter-colour.selected-colour").length;
				//TISTNL-894 | Colourfamily mobile view
				filterCount+=$(".facet_mobile .filter-colorfamilytrlg.selected-colour").length;
				filterCount+=$(".facet_mobile .filter-colorfamilytrlg.selected-multi-colour").length;
				//Dial Colour Watches mobile view
				filterCount+=$(".facet_mobile .filter-dialColourWatches.selected-colour").length;
				filterCount+=$(".facet_mobile .filter-dialColourWatches.selected-multi-colour").length;
				
				//TISQAUATS-12 starts 
				filterCount+=$(".facet_mobile .filter-colour.selected-multi-colour").length;
				
				
				//TISQAUATS-12 ends
				filterCount+=$(".facet_mobile .filter-size.selected-size").length;
			});
			//TISQAUATS-27 starts
			//INC144318011
			if ($('#customMinPriceMob').val() || $('#customMaxPriceMob').val()) {
				filterCount++;
			}
			
			//TISQAUATS-27 ends
			if(filterCount<=0){
				return false;
			}
			// TPR-1507 Ends
			else{
				$("body").append("<div id='no-click' style='opacity:0.60; background:black; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
				$("body").append('<div class="loaderDiv" style="position: fixed; left: 50%;top: 50%;"><img src="/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>');
				// generating postAjaxURL
				var browserURL = window.location.href.split('?');
				var dataString = null;
				var nonEmptyDataString= null;
				
				//TISQAUATS-27 starts 
				//INC144318011
				if ($('#customMinPriceMob').val() || $('#customMaxPriceMob').val()) {
					//TISPRDT-1645 starts
					if(countCustomPrice==0){
						countCustomPrice=1;
						$("#applyCustomPriceFilterMob").click();
					}
					//TISPRDT-1645 ends
					//var minPriceSearchTxt = $('#customMinPriceMob').val();
					//var maxPriceSearchTxt = $('#customMaxPriceMob').val();
					
					//INC144318011 start
					var minPriceSearchTxt = ($('#customMinPriceMob').val() == null || $('#customMinPriceMob').val() == "") ? 0 : $('#customMinPriceMob').val() ;
		            		var maxPriceSearchTxt = ($('#customMaxPriceMob').val() == null || $('#customMaxPriceMob').val() == "") ? 99999999 : $('#customMaxPriceMob').val() ;
		            		//INC144318011 end
					var price = "₹" + minPriceSearchTxt + "-" + "₹" + maxPriceSearchTxt;
					//$('#facetValue').val(facetValue);
					if (/:price:(.*)/.test(updatedsearchQuery)) {
						if (/:price:(.*):/.test(updatedsearchQuery)) {
							updatedsearchQuery = updatedsearchQuery.replace(/:price:(.*?):/,":price:" + price + ':');
						}
						else {
							updatedsearchQuery = updatedsearchQuery.replace(/:price:(.*)/,":price:" + price);
						}
					}
					else {
						updatedsearchQuery = updatedsearchQuery + ":price:" + price;
					}
				}
				//TISQAUATS-27 ends
				
				// generating datastring and postAjaxURL
				dummyForm.find('input[type="hidden"]').each(function(){
					if(dataString == null){
						dataString = $(this).attr('name')+"="+$(this).val();
					}
					else{
						if($(this).attr('name') == 'q'){
							dataString = dataString + ("&"+$(this).attr('name')+"="+updatedsearchQuery);
						}else{
							dataString = dataString + ("&"+$(this).attr('name')+"="+$(this).val());
						}
						
					}
					//console.log("dataString : "+dataString);
					
					if($(this).val().length >0){
						if(nonEmptyDataString == null){
							nonEmptyDataString = $(this).attr('name')+"="+$(this).val();
						}
						else{
							//TISQAUATS-27 starts
							//nonEmptyDataString = nonEmptyDataString + ("&"+$(this).attr('name')+"="+$(this).val());
							if($(this).attr('name') == 'q'){
								nonEmptyDataString = nonEmptyDataString + ("&"+$(this).attr('name')+"="+updatedsearchQuery);
							}else{
								nonEmptyDataString = nonEmptyDataString + ("&"+$(this).attr('name')+"="+$(this).val());
							}
							//TISQAUATS-27 ends
						}
					}
				})
				var browserUrlLazy = appendPageNo(browserURL[0]);
				// generating postAjaxURL
				var pageURL = browserUrlLazy +'?'+nonEmptyDataString.replace(/%/g,"%25").replace(/ - /g,"+-+").replace(/:/g,"%3A");		//SDI-2191 fix
				var requiredUrl="";
				var action = dummyForm.attr('action');
				
				// generating request mapping URL
				if($("#isCategoryPage").val() == 'true'){
					if ($("input[name=customSku]").val()) {
						var collectionId = $("input[name=customSkuCollectionId]").val();
						requiredUrl = '/CustomSkuCollection/'+collectionId+'/getFacetData';
					}
					else {
						action = action.split('/c-');
						action = action[1].split('/');
						requiredUrl = "/c-"+action[0];
						requiredUrl += "/getFacetData";
					}
				} else {
					if(action.indexOf("/getFacetData") == -1){
						if(action.indexOf("offer") > -1 || action.indexOf("viewOnlineProducts") > -1 || action.indexOf('/s/') > -1){
							requiredUrl = action.concat("/getFacetData");
						}
						else if ($("input[name=customSku]").val()) {
							var collectionId = $("input[name=customSkuCollectionId]").val();
							requiredUrl = '/CustomSkuCollection/'+collectionId+'/getFacetData';
						}
						else{
							//requiredUrl = action.concat("getFacetData");
							// INC_11277 start
							if ((/.*\/$/g).test(action)) {
								requiredUrl = action.concat("getFacetData");
							}
							else{
								requiredUrl = action.concat("/getFacetData");
							}
							 // INC_11277 end
						}
					}
					else{
						requiredUrl = action;
					}
				}
				// AJAX call
				// console.log("Controle Came");
				filterValue = "";
				filterName = "";
				filterDataAjax(requiredUrl,encodeURI(dataString),pageURL);
				return false;
			}	
		})

		// TPR-845
		$(document).on("click"," .filter-clear ",function(e){
			// TPR-1536
			//TISQAUATS-12 starts
			/*var filterCount=0;
			$(".facet_mobile .facet.js-facet").each(function(){
				filterCount+=$(this).find(".facet-list.js-facet-list li").find("input[type=checkbox]:checked").length;
				filterCount+=$(".facet_mobile .filter-colour.selected-colour").length;
				filterCount+=$(".facet_mobile .filter-size.selected-size").length;
			})
			if(filterCount<=0){
				return false;
			}
			// TPR-1536 Ends
			
			else{*/
			//TISQAUATS-12 ends 
				$("body").append("<div id='no-click' style='opacity:0.60; background:black; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
				$("body").append('<div class="loaderDiv" style="position: fixed; left: 50%;top: 50%;"><img src="/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>');

				var browserURL = window.location.href;
				var pageURL;
				if(browserURL.indexOf("%3A")!=-1){
					pageURL = browserURL.substring(0,browserURL.indexOf("%3A"));
				}
				else{
					pageURL=browserURL;
				}
				
				// redirecting to pageURL on page reload
				window.location.href =pageURL;
				return false;
			//TISQAUATS-12 starts 
			//}
			//TISQAUATS-12 ends
			})

		$(document).off('click', '.js-facet-colourbutton').on('click', '.js-facet-colourbutton', function() { 
			//TISQAUATS-12 starts
			//$(this).parents(".filter-colour").toggleClass("selected-colour");
			var colour_name = $(this).parent().find("input[name=facetValue]").val().split("_", 1);
			if(colour_name == "Multi"){
				$(this).parents(".filter-colour, .filter-colorfamilytrlg").toggleClass("selected-multi-colour");
			}
			else{
				$(this).parents(".filter-colour, .filter-colorfamilytrlg").toggleClass("selected-colour");
			}
			//TISQAUATS-12 ends
			//TISQAUATS-12 starts
			/*var li_index = $('.facet_mobile .facet.js-facet.Colour .facet-list.js-facet-top-values.active li').index($(this).parents(".filter-colour"));
			$(".facet_mobile .facet.js-facet.Colour .facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden li").eq(li_index).toggleClass("selected-colour");
			var spanCountMoreColor = $('.facet_mobile .facet.js-facet.Colour ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').find("li.selected-colour").length;*/
			if ($('.facet_mobile .facet.js-facet.Colour .facet-list.js-facet-top-values.active li').length) {
				var colourName = $(this).parent().find("input[name=facetValue]").val();
				if(colour_name == "Multi"){
					$(this).closest('ul').next().find('li input[value="'+colourName+'"]').parents(".filter-colour, .filter-colorfamilytrlg").toggleClass("selected-multi-colour");
				}
				else{
					$(this).closest('ul').next().find('li input[value="'+colourName+'"]').parents(".filter-colour, .filter-colorfamilytrlg").toggleClass("selected-colour");
				}
				var spanCountMoreColor = $('.facet_mobile .facet.js-facet.Colour ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').find("li.selected-colour").length;
				spanCountMoreColor = spanCountMoreColor + $('.facet_mobile .facet.js-facet.Colour ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').find("li.selected-multi-colour").length;
			}
			else {
				var spanCountMoreColor = $('.facet_mobile .facet.js-facet.Colour ul.facet-list.js-facet-list').find("li.selected-colour").length;
				spanCountMoreColor = spanCountMoreColor + $('.facet_mobile .facet.js-facet.Colour ul.facet-list.js-facet-list').find("li.selected-multi-colour").length;
			}
			//TISQAUATS-12 ends
			if(spanCountMoreColor){
				if(spanCountMoreColor)
				{
					$(this).parents(".facet.js-facet").find(".category-icons").removeClass("blank");
					$(this).parents(".facet.js-facet").find(".category-icons span").text(spanCountMoreColor);
				}	
				else
				{
					$(this).parents(".facet.js-facet").find(".category-icons").addClass("blank");
				}
			}
			else {
			// Fixing error of facet ends
				var spanCount=$(".facet_mobile .filter-colour.selected-colour").length;
				if(spanCount>0)
				{
					$(this).parents(".facet.js-facet").find(".category-icons").removeClass("blank");
					$(this).parents(".facet.js-facet").find(".category-icons span").text(spanCount);
				}	
				else
				{
					$(this).parents(".facet.js-facet").find(".category-icons").addClass("blank");
				}
			// Fixing error of facet starts
			}
			// Fixing error of facet ends	
		});

		$(document).off('click', '.js-facet-sizebutton').on('click', '.js-facet-sizebutton', function() { 
			$(this).parents(".filter-size").toggleClass("selected-size");
			var spanCount=$(".facet_mobile .filter-size.selected-size").length;
			if(spanCount>0)
			{
				$(this).parents(".facet.js-facet").find(".category-icons").removeClass("blank");
				$(this).parents(".facet.js-facet").find(".category-icons span").text(spanCount);
			}
			else
			{
				$(this).parents(".facet.js-facet").find(".category-icons").addClass("blank");
			}
		});

		
		$(document).off('change', '.facet_mobile .facet.js-facet').on('change', '.facet_mobile .facet.js-facet', function() { 
			$(".facet_mobile .facet.js-facet").not(".Colour,.Size").each(function(){
				var spanCountMore = $(this).find('ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').find("input[type=checkbox]:checked").length;
				if(spanCountMore){
					//var spanCount=$(this).find(".facet-list li").find("input[type=checkbox]:checked").length;
					if(spanCountMore>0)
					{
						//TISJEW-3501
						//$('li.facet.js-facet.Brand').find('span.category-icons').removeClass("blank");
						//$('li.facet.js-facet.Brand').find('span.category-icons span').text(spanCountMore);
						$(this).find("span.category-icons").removeClass("blank");
						$(this).find("span.category-icons span").text(spanCountMore);
					}
					else
					{
						//TISJEW-3501
						$(this).find("span.category-icons").addClass("blank");
						//$('li.facet.js-facet.Brand').find('span.category-icons').addClass("blank");
					}
				}else{
					var spanCount=$(this).find(".facet-list li").find("input[type=checkbox]:checked").length;
					if(spanCount>0)
					{
						$(this).find(".category-icons").removeClass("blank");
						$(this).find(".category-icons span").text(spanCount);
					}
					else
					{
						$(this).find(".category-icons").addClass("blank");
					}
				}
				
			});
		});
		/*$(document).on("click",".pagination.mobile li a",function(e){
				if ($("input[name=customSku]").val()) {			
					// for pagination ajax call
					e.preventDefault();
					var requiredUrl = $(this).attr('href');
					var dataString = '';
					$.ajax({
						contentType : "application/json; charset=utf-8",
						url : requiredUrl,
						data : dataString,
						success : function(response) {
							// console.log(response);
							// putting AJAX respons to view
							$('#facetSearchAjaxData .right-block, #facetSearchAjaxData .bottom-pagination, #facetSearchAjaxData .facet-list.filter-opt').remove();
							$('#facetSearchAjaxData .left-block').after(response);
						},
						error : function(xhr, status, error) {				
							console.log("Error >>>>>> " + error);
						},
						complete: function() {
							// AJAX changes for custom price filter
							
						}
					});
					return false;
				}
		});*/
		

		$(document).on("click",".priceBucketExpand, .any_price_mobile",function(e){
			// generating postAjaxURL
			//$(this).parents("a").attr("href","#");
			removeMobilePriceRange();
			return false;
		});

		//TISQAUATS-27 starts 
		//$(document).on("click","#applyCustomPriceFilter",function(e){
		$(document).on("click","#applyCustomPriceFilterMob",function(e){
		//TISQAUATS-27 ends
			//TISPRDT-1645 starts
			var currentQryParam = $('.currentPriceQueryParams').val();
			//alert("CQP :"+currentQryParam);
			var filterMobileQuery = $(this).parents("form").find('input[name="q"]').val();
			dummyForm = $(this).parents("form");
			if(updatedsearchQuery=='' && filterMobileQuery!=''){
				
				updatedsearchQuery=filterMobileQuery;
			}else if(filterMobileQuery=='' && updatedsearchQuery==''){
				updatedsearchQuery=currentQryParam;
			}
			else{
				var ownVal = $(this).parents("form").find('input[name="facetValue"]').val();
				var ownIdentifier = $(this).parents("li").attr('class').replace(/^(\S*).*/, '$1').replace('filter-',"");
				var newFilter = ':' + ownIdentifier + ':' + ownVal;
				if(updatedsearchQuery.indexOf(newFilter) > -1)
				{
					updatedsearchQuery=updatedsearchQuery.replace(newFilter,"");
				}
				else{
					updatedsearchQuery+=newFilter;
				}
			}
			if (countCustomPrice==1){
				return false;
			}
			//alert("USQ :"+updatedsearchQuery);
			countCustomPrice=0;
			//dummyForm = $(this).parents("form");
			//TISPRDT-1645 ends
			
			// generating postAjaxURL
			$(".filter-apply").click();
		});
		
	},
	
	bindMoreStoresToggles: function ()
	{
		$(document).on("click",".js-shop-stores-facet .js-more-stores-facet-values",function(e){
			e.preventDefault();
			$(".js-shop-stores-facet ul.js-facet-list li.hidden").slice(0, 5).removeClass('hidden').first().find('.js-facet-checkbox').focus();
			if($(".js-shop-stores-facet ul.js-facet-list li.hidden").length==0){
				$(".js-shop-stores-facet .js-more-stores-facet-values").hide()
			}
		})
		
	}
};

// function implements AJAX : TPR-198
function filterDataAjax(requiredUrl,dataString,pageURL){
	facetAjaxUrl = pageURL;
	//console.log(requiredUrl);
	//console.log(pageURL);
	var pathName = window.location.pathname;
    var query = window.location.search;
	/*TPR-1283 CHANGES--Starts*/
	if($("#flagVal").val()=='true'){
		var category=$("div.list_title > h1").text($("#catName").val()).text();
		$("div.list_title > h1").text(category);
	}
	/*TPR-1283 CHANGES--Ends*/
	//INC144316143
	if ($('#pageType').val() == 'productsearch' || $('#pageType').val() == 'category') {
		   window.localStorage.setItem('lastUrlpathName',encodeURI(pathName));
		   window.localStorage.setItem('lastUrlquery',encodeURI(query));
		   $("img.lazy").lazyload();
	 }
	
	// INC144315462 and INC144315104 
	
	//TISSQAUAT-3418 starts
	// Added For INC144315104 and INC144315462

	if ($("input[name=customSku]").val()) {
		//dataString = dataString + "&sort=" + $("select[name=sort]").val() + "&pageSize=" + $("select[name=pageSize]").val(); 
		dataString = dataString + "&pageSize=24"; 
	}
	if($( "span.sort[style*='color']" ).length == 1){
 		var sortData = $( "span.sort[style*='color']" ).attr('data-name');
 		dataString = dataString + getSortCode(sortData); 
  	}
	//TISSQAUAT-3418 ends
	
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		success : function(response) {
			// console.log(response);
			// putting AJAX respons to view
			if($("#isCategoryPage").val() == 'true' && !$("input[name=customSku]").val()){
				//$("#productGrid").html(response);
				lazyPaginationFacet(response);

			}		
			else{
				
				if(requiredUrl.indexOf("offer") > -1 || requiredUrl.indexOf("viewOnlineProducts") > -1 || requiredUrl.indexOf("/s/") > -1){
					//$("#productGrid").html(response);
					lazyPaginationFacet(response);

				}
				else{
					$("#facetSearchAjaxData").html(response);
					lazyPaginationFacet(response);
				}
			}
			
			$("#no-click").remove();
			$(".loaderDiv").remove();
			//UF-15
			if ($(".facet-list.filter-opt").children().length){
				$("body.page-productGrid .product-listing.product-grid.lazy-grid, body.page-productGrid .product-listing.product-grid.lazy-grid-facet, body.page-productGrid .product-listing.product-grid.lazy-grid-normal").css("padding-top","15px");  //INC144315068
				$("body.page-productGrid .facet-list.filter-opt").css("padding-top","65px");
				//TISSQAUAT-3418 starts
				var filter_height = $(".facet-list.filter-opt").height() - 8;   /* PRDI-69 */
				$("body.page-productGrid .listing.wrapper .left-block").css("margin-top",filter_height + "px");
				//TISSQAUAT-3418 ends

				/* UF-253 start */
				if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
				var sort_height ="-" + $(".facet-list.filter-opt").outerHeight() + "px";
				$("body.page-productGrid .listing.wrapper .right-block .listing-menu").css("margin-top",sort_height);
				}
				else{
					var sort_height =$(".facet-list.filter-opt").outerHeight() - 12 + "px";
					$("body.page-productGrid .listing.wrapper .right-block .listing-menu").css("margin-top",sort_height);	
				}
				/* UF-253 end */
			}
			else{
				//TISSQAUAT-3418 starts
				//$("body.page-productGrid .product-listing.product-grid").css("margin-top","60px");  //INC144315068

				$("body.page-productGrid .listing.wrapper .left-block").css("margin-top","65px"); /* PRDI-69 */

				//TISSQAUAT-3418 ends

			}
			// Keeps expansion-closure state of facets
			$(".facet-name.js-facet-name h3").each(function(){
				if($(this).hasClass("true")){
					
					if(sessionStorage.getItem($(this).text()) == "true" || sessionStorage.getItem($(this).text()) == null){
						$(this).addClass("active");
						$(this).parent().siblings('.facet-values.js-facet-values.js-facet-form').addClass("active");
				    	$(this).siblings('.brandSelectAllMain').addClass("active");
				    	$(this).parent().siblings('#searchPageDeptHierTreeForm').find("#searchPageDeptHierTree").addClass("active");
				    	$(this).parent().siblings('#categoryPageDeptHierTreeForm').find("#categoryPageDeptHierTree").addClass("active");
					}
				}
			 });
			
			if(sessionStorage.getItem($('ul.product-facet > .js-facet-name > h3').text()) == "false" && null != sessionStorage.getItem($('ul.product-facet > .js-facet-name > h3').text())) {
				$('ul.product-facet > .js-facet-name > h3').removeClass('active');
				$('#searchPageDeptHierTreeForm #searchPageDeptHierTree').hide(100);
		    	$("#categoryPageDeptHierTreeForm #categoryPageDeptHierTree").hide(100);
			}
			
			/*
			 * var filter_height=$(".facet-list.filter-opt").height() + 55;
			 * $(".listing.wrapper
			 * .left-block").css("margin-top",filter_height+"px");
			 */
			
			// TPR - 565
			if (!$("input[name=customSku]").val()) {
			// Scroll up to the top
				$("body,html").animate({scrollTop:0},500);
				
				// Re-write URL after ajax
				window.history.replaceState(response,"",pageURL);
				//INC144316143
				var pathName = window.location.pathname;
			    var query = window.location.search;
				
				if ($('#pageType').val() == 'productsearch' || $('#pageType').val() == 'category') {
				       window.localStorage.setItem('lastUrlpathName',encodeURI(pathName));
					   window.localStorage.setItem('lastUrlquery',encodeURI(query));
				 }
			}
			//TISSPTEN-130 starts
			else {
				$("body,html").animate({scrollTop:$('#productGrid').offset().top - 200},500);
				pageURL = window.location.pathname;
				pageURL = pageURL.replace(/page-[0-9]+/, 'page-1');
				if (facetAjaxUrl.indexOf("?") > -1) {
					var queryArr = facetAjaxUrl.split("?");
					if (queryArr[1] != "") {
						pageURL = pageURL + '?' + queryArr[1];
					}
					if($( "span.sort[style*='color']" ).length == 1){
				 		var sortData = $( "span.sort[style*='color']" ).attr('data-name');
				 		pageURL = pageURL + getSortCode(sortData); 
				  	}
				}

				window.history.replaceState(response,"",pageURL);
			}
			//TISSPTEN-130 ends
			// TPR-158 and TPR-413 starts here
			
			$("#displayAll").show();
			$("#clickToMore").hide();
			donotShowAll();
			
			$("#displayAll").on("click",function(e){
				showAll();		
				$("#displayAll").hide();
				$("#clickToMore").show();
			});
			
			$("#clickToMore").click(function(e){
				donotShowAll();		
				$("#displayAll").show();
				$("#clickToMore").hide();
				});	
			// TPR-158 and TPR-413 ends here
			//TPR-4720 first 5 product display
			if($('#pageType').val() == "productsearch"){
				populateFirstFiveProductsSerp();
				dtmSearchTags();
			}
			
			if($('#pageType').val() == "category" || $('#pageType').val() == "electronics"){
				populateFirstFiveProductsPlp();
				dtmSearchTags();
			}
		},
		error : function(xhr, status, error) {
			$('#wrongPin,#unsevisablePin,#emptyPin')
					.hide();
			$('#unableprocessPin').show();
			console.log("Error >>>>>> "+error);

		},
		complete: function() {
			// AJAX changes for custom price filter
			loadPriceRange();
			if(filterName != "" && filterValue != ""){
				if(filterChecked == true){
					onFilterRemoveAnalytics(filterName,filterValue);
				}
				else{
					onFilterAddAnalytics(filterName,filterValue);
				}
			}
		}
	});
	


}
//TISSQAUAT-3418 starts
//Added For INC144315104 and INC144315462
function getSortCode(item){
 	var code = '';
 	switch (item) {
 	case 'relevance':
 		code = '&sort=relevance';
 		break;
 	case 'new':
 		code = '&sort=isProductNew';
 		break;
 	case 'discount':
 		code = '&sort=isDiscountedPrice';
 		break;
 	case 'low':
 		code = '&sort=price-asc';
 		break;
 	case 'high':
 		code = '&sort=price-desc';
 		break;
 	default:
 		break;
 	}
 	return code;
 }
//TISSQAUAT-3418 ends

// INC144315462 and INC144315104  
function getSortCode(item){
	  	var code = '';
	  	switch (item) {
	  	case 'relevance':
	  		code = '&sort=relevance';
	  		break;
	  	case 'new':
	  		//TISSPTEN-125 starts
			if ($("input[name=customSku]").length) {
				code = '&sort=new';
			}
			else {
				code = '&sort=isProductNew';
			}
			//TISSPTEN-125 ends
	  		break;
	  	case 'discount':
	  		code = '&sort=isDiscountedPrice';
	  		break;
	  	case 'low':
	  		code = '&sort=price-asc';
	  		break;
	  	case 'high':
	  		code = '&sort=price-desc';
	  		break;
	  	default:
	  		break;
	  	}
	  	return code;
	  }
/*
 * $("#paginationForm .pagination.mobile li a").click(function(e){
 * 
 * e.prevent alert($(this).attr('href')); });
 */

// TPR-845
function createSearchQuery(filterMobileQuery){
	var queryString='';
	var splited=[];
	if(filterMobileQuery!=undefined){
		splited=filterMobileQuery.split(":");
	}
	for (k = 0; k < splited.length; k++) {
		if(splited.length-3<k){
			queryString+=':'+splited[k];
		}
	}
	return queryString;
}



//TPR-645,TPR-4704,TPR-4719

function onFilterAddAnalytics(filterName,filterValue){
	var filter_type = (filterName).toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	var filter_value = (filterValue).toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	// TPR-6287 | filter tacking
	if (typeof _satellite != "undefined") {
		_satellite.track('filter_temp');
    }
	if(typeof digitalData.filter != "undefined"){
		if(typeof digitalData.filter.temp != "undefined"){
			digitalData.filter.temp.type = filter_type;
			digitalData.filter.temp.value = filter_value;
		}
		else{
			digitalData.filter.temp = {
				type : filter_type,
				value : filter_value
			}
		}
	}
	else{
		digitalData.filter = {
			temp :  {
				type : filter_type,
				value : filter_value
			}
     	}
	}
	
	
	utag.link({
		link_text: 'search_filter_applied' ,
		event_type : 'search_filter_applied',
		"filter_type" : filter_type,
		"filter_value" : filter_value
	});
}

function onFilterRemoveAnalytics(filterName,filterValue){
	var filter_type = (filterName).toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	var filter_value = (filterValue).toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	
	utag.link({
		link_text: 'search_filter_removed' ,
		event_type : 'search_filter_removed',
		"filter_type" : filter_type,
		"filter_value" : filter_value
	});
}


function loadMobilePriceRange(){
	var q = updatedsearchQuery;
	var priceRange = '';
	var pvStr = ':price:';	
	if (q.indexOf(pvStr) > -1) {		
		priceRange = q.substring(q.indexOf(pvStr) + pvStr.length);
		if (priceRange.indexOf(':') > -1) {
			priceRange = priceRange.substring(0, priceRange.indexOf(':'));
		}		
		var prices = splitPrice(priceRange);
		console.log("priceRange"+priceRange+"prices[0]"+prices[0]+"prices[1]" +prices[1]);
		$('.minPriceSearchTxt').val(prices[0]);
		$('.maxPriceSearchTxt').val(prices[1]);		
		/* $('li.price').find('div.facet-name').hide(); */
		$('li.price').find('div.facet-values .facet-list.js-facet-list').hide();
		$('.priceBucketExpand').show();
	}
}

function removeMobilePriceRange(){
	var q = updatedsearchQuery;
	var priceRange = '';
	var pvStr = ':price:';	
	console.log("removing mobile"+updatedsearchQuery);
	$('.minPriceSearchTxt').val('');  
	$('.maxPriceSearchTxt').val('');  
	$('li.price').find('div.facet-values .facet-list.js-facet-list').show();
	$('.priceBucketExpand').hide();
	$(".js-facet-checkbox-price").each(function(){
		$(this).attr("checked",false);
	});
	
	//TISQAUATS-27 starts
	if (/:price:(.*):/.test(updatedsearchQuery)) {
		updatedsearchQuery=updatedsearchQuery.replace(/:price:(.*?):/,":");
	}
	else {
		updatedsearchQuery=updatedsearchQuery.replace(/:price:(.*)/,"");
	}
	$(".facet_mobile .facet.js-facet.Price .category-icons").addClass('blank');
	$(".facet_mobile .facet.js-facet.Price .category-icons span").html('');
	
	//console.log("updatedsearchQuery removing mobile : "+updatedsearchQuery);
	//TISQAUATS-27 ends
	
}


function isCustomSku(requiredUrl){
	if (($("input[name=customSku]").length) && ($("input[name=customSku]").val() == "true")) {			
		// for pagination ajax call
		var dataString = '';
		$.ajax({
			contentType : "application/json; charset=utf-8",
			url : requiredUrl,
			data : dataString,
			success : function(response) {
				// putting AJAX respons to view
				$('#facetSearchAjaxData .right-block, #facetSearchAjaxData .bottom-pagination, #facetSearchAjaxData .facet-list.filter-opt').remove();
				$('#facetSearchAjaxData .left-block').after(response);
			},
			error : function(xhr, status, error) {				
				console.log("Error >>>>>> " + error);
			},
			complete: function() {
			// AJAX changes for custom price filter
			}
		});
		return false;
	}
	return true;
}

//UF-15
function lazyPaginationFacet(response){
	res = response;
	// Added for UF-359
	var ulProduct = $(response).find('ul.product-listing.product-grid,ul.product-list,ul.product-listing.product-grid.custom-sku');
	//Add to bag and quick view ui fixes starts here
	$(".product-tile .image .item.quickview").each(function(){
    	if($(this).find(".addtocart-component").length == 1){
    		$(this).addClass("quick-bag-both");
    	}
    });
	//Add to bag and quick view ui fixes ends here
	productItemArray = [];
    $(ulProduct).find('li.product-item').each(function() {
        productItemArray.push($(this));
    });
    console.log(""+productItemArray);
    // Added for UF-359
    $("#productGrid").html($.strRemove("ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,ul.product-listing.product-grid.custom-sku", response));
	initPageLoad = true;
    innerLazyLoad({isSerp:true});
    settings.totalPages = $('input[name=noOfPages]').val();	
    $(".pagination-blocks").twbsPagination('destroy');
    $('.pagination-blocks').twbsPagination(settings);
}

//UF-15
(function($) {
    $.strRemove = function(theTarget, theString) {
        return $("<div/>").append(
            $(theTarget, theString).empty().end()
        ).html();
    };
})(jQuery);

//UF-254
function appendPageNo(url){
	var modifiedUrl = null;
	if(!(/page-[0-9]+/).test(url)){
		modifiedUrl = url.replace(/[/]$/,"") + '/page-1';
	}else{
		modifiedUrl = url;
	}
	return modifiedUrl;
}

/*code added for TPR-1283--Starts*/
$(document).on("click",".brandFacetRequire",function(){
	var browserURL = window.location.href.split('?');
	if($(".brandFacetRequire").parents("label").children("input.facet-checkbox.js-facet-checkbox.sr-only:checked").length != 0)
		countBrand = $(".brandFacetRequire").parents("label").children("input.facet-checkbox.js-facet-checkbox.sr-only:checked").length+1;
	else{
		countBrand = 1;
		}
	if(countBrand<2){
		var pageURL=$(this).attr("href");
		window.location.href=pageURL;
	}
	else{
		event.preventDefault();
		/*refer to the AJAX for checkbox code--from line no.111--Starts*/
		var staticHost=$('#staticHost').val();
		$("body").append("<div id='no-click' style='opacity:0.60; background:black; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		//$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 50%;top: 50%; height: 30px;">');
		//PRDI-540 spinner issue on multiple brand selection
		$("body").append('<div class="loaderDiv" style="position: fixed; left: 50%;top: 50%;"><img src="'+staticHost+'/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>');
		
		var dataString = null;
		var nonEmptyDataString= null;
		
		// generating datastring and postAjaxURL
		$(this).parents("form").find('input[type="hidden"]').each(function(){
			if(dataString == null){
				dataString = $(this).attr('name')+"="+$(this).val();
			}
			else{
				dataString = dataString + ("&"+$(this).attr('name')+"="+$(this).val());
			}
			
			if($(this).val().length >0){
				if(nonEmptyDataString == null){
					nonEmptyDataString = $(this).attr('name')+"="+$(this).val();
				}
				else{
					nonEmptyDataString = nonEmptyDataString + ("&"+$(this).attr('name')+"="+$(this).val());
				}
			}
		})
		
		/*TPR-1283 CHANGES--Starts*/
	   if(browserURL[0].includes("/b")){
			var indx1=browserURL[0].indexOf("/b");
			var res = browserURL[0].substring(0, indx1);
			browserURL[0]=res;
			//window.location.href=browserURL[0];
		}
		/*TPR-1283 CHANGES--Ends*/
		//UF-254 later page push is not considered . Putting page 1 for default.
		var browserUrlLazy = appendPageNo(browserURL[0]);
		// generating postAjaxURL
		var pageURL = browserUrlLazy +'?'+nonEmptyDataString.replace(/%/g,"%25").replace(/ - /g,"+-+").replace(/:/g,"%3A");
		var requiredUrl="";
		var action = $(this).parents("form").attr('action');
		
		// generating request mapping URL
		if($("#isCategoryPage").val() == 'true'){
			if ($("input[name=customSku]").val()) {
				var collectionId = $("input[name=customSkuCollectionId]").val();
				requiredUrl = '/CustomSkuCollection/'+collectionId+'/getFacetData';
			}
			else {
				action = action.split('/c-');
				action = action[1].split('/');
				requiredUrl = "/c-"+action[0];
				requiredUrl += "/getFacetData";
			}
		} else {
			if(action.indexOf("/getFacetData") == -1){
				if(action.indexOf("offer") > -1 || action.indexOf("viewOnlineProducts") > -1 || action.indexOf('/s/') > -1) {
					requiredUrl = action.concat("/getFacetData");
				}
				else if ($("input[name=customSku]").val()) {
					var collectionId = $("input[name=customSkuCollectionId]").val();
					requiredUrl = '/CustomSkuCollection/'+collectionId+'/getFacetData';
				}
				else{
					//requiredUrl = action.concat("getFacetData");
					// INC_11277 start
					if ((/.*\/$/g).test(action)) {
						requiredUrl = action.concat("getFacetData");
					}
					else{
						requiredUrl = action.concat("/getFacetData");
					}
					// INC_11277 end
				}
			}
			else{
				requiredUrl = action;
			}
		}
		
		// TPR-645 Start  INC_11511  fix--h3 tag done
		filterValue = $(this).parent().find('span.facet-text').text().trim();
		filterName = $(this).parents('li.facet.js-facet').find('div.facet-name.js-facet-name h3').text().trim();
		if($(this).attr('checked')){
			filterChecked = true;
			//onFilterRemoveAnalytics(filterName,filterValue);
		}
		else{
			filterChecked = false;
			//onFilterAddAnalytics(filterName,filterValue);
		}
		// TPR-645 End
		// AJAX call
		filterDataAjax(requiredUrl,encodeURI(dataString),pageURL);
	}
	/*refer to the AJAX for checkbox code--from line no.111--Ends*/
});
/*code added for TPR-1283--Ends*/
