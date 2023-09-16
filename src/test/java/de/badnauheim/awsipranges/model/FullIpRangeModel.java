package de.badnauheim.awsipranges.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
        return this.syncToken.concat("\n" + this.createDate).concat(this.ipRanges.toString()).concat(this.ipRangesV6.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }
        FullIpRangeModel secondModel = (FullIpRangeModel) obj;
        return  this.syncToken.equals(secondModel.syncToken) &&
                this.createDate.equals(secondModel.createDate) &&
                this.ipRanges.equals(secondModel.ipRanges) &&
                this.ipRangesV6.equals(secondModel.ipRangesV6);
    }
}
