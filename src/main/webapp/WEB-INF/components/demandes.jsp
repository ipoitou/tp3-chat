<%@ page import="jakarta.servlet.http.HttpServletResponse" %>
<%@ page import="javax.naming.NameAlreadyBoundException" %>
<%@ page import="javax.naming.NameNotFoundException" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<jsp:useBean id="user" scope="session" type="fr.univlyon1.m1if.m1if03.classes.User" />

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Chatons !</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h2>Mes demandes</h2>

<p>Cette page contient uniquement les demandes qui vous ont été adressées. Celle contenant les demandes que vous avez envoyées n'a pas été implémentée...</p>

<p><strong>${requestScope.requestResult}</strong></p>

<table>
    <thead>
    <tr>
        <td>Auteur</td>
        <td>Salon</td>
        <td>Action demandée</td>
        <td>(&Eacute;tat)</td>
    </tr>
    </thead>
    <tbody>

    <c:forEach items="${requestScope.userDemandesToId.keySet()}" var="demande">
    	<c:set var="demandeId" value="${requestScope.userDemandesToId.get(demande)}"/>
        <tr>
            <td>${demande.demandeurLogin}</td>
            <td>${demande.salonId}</td>
            <td>${demande.action}</td>
            <td>(${demande.state})</td>
            <c:if test="${demande.state.equals('En cours')}">
                <td><a href="?idDemande=${demandeId}&result=accept">accepter</a></td>
                <td><a href="?idDemande=${demandeId}&result=refuse">refuser</a></td>
            </c:if>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
