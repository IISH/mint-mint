
<head>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css">
#one{
    width: 90%;
    margin: 0 auto;
    overflow: hidden;
    }
#chart_1{
    width: 90%;
    margin: 0 auto;
    height: 90%;
    }
#chart_4{
    width: 90%;
    margin: 0 auto;
    height: 90%;
    }

</style>

<script type="text/javascript">
    
 
     $(function(){
    	 var self = this;
    	 var content = $('#chart_1');
    	 var panel = content.closest('div[id^="kp"]');
    	 $K.kaiten('maximize', panel);
		drawBarChart()  
     
 function drawBarChart()
    {
    $('#chart_1').html('<span><img src="images/buttons/loading.gif" alt=""></span>');
    $('#chart_2').html('<span><img src="images/buttons/loading.gif" alt=""></span>');

    $('#chart_3').html('<span><img src="images/buttons/loading.gif" alt=""></span>');
    $.ajax({
    type:'GET',
    url:"OrganizationStat.action",
     success:function(data) {
   
   
    var sets = []
 	var headers = [];
 	var keys = [];
 	
 	var res = data.result;
 	
 	var org = data.result[0];
 	
 	
 	var values = [];
 	for (var i = 0; i< org.length;i++){
 		var attributes = org[i];
     	for (var key in attributes){
 			headers.push(key);
 		}
     	
 	}
  	sets.push(headers);

 	for (var k = 0; k< res.length;k++){
 		var org = res[k];
 	 var values = [];
 	for (var i = 0; i< org.length;i++){
 		var attributes = org[i];
     	for (var key in attributes){
	
 			value = attributes[key];
 			values.push(value);
 		} 
     	
 	}
  	sets.push(values);
 	}
 	    

    var data = google.visualization.arrayToDataTable(sets);
	
	var options = {'title': 'Project <%=request.getAttribute("getName()")%>'};
    var table = new google.visualization.Table(document.getElementById('chart_1'));
    table.draw(data, options);
   
    var dataView2 = new google.visualization.DataView(data);
    var dataView1 = new google.visualization.DataView(data);
    dataView1.setColumns([0, 1]);
    options = {'title': 'Imported Items','width' : 500,'height' : 250};
    var chart = new google.visualization.PieChart(document.getElementById('chart_2'));
    chart.draw(dataView1, options);

    var chart2 = new google.visualization.PieChart(document.getElementById('chart_3'));
    dataView2.setColumns([0, 3]);
    options = {'title': 'Published  Items','width' : 500,'height' : 250};
    chart2.draw(dataView2, options);
  
    }								   
    });
    }
     
     })

   
    
    </script>


</head>



<div class="panel-body">

	<div class="block-nav">
		<div class="summary">
			<div class="label"> Project Overall : <%=request.getAttribute("getName()")%></div>
			<div class="info"><br/></div>
		 </div>
		 
		 <div id="one"style="width: 1060px;">
		 
   		 <div id="chart_3" style="width: 600px; float: left;"> Left </div>
  		  <div id="chart_2" style="margin-left: 620px;"> Right </div>
		</div>
		<div id="chart_4" style="width: 960px;">
		<h3>Overall</h3>	
		
		<div id="chart_1" style="width: 960px;height: 750px;">
		</div>
		</div>
	</div>
</div>

