$(document).ready(function() {


let searchKeyword = document.getElementById("searchKeyword");
let doSearch = document.getElementById("doSearch");


doSearch.addEventListener("click", (e)=>{
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
			window.location.href = '/home/searchResult?searchKeyword='+keywordValue+'&totalCount='+data +'&page=1';
		}
	});
	}

});

});