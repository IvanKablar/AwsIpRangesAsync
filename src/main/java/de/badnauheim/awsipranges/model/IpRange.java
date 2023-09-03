package de.badnauheim.awsipranges.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: Ivan Kablar
 * <p>
 * Description:
 * This record is a model of the json object from the json string from the aws website. It contains information about
 * ip-ranges.
 */
public record IpRange(String ipPrefix, String region, String service, String networkBorderGroup) {
    public IpRange(@JsonProperty(value = "ip_prefix") @JsonAlias(value = "ipv6_prefix") String ipPrefix,
                   @JsonProperty(value = "region") String region,
                   @JsonProperty(value = "service") String service,
                   @JsonProperty(value = "network_border_group") String networkBorderGroup) {

        this.ipPrefix = ipPrefix;
        this.region = region;
        this.service = service;
        this.networkBorderGroup = networkBorderGroup;
    }

    @Override
    public String toString() {
        return "ip_prefix: " + ipPrefix() + "\n"
                + "region: " + region() + "\n"
                + "service: " + service() + "\n"
                + "network_border_group: " + networkBorderGroup();
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }
        return this.ipPrefix.equals(((IpRange) obj).ipPrefix) &&
                this.region.equals(((IpRange) obj).region) &&
                this.service.equals(((IpRange) obj).service) &&
                this.networkBorderGroup.equals(((IpRange) obj).networkBorderGroup);
    }
}

