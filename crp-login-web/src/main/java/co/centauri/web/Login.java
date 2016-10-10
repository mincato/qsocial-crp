package co.centauri.web;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.centauri.security.TokenHandler;
import co.centauri.security.TokenHandlerFactory;
import co.centauri.security.TokenHandlerStandaloneFactory;
import co.centauri.security.UserData;

public class Login extends HttpServlet {

	private static final long serialVersionUID = 1627012520199702295L;

	private static final String ADMIN = "administrador";

	private static final String ODATECH = "odatech";

	private TokenHandler tokenHandler;
	
	@Override
	public void init() throws ServletException {
		TokenHandlerFactory factory = new TokenHandlerStandaloneFactory();
		tokenHandler = factory.create();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		UserData user = new UserData();
		user.setUsername(username);
		user.setOdatech(ODATECH.compareToIgnoreCase(username) == 0);
		user.setPrcAdmin(ADMIN.compareToIgnoreCase(username) == 0);
		String url = getCrpTicketManagerWebUrl(req);
		String token = tokenHandler.createToken(user);
		resp.setHeader("Authorization", "Bearer ".concat(token));
		resp.sendRedirect(url);
	}

	public String getCrpTicketManagerWebUrl(HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		String serverPort = Integer.valueOf(request.getServerPort()).toString();
		String contextPath = "crp-ticket-manager-web";
		return MessageFormat.format("{0}://{1}:{2}/{3}", scheme, serverName, serverPort, contextPath);
	}
}
