package Cotizacion;


import com.borland.silktest.jtf.BrowserBaseState;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import com.borland.silktest.jtf.xbrowser.DomElement;
import com.borland.silktest.jtf.xbrowser.DomListBox;
import com.borland.silktest.jtf.xbrowser.DomTextField;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import freemarker.template.utility.CaptureOutput;

import com.borland.silktest.jtf.xbrowser.DomRadioButton;
import com.borland.silktest.jtf.xbrowser.DomCheckBox;
import com.borland.silktest.jtf.xbrowser.BrowserApplication;
import com.borland.silktest.jtf.xbrowser.DomButton;
import com.borland.silktest.jtf.Desktop;

import com.aventstack.*;
import com.aventstack.extentreports.observer.entity.MediaEntity.MediaEntityBuilder;

//import org.test;

public class Cotizar {

	private Desktop desktop = new Desktop();
	static ExtentTest test;
	static ExtentReports report;
	
	@BeforeClass
	public static void ComienzaTest()


	{
	report = new ExtentReports(System.getProperty("user.dir")+"\\CotizaAutoGNP\\ExtentReportResultadosCotizacion.html");

	//C:\Users\QAIT-0069\eclipse-workspace\CotizaAutoGNP\CotizaAutoGNP-report
	test = report.startTest("ExtentDemoCotiza");
	
	}
	
	@Before
	public void baseState() {

		// Go to web page 'www.gnp.com.mx'
		BrowserBaseState baseState = new BrowserBaseState();
		baseState.execute(desktop);
	}
	

	@Test
	public void Cotiza() {
		
		desktop.<DomElement>find("gnp_com_mx.BrowserWindow.×").click();
		desktop.<DomElement>find("gnp_com_mx.BrowserWindow.section-carro").click();
		desktop.<DomListBox>find("gnp_com_mx.BrowserWindow.tipo_vehiculo").select("PICK UP");
		desktop.<DomListBox>find("gnp_com_mx.BrowserWindow.modelo").select("2017");
		desktop.<DomListBox>find("gnp_com_mx.BrowserWindow.marca").select("FORD");
		desktop.<DomListBox>find("gnp_com_mx.BrowserWindow.submarca").select("F 150");
		desktop.<DomListBox>find("gnp_com_mx.BrowserWindow.descripcion")
				.select("FORD F-150 5.0 XL REG. CAB. 4X2 V8 AUT.");
		desktop.<DomTextField>find("gnp_com_mx.BrowserWindow.edad").setText("26");
		desktop.<DomRadioButton>find("gnp_com_mx.BrowserWindow.genero").select();
		desktop.<DomTextField>find("gnp_com_mx.BrowserWindow.cp").setText("55070");
		desktop.<DomTextField>find("gnp_com_mx.BrowserWindow.email").setText("luis@gmail.com");
	//  desktop.<DomListBox>find("gnp_com_mx.BrowserWindow.tipo_tel").select("Celular");
		desktop.<DomTextField>find("gnp_com_mx.BrowserWindow.tel").setText("5507859563");
		desktop.<DomTextField>find("gnp_com_mx.BrowserWindow.browser-default ng-p").setText("");
		desktop.<DomListBox>find("gnp_com_mx.BrowserWindow.contacto").select("Sí");
		desktop.<DomCheckBox>find("gnp_com_mx.BrowserWindow.browser-default ng-p").select(1);
		desktop.<DomButton>find("gnp_com_mx.BrowserWindow.cotizar").click();
		

	try {

		
		ExcelUtils xlsx = new ExcelUtils("C:\\Users\\QAIT-0069\\eclipse-workspace\\CotizaCarroGNP\\DatosCotizacionCoche.xlsx");
		
		xlsx.selectSheet("Hoja 1");
		xlsx.setHeaderRow(0);
		
		String[] encabezado = xlsx.getRowStringValues(0);
		
		ArrayList<String[]> data = xlsx.getSheetDataAsString();
		
		
		for (String[] col : data) {
			
			for (int i = 0; i < col.length; i++) {
				System.setProperty(encabezado[i],col[i]);
	            //System.out.printf(encabezado[i] );     
	            //System.out.printf("  " );
	            System.out.printf(col[i] );
			} // fin for
			
			try {
				//System.setProperty("", _"");
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		} // fin foreach
		
		

	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		 System.out.printf("Error" + e);
	}

//	if(desktop.getText().equals("GNP")){
test.log(LogStatus.PASS, "Cotizacion correcta");
test.log(LogStatus.INFO, "HTML", "Usage: <b>hola mundo</b>");
test.assignAuthor("Luis Antonio Guzman");
test.assignCategory("Prueba Funcional");
test.addScreenCapture("base64String");
//	
//	}else{
//	test.log(LogStatus.FAIL, "Test Failed");
//	//test.log(LogStatus.FAIL,test.addScreenCapture(null)+ "Test Failed");
		}
	
	

	@AfterClass
	public static void endTest()
	{
	report.endTest(test);
	report.flush();
	}
	
}