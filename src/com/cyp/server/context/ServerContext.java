package com.cyp.server.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.cyp.accounts.Account;
import com.cyp.application.Context;
import com.cyp.application.Logger;

public class ServerContext implements Context {

	ServerLogger logger = new ServerLogger();

	@Override
	public void initialize(Object contextData) {
		// TODO Auto-generated method stub

	}

	@Override
	public InputStream getResourceAsInputStream(String resource)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Logger getLogger() {
		return this.logger;
	}

	@Override
	public List<Account> listAccounts() {
		return null;
	}

	@Override
	public void registerAccount(Account account) {
	}

	@Override
	public void removeAccount(Account account) {
	}

	@Override
	public String getApplicationName() {
		return "cyp.server.rooms";
	}

	@Override
	public String getVersion() {
		return "0.1";
	}

	@Override
	public PLATFORM getPlatform() {
		return PLATFORM.DESKTOP_JAVA;
	}

	@Override
	public List<String> getApplicationFutures() {
		return null;
	}

	public String getGameBaseURL() {
		return "http://api.chessyoup.com";
	}
}
