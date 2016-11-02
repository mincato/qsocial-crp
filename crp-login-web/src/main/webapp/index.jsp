<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/login.css">
<link rel="shortcut icon" href="img/favicon.png" type="image/x-icon" >
<title>Login</title>
</head>
<body class="webkit chrome breeze" style="overflow: visible;">
	<div class="wrapper">
		<div class="member-container login">
			<img src="img/login-logo.png" class="login-logo" />
			<div class="member-container-inside">
				<form class="rounded" method="post" action="login">
					<div class="form-group">
						<label>User</label><input name="username"
							class="rounded form-control" type="text">
					</div>
					<div class="form-group">
						<label>Password</label><input class="rounded form-control"
							name="password" autocomplete="off" value="" type="password">
					</div>
					<div class="form-group">
						<div class="text-center">
							<button type="submit" class="btn-sm btn-lg btn-default">Log
								in</button>
						</div>
					</div>
					<p>
						<small>Copyright © 2016 QSocialNow</small>
					</p>
					<h6>Usuario: odatech</h6>
					<h6>Usuario: administrador</h6>
					<h6>Cualquier otro es un operador</h6>
					<h6>A cualquier usuario que se le agregue _latino pasa a ver
						la UI en español y en GMT-3</h6>
				</form>
			</div>

		</div>
	</div>
</html>