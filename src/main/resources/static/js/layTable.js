var layTable={};
var table={};
var layTables={};

layTable.getColSum=function(fieldNames){
	if(fieldNames){
 		var arrays=fieldNames.split(",");
 		var sum=0;
 		 for ( var i = 0; i <arrays.length; i++){
 		    var fieldName=arrays[i];
 		    var fieldObj=$(".layui-table-main [data-field='"+fieldName+"']");
 		    $(fieldObj).each(function() {
 		    	var t=$(this);
 		    	var tdata=t.data("content");
 		    	if(tdata||tdata==0){
 		    		var val=tdata+'';
 		    		if(val)sum = numAdd(sum,val);
 		    	}else{
 		    		var val=t.find('div').text();
 		    		if(val)sum = numAdd(sum,val);
 		    	}
 		    });
 		 }
 	   return sum;
	}
	return 0;
}
layTable.getlayTableTotalField=function(fieldName){
	var fillValue=$(".layui-table-total [data-field='"+fieldName+"']").find("div");
	return fillValue;
}
layTable.getlayTableFieldLength=function(fieldName){
	var length=$(".layui-table-main [data-field='"+fieldName+"']").length;
	return length;
}
layTable.addTitle=function(){
	$(".layui-table-header th").each(function(){
		var t=$(this);
		t.attr("title",t.text());
	 });
}
 layTable.colSum=function(fieldNames,str){
	 var joinStr='';
	 if (arguments.length>1) {
		 joinStr = str?str:''; 
	 }
	 if(fieldNames){
		 var arrays=fieldNames.split(",");
		 for ( var i = 0; i <arrays.length; i++){
			 var fieldName=arrays[i];
			 var sum=layTable.getColSum(fieldName);
			 layTable.getlayTableTotalField(fieldName).text(sum+joinStr);
		 }
	 }
 }
 /***
  * 计算平均数 未转换
  * @param fieldName
  */
 layTable.colAvg=function(fieldNames,str){
	 var joinStr='';
	 if (arguments.length>1) {
		 joinStr=str?str:''; 
	 }
	 if(fieldNames){
		 var arrays=fieldNames.split(",");
		 for ( var i = 0; i <arrays.length; i++){
			 var fieldName=arrays[i];
			 var sum=layTable.getColSum(fieldName);
			 var length=layTable.getlayTableFieldLength(fieldName);
			 var result=numDiv(sum,length);
			 layTable.getlayTableTotalField(fieldName).text(result+joinStr);
		 }
	 }
 }

function numAdd(num1, num2) { 
   var baseNum, baseNum1, baseNum2; 
   try { 
      baseNum1 = num1.toString().split(".")[1].length; 
   } catch (e) {  
     baseNum1 = 0;
   } 
   try {
       baseNum2 = num2.toString().split(".")[1].length; 
   } catch (e) {
     baseNum2 = 0; 
   } 
   baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
   var precision = (baseNum1 >= baseNum2) ? baseNum1 : baseNum2;//精度
   return ((num1 * baseNum + num2 * baseNum) / baseNum).toFixed(precision);; 
};

//除法运算，避免数据相除小数点后产生多位数和计算精度损失。 
function numDiv(num1, num2) { 
	var baseNum1 = 0, baseNum2 = 0; 
	var baseNum3, baseNum4; 
	try { 
		baseNum1 = num1.toString().split(".")[1].length; 
	} catch (e) { 
		baseNum1 = 0; 
	} 
	try { 
		baseNum2 = num2.toString().split(".")[1].length; 
	} catch (e) { 
		baseNum2 = 0; 
	} 
	with (Math) { 
		baseNum3 = Number(num1.toString().replace(".", "")); 
		baseNum4 = Number(num2.toString().replace(".", "")); 
		//return (baseNum3 / baseNum4) * pow(10, baseNum2 - baseNum1); 
		return (baseNum3 / baseNum4).toFixed(2); 
	} 
}; 

layTable.totalRow = function(option){
	var othis=option['othis'];
	if(othis.totalRow){
		var cols = othis['cols'][0];
		for(var index in cols){
			var row  = cols[index];
			if(row['totalRow']){
				var fieldName  = row['field'];
				var totalRow  = row['totalRow'];
				var totalFormat  = row['totalFormat'];
				var sum=0;		
				var fieldObj = othis['elem'].next().find(".layui-table-main [data-field='"+fieldName+"']");
				$(fieldObj).each(function() {
	 		    	var t=$(this);
	 		    	var tdata=t.data("content");
	 		    	if((tdata||tdata==0)&&tdata!=''){
	 		    		var val=tdata+'';
	 		    		if(val)sum = numAdd(sum,val);
	 		    	}else{
	 		    		var val=t.find('div').text();
	 		    		if(val)sum = numAdd(sum,val);
	 		    	}
 		      });
			  if(totalRow=='avg'){
				  var length = othis['elem'].next().find(".layui-table-main [data-field='"+fieldName+"']").length;
				  var result = numDiv(sum,length);
				  othis['elem'].next().find(".layui-table-total [data-field='"+fieldName+"']").find("div").text(formatVal(result,totalFormat));
			 }else{
				  othis['elem'].next().find(".layui-table-total [data-field='"+fieldName+"']").find("div").text(formatVal(sum,totalFormat));
			 }
		  }
		}
	}
	function formatVal(val,format){
		if(format){
			return format.replace('this',val);
		}else{
			return val;
		}
	}
}


function getlayTable(el,option){
	!!option || (option = {});
	var elem=option.elem;
	var table=null;
	if(elem){
		if($.isPlainObject(elem)){
			table=elem;
		}else{
			table=$($g(option.elem));
		}
	}else{
		if(option.id){
			table=el.find('#'+option.id);
		}else{
			table=el.find('table').first();
		}
	}
	var id=new Date().getTime();
	if(table.attr("id")==''||table.attr("id")==undefined){
		table.attr("id",id);
	}
	id=table.attr("id");
	if(table.attr("lay-filter")==undefined||table.attr("lay-filter")==''){
		table.attr("lay-filter",id);
	}
	return table;
	
}
function getLayUrl(option){
	var url=option.url;
	var pathName = window.document.location.pathname;
    var contextPath=pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    if(url){
		return url;
    }else{
    	if(option.mars){
    		return contextPath+"/webcall?action="+option.mars
    	}else{
    		return '';
    	}
    }
}
/***
 * 返回渲染成功后的字符串
 * @param templateId jsrender魔板Id
 * @param data 数据
 */
function renderTpl(templateId,data){
	var tmp=null;
	if(templateId){
		 tmp = $.templates("#"+templateId);
	}else{
		return '';
	}
	if(data){
		return tmp.render(data);
	}else{
		return '';
	}
}
$.fn.getConfig = function(option){
	!!option || (option = {});
	var el=$(this);
	var tableObj=getlayTable(el,option);
	var tableId=option.id||tableObj.attr("id");
	if(layTables[tableId]==null){
		console.error("table "+tableId+" not init.");
		return {};
	}
   return layTables[tableId].config;
}
$.fn.queryData = function(option){
	!!option || (option = {});
	var el=$(this);
	var elData=el.data()||{'form':true};
	if(elData['form']==false){
		 dataObj = $.extend({},{pageType:3,searchFlag:1},option.data,option.where);
	}else{
		dataObj = $.extend({},{pageType:3,searchFlag:1},el.serializeObject(),elData,option.data,option.where);
	}
	delete option['where'];
	var tableObj=getlayTable(el,option);
	var tableId=option.id||tableObj.attr("id");
	if(layTables[tableId]==null){
		console.error("table "+tableId+" not init.");
		return;
	}
	if(option['jumpOne']==false){

	}else{
		if(option.page==false){
			delete option['page'];
		}else{
			option['page']={curr:1};
		}
	}
	var dataStr=JSON.stringify(dataObj);
	if(typeof(filterXSS)!="undefined"){
		dataStr=filterXSS(dataStr);
	}
	layTables[tableId].reload($.extend({loading:true},{where:{data:dataStr}},option),false);
}
layTable.autoFill=function(options){
	if(options.autoFill){
		var id=options.id;
		var res=options.result;
		var config=layTables[id].config;
		var maxRow=config.limit;
		var datarow=0;
		var colsLength=0;
		if(options.cols){
			 colsLength = options.cols[0].length;
		}else{
			colsLength = $("#"+id).next().find(".layui-table-box thead th").length;
		}
		if(res&&res.data){
			datarow=res.data.length;
		}else{
			datarow = $("#"+id).next().find(".layui-table-box tbody tr").length;
		}
		if(typeof(options.autoFill)=='number'){
			maxRow=options.autoFill;
		}
		if(typeof(options.autoFill)=='object'){
			maxRow=options.autoFill[0];
			colsLength=options.autoFill[1];
		}
		if(datarow<maxRow){
			var fillRow=maxRow-(maxRow-datarow);
			var html="";
			for(var i=fillRow;i<maxRow;i++){
				html=html+"<tr data-index='"+i+"'>";
				for(var j=0;j<colsLength;j++){
					html=html+"<td><div class='layui-table-cell laytable-cell-"+(i+1)+"-0-"+j+"'>&nbsp;</div></td>";
				}
				html=html+"</tr>";
				
			}
			$(".layui-none").remove();
			$("#"+id).next().find(".layui-table-box tbody").append(html);
		}
	}
}

function layTableAllChoose(args){
	
}

function excuteFn(eventName,args){
	try{
		var fn = eval(eventName); 
		if(typeof(fn)=='function'){
			fn.apply(this,args)
		}
		
	}catch (e) {
		console.error(e);
	}
}

$.fn.initTable = function (option){
	!!option || (option = {});
	var el=$(this);
	layui.use('table', function(){
		table = layui.table;
		var dataObj = {};
		var elData=el.data();
		if(elData==undefined){
			elData = {'form':true};
		}
		if(elData['form']==false){
			 dataObj = $.extend({},{pageType:3},option.data,option.where);
		}else{
			dataObj = $.extend({},{pageType:3},el.serializeObject(),option.data,elData,option.where);
		}
		delete option['where'];
		var tableObj=getlayTable(el,option);
		id=tableObj.attr("id");
		//表格装载数据
		var tempDone=option.done;
		var then=option.then;
		if(option.done){
			delete option['done'];
		}
		var dataStr=JSON.stringify(dataObj);
		if(typeof(filterXSS)!="undefined"){
			dataStr=filterXSS(dataStr);
		}
		 var tableOption=$.extend(
				 {
					elem: tableObj
					,id:id
					,title:'列表数据'
					,autoFill:false
					,escape:true
					,page:{theme: '#337ab7'}
					,loading:true
					,contentType:'application/x-www-form-urlencoded;charset=UTF-8'
					,method:'post'
					,where:{data:dataStr}
					,parseData:function(res){
					    var state=1;
						if(res.state!=undefined){
							state=res.state;
						}
						return $.extend({},res,{state:state,totalRow:res.totalData,data:res.data,count:res.totalRow||res.count});
					}
					,request:{pageName:'pageIndex',limitName:'pageSize'}
					,response:{statusCode:1,statusName:'state'}
					,error:function(e,content){
						console.error(e,content);
					}
				}
		 		,tableObj.data(),option
		  );
	     var options=$.extend(tableOption,{url:getLayUrl(tableOption)},{done:function(res,curr,count){
	    	 
	    	 //init begin
	    	 var events=el.find("[lay-filter]");
	    		if(events.length>0){
	    			layui.use('form', function(){
	    				var form = layui.form;
	    				events.each(function(){
	    					var obj=$(this);
	    					var tagName = obj[0].tagName.toLowerCase();
	    					var tagNametype = "";
	    					if(obj[0].type){
	    						tagNametype=obj[0].type.toLowerCase()
	    					}
	    					var event=obj.attr("lay-filter");
	    					var action='';
	    					if("select"==tagName){
	    						action="select("+event+")"
	    					}else if(tagNametype=="checkbox"){
	    						if(obj.attr("lay-skin")=='switch'){
	    							action="switch("+event+")"
	    						}else{
	    							action="checkbox("+event+")"
	    						}
	    				    }else if(tagNametype=="radio"){
	    						action="radio("+event+")"
	    					}else if(tagName=="button"){
	    						action="submit("+event+")"
	    					}else{
	    						return true;
	    					}
	    					form.on(action, function(data){
	    						if(event){
	    							excuteFn(event,[data]);
	    						  }
	    					});
	    				});
	    			});
	    	}
    		 //头工具栏事件
    	     table.on('toolbar('+tableOption.id+')', function(obj){
    		   var checkStatus = table.checkStatus(obj.config.id);
    		   var data = checkStatus.data;
    		   var event=obj.event;
    		   if(event){
    			  excuteFn(event,[data,obj]);
    		   }
	    	  });
	    	 //监听行工具事件
	    	  table.on('tool('+tableOption.id+')', function(obj){
	    			  var data = obj.data;
	    			  var event=obj.event;
	    			  if(event){
	    				  excuteFn(event,[data,obj]);
	    			  }
	    	  });
	    	 //监听行单击事件（单击事件/双击事件 row/rowDouble）
	    	  var rowDoubleEvent=tableOption.rowDoubleEvent;
	    	  var rowEvent=tableOption.rowEvent;
	    	  if(rowDoubleEvent){
	    		 var action='rowDouble('+tableOption.id+')';
	    		 tableRowEvent(action,rowDoubleEvent);
	    	  } 
	    	  if(rowEvent){
	    		  var action='row('+tableOption.id+')';
	    		  tableRowEvent(action,rowEvent);
	    	  }
	    	  function tableRowEvent(action,event){
	    		  table.on(action, function(obj){
	    			  var data = obj.data;
	    			  if(event){
	    				  excuteFn(event,[data,obj]);
	    				  obj.tr.addClass('layui-table-click').siblings().removeClass('layui-table-click');
	    			  }
	    		  });
	    	  }
	    	  if(tableOption.edit){
	    		  table.on('edit('+tableOption.id+')', function(obj){
	    			  var event=tableOption.edit;
	    			  if(event){
	    				  excuteFn(event,[obj]);
	    			  }
	    		  });
	    	  }
	    	  if(!tableOption.autoSort){
	    		  table.on('sort('+tableOption.id+')', function(obj){
					  el.attr('data-sort-name',obj.field||'');
					  el.attr('data-sort-type',obj.type||'');
	    			  el.queryData({initSort: obj,id:tableOption.id,data:{sortName:obj.field,sortType:obj.type}});
	    		  });
	    	  }
	    	 //init end
			res['othis'] = this;
	    	tableOption.result=res;
			layTable.totalRow(res);
			tempDone&&tempDone(res,curr,count);
			option.success&&option.success(res,curr,count);
			then&&then(this,res,curr,count);
			layTable.autoFill(tableOption);
			table.resize(tableOption.id);
		  } 
		 });
		 var tableIns=table.render(options);
		 layTables[id]=tableIns; 
		 
		 var laydates=el.find("[data-laydate]");
 		 if(laydates.length>0){
 			layui.use('laydate', function(){
 				var laydate = layui.laydate;
 				laydates.each(function(){
 					  var obj=$(this);
 					  var config=eval('(' + obj.data("laydate") + ')');
 					  laydate.render($.extend({},{elem:this},config));
 			    });
 			});
 		 }
	});
	
};
if(typeof layui != 'undefined'){
	//以下代码是配置layui扩展模块的目录，每个页面都需要引入
	layui.config({
		version:'2.5',
		base: getPrefixPath()+'/easitline-static/lib/layui/modules/'
	}).extend({
		notice: 'notice/notice',
		dropdown:'dropdown/dropdown',
		cascader:'cascader/cascader'
	})
	layui.config({dir: '/easitline-static/lib/layui/'});
}
