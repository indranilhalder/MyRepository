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
(function(e, d, a, f) {
    var b = e.fn.twbsPagination;
    var c = function(i, g) {
        this.$element = e(i);
        this.options = e.extend({}, e.fn.twbsPagination.defaults, g);
        if (this.options.startPage < 1 || this.options.startPage > this.options.totalPages) {
            throw new Error("Start page option is incorrect")
        }
        this.options.totalPages = parseInt(this.options.totalPages);
        if (isNaN(this.options.totalPages)) {
            throw new Error("Total pages option is not correct!")
        }
        this.options.visiblePages = parseInt(this.options.visiblePages);
        if (isNaN(this.options.visiblePages)) {
            throw new Error("Visible pages option is not correct!")
        }
        if (this.options.totalPages == 1) {
            return this
        }
        if (this.options.totalPages < this.options.visiblePages) {
            this.options.visiblePages = this.options.totalPages
        }
        if (this.options.onPageClick instanceof Function) {
            this.$element.first().on("page", this.options.onPageClick)
        }
        if (this.options.href) {
            this.options.startPage = this.getPageFromQueryString();
            if (!this.options.startPage) {
                this.options.startPage = 1
            }
        }
        var h = (typeof this.$element.prop === "function") ? this.$element.prop("tagName") : this.$element.attr("tagName");
        if (h === "UL") {
            this.$listContainer = this.$element
        } else {
            this.$listContainer = e("<ul></ul>")
        }
        this.$listContainer.addClass(this.options.paginationClass);
        if (h !== "UL") {
            this.$element.append(this.$listContainer)
        }
        if (this.options.initiateStartPageClick) {
            this.show(this.options.startPage)
        } else {
            this.render(this.getPages(this.options.startPage));
            this.setupEvents()
        }
        return this
    };
    c.prototype = {
        constructor: c,
        destroy: function() {
            this.$element.empty();
            this.$element.removeData("twbs-pagination");
            this.$element.off("page");
            return this
        },
        show: function(g) {
            if (g < 1 || g > this.options.totalPages) {
                throw new Error("Page is incorrect.")
            }
            this.currentPage = g;
            this.render(this.getPages(g));
            this.setupEvents();
            this.$element.trigger("page", g);
            return this
        },
        buildListItems: function(g) {
            var l = [];
            if (this.options.first) {
                l.push(this.buildItem("first", 1))
            }
            if (this.options.prev) {
                var k = g.currentPage > 1 ? g.currentPage - 1 : this.options.loop ? this.options.totalPages : 1;
                l.push(this.buildItem("prev", k))
            }
            for (var h = 0; h < g.numeric.length; h++) {
                l.push(this.buildItem("page", g.numeric[h]))
            }
            if (this.options.next) {
                var j = g.currentPage < this.options.totalPages ? g.currentPage + 1 : this.options.loop ? 1 : this.options.totalPages;
                l.push(this.buildItem("next", j))
            }
            if (this.options.last) {
                l.push(this.buildItem("last", this.options.totalPages))
            }
            return l
        },
        buildItem: function(i, j) {
            var k = e("<li></li>"),
                h = e("<a></a>"),
                g = this.options[i] ? this.makeText(this.options[i], j) : j;
            k.addClass(this.options[i + "Class"]);
            k.data("page", j);
            k.data("page-type", i);
            k.append(h.attr("href", (window.location.pathname).replace(/page-[0-9]+/, 'page-'+j)+this.makeHref(j)).addClass(this.options.anchorClass).html(g));
            return k
        },
        getPages: function(j) {
            var g = [];
            var k = Math.floor(this.options.visiblePages / 2);
            var l = j - k + 1 - this.options.visiblePages % 2;
            var h = j + k;
            if (l <= 0) {
                l = 1;
                h = this.options.visiblePages
            }
            if (h > this.options.totalPages) {
                l = this.options.totalPages - this.options.visiblePages + 1;
                h = this.options.totalPages
            }
            var i = l;
            while (i <= h) {
                g.push(i);
                i++
            }
            return {
                currentPage: j,
                numeric: g
            }
        },
        render: function(g) {
            var i = this;
            this.$listContainer.children().remove();
            var h = this.buildListItems(g);
            jQuery.each(h, function(j, k) {
                i.$listContainer.append(k)
            });
            this.$listContainer.children().each(function() {
                var k = e(this),
                    j = k.data("page-type");
                switch (j) {
                    case "page":
                        if (k.data("page") === g.currentPage) {
                            k.addClass(i.options.activeClass)
                        }
                        break;
                    case "first":
                        k.toggleClass(i.options.disabledClass, g.currentPage === 1);
                        break;
                    case "last":
                        k.toggleClass(i.options.disabledClass, g.currentPage === i.options.totalPages);
                        break;
                    case "prev":
                        k.toggleClass(i.options.disabledClass, !i.options.loop && g.currentPage === 1);
                        break;
                    case "next":
                        k.toggleClass(i.options.disabledClass, !i.options.loop && g.currentPage === i.options.totalPages);
                        break;
                    default:
                        break
                }
            })
        },
        setupEvents: function() {
            var g = this;
            this.$listContainer.off("click").on("click", "li", function(h) {
                var i = e(this);
                if (i.hasClass(g.options.disabledClass) || i.hasClass(g.options.activeClass)) {
                    return false
                }!g.options.href && h.preventDefault();
                g.show(parseInt(i.data("page")))
            })
        },
        makeHref: function(g) {
            return this.options.href ? this.generateQueryString(g) : "#"
        },
        makeText: function(h, g) {
            return h.replace(this.options.pageVariable, g).replace(this.options.totalPagesVariable, this.options.totalPages)
        },
        getPageFromQueryString: function(g) {
            var h = this.getSearchString(g),
                i = new RegExp(this.options.pageVariable + "(=([^&#]*)|&|#|$)"),
                j = i.exec(h);
            if (!j || !j[2]) {
                return null
            }
            j = decodeURIComponent(j[2]);
            j = parseInt(j);
            if (isNaN(j)) {
                return null
            }
            return j
        },
        generateQueryString: function(g, h) {
            var i = this.getSearchString(h),
                j = new RegExp(this.options.pageVariable + "=*[^&#]*");
            if (!i) {
                return ""
            }
            return "?" + i.replace(j, this.options.pageVariable + "=" + g)
        },
        getSearchString: function(g) {
            var h = g || d.location.search;
            if (h === "") {
                return null
            }
            if (h.indexOf("?") === 0) {
                h = h.substr(1)
            }
            return h
        }
    };
    e.fn.twbsPagination = function(i) {
        var h = Array.prototype.slice.call(arguments, 1);
        var k;
        var l = e(this);
        var j = l.data("twbs-pagination");
        var g = typeof i === "object" ? i : {};
        if (!j) {
            l.data("twbs-pagination", (j = new c(this, g)))
        }
        if (typeof i === "string") {
            k = j[i].apply(j, h)
        }
        return (k === f) ? l : k
    };
    e.fn.twbsPagination.defaults = {
        totalPages: 1,
        startPage: 1,
        visiblePages: 5,
        initiateStartPageClick: true,
        href: true,
        pageVariable: "{{page}}",
        totalPagesVariable: "{{total_pages}}",
        page: null,
        first: "First",
        prev: "Previous",
        next: "Next",
        last: "Last",
        loop: false,
        onPageClick: null,
        paginationClass: "pagination",
        nextClass: "page-item next",
        prevClass: "page-item prev",
        lastClass: "page-item last",
        firstClass: "page-item first",
        pageClass: "page-item",
        activeClass: "active",
        disabledClass: "disabled",
        anchorClass: "page-link"
    };
    e.fn.twbsPagination.Constructor = c;
    e.fn.twbsPagination.noConflict = function() {
        e.fn.twbsPagination = b;
        return this
    };
    e.fn.twbsPagination.version = "1.4"
})(window.jQuery, window, document);
$('.pagination-blocks').twbsPagination(settings);


/*$(window).on("resize",function(){
if($(window).width() <= 410){
$('.pagination-blocks').twbsPagination('destroy');
settings.visiblePages = 2;
$('.pagination-blocks').twbsPagination(settings);
}
});*/
