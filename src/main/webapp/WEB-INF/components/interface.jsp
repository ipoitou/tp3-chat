<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="user" type="fr.univlyon1.m1if.m1if03.classes.User" scope="session"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chatons !</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<header>
    <h1 class="header-titre">Chatons.org</h1>
    <p class="header-user">Bonjour <strong><a href="user" target="messages"><jsp:getProperty name="user" property="name"/></a></strong></p>
    <p><strong>${requestScope.requestResult}</strong></p>
</header>

<div class="wrapper">
    <aside class="menu">
        <h2>Menu</h2>
        <div>
            <a href="demandes" target="messages">Mes demandes</a>
            <br>
            <a href="salons" target="messages">Salons</a>
            <br>
            <a href="deco">D&eacute;connexion</a>
        </div>
    </aside>

    <article class="contenu">
        <iframe src="salons" name="messages"></iframe>
    </article>
</div>

<footer>
    <div>Licence : <a rel="license" href="https://creativecommons.org/licenses/by-nc-sa/3.0/fr/"><img alt="Licence Creative Commons" style="border-width:0; vertical-align:middle;" src="https://i.creativecommons.org/l/by-nc-sa/3.0/fr/88x31.png" /></a></div>
</footer>
</body>
</html>