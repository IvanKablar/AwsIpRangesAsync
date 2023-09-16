package de.badnauheim.awsipranges.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.badnauheim.awsipranges.model.FullIpRangeModel;
import de.badnauheim.awsipranges.model.IpRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Author:
 * Ivan Kablar
 * <p>
 * Description:
 * This class tests the semantics of the AwsIpRangesServiceImpl class.
 */
@SpringBootTest
class AwsIpRangesServiceImplTest {

    @Spy
    private ObjectMapper objectMapper;

    private AwsIpRangesServiceImpl awsIpRangesServiceImpl;

    private ByteArrayInputStream smallJsonInputStream;

    private FullIpRangeModel fullIpRangeModel;

    private String smallJsonString;

    @BeforeEach
    public void setup() throws IOException {
        // setup small json object test string
        smallJsonString = """
                 {
                  "syncToken": "1692296590",
                  "createDate": "2023-08-17-18-23-10",
                  "prefixes": [
                    {
                      "ip_prefix": "3.2.34.0/26",
                      "region": "af-south-1",
                      "service": "AMAZON",
                      "network_border_group": "af-south-1"
                    },
                    {
                      "ip_prefix": "3.5.140.0/22",
                      "region": "ap-northeast-2",
                      "service": "AMAZON",
                      "network_border_group": "ap-northeast-2"
                    }],
                    "ipv6_prefixes": [
                     {
                         "ipv6_prefix": "2a05:d07a:a000::/40",
                         "region": "eu-south-1",
                         "service": "AMAZON",
                         "network_border_group": "eu-south-1"
                     },
                     {
                          "ipv6_prefix": "2600:1f68:1000::/40",
                          "region": "ca-central-1",
                          "service": "AMAZON",
                          "network_border_group": "ca-central-1"
                      }]}\
                """;

        awsIpRangesServiceImpl = new AwsIpRangesServiceImpl(objectMapper);

        JsonParser fullModelJsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(
                smallJsonString.getBytes()));

        fullIpRangeModel = fullModelJsonParser.readValueAs(FullIpRangeModel.class);

        smallJsonInputStream = new ByteArrayInputStream(smallJsonString.getBytes());
    }

    @Test
    void testMappingWorksWithSmallJson() {
        Assertions.assertEquals("1692296590", fullIpRangeModel.syncToken());
        Assertions.assertEquals("2023-08-17-18-23-10", fullIpRangeModel.createDate());

        Assertions.assertEquals("3.2.34.0/26", fullIpRangeModel.ipRanges().get(0).ipPrefix());
        Assertions.assertEquals("af-south-1", fullIpRangeModel.ipRanges().get(0).region());
        Assertions.assertEquals("AMAZON", fullIpRangeModel.ipRanges().get(0).service());
        Assertions.assertEquals("af-south-1", fullIpRangeModel.ipRanges().get(0).networkBorderGroup());

        Assertions.assertEquals("3.5.140.0/22", fullIpRangeModel.ipRanges().get(1).ipPrefix());
        Assertions.assertEquals("ap-northeast-2", fullIpRangeModel.ipRanges().get(1).region());
        Assertions.assertEquals("AMAZON", fullIpRangeModel.ipRanges().get(1).service());
        Assertions.assertEquals("ap-northeast-2", fullIpRangeModel.ipRanges().get(1).networkBorderGroup());

        Assertions.assertEquals("2a05:d07a:a000::/40", fullIpRangeModel.ipRangesV6().get(0).ipPrefix());
        Assertions.assertEquals("eu-south-1", fullIpRangeModel.ipRangesV6().get(0).region());
        Assertions.assertEquals("AMAZON", fullIpRangeModel.ipRangesV6().get(0).service());
        Assertions.assertEquals("eu-south-1", fullIpRangeModel.ipRangesV6().get(0).networkBorderGroup());

        Assertions.assertEquals("2600:1f68:1000::/40", fullIpRangeModel.ipRangesV6().get(1).ipPrefix());
        Assertions.assertEquals("ca-central-1", fullIpRangeModel.ipRangesV6().get(1).region());
        Assertions.assertEquals("AMAZON", fullIpRangeModel.ipRangesV6().get(1).service());
        Assertions.assertEquals("ca-central-1", fullIpRangeModel.ipRangesV6().get(1).networkBorderGroup());
    }

    @Test
    void testParam_ALL_ReturnsAllResultsAndResultsAreInFullModel() throws IOException {
        List<IpRange> ipRanges = awsIpRangesServiceImpl.getIpRanges("all", smallJsonInputStream);

        Assertions.assertEquals(4, ipRanges.size());

        Assertions.assertTrue(ipRanges.containsAll(fullIpRangeModel.ipRanges()));
        Assertions.assertTrue(ipRanges.containsAll(fullIpRangeModel.ipRangesV6()));
    }

    @Test
    void testEUParamReturnsOneIpRange() throws IOException {
        Assertions.assertEquals(1, awsIpRangesServiceImpl.getIpRanges("eu",
                smallJsonInputStream).size());
    }

    @Test
    void testUnknownParamReturnsEmptyList() throws IOException {
        Assertions.assertEquals(0, awsIpRangesServiceImpl.getIpRanges("dummyParam",
                smallJsonInputStream).size());
    }

    @Test
    void testEqualsMethodsSucceedsWithEqualJsons() throws IOException {
        // creating a second parser
        JsonParser secondFullModelJsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(
                smallJsonString.getBytes()));
        // creating a second object of type FullIpRangeModel for comparison with the first model
        FullIpRangeModel secondFullIpRangeModel = secondFullModelJsonParser.readValueAs(FullIpRangeModel.class);
        Assertions.assertEquals(secondFullIpRangeModel, fullIpRangeModel);
    }

    @Test
    void testEqualsMethodsFailsWithUnequalJsons() throws IOException {
        // added '_XXX' to the second object of the 'prefixes' array to make this json different from the first json.
        String differingJsonString = """
                 {
                  "syncToken": "1692296590",
                  "createDate": "2023-08-17-18-23-10",
                  "prefixes": [
                    {
                      "ip_prefix": "3.2.34.0/26",
                      "region": "af-south-1",
                      "service": "AMAZON",
                      "network_border_group": "af-south-1"
                    },
                    {
                      "ip_prefix": "3.5.140.0/22_XXXX",
                      "region": "ap-northeast-2",
                      "service": "AMAZON",
                      "network_border_group": "ap-northeast-2"
                    }],
                    "ipv6_prefixes": [
                     {
                         "ipv6_prefix": "2a05:d07a:a000::/40",
                         "region": "eu-south-1",
                         "service": "AMAZON",
                         "network_border_group": "eu-south-1"
                     },
                     {
                          "ipv6_prefix": "2600:1f68:1000::/40",
                          "region": "ca-central-1",
                          "service": "AMAZON",
                          "network_border_group": "ca-central-1"
                      }]}\
                """;

        JsonParser secondFullModelJsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(
                differingJsonString.getBytes()));
        // creates a second object of type FullIpRangeModel for comparison with the first model
        FullIpRangeModel secondFullIpRangeModel = secondFullModelJsonParser.readValueAs(FullIpRangeModel.class);
        Assertions.assertNotEquals(secondFullIpRangeModel, fullIpRangeModel);
    }
}
