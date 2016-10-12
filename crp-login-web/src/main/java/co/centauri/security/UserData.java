package co.centauri.security;


public class UserData {

	private Long id;

	private String username;

	private boolean isOdatech;

	private boolean isPrcAdmin;

	private String timezone;

	private String language;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isOdatech() {
		return isOdatech;
	}

	public void setOdatech(boolean isOdatech) {
		this.isOdatech = isOdatech;
	}

	public boolean isPrcAdmin() {
		return isPrcAdmin;
	}

	public void setPrcAdmin(boolean isPrcAdmin) {
		this.isPrcAdmin = isPrcAdmin;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
