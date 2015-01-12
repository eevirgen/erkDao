package com.erkspace.erkSimpleDAO.builder;

public class Configuration {

	private String user;

	private String password;

	private String url;

	public static class Builder {

		private Configuration configuration = new Configuration();

		public Builder url(String url) {
			this.configuration.url = url;
			return this;
		}

		public Builder user(String user) {
			this.configuration.user = user;
			return this;
		}

		public Builder password(String password) {
			this.configuration.password = password;
			return this;
		}

		public Configuration build() {
			return configuration;
		}
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return "Configuration [user=" + user + ", password=" + password
				+ ", url=" + url + "]";
	}
}
