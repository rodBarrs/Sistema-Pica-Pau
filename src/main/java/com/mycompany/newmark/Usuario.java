/**
 * @author Felipe Marques, Gabriel Ramos, Rafael Henrique e Adriano Vilhena 
 * 
 */
//Acescentar função de exibir e salvar senha
package com.mycompany.newmark;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class Usuario {
    private String Login = "";
    private String Senha = "";

    public String getLogin() {
        return Login;
    }

    public void setLogin(String Login) {
        this.Login = Login;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String Senha) {
        this.Senha = Senha;
    }
    
    public void logar(WebDriver driver, WebDriverWait wait, Usuario usuario) throws InterruptedException
    {
        //Recebe o elemento para colocar o login
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='cpffield-1017-inputEl']")));
        WebElement login = driver.findElement(By.xpath("//input[@id='cpffield-1017-inputEl']"));
        //Envia o elemento para enviar os dados do login
        login.click();
        login.sendKeys(usuario.getLogin());
        //Recebe o elemento para colocar a senha
        WebElement senha = driver.findElement(By.xpath("//td[@id='textfield-1018-bodyEl']/input"));
        senha.click();
        senha.sendKeys(usuario.getSenha());
        //Envia o elemento para enviar os dados da senha
        WebElement Click = driver.findElement(By.xpath("//span[@id='button-1019-btnInnerEl']"));
        Click.click();
    }
}