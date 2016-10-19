<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Example</title>
</head>
<body bgcolor="silver">
	<form method="post" action="login">
		<center>
			<table border="0" width="30%" cellpadding="3">
				<thead>
					<tr>
						<th colspan="2">Login Page</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>Username</td>
						<td><input type="text" name="username" value="" /></td>
					</tr>
					<tr>
						<td>Password</td>
						<td><input type="password" name="password" value="" /></td>
					</tr>
					<tr>
						<td><input type="submit" value="Login" /></td>
						<td><input type="reset" value="Reset" /></td>
					</tr>
				</tbody>
			</table>
		</center>
	</form>
	<h3>Usuario: odatech</h3>
	<h3>Usuario: administrador</h3>
	<h3>Cualquier otro es un operador</h3>
	<h3>A cualquier usuario que se le agregue _latino pasa a ver la UI en español y en GMT-3</h3>
</body>
</html>