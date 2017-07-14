var pageNoPagination = 1;
var totalNoOfPages = 0;
var innerRecordSize = 7;
var loadMoreCount = 72;
var initPageLoad = true;
var directPaginatedLoad = true;
var lazyPagePush = true;
var serpURL;
var ajaxUrl = '';
var recordsLoadedCount = 0;
var pageType = $('#pageType').val();
var isSerp = false;

function innerLazyLoad(options) {
	if(typeof(options)!='undefined' && options.isSerp){
    	totalNoOfPages = $('input[name=noOfPages]').val();
        totalNoOfPages == '' ? 0 : parseInt(totalNoOfPages);
        isSerp = true;
    }
    //get the 8 items from the array and render // TODO: identify place holder done
    var gridHTML = '';
    //if total no of pages is 1 then load all 24 products at once.
    if(1 == totalNoOfPages){
    	$.each(productItemArray, function(index, element) {
                    gridHTML += '<li class="product-item">' + $(element).html() + '</li>';
        });
    }else{
    	 $.each(productItemArray, function(index, element) {
    	        if (index <= innerRecordSize) {
    	            if (index == innerRecordSize) {
    	                //productsLoaded+= index;
    	                gridHTML += '<li class="product-item lazy-reached">' + $(element).html() + '</li>';
    	                //$('ul.product-listing.product-grid').eq(1).append('<li class="product-item lazy-reached">' + $(element).html() + '</li>');
    	            } else {
    	                gridHTML += '<li class="product-item">' + $(element).html() + '</li>';
    	                //$('ul.product-listing.product-grid').eq(1).append('<li class="product-item">' + $(element).html() + '</li>');
    	            }
    	        }
    	    });
    }
   
    if (initPageLoad) { //TODO: duplicate loading prevention
        //$('ul.product-listing.product-grid').eq(2).html(gridHTML).hide().fadeIn(500);
        $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,ul.product-listing.product-grid.lazy-grid-normal,ul.product-listing.product-grid.custom-sku').html(gridHTML).hide().fadeIn(500);
        initPageLoad = false;
    } else {
        //$('ul.product-listing.product-grid').eq(2).append(gridHTML);
        $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,ul.product-listing.product-grid.lazy-grid-normal,ul.product-listing.product-grid.custom-sku').append(gridHTML);
    }
    deleteArraySet(productItemArray);
    
	/*if(pageNoPagination == totalNoOfPages){
		$('li').removeClass('lazy-reached');
	}*/
}

function innerLazyStorageLoad(productItemArrayStore) {
    var gridHTML = '';
    $.each(productItemArrayStore, function(index, element) {
                gridHTML += '<li class="product-item">' + $(element).html() + '</li>';
    });
 // INC144315462 and INC144315104
    $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,ul.product-listing.product-grid.lazy-grid-normal,ul.product-listing.product-grid.custom-sku').html(gridHTML).hide().fadeIn(500);
}

function deleteArraySet(productItemArray) {
	
    if (productItemArray.length != 0) {
        for (i = 0; i <= innerRecordSize; i++) {
        	productItemArray.shift();
        }
        //for serp
        if($('ul.product-listing.product-grid').length==0){
        	recordsLoadedCount = $('.product-list').find('li.product-item').length;
        }else{

        	// INC144315462 and INC144315104  
        	
        	// INC144315462 and INC144315104
        	//recordsLoadedCount = $('.product-listing.product-grid.lazy-grid,.product-listing.product-grid.lazy-grid-facet,.product-listing.product-grid.lazy-grid-normal').find('li.product-item').length;
        	  recordsLoadedCount = $('.product-listing.product-grid.lazy-grid,.product-listing.product-grid.lazy-grid-facet,.product-listing.product-grid.lazy-grid-normal,ul.product-listing.product-grid.custom-sku').find('li.product-item').length;
        }
    }
}

function getProductSetData() {

    var pathName = window.location.pathname;
    var query = window.location.search;
    
    //INC144316143
    if ($('#pageType').val() == 'productsearch' || $('#pageType').val() == 'category') {

        window.localStorage.setItem('lastUrlpathName',encodeURI(pathName));
        window.localStorage.setItem('lastUrlquery',encodeURI(query));

    }
    if (pageNoPagination <= totalNoOfPages) {

        //url with page no occourance found.
        if (/page-[0-9]+/.test(pathName)) {
            var currentPageNo = pathName.match(/page-[0-9]+/);

            currentPageNo = currentPageNo[0].split("-");
            currentPageNo = parseInt(currentPageNo[1]);
            if (directPaginatedLoad) {
                directPaginatedLoad = false;
            } else {
                currentPageNo++; 
            }
            if (currentPageNo <= totalNoOfPages) {
            	if(facetAjaxUrl){
            		ajaxUrl = facetAjaxUrl.replace(/page-[0-9]+/, 'page-' + currentPageNo);
            		var sort = findGetParameter('sort');
            		if(sort){
            			ajaxUrl = ajaxUrl + '&sort='+ sort;
            		}
            		window.history.replaceState({}, "", ajaxUrl);
            	}else{
            		ajaxUrl = pathName.replace(/page-[0-9]+/, 'page-' + currentPageNo);
            		var nextPaginatedAjaxUrl = pathName.replace(/page-[0-9]+/, 'page-' + currentPageNo);
                    if (query) {
                        ajaxUrl = ajaxUrl + query;
                        nextPaginatedAjaxUrl = nextPaginatedAjaxUrl + query;
                    }
                    window.history.replaceState({}, "", nextPaginatedAjaxUrl);
            	}
				ajaxPLPLoad(ajaxUrl);
            }
        } else { // if no url with page no occourance found.
            if (pageNoPagination <= totalNoOfPages) {
                ajaxUrl = pathName.replace(/[/]$/,"") + '/page-' + pageNoPagination;
                if(pageType == 'productsearch'){//for serp initial page 
                	ajaxUrl = ajaxUrl + '?'+ $('#searchPageDeptHierTreeForm').serialize();
            	}else if(query){
            		ajaxUrl = ajaxUrl + query;
            	}
                var nextPaginatedAjaxUrl = ajaxUrl;
                //window.history.replaceState({}, "", nextPaginatedAjaxUrl);
                directPaginatedLoad =false;
            }
			ajaxPLPLoad(ajaxUrl);
        }
    }
}

//INC144315462 and INC144315104
function getProductSetDataCustomSku() {
	 	 
	  	var pathName = $('input[name=customSkuUrl]').val();
	      var query = window.location.search;
	     if (pageNoPagination <= totalNoOfPages) {
	  
	          //url with page no occourance found.
	          if (/page-[0-9]+/.test(pathName)) {
	              var currentPageNo = pathName.match(/page-[0-9]+/);
	  
	              currentPageNo = currentPageNo[0].split("-");
	              currentPageNo = parseInt(currentPageNo[1]);
	              if (directPaginatedLoad) {
	                  directPaginatedLoad = false;
	              } else {
	                  currentPageNo++; 
	              }
	              if (currentPageNo <= totalNoOfPages) {
	              	if(facetAjaxUrl){
	              		ajaxUrl = facetAjaxUrl.replace(/page-[0-9]+/, 'page-' + currentPageNo);
	              		var sort = findGetParameter('sort');
	              		if(sort){            			
	              			ajaxUrl = ajaxUrl + '&sort='+ sort;
	              			
	              		}
	              		window.history.replaceState({}, "", ajaxUrl);
	              	}else{
	              		ajaxUrl = pathName.replace(/page-[0-9]+/, 'page-' + currentPageNo);
	              		var nextPaginatedAjaxUrl = pathName.replace(/page-[0-9]+/, 'page-' + currentPageNo);
	                      if (query) {
	                          ajaxUrl = ajaxUrl + query;
	                          nextPaginatedAjaxUrl = nextPaginatedAjaxUrl + query;
	                      }
	                      window.history.replaceState({}, "", nextPaginatedAjaxUrl);
	              	}
	              	
	  				ajaxPLPLoad(ajaxUrl);
	              }
	          } else { // if no url with page no occourance found.
	              if (pageNoPagination <= totalNoOfPages) {
	                  ajaxUrl = pathName.replace(/[/]$/,"") + '/page-' + pageNoPagination;
	                  directPaginatedLoad =false;
	              }
	   			ajaxPLPLoad(ajaxUrl);
	           }
	       }
}

$(document).ready(function() {
    //set the total no of pages 
    totalNoOfPages = $('input[name=noOfPages]').val();
    totalNoOfPages == '' ? 0 : parseInt(totalNoOfPages);
    var lazyfrompdp = window.localStorage.getItem("lazyfrompdp");
    
    //INC144316143
    var pathName = window.location.pathname;
    var query    = window.location.search;
    var searchResultStorageData = "true";
    var lastUrlpathName = window.localStorage.getItem('lastUrlpathName');
    var lastUrlquery    = window.localStorage.getItem('lastUrlquery');
    

    if( (lastUrlpathName != undefined  && encodeURI(pathName) != lastUrlpathName) || (lastUrlquery != undefined && encodeURI(query) != lastUrlquery)) {
    	searchResultStorageData = "false"; // not same with last search url
    } 
    
    if (window.localStorage &&  (html = window.localStorage.getItem('productlazyarray')) && searchResultStorageData == 'true' && html != '' && lazyfrompdp != '' && lazyfrompdp == 'true' && ($('#pageType').val() == 'productsearch' || $('#pageType').val() == 'category')) {
    	
    		$('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list').html(decodeURI(html));
        	
        	//added for load more
            if($('ul.product-listing.product-grid').length==0){
            	recordsLoadedCount = $('.product-list').find('li.product-item').length;
            }else{
          	  // INC144315462 and INC144315104
            	//recordsLoadedCount = $('.product-listing.product-grid.lazy-grid,.product-listing.product-grid.lazy-grid-facet,.product-listing.product-grid.lazy-grid-normal').find('li.product-item').length;
            	 recordsLoadedCount = $('.product-listing.product-grid.lazy-grid,.product-listing.product-grid.lazy-grid-facet,.product-listing.product-grid.lazy-grid-normal,.product-listing.product-grid.custom-sku').find('li.product-item').length;
            }
            
            if (recordsLoadedCount!=0 && (recordsLoadedCount % loadMoreCount) == 0) {            	
                $('.loadMorePageButton').remove();

                // INC144315462 and INC144315104  
               // $('ul.product-listing.product-grid.lazy-grid,.product-listing.product-grid.lazy-grid-facet,ul.product-list,.product-listing.product-grid.lazy-grid-normal').after('<button class="loadMorePageButton" style="background: #a9143c;color: #fff;margin: 5px auto;font-size: 12px;height: 40px;padding: 9px 18px;width: 250px;">Load More</button>');                


                $('ul.product-listing.product-grid.lazy-grid,.product-listing.product-grid.lazy-grid-facet,ul.product-list,.product-listing.product-grid.lazy-grid-normal,.product-listing.product-grid.custom-sku').after('<button class="loadMorePageButton" style="background: #a9143c;color: #fff;margin: 5px auto;font-size: 12px;height: 40px;padding: 9px 18px;width: 250px;">Load More</button>');
            }
            //end added for load more
            window.localStorage.setItem('lazyfrompdp','false');

    }else{
    	//TISSQAUAT-3429 starts
    	//getProductSetData();
    	// INC144315462 and INC144315104
    	//TISSQAUAT-3429 ends
    	if($('input[name=customSku]').length == 1){
    		getProductSetDataCustomSku();
    	}else{
    		getProductSetData();
    	}
    }
        $(window).on('scroll', function() {
            if ($('.lazy-reached').length != 0) {
            	
            	if(productItemArray.length == 16){
					lazyPushInitalPage();
				}
                var hT = $('.lazy-reached').offset().top,
                    hH = $('.lazy-reached').outerHeight(),
                    wH = $(window).height(),
                    wS = $(this).scrollTop();
                if (wS > (hT + hH - wH)) {
				  $('.product-item').removeClass('lazy-reached');
					//$('li').removeClass('lazy-reached');
                    if (recordsLoadedCount!=0 && (recordsLoadedCount % loadMoreCount) == 0) {
                        $('.loadMorePageButton').remove();
                     // INC144315462 and INC144315104
                        //$('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,.product-listing.product-grid.lazy-grid-normal').after('<button class="loadMorePageButton" style="background: #a9143c;color: #fff;margin: 5px auto;font-size: 12px;height: 40px;padding: 9px 18px;width: 250px;">Load More</button>');
                          $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,.product-listing.product-grid.lazy-grid-normal,.product-listing.product-grid.custom-sku').after('<button class="loadMorePageButton" style="background: #a9143c;color: #fff;margin: 5px auto;font-size: 12px;height: 40px;padding: 9px 18px;width: 250px;">Load More</button>');
                    } else {
                        if (productItemArray.length == 0) { //TODO: check if category page 
                            //window.history.replaceState({},"",ajaxUrl);
                           // getProductSetData();

                        	if($('input[name=customSku]').length == 1){
                        		getProductSetDataCustomSku();
                        		}else{
                        	 	//getProductSetData();
                        			// INC144315462 and INC144315104
                        			if($('input[name=customSku]').length == 1){
                        				getProductSetDataCustomSku();
                        				  	}else{
                        		 		getProductSetData();
                        				}
                        	 	}
                        } else {
                            innerLazyLoad({
                                effect: true
                            });
                        }
                    }
                }
            }
        });
        // listner for load more
        $(document).on('click', '.loadMorePageButton', function() {
          //  getProductSetData();
        	// INC144315462 and INC144315104
        	if($('input[name=customSku]').length == 1){
        	 		getProductSetDataCustomSku();
        				}else{
        			getProductSetData();

        		}
            $('.loadMorePageButton').remove();
        });
   // }
        $(window).on("load resize",function(){
        	$("body.page-productGrid .list_title h1").css("display","block");		/*TPR-250 defect fix No result*/	/*TISSTRT-1520*/	/*TISSQAEE-458*/
            if($("body.page-productGrid .list_title h1").width() > 200 && $("body.page-productGrid .totalResults").length)
            	$("body.page-productGrid .list_title h1").css("display","block").addClass("shiftDownZero");		/*TPR-250 defect fix No result*/	/*TISSTRT-1520*/	/*TISSQAEE-458*/
        if($("body").hasClass("page-productGrid")){
        $("body.page-productGrid .list_title h1").css("margin-left",$(".right-block").offset().left+parseInt($(".right-block").css("padding-left")));
        $("body.page-productGrid .list_title h1.shiftDownZero").css("margin-left","0");		/*TPR-250 defect fix No result*/	/*TISSTRT-1520*/	/*TISSQAEE-458*/
        }
        });
        
        $(document).on('click','.sort',function(){
        	//sort($(this),false);

        	// INC144315462 and INC144315104
        	if($('input[name=customSku]').length){
        		sortCustomSku($(this),false);
        		}else{
        		 sort($(this),false);
        	 	}


        });
      //Added for PRDI-109  and INC144315439
        $(document).on('change','.responsiveSort',function(){ 
      //  $('.responsiveSort').change(function(){
        	//sort($(this).find(':selected'),true);
        	// INC144315462 and INC144315104
        	if($('input[name=customSku]').length){
        		sortCustomSku($(this).find(':selected'),true);
        		}else{
        		sort($(this).find(':selected'),true);
        			}
        });        
       
        
        $(document).on('click','.product-item',function(){
        	if(window.localStorage){
        		var productGridHtml = $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,.product-listing.product-grid.lazy-grid-normal').html();
        		window.localStorage.setItem("productlazyarray",encodeURI(productGridHtml));
        		window.localStorage.setItem('lazyfrompdp','true');
        	}
        });
        
        $(window).keypress(function( event ) {
        	  if ( event.which == 13 ) {
        		window.localStorage.removeItem('lazyfrompdp');
          		window.localStorage.removeItem('productlazyarray');
          		window.localStorage.removeItem('lastUrlpathName');
          		window.localStorage.removeItem('lastUrlquery');
        	  }
        });
        $(document).on('click','.words,.departmenthover,.searchButton',function(){
        	window.localStorage.removeItem('lazyfrompdp');
      		window.localStorage.removeItem('productlazyarray');
      		window.localStorage.removeItem('lastUrlpathName');
      		window.localStorage.removeItem('lastUrlquery');
        });
});

function findGetParameter(parameterName) {
    var result = null,
        tmp = [];
    location.search
    .substr(1)
        .split("&")
        .forEach(function (item) {
        tmp = item.split("=");
        if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
    });
    return result;
}
function findGetParameterUrl(parameterName,URL) {
    var result = null,
        tmp = [];
    URL
    .substr(1)
        .split("&")
        .forEach(function (item) {
        tmp = item.split("=");
        if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
    });
    return result;
}
function removeURLParameter(url, parameter) {
    var urlparts= url.split('?');   
    if (urlparts.length>=2) {
        var prefix= encodeURIComponent(parameter)+'=';
        var pars= urlparts[1].split(/[&;]/g);
        for (var i= pars.length; i-- > 0;) {    
            if (pars[i].lastIndexOf(prefix, 0) !== -1) {  
                pars.splice(i, 1);
            }
        }
        url= urlparts[0] + (pars.length > 0 ? '?' + pars.join('&') : "");
        return url;
    } else {
        return url;
    }
}
function ajaxPLPLoad(ajaxUrl){
	
    $.ajax({
        url: ajaxUrl,
        data: {
            pageSize: 24,
            /* q: '', */
            lazyInterface:'Y'
        },
        beforeSend: function() {
            var staticHost = $('#staticHost').val();
            $('ul.product-listing.product-grid.lazy-grid,ul.product-list').after('<p class="lazyLoadPagination" style="text-align: center;margin: 5px 0;font-size: 18px;">Loading...<span class="loaderDiv" style="vertical-align:middle;"><img src="' + staticHost + '/_ui/responsive/common/images/red_loader.gif" class="spinner"></span></p>');
            
        },
        success: function(x) {
            var filtered = $.parseHTML(x);
            var ulProduct = null;
            //for serp
            if($('ul.product-listing.product-grid').length==0){
            	 ulProduct = $(filtered).find('ul.product-list');


            }
            else if($('input[name=customSku]').length == 1){
           	// ulProduct = $(filtered).find('ul.product-listing.product-grid.custom-sku');
            	ulProduct = $(filtered).find('ul.product-list');
            }
            else{
            	 ulProduct = $(filtered).find('ul.product-listing.product-grid.lazy-grid,.product-listing.product-grid.lazy-grid-normal');
            }
          //Add to bag and quick view ui fixes starts here
            $(".product-tile .image .item.quickview").each(function(){
            	if($(this).find(".addtocart-component").length == 1){
            		$(this).addClass("quick-bag-both");
            	}
            });
          //Add to bag and quick view ui fixes ends here
            productItemArray = [];
            
            $(ulProduct).find('li.product-item').each(function() {
                productItemArray.push($(this))
            });
            ACC.quickview.bindToUiCarouselLink();
        },
        complete: function() {
            $('.lazyLoadPagination').remove();
            innerLazyLoad();
            //TPR-4720 first 5 product display
			if($('#pageType').val() == "productsearch"){
				populateFirstFiveProductsSerp();	
			}
			
			if($('#pageType').val() == "category" || $('#pageType').val() == "electronics"){
				populateFirstFiveProductsPlp();
			}
            //ACC.quickview.bindToUiCarouselLink();
        }
    });
}

function sortReplaceState(url){
		var nextPaginatedAjaxUrl = url.replace(/page-[0-9]+/, 'page-1');
		 window.history.replaceState({}, '', nextPaginatedAjaxUrl);
}


//Added for custom sku
//INC144315462 and INC144315104
function sortReplaceStateCustomSku(url){
	
	console.log(url);
	var customSkuCollectionId = $('input[name=customSkuCollectionId]').val();
	url = url.replace(customSkuCollectionId,customSkuCollectionId+'/page-1')
	$('input[name=customSkuUrl]').val(url);
}

//INC144315462 and INC144315104  
function sortReplaceStateCustomSku(url){
	 	console.log(url);
	 	var customSkuCollectionId = $('input[name=customSkuCollectionId]').val();
	 	url = url.replace(customSkuCollectionId,customSkuCollectionId+'/page-1')
	 	$('input[name=customSkuUrl]').val(url);
	 }

function sort(this_data,drop_down){
	var item = $(this_data).attr('data-name');
	$('.sort').removeAttr('style');
	if(!drop_down){
		$(this_data).css('color', '#a5173c');
	}
	var pathName = window.location.pathname;
	pathName = pathName.replace(/page-[0-9]+/, 'page-1');
	
	var url = '';
	switch (item) {
	case 'relevance':
		if(pageType == 'productsearch'){
			var url = $('#searchPageDeptHierTreeForm').serialize();
			url = url+'&sort=relevance';
		}else{
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			url = url+'&sort=relevance';
		}
		ajaxPLPLoad(pathName +'?'+url);
		sortReplaceState(pathName +'?'+url); 
		initPageLoad = true;
		break;
	case 'new':
		if(pageType == 'productsearch'){
			var url = $('#searchPageDeptHierTreeForm').serialize();
			url = url+'&sort=isProductNew';
		}else{
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			url = url+'&sort=isProductNew';
		}
		ajaxPLPLoad(pathName +'?'+url);
		sortReplaceState(pathName +'?'+url); 
		initPageLoad = true;
		break;
	case 'discount':
		if(pageType == 'productsearch'){
			var url = $('#searchPageDeptHierTreeForm').serialize();
			url = url+'&sort=isDiscountedPrice';
		}else{
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			url = url+'&sort=isDiscountedPrice';
		}
		ajaxPLPLoad(pathName +'?'+url);
		sortReplaceState(pathName +'?'+url); 
		initPageLoad = true;
		break;
	case 'low':
		if(pageType == 'productsearch'){
			var url = $('#searchPageDeptHierTreeForm').serialize();
			url = url+'&sort=price-asc';
		}else{
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			url = url+'&sort=price-asc';
		}
		ajaxPLPLoad(pathName +'?'+url);
		sortReplaceState(pathName +'?'+url); 
		initPageLoad = true;
		break;
	case 'high':
		if(pageType == 'productsearch'){
			var url = $('#searchPageDeptHierTreeForm').serialize();
			url = url+'&sort=price-desc';
		}else{
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			url = url+'&sort=price-desc';
		}
		ajaxPLPLoad(pathName +'?'+url);
		sortReplaceState(pathName +'?'+url); 
		initPageLoad = true;
		break;
	default:
		break;
	}
}


//Added for custom sku
//INC144315462 and INC144315104
function sortCustomSku(this_data,drop_down){
	console.log(typeof(this_data));
	var item = $(this_data).attr('data-name');
	$('.sort').removeAttr('style');
	if(!drop_down){
		$(this_data).css('color', 'red');
	}
	var url = '';
	switch (item) {
	case 'relevance':
		if($('input[name=customSku]').length == 1){
			var lookId = $('input[name=customSkuCollectionId]').val();
			if($('#searchPageDeptHierTreeForm').serialize()!=""){
 				url = '/CustomSkuCollection/'+lookId+'?'+$('#searchPageDeptHierTreeForm').serialize()+'&sort=relevance';
 			}else{
 				url = '/CustomSkuCollection/'+lookId+'?q='+$('#js-site-search-input').val()+'&sort=relevance';
 			}
			ajaxPLPLoad(url);
		}
		initPageLoad = true;
		break;
	case 'new':
		if($('input[name=customSku]').length == 1){
			var lookId = $('input[name=customSkuCollectionId]').val();
			if($('#searchPageDeptHierTreeForm').serialize()!=""){
 				url = '/CustomSkuCollection/'+lookId+'?'+$('#searchPageDeptHierTreeForm').serialize()+'&sort=new';
 			}else{
 				url = '/CustomSkuCollection/'+lookId+'?q='+$('#js-site-search-input').val()+'&sort=new';
 			}
			ajaxPLPLoad(url);
		}
		initPageLoad = true;
		break;
	case 'discount':
		if($('input[name=customSku]').length == 1){
			var lookId = $('input[name=customSkuCollectionId]').val();
			if($('#searchPageDeptHierTreeForm').serialize()!=""){
 				url = '/CustomSkuCollection/'+lookId+'?'+$('#searchPageDeptHierTreeForm').serialize()+'&sort=isDiscountedPrice';
 			}else{
 				url = '/CustomSkuCollection/'+lookId+'?q='+$('#js-site-search-input').val()+'&sort=isDiscountedPrice';
 			}
			ajaxPLPLoad(url);
		}
		initPageLoad = true;
		break;
	case 'low':
		if($('input[name=customSku]').length == 1){
			var lookId = $('input[name=customSkuCollectionId]').val();
			if($('#searchPageDeptHierTreeForm').serialize()!=""){
 				url = '/CustomSkuCollection/'+lookId+'?'+$('#searchPageDeptHierTreeForm').serialize()+'&sort=price-asc';
 			}else{
 				url = '/CustomSkuCollection/'+lookId+'?q='+$('#js-site-search-input').val()+'&sort=price-asc';
 			}
			ajaxPLPLoad(url);
		}
		initPageLoad = true;
		break;
	case 'high':
		if($('input[name=customSku]').length == 1){
			var lookId = $('input[name=customSkuCollectionId]').val();
			if($('#searchPageDeptHierTreeForm').serialize()!=""){
 				url = '/CustomSkuCollection/'+lookId+'?'+$('#searchPageDeptHierTreeForm').serialize()+'&sort=price-desc';
 			}else{
 				url = '/CustomSkuCollection/'+lookId+'?q='+$('#js-site-search-input').val()+'&sort=price-desc';
 			}
			ajaxPLPLoad(url);
		}
		initPageLoad = true;
		break;
	default:
		break;
	}
}

function lazyPushInitalPage(){
	
	var pathName = window.location.pathname;
	var pageNoExists = /page-[0-9]+/.test(pathName);

	if(lazyPagePush && !pageNoExists){
		var query = window.location.search;
		var initialUrl = pathName.replace(/[/]$/,"") + '/page-1';
		if(pageType == 'productsearch'){//for serp initial page 
		initialUrl = initialUrl + '?'+ $('#searchPageDeptHierTreeForm').serialize();
		}else if(query){
		initialUrl = initialUrl + query;
		}
		window.history.replaceState({}, "", initialUrl);
		lazyPagePush = false;
	}
}
