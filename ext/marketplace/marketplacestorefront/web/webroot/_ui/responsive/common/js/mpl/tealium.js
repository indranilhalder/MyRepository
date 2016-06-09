$(document).ready(
		function() {

			
			var pageType = $('#pageType').val();
			var pageName=$('#pageName').val();
			// Added for tealium
			if (pageType == "homepage") {
				// Added for tealium
				$
						.ajax({
							url : ACC.config.encodedContextPath
									+ "/getTealiumDataHome",
							type : 'GET',
							cache : false,
							success : function(data) {
								// console.log(data);
								$('#tealiumHome').html(data);
							}
						});
			}
			// Tealium end

			if (pageType == "product"
					|| pageType == "/sellersdetailpage") {
				// Added for tealium
				$.ajax({
					url : ACC.config.encodedContextPath
							+ "/getTealiumDataProduct",
					type : 'GET',
					cache : false,
					success : function(data) {

						var tealiumData = "";
						tealiumData += ',"product_unit_price":["'
								+ $("#product_unit_price").val() + '"],';
						tealiumData += '"site_section":"'
								+ $("#site_section").val() + '",';
						tealiumData += '"product_list_price":["'
								+ $("#product_list_price").val() + '"],';
						tealiumData += '"product_name":["'
								+ $("#product_name").val() + '"],';
						tealiumData += '"product_sku":["'
								+ $("#product_sku").val() + '"],';
						tealiumData += '"page_category_name":"'
								+ $("#page_category_name").val() + '",';
						tealiumData += '"page_section_name":"'
								+ $("#page_section_name").val() + '",';
						tealiumData += '"page_name":"' + $("#page_name").val()
								+ '",';
						tealiumData += '"product_id":["'
								+ $("#product_id").val() + '"],';
						tealiumData += '"page_subcategory_name":"'
								+ $("#page_subcategory_name").val() + '",';
						tealiumData += '"product_brand":["'
								+ $("#product_brand").val() + '"],';
						tealiumData += '"site_section_detail":"'
								+ $("#site_section_detail").val() + '",';
						tealiumData += '"product_category":["'
								+ $("#product_category").val() + '"]}';
						data = data.replace("}<TealiumScript>", tealiumData);
						// console.log(data);
						$('#tealiumHome').html(data);
					}
				});
			}

			// Generic Page Script
			
			if (pageType != 'homepage' && pageType != 'product'
					&& pageType != '/sellersdetailpage' && pageType != 'productsearch'
					&& pageType != 'category' && pageType != 'cart'
					&& pageType != 'multistepcheckoutsummary'
					&& pageType != 'profile' && pageType != 'wishlist'
					&& pageType != 'orderconfirmation'
					&& pageType != 'notfound'
					&& pageType != 'businesserrorfound'
					&& pageType != 'nonbusinesserrorfound'
					&& pageType != 'productsearch') {
				
				// Added for tealium
				$
						.ajax({
							url : ACC.config.encodedContextPath
									+ "/getTealiumDataGeneric",
							type : 'GET',
							data:'pageName='+pageName,
							cache : false,
							success : function(data) {
								// console.log(data);
								$('#tealiumHome').html(data);
							}
						});
			}
			// Tealium end
			
			// Added for tealium
			if (pageType == "category") {
				// Added for tealium
				$
						.ajax({
							url : ACC.config.encodedContextPath
									+ "/getTealiumDataCategory",
							type : 'GET',
							cache : false,
							success : function(data) {
								// console.log(data);
								var tealiumData = "";
								tealiumData += ',"page_category_name":"'
										+ $("#page_category_name").val() + '",';
								tealiumData += '"site_section":"'
										+ $("#site_section").val() + '",';
								tealiumData += '"page_name":"'
									+ $("#page_name").val() + '",';
								tealiumData += '"categoryId":"'
									+ $("#categoryId").val() + '"}';
								data = data.replace("}<TealiumScript>", tealiumData);
								$('#tealiumHome').html(data);
							}
						});
			}
			// Tealium end
			
			//Search
			if(pageType == "productsearch"){
				$
				.ajax({
					url : ACC.config.encodedContextPath
							+ "/getTealiumDataSearch",
					type : 'GET',
					cache : false,
					success : function(data) {
						// console.log(data);
						var tealiumData = "";
						tealiumData += ',"search_keyword":"'
								+ $("#search_keyword").val() + '",';
						tealiumData += '"searchCategory":"'
								+ $("#searchCategory").val() + '",';
						tealiumData += '"page_name":"'
							+ $("#page_name").val() + '",';
						tealiumData += '"search_results":"'
							+ $("#search_results").val() + '"}';
						data = data.replace("}<TealiumScript>", tealiumData);
						$('#tealiumHome').html(data);
					}
				});
				
			}

		
		});
