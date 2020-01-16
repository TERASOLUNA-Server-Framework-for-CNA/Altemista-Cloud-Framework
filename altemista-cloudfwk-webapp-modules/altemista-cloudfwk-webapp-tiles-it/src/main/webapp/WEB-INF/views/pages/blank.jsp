<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<h1>Hello, World</h1>


Language :<a href="?lang=en">English</a>|<a href="?lang=jp">Japanese</a>|<a href="?lang=es_ES">Spanish</a>
<br><br>
<p>
I18n CodeList

<table border="1">
  <c:forEach items="${CL_I18N_PRICE}" var="item">
    <tr>
      <td><c:out value="${item.key}" /></td>
      <td><c:out value="${item.value}" /></td>
    </tr>
  </c:forEach>
</table>
</p>

Current Locale : ${pageContext.response.locale}