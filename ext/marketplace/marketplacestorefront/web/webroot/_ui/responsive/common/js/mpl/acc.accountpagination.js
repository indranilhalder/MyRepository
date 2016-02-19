var i=0;
var pageCount=0;
var pagelimitAcc=10;
var totalItem=$('#accountAddressCount').val();
var noofpageCount=Math.ceil(totalItem/pagelimitAcc);
var start = 1;
var end = "";
var pageNo = 1;

$(document).ready(function (){

	// -----------*** TISPRO-48 --- Pagination with Lazy loading ***----------------
	var pageIndex = $('#pageIndex').val();
	var pagableSize = $('#pagableSize').val();
	var pageNum = parseInt(pageIndex)+1;
	var endCount = pageNum*pagableSize;
	var startCount = endCount-pagableSize+1;
	var totalNumberOfResults = $('#totalNumberOfResults').val();
	if(endCount>totalNumberOfResults){
		endCount = totalNumberOfResults;
	}
	var displayOrder = startCount+"-"+endCount+ " of " + totalNumberOfResults + " Orders";
	$("#displayPaginationCountUp").html(displayOrder);
	$("#ofPagination").html(displayOrder);
	//	alert("pageIndex : "+pageIndex+"\n"+"pagableSize : "+pagableSize+"\n"+"pageNum : "+pageNum+"\n"+"endCount : "+endCount+"\n"+"startCount : "+startCount);
	//	alert(startCount+"-"+endCount+ " of " + totalItemNew + " Orders");

	// -----------*** TISPRO-48 --- Pagination with Lazy loading ***----------------

	
	$(document).ready(function (){
		// -----------*** TISSRT-630 --- Pagination with Lazy loading ***----------------
		var pageIndexC = $('#pageIndexC').val();
		var pagableSizeC = $('#pagableSizeC').val();
		var pageNumC = parseInt(pageIndexC)+1;
		var endCountC = pageNumC*pagableSizeC;
		var startCountC = endCountC-pagableSizeC+1;
		var totalNumberOfResultsC = $('#totalNumberOfResultsC').val();
		if(endCountC>totalNumberOfResultsC){
			endCountC = totalNumberOfResultsC;
		}

		var displayCoupon = startCountC+"-"+endCountC+ " of " + totalNumberOfResultsC + " Coupons";
		$("#displayPaginationCountUpCoupon").html(displayCoupon);
	});
	
	
	
// [START]

	var divItem = "";
	for(var i=1; i<=noofpageCount; i++){
		divItem = divItem + "<a href='#nogo' onclick='pageNavigation("+i+")'>"+i+"</a>&emsp;";
	}
	$('#prev').html(divItem);
	$('.right-account #address_item #item_ul>li').each(function(i)  {
		$(this).attr('id', 'p_' + i);
		i=i + 1;
	});
	dispPageLimit(0,pagelimitAcc);
	
	
	for (var i = 1; i <= 2; i++) {
		if (i == 1) {
			divItem = divItem
			+ "<a href='#nogo' onclick='pageNavigation("
			+ i + ")' class='active'>" + i
			+ "</a>&emsp;";
		} else {
			divItem = divItem
			+ "<a href='#nogo' onclick='pageNavigation("
			+ i + ")'>" + i + "</a>&emsp;";
		}
		//divItem = divItem + "<a href='#nogo' onclick='pageNavigation("+i+")'>"+i+"</a>&emsp;";
	}
	divItem = divItem + "<a href='#nogo' onclick='nextAction(" + 0
	+ "," + 2 + ")' class='order-pagination-next'>Next</a>"

	$('#paginationDiv').html(divItem);
	$('#paginationDiv2').html(divItem);
	$('.right-account .order-history .pagination_ul>li').each(
			function(i) {
				$(this).attr('id', 'p_' + i);
				i = i + 1;
			});

	var start = 1;
	var end = "";
	var initStr = "";
	var endStr = "";
	
	if (totalItem > 1) {
		if(parseInt(pagelimitAcc) < parseInt(totalItem)){
			end = pagelimitAcc;
		}
		else{
			end = totalItem;
		}
		initStr = start + "-" + end;
		endStr = "s";
	} else {
		end = 1;
		initStr = "1";
		endStr = "";
	}
	var divOrder = "";
	divOrder = initStr + " of " + totalItem + " Order" + endStr;
//	$("#ofPagination").html(divOrder);
	$("#ofPaginationUp").html(divOrder);

	dispPageLimit(0, pagelimitAcc);
	//}

});
/*-------------- For account --------------*/
var divItem = "";
function pageNavigation(num) {
	pageNo = num;

	$('body').on("click",".address_pagination a",function(){
		$(".address_pagination a").removeClass("active");
		$(this).addClass("active");
	});

	var endIndex = (pageNo * pagelimitAcc);
	var startIndex = endIndex - pagelimitAcc;

	var start = startIndex + 1;
	var end = endIndex;
	if (pageNo == noofpageCount) {
		end = totalItem;
	}
	var divOrder = "";
	divOrder = start + "-" + end + " of " + totalItem + " Orders";
//	$("#ofPagination").html(divOrder);
	$("#ofPaginationUp").html(divOrder);
	dispPageLimit(startIndex, endIndex);
}


function nextAction(startNo, endNo) {
	//alert("Page No >>>>>>"+pageNo);
	if (pageNo == 1) {
		endNo = 2;
	}
	var endNoOfPagination = endNo + 1;
	pageNo = endNo;

	if (endNoOfPagination > noofpageCount) {
		endNoOfPagination = endNo;
	}
	//alert(startNo + "      " + endNo);
	if (noofpageCount >= 2) {
		divItem = "";

		divItem = "<a href='#nogo' onclick='prevAction(" + startNo + ","
		+ endNo + ")' class='order-pagination-prev'>Prev</a>&emsp;"
		for (var i = endNoOfPagination - 1; i <= endNoOfPagination; i++) {
			if (i == endNo) {
				divItem = divItem
				+ "<a href='#nogo' onclick='pageNavigation(" + i
				+ ")' class='active'>" + i + "</a>&emsp;";
			} else {
				divItem = divItem
				+ "<a href='#nogo' onclick='pageNavigation(" + i
				+ ")'>" + i + "</a>&emsp;";
			}
			//divItem = divItem + "<a href='#nogo' onclick='pageNavigation(" + i + ")'>" + i + "</a>&emsp;";
		}
		if (pageNo != noofpageCount) {
			divItem = divItem + "<a href='#nogo' onclick='nextAction("
			+ startNo + "," + endNoOfPagination + ")' class='order-pagination-next'>Next</a>";
		}
	}
	$('#paginationDiv').html(divItem);
	$('#paginationDiv2').html(divItem);
	pageNavigation(pageNo);
}

//for PREV button Action
function prevAction(startNo, endNo) {
	//alert("Page No >>>>>>"+pageNo);
	var endNoOfPagination = endNo + 1;
	if (endNoOfPagination > noofpageCount) {
		endNoOfPagination = endNo;
	}
	var newPrevEnd = pageNo - 1;
	if (newPrevEnd <= pagelimitAcc) {
		newPrevEnd = 2
	}
	var newPrevPageNo = pageNo - 1;

	//alert(startNo + "      " + endNo);
	//alert("new Prev End No:  >>>> "+newPrevEnd+"\n newPrevPageNo: "+newPrevPageNo);
	if (noofpageCount >= 2) {
		divItem = "";
		if (newPrevPageNo != 1) {
			divItem = "<a href='#nogo' onclick='prevAction(" + startNo
			+ "," + newPrevEnd + ")' class='order-pagination-prev'>Prev</a>&emsp;"
		}
		for (var i = newPrevEnd - 1; i <= newPrevEnd; i++) {
			if (i == newPrevPageNo) {
				divItem = divItem
				+ "<a href='#nogo' onclick='pageNavigation(" + i
				+ ")' class='active'>" + i + "</a>&emsp;";
			} else {
				divItem = divItem
				+ "<a href='#nogo' onclick='pageNavigation(" + i
				+ ")'>" + i + "</a>&emsp;";
			}
			//divItem = divItem + "<a href='#nogo' onclick='pageNavigation(" + i + ")'>" + i + "</a>&emsp;";
		}
		divItem = divItem + "<a href='#nogo' onclick='nextAction("
		+ startNo + "," + endNoOfPagination + ")' class='order-pagination-next'>Next</a>";
	}
	$('#paginationDiv').html(divItem);
	$('#paginationDiv2').html(divItem);
	pageNavigation(newPrevPageNo);
} 
// Pagination ***********************************


function nextAcc()
{
	if(pageCount<noofpageCount-1)
	{
		pageCount++;
		dispPageLimit(pageCount*pagelimitAcc,(pageCount+1)*pagelimitAcc);
	}
}

function prevAcc()
{
	if(pageCount>0)
	{
		pageCount=pageCount-1;
		dispPageLimit(pageCount*pagelimitAcc,(pageCount+1)*pagelimitAcc);
	}
}
// [END]


//For Address Book Pagination
function dispPageLimit(start,end)
{
	if(undefined !=totalItem){
		for(var i=0;i<totalItem;i++)
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
}


function pageNavigation(num){
	var index = num;
	var endIndex = (index*pagelimitAcc);
	var startIndex = endIndex - pagelimitAcc;
	dispPageLimit(startIndex,endIndex);
}

//For Address Book Pagination
