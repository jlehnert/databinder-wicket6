package net.databinder.components;

import net.databinder.AuthDataSession;
import wicket.authentication.panel.SignInPanel;

public class DataSignInPanel extends SignInPanel {
	public DataSignInPanel(String id) {
		super(id);
	}
	
	@Override
	public boolean signIn(String username, String password) {
		return ((AuthDataSession) getSession()).signIn(username, password);
	}
}