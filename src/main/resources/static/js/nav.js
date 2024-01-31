

$(document).ready(function() {


let searchKeyword = document.getElementById("searchKeyword");
let doSearch = document.getElementById("doSearch");
let notice = document.getElementById("notice");
let memberId = document.getElementById("memberId");

if(memberId != null){
	memberIdValue = memberId.value;
}

notice.addEventListener("click",(e)=>{
	e.preventDefault();
	console.log("눌림");
	window.location.href = '/notice/'+memberIdValue;
});

doSearch.addEventListener("click", (e)=>{
	e.preventDefault();
	console.log('눌림');
	let keywordValue = searchKeyword.value;
	console.log(keywordValue);
	if(keywordValue == ""){
		alert("검색어를 입력해주세요.");
		return false;
	}else{
		$.ajax({
		type : "POST",
		url : "/home/search/" + keywordValue,
		data : {
			keywordValue
		},success:function (data) {
			console.log(data);
			window.location.href = '/home/searchResult?searchKeyword='+keywordValue+'&totalCount='+data+'&page=1';
	}});
	}

});

});