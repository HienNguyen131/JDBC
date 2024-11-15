<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>JDBC and Database</title>
    <link rel="stylesheet" href="./main.css" type="text/css"/>
</head>
<body>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${sqlStatement == null}">
    <c:set var="sqlStatement" value="SELECT * FROM userdb" />
</c:if>

<h1>The SQL Gateway</h1>
<p>Enter an SQL statement and click the Execute button.</p>
<p><b>SQL statement:</b></p>

<form action="sqlGateway" method="post">
    <textarea name="sqlStatement" cols="60" rows="8">${sqlStatement}</textarea>
    <br>
    <input type="submit" value="Execute">
</form>

<p><b>SQL result:</b></p>
<c:out value="${sqlResult}" escapeXml="false" />

</body>
</html>
