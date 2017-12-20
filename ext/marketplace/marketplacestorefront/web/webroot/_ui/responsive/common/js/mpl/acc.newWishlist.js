ACC.newWishlist = {

		_autoload: [
		            "bindHelp",
		            "bindManageMyList",
		            "bindRenameWl"
		            ],

		            bindHelp: function(){
		            	$(document).on("click",".js-new-wishlist",function(e){
		            		e.preventDefault();
		            		
		            	})
		            },


		            bindManageMyList: function(){
		            	$(document).on("click",".js-manage-myList",function(e){
		            		e.preventDefault();
		            		var title = $(this).data("mylist");
		            		
		            	})
		            },
		            
		            bindRenameWl: function(){
		            	
		            	$(document).on("click",".js-rename-wishlist",function(e){
		            		e.preventDefault();
		            		
		            	});
		            	
		            }

};

$(document).on("click",".delete_wishlist",function(e){
	e.preventDefault();
	$("#delete_wishlistName").val($(this).parent().siblings('.title').find('span.wishlist-name').text());
	$(".particular-wishlist-name").html($("#delete_wishlistName").val());
});

$(document).on("click",".deleteWlConfirmation",function(e){
	e.preventDefault();
	deleteWishlist($("#delete_wishlistName").val());
});

function deleteWishlist(wishlistName){
	var requiredUrl = ACC.config.encodedContextPath+"/my-account/deleteWishlist";
	var dataString = "wishlistName=" +wishlistName;
	var isLuxury = $("#isLuxury").val();
	var wishName= wishlistName.replace(/ /g,'').toLowerCase();
	$.ajax({
		url: requiredUrl,
		type: "GET",
		data: {"wishlistName":wishlistName},
		dataType : "json",
		cache: false,
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			/*TPR-646*/
			utag.link({
				"link_obj" : this,
			    "link_text": 'manage_wishlist_delete',
			    "event_type": 'manage_wishlist_click',
			    "wishlist_name" : "" + wishName
			});
			/*TPR-646 Ends*/
			window.location.href = ACC.config.encodedContextPath + "/my-account/wishList?isLux="+isLuxury;
		},
		//error : function(data) {
			//alert("Some issues are there with Wishlist at this time. Please try later or contact out helpdesk");
			//console.log(data.responseText) 
		//}
		error: function (xhr, status, error) {
			if(status == "parsererror"){
				window.location.href = ACC.config.encodedContextPath + "/login";
			} else {
				alert("Some issues are there with Wishlist at this time. Please try later or contact out helpdesk");	
			}          
		}
	});
}

$(document).on("click",".rename_link",function(e){
	e.preventDefault();
	editWishlistName = ($(this).parent().find('#editWishList').val()).trim();
	//console.log(editWishlistName);
	renameWishlist(editWishlistName);
});
$(document).on("click",".js-rename-wishlist",function(e){
	e.preventDefault();
	$(".rename-input").html(null);
	//var wishName =$(this).prev('div').prev('span').text();
	
});
function renameWishlist(newWishlistName) {
	var oldName = $("#editWishListOld").val();
	var requiredUrl = ACC.config.encodedContextPath+"/my-account/editParticularWishlistName";
	var dataString = "newWishlistName=" +newWishlistName+"&wishlistOldName=" +oldName;
	var wishName= oldName.replace(/ /g,'').toLowerCase();
	$.ajax({
		url: requiredUrl,
		type: "GET",
		data: dataString,
		dataType : "json",
		cache: false,
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			if(data == "success") {
				/*TPR-646*/
				utag.link({
					"link_obj" : this,
				    "link_text": 'manage_wishlist_rename',
				    "event_type": 'manage_wishlist_click',
				    "wishlist_name" : "" + wishName
				});
				
				
				window.location.href = ACC.config.encodedContextPath + "/my-account/viewParticularWishlist?particularWishlist="+newWishlistName;
			}
			else if(data == "duplicate_wishlist_name") {
				$(".rename-input").css("display","block");
				$(".rename-input").html("Wishlist with this name already exists");
			}
			else {
				$(".rename-input").css("display","block");
				$(".rename-input").html("Your wishlist needs a name!");
			}
		},
		//error : function(data) {
			//alert("Some issues are there with Wishlist at this time. Please try later or contact out helpdesk");
			//console.log(data.responseText) 
		//}
		error: function (xhr, status, error) {
			if(status == "parsererror"){
				window.location.href = ACC.config.encodedContextPath + "/login";
			} else {
			 alert("Some issues are there with Wishlist at this time. Please try later or contact out helpdesk");	
			}           
		 }
	});
}


$(document).on("click",".js-new-wishlist,.create-newlist-link",function(e){
	e.preventDefault();
	$("#errorCreate").html(null);
	$("#newWishlistName").val('');
});


$(document).on("click",".create_wishlist",function(){
	newWishlistData =  ($(this).parent().find('#newWishlistName').val()).trim();
	var requiredUrl = ACC.config.encodedContextPath+"/my-account/createNewWishlistWP";
	var dataString = "newWishlistData=" +newWishlistData;
	
		$.ajax({
		url: requiredUrl,
		type: "GET",
		data: dataString,
		dataType : "json",
		cache: false,
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			if(data == "success") {
				
				window.location.href = ACC.config.encodedContextPath + "/my-account/viewParticularWishlist?particularWishlist="+newWishlistData;
			}else if(data == "duplicate_wishlist_name") {
				$("#errorCreate").css("display","block");
				$("#errorCreate").html("Wishlist with this name already exists");
			}
			else {
				$("#errorCreate").css("display","block");
				$("#errorCreate").html("Your wishlist needs a name!");
				
			}
		},
		error: function (xhr, status, error) {
			if(status == "parsererror"){
				window.location.href = ACC.config.encodedContextPath + "/login";
			} else {
			 alert("Some issues are there with Wishlist at this time. Please try later or contact out helpdesk");	
			}           
		 }
	});
});



function disp(start,end)
{
	for(var i=0;i<total;i++)
	{
	if(i>=start && i<end){
		$('#p_'+i).show();
	}
	else
	{
		$('#p_'+i).hide();
	}
	}

}

$(document).on("click",".sizeNotSpecified_wl",function(e){
	e.preventDefault();
	/*TPR-646*/
	var productCode = $(this).closest(".add_to_cart_wl_form").find("input[name='productCodePost']").val();
	if(typeof(utag) != "undefined"){
		utag.link({
		    "link_text": 'add_tobag_wishlist',
		    "event_type": 'add_tobag_wishlist',
		    "product_sku_wishlist" : "" + productCode
		});
	}
	/*TPR-646 ends*/
	$("#redirectsToPdp_Wl").val($(this).parent().siblings(".redirectsToPdp_Wl").val());
});

$(document).on("click",".redirectsToPdpPage",function(e){
	e.preventDefault();
	redirectsToPDP($("#redirectsToPdp_Wl").val());
});

function redirectsToPDP(producturl) {
	var url = ACC.config.encodedContextPath + producturl;
	window.location.href = url;
}

$(document).on("click",".remove_product_from_wl",function(e){
	e.preventDefault();
	
	$("#removeFrmWl_name").val($(this).parent().siblings('#particularWishlistName_wl').val());
	$("#removeFrmWl_productcode").val($(this).parent().siblings('#productCode_wl').val());
	$("#removeFrmWl_ussid").val($(this).parent().siblings('#ussid_wl').val());
	//For MSD
	$("#removeFrmWl_isMSDEnabled").val($(this).parent().siblings('#isMSDEnabled_wl').val());
	$("#removeFrmWl_isApparelExist").val($(this).parent().siblings('#isApparelExist_wl').val());
	$("#removeFrmWl_salesHierarchyCategoryMSD").val($(this).parent().siblings('#salesHierarchyCategoryMSD_wl').val());
	$("#removeFrmWl_productCodeForMSD").val($(this).parent().siblings('#productCodeForMSD_wl').val());
	$("#removeFrmWl_sppriceForMSD").val($(this).parent().siblings('#sppriceForMSD_wl').val());
	$("#removeFrmWl_moppriceForMSD").val($(this).parent().siblings('#moppriceForMSD_wl').val());
	$("#removeFrmWl_rootCategoryMSD").val($(this).parent().siblings('#rootCategoryMSD_wl').val());	
	//End MSD
	
});

$(document).on("click",".removeProductConfirmation",function(e){
	e.preventDefault();
	var wishlistName = $("#removeFrmWl_name").val();
	var productCode = $("#removeFrmWl_productcode").val();
	var ussid = $("#removeFrmWl_ussid").val();
	//for MSD
	var isMSDEnabled = $("#removeFrmWl_isMSDEnabled").val();
	var isApparelExist = $("#removeFrmWl_isApparelExist").val();
	var salesHierarchyCategoryMSD = $("#removeFrmWl_salesHierarchyCategoryMSD").val();
	var sppriceForMSD = $("#removeFrmWl_sppriceForMSD").val();
	var moppriceForMSD = $("#removeFrmWl_moppriceForMSD").val();
	var rootCategoryMSD = $("#removeFrmWl_rootCategoryMSD").val();
	var price;	
	//alert(sppriceForMSD + moppriceForMSD);
	if(typeof sppriceForMSD === 'undefined')
	{
	price = moppriceForMSD;
	}	
	else
	{
	price = sppriceForMSD;
	}
	
	if(typeof moppriceForMSD === 'undefined')
	{
	price = sppriceForMSD;
	}	
	else
	{
	price = moppriceForMSD
	}
	
	if(typeof isMSDEnabled === 'undefined')
	{
		isMSDEnabled = false;						
	}
	
	if(typeof isApparelExist === 'undefined')
	{
		isApparelExist = false;						
	}
	
	removeFromWishlist(wishlistName, productCode, ussid,isMSDEnabled,isApparelExist,rootCategoryMSD,salesHierarchyCategoryMSD,price,"INR");
});

function removeFromWishlist(wishlistName, productCode, ussid,isMSDEnabled,isApparelExist,rootCategoryMSD,catID,price,currency){	
	var requiredUrl = ACC.config.encodedContextPath+"/my-account/wishList/remove";
	var dataString = "wishlistName=" +wishlistName+ "&productCodeWl=" +productCode+ "&ussidWl=" +ussid;
	var isLuxury = $("#isLuxury").val();
	$.ajax({
		url: requiredUrl,
		type: "GET",
		data: dataString,
		dataType : "json",
		cache: false,
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			//FOR MSD
			if(Boolean(isMSDEnabled) && Boolean(isApparelExist) && (rootCategoryMSD == 'Clothing'))
			{
				try
				{
				track(['removeFromWishlist',productCode,catID,price,currency]);	
				//console.log("productCode:-"+productCode+"catID:-"+catID + "price:-" + price + "currency:-" + currency );
				}
				catch(err)
				{
					console.log('Error Adding trackers when remove from cart: '+err.message);					
				}
			}
			
			/*TPR-646 Changes*/
			if(typeof(utag) != "undefined"){
				utag.link({
			        "link_text": 'remove_from_wishlist',
			        "event_type": 'remove_from_wishlist',
			        "product_sku_wishlist": "" + productCode
			    });	
			}
		
			//END MSD
//			window.location.href = ACC.config.encodedContextPath + "/my-account/wishList";
			window.location.href = ACC.config.encodedContextPath + "/my-account/viewParticularWishlist?particularWishlist="+wishlistName+"&isLux="+isLuxury;
		},
		error: function (xhr, status, error) {
			if(status == "parsererror"){
				window.location.href = ACC.config.encodedContextPath + "/login";
			} else {

				 alert("Some issues are there with Wishlist at this time. Please try later or contact out helpdesk");	
			}
           
        }
	});
}

$(document).on("keypress",'#newWishlistName',function(e) {
	var wishlistname = $("#newWishlistName").val();
	var mainDiv = 'newWishlistName';
	var errorDiv = "#errorCreate";
	validateSpcharWlName(e,wishlistname,mainDiv,errorDiv);
});

function validateSpcharWlName(e,wishlistname,mainDiv,errorDiv){
	var key = e.keyCode;
	if((key>=33 && key<48) || (key>=58 && key<65) || (key>=91 && key<97)){
		e.preventDefault();
		 var startWl = document.getElementById(mainDiv).selectionStart;
         var endWl = document.getElementById(mainDiv).selectionEnd;
		$('#'+mainDiv).val(wishlistname);
		$(errorDiv).show();
		$(errorDiv).html("<font color='#ff1c47'><b>Special characters are not allowed</b></font>");
		$(errorDiv).show().fadeOut(3000);
		document.getElementById(mainDiv).setSelectionRange(startWl, endWl);
	} 
}
$(document).ready(function() {    
    $('#myWishlistHeader').click(function(evt) {    	
        evt.preventDefault();        
        window.location.href = $(this).attr('href');

    });

    $('#createNewList').on('show.bs.modal', function () {
	    $(".product-info .product-image-container .zoom").css("z-index","1");
	    $(".zoomContainer").css("z-index","1");

	}); 
    $('#createNewList').on('hidden.bs.modal', function () {
	    $(this).find("input,textarea,select").val('').end();
	    $(".product-info .product-image-container .zoom").css("z-index","10000");
	    $(".zoomContainer").css("z-index","9999");

	}); 

});


$(document).on("keypress","#editWishList",function(e) {
	var wishlistname = $("#editWishList").val();
	var mainDiv = 'editWishList';
	var errorDiv = "#errRename";
	validateSpcharWlName(e,wishlistname,mainDiv,errorDiv);
}); 


/*TPR-646 Changes*/
$(document).on("click",".js-manage-myList",function(e){
	e.preventDefault();
	utag.link({
		"link_obj" : this,
	    "link_text": 'manage_wishlist',
	    "event_type": 'manage_wishlist_click'
	});
});
$(document).on("click",".manageWishlistClose",function(e){
	e.preventDefault();
	utag.link({
		"link_obj" : this,
	    "link_text": 'manage_wishlist_cancel',
	    "event_type": 'manage_wishlist_click'
	});
});
