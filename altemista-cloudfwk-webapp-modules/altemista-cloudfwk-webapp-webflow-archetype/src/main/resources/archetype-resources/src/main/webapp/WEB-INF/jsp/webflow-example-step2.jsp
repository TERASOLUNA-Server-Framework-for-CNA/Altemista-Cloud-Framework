<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<meta charset="ISO-8859-1">
<head><title>${rootArtifactId}</title></head>
<body>

	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title"><spring:message code="webflow.example.title" arguments="2" /></h3>
		</div>
		
		<div class="panel-body">
			<h3>${exampleVariable}</h3>
			<a href='<%= request.getContextPath() %>/example-flow'><spring:message code="webflow.example.button.restart" /></a>
		</div>
	</div>
	
</body>
</html>
