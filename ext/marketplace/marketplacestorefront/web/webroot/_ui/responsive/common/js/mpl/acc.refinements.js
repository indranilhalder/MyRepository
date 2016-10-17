
var updatedsearchQuery = "";
var dummyForm ;
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
//Fix For TISPRO-194(Including Loader)
		
		/*TPR-198 : AJAX Call in SERP and PDP START*/
		
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
				action = action.split('/c-');
				action = action[1].split('/');
				requiredUrl = "/c-"+action[0];
				requiredUrl += "/getFacetData";
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
		})
		
		//TPR-845
		$(document).on("change",".js-product-facet .facet_mobile .js-facet-checkbox, .js-product-facet .facet_mobile .js-facet-checkbox-price",function(){
			var filterMobileQuery = $(this).parents("form").find('input[name="q"]').val();
			dummyForm = $(this).parents("form");
			if(updatedsearchQuery==''){
				updatedsearchQuery=filterMobileQuery;
			}else{
				var newFilter=createSearchQuery(filterMobileQuery);	
				if(updatedsearchQuery.includes(newFilter))
				{
					updatedsearchQuery=updatedsearchQuery.replace(newFilter,"");
				}
				else{
					updatedsearchQuery+=newFilter;
				}
			}
			console.log("updatedsearchQuery : "+updatedsearchQuery);			
		})
		
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
				action = action.split('/c-');
				action = action[1].split('/');
				requiredUrl = "/c-"+action[0];
				requiredUrl += "/getFacetData";
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
		})
		
		//TPR-845
		$(document).on("click",".js-product-facet .facet_mobile .js-facet-colourbutton , .js-product-facet .facet_mobile .js-facet-sizebutton",function(){
			var filterMobileQuery = $(this).parents("form").find('input[name="q"]').val();
			dummyForm = $(this).parents("form");
			if(updatedsearchQuery==''){
				updatedsearchQuery=filterMobileQuery;
				
			}else{
				var newFilter=createSearchQuery(filterMobileQuery);
				
				if(updatedsearchQuery.includes(newFilter))
				{
					updatedsearchQuery=updatedsearchQuery.replace(newFilter,"");
				}
				else{
					updatedsearchQuery+=newFilter;
				}			
			}
			console.log("updatedsearchQuery : "+updatedsearchQuery);
			
		})
		
		// AJAX for removal of filters
		$(document).on("click",".facet-list.filter-opt .remove_filter , .priceBucketExpand .any_price",function(e){
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
				action = action[0].split('/c-');
				action = action[1].split('/');
				requiredUrl = "/c-"+action[0];
				requiredUrl += "/getFacetData";
			}			
			else {
				requiredUrl = action[0].concat("/getFacetData");
			}
			
			// AJAX call
			filterDataAjax(requiredUrl,dataString,pageURL);
			return false;
		})
		
		/*TPR-198 : AJAX Call in SERP and PDP END*/
		
		
		
		
		$(document).on("click",".js-product-facet .js-more-facet-values-link",function(e){
			e.preventDefault();
			$(this).parents(".js-facet").find(".js-facet-top-values").hide();
			$(this).parents(".js-facet").find(".js-facet-list-hidden").show();

			$(this).parents(".js-facet").find(".js-more-facet-values").hide();
			$(this).parents(".js-facet").find(".js-less-facet-values").show();
		})

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
			//console.log(response);
			// putting AJAX respons to view
			if($("#isCategoryPage").val() == 'true'){
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
			
			/*var filter_height=$(".facet-list.filter-opt").height() + 55;
			$(".listing.wrapper .left-block").css("margin-top",filter_height+"px");*/
			
			//TPR - 565
			if (!$("input[name=customSku]").val()) {
			// Scroll up to the top
				$("body,html").animate({scrollTop:0},500);
				
				//Re-write URL after ajax
				window.history.replaceState(response,"",pageURL);
			}		
			//TPR-158 and TPR-413 starts here
			
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
			//TPR-158 and TPR-413 ends here			
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

/*$("#paginationForm .pagination.mobile li a").click(function(e){
	
	e.prevent
	alert($(this).attr('href'));
});*/

//TPR-845
function createSearchQuery(filterMobileQuery){
	var queryString='';
	var splited=filterMobileQuery.split(':');
	for (k = 0; k < splited.length; k++) {
	if(splited.length-3<k){
		queryString+=':'+splited[k];
		}
	}
	return queryString;
}

//AJAX for removal of filters
$(document).on("click",".filter-apply",function(e){
	//TPR-1507
	var filterCount=0;
	$(".facet_mobile .facet.js-facet").each(function(){
		filterCount+=$(this).find(".facet-list.js-facet-list li").find("input[type=checkbox]:checked").length;
		filterCount+=$(".facet_mobile .filter-colour.selected-colour").length;
		filterCount+=$(".facet_mobile .filter-size.selected-size").length;
	})
	if(filterCount<=0){
		return false;
	}
	//TPR-1507 Ends
	else{
		$("body").append("<div id='no-click' style='opacity:0.60; background:black; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		$("body").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 50%;top: 50%; height: 30px;">');
		// generating postAjaxURL
		var browserURL = window.location.href.split('?');
		var dataString = null;
		var nonEmptyDataString= null;
		
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
			console.log("dataString : "+dataString);
			
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
		var pageURL = browserURL[0]+'?'+nonEmptyDataString.replace(/:/g,"%3A");
		var requiredUrl="";
		var action = dummyForm.attr('action');
		
		// generating request mapping URL
		if($("#isCategoryPage").val() == 'true'){
			action = action.split('/c-');
			action = action[1].split('/');
			requiredUrl = "/c-"+action[0];
			requiredUrl += "/getFacetData";
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
		console.log("Controle Came");

		filterDataAjax(requiredUrl,encodeURI(dataString),pageURL);
		return false;
	}	
})

//TPR-845
$(document).on("click"," .filter-clear ",function(e){
	//TPR-1536
	var filterCount=0;
	$(".facet_mobile .facet.js-facet").each(function(){
		filterCount+=$(this).find(".facet-list.js-facet-list li").find("input[type=checkbox]:checked").length;
		filterCount+=$(".facet_mobile .filter-colour.selected-colour").length;
		filterCount+=$(".facet_mobile .filter-size.selected-size").length;
	})
	if(filterCount<=0){
		return false;
	}
	//TPR-1536 Ends
	
	else{
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
		
		//redirecting to pageURL on page reload
		window.location.href =pageURL;
		return false;
	}
	})

$(document).off('click', '.js-facet-colourbutton').on('click', '.js-facet-colourbutton', function() { 
	$(this).parents(".filter-colour").toggleClass("selected-colour");
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
					//console.log(response);
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


	
