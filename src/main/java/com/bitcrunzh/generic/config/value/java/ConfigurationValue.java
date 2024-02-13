package com.bitcrunzh.generic.config.value.java;

import java.time.Instant;
import java.util.UUID;

public class ConfigurationValue {
    private final UUID configurationInstanceId;
    private final String lastModifiedBy;
    private final Instant timeOfLastModification;
    private final Value configurationValue;
    private final byte[] signature;

    public ConfigurationValue(UUID configurationInstanceId, String lastModifiedBy, Instant timeOfLastModification, Value configurationValue, byte[] signature) {
        this.configurationInstanceId = configurationInstanceId;
        this.lastModifiedBy = lastModifiedBy;
        this.timeOfLastModification = timeOfLastModification;
        this.configurationValue = configurationValue;
        this.signature = signature;
    }

    public UUID getConfigurationInstanceId() {
        return configurationInstanceId;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Instant getTimeOfLastModification() {
        return timeOfLastModification;
    }

    public Value getConfigurationValue() {
        return configurationValue;
    }

    public byte[] getSignature() {
        return signature;
    }
}
