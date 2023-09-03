package de.badnauheim.awsipranges.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.badnauheim.awsipranges.model.IpRange;
import de.badnauheim.awsipranges.service.AwsIpRangesService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:
 * Ivan Kablar
 * <p>
 * Description:
 * This is a service class, that is used for retrieving the ip-ranges from the Amazon AWS website through an InputStream.
 */
@Service
public class AwsIpRangesServiceImpl implements AwsIpRangesService {

    private ObjectMapper objectMapper;

    private final static String IP4PREFIXES_ARRAY_NAME = "prefixes";

    private static final String IP6PREFIXES_ARRAY_NAME = "ipv6_prefixes";

    private final static Character REGION_HYPHEN = '-';

    private final static String ALL_REGIONS = "all";

    public AwsIpRangesServiceImpl(final ObjectMapper pObjectMapper) {
        this.objectMapper = pObjectMapper;
    }

    /**
     * This method returns a list of ip-ranges provided by an InputStream, that is filtered by the region.
     *
     * @param pRegion     the region parameter for the filtering of regions.
     * @param inputStream contains the json.
     * @return a list of ip-ranges.
     * @throws IOException when the object mapper or the underlying parse-method throw an error.
     */
    @Override
    public List<IpRange> getIpRanges(String pRegion, InputStream inputStream) throws IOException {
        List<IpRange> ipRanges = new ArrayList<>();
        try (JsonParser jsonParser = objectMapper.getFactory().createParser(inputStream)) {
            parseJson(pRegion, jsonParser, ipRanges);
        }
        return ipRanges;
    }

    /**
     * This method populates a list of ip-ranges using the JsonParser. Initially, the parser moves its cursor to the
     * position of the json array "prefixes" and from there begins to map the objects inside the array to Java objects
     * of type IpRange. When the cursor reaches the json array with name "ipv6_prefixes" it parses this array as well.
     * The result can be filtered by regions using the parameter.
     *
     * @param pRegion     the parameter, substituted with a valid abbreviation for the region.
     * @param pJsonParser the JsonParser.
     * @param pIpRanges   an empty the list of ip ranges, that can be populated by this method.
     * @throws IOException if an exception during the parsing occurs.
     */
    private void parseJson(String pRegion, JsonParser pJsonParser, List<IpRange> pIpRanges) throws IOException {
        while (pJsonParser.nextToken() != JsonToken.END_OBJECT) {
            if (IP4PREFIXES_ARRAY_NAME.equals(pJsonParser.getCurrentName())) {
                parseInnerJsonArray(pRegion, pJsonParser, pIpRanges);
            }
            if (IP6PREFIXES_ARRAY_NAME.equals(pJsonParser.getCurrentName())) {
                parseInnerJsonArray(pRegion, pJsonParser, pIpRanges);
            }
        }
    }

    /**
     * This method parses an array, that starts with an <code>START_ARRAY</code> token and ends with an
     * <code>END_ARRAY</code> token. It populates a list of objects of type IpRange. The region parameter
     * is used for the filtering of the region.
     *
     * @param pRegion     the parameter, substituted with a valid abbreviation for the region.
     * @param pJsonParser the JsonParser.
     * @param pIpRanges   a list of ip ranges, that can be populated by this method.
     * @throws IOException if an exception during the parsing occurs.
     */
    private void parseInnerJsonArray(String pRegion, JsonParser pJsonParser, List<IpRange> pIpRanges) throws IOException {
        if (pJsonParser.nextToken() == JsonToken.START_ARRAY) {
            while (pJsonParser.nextToken() != JsonToken.END_ARRAY) {

                IpRange ipRange = pJsonParser.readValueAs(IpRange.class);

                if (ipRange.region().startsWith(pRegion.toLowerCase() + REGION_HYPHEN)) {
                    pIpRanges.add(ipRange);
                } else if (ALL_REGIONS.equalsIgnoreCase(pRegion)) {
                    pIpRanges.add(ipRange);
                }
            }
        }
    }

}