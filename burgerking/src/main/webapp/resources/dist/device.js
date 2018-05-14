 //初始化下拉列表
 	function initDeviceSelect(){
 		 $.ajax({
				type: "GET",
		        url: "/burgerking/sys/findSelects",
		        data: {},
		        dataType: "json",
		        contentType:"application/x-www-form-urlencoded; charset=utf-8",
			    cache : false,
			    async : false,
		        success: function(data){
		        	var dhtml='<option value="">----请选择设备类型----</option>';
		        	$(data.device).each(function(i, item){
		        		dhtml+='<option value="'+item.typeId+'">'+item.typeName+'</option>';
		        	});
		        	$('#typeId').html(dhtml);
		        	var shtml='<option value="">----请选择门店名称----</option>';
		        	$(data.store).each(function(i, item){
		        		shtml+='<option value="'+item.storeId+'">'+item.storeName+'</option>';
		        	});
		        	$('#storeId').html(shtml);
		        }
		        });
 	}