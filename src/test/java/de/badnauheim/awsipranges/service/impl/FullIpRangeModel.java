package de.badnauheim.awsipranges.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.badnauheim.awsipranges.model.IpRange;

import java.util.List;

/**
 * Author: Ivan Kablar
 * <p>
 * Description:
 * This record is a model of the json string from the aws website.
 */
public record FullIpRangeModel(String syncToken, String createDate, List<IpRange> ipRanges, List<IpRange> ipRangesV6) {

    public FullIpRangeModel(@JsonProperty("syncToken") String syncToken,
                            @JsonProperty("createDate") String createDate,
                            @JsonProperty("prefixes") List<IpRange> ipRanges,
                            @JsonProperty("ipv6_prefixes") List<IpRange> ipRangesV6) {
        this.syncToken = syncToken;
        this.createDate = createDate;
        this.ipRanges = ipRanges;
        this.ipRangesV6 = ipRangesV6;
    }

    @Override
    public String toString() {
        return syncToken.concat("\n" + createDate).concat(ipRanges.toString()).concat(ipRangesV6.toString());
    }

    @Override
    public boolean equals(Object obj) {
        FullIpRangeModel secondModel = (FullIpRangeModel) obj;
        return syncToken.equals(secondModel.syncToken) &&
                createDate.equals(secondModel.createDate) &&
                ipRanges.equals(secondModel.ipRanges) &&
                ipRangesV6.equals(secondModel.ipRangesV6) &&
                syncToken.equals(secondModel.syncToken) &&
                createDate.equals(secondModel.createDate);
    }
}
