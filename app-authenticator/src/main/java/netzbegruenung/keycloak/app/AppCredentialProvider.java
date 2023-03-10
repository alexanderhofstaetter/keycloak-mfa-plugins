package netzbegruenung.keycloak.app;

import netzbegruenung.keycloak.app.credentials.AppCredentialModel;
import org.jboss.logging.Logger;
import org.keycloak.common.util.Time;
import org.keycloak.credential.*;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.stream.Stream;

public class AppCredentialProvider implements CredentialProvider<AppCredentialModel>, CredentialInputValidator {

	private static final Logger logger = Logger.getLogger(AppCredentialProvider.class);

	protected KeycloakSession session;
	public AppCredentialProvider(KeycloakSession session) {
		this.session = session;
	}

	@Override
	public String getType() {
		return AppCredentialModel.TYPE;
	}

	@Override
	public CredentialModel createCredential(RealmModel realm, UserModel user, AppCredentialModel appCredentialModel) {
		if (appCredentialModel.getCredentialData() == null) {
			appCredentialModel.setCreatedDate(Time.currentTimeMillis());
		}
		return user.credentialManager().createStoredCredential(appCredentialModel);
	}

	@Override
	public boolean deleteCredential(RealmModel realm, UserModel user, String credentialId) {
		return user.credentialManager().removeStoredCredentialById(credentialId);
	}

	@Override
	public AppCredentialModel getCredentialFromModel(CredentialModel credentialModel) {
		return AppCredentialModel.createFromCredentialModel(credentialModel);
	}

	@Override
	public CredentialTypeMetadata getCredentialTypeMetadata(CredentialTypeMetadataContext metadataContext) {
		return CredentialTypeMetadata.builder()
			.type(getType())
			.category(CredentialTypeMetadata.Category.TWO_FACTOR)
			.displayName(AppCredentialProviderFactory.PROVIDER_ID)
			.helpText("app-authenticator-text")
			.createAction(AppRequiredAction.PROVIDER_ID)
			.removeable(true)
			.build(session);
	}

	@Override
	public boolean supportsCredentialType(String credentialType) {
		return getType().equals(credentialType);
	}

	@Override
	public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
		if (!supportsCredentialType(credentialType)) {
			return false;
		}
		return user.credentialManager().getStoredCredentialsByTypeStream(credentialType).findAny().isPresent();
	}

	@Override
	public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
		return false;
	}

}
