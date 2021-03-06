/*
 *Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *WSO2 Inc. licenses this file to you under the Apache License,
 *Version 2.0 (the "License"); you may not use this file except
 *in compliance with the License.
 *You may obtain a copy of the License at
 *
 *http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing,
 *software distributed under the License is distributed on an
 *"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *KIND, either express or implied.  See the License for the
 *specific language governing permissions and limitations
 *under the License.
 */

package org.wso2.carbon.identity.application.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiceProvider implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1018969519851610663L;
    private static final Log log = LogFactory.getLog(ServiceProvider.class);

    private int applicationID = 0;
    private String applicationName;
    private String description;
    private User owner;
    private InboundAuthenticationConfig inboundAuthenticationConfig;
    private LocalAndOutboundAuthenticationConfig localAndOutBoundAuthenticationConfig;
    private RequestPathAuthenticatorConfig[] requestPathAuthenticatorConfigs;
    private InboundProvisioningConfig inboundProvisioningConfig;
    private OutboundProvisioningConfig outboundProvisioningConfig;
    private ClaimConfig claimConfig;
    private PermissionsAndRoleConfig permissionAndRoleConfig;
    private boolean saasApp;

    /**
     * 
     * @return
     */
    public int getApplicationID() {
        return applicationID;
    }

    /**
     * 
     * @param applicationID
     */
    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    /**
     * 
     * @return
     */
    public InboundAuthenticationConfig getInboundAuthenticationConfig() {
        return inboundAuthenticationConfig;
    }

    /**
     * 
     * @param inboundAuthenticationConfig
     */
    public void setInboundAuthenticationConfig(
            InboundAuthenticationConfig inboundAuthenticationConfig) {
        this.inboundAuthenticationConfig = inboundAuthenticationConfig;
    }

    /**
     * 
     * @return
     */
    public LocalAndOutboundAuthenticationConfig getLocalAndOutBoundAuthenticationConfig() {
        return localAndOutBoundAuthenticationConfig;
    }

    /**
     * 
     * @param localAndOutBoundAuthenticationConfig
     */
    public void setLocalAndOutBoundAuthenticationConfig(
            LocalAndOutboundAuthenticationConfig localAndOutBoundAuthenticationConfig) {
        this.localAndOutBoundAuthenticationConfig = localAndOutBoundAuthenticationConfig;
    }

    /**
     * 
     * @param requestPathAuthenticatorConfigs
     */
    public void setRequestPathAuthenticatorConfigs(
            RequestPathAuthenticatorConfig[] requestPathAuthenticatorConfigs) {
        this.requestPathAuthenticatorConfigs = requestPathAuthenticatorConfigs;
    }

    /**
     * 
     * @return
     */
    public RequestPathAuthenticatorConfig[] getRequestPathAuthenticatorConfigs() {
        return requestPathAuthenticatorConfigs;
    }

    /**
     * 
     * @return
     */
    public InboundProvisioningConfig getInboundProvisioningConfig() {
        return inboundProvisioningConfig;
    }

    /**
     * 
     * @param inboundProvisioningConfig
     */
    public void setInboundProvisioningConfig(InboundProvisioningConfig inboundProvisioningConfig) {
        this.inboundProvisioningConfig = inboundProvisioningConfig;
    }

    /**
     * 
     * @return
     */
    public OutboundProvisioningConfig getOutboundProvisioningConfig() {
        return outboundProvisioningConfig;
    }

    /**
     * 
     * @param outboundProvisioningConfig
     */
    public void setOutboundProvisioningConfig(OutboundProvisioningConfig outboundProvisioningConfig) {
        this.outboundProvisioningConfig = outboundProvisioningConfig;
    }

    /**
     * 
     * @return
     */
    public ClaimConfig getClaimConfig() {
        return claimConfig;
    }

    /**
     * 
     * @param claimConfig
     */
    public void setClaimConfig(ClaimConfig claimConfig) {
        this.claimConfig = claimConfig;
    }

    /**
     * 
     * @return
     */
    public PermissionsAndRoleConfig getPermissionAndRoleConfig() {
        return permissionAndRoleConfig;
    }

    /**
     * 
     * @param permissionAndRoleConfig
     */
    public void setPermissionAndRoleConfig(PermissionsAndRoleConfig permissionAndRoleConfig) {
        this.permissionAndRoleConfig = permissionAndRoleConfig;
    }

    /**
     * 
     * @return
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * 
     * @param applicationName
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * 
     * @return
     */
    public User getOwner() {
        return owner;
    }

    /**
     * 
     * @param owner
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSaasApp() {
        return saasApp;
    }

    public void setSaasApp(boolean saasApp) {
        this.saasApp = saasApp;
    }

    /*
     * <ServiceProvider> <ApplicationID></ApplicationID> <Description></Description>
     * <Owner>....</Owner>
     * <IsSaaSApp>....</IsSaaSApp><InboundAuthenticationConfig>..</InboundAuthenticationConfig>
     * <LocalAndOutBoundAuthenticationConfig>..</LocalAndOutBoundAuthenticationConfig>
     * <RequestPathAuthenticatorConfigs>...</RequestPathAuthenticatorConfigs>
     * <InboundProvisioningConfig>...</InboundProvisioningConfig>
     * <OutboundProvisioningConfig>..</OutboundProvisioningConfig>
     * <PermissionAndRoleConfig>...</PermissionAndRoleConfig> <ClaimConfig>...</ClaimConfig>
     * </ServiceProvider>
     */
    public static ServiceProvider build(OMElement serviceProviderOM) {

        ServiceProvider serviceProvider = new ServiceProvider();
        
        // by default set to true.
        serviceProvider.setSaasApp(true);

        Iterator<?> iter = serviceProviderOM.getChildElements();

        while (iter.hasNext()) {

            OMElement element = (OMElement) (iter.next());
            String elementName = element.getLocalName();

            if (elementName.equals("ApplicationID")) {
                if (element.getText() != null) {
                    serviceProvider.setApplicationID(Integer.parseInt(element.getText()));
                }
            } else if (elementName.equals("ApplicationName")) {
                if (element.getText() != null) {
                    serviceProvider.setApplicationName(element.getText());
                } else {
                    log.error("Service provider not loaded from the file. Application Name is null.");
                    return null;
                }
            } else if (elementName.equals("Description")) {
                serviceProvider.setDescription(element.getText());
            } else if (elementName.equals("IsSaaSApp")) {
                if (element.getText() != null && "true".equals(element.getText())) {
                    serviceProvider.setSaasApp(true);
                } else {
                    serviceProvider.setSaasApp(false);
                }
            } else if (elementName.equals("Owner")) {
                // build service provider owner.
                serviceProvider.setOwner(User.build(element));
            } else if (elementName.equals("InboundAuthenticationConfig")) {
                // build in-bound authentication configuration.
                serviceProvider.setInboundAuthenticationConfig(InboundAuthenticationConfig
                        .build(element));
            } else if (elementName.equals("LocalAndOutBoundAuthenticationConfig")) {
                // build local and out-bound authentication configuration.
                serviceProvider
                        .setLocalAndOutBoundAuthenticationConfig(LocalAndOutboundAuthenticationConfig
                                .build(element));
            } else if (elementName.equals("RequestPathAuthenticatorConfigs")) {
                // build request-path authentication configurations.
                Iterator<?> requestPathAuthenticatorConfigsIter = element.getChildElements();

                if (requestPathAuthenticatorConfigsIter == null) {
                    continue;
                }

                ArrayList<RequestPathAuthenticatorConfig> requestPathAuthenticatorConfigsArrList;
                requestPathAuthenticatorConfigsArrList = new ArrayList<RequestPathAuthenticatorConfig>();

                while (requestPathAuthenticatorConfigsIter.hasNext()) {
                    OMElement requestPathAuthenticatorConfigsElement = (OMElement) (requestPathAuthenticatorConfigsIter
                            .next());
                    RequestPathAuthenticatorConfig reqConfig = RequestPathAuthenticatorConfig
                            .build(requestPathAuthenticatorConfigsElement);

                    if (reqConfig != null) {
                        // we only need not-null values.
                        requestPathAuthenticatorConfigsArrList.add(reqConfig);
                    }
                }

                if (requestPathAuthenticatorConfigsArrList.size() > 0) {
                    // add to the service provider, only if we have any.
                    RequestPathAuthenticatorConfig[] requestPathAuthenticatorConfigsArr;
                    requestPathAuthenticatorConfigsArr = requestPathAuthenticatorConfigsArrList
                            .toArray(new RequestPathAuthenticatorConfig[0]);
                    serviceProvider
                            .setRequestPathAuthenticatorConfigs(requestPathAuthenticatorConfigsArr);
                }

            } else if (elementName.equals("InboundProvisioningConfig")) {
                // build in-bound provisioning configuration.
                serviceProvider.setInboundProvisioningConfig(InboundProvisioningConfig
                        .build(element));
            } else if (elementName.equals("OutboundProvisioningConfig")) {
                // build out-bound provisioning configuration.
                serviceProvider.setOutboundProvisioningConfig(OutboundProvisioningConfig
                        .build(element));
            } else if (elementName.equals("ClaimConfig")) {
                // build claim configuration.
                serviceProvider.setClaimConfig(ClaimConfig.build(element));
            } else if (elementName.equals("PermissionAndRoleConfig")) {
                // build permission and role configuration.
                serviceProvider.setPermissionAndRoleConfig(PermissionsAndRoleConfig.build(element));
            } 

        }

        return serviceProvider;
    }

}