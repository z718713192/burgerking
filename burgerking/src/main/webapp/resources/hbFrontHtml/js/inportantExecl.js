var excelName = "excelfile";
var i = 1;
function addExcelInput() {

	$("input[name='" + excelName + "']").each(function(j, item) {
		if (item.value == "") {
			$(this).parent().parent().remove();
		}
	});
	var attach ="";
	if (i > 0) {
		attach = excelName + i;
		createEInput(attach, excelName)
		i = i + 1;
	}
	$("#" + attach + "").attr("onchange", "efileChange(this)")
	$("#" + attach + "").click();
}

function efileChange(id) {
	var a = 0;
	$("input[name='" + excelName + "']").each(
			function(j, item) {
				if (item.value == "") {
					$(this).parent().parent().remove();
				}
				var fileType = $(this).val().substring(
						$(this).val().lastIndexOf(".")).toLowerCase();
				if (fileType != '.xlsx') {
					a++;
					$(this).parent().parent().remove();
				}
				$(this).attr("disabled", "disabled")
			});
	if (a == 0) {
		EselectFile();
	} else {
		alert("请选择excel文件");
	}
}
//application/msword,
function createEInput(id, name) {
	var html = "<div  class='col-md-12' style='height:35px;line-height:35px;'><div  class='col-md-1'><input type='checkbox' name='excelCheck' value='"+id+"' /></div>"
			+ "<div  class='col-md-8'><input name='"
			+ name
			+ "' id='"
			+ id
			+ "' type='file' size='50' accept='.xlsx' onchange='fileChange(this)'  /></div><div>";
	$("#Eupload").append(html);
}

function EselectFile() {

	$("#EselectFile").show(0);
	$("#fileImport").hide(0);
	
	ulliNext(2, 1, 0, 0);
}

function EselectFilePrev() {
	$("#EselectFile").hide(0);
	$("#fileImport").show(0);
	ulliPrev(2, 1, 1, 0, 0);
}
function EselectFileNext() {
	var id = '';
	var obj=document.getElementsByName('excelCheck');
	for(var i=0; i<obj.length; i++){
		if(obj[i].checked) id+=obj[i].value+',';
	}
	if(id == null || id == ""){
		alert("请选择上传文件！");
		return;
	}
	for(var i=0; i<obj.length; i++){
		if(obj[i].checked){
			document.getElementById(obj[i].value).disabled=false;
		}
	}
	var fd = new FormData($('#uploadForm')[0]);
	$.ajax({
		url : '/burgerking/client/fileUpload',
		type : 'POST',
		data : fd,
		async : false,
		cache : false,
		processData: false,
		contentType: false,
		success : function(data){
			if(data.res=='success'){				
				$.ajax({
					url : '/burgerking/client/saveExcelData',
					type : 'POST',
					data : {'fileName':data.fileName},
					async : false,
					cache : false,
					dataType: "json",
			        contentType:"application/x-www-form-urlencoded; charset=utf-8",
					success : function(res){
						if(res.res=='false'){
							alert("录入失败，"+res.msg);
						}
						esealTableShow();
						timeOut();
						$("#EselectFile").hide(0);
						$("#Ee-seal").show(0);
						ulliNext(3, 2, 1, 1);
					}
				});			
			}else{
				alert("上传失败,"+data.msg);
				return;
			}
		}
	});
}

 

function timeOut(){
	var countdown =90;
	$("#EesealNext").attr("disabled","disabled");
	$("#EesealNext").css("background-color","#bdbdbd");
 	var s=setInterval(function(){
 		if (countdown == 0) { 
 			$("#EesealNext").removeAttr("disabled"); 
 			$("#EesealNext").css("background-color","#E0592B");
 			$("#EesealNext").html("下一步");
 			clearInterval(s);
 		}else{
 			$("#EesealNext").html(countdown);
 		}
 		countdown--; 
	},1000); 
}


function EesealPrev() {
	$("#Ee-seal").hide(0);
	$("#EselectFile").show(0);
	ulliPrev(3, 2, 2, 1, 1);
}

function EesealNext() {
	var chkValue = new Array();
	
	$('input[name="EesealFileName"]:checked').each(function(){
		chkValue.push($(this).val());
	});
	if(chkValue.length == 0){
		alert("请选择需要盖章的报表！");
		return;
	}
	var chkEsealValue = new Array();
	for(var i=0; i<chkValue.length; i++){
//		alert(chkValue[i]);
//		alert($("#esealId"+chkValue[i]).val());
		var v1 = chkValue[i];
		var v2 = $("#esealId"+chkValue[i]).val();
		if(v2 == null || v2 == ""){
			alert("请选择相应印章！");
			return;
		}
		chkEsealValue[i] = v1+"|"+v2;
//		if((v2 != null && v2 != "")&& (v3 == null || v3 == "") ){
//			chkEsealValue[i] = v1;
//		}else if((v2 == null || v2 == "") &&(v3 != null && v3 != "")  ){
//			chkEsealValue[i] = v3;
//		}else{
//			chkEsealValue[i] = v1+"|"+v3;
//		}
	}
	
	$.ajax({
		url : '/burgerking/client/signEseal',
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
			pdfTableShow();
		}
	});
	
	$("#Ee-seal").hide(0);
	$("#EchengePDF").show(0);
	ulliNext(4, 3, 2, 2);
}
function EchengePDFPrev() {
	esealTableShow();
	$("#EchengePDF").hide(0);
	$("#Ee-seal").show(0);
	ulliPrev(4, 3, 3, 2, 2);
}
function EchengePDFNext() {
	
	var chkValue = new Array();
	$('input[name="pdfPageName"]:checked').each(function(){
		chkValue.push($(this).val());
	});
	if(chkValue.length == 0){
		alert("请选择需要转换PDF的报表！");
		return;
	}
	
	$.ajax({
		url : '/burgerking/client/toPdf',
		type : 'POST',
		data : {'chkEsealValue':chkValue},
		async : false,
		cache : false,
		traditional: true,
		dataType: "json",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
		success : function(res){
			emailTableShow();
		}
	});
	
	$("#EchengePDF").hide(0);
	$("#EsendEmail").show(0);
	ulliNext(5, 4, 3, 3);
}
function EsendEmailPrev() {
	pdfTableShow();
	$("#EsendEmail").hide(0);
	$("#EchengePDF").show(0);
	ulliPrev(5, 4, 4, 3, 3);
}
function EsendEmails() {
	var chkValue = new Array();
	
	$('input[name="emailPageName"]:checked').each(function(){
		chkValue.push($(this).val());
	});
	/**
	if(chkValue.length == 0){
		alert("请选择需要发送邮件的报表！");
		return;
	}
	var chkEsealValue = new Array();
	for(var i=0; i<chkValue.length; i++){
		var v1 = chkValue[i];
		var v2 = $("#moreEmail"+chkValue[i]).val();
		if(v2 == null || v2 == ""){
			alert("请选择收件人！");
			return;
		}
		chkEsealValue[i] = v1+"|"+v2;
	}
	
	$.ajax({
		url : '/burgerking/client/sendEmail',
		type : 'POST',
		data : {'chkEsealValue':chkEsealValue},
		async : false,
		cache : false,
		traditional: true,
		dataType: "json",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
		success : function(data){
			if(data.res=='false'){
				alert("发送失败记录数："+data.msg);
			}
		}
	});
	*/
	if(chkValue.length > 0){
		$.ajax({
			url : '/burgerking/client/sendEmail',
			type : 'POST',
			data : {'chkEsealValue':chkValue},
			async : false,
			cache : false,
			traditional: true,
			dataType: "json",
	        contentType:"application/x-www-form-urlencoded; charset=utf-8",
			success : function(data){
				if(data.res=='false'){
					alert("记录更新失败！");
				}
			}
		});
	}
	
	subjectEmailTableShow();
	$("#EsendEmail").hide(0);
	$("#EfileTipc").show(0);
	ulliNext(6, 5, 4, 4);
}
function EfileTipcPrev() {
	emailTableShow();
	$("#EfileTipc").hide(0);
	$("#EsendEmail").show(0);
	ulliPrev(6, 5, 5, 4, 4);
}

function EfileTipcSendEmail() {
	var chkValue = new Array();
	
	$('input[name="subjectEmailPageName"]:checked').each(function(){
		chkValue.push($(this).val());
	});
	if(chkValue.length == 0){
		alert("请选择需要发送邮件的报表！");
		return;
	}
	var chkEsealValue = new Array();
	for(var i=0; i<chkValue.length; i++){
		var v1 = chkValue[i];
		var v2 = $("#defaultEmail"+chkValue[i]).val();
		var v3 = $("#moreEmail"+chkValue[i]).val();
		if((v2 == null || v2 == "") && (v3 == null || v3 == "")){
			alert("请选择收件人！");
			return;
		}
		if((v2 != null && v2 != "")&& (v3 == null || v3 == "") ){
			chkEsealValue[i] = v1+"|"+v2;
		}else if((v2 == null || v2 == "") &&(v3 != null && v3 != "")  ){
			chkEsealValue[i] =  v1+"|"+v3;
		}else{
			chkEsealValue[i] =  v1+"|"+v2+"，"+v3;
		}
//		chkEsealValue[i] = v1+"|"+v3;
	}
	
	$.ajax({
		url : '/burgerking/client/sendSubjectEmail',
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
	
	auditTableShow(null);
	
//	$('#EsendTureMode').modal({
//		backdrop : 'static',
//		keyboard : false
//	});
	$("#EfileTipc").hide(0);
	$("#EcheckFile").show(0);
	ulliNext(7, 6, 5, 5);
}
function ElookFile() {
	$('#EsendTureMode').modal('hide');
	fileTipcPrev();
}
function EcheckFile() {
	$('#EsendTureMode').modal('hide');
	$("#EfileTipc").hide(0);
	$("#EcheckFile").show(0);
	ulliNext(7, 6, 5, 5);
}

function esealTableShow(){
	$.ajax({
		type: "GET",
        url: "/burgerking/client/findExcelDatas",
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
	          		   selectHtml="<select id='esealId"+data.dataList[i][0]+"' style='border: none; scrollbar-arrow-color: #FFCC00; border-collapse: collapse;max-width: 110px' >";
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
        		 		"<td  style='text-align: left;' width='20%'><div class='divtable' title='"+data.dataList[i][1]+"' ><input type='checkbox' name='EesealFileName'  value='"+data.dataList[i][0]+"' />"+data.dataList[i][1]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='20%'><div class='divtable' ><input type='text' name='' value='' style='width:100%' placeholder='关键词或者提示字(系统自提)' /></div></td>" +
        		 		"<td  style='text-align: center;' width='20%'><div class='divtable' title='"+data.dataList[i][2]+"' >"+data.dataList[i][2]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='20%'><div class='divtable' title='"+data.dataList[i][3]+"' >"+data.dataList[i][3]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='20%'><div class='divtable' >"+selectHtml+"</div></td>" +
        		 	  "</tr>" ;
			}
        	 document.getElementById("Ee-sealTable").innerHTML = html;
        	 goPage(1,10,"Ee-sealTable","Ee-sealTablePageId");
        }
 });
}

function EsealCheckedFileAll(){
	var EesealFileName=  document.getElementsByName("EesealFileName");
	for (var i = 0; i < EesealFileName.length; i++) {
		EesealFileName[i].checked=true;
	}
}
function EsealCheckedFileNotAll(){
	var EesealFileName=  document.getElementsByName("EesealFileName");
	for (var i = 0; i < EesealFileName.length; i++) {
		EesealFileName[i].checked=false;
	}
}

function pdfTableShow(){
	$.ajax({
		type: "POST",
        url: "/burgerking/client/findPdfData",
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
        	var EchengePDF="EchengePDF";
        	for (var i = 0; i < data.dataList.length; i++) {
        		 html+="<tr id='"+data.dataList[i][0]+"'>" +
        		 		"<td  style='text-align: left;' width='20%'><div class='divtable' title='"+data.dataList[i][1]+"'><input type='checkbox' name='pdfPageName'  value='"+data.dataList[i][0]+"' />"+data.dataList[i][1]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='20%'><div class='divtable' ><input type='text' name='' value='' style='width:100%' placeholder='关键词或者提示字(系统自提)' /></div></td>" +
        		 		"<td  style='text-align: center;' width='10%' title='"+data.dataList[i][2]+"'>"+data.dataList[i][2]+"</td>" +
        		 		"<td  style='text-align: center;' width='20%'><div class='divtable' title='"+data.dataList[i][3]+"' >"+data.dataList[i][3]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='20%'><div class='divtable' title='"+data.dataList[i][4]+"'>"+data.dataList[i][4]+"</div></td>" +
        		 		'<td  style="text-align: center;" width="10%"><button  value="'+data.dataList[i][2]+'" onclick="lookPdf(this.value,\''+EchengePDF+'\')" >查看pdf</button></td>' +
        		 	  "</tr>" ;
        	}
        	 document.getElementById("Ee-pdfTable").innerHTML = html;
        	 goPage(1,10,"Ee-pdfTable","Ee-pdfTablePageId");
        }
	});
}


function lookPdf(value,showId){
    var	month= $("#month").val();
    var companyId = $("#companyId").val();
	$("#maincontent").load('../resources/hbFrontHtml/look_PDF.html',
			function() {
				ready(value,month,companyId,showId);
	});
}

function emailTableShow(){
	$.ajax({
		type: "POST",
        url: "/burgerking/client/findExcelEmailData",
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
        		
//        		 html+="<tr id='"+data.dataList[i][0]+"'>" +
//        		 		"<td  style='text-align: left;' width='20%'><div class='divtable' title='"+data.dataList[i][1]+"'><input type='checkbox' name='emailPageName'  value='"+data.dataList[i][0]+"' />"+data.dataList[i][1]+"</div></td>" +
//        		 		"<td  style='text-align: center;' width='20%'><div class='divtable' ><input type='text' name='' value='' style='width:100%' placeholder='关键词或者提示字(系统自提)' /></div></td>" +
//        		 		"<td  style='text-align: center;' width='20%'><div class='divtable' title='"+data.dataList[i][2]+"'>"+data.dataList[i][2]+"</div></td>" +
//        		 		"<td  style='text-align: center;' width='20%'><div class='divtable' title='"+data.dataList[i][3]+"'>"+data.dataList[i][3]+"</div></td>" +
//        		 		"<td  style='text-align: center;' width='20%'  onclick='showmoreEmail("+data.dataList[i][2]+")'>" +
//        		 			"<input type='hidden' value='' name='moreEmail' id='moreEmail"+data.dataList[i][0]+"' />" +
//        		 			"<div id='"+data.dataList[i][2]+"moreEmail' class='divtable'>点击选择</div>"
//        		 		"</td>" +
//        		 	  "</tr>" ;
        		
        		html+="<tr id='"+data.dataList[i][0]+"'>" +
		 		"<td  style='text-align: left;' width='20%'><div class='divtable' title='"+data.dataList[i][1]+"'><input type='checkbox' name='emailPageName'  value='"+data.dataList[i][0]+"' />"+data.dataList[i][1]+"</div></td>" +
		 		"<td  style='text-align: center;' width='20%'><div class='divtable' ><input type='text' name='' value='' style='width:100%' placeholder='关键词或者提示字(系统自提)' /></div></td>" +
		 		"<td  style='text-align: center;' width='20%'><div class='divtable' title='"+data.dataList[i][2]+"'>"+data.dataList[i][2]+"</div></td>" +
		 		"<td  style='text-align: center;' width='20%'><div class='divtable' title='"+data.dataList[i][3]+"'>"+data.dataList[i][3]+"</div></td>" +
		 		"<td  style='text-align: center;' width='20%'><div class='divtable' title='"+data.dataList[i][4]+"'>"+data.dataList[i][4]+"</div></td>" +
		 	  "</tr>" ;
        	}
        	 document.getElementById("EsendEmailTable").innerHTML = html;
        	 goPage(1,10,"EsendEmailTable","EsendEmailTablePageId");
        }
	});
}

function subjectEmailTableShow(){
	$.ajax({
		type: "POST",
        url: "/burgerking/client/findSubjectEmailData",
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
        		 		"<td  style='text-align: left;' width='20%'><div class='divtable' title='"+data.dataList[i][1]+"' ><input type='checkbox' name='subjectEmailPageName'  value='"+data.dataList[i][0]+"' />"+data.dataList[i][1]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='10%'>"+data.dataList[i][2]+"</td>" +
        		 		"<td  style='text-align: center;' width='10%'><div style='overflow: hidden; text-overflow: ellipsis; white-space: nowrap;width:50px;' title='"+data.dataList[i][3]+"'>"+data.dataList[i][3]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='20%'><div class='divtable' title='"+data.dataList[i][2]+".pdf'>"+data.dataList[i][2]+".pdf</div></td>" +
        		 		"<td  style='text-align: center;' width='20%'>" +
        		 			"<input type='hidden' id='defaultEmail"+data.dataList[i][0]+"' value='"+data.dataList[i][4]+"' />" +
        		 			"<div class='divtable' title='"+data.dataList[i][4]+"'>"+data.dataList[i][4]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='20%'  onclick='showmoreEmail("+data.dataList[i][2]+")'>" +
    		 				"<input type='hidden' value='' name='moreEmail' id='moreEmail"+data.dataList[i][0]+"' />" +
    		 				"<div id='"+data.dataList[i][2]+"moreEmail' class='divtable'>点击选择</div>" +
    		 			"</td>" +
        		 		"</tr>" ;
        	}
        	 document.getElementById("EsendEmailTableData").innerHTML = html;
        	 goPage(1,10,"EsendEmailTableData","EsendEmailTableDataPageId");
        }
	});
}


function auditTableShow(companyName){
	$.ajax({
		type: "POST",
        url: "/burgerking/client/findAuditData",
        data: {
        	"companyId":$("#companyId").val(),
        	"month":$("#month").val(),
        	"companyName":companyName
        },
        dataType: "json",
        contentType:"application/x-www-form-urlencoded; charset=utf-8",
	    cache : false,
	    async : false,
        success: function(data){
        	var  html='';
        	var EcheckFile='EcheckFile';
        	for (var i = 0; i < data.dataList.length; i++) {
        		 html+="<tr id='"+data.dataList[i][0]+"'>" +
        		 		"<td  style='text-align: left;' width='20%'><div style='word-break:break-all;'>"+data.dataList[i][1]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='20%'><div style='word-break:break-all;'>"+data.dataList[i][2]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='20%'><div style='word-break:break-all;'>"+data.dataList[i][3]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='15%'><div style='word-break:break-all;'>"+data.dataList[i][4]+"</div></td>" +
        		 		"<td  style='text-align: center;' width='10%'>"+statusText(data.dataList[i][5])+"</td>" +
        		 		'<td  style="text-align: center;" width="15%"><button  value="'+data.dataList[i][6]+'" onclick="lookPdf(this.value,\''+EcheckFile+'\')" >查看pdf</button></td>' +
        		 		"</tr>" ;
        	}
        	 document.getElementById("EauditTableData").innerHTML = html;
        }
	});
}
$("#eCheckFileSearch").click(function(){
	var companyName=$("#eCheckFileCompany").val();
	auditTableShow(companyName);
})


function statusText(data){
	if(data!=null){
		if(data==1){
			return '已发送';
		}else{
			return '未知状态';
		}
	}
}

function showmoreEmail(id){
	$('#moreEamilModal').modal({backdrop: 'static', keyboard: false});
	$("#moreEamilModalButton").attr("value",id);
	 $.ajax({
			type: "GET",
	        url: "/burgerking/sys/findSupplierByStoreId",
	        data: {
	        	"id":id
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
		 					html+="<div><input type='checkbox' name='moreEmailMode' value='"+emailArray[i]+"' />"+emailArray[i]+"</div>";
						}
	        		}
	 				 
	 				$("#moreEamilModalBody").html(html); 
	        	}
	        }
	 });
	 var as= $("#"+id+"moreEmail").html().split(',');
	 for (var i = 0; i < as.length; i++) {
		$("input[name='moreEmailMode']").each(function(){
			if(as[i]==$(this).val()){
				$(this).prop("checked","true");
			}
		});
	}
	  
}

function checkedEmail(){
	
	var html="";
	$("input[name='moreEmailMode']").each(function(){
		var checked=  $(this).prop("checked");
		if(checked){
			html+=  $(this).val()+"，";
		}
	})
	html=html.substring(0,html.length-1);
	$("#"+$('#moreEamilModalButton').attr('value')+"moreEmail").html(html);
	$("#"+$('#moreEamilModalButton').attr('value')+"moreEmail").attr("title",html);
	$("#"+$('#moreEamilModalButton').attr('value')+"moreEmail").prev("input").val(html);
	$('#moreEamilModal').modal('hide');
}
function EbackFile() {
	$("#maincontent").load('../resources/hbFrontHtml/homePage.html',
			function() {
				ready();
			});
}