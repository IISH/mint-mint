<%@ include file="_include.jsp"%>
<%@ page language="java" errorPage="error.jsp"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

<link href="images/mintsmall.png" type="image/png" rel="icon" />


<!-- new stuff here -->
<script src="js/fileuploader.js" type="text/javascript"></script>


<script type="text/javascript" src="https://www.google.com/jsapi"></script>


<link rel="stylesheet" type="text/css" href="css/jquery/jquery.layout.default.css" />
<link rel="stylesheet" type="text/css" href="css/kaiten.css"/>
<link rel="stylesheet" type="text/css" href="css/jquery/Aristo/Aristo.css" />
<link rel="stylesheet" type="text/css" href="css/jquery/jquery.toolbar.css" />
<link rel="stylesheet" type="text/css" href="css/fileuploader.css"/>
<link rel="stylesheet" type="text/css" href="css/widgets/schemaTree.css"/>
<link rel="stylesheet" type="text/css" href="css/widgets/schemaTree-ann.css"/>
<link rel="stylesheet" type="text/css" href="css/widgets/valueBrowser.css"/>
<link rel="stylesheet" type="text/css" href="css/widgets/thesaurusBrowser.css"/>
<link rel="stylesheet" type="text/css" href="css/xsdmapping/mappingElement.css"/>
<link rel="stylesheet" type="text/css" href="css/xsdmapping/annotatorElement.css"/>
<link rel="stylesheet" type="text/css" href="css/xsdmapping/xsdmapping.css"/>
<link rel="stylesheet" type="text/css" href="css/annotator/annotator.css"/>
<link rel="stylesheet" type="text/css" href="js/slickgrid/slick.grid.css"/>
<link rel="stylesheet" type="text/css" href="js/chosen/chosen.css"/>
<link rel="stylesheet" href="js/slickgrid/examples/examples.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/pagination.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="css/athform.css">
<link rel="stylesheet" type="text/css" href="css/evol.colorpicker.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="css/core.css">
<link rel="stylesheet" type="text/css" href="css/mint2.css" />
<link rel="stylesheet" type="text/css" href="css/toggles.css" />
<link rel="stylesheet" type="text/css" href="css/toggles-full.css" />
<link rel="stylesheet" href="js/jquery/jstree/themes/default/style.min.css" />
<link rel="stylesheet" href="js/jquery/jstree/themes/proton/style.min.css" />
<!-- highlighter  -->


<link rel="stylesheet" type="text/css" href="css/highlighter/styles/shThemeMint.css"/>  
<link rel="stylesheet" type="text/css" href="css/highlighter/styles/shCore.css"/>
<script type="text/javascript" src="js/highlighter/scripts/XRegExp.js"></script>
<script type="text/javascript" src="js/highlighter/scripts/shCore.js"></script>
<script type="text/javascript" src="js/highlighter/scripts/shBrushXml.js"></script>

   <script type="text/javascript" src="js/highlighter/scripts/shBrushPlain.js"></script>

  
<!-- highlighter end -->


<script src="js/slickgrid/lib/jquery-1.11.min.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.0.0.js"></script>
<script type="text/javascript" src="js/mint2/mint2.js"></script>
<script type="text/javascript" src="js/jquery/jquery.tools.min.js"></script> 
<script type="text/javascript" src="js/jquery/jquery-ui.min.js"> </script>
<script type="text/javascript" src="js/jquery/jquery.layout.js"> </script>
<script type="text/javascript" src="js/jquery/jquery.jeditable.mini.js"> </script>
<script type="text/javascript" src="js/jquery/jquery.qtip.min.js"> </script>
<script type="text/javascript" src="js/jquery/jquery.pagination.js"> </script>
<script type="text/javascript" src="js/jquery/jquery.blockUI.js"> </script>
<script type="text/javascript" src="js/jquery/jquery.toolbar.min.js"> </script>
<script type="text/javascript" src="js/jquery/jstree/jquery.jstree.js"></script>
<script type="text/javascript" src="js/jquery/jstree/jstreegrid.js"></script>
<script type="text/javascript" src="js/jquery/jquery.ui.touch.js"> </script>
<script type="text/javascript" src="js/jquery/toggles.min.js"> </script>

<script type="text/javascript" src="js/kaiten.js"> </script>
<script type="text/javascript" src="js/chosen/chosen.jquery.min.js"> </script>
<script type="text/javascript" src="js/ace/ace.js"> </script>
<script type="text/javascript" src="js/ace/mode-xml.js"> </script>
<script type="text/javascript" src="js/ace/mode-groovy.js"> </script>

<script type="text/javascript" src="js/slickgrid/lib/jquery.event.drag-2.0.min.js"> </script>
<script type="text/javascript" src="js/slickgrid/slick.core.js"> </script>
<script type="text/javascript" src="js/slickgrid/slick.grid.js"> </script>
<script type="text/javascript" src="js/slickgrid/slick.formatters.js"> </script>
<script type="text/javascript" src="js/slickgrid/slick.editors.js"> </script>
<script type="text/javascript" src="js/slickgrid/slick.dataview.js"> </script>
<script type="text/javascript" src="js/slickgrid/plugins/slick.checkboxselectcolumn.js"></script>
<script type="text/javascript" src="js/slickgrid/plugins/slick.rowselectionmodel.js"></script>

<script type="text/javascript" src="js/mint2/documentation.js"></script>
<script type="text/javascript" src="js/mint2/documentation-resources.js"></script>
<script type="text/javascript" src="js/mint2/preferences.js"></script>
<script type="text/javascript" src="js/mint2/widgets/schemaTree.js"></script>
<script type="text/javascript" src="js/mint2/widgets/valueBrowser.js"></script>
<script type="text/javascript" src="js/mint2/widgets/thesaurusBrowser.js"></script>
<script type="text/javascript" src="js/mint2/widgets/itemBrowser.js"></script>
<script type="text/javascript" src="js/mint2/widgets/itemPreview.js"></script>
<script type="text/javascript" src="js/mint2/xsdmapping/mappingAjax.js"></script>
<script type="text/javascript" src="js/mint2/xsdmapping/mappingArea.js"></script>
<script type="text/javascript" src="js/mint2/xsdmapping/mappingElement.js"></script>
<script type="text/javascript" src="js/mint2/annotator/annotatorElement.js"></script>
<script type="text/javascript" src="js/mint2/annotator/annotatorElementSimpleView.js"></script>
<script type="text/javascript" src="js/mint2/annotator/annotatorFunction.js"></script>
<script type="text/javascript" src="js/mint2/annotator/annotatorDuplicate.js"></script>
<script type="text/javascript" src="js/mint2/annotator/annotatorSchemaTree.js"></script>
<script type="text/javascript" src="js/mint2/xsdmapping/mappingFunction.js"></script>
<script type="text/javascript" src="js/mint2/xsdmapping/mappingValues.js"></script>
<script type="text/javascript" src="js/mint2/xsdmapping/mappingCondition.js"></script>
<script type="text/javascript" src="js/mint2/xsdmapping/mappingView.js"></script>
<script type="text/javascript" src="js/mint2/xsdmapping/xsdmappingeditor.js"></script>
<script type="text/javascript" src="js/mint2/xsdmapping/xsdmappingPreview.js"></script>
<script type="text/javascript" src="js/mint2/xsdmapping/xsdmappingValidation.js"></script>
<script type="text/javascript" src="js/mint2/xsdmapping/xsdmappingNavigation.js"></script>
<script type="text/javascript" src="js/mint2/xsdmapping/xsdmappingPreferences.js"></script>
<script type="text/javascript" src="js/mint2/annotator/annotator.js"></script>
<script type="text/javascript" src="js/mint2/publish/testWith.js"></script>


<script type="text/javascript" src="https://www.google.com/jsapi"></script>

<% if( Config.get("with.url") != null ) {  %>
<script src="<%=Config.get( "with.url" ) %>/assets/js/app/withToken.js"></script>
<% } %>


<script type="text/javascript" src="js/modalPopup.js"></script>

<!--  used stuff  (replace all YUI)-->
<script type="text/javascript" src="js/jquery/evol.colorpicker.min.js"></script>
<script type="text/javascript" src="js/labelRequest.js"></script>
<script type="text/javascript" src="js/itemRequest.js"></script>
<script type="text/javascript" src="js/mappingRequest.js"></script>
<script type="text/javascript" src="js/outputxsdRequest.js"></script>
<script type="text/javascript" src="js/importRequest.js"></script>
<script type="text/javascript" src="js/adminRequest.js"></script>
<script src="js/oaiRequest.js" type="text/javascript"></script>
 
 <script type="text/javascript">
 	google.load('visualization', '1', {'packages':['table']});
    google.load('visualization', '1', {'packages':['corechart']});
    google.load('visualization', '1.0', {'packages':['controls']});
</script>
  
<title><%=Config.get("mint.title")%></title>

</head>

<body>
<div id="container">