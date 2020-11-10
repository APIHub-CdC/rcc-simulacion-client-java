package com.cdc.apihub.mx.RCC.simulacion.test;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdc.apihub.mx.RCC.simulacion.client.ApiClient;
import com.cdc.apihub.mx.RCC.simulacion.client.ApiException;
import com.cdc.apihub.mx.RCC.simulacion.client.api.RCCApi;
import com.cdc.apihub.mx.RCC.simulacion.client.model.CatalogoEstados;
import com.cdc.apihub.mx.RCC.simulacion.client.model.Consultas;
import com.cdc.apihub.mx.RCC.simulacion.client.model.Creditos;
import com.cdc.apihub.mx.RCC.simulacion.client.model.DomicilioPeticion;
import com.cdc.apihub.mx.RCC.simulacion.client.model.DomiciliosRespuesta;
import com.cdc.apihub.mx.RCC.simulacion.client.model.Empleos;
import com.cdc.apihub.mx.RCC.simulacion.client.model.PersonaPeticion;
import com.cdc.apihub.mx.RCC.simulacion.client.model.Respuesta;
import okhttp3.OkHttpClient;

public class ApiTest {
	private Logger logger = LoggerFactory.getLogger(ApiTest.class.getName());
	
	private final RCCApi api = new RCCApi();
	private ApiClient apiClient;
    private String xApiKey = "your_api_key";
    private String url = "the_url";

    @Before()
    public void setUp() {
        this.apiClient = api.getApiClient();
         this.apiClient.setBasePath(url);
         OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
         apiClient.setHttpClient(okHttpClient);
    }

	@Test
	public void getFullReportTest() throws ApiException {
		String xFullReport = "true";
		PersonaPeticion body = new PersonaPeticion();

        body.setPrimerNombre("JUAN");
        body.setApellidoPaterno("SESENTAYDOS");
        body.setApellidoMaterno("PRUEBA");
        body.setFechaNacimiento("1965-08-09");
        body.setRFC("SEPJ650809JG1");
        body.setNacionalidad("MX");

        DomicilioPeticion dom = new DomicilioPeticion();
        dom.setDireccion("PASADISO ENCONTRADO 58");
        dom.setColoniaPoblacion("MONTEVIDEO");
        dom.setDelegacionMunicipio("GUSTAVO A MADERO");
        dom.setCiudad("CIUDAD DE MÉXICO");
        dom.setEstado(CatalogoEstados.CDMX);
        dom.setCP("07730");
        body.setDomicilio(dom);

		Respuesta response = api.getReporte(this.xApiKey, body, xFullReport);
		logger.info("FullReportTest: "+response.toString());
		Assert.assertTrue(response.getFolioConsulta() != null);
	}
	
	@Test
	public void getReporteTest() throws ApiException {
		String xFullReport = "false";
		PersonaPeticion body = new PersonaPeticion();
        body.setPrimerNombre("JUAN");
        body.setApellidoPaterno("SESENTAYDOS");
        body.setApellidoMaterno("PRUEBA");
        body.setFechaNacimiento("1965-08-09");
        body.setRFC("SEPJ650809JG1");
        body.setNacionalidad("MX");

        DomicilioPeticion dom = new DomicilioPeticion();
        dom.setDireccion("PASADISO ENCONTRADO 58");
        dom.setColoniaPoblacion("MONTEVIDEO");
        dom.setDelegacionMunicipio("GUSTAVO A MADERO");
        dom.setCiudad("CIUDAD DE MÉXICO");
        dom.setEstado(CatalogoEstados.CDMX);
        dom.setCP("07730");
        body.setDomicilio(dom);

		Respuesta response = api.getReporte(this.xApiKey, body, xFullReport);
		logger.info("SegmentReportTest: "+response.toString());
		Assert.assertTrue(response.getFolioConsulta() != null);

		if (response.getFolioConsulta() != null) {
			String folioConsulta = response.getFolioConsulta();

			Consultas consultas = api.getConsultas(folioConsulta, xApiKey);
			Assert.assertTrue(consultas.getConsultas() != null);

			Creditos creditos = api.getCreditos(folioConsulta, xApiKey);
			Assert.assertTrue(creditos.getCreditos() != null);

			DomiciliosRespuesta domicilios = api.getDomicilios(folioConsulta, xApiKey);
			Assert.assertTrue(domicilios.getDomicilios() != null);

			Empleos empleos = api.getEmpleos(folioConsulta, xApiKey);
			Assert.assertTrue(empleos.getEmpleos() != null);
		}
	}
}
