
$(document).ready(function(){
	
	//Added for tealium
	if($('#ia_site_page_id').val()=="homepage"){
		//Added for tealium
		   $.ajax({
		        url: ACC.config.encodedContextPath + "/getTealiumDataHome",
		        type: 'GET',
		        cache:false,
		        success: function(data) {
		           //console.log(data);
		           $('#tealiumHome').html(data);
		        }
		    });
	}
	//Tealium end
	
	
	
	if($('#ia_site_page_id').val()=="productDetails" || $('#ia_site_page_id').val()=="viewSellers"){
		//Added for tealium
		   $.ajax({
		        url: ACC.config.encodedContextPath + "/getTealiumDataProduct",
		        type: 'GET',
		        cache:false,
		        success: function(data) {
		     	          
		        var tealiumData="";
		        tealiumData+=',"product_unit_price":["'+$("#product_unit_price").val()+'"],';
		        tealiumData+='"site_section":"'+$("#site_section").val()+'",';
		        tealiumData+='"product_list_price":["'+$("#product_list_price").val()+'"],';
		        tealiumData+='"product_name":["'+$("#product_name").val()+'"],';
		        tealiumData+='"product_sku":["'+$("#product_sku").val()+'"],';
		        tealiumData+='"page_category_name":"'+$("#page_category_name").val()+'",';
		        tealiumData+='"page_section_name":"'+$("#page_section_name").val()+'",';
		        tealiumData+='"page_name":"'+$("#page_name").val()+'",';
		        tealiumData+='"product_id":["'+$("#product_id").val()+'"],';
		        tealiumData+='"page_subcategory_name":"'+$("#page_subcategory_name").val()+'",';
		        tealiumData+='"product_brand":["'+$("#product_brand").val()+'"],';
		        tealiumData+='"site_section_detail":"'+$("#site_section_detail").val()+'",';
		        tealiumData+='"product_category":["'+$("#product_category").val()+'"]}';
		        data=data.replace("}<TealiumScript>",tealiumData);
		        console.log(data);
		           $('#tealiumHome').html(data);
		        }
		    });
	}
});
