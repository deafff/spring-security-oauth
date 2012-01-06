package org.springframework.security.oauth2.provider;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * An OAuth 2 authentication token can contain two authentications: one for the client and one for the user. Since
 * some OAuth authorization grants don't require user authentication, the user authentication may be null.
 * 
 * @author Ryan Heaton
 */
public class OAuth2Authentication extends AbstractAuthenticationToken {

	private static final long serialVersionUID = -4809832298438307309L;

	private final AuthorizationRequest clientAuthentication;
	private final Authentication userAuthentication;

	/**
	 * Construct an OAuth 2 authentication. Since some OAuth authorization grants don't require user authentication, the user
	 * authentication may be null.
	 * 
	 * @param clientAuthentication The client authentication (may NOT be null).
	 * @param userAuthentication The user authentication (possibly null).
	 */
	public OAuth2Authentication(AuthorizationRequest clientAuthentication, Authentication userAuthentication) {
		super(userAuthentication == null ? clientAuthentication.getAuthorities() : userAuthentication.getAuthorities());
		this.clientAuthentication = clientAuthentication;
		this.userAuthentication = userAuthentication;
	}

	public Object getCredentials() {
		return "";
	}

	public Object getPrincipal() {
		return this.userAuthentication == null ? this.clientAuthentication.getClientId() : this.userAuthentication
				.getPrincipal();
	}

	/**
	 * The client authentication.
	 * 
	 * @return The client authentication.
	 */
	public AuthorizationRequest getAuthorizationRequest() {
		return clientAuthentication;
	}

	/**
	 * The user authentication.
	 * 
	 * @return The user authentication.
	 */
	public Authentication getUserAuthentication() {
		return userAuthentication;
	}

	@Override
	public boolean isAuthenticated() {
		return this.clientAuthentication.isAuthenticated()
				&& (this.userAuthentication == null || this.userAuthentication.isAuthenticated());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof OAuth2Authentication)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}

		OAuth2Authentication that = (OAuth2Authentication) o;

		if (!clientAuthentication.equals(that.clientAuthentication)) {
			return false;
		}
		if (userAuthentication != null ? !userAuthentication.equals(that.userAuthentication)
				: that.userAuthentication != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + clientAuthentication.hashCode();
		result = 31 * result + (userAuthentication != null ? userAuthentication.hashCode() : 0);
		return result;
	}
}
