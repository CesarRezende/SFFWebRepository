$(document).ready(function(){
	
	
});
function DefaultCurrencyToRealFormat(number) {
	var p = number.toFixed(2).split(".");
	var formatNumber = p[0].split("").reverse().reduce(function (acc, number, i, orig) {
		return number + (i && !(i % 3) ? "." : "") + acc;
	}, "") + "," + p[1];
	
	
	formatNumber = formatNumber.replace("-.", "-");
	
	if(formatNumber.charAt(0) == '.')
		formatNumber = formatNumber.substring(1);
	
	return formatNumber;

} 
function loadJQueryDashboard(){
	
		 
}