package com.Enkpay.alltransctionsPOS.nibbs.model;

import com.Enkpay.alltransctionsPOS.nibbs.miscellaneous.ConfigData;
import generalModel.KeyHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HostConfig {
    private String terminalId;
    private NibsUtilityData connectionData;
    private KeyHolder keyHolder;
    private ConfigData configData;

//    public HostConfig(String terminalId, NibsUtilityData connectionData, KeyHolder keyHolder, ConfigData configData) {
//        this.terminalId = terminalId;
//        this.connectionData = connectionData;
//        this.keyHolder = keyHolder;
//        this.configData = configData;
//    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public NibsUtilityData getConnectionData() {
        return connectionData;
    }

    public void setConnectionData(NibsUtilityData connectionData) {
        this.connectionData = connectionData;
    }

    public KeyHolder getKeyHolder() {
        return keyHolder;
    }

    public void setKeyHolder(KeyHolder keyHolder) {
        this.keyHolder = keyHolder;
    }

    public ConfigData getConfigData() {
        return configData;
    }

    public void setConfigData(ConfigData configData) {
        this.configData = configData;
    }
}
