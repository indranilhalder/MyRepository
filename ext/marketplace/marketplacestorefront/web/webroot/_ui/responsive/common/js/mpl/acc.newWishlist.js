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
	$.ajax({
		url: requiredUrl,
		type: "GET",
		data: {"wishlistName":wishlistName},
		dataType : "json",
		cache: false,
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			window.location.href = ACC.config.encodedContextPath + "/my-account/wishList";
		},
		error : function(data) {
			alert("Some issues are there with Wishlist at this time. Please try later or contact out helpdesk");
			//console.log(data.responseText) 
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
});
function renameWishlist(newWishlistName) {
	var oldName = $("#editWishListOld").val();
	var requiredUrl = ACC.config.encodedContextPath+"/my-account/editParticularWishlistName";
	var dataString = "newWishlistName=" +newWishlistName+"&wishlistOldName=" +oldName;
	$.ajax({
		url: requiredUrl,
		type: "GET",
		data: dataString,
		dataType : "json",
		cache: false,
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			if(data == "success") {
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
		error : function(data) {
			alert("Some issues are there with Wishlist at this time. Please try later or contact out helpdesk");
			//console.log(data.responseText) 
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
		error : function(data) {
			alert("Some issues are there with Wishlist at this time. Please try later or contact out helpdesk");
			//console.log(data.responseText) 
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
			//END MSD
//			window.location.href = ACC.config.encodedContextPath + "/my-account/wishList";
			window.location.href = ACC.config.encodedContextPath + "/my-account/viewParticularWishlist?particularWishlist="+wishlistName;
		},
		error : function(data) {
			alert("Some issues are there with Delete Wishlist at this time. Please try later or contact out helpdesk");
			//console.log(data.responseText) 
		}
	});
}

$(document).ready(function() {

	
	$(document).on("keyup","#editWishList",function() {

		validateEnteredName('editWishList',"errRename");
	});
	$('#editWishList').blur(function() {
		validateEnteredName('editWishList',"errRename");
	});
});
function validateEnteredName(divId,errorDivId) {
	var value=$('#'+divId).val();
	var re = /^[ _a-zA-Z0-9_ ]*[ _a-zA-Z0-9_ ]+[ _a-zA-Z_ ]*$/i;
	var isValid = re.test(value);
	if (!isValid) {
		value = value.substring(0, value.length - 1);
		$("#"+errorDivId).html("<font color='#ff1c47'><b>*</b>Special charecters are not allowed</font>");
		$("#"+errorDivId).show().fadeOut(3000);
	}
	$('#'+divId).val(value);
} 