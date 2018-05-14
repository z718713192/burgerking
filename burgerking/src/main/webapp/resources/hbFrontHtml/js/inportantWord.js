var wordName = "wordfile";
var j = 1;
function addWordInput() {

	$("input[name='" + wordName + "']").each(function(j, item) {
		if (item.value == "") {
			$(this).parent().parent().remove();
		}
	});
	var attach ="";
	if (j > 0) {
		attach = wordName + j;
		createWInput(attach, wordName)
		j = j + 1;
	}
	$("#" + attach + "").attr("onchange", "wfileChange(this)")
	$("#" + attach + "").click();
}

function wfileChange(id) {
	var a = 0;
	$("input[name='" + wordName + "']").each(
			function(j, item) {
				if (item.value == "") {
					$(this).parent().parent().remove();
				}
				var fileType = $(this).val().substring(
						$(this).val().lastIndexOf(".")).toLowerCase();
				if (fileType != '.doc' && fileType != '.docx') {
					a++;
					$(this).parent().parent().remove();
				}
				$(this).attr("disabled", "disabled")
			});
	if (a == 0) {
		WselectFile();
	} else {
		alert("请选择word文件");
	}
}
//,
function createWInput(id, name) {
	var html = "<div  class='col-md-12' style='height:35px;line-height:35px;'><div  class='col-md-1'><input type='checkbox' name='wordCheck'  value='"+id+"'  /></div>"
			+ "<div  class='col-md-8'><input name='"
			+ name
			+ "' id='"
			+ id
			+ "' type='file' size='50' accept='.doc,.docx' onchange='fileChange(this)'  /></div><div>";
	$("#Wupload").append(html);
}

function WselectFile() {
//	$("#ul").find("li").eq(2).hide(0);
	$("#WselectFile").show(0);
	$("#fileImport").hide(0);
	ulliNext(2, 1, 0, 0);
}

function WselectFilePrev() {
//	$("#ul").find("li").eq(2).show(0);
	$("#WselectFile").hide(0);
	$("#fileImport").show(0);
	ulliPrev(2, 1, 1, 0, 0);
}
function WselectFileNext() {
	var id = '';
	var obj=document.getElementsByName('wordCheck');
	console.log(obj);
	for(var i=0; i<obj.length; i++){
		if(obj[i].checked && obj[i].disabled==false){
				id+=obj[i].value+',';
				obj[i].disabled=true;
		} 
	}
	if(id == null || id == ""){
		alert("请选择上传文件！");
		return;
	}
	id=id.substring(0,id.length-1);
	var ids=id.split(',');
	for(var i=0; i<ids.length; i++){
		if(document.getElementById(ids[i]).value.indexOf("_")>0){
			document.getElementById(ids[i]).disabled=false;
		}else{
			for(var i=0; i<obj.length; i++){
				if(obj[i].checked){
						obj[i].disabled=false;
				} 
			}
			alert("请上传正确格式的word文件");
			return false;
		}
	}
	var fd = new FormData($('#wuploadForm')[0]);
	$.ajax({
		url : '/burgerking/client/wordFileUpload',
		type : 'POST',
		data : fd,
		async : false,
		cache : false,
		processData: false,
		contentType: false,
		success : function(data){
			if(data.res=='success'){				
				$.ajax({
					url : '/burgerking/client/saveWordData',
					type : 'POST',
					data : {'fileName':data.fileName,'month':$("#month").val()},
					async : false,
					cache : false,
					dataType: "json",
			        contentType:"application/x-www-form-urlencoded; charset=utf-8",
					success : function(res){
						if(res.res=='success'){
//							wpdfTableShow();
							WsealTableShow();
							WtimeOut();
							$("#WselectFile").hide(0);
							$("#We-seal").show(0);
							ulliNext(3, 2, 1, 1);
						}else{
							alert("录入失败！"+res.msg);
							return;
						}
					}
				});			
			}else{
				alert("上传失败,"+data.msg);
				return;
			}
		}
	});
	for(var i=0; i<ids.length; i++){
		document.getElementById(ids[i]).disabled=true;
	}
}



function WtimeOut(){
	var countdown =90;
	$("#WesealNext").attr("disabled","disabled");
	$("#WesealNext").css("background-color","#bdbdbd");
 	var s=setInterval(function(){
 		if (countdown == 0) { 
 			$("#WesealNext").removeAttr("disabled"); 
 			$("#WesealNext").css("background-color","#E0592B");
 			$("#WesealNext").html("下一步");
 			clearInterval(s);
 		}else{
 			$("#WesealNext").html(countdown);
 		}
 		countdown--; 
	},1000); 
}


function wpdfTableShow(){
	$.ajax({
		type: "GET",
        url: "/burgerking/client/findPDFWordDatas",
        data: {
        	"companyId":$("#companyId").val(),
        	"month":$("#month").val()
        },
        dataType: "json",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
	    cache : false,
	    async : false,
        success: function(data){
        	var  html='';
        	 for (var i = 0; i < data.dataList.length; i++) {
//        		 var selectHtml = "";
//        		 if(data.dataList[i][4] != null && data.dataList[i][4].length > 0){
//	          		   var esealArray = data.dataList[i][4].split(":");
//	          		   selectHtml="<select id='esealId"+data.dataList[i][0]+"' style='max-width: 180px' >";
//	          		   selectHtml+="<option value=''>---请选择---</option>"
//		            	   for (var j = 0; j < esealArry.length; j++) {
//		            		   selectHtml+="<option value='"+esealArray[j].split("|")[0]+"'>"+esealArray[j].split("|")[1]+"</option>";
//						   }
//		            	   selectHtml+="</select>";
//	        	 }
        		 html+="<tr id='"+data.dataList[i][0]+"'>" +
        		 		"<td  style='text-align: left;'width='50%'><input type='checkbox' name='WpdfFileName'  value='"+data.dataList[i][0]+"' />"+data.dataList[i][1]+"</td>" +
        		 	  "</tr>" ;
			}
        	 document.getElementById("WchengePDFTable").innerHTML = html;
        	 goPage(1,10,"WchengePDFTable","WchengePDFTablePageId");
        }
 });
}

function WchengePDFPrev() {
	$("#WchengePDF").hide(0);
	$("#We-seal").show(0);
	ulliPrev(4, 3, 3, 2, 2);
}
function WchengePDFNext() {
	var id = '';
	var obj=document.getElementsByName('WpdfFileName');
	console.log(obj);
//	for(var i=0; i<obj.length; i++){
//		if(obj[i].checked) id+=obj[i].value+',';
//	}
	for(var i=0; i<obj.length; i++){
		if(obj[i].checked && obj[i].disabled==false){
			id+=obj[i].value+',';
			obj[i].disabled=true;
		} 
	}
	if(id == null || id == ""){
		alert("请选择要转pdf的文件");
		return;
	}
	id=id.substring(0,id.length-1);
	$.ajax({
		url : '/burgerking/client/WchangePDF',
		type : 'POST',
		data : {"fileId":id},
		dataType: "json",
		contentType:"application/x-www-form-urlencoded; charset=utf-8",
		cache : false,
		async : false,
		success: function(data){
			if(data.res){
				$("#WchengePDF").hide(0);
				$("#WsendEmail").show(0);
				ulliNext(5, 4, 3, 3);
				WemailTableShow();	
			}else{
				 
			}
		}
	});
}

function WemailTableShow(){
	$.ajax({
		type: "GET",
        url: "/burgerking/client/findOlreadyWordToPDF",
        data: {
        	"companyId":$("#companyId").val(),
        	"month":$("#month").val()
        },
        dataType: "json",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
	    cache : false,
	    async : false,
        success: function(data){
        	var  html='';
        	 for (var i = 0; i < data.dataList.length; i++) {
//        		 var selectHtml = "";
//        		 if(data.dataList[i][4] != null && data.dataList[i][4].length > 0){
//	          		   var esealArray = data.dataList[i][4].split(":");
//	          		   selectHtml="<select id='esealId"+data.dataList[i][0]+"' style='max-width: 180px' >";
//	          		   selectHtml+="<option value=''>---请选择---</option>"
//		            	   for (var j = 0; j < esealArry.length; j++) {
//		            		   selectHtml+="<option value='"+esealArray[j].split("|")[0]+"'>"+esealArray[j].split("|")[1]+"</option>";
//						   }
//		            	   selectHtml+="</select>";
//	        	 }
        		 html+="<tr id='"+data.dataList[i][0]+"'>" +
        		 		"<td  style='text-align: left;'width='50%'><input type='checkbox' name='WsendEmailName'  value='"+data.dataList[i][0]+"' />"+data.dataList[i][1]+"</td>" +
	        		 	"<td  style='text-align: center;' width='50%' >" +
			 				"<div>"+data.dataList[i][4]+"</div>" +
        		 		" </td>" +
        		 	  "</tr>" ;
			}
        	 document.getElementById("WsendEmailTable").innerHTML = html;
        	 goPage(1,10,"WsendEmailTable","WsendEmailTablePageId");
        }
	});
}

function WsendEmailPrev() {
	
	$("#WsendEmail").hide(0);
	$("#WchengePDF").show(0);
	ulliPrev(5, 4, 4, 3, 3);
}
function WsendEmails() {
	var id = '';
	var obj=document.getElementsByName('WsendEmailName');
	console.log(obj);
//	for(var i=0; i<obj.length; i++){
//		if(obj[i].checked) id+=obj[i].value+',';
//	}
	for(var i=0; i<obj.length; i++){
		if(obj[i].checked && obj[i].disabled==false){
			id+=obj[i].value+',';
			obj[i].disabled=true;
		} 
	}
	if(id == null || id == ""){
		alert("请选择要发送文件");
		return;
	}
	id=id.substring(0,id.length-1);
	$.ajax({
		url : '/burgerking/client/WWsendEmail',
		type : 'POST',
		data : {"fileId":id},
		dataType: "json",
		contentType:"application/x-www-form-urlencoded; charset=utf-8",
		cache : false,
		async : false,
		success: function(data){
			if(data.res){
				$("#WsendEmail").hide(0);
				$("#WfileTipc").show(0);
				ulliNext(6, 5, 4, 4);
				WfileTipcShow();	
			}else{
				 
			}
		}
	});
	
}

function  WfileTipcShow(){
	$.ajax({
		type: "GET",
        url: "/burgerking/client/findOlreadyPDFtoEmail",
        data: {
        	"companyId":$("#companyId").val(),
        	"month":$("#month").val()
        },
        dataType: "json",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
	    cache : false,
	    async : false,
        success: function(data){
        	var  html='';
        	var WfileTipc="WfileTipc";
        	 for (var i = 0; i < data.dataList.length; i++) {
        		 html+="<tr id='"+data.dataList[i][0]+"'>" +
 		 		"<td  style='text-align: left;' width='20%'><div class='divtable' title='"+data.dataList[i][1]+"' ><input type='checkbox' name='WfileTipcName'  value='"+(data.dataList[i][1].split('.'))[0]+"' />"+data.dataList[i][1]+"</div></td>" +
 		 		"<td  style='text-align: center;' width='10%'>"+data.dataList[i][2]+"</td>" +
 		 		"<td  style='text-align: center;' width='10%'><div style='overflow: hidden; text-overflow: ellipsis; white-space: nowrap;width:50px;' title='"+data.dataList[i][3]+"'>"+data.dataList[i][3]+"</div></td>" +
 		 		"<td  style='text-align: center;' width='20%' ><button  value='"+(data.dataList[i][1].split('.'))[0]+"' onclick='lookPdf(this.value,\""+WfileTipc+"\")' >"+(data.dataList[i][1].split('.'))[0]+".pdf</button></td>" +
 		 		"<td  style='text-align: center;' width='20%'>" +
 		 			"<input type='hidden' id='WdefaultEmail"+(data.dataList[i][1].split('.'))[0]+"' value='"+data.dataList[i][4]+"' />" +
 		 			"<div class='divtable' title='"+data.dataList[i][4]+"'>"+data.dataList[i][4]+"</div></td>" +
 		 		"<td  style='text-align: center;' width='20%'  onclick='showWmoreEmail(\""+(data.dataList[i][1].split('.'))[0]+"\","+data.dataList[i][2]+")'>" +
		 				"<input type='hidden' value='' name='moreEmail' id='WmoreEmail"+(data.dataList[i][1].split('.'))[0]+"' />" +
		 				"<div id='"+(data.dataList[i][1].split('.'))[0]+"WmoreEmail' class='divtable'>点击选择</div>" +
		 			"</td>" +
 		 		"</tr>" ;
			}
        	 document.getElementById("WfileTipcTable").innerHTML = html;
        	 goPage(1,10,"WfileTipcTable","WfileTipcTablePageId");
        }
	});
}


function showWmoreEmail(id,sa){
	$('#WmoreEamilModal').modal({backdrop: 'static', keyboard: false});
	$("#WmoreEamilModalButton").attr("value",id);
	 $.ajax({
			type: "GET",
	        url: "/burgerking/sys/findSupplierByStoreId",
	        data: {
	        	"id":sa
	        },
	        dataType: "json",
	        contentType:"application/x-www-form-urlencoded; charset=utf-8",
		    cache : false,
		    async : false,
	        success: function(data){
	        	if(data.code==400){
	        		location.href = "/burgerking/sys/hbLogin";
	        	}else{
	        		var html="";
	        		if(data.supplier.length > 0){
	        			var emailArray="";
	        			for (var i = 0; i < data.supplier.length; i++) {
	        				if($.trim(data.supplier[i].email)!=""){
	        					emailArray+= data.supplier[i].email+"，";
	        				}
						}
	        			emailArray=emailArray.substring(0,emailArray.length-1);
	        			emailArray=emailArray.split("，");
	        			for (var i = 0; i < emailArray.length; i++) {
		 					html+="<div><input type='checkbox' name='WmoreEmailMode' value='"+emailArray[i]+"' />"+emailArray[i]+"</div>";
						}
	        		}
	 				 
	 				$("#WmoreEamilModalBody").html(html); 
	        	}
	        }
	 });
	 var as= $("#"+id+"WmoreEmail").html().split(',');
	 for (var i = 0; i < as.length; i++) {
		$("input[name='WmoreEmailMode']").each(function(){
			if(as[i]==$(this).val()){
				$(this).prop("checked","true");
			}
		});
	}
	  
}

function WcheckedEmail(){
	
	var html="";
	$("input[name='WmoreEmailMode']").each(function(){
		var checked=  $(this).prop("checked");
		if(checked){
			html+=  $(this).val()+"，";
		}
	})
	html=html.substring(0,html.length-1);
	$("#"+$('#WmoreEamilModalButton').attr('value')+"WmoreEmail").html(html);
	$("#"+$('#WmoreEamilModalButton').attr('value')+"WmoreEmail").attr("title",html);
	$("#"+$('#WmoreEamilModalButton').attr('value')+"WmoreEmail").prev("input").val(html);
	$('#WmoreEamilModal').modal('hide');
}
function WsealTableShow(){
	$.ajax({
		type: "GET",
        url: "/burgerking/client/findSealWordDatas",
        data: {
        	"companyId":$("#companyId").val(),
        	"month":$("#month").val()
        },
        dataType: "json",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
	    cache : false,
	    async : false,
        success: function(data){
        	var  html='';
        	 for (var i = 0; i < data.dataList.length; i++) {
        		 var selectHtml = "";
        		 var existEseal = "false";
        		 if(data.dataList[i][4] != null && data.dataList[i][4].length > 0){
	          		   var esealArray = data.dataList[i][4].split(":");
	          		   selectHtml="<select id='WesealId"+data.dataList[i][0]+"' style='border: none; scrollbar-arrow-color: #FFCC00; border-collapse: collapse;width:80%' >";
	          		   //selectHtml+="<option value=''>-未找到财务章,请选择-</option>"
		            	   for (var j = 0; j < esealArray.length; j++) {
		            		   var esealName = esealArray[j].split("|")[1];
		            		   if(esealName.indexOf('财务')<0){
		            			   selectHtml+="<option value='"+esealArray[j].split("|")[0]+"'>"+esealName+"</option>";
		            		   }else{
		            			   existEseal = "true";
		            			   selectHtml+="<option selected='selected' value='"+esealArray[j].split("|")[0]+"'>"+esealName+"</option>";
		            		   }
						   }
		            	   if(existEseal=="false"){
		            		   selectHtml+="<option selected='selected' value=''>-未找到财务章,请选择-</option>"
		            	   }
		            	   selectHtml+="</select>";
	        	 }
        		 html+="<tr id='"+data.dataList[i][0]+"'>" +
        		 		"<td  style='text-align: left;' width='50%'><div class='divtable' title='"+data.dataList[i][1]+"' ><input type='checkbox' name='WesealFileName'  value='"+data.dataList[i][0]+"' />"+data.dataList[i][1]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='50%'><div>"+selectHtml+"</div></td>" +
        		 	  "</tr>" ;
			}
        	 document.getElementById("We-sealTable").innerHTML = html;
        	 goPage(1,10,"We-sealTable","We-sealTablePageId");
        }
 });
}

function WsealCheckedFileAll(name){
	var EesealFileName=  document.getElementsByName(name);
	for (var i = 0; i < EesealFileName.length; i++) {
		if(EesealFileName[i].disabled!=true){
			EesealFileName[i].checked=true;
		}
	}
}
function WsealCheckedFileNotAll(name){
	var EesealFileName=  document.getElementsByName(name);
	for (var i = 0; i < EesealFileName.length; i++) {
		if(EesealFileName[i].disabled!=true){
			EesealFileName[i].checked=false;
		}
	}
}

function WesealPrev() {
	$("#We-seal").hide(0);
	$("#WselectFile").show(0);
	ulliPrev(3, 2, 2, 1, 1);
}

function WesealNext() {
	var chkValue = new Array();
	
//	$('input[name="WesealFileName"]:checked').each(function(){
//		chkValue.push($(this).val());
//	});
	var obj=document.getElementsByName('WesealFileName');
	for(var i=0; i<obj.length; i++){
		if(obj[i].checked && obj[i].disabled==false){
			chkValue.push(obj[i].value);
			obj[i].disabled=true;
		} 
	}
	if(chkValue.length == 0){
		alert("请选择需要盖章的报表！");
		return;
	}
	var chkEsealValue = new Array();
	for(var i=0; i<chkValue.length; i++){
//		alert(chkValue[i]);
//		alert($("#esealId"+chkValue[i]).val());
		var v1 = chkValue[i];
		var v2 = $("#WesealId"+chkValue[i]).val();
		if(v2 == null || v2 == ""){
			alert("请选择相应印章！");
			return;
		}
		chkEsealValue[i] = v1+"|"+v2;
	}
	
	$.ajax({
		url : '/burgerking/client/signWseal',
		type : 'POST',
		data : {'chkEsealValue':chkEsealValue},
		async : false,
		cache : false,
		traditional: true,
		dataType: "json",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
		success : function(data){
			if(data.res=='false'){
				alert("盖章失败店铺：["+data.msg+"]");
			}
			wpdfTableShow();
		}
	});
	
	$("#We-seal").hide(0);
	$("#WchengePDF").show(0);
	ulliNext(4, 3, 2, 2);
}



function WfileTipcPrev() {
	$("#WfileTipc").hide(0);
	$("#WsendEmail").show(0);
	ulliPrev(6, 5, 5, 4, 4);
}

function WfileTipcSendEmail() {
//	$('#WsendTureMode').modal({
//		backdrop : 'static',
//		keyboard : false
//	});
//	
	var chkValue = new Array();
	$('input[name="WfileTipcName"]:checked').each(function(){
		chkValue.push($(this).val());
	});
	if(chkValue.length == 0){
		alert("请选择需要发送邮件的报表！");
		return;
	}
	var chkEsealValue = new Array();
	for(var i=0; i<chkValue.length; i++){
		var v1 = $.trim(chkValue[i]);
		var v2 = $("#WdefaultEmail"+chkValue[i]).val();
		var v3 = $("#WmoreEmail"+chkValue[i]).val();
		
		if((v2 == null || v2 == "") && (v3 == null || v3 == "")){
			alert("请选择收件邮箱！");
			return;
		}
		if((v2 != null && v2 != "")&& (v3 == null || v3 == "") ){
			chkEsealValue[i] = v1+"|"+v2;
		}else if((v2 == null || v2 == "") &&(v3 != null && v3 != "")  ){
			chkEsealValue[i] = v1+"|"+v3;
		}else{
			chkEsealValue[i] = v1+"|"+v2+"，"+v3;
		}
	}
	
	$.ajax({
		url : '/burgerking/client/sendWordSubjectEmail',
		type : 'POST',
		data : {'chkEsealValue':chkEsealValue},
		async : false,
		cache : false,
		traditional: true,
		dataType: "json",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
		success : function(data){
			if(data.res=='false'){
				alert("邮件发送失败个数："+data.msg);
			}
		}
	});
	WauditTableShow();
	$("#WfileTipc").hide(0);
	$("#WcheckFile").show(0);
	ulliNext(7, 6, 5, 5);

	
}

function WauditTableShow(){
	$.ajax({
		type: "POST",
        url: "/burgerking/client/findWordAuditData",
        data: {
        	"companyId":$("#companyId").val(),
        	"month":$("#month").val()
        },
        dataType: "json",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
	    cache : false,
	    async : false,
        success: function(data){
        	var  html='';
        	for (var i = 0; i < data.dataList.length; i++) {
        		 html+="<tr id='"+data.dataList[i][0]+"'>" +
        		 		"<td  style='text-align: left;' width='20%'><div style='word-break:break-all;'>"+data.dataList[i][1]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='20%'><div style='word-break:break-all;'>"+data.dataList[i][2]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='20%'><div style='word-break:break-all;'>"+data.dataList[i][3]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='20%'><div style='word-break:break-all;'>"+data.dataList[i][4]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='20%'>"+statusText(data.dataList[i][5])+"</td>" +
        		 		"</tr>" ;
        	}
        	 document.getElementById("WcheckFileTable").innerHTML = html;
        	 goPage(1,10,"WcheckFileTable","WcheckFileTablePageId");
        }
	});
}




function WlookFile() {
	$('#WsendTureMode').modal('hide');
	fileTipcPrev();
}
function WcheckFile() {
	$('#WsendTureMode').modal('hide');
	$("#WfileTipc").hide(0);
	$("#WcheckFile").show(0);
	ulliNext(7, 6, 5, 5);
}
function WsavaMessager() {
	$('#WsavePwdMode').modal({
		backdrop : 'static',
		keyboard : false
	});
}
function WupdatePwdMode() {
	$('#WupdatePwdMode').modal({
		backdrop : 'static',
		keyboard : false
	});
}

function WbackFile() {
	$("#maincontent").load('../resources/hbFrontHtml/homePage.html',
			function() {
				ready();
			});
}