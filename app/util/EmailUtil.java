package util;

//import play.api.libs.mailer.MailerAPI;
//import play.api.libs.mailer.MailerPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import models.Usuario;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;

//import com.typesafe.plugin.MailerAPI;
//import com.typesafe.plugin.MailerPlugin;


/**
 * Responsavel por enviar os emails <br>
 * O email e enviado assincronamente, atraves de Promise.promise() <br>
 *
 */
public class EmailUtil {

	/**
	 * Enviar email
	 * 
	 * @param email a ser enviado
	 * **/
	public static void enviarEmailContato(String emailUsuario, String subject, String bodyHtml) {
		
		Promise.promise(new Function0<Boolean>() {
			
    		@Override
	    	public Boolean apply() throws Throwable {
				try {
					
					String emailUsuarioEnviado = "ABICS-LOG <"+emailUsuario+">";
					
					Email email = new Email();
					email.setSubject("[ABICS-LOG] " + subject + "  " + emailUsuario);
					email.setFrom(emailUsuarioEnviado);
					email.setBodyHtml(bodyHtml);
					MailerPlugin.send(email);
				
					return true;
				} catch(Exception e) {
					e.printStackTrace();
					return false;
				}
    		}
		});
	}
	
	/**
	 * Envia a senha para o email <br>
	 * 
	 * pagina: esqueceusenha.html
	 * **/
	public static void enviarEmailRecuperarSenha(Usuario usuarioRecuperar, String senha) {
		
    	Promise.promise(new Function0<Boolean>() {
		
    		@Override
	    	public Boolean apply() throws Throwable {
				
				try {
					
					Email email = new Email();
					email.setSubject("[ABICS-LOG] Recuperação de senha do usuário: " + usuarioRecuperar.getLogin());
					email.setFrom("ABICS-LOG");
					email.addTo(usuarioRecuperar.getEmail());
					BufferedReader reader = FileUtil.lerArquivo(AbicsLogConfig.getString(AbicsLogConfig.EMAIL_FOLDER) + "esqueceusenha.html");
				    String         line = null;
				    StringBuilder  emailBoasVindas = new StringBuilder();
				    String         ls = System.getProperty("line.separator");
		
				    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
     			    LocalDateTime dateTime = LocalDateTime.now();
					String dataHoje = dateTime.format(formatter);
				    
					while( ( line = reader.readLine() ) != null ) {
						
						line = line.replace("$param_usuario_nome", usuarioRecuperar.getNome());
						line = line.replace("$param_usuario_login", usuarioRecuperar.getLogin());
						line = line.replace("$param_usuario_senha", senha);
						line = line.replace("$param_data_envio", dataHoje);
						
				        emailBoasVindas.append( line );
				        emailBoasVindas.append( ls );
				    }
		
					email.setBodyHtml(emailBoasVindas.toString());
					MailerPlugin.send(email);
				
					return true;
				} catch(Exception e) {
					e.printStackTrace();
					return false;
				}
    	    }
    	});
	}
	
	/**
	 * Envia a senha para o email <br>
	 * 
	 * pagina: boasvindas.html
	 * @throws IOException 
	 * **/
	public static void enviarEmailBoasVindas(Usuario usuario, String senhaNaoCriptografada, Usuario usuarioConvite) {
		
    	Promise.promise(new Function0<Boolean>() {
		
    		@Override
	    	public Boolean apply() throws Throwable {
				
				try {
					
					Email email = new Email();
					email.setSubject("[ABICS-LOG] Bem vindo usuário: " + usuario.getEmail());
					email.setFrom("ABICS-LOG");
					email.addTo(usuario.getEmail());
					BufferedReader reader = FileUtil.lerArquivo(AbicsLogConfig.getString(AbicsLogConfig.EMAIL_FOLDER) + "boasvindas.html");
				    String         line = null;
				    StringBuilder  emailBoasVindas = new StringBuilder();
				    String         ls = System.getProperty("line.separator");
					
				    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
     			    LocalDateTime dateTime = LocalDateTime.now();
					String dataHoje = dateTime.format(formatter);
					String permissaoDescricao = Usuario.Permissao.getDescricao(usuario.getPermissao());
					
					while( ( line = reader.readLine() ) != null ) {
						
						line = line.replace("$param_usuario_login", usuario.getLogin());
						line = line.replace("$param_usuario_senha", senhaNaoCriptografada);
						line = line.replace("$param_usuario_email", usuario.getEmail());
						line = line.replace("$param_data_cadastro", dataHoje);
						line = line.replace("$param_convite_nome", usuarioConvite.getNome());
						line = line.replace("$param_usuario_permissao", permissaoDescricao);
						
				        emailBoasVindas.append( line );
				        emailBoasVindas.append( ls );
				    }
		
					email.setBodyHtml(emailBoasVindas.toString());
					MailerPlugin.send(email);
				
					return true;
				} catch(Exception e) {
					e.printStackTrace();
					return false;
				}
    	    }
    	});
	}
	
}