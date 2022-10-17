<%@ page import="jakarta.servlet.http.HttpServletResponse" %><%--
  Created by IntelliJ IDEA.
  User: Lionel
  Date: 08/09/2022
  Time: 17:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="userDao" type="fr.univlyon1.m1if.m1if03.daos.UserDao" scope="application"/>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>messages</title>
    <link rel="stylesheet" href="css/style.css">
    <meta http-equiv="refresh" content="5">
</head>
<body>
<h2>Messages</h2>

<p>Il y a actuellement ${requestScope.nbUserOnline} utilisateur(s) dans ce salon.</p>

<table>
    <tr><th>User</th><th>Message</th></tr>
    <c:forEach items="${requestScope.salonMessages}" var="message">
    	<tr>
    		<td><c:out value="${message.userName}"/></td>
    		<td><c:out value="${message.text}"/></td>
    	</tr>
    </c:forEach>
</table>

<hr>
<form method="post" action="messages">
    <p>
        <label for="text">Message :</label>
        <input type="text" name="text" id="text">
        <input type="submit" value="Envoyer">
        <input type="hidden" name="login" value="${sessionScope.user.login}">
    </p>
</form>

</body>
</html>
