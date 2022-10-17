<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Chatons !</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h2>Mon compte</h2>

<p><strong>${requestScope.requestResult}</strong></p>

<form method="post" action="user" target="messages">
    <p>
        <label>Login : <input type="text" name="login" value="${sessionScope.user.login}" readonly></label><br>
        <label>Prénom : <input type="text" name="name" value="${sessionScope.user.name}"></label><br>
        <input type="hidden" name="action" value="updateUser">
        <input type="submit" value="Enregistrer">
    </p>
</form>
</body>
</html>
