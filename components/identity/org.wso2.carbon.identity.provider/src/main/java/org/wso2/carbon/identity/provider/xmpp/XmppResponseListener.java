/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.wso2.carbon.identity.provider.xmpp;

import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Message;


public class XmppResponseListener implements org.jivesoftware.smack.PacketListener {

    private boolean responseReceived;
    private String response;

    /**
     * Capturing the packets coming from the user and setting the responseReceived flag.
     * @param packet
     */
    public void processPacket(Packet packet) {
        Message message = (Message) packet;
        if(message.getBody() == null){
            return;
        }
        response = message.getBody();
        this.responseReceived = true;
    }

    /**
     *
     * @return
     */
    public boolean isResponseReceived() {
        return responseReceived;
    }

    /**
     *
     * @return
     */
    public String getResponse() {
        return response;
    }

    /**
     *
     * @param responseReceived
     */
    public void setResponseReceived(boolean responseReceived) {
        this.responseReceived = responseReceived;
    }
}