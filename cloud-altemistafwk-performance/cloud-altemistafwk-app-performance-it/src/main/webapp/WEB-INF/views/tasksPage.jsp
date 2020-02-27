<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container-fluid">
	<h1>${title}</h1>
	<ul class="list-group">
		<jsp:include page="taskLoop.jsp" />
	</ul>
</div>