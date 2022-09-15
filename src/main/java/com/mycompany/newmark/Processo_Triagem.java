/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena 
 * 
 */
package com.mycompany.newmark;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.mycompany.newmark.Controllers.ControllerMaternida;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mycompany.newmark.system.Sistema;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 *
 * @author admin-felipe
 */
public class Processo_Triagem {

	public Processo_Triagem(WebDriver driver, String banco) throws ParseException {
		super();
	}

	//Inicia a Triagem
	public boolean iniciarTriagem(WebDriver driver, WebDriverWait wait, String bancos, boolean triagemIniciada, Chaves_Configuracao debugPi, String etiquetaFiltro)
			throws SQLException, InterruptedException, UnsupportedFlavorException, IOException {

		Actions actions = new Actions(driver);
		Chaves_Resultado resultado = new Chaves_Resultado();
		resultado.setEtiqueta("NÃO FOI POSSÍVEL LOCALIZAR FRASE CHAVE ATUALIZADA");
		resultado.setDriver(driver);
		Chaves_Configuracao config = new Chaves_Configuracao();
		Banco banco = new Banco();
		config = banco.pegarConfiguracao(config);
		Processo_Grid processo_grid = new Processo_Grid(resultado.getDriver());
		Processo_Movimentacao movimentacao = new Processo_Movimentacao();
		Processo_Documento documento = new Processo_Documento();
		Processo_Pericial pericial = new Processo_Pericial();
		ControllerMaternida controllerMaternida = new ControllerMaternida();
		ControllerMaternida peticao = new ControllerMaternida();
		Processo_Etiquetar etiqueta = new Processo_Etiquetar();
//		URL url = getClass().getResource("/SOUNDS/ferrolho.wav"); // Som duvidoso
//		AudioClip clip = Applet.newAudioClip(url);
//		clip.play();
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div/div[4]/div/table/tbody/tr[1]/td[3]/div/a[1]")));
		wait.until(ExpectedConditions.elementToBeClickable(By
				.xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div/div[4]/div/table/tbody/tr[1]/td[3]/div/a[1]")));
		boolean confirmacaoDeLogin1 = driver
				.findElement(
						By.xpath("/html/body/div[4]/div[1]/div[2]/div/div[1]/div[1]/div[2]/div/a[2]/span/span/span[1]"))

				.getText().toUpperCase().contains("Tramitações");
		boolean confirmacaoDeLogin2 = driver
				.findElement(
						By.xpath("/html/body/div[4]/div[1]/div[2]/div/div[1]/div[1]/div[2]/div/a[3]/span/span/span[1]"))
				.getText().toUpperCase().contains("Comunicações");
		boolean grid;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS).pageLoadTimeout(30, TimeUnit.SECONDS);
		WebElement setaAparecer = driver.findElement(
				By.xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div[1]/div[3]/div/div/div[33]/div/span"));
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div[1]/div[3]/div/div/div[33]/div/span")));
		setaAparecer.click();
		WebElement seta = driver.findElement(
				By.xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div[1]/div[3]/div/div/div[33]/div/div"));
		seta.click();

		WebElement filtro = driver.findElement(By.xpath("/html/body/div[11]/div/div[2]/div/div[6]/a/div[1]"));
		filtro.click();
		WebElement filtroEs = driver.findElement(By.xpath("/html/body/div[13]/div/div[2]"));
		filtroEs.click();
		WebElement filtroSpace = driver
				.findElement(By.xpath("/html/body/div[13]/div/div[2]/div/table/tbody/tr/td[2]/input"));
		filtroSpace.click();
		System.out.println("etiqueta no filtro: " + etiqueta);
		filtroSpace.sendKeys(etiquetaFiltro);
		Thread.sleep(1000);
		long time = 100;

		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div/div[2]/div/div/div[7]")));
		wait.until(ExpectedConditions.elementToBeClickable(By
				.xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/div/div[2]/div/div/div[7]")));
		Thread.sleep(1000);
		try {

			do {
				resultado = processo_grid.buscar_processo(resultado.getDriver(), wait);
				grid = resultado.isGrid();
				if (grid == false) {
					return true;
				} else {
					//Setar todas as configurações especificas nesses ifs
					if (config.isLaudoPericial() == true) {
						resultado = pericial.pericial(resultado.getDriver(), wait, bancos);
					} else if (config.isPeticaoInicial() == true) {
					//	if (resultado.getAssunto().contains("SALÁRIO-MATERNIDADE") || resultado.getAssunto().contains("RURAL")) {
							resultado = controllerMaternida.iniciar(resultado.getDriver(), resultado.getAssunto(), wait, config, bancos);
							List<String> janela = new ArrayList(resultado.getDriver().getWindowHandles());
							resultado.getDriver().switchTo().window(janela.get(1)).close();
							resultado.getDriver().switchTo().window(janela.get(0));


//						} else {
//							resultado.setEtiqueta("ASSUNTO NÃO APLICÁVEL");
//							resultado.setObservacao("");
//						}

						etiqueta.etiquetar(resultado.getDriver(), wait, resultado);


					}
				}
			} while (grid != false);
		} catch (Exception e) {
			final Exception ex = e;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert erro = new Alert(Alert.AlertType.INFORMATION);
					erro.setTitle("Alerta");
					erro.setHeaderText("Ocorreu um erro duranto o processo de triagem!");
					erro.setContentText("Este relatório será enviado para os desenvolvedores");
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					ex.printStackTrace(pw);
					String exceptionText = "Versão: " + Sistema.VERSAO + "\nUsuário: " + System.getProperty("user.name");
					exceptionText += "\n" + sw.toString();

					Label label = new Label("The exception stacktrace was:");

					TextArea textArea = new TextArea(exceptionText);
					textArea.setEditable(false);
					textArea.setWrapText(true);
					textArea.setMaxWidth(Double.MAX_VALUE);
					textArea.setMaxHeight(Double.MAX_VALUE);
					GridPane.setVgrow(textArea, Priority.ALWAYS);
					GridPane.setHgrow(textArea, Priority.ALWAYS);

					GridPane expContent = new GridPane();
					expContent.setMaxWidth(Double.MAX_VALUE);
					expContent.add(label, 0, 0);
					expContent.add(textArea, 0, 1);
					// Set expandable Exception into the dialog pane.
					erro.getDialogPane().setExpandableContent(expContent);
					
					final UtilEmail email = new UtilEmail(exceptionText);
					Runnable task = new Runnable() {
						@Override
						public void run() {
							email.enviarEmail();
						}

					};
					Thread mail = new Thread(task);
					mail.setDaemon(true);
					mail.start();
					
					erro.show();
				}
			});
			return true;
		}
		return true;
	}
}