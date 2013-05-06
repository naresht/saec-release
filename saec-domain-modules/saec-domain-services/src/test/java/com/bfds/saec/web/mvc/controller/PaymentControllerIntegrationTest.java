package com.bfds.saec.web.mvc.controller;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bfds.saec.external.service.dto.PaymentDto;

@Ignore
public class PaymentControllerIntegrationTest {
	
	private RestTemplate restTemplate;
	private String serverUrl =  "http://localhost:8080/saec/services";
	
	@Before
	public void before() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(3000);
		clientHttpRequestFactory.setReadTimeout(5000);
		restTemplate = new RestTemplate(clientHttpRequestFactory);
		restTemplate.setErrorHandler(new DefaultJsonResponseErrorHandler());
		
	}
	
	@Test
	public void getPaymentByCheckNo() {
		final String checkNo = "6052057280";
		PaymentDto dto = restTemplate.getForObject( serverUrl+ "/payments/"+checkNo, PaymentDto.class);
		assertThat(dto.getCheckNo()).isEqualTo(checkNo);
	}
	
	public static class DefaultJsonResponseErrorHandler extends DefaultResponseErrorHandler {
		
		public void handleError(ClientHttpResponse response) throws IOException {
			HttpStatus statusCode = response.getStatusCode();
			MediaType contentType = response.getHeaders().getContentType();
			Charset charset = contentType != null ? contentType.getCharSet() : null;
			byte[] body = getResponseBody(response);
			switch (statusCode.series()) {
				case CLIENT_ERROR:
					throw new HttpClientErrorException(statusCode, response.getStatusText() + " - " + new String(body), body, charset);
				case SERVER_ERROR:
					throw new HttpServerErrorException(statusCode, response.getStatusText() + " - " + new String(body), body, charset);
				default:
					throw new RestClientException("Unknown status code [" + statusCode + "]");
			}
		}
		
		private byte[] getResponseBody(ClientHttpResponse response) {
			try {
	            InputStream responseBody = response.getBody();
	            if (responseBody != null) {
	                return FileCopyUtils.copyToByteArray(responseBody);
	            }
			}
			catch (IOException ex) {
	            // ignore
			}
	        return new byte[0];
		}
	}	
}
