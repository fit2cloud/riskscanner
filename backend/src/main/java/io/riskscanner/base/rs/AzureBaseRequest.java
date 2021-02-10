package io.riskscanner.base.rs;

import com.google.gson.Gson;
import io.riskscanner.commons.exception.PluginException;

public class AzureBaseRequest extends Request {
	private AzureCredential azureCredential;
	
	public AzureBaseRequest() {
		super("", "");
	}
	
	public AzureBaseRequest(Request req) {
		super(req.getCredential(), req.getRegionId());
		setCredential(req.getCredential());
		setRegionId(req.getRegionId());
	}
	
	public AzureClient getAzureClient() throws PluginException {
		try {
			if(azureCredential == null) {
				azureCredential = new Gson().fromJson(getCredential(), AzureCredential.class);
			}
			return new AzureClient(azureCredential);
		} catch (Exception e) {
			throw new PluginException(e);
		}
	}
	
}
