$(document).ready(function() {


  $(document).on('click','.sort',function(){
        	sort($(this),false);
   });
        
        $(document).on('change','.responsiveSort',function(){
        	sort($(this).find(':selected'),true);
        });



function sort(this_data,drop_down){
	var item = $(this_data).attr('data-name');
	$('.sort').removeAttr('style');
	if(!drop_down){
		$(this_data).css('color', '#a5173c');
	}
	var pathName = window.location.pathname;
	var pageType = $('#pageType').val();
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

function ajaxPLPLoad(ajaxUrl){
	
    $.ajax({
        url: ajaxUrl,
        data: {
            pageSize: 24,
            /* q: '', */
            lazyInterface:'Y'
        },
        success: function(x) {
            var filtered = $.parseHTML(x);
            
            if($(filtered).has('.product-grid')){
            	$('.product-grid-wrapper').html("");
            	$(filtered).find('.product-grid').each(function() {
                	$('.product-grid-wrapper').append($(this));
            	});
            }           	
        }
    });
}





$(document).on("click",".add-to-wishlist",function(e){
	//addToWishlistForPLP($(this).data("product"),this);
	if ($(this).hasClass("added")){
		 removeToWishlistForPLP($(this).data("product"),this);
	} else {
		 addToWishlistForPLP($(this).data("product"),this);
	}
	return false;
})
	

		function removeToWishlistForPLP(productURL,el) {
			//var loggedIn=$("#loggedIn").val();
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
		    var dataString = 'wish=' + wishName + '&product=' + productCode
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
						$(this).removeClass("added");
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


		function addToWishlistForPLP(productURL,el) {
			//var loggedIn=$("#loggedIn").val();
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
		    var dataString = 'wish=' + wishName + '&product=' + productCode
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
						$(this).addClass("added");
						
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
		    return productCode.toUpperCase();
			
		}



});