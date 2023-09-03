package de.badnauheim.awsipranges.controller;

import de.badnauheim.awsipranges.model.IpRange;
import de.badnauheim.awsipranges.service.AwsIpRangesService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author:
 * Ivan Kablar
 * <p>
 * Description:
 * this class represents a RestController that accepts requests on the mapping 'ipranges' and requires a valid parameter
 * in order to return a result. The parameter is used to filter ip-ranges by their regions. Valid values for the
 * parameter are EU, US, AP, CN, SA, AF, CA and ALL for all regions.
 * <p>
 * For more details about the json file holding the ip-ranges see:
 * * <a href="https://docs.aws.amazon.com/vpc/latest/userguide/aws-ip-ranges.html#aws-ip-syntax">...</a> for more
 * details.
 * <p>
 * Note that there is no need to declare the 'maxInMemorySize' for the WebClient as the parsing is so efficient,
 * that it consumes a minimum of memory.
 */
@RestController
public class AwsIpRangesController {

    private final AwsIpRangesService awsIpRangesService;

    private final WebClient webClient;

    public AwsIpRangesController(@Value("${url}") final String pUrl,
                                 AwsIpRangesService pAwsIpRangesService) {
        this.awsIpRangesService = pAwsIpRangesService;
        webClient = WebClient.builder()
                .baseUrl(pUrl)
                .build();
    }

    @RequestMapping(value = "/ipranges", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getFluxIpRange(@RequestParam(value = "region") String pRegion) throws IOException {
        Flux<DataBuffer> responseBody = getDataBufferFlux();

        List<IpRange> ipRanges = awsIpRangesService.getIpRanges(pRegion, readAsInputStream(responseBody));

        return ipRanges.stream()
                .map(IpRange::toString)
                .collect(Collectors.joining("\n\n"));
    }

    private Flux<DataBuffer> getDataBufferFlux() {
        Flux<DataBuffer> responseBody = this.webClient
                .get()
                .exchangeToFlux(clientResponse -> clientResponse.body(BodyExtractors.toDataBuffers()));
        return responseBody;
    }

    private InputStream readAsInputStream(Publisher<DataBuffer> responseBody) throws IOException {
        PipedOutputStream osPipe = new PipedOutputStream();
        PipedInputStream isPipe = new PipedInputStream(1024 * 10);

        isPipe.connect(osPipe);

        DataBufferUtils.write(responseBody, osPipe).subscribe();

        return isPipe;
    }
}
