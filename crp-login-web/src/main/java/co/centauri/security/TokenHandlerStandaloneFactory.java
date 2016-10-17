package co.centauri.security;

public class TokenHandlerStandaloneFactory implements TokenHandlerFactory {

	@Override
	public TokenHandler create() {
		TokenHandler tokenHandler = new TokenHandler();
		tokenHandler.setHeaderContentType("text/plain");
		tokenHandler.setSimetricKey("eeeb0a3e-d96b-4229-9201-d9c0b1a394be");
		tokenHandler.setExpirationInMinutes(1440);
		tokenHandler.setIssuer("QSocialNow");
		tokenHandler.setAudience("http://qsocialnow.com");
		tokenHandler.setSubject("QSocialNow User");
		return tokenHandler;
	}

}
