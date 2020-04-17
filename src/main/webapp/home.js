/**
 * 
 */
if(typeof jQuery == "undefined")window.alert("Internet Not Connected. App may not Work Well");


$(document).ready(function () {
	console.log("jq");

	$("#traceCall").click(function (ev) {
		let val = $("#userInp").val();
		$.ajax({
			url: "/catalog/trace/" + val,
			type: "GET",
			success: function (response) {
				$("#result").val("Welcome "+response.userId +" !!! ");
				console.log("success");
				console.log(response);
			},
			error: function (response) {
				console.log("error");
				console.log(response);
			}
		});
	});


});