package de.badnauheim.awsipranges.service;


import de.badnauheim.awsipranges.model.IpRange;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface AwsIpRangesService {
    List<IpRange> getIpRanges(String pRegion, InputStream responseBody) throws IOException;
}
