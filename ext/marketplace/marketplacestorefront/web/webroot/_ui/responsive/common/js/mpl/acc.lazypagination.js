var pageNoPagination = 1;
var totalNoOfPages = 0;
var initPageLoad = true;
var lazyPagePush = true;
var ajaxUrl = '';
var pageType = $('#pageType').val();
var isSerp = false;
var totalPageCountToShow = 10; 
//////SDI-4008//////
var directPaginatedLoad = true;
var recordsLoadedCount = 0;
if($(window).width() <= 410){
totalPageCountToShow = 2;
}
var settings = {
	    totalPages: $('input[name=noOfPages]').val(),
	    visiblePages: totalPageCountToShow,
	    next: 'Next',
	    prev: 'Prev',
	    href: false,
	    onPageClick: function (event, page) {
	    	event.preventDefault();
	    	//////SDI-4008//////
	    	if($('input[name=customSku]').length == 1){
	    		getProductSetDataCustomSku(page);
	    	}else{
	    		getProductSetData(page);	
	    	}
	    	$('html, body').animate({
	            scrollTop: 0
	        }, 100);
		$("#pageOf").text(page);    
	    }
}
//INC144315462 and INC144315104
function getProductSetDataCustomSku(pageNoPagination) {
	 	 
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
	              if (pageNoPagination <= totalNoOfPages) {
	            	if(facetAjaxUrl){
	              		//ajaxUrl = facetAjaxUrl.replace(/page-[0-9]+/, 'page-' + currentPageNo);
	            		//TISSPTEN-130 starts
	            		nextPaginatedUrl = facetAjaxUrl.replace(/page-[0-9]+/, 'page-' + pageNoPagination);
	            		var lookId = $('input[name=customSkuCollectionId]').val();
	            		ajaxUrl = '/CustomSkuCollection/'+lookId+'/page-'+pageNoPagination;
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
	              		ajaxUrl = pathName.replace(/page-[0-9]+/, 'page-' + pageNoPagination);
	              		/* TISSPTEN-124 & TISSPTEN-123 starts */
		              	//var nextPaginatedUrl = browserPathName.replace(/page-[0-9]+/, 'page-' + currentPageNo);
		              	if (/page-[0-9]+/.test(browserPathName)) {
		              		var nextPaginatedUrl = browserPathName.replace(/page-[0-9]+/, 'page-' + pageNoPagination);
		              	}
		              	else {
		              		var nextPaginatedUrl = browserPathName.replace(/[/]$/,"") + '/page-' + pageNoPagination;
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
$(document).ready(function(){

	//INC144318859 remove previous cache data | old cache issue
    if ($('#pageType').val() != 'productsearch' && $('#pageType').val() != 'product' && $('#pageType').val() != 'category') 
	{ 
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
   // toggleNextPrevButton();
  //set the total no of pages 
    totalNoOfPages = $('input[name=noOfPages]').val();
    totalNoOfPages == '' ? 0 : parseInt(totalNoOfPages);
    
$(window).on('scroll', function() { 
	var productItemArrayLength = $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,ul.product-listing.product-grid.lazy-grid-normal,ul.product-listing.product-grid.custom-sku').find('li.product-item').length;
	
    if(productItemArrayLength > 16){
    	lazyPushInitalPage();
    }
});








/*
var $lis = $(".pagination-block li").hide();
var size_li = $lis.length;
var x = totalPageCountToShow,
start = 0;
//$(".pageNoLi").removeAttr("style");
$lis.slice(0, totalPageCountToShow).show();*/

/*$(document).on('click', '.pageNo', function(e) {
	e.preventDefault();
    var clickedPageNo = parseInt($(this).text());
    $(".pageNo").removeClass("active");
    $(this).addClass("active");
    getProductSetData(clickedPageNo);
    $("#pageOf").text(clickedPageNo);
    if (start + x < size_li) {
        $lis.slice(start, start + x).hide();
        start += x;
        $lis.slice(start, start + x).show();
        getProductSetData($(nextPageNo).text());
        $(nextPageNo).find("a").addClass("active");
		$(nextPageNo).prev().find("a").removeClass("active");
		$("#pageOf").text($(nextPageNo).text());
    }
    $('html, body').animate({
        scrollTop: 0
  }, 100); 
    toggleNextPrevButton(); 
});*/


/*if($(".pagination-block li").find(".active").parent().index() == 1){
	$(".prev-page").hide();
}

$(".pageNo").removeClass("active");
if(!$(".pagination-block li").first().find(".pageNo").hasClass("active")){
	$(".pagination-block li").first().find(".pageNo").addClass("active");
}*/

/*$(document).on('click', '.next-block', function(e) {
	e.preventDefault();
	var nextPageNo = $(".pageNo.active").parent().next();
	if($(nextPageNo).text() && isLastPageNext()){
		getProductSetData($(nextPageNo).text());
		$(nextPageNo).find("a").addClass("active");
		$(nextPageNo).prev().find("a").removeClass("active");
		$("#pageOf").text($(nextPageNo).text());
	}else if (start + x < size_li) {
        $lis.slice(start, start + x).hide();
        start += x;
        $lis.slice(start, start + x).show();
        getProductSetData($(nextPageNo).text());
        $(nextPageNo).find("a").addClass("active");
		$(nextPageNo).prev().find("a").removeClass("active");
		$("#pageOf").text($(nextPageNo).text());
    }
	toggleNextPrevButton();
	$('html, body').animate({
        scrollTop: 0
    }, 100);
});*/

/*$(document).on('click', '.prev-block', function(e) {
	e.preventDefault();
	var nextPageNo = $(".pageNo.active").parent().prev();
	if($(nextPageNo).text() && isLastPagePrev()){
		getProductSetData($(nextPageNo).text());
		$(nextPageNo).find("a").addClass("active");
		$(nextPageNo).next().find("a").removeClass("active");
		$("#pageOf").text($(nextPageNo).text());
	}else if (start - x >= 0) {
        $lis.slice(start, start + x).hide();
        start -= x;
        $lis.slice(start, start + x).show();
        getProductSetData($(nextPageNo).text());
        $(nextPageNo).find("a").addClass("active");
		$(nextPageNo).next().find("a").removeClass("active");
		$("#pageOf").text($(nextPageNo).text());
    }
	toggleNextPrevButton();
	$('html, body').animate({
        scrollTop: 0
    }, 100);
});*/


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

$(document).on("click",".page-link",function(e){
	e.preventDefault();
});

});

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

//Added for custom sku
//INC144315462 and INC144315104
function sortCustomSku(this_data,drop_down){
	console.log(typeof(this_data));
	var item = $(this_data).attr('data-name');
	$('.sort').removeAttr('style');
	if(!drop_down){
		$(this_data).css('color', '#a9143c');
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
	$(".pagination-blocks").twbsPagination('destroy');
	$('.pagination-blocks').twbsPagination(settings);
}

function isLastPageNext(){
	
	return $(".pageNo.active").parent().next().is(":visible");
}

function isLastPagePrev(){
	
	return $(".pageNo.active").parent().prev().is(":visible");
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
		//$('.pagination-blocks').twbsPagination(settings);
	}
}

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
     	    gridHTML += '<li class="product-item">' + $(element).html() + '</li>';
     	 });
    }
   
    if (initPageLoad) { //TODO: duplicate loading prevention
        $('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,ul.product-listing.product-grid.lazy-grid-normal,ul.product-listing.product-grid.custom-sku').html(gridHTML).hide().fadeIn(500);
        initPageLoad = false;
        //TISSPTXI-21
        $("img.lazy").lazyload();
    } else {
    	$('ul.product-listing.product-grid.lazy-grid,ul.product-listing.product-grid.lazy-grid-facet,ul.product-list,ul.product-listing.product-grid.lazy-grid-normal,ul.product-listing.product-grid.custom-sku').html(gridHTML);
        $("img.lazy").lazyload();
    }
}

function getProductSetData(pageNoPagination) {

    	 var pathName = window.location.pathname;
    	 var query = window.location.search;
    	    
    	    //INC144316143
    	    if ($('#pageType').val() == 'productsearch' || $('#pageType').val() == 'category') {

    	        window.localStorage.setItem('lastUrlpathName',encodeURI(pathName));
    	        window.localStorage.setItem('lastUrlquery',encodeURI(query));

    	    }
        
        if(pageNoPagination <= totalNoOfPages){
        	
        	if(facetAjaxUrl){
        		ajaxUrl = facetAjaxUrl.replace(/page-[0-9]+/, 'page-' + pageNoPagination);
        		var sort = findGetParameter('sort');
        		if(sort){
        			ajaxUrl = ajaxUrl + '&sort='+ sort;
        		}
        		window.history.replaceState({}, "", ajaxUrl);
        	}else{
        		ajaxUrl = pathName.replace(/page-[0-9]+/, 'page-' + pageNoPagination);
        		var nextPaginatedAjaxUrl = pathName.replace(/page-[0-9]+/, 'page-' + pageNoPagination);
                if (query) {
                    ajaxUrl = ajaxUrl + query;
                    nextPaginatedAjaxUrl = nextPaginatedAjaxUrl + query;
                }
                window.history.replaceState({}, "", nextPaginatedAjaxUrl);
        	}
        	
        	ajaxPLPLoad(ajaxUrl);
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
            //TISPRDT-7541 $('ul.product-listing.product-grid.lazy-grid,ul.product-list').after('<p class="lazyLoadPagination" style="text-align: center;margin: 5px 0;font-size: 18px;">Loading...<span class="loaderDiv" style="vertical-align:middle;"><img src="' + staticHost + '/_ui/responsive/common/images/red_loader.gif" class="spinner"></span></p>');
            
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
				dtmSearchTags();
			}
			
			if($('#pageType').val() == "category" || $('#pageType').val() == "electronics"){
				populateFirstFiveProductsPlp();
				dtmSearchTags();
			}
            //ACC.quickview.bindToUiCarouselLink();
        }
    });
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
		}
		//SDI-810
		else if(pageType == 'product'){
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			if(url.indexOf('&') > -1){
				url = url+'&sort=relevance';
			} else {
				url = url+'sort=relevance';
			}
		}
		else{
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
		}
		//SDI-810
		else if(pageType == 'product'){
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			if(url.indexOf('&') > -1){
				url = url+'&sort=isProductNew';
			} else {
				url = url+'sort=isProductNew';
			}
		}
		else{
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
		}
		//SDI-810
		else if(pageType == 'product'){
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			if(url.indexOf('&') > -1){
				url = url+'&sort=isDiscountedPrice';
			} else {
				url = url+'sort=isDiscountedPrice';
			}
		}
		else{
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
		}
		//SDI-810
		else if(pageType == 'product'){
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			if(url.indexOf('&') > -1){
				url = url+'&sort=price-asc';
			} else {
				url = url+'sort=price-asc';
			}
		}
		else{
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
		}
		//SDI-810
		else if(pageType == 'product'){
			var url = $('#categoryPageDeptHierTreeForm').serialize();
			if(url.indexOf('&') > -1){
				url = url+'&sort=price-desc';
			} else {
				url = url+'sort=price-desc';
			}
		}
		else{
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
	$(".pagination-blocks").twbsPagination('destroy');
	$('.pagination-blocks').twbsPagination(settings);
}

function sortReplaceState(url){
	var nextPaginatedAjaxUrl = url.replace(/page-[0-9]+/, 'page-1');
	 window.history.replaceState({}, '', nextPaginatedAjaxUrl);
}
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

function initialPaginationDisplay(){
	
	var $lis = $(".pagination-block li").hide();
	var size_li = $lis.length;
	var x = totalPageCountToShow,
	start = 0;
	//$(".pageNoLi").removeAttr("style");
	$lis.slice(0, totalPageCountToShow).show();
	$(".pageNo").removeClass("active");
	if(!$(".pagination-block li").first().find(".pageNo").hasClass("active")){
		$(".pagination-block li").first().find(".pageNo").addClass("active");
	}
}

function toggleNextPrevButton(){
	
	if(totalNoOfPages == ($(".pagination-block li").find(".active").parent().index() + 1)){
		$(".next-block").hide();
	}else{
		$(".next-block").show();
	}
	
	if($(".pagination-block li").find(".active").parent().index() == 0){
		$(".prev-block").hide();
	}else{
		$(".prev-block").show();
	}
}


/*!
 * jQuery pagination plugin v1.4.1
 * http://esimakin.github.io/twbs-pagination/
 *
 * Copyright 2014-2016, Eugene Simakin
 * Released under Apache 2.0 license
 * http://apache.org/licenses/LICENSE-2.0.html
 */
 var initLoad = true;
(function ($, window, document, undefined) {

    'use strict';

    var old = $.fn.twbsPagination;

    // PROTOTYPE AND CONSTRUCTOR

    var TwbsPagination = function (element, options) {
        this.$element = $(element);
        this.options = $.extend({}, $.fn.twbsPagination.defaults, options);

        if (this.options.startPage < 1 || this.options.startPage > this.options.totalPages) {
            throw new Error('Start page option is incorrect');
        }

        this.options.totalPages = parseInt(this.options.totalPages);
        if (isNaN(this.options.totalPages)) {
            throw new Error('Total pages option is not correct!');
        }

        this.options.visiblePages = parseInt(this.options.visiblePages);
        if (isNaN(this.options.visiblePages)) {
            throw new Error('Visible pages option is not correct!');
        }

        if (this.options.onPageClick instanceof Function) {
            this.$element.first().on('page', this.options.onPageClick);
        }

        // hide if only one page exists
        if (this.options.hideOnlyOnePage && this.options.totalPages == 1) {
            this.$element.trigger('page', 1);
            return this;
        }

        if (this.options.totalPages < this.options.visiblePages) {
            this.options.visiblePages = this.options.totalPages;
        }

        if (this.options.href) {
            this.options.startPage = this.getPageFromQueryString();
            if (!this.options.startPage) {
                this.options.startPage = 1;
            }
        }

        var tagName = (typeof this.$element.prop === 'function') ?
            this.$element.prop('tagName') : this.$element.attr('tagName');

        if (tagName === 'UL') {
            this.$listContainer = this.$element;
        } else {
            this.$listContainer = $('<ul></ul>');
        }

        this.$listContainer.addClass(this.options.paginationClass);

        if (tagName !== 'UL') {
            this.$element.append(this.$listContainer);
        }

        if (this.options.initiateStartPageClick) {
            this.show(this.options.startPage);
        } else {
            this.currentPage = this.options.startPage;
            this.render(this.getPages(this.options.startPage));
            this.setupEvents();
        }

        return this;
    };

    TwbsPagination.prototype = {

        constructor: TwbsPagination,

        destroy: function () {
            this.$element.empty();
            this.$element.removeData('twbs-pagination');
            this.$element.off('page');

            return this;
        },

        show: function (page) {
            if (page < 1 || page > this.options.totalPages) {
                throw new Error('Page is incorrect.');
            }
            this.currentPage = page;

            this.render(this.getPages(page));
            this.setupEvents();

            this.$element.trigger('page', page);

            return this;
        },

        enable: function () {
            this.show(this.currentPage);
        },

        disable: function () {
            var _this = this;
            this.$listContainer.off('click').on('click', 'li', function (evt) {
                evt.preventDefault();
            });
            this.$listContainer.children().each(function () {
                var $this = $(this);
                if (!$this.hasClass(_this.options.activeClass)) {
                    $(this).addClass(_this.options.disabledClass);
                }
            });
        },

        buildListItems: function (pages) {
            var listItems = [];

            if (this.options.first && initLoad) {
                listItems.push(this.buildItem('first', 1));
            }

            if (this.options.prev && initLoad) {
                var prev = pages.currentPage > 1 ? pages.currentPage - 1 : this.options.loop ? this.options.totalPages  : 1;
                listItems.push(this.buildItem('prev', prev));
            }

           // $("#pagination li")
		   if(initLoad){
			for (var i = 0; i < $("#pagination li.block").length; i++) {
              //listItems.push(this.buildItem('page', pages.numeric[i]));
			  $($("#pagination li")[i]).data('page', pages.numeric[i]);
			  $($("#pagination li")[i]).data('page-type', 'page');
			  listItems.push($("#pagination li")[i]);
            }
		   }
			$("#pagination li.block").hide();
			
			for (var i = 0; i < pages.numeric.length; i++) {
				$($("#pagination li.block")[(pages.numeric[i]-1)]).show();
			}

            if (this.options.next && initLoad) {
                var next = pages.currentPage < this.options.totalPages ? pages.currentPage + 1 : this.options.loop ? 1 : this.options.totalPages;
                listItems.push(this.buildItem('next', next));
            }

            if (this.options.last && initLoad) {
                listItems.push(this.buildItem('last', this.options.totalPages));
            }
			initLoad = false;
			$("#pagination").find("li").removeClass("active");
            return listItems;
        },

        buildItem: function (type, page) {
            var $itemContainer = $('<li></li>'),
                $itemContent = $('<a></a>'),
                itemText = this.options[type] ? this.makeText(this.options[type], page) : page;

            $itemContainer.addClass(this.options[type + 'Class']);
            $itemContainer.data('page', page);
            $itemContainer.data('page-type', type);
            $itemContainer.append($itemContent.attr('href', this.makeHref(page)).addClass(this.options.anchorClass).html(itemText));

            return $itemContainer;
        },

        getPages: function (currentPage) {
            var pages = [];

            var half = Math.floor(this.options.visiblePages / 2);
            var start = currentPage - half + 1 - this.options.visiblePages % 2;
            var end = currentPage + half;

            // handle boundary case
            if (start <= 0) {
                start = 1;
                end = this.options.visiblePages;
            }
            if (end > this.options.totalPages) {
                start = this.options.totalPages - this.options.visiblePages + 1;
                end = this.options.totalPages;
            }

            var itPage = start;
            while (itPage <= end) {
                pages.push(itPage);
                itPage++;
            }

            return {"currentPage": currentPage, "numeric": pages};
        },

        render: function (pages) {
            var _this = this;
            //this.$listContainer.children().remove();
            var items = this.buildListItems(pages);
            $.each(items, function(key, item){
                _this.$listContainer.append(item);
            });

            this.$listContainer.children().each(function () {
                var $this = $(this),
                    pageType = $this.data('page-type');

                switch (pageType) {
                    case 'page':
                        if ($this.data('page') === pages.currentPage) {
                            $this.addClass(_this.options.activeClass);
                        }
                        break;
                    case 'first':
                            $this.toggleClass(_this.options.disabledClass, pages.currentPage === 1);
                        break;
                    case 'last':
                            $this.toggleClass(_this.options.disabledClass, pages.currentPage === _this.options.totalPages);
                        break;
                    case 'prev':
                            $this.toggleClass(_this.options.disabledClass, !_this.options.loop && pages.currentPage === 1);
                        break;
                    case 'next':
                            $this.toggleClass(_this.options.disabledClass,
                                !_this.options.loop && pages.currentPage === _this.options.totalPages);
                        break;
                    default:
                        break;
                }

            });
        },

        setupEvents: function () {
            var _this = this;
            this.$listContainer.off('click').on('click', 'li', function (evt) {
                var $this = $(this);
                if ($this.hasClass(_this.options.disabledClass) || $this.hasClass(_this.options.activeClass)) {
                    return false;
                }
                // Prevent click event if href is not set.
                !_this.options.href && evt.preventDefault();
				
				if($(this).find("a").hasClass("anchor")){
				var no = $(this).find("a").text();
                _this.show(parseInt(no));
				}else{
				_this.show(parseInt($this.data('page')));
				}
				
               // _this.show(parseInt($this.data('page')));
            });
        },

        makeHref: function (page) {
            return this.options.href ? this.generateQueryString(page) : "#";
        },

        makeText: function (text, page) {
            return text.replace(this.options.pageVariable, page)
                .replace(this.options.totalPagesVariable, this.options.totalPages)
        },
        getPageFromQueryString: function (searchStr) {
            var search = this.getSearchString(searchStr),
                regex = new RegExp(this.options.pageVariable + '(=([^&#]*)|&|#|$)'),
                page = regex.exec(search);
            if (!page || !page[2]) {
                return null;
            }
            page = decodeURIComponent(page[2]);
            page = parseInt(page);
            if (isNaN(page)) {
                return null;
            }
            return page;
        },
        generateQueryString: function (pageNumber, searchStr) {
            var search = this.getSearchString(searchStr),
                regex = new RegExp(this.options.pageVariable + '=*[^&#]*');
            if (!search) return '';
            return '?' + search.replace(regex, this.options.pageVariable + '=' + pageNumber);

        },
        getSearchString: function (searchStr) {
            var search = searchStr || window.location.search;
            if (search === '') {
                return null;
            }
            if (search.indexOf('?') === 0) search = search.substr(1);
            return search;
        },
        getCurrentPage: function () {
            return this.currentPage;
        }
    };

    // PLUGIN DEFINITION

    $.fn.twbsPagination = function (option) {
        var args = Array.prototype.slice.call(arguments, 1);
        var methodReturn;

        var $this = $(this);
        var data = $this.data('twbs-pagination');
        var options = typeof option === 'object' ? option : {};

        if (!data) $this.data('twbs-pagination', (data = new TwbsPagination(this, options) ));
        if (typeof option === 'string') methodReturn = data[ option ].apply(data, args);

        return ( methodReturn === undefined ) ? $this : methodReturn;
    };

    $.fn.twbsPagination.defaults = {
        totalPages: 1,
        startPage: 1,
        visiblePages: 5,
        initiateStartPageClick: true,
        hideOnlyOnePage: false,
        href: false,
        pageVariable: '{{page}}',
        totalPagesVariable: '{{total_pages}}',
        page: null,
        first: 'First',
        prev: 'Previous',
        next: 'Next',
        last: 'Last',
        loop: false,
        onPageClick: null,
        paginationClass: 'pagination',
        nextClass: 'page-item next',
        prevClass: 'page-item prev',
        lastClass: 'page-item last',
        firstClass: 'page-item first',
        pageClass: 'page-item',
        activeClass: 'active',
        disabledClass: 'disabled',
        anchorClass: 'page-link'
    };

    $.fn.twbsPagination.Constructor = TwbsPagination;

    $.fn.twbsPagination.noConflict = function () {
        $.fn.twbsPagination = old;
        return this;
    };

    $.fn.twbsPagination.version = "1.4.1";

})(window.jQuery, window, document);


/*$(window).on("resize",function(){
if($(window).width() <= 410){
$('.pagination-blocks').twbsPagination('destroy');
settings.visiblePages = 2;
$('.pagination-blocks').twbsPagination(settings);
}
});*/
