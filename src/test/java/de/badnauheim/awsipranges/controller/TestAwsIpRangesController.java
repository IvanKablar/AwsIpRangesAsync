package de.badnauheim.awsipranges.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestAwsIpRangesController {

    @Autowired
    private WebTestClient webTestClient;

    private WebTestClient.BodyContentSpec testWebClientResult;


    private @Value("${url}") String url;

    @BeforeEach
    public void setup() {

        // setup WebTestClientResponse holding full json from aws website
        testWebClientResult = webTestClient.mutate()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(1024 * 4096))
                .build().get().uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();
    }

    @Test
    void testRelevantAttributesNotEmpty() {
        testWebClientResult
                .jsonPath("$.syncToken").isNotEmpty()
                .jsonPath("$.createDate").isNotEmpty()
                .jsonPath("$.prefixes").isArray()
                .jsonPath("$.prefixes").isNotEmpty()
                .jsonPath("$.prefixes[0]").isNotEmpty() // assume first ip-range object inside array should never be empty
                .jsonPath("$.ipv6_prefixes").isArray()
                .jsonPath("$.ipv6_prefixes").isNotEmpty()
                .jsonPath("$.ipv6_prefixes[0]").isNotEmpty();  // assume first ip-range object inside array should never be empty;
    }

//    @Test
//    void testIpRangesStartWithAp() throws IOException {
//
//        List<IpRange> ipRanges = awsIpRangesServiceImpl.getIpRanges("ap", fullJsonInputStream);
//        Assertions.assertTrue(ipRanges.size()>1);
//        for(IpRange ipRange : ipRanges) {
//            String ipRangeRegion = ipRange.getRegion();
//            Assertions.assertEquals(true, ipRangeRegion.startsWith("ap"));
//        }
//    }
}
