<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<jsp:useBean id="user" scope="session" type="fr.univlyon1.m1if.m1if03.classes.User"/>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Chatons !</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<p><strong>${requestScope.requestResult}</strong></p>

<h2>Salons</h2>
<h3>Les salons dont vous êtes propriétaire</h3>
<ul>
    <c:forEach items="${requestScope.userSalonsOwned}" var="salon">
        <li>
            <form method="post">
                <em><label>Nom : <input type="text" name="salon" value="${salon.getName()}" readonly></label></em><br>
                <label>Ajout membre : <input type="text" name="membre"></label> 
                <input type="submit" name="ajoutMembre" value="Ajouter"><br>
                <input type="submit" name="suppression" value="Supprimer le salon">
            </form>
            
            <a href="messages?owner=${ownerLogin}&salonId=${salon.getId()}">Y aller</a>
        </li>
    </c:forEach>
</ul>

<h3>Créer un nouveau salon</h3>
<form method="post">
    <label>Nom : <input type="text" name="salon"></label>
    <input type="submit" name="creation" value="Créer le salon">
</form>

<h3>Les salons dont vous êtes membre</h3>
<ul>
	<c:forEach items="${requestScope.userSalonsMember}" var="salon" >
		<c:set var="ownerName" value="${salon.getOwnerName()}" />
		<c:set var="ownerLogin" value="${salon.getOwnerLogin()}" />
		
	    <li>
	        <em>${salon.getName()} (${ownerName})</em> :
	        <a href="messages?owner=${ownerLogin}&salonId=${salon.id}">y aller</a> ou
	        <form action="demandes" method="POST">
	       		<input type="hidden" name="demandeurLogin" value="${user.login}"/>
	       		<input type="hidden" name="salonOwnerLogin" value="${salon.getOwnerLogin()}"/>
	       		<input type="hidden" name="salonId" value="${salon.getId()}"/>
	       		<input type="hidden" name="action" value="quit"/>
	       		
	       		<input type="submit" name="registerButton" value="Demander à quitter ce salon" />
	        </form>
        	<!-- 
			<a href="demandes?owner=${ownerLogin}&salonId=${salon.id}&user=${user.login}&action=quit">quitter ce salon</a>
			 -->
	    </li>
	</c:forEach>
</ul>

<h3>Les salons dont vous n'êtes pas (encore) membre</h3>
<ul>
	<c:forEach items="${requestScope.salonsNotMember}" var="salon">
		<li>
	       	<em>${salon.getName()} (${salon.getOwnerLogin()})</em> :
	       	<form action="demandes" method="POST">
	       		<input type="hidden" name="demandeurLogin" value="${user.login}"/>
	       		<input type="hidden" name="salonOwnerLogin" value="${salon.getOwnerLogin()}"/>
	       		<input type="hidden" name="salonId" value="${salon.getId()}"/>
	       		<input type="hidden" name="action" value="register"/>
	       		
	       		<input type="submit" name="registerButton" value="Demander à être membre de ce salon" />
	       	</form>
	           	<!-- <a href="demandes?salonOwnerLogin=${salon.getOwnerLogin()}&salonId=${salon.getId()}&demandeurLogin=${user.login}&action=register">demander à être membre
	            de ce salon</a>  -->
	    </li>
    </c:forEach>
</ul>

</body>
</html>
