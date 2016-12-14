
var updatedsearchQuery = "";
var dummyForm ;
var lessBrands = [];
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
			$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 50%;top: 50%; height: 30px;">');
			
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
			
			// generating postAjaxURL
			var pageURL = browserURL[0]+'?'+nonEmptyDataString.replace(/%/g,"%25").replace(/ - /g,"+-+").replace(/:/g,"%3A");
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
						requiredUrl = action.concat("getFacetData");
					}
				}
				else{
					requiredUrl = action;
				}
			}
			
			// AJAX call
			filterDataAjax(requiredUrl,encodeURI(dataString),pageURL);
			// TPR-645 Start
			var filterValue = $(this).parent().find('span.facet-text').text().trim();
			var filterName = $(this).parents('li.facet.js-facet').find('div.facet-name.js-facet-name h4').text().trim();
			onFilterClickAnalytics(filterName,filterValue);
			// TPR-645 End
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
			//console.log("Full View: updatedsearchQuery 4: "+updatedsearchQuery);			
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
			$("body").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 50%;top: 50%; height: 30px;">');
			
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
			
			// generating postAjaxURL
			var pageURL = browserURL[0]+'?'+nonEmptyDataString.replace(/%/g,"%25").replace(/ - /g,"+-+").replace(/:/g,"%3A");
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
						requiredUrl = action.concat("getFacetData");
					}
				}
				else{
					requiredUrl = action;
				}
			}
			// AJAX call
			filterDataAjax(requiredUrl,encodeURI(dataString),pageURL);
			// TPR-645 Start
			var filterValue = '';
			var filterName = $(this).parents('li.facet.js-facet').find('div.facet-name.js-facet-name h4').text().trim();
			if($(this).attr('class').indexOf('size') > -1){
				filterValue = $(this).attr('value');
			}
			else{
				// console.log($(this).attr('class').text());
				filterValue = $(this).parent().find('span > span').text();
			}
			onFilterClickAnalytics(filterName,filterValue);
			// TPR-645 End
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
			$("body").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 50%;top: 50%; height: 30px;">');
			
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
			
			// AJAX call
			filterDataAjax(requiredUrl,dataString,pageURL);
			return false;
		});
		
		/* TPR-198 : AJAX Call in SERP and PDP END */
		
		$(document).on("change","ul.facet-list.js-facet-top-values.active:first input[type=checkbox]",function(){
			//$('ul.facet-list.js-facet-top-values.active').first().find('input[type=checkbox]').change(function(){
				var brandNode = $(this).parent().find('span.facet-text').text().trim();
				if($(this).is(':checked')){
					$('ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').first().find("span.facet-text:contains('"+brandNode+"')").closest('label').find('input[type=checkbox]').prop('checked',true);	
				}else{
					$('ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').first().find("span.facet-text:contains('"+brandNode+"')").closest('label').find('input[type=checkbox]').prop('checked',false);
				}
		});
			
		$(document).on("change","ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden:first input[type=checkbox]",function(){
			//$('ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').first().find('input[type=checkbox]').change(function(){
				var brandNode = $(this).parent().find('span.facet-text').text().trim();
				if($(this).is(':checked')){
					$('ul.facet-list.js-facet-top-values.active').first().find("span.facet-text:contains('"+brandNode+"')").closest('label').find('input[type=checkbox]').prop('checked',true);	
				}else{
					$('ul.facet-list.js-facet-top-values.active').first().find("span.facet-text:contains('"+brandNode+"')").closest('label').find('input[type=checkbox]').prop('checked',false);
				}
		});
		
		
		
		$(document).on("click",".js-product-facet .js-more-facet-values-link",function(e){
			e.preventDefault();
			$(this).parents(".js-facet").find(".js-facet-top-values").hide();
			$(this).parents(".js-facet").find(".js-facet-list-hidden").show();

			$(this).parents(".js-facet").find(".js-more-facet-values").hide();
			$(this).parents(".js-facet").find(".js-less-facet-values").show();
		});
		
			$(document).on("click",".js-less-facet-values-link",function(e){
			e.preventDefault();
			//var brandFacet = [];
			
			$(this).parents(".js-facet").find(".js-facet-top-values").show();
			$(this).parents(".js-facet").find(".js-facet-list-hidden").hide();

			$(this).parents(".js-facet").find(".js-more-facet-values").show();
			$(this).parents(".js-facet").find(".js-less-facet-values").hide();
		});

		// AJAX for removal of filters
		$(document).on("click",".filter-apply",function(e){
			// TPR-1507
			var filterCount=0;
			$(".facet_mobile .facet.js-facet").each(function(){
				filterCount+=$(this).find(".facet-list.js-facet-list li").find("input[type=checkbox]:checked").length;
				filterCount+=$(".facet_mobile .filter-colour.selected-colour").length;
				//TISQAUATS-12 starts 
				filterCount+=$(".facet_mobile .filter-colour.selected-multi-colour").length;
				//TISQAUATS-12 ends
				filterCount+=$(".facet_mobile .filter-size.selected-size").length;
			});
			//TISQAUATS-27 starts
			if ($('#customMinPriceMob').val() && $('#customMaxPriceMob').val()) {
				filterCount++;
			}
			//TISQAUATS-27 ends
			if(filterCount<=0){
				return false;
			}
			// TPR-1507 Ends
			else{
				$("body").append("<div id='no-click' style='opacity:0.60; background:black; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
				$("body").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 50%;top: 50%; height: 30px;">');
				// generating postAjaxURL
				var browserURL = window.location.href.split('?');
				var dataString = null;
				var nonEmptyDataString= null;
				
				//TISQAUATS-27 starts 
				if ($('#customMinPriceMob').val() && $('#customMaxPriceMob').val()) {
					var minPriceSearchTxt = $('#customMinPriceMob').val();
					var maxPriceSearchTxt = $('#customMaxPriceMob').val();
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
				
				// generating postAjaxURL
				var pageURL = browserURL[0]+'?'+nonEmptyDataString.replace(/:/g,"%3A");
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
							requiredUrl = action.concat("getFacetData");
						}
					}
					else{
						requiredUrl = action;
					}
				}
				// AJAX call
				// console.log("Controle Came");

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
				$("body").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 50%;top: 50%; height: 30px;">');

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
				$(this).parents(".filter-colour").toggleClass("selected-multi-colour");
			}
			else{
				$(this).parents(".filter-colour").toggleClass("selected-colour");
			}
			//TISQAUATS-12 ends
			//TISQAUATS-12 starts
			/*var li_index = $('.facet_mobile .facet.js-facet.Colour .facet-list.js-facet-top-values.active li').index($(this).parents(".filter-colour"));
			$(".facet_mobile .facet.js-facet.Colour .facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden li").eq(li_index).toggleClass("selected-colour");
			var spanCountMoreColor = $('.facet_mobile .facet.js-facet.Colour ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').find("li.selected-colour").length;*/
			if ($('.facet_mobile .facet.js-facet.Colour .facet-list.js-facet-top-values.active li').length) {
				var colourName = $(this).parent().find("input[name=facetValue]").val();
				if(colour_name == "Multi"){
					$(this).closest('ul').next().find('li input[value="'+colourName+'"]').parents(".filter-colour").toggleClass("selected-multi-colour");
				}
				else{
					$(this).closest('ul').next().find('li input[value="'+colourName+'"]').parents(".filter-colour").toggleClass("selected-colour");
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
						$('li.facet.js-facet.Brand').find('span.category-icons').removeClass("blank");
						$('li.facet.js-facet.Brand').find('span.category-icons span').text(spanCountMore);
						//$(this).find(".category-icons").removeClass("blank");
						//$(this).find(".category-icons span").text(spanCount);
					}
					else
					{
						//$(this).find(".category-icons").addClass("blank");
						$('li.facet.js-facet.Brand').find('span.category-icons').addClass("blank");
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
		$(document).on("click",".pagination.mobile li a",function(e){
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
		});
		

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
	console.log(requiredUrl);
	console.log(pageURL);
	if ($("input[name=customSku]").val()) {
		dataString = dataString + "&sort=" + $("select[name=sort]").val() + "&pageSize=" + $("select[name=pageSize]").val(); 
	}
	
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		success : function(response) {
			// console.log(response);
			// putting AJAX respons to view
			if($("#isCategoryPage").val() == 'true' && !$("input[name=customSku]").val()){
				$("#productGrid").html(response);
			}		
			else{
				
				if(requiredUrl.indexOf("offer") > -1 || requiredUrl.indexOf("viewOnlineProducts") > -1 || requiredUrl.indexOf("/s/") > -1){
					$("#productGrid").html(response);
				}
				else{
					$("#facetSearchAjaxData").html(response);
				}
			}
			
			$("#no-click").remove();
			$(".spinner").remove();
			
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
			}		
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
		}
	});
	
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

// TPR-645
function onFilterClickAnalytics(filterName,filterValue){
	var msg = (filterName+"_"+filterValue).toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	utag.link({
		link_obj: this,
		link_text: msg ,
		event_type : 'search_filter_usage',
		search_filter : msg
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




