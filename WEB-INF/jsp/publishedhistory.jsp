<head>
<!--Load the AJAX API-->
<style type="text/css">
#chart{
    width: 90%;
    margin: 0 auto;
    }
    #grouped_table{
    width: 60%;
    height: 90%;
    margin: 0 auto;
    }
#grouped_table1{
    width: 90%;
    margin: 0 auto;
    }
</style>

<script type="text/javascript">
     $(function(){
    	 var self = this;
    	 var content = $('#chart');
    	 var panel = content.closest('div[id^="kp"]');
    	 $K.kaiten('maximize', panel);
    	 drawChart()
         function drawChart()
         {
         $('#chart').html('<span><img src="images/buttons/loading.gif" alt=""></span>');
         $('#chart_div').html('<span><img src="images/buttons/loading.gif" alt=""></span>');
 	     $('#grouped_table').html('<span><img src="images/buttons/loading.gif" alt=""></span>');
         $.ajax({
         type:'GET',
         url:"UrlApi?isApi=true&action=list&type=Publication",data:"organizationId="+<%=request.getAttribute("getOrganizationId()")%>,
         success:function(data) {
         var sets = [];
         var headers = ["Parent Dataset Name","Publication Date","Published Items","Invalid Items","Target Schema"];
         sets.push(headers);
         for(var i=0; i<data.result.length; i++)
         {
        	 	var parent = data.result[i].name;
        	 	if (data.result[i].parentDataset !== null) var parent = data.result[i].parentDataset;
        	    var date = new Date(data.result[i].publicationEndDate);
        	    var items = data.result[i].publishedItems;
        	    var invalid =  data.result[i].invalidItems;
        	    var target = data.result[i].targetSchema;
        	   /*  var mapping = data.result[i].mappingUsed; */
        	    sets.push([parent, date,items,invalid,target]);
   
         }   
        var newdata = google.visualization.arrayToDataTable(sets);
     	var options = {'title': '<%=request.getAttribute("getName()")%>'};
         
        var table = new google.visualization.Table(document.getElementById('chart'));
        table.draw(newdata, options);
        
        var grouped_dt = google.visualization.data.group(
        	      newdata, [4],[{'column': 2, 'aggregation': google.visualization.data.sum, 'type': 'number'}]);
        
        var grouped_table = new google.visualization.ColumnChart(document.getElementById('grouped_table'));
        grouped_table.draw(grouped_dt, options);
        
        if (data.result.length !==0){
      	    var g_dt = google.visualization.data.group(newdata, [1, 2]);
      	    var chart = new google.visualization.ScatterChart(document.getElementById('chart_div'));
              chart.draw(g_dt, options)
      	    }
        else{
	    	 
	    	 $('#chart').html('<span> None </span>');
	    	 $('#chart_div').html('<span> None </span>');
	     }
        
        
        }
        });   
        }     
     })
    </script>
</head>


<div class="panel-body" style="height: 100%; width: 100%">



				  <pre class='brush: plain'></pre>
 	<div class="block-nav"  style="height: 100%; width: 100%" >
		<div class="summary">
			<div class="label">
			Publications History : <%=request.getAttribute("getName()")%>
			</div>
			<div class="info"><br/>
			</div>
		</div>
		<div id="grouped_table1" style="width: 960px; height: 900px;">	
		 <h3>Publications History</h3>	
   		 <div id="chart_div" style="height: 300px;"> None </div>
			<h3>Publications</h3>	
			<div id="chart" style="width: 960px;height: 300px;" ></div>
		<h3>Published items per schema</h3>	
      		 <div id="grouped_table" style="height: 300px; ">none</div>

		
		</div>
</div>
</div>
