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
        $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,ul.product-listing.product-grid.lazy-grid-normal,ul.product-listing.product-grid.custom-sku').html(gridHTML).hide().fadeIn(500);
        initPageLoad = false;
    } else {
        $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,ul.product-listing.product-grid.lazy-grid-normal,ul.product-listing.product-grid.custom-sku').append(gridHTML);
        $("img.lazy").lazyload();
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
	  	var browserPathName = window.location.pathname;
	    var query = window.location.search;
	    
	     if (pageNoPagination <= totalNoOfPages) {
	  
	          //url with page no occourance found.
	          if (/page-[0-9]+/.test(pathName)) {
	        	/* TISSPTEN-124 & TISSPTEN-123 starts */
	        	//var currentPageNo = pathName.match(/page-[0-9]+/);
	        	if (/page-[0-9]+/.test(browserPathName)) {
	        	  var currentPageNo = browserPathName.match(/page-[0-9]+/);
	        	}
	        	else {
	        	  var currentPageNo = pathName.match(/page-[0-9]+/);  
	        	}
	        	/* TISSPTEN-124 & TISSPTEN-123 ends */
	  
	              currentPageNo = currentPageNo[0].split("-");
	              currentPageNo = parseInt(currentPageNo[1]);
	              if (directPaginatedLoad) {
	                  directPaginatedLoad = false;
	              } else {
	                  currentPageNo++; 
	              }
	              if (currentPageNo <= totalNoOfPages) {
	            	if(facetAjaxUrl){
	              		//ajaxUrl = facetAjaxUrl.replace(/page-[0-9]+/, 'page-' + currentPageNo);
	            		//TISSPTEN-130 starts
	            		nextPaginatedUrl = facetAjaxUrl.replace(/page-[0-9]+/, 'page-' + currentPageNo);
	            		var lookId = $('input[name=customSkuCollectionId]').val();
	            		ajaxUrl = '/CustomSkuCollection/'+lookId+'/page-'+currentPageNo;
	            		if (facetAjaxUrl.indexOf("?") > -1) {
	            			var facetAjaxUrlArr = facetAjaxUrl.split('?');
	            			if (facetAjaxUrlArr[1] != "") {
	            				ajaxUrl = ajaxUrl + '?' + facetAjaxUrlArr[1];
	            			}
	            		}
	            		var sort = findGetParameter('sort');
	              		if(sort){            			
	              			ajaxUrl = ajaxUrl + '&sort='+ sort;
	              			nextPaginatedUrl = nextPaginatedUrl + '&sort='+ sort;
	              		}
	              		window.history.replaceState({}, "", nextPaginatedUrl);
	              		//TISSPTEN-130 ends
	              	}else{
	              		ajaxUrl = pathName.replace(/page-[0-9]+/, 'page-' + currentPageNo);
	              		/* TISSPTEN-124 & TISSPTEN-123 starts */
		              	//var nextPaginatedUrl = browserPathName.replace(/page-[0-9]+/, 'page-' + currentPageNo);
		              	if (/page-[0-9]+/.test(browserPathName)) {
		              		var nextPaginatedUrl = browserPathName.replace(/page-[0-9]+/, 'page-' + currentPageNo);
		              	}
		              	else {
		              		var nextPaginatedUrl = browserPathName.replace(/[/]$/,"") + '/page-' + currentPageNo;
		              	}
		              	//alert(nextPaginatedUrl);
		              	/* TISSPTEN-124 & TISSPTEN-123 ends */
	                    if (query != "?q=") {
		              		  ajaxUrl = ajaxUrl + query;
		              		//TISSPTEN-130 starts
		              		  if(/\?q=\?/.test(ajaxUrl)){
			              		ajaxUrl = ajaxUrl.replace("?q=?","?"); 
		                      }
		              		//TISSPTEN-130 ends
		              		  nextPaginatedUrl = nextPaginatedUrl + query;
	                      }
	                      window.history.replaceState({}, "", nextPaginatedUrl);
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
	//INC144318859 remove previous cache data | old cache issue
    if ($('#pageType').val() != 'productsearch' && $('#pageType').val() != 'product' && $('#pageType').val() != 'category') 
	{ 
	    //console.log("trying to remove previous cache data not for plp(category&productsearch) & pdp ");
	        var lastUrlpathNameprvdata = window.localStorage.getItem('lastUrlpathName');
                var lastUrlqueryprvdata    = window.localStorage.getItem('lastUrlquery');
		var htmlprvdata            = window.localStorage.getItem('productlazyarray');
		var lazyfrompdpprvdata     = window.localStorage.getItem("lazyfrompdp");
		if(lastUrlpathNameprvdata != null || lastUrlqueryprvdata != null || htmlprvdata != null || lazyfrompdpprvdata != null) 
		    {
			   // console.log("removed cache data");
        		window.localStorage.removeItem('lazyfrompdp');
          		window.localStorage.removeItem('productlazyarray');
          		window.localStorage.removeItem('lastUrlpathName');
          		window.localStorage.removeItem('lastUrlquery');
		    }	
				
	}	
    //lazy image load initialization
	if($('#pageType').val() == "productsearch" || $('#pageType').val() == "product" || $('#pageType').val() == "category" || $('input[name=customSku]').length){
	    $("img.lazy").lazyload();	
	}
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
    		//inital call to this function commented for starting the lazy load from page 2 UF-409
    		   //	getProductSetData();
    		initPageLoad = false;
    		directPaginatedLoad = false;
    	}
    }
        $(window).on('scroll', function() {
            if ($('.lazy-reached').length != 0) {
            	
            	var productItemArrayLength = $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,ul.product-listing.product-grid.lazy-grid-normal,ul.product-listing.product-grid.custom-sku').find('li.product-item').length;
            	if(productItemArrayLength > 16){
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
        			if(lazyPagePush){
        				lazyPushInitalPage();
        			}
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
//UF-409 -> added for ajax complete events to auto lazy load
$( document ).ajaxComplete(function( event, xhr, settings ) {
	if($('#pageType').val() == "productsearch" || $('#pageType').val() == "product" || $('#pageType').val() == "category" || $('input[name=customSku]').length){
		$("img.lazy").lazyload();
	}
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

//TISSPTEN-130 starts
//INC144315462 and INC144315104  
function sortReplaceStateCustomSku(url, browserPathName){
	 	//$('input[name=customSkuUrl]').val(url);
	 	if (/page-[0-9]+/.test(browserPathName)) {
      		var nextPaginatedUrl = browserPathName.replace(/page-[0-9]+/, 'page-1');
      	}
      	else {
      		var nextPaginatedUrl = browserPathName.replace(/[/]$/,"") + '/page-1';
      	}
      	window.history.replaceState({}, "", nextPaginatedUrl);
}
//TISSPTEN-130 ends

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
	var browserPathName = window.location.pathname;
	
	switch (item) {
	case 'relevance':
		if($('input[name=customSku]').length == 1){
			var lookId = $('input[name=customSkuCollectionId]').val();
			if($('#searchPageDeptHierTreeForm').serialize()!=""){
 				url = '/CustomSkuCollection/'+lookId+'?'+$('#searchPageDeptHierTreeForm').serialize()+'&sort=relevance';
 				browserPathName = browserPathName +'?'+$('#searchPageDeptHierTreeForm').serialize()+'&sort=relevance';
 			}
			//TISSPTEN-130 starts
			else if($('#categoryPageDeptHierTreeForm').serialize()!="") {
				url = '/CustomSkuCollection/'+lookId+'/page-1?'+$('#categoryPageDeptHierTreeForm').serialize()+'&sort=relevance';
 				browserPathName = browserPathName +'?'+$('#categoryPageDeptHierTreeForm').serialize()+'&sort=relevance';
			}
			//TISSPTEN-130 ends
			else{
 				url = '/CustomSkuCollection/'+lookId+'?q='+$('#js-site-search-input').val()+'&sort=relevance';
 				browserPathName = browserPathName +'?q='+$('#js-site-search-input').val()+'&sort=relevance';
 			}
			ajaxPLPLoad(url);
			//TISSPTEN-130
			sortReplaceStateCustomSku(url, browserPathName);
		}
		initPageLoad = true;
		break;
	case 'new':
		if($('input[name=customSku]').length == 1){
			var lookId = $('input[name=customSkuCollectionId]').val();
			if($('#searchPageDeptHierTreeForm').serialize()!=""){
 				url = '/CustomSkuCollection/'+lookId+'?'+$('#searchPageDeptHierTreeForm').serialize()+'&sort=new';
 				browserPathName = browserPathName +'?'+$('#searchPageDeptHierTreeForm').serialize()+'&sort=new';
 			}
			//TISSPTEN-130 starts
			else if($('#categoryPageDeptHierTreeForm').serialize()!="") {
				url = '/CustomSkuCollection/'+lookId+'/page-1?'+$('#categoryPageDeptHierTreeForm').serialize()+'&sort=new';
 				browserPathName = browserPathName +'?'+$('#categoryPageDeptHierTreeForm').serialize()+'&sort=new';
			}
			//TISSPTEN-130 ends
			else{
 				url = '/CustomSkuCollection/'+lookId+'?q='+$('#js-site-search-input').val()+'&sort=new';
 				browserPathName = browserPathName +'?q='+$('#js-site-search-input').val()+'&sort=new';
 			}
			ajaxPLPLoad(url);
			//TISSPTEN-130
			sortReplaceStateCustomSku(url, browserPathName);
		}
		initPageLoad = true;
		break;
	case 'discount':
		if($('input[name=customSku]').length == 1){
			var lookId = $('input[name=customSkuCollectionId]').val();
			if($('#searchPageDeptHierTreeForm').serialize()!=""){
 				url = '/CustomSkuCollection/'+lookId+'?'+$('#searchPageDeptHierTreeForm').serialize()+'&sort=isDiscountedPrice';
 				browserPathName = browserPathName +'?'+$('#searchPageDeptHierTreeForm').serialize()+'&sort=isDiscountedPrice';
 			}
			//TISSPTEN-130 starts
			else if($('#categoryPageDeptHierTreeForm').serialize()!="") {
				url = '/CustomSkuCollection/'+lookId+'/page-1?'+$('#categoryPageDeptHierTreeForm').serialize()+'&sort=isDiscountedPrice';
 				browserPathName = browserPathName +'?'+$('#categoryPageDeptHierTreeForm').serialize()+'&sort=isDiscountedPrice';
			}
			//TISSPTEN-130 ends
			else{
 				url = '/CustomSkuCollection/'+lookId+'?q='+$('#js-site-search-input').val()+'&sort=isDiscountedPrice';
 				browserPathName = browserPathName +'?q='+$('#js-site-search-input').val()+'&sort=isDiscountedPrice';
 			}
			ajaxPLPLoad(url);
			//TISSPTEN-130
			sortReplaceStateCustomSku(url, browserPathName);
		}
		initPageLoad = true;
		break;
	case 'low':
		if($('input[name=customSku]').length == 1){
			var lookId = $('input[name=customSkuCollectionId]').val();
			if($('#searchPageDeptHierTreeForm').serialize()!=""){
 				url = '/CustomSkuCollection/'+lookId+'/page-1?'+$('#searchPageDeptHierTreeForm').serialize()+'&sort=price-asc';
 				browserPathName = browserPathName +'?'+$('#searchPageDeptHierTreeForm').serialize()+'&sort=price-asc';
 			}
			//TISSPTEN-130 starts
			else if($('#categoryPageDeptHierTreeForm').serialize()!="") {
				url = '/CustomSkuCollection/'+lookId+'/page-1?'+$('#categoryPageDeptHierTreeForm').serialize()+'&sort=price-asc';
 				browserPathName = browserPathName +'?'+$('#categoryPageDeptHierTreeForm').serialize()+'&sort=price-asc';
			}
			//TISSPTEN-130 ends
			else{
 				url = '/CustomSkuCollection/'+lookId+'/page-1?q='+$('#js-site-search-input').val()+'&sort=price-asc';
 				browserPathName = browserPathName +'?q='+$('#js-site-search-input').val()+'&sort=price-asc';
 			}
			ajaxPLPLoad(url);
			//TISSPTEN-130
			sortReplaceStateCustomSku(url, browserPathName);
		}
		initPageLoad = true;
		break;
	case 'high':
		if($('input[name=customSku]').length == 1){
			var lookId = $('input[name=customSkuCollectionId]').val();
			if($('#searchPageDeptHierTreeForm').serialize()!=""){
 				url = '/CustomSkuCollection/'+lookId+'/page-1?'+$('#searchPageDeptHierTreeForm').serialize()+'&sort=price-desc';
 				browserPathName = browserPathName +'?'+$('#searchPageDeptHierTreeForm').serialize()+'&sort=price-desc';
 			}
			//TISSPTEN-130 starts
			else if($('#categoryPageDeptHierTreeForm').serialize()!="") {
				url = '/CustomSkuCollection/'+lookId+'/page-1?'+$('#categoryPageDeptHierTreeForm').serialize()+'&sort=price-desc';
 				browserPathName = browserPathName +'?'+$('#categoryPageDeptHierTreeForm').serialize()+'&sort=price-desc';
			}
			//TISSPTEN-130 ends
			else{
 				url = '/CustomSkuCollection/'+lookId+'/page-1?q='+$('#js-site-search-input').val()+'&sort=price-desc';
 				browserPathName = browserPathName +'?q='+$('#js-site-search-input').val()+'&sort=price-desc';
 			}
			ajaxPLPLoad(url);
			//TISSPTEN-130
			sortReplaceStateCustomSku(url, browserPathName);
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
