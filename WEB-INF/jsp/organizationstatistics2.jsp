<head>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css">
#one{
    width: 100%;
    margin: 0 auto;
    }
#chart_div1{
    width: 100%;
    margin: 0 auto;
    }
#chart_4{
    width: 100%;
    margin: 0 auto;
    }
</style>

<script type="text/javascript">

     $(function(){
    	 var self = this;
    	 var content = $('#chart_div');
    	 var panel = content.closest('div[id^="kp"]');
    	 $K.kaiten('maximize', panel);
 
			drawBarChart()

 	function drawBarChart()
    {

     $('#chart_div').html('<span><img src="images/buttons/loading.gif" alt=""></span>');
     $('#chart_1').html('<span><img src="images/buttons/loading.gif" alt=""></span>');

     
     
    $.ajax({
    type:'GET',
    url:"OrganizationStat.action",
  	data:"organizationId="+<%=request.getAttribute("getOrganizationId()")%>,
    success:function(data) {

    	var sets = []
     	var headers = [];
     	var keys = [];
     	//var org = data.result;
     	var org = data.result[0];
     	
     	
     	var values = [];
     	for (var i = 0; i< org.length;i++){
     		var attributes = org[i];
	     	for (var key in attributes){
     			headers.push(key);
     			value = attributes[key];
     			values.push(value);
     		}
	     	
     	}
      	sets.push(headers);
      	sets.push(values);
     	
     	
     	
     	var data = google.visualization.arrayToDataTable(sets);
    	
		var options = {'title':'Items counted : <%=request.getAttribute("getName()")%>'}
        
		var table = new google.visualization.Table(document.getElementById('chart_1'));
        table.draw(data, options);
        
       
        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
        chart.draw(data, options);
        
    }
       
    });
    }
 	
     })
    </script>
   
   	
</head>

<div class="panel-body"  style="height: 100%; width: 100%">
	<div class="block-nav"  style="height: 100%; width: 100%">
		<div class="summary">
			<div class="label">
			Imported, Transformed and Published Data Items  :  <%=request.getAttribute("getName()")%></div>
			<div class="info"><br/></div>
		</div>
		
 		<!-- <div id="chart_div"  style="height: 50%; width: 70%;left:15%;right:15%"></div>
 		 <div id="chart_1"  style="height: 50%; width: 100%"></div> 
 -->
 		<div id="chart_div1" style="width: 960px; height: 400px;">
		<h3>Overall</h3>
        <div id="chart_div" style="height: 100%; width: 60%; left:25%"></div>
 
		<div id="chart_1" style="width: 960px; height: 100%;" ></div>
						
			
       		 
	</div>
</div>
</div>

