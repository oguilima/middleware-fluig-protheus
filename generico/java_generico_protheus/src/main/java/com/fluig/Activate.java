package com.fluig;

import com.fluig.customappkey.Keyring;
import com.fluig.sdk.api.component.activation.ActivationEvent;
import com.fluig.sdk.api.component.activation.ActivationListener;

import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Singleton(mappedName = "activator/java_generico_protheus", name = "activator/java_generico_protheus")
public class Activate implements ActivationListener {
	public String getArtifactFileName() throws Exception {
        return "java_generico_protheus.war";
    }
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void install(ActivationEvent event) throws Exception {
    }

    public void disable(ActivationEvent evt) throws Exception {
    }

    public void enable(ActivationEvent evt) throws Exception {
        Keyring.provision(RestConstant.APP_KEY);
    }
}