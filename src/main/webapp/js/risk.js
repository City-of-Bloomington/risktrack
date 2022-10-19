$(function() { 
		$("#emp_name").autocomplete({
				source: APPLICATION_URL+"EmployeeService?format=json",
				minLength: 2,
				dataType:"json",
				delay: 100,
				select: function( event, ui ) {
						if(ui.item){
								$("#emp_name").val(ui.item.fullname);
								$("#full_name_id").val(ui.item.fullname);
								$("#userid_id").val(ui.item.id);
								$("#phone_id").val(ui.item.phone);
								$("#title_id").val(ui.item.title);
						}
				}
		});
});		

$(function() { 
    $("#person_name").autocomplete({
	source: APPLICATION_URL+"PersonService?format=json",
	minLength: 2,
	dataType:"json",
	delay: 100,
	select: function( event, ui ) {
	    if(ui.item){
		$("#person_name").val(ui.item.lname);
		$("#fname").val(ui.item.fname);
		$("#id").val(ui.item.id);
		}
	    }
	});
});
function popwit(url, name) {
    if(typeof(popupWin) != "object" || popupWin.closed)  {
        popupWin =  window.open(url, name, 'top=200,left=200,height=400,width=400,toolbar=0,menubar=0,location=0');
    }
    else{
        popupWin.location.href = url;
    }
    if (window.focus) {popupWin.focus()}
    return false;
 }

function doRefresh(){
    document.getElementById("action2").value="Refresh";
    document.getElementById("form_id").submit();
}
$('#show_info_button').click(function() {
    $('#show_info').hide();
    $('#hide_info').show();
    return false;
});
$('#hide_info_button').click(function() {
    $('#show_info').show();
    $('#hide_info').hide();
    return false;
});

function windowOpener(url, name, args) {
    if(typeof(popupWin) != "object" || popupWin.closed)  {
        popupWin =  window.open(url, name, args);
    }
    else{
        popupWin.location.href = url;
    }
    setTimeout(function(){popupWin.focus();},1000);
 }

function verifyCancel() {
    var x = confirm("Are you sure you want to cancel this request");
    if(x){
        document.getElementById("form_id").submit();
    }
    return x;
 };

