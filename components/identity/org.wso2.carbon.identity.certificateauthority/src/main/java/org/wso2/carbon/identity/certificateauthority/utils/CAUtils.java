package org.wso2.carbon.identity.certificateauthority.utils;

import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.base.ServerConfiguration;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.core.util.KeyStoreManager;
import org.wso2.carbon.identity.certificateauthority.CaConfigurations;
import org.wso2.carbon.identity.certificateauthority.Constants;
import org.wso2.carbon.identity.certificateauthority.data.CertAuthException;

import java.security.Key;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class CAUtils {

    public static X509Certificate getConfiguredCaCert() {

        int tenantID = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        KeyStoreManager keyStoreManager = KeyStoreManager.getInstance(tenantID);
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        try {
            String keyStore = getKeyStoreName();
            String alias = getAlias();

            if (keyStore == null) {
                if (tenantID == MultitenantConstants.SUPER_TENANT_ID) {
                    return keyStoreManager.getDefaultPrimaryCertificate();
                } else {
                    String ksName = tenantDomain.trim().replace(".", "-");
                    String jksName = ksName + ".jks";
                    return (X509Certificate) keyStoreManager.getKeyStore(jksName).getCertificate(tenantDomain);
                }
            }
            return (X509Certificate) keyStoreManager.getKeyStore(keyStore).getCertificate(alias);
        } catch (CertAuthException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PrivateKey getConfiguredPrivateKey() {
        int tenantID = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        KeyStoreManager keyStoreManager = KeyStoreManager.getInstance(tenantID);
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        try {
            String keystore = getKeyStoreName();
            String alias = getAlias();

            if (keystore == null) {
                if (tenantID == MultitenantConstants.SUPER_TENANT_ID) {
                    return keyStoreManager.getDefaultPrivateKey();
                } else {
                    String ksName = tenantDomain.trim().replace(".", "-");
                    String jksName = ksName + ".jks";
                    return (PrivateKey) keyStoreManager.getPrivateKey(jksName, tenantDomain);
                }
            }

            Key privateKey = keyStoreManager.getPrivateKey(keystore, alias);
            return (PrivateKey) privateKey;
        } catch (CertAuthException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getKeyStoreName() throws CertAuthException {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        return CaConfigurations.getKeyStoreName(tenantId);
    }

    public static String getAlias() throws CertAuthException {
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        return CaConfigurations.getAlias(tenantId);
    }

    public static String getServerURL() {
        int offset = Integer.parseInt(ServerConfiguration.getInstance().getFirstProperty(Constants.PORT_OFFSET));
        return "http://" + ServerConfiguration.getInstance().getFirstProperty(Constants.HOST_NAME) + ":" + (Constants.DEFAULT_HTTP_PORT + offset);
    }
}
