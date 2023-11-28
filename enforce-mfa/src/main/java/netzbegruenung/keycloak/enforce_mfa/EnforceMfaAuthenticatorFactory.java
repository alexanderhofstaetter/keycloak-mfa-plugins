package netzbegruenung.keycloak.enforce_mfa;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;

public class EnforceMfaAuthenticatorFactory implements AuthenticatorFactory {

	public static final String PROVIDER_ID = "enforce-mfa";

	private static AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
		AuthenticationExecutionModel.Requirement.ALTERNATIVE,
		AuthenticationExecutionModel.Requirement.DISABLED,
		AuthenticationExecutionModel.Requirement.REQUIRED
	};
	@Override
	public String getDisplayType() {
		return "Enforce MFA";
	}

	@Override
	public String getReferenceCategory() {
		return null;
	}

	@Override
	public boolean isConfigurable() {
		return false;
	}

	@Override
	public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
		return REQUIREMENT_CHOICES;
	}

	@Override
	public boolean isUserSetupAllowed() {
		return false;
	}

	@Override
	public String getHelpText() {
		return "This authenticator must always be encapsulated in conditional subflow. Eligible choices must be configured by the first adjacent subflow.";
	}

	@Override
	public List<ProviderConfigProperty> getConfigProperties() {
		return null;
	}

	@Override
	public Authenticator create(KeycloakSession keycloakSession) {
		return new EnforceMfaAuthenticator();
	}

	@Override
	public void init(Config.Scope scope) {

	}

	@Override
	public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

	}

	@Override
	public void close() {

	}

	@Override
	public String getId() {
		return PROVIDER_ID;
	}
}
