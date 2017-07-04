//校验单个字数
function checkLength(name,len,massage){
	var vName = $('input[name='+ name +']').val();
	if(vName.length>len){
		alertM(massage+'字数不能超过'+len+'!',{time:3000});
		return;
	}	
}