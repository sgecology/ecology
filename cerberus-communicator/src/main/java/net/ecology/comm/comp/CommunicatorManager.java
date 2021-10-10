package net.ecology.comm.comp;

import java.nio.charset.StandardCharsets;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import net.ecology.comm.domain.MailMessage;
import net.ecology.comm.global.CommunicatorConstants;
import net.ecology.common.CommonUtility;
import net.ecology.domain.Context;
import net.ecology.exceptions.CerberusException;
import net.ecology.framework.component.BasisComp;

@Component
public class CommunicatorManager extends BasisComp {
	private static final long serialVersionUID = -7426015807103285508L;

	@Inject
	private JavaMailSender mailSender;

	@Inject
	private Configuration freemarkerConfig;

	@Inject 
	private EmailTemplateHelper emailTemplateHelper;

	private String emailTemplateLoadingDir;

	public void init() {
		//Maybe fetch all configuration entries from database or something like that
	}

	public void sendEmail(MailMessage mail) throws Exception {
		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message);

		// Using a sub-folder such as /templates here
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), getEmailTemplateLoadingDir());

		Template t = freemarkerConfig.getTemplate("/auth/forgotPassword.ftl");
		String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getDefinitions());

		helper.setTo(mail.getRecipients());
		helper.setText(text, true);
		helper.setSubject(mail.getSubject());

		mailSender.send(message);
	}

	public String getEmailTemplateLoadingDir() {
		return emailTemplateLoadingDir;
	}

	public void setEmailTemplateLoadingDir(String emailTemplateLoadingDir) {
		this.emailTemplateLoadingDir = emailTemplateLoadingDir;
	}

	public void sendEmail(Context context) throws CerberusException {
		MailMessage corpMimeMessage = null;
		MimeMessage mimeMessage = null;
    MimeMessageHelper helper = null;
    String templateId = null, messageText = null;
		try {
			corpMimeMessage = (MailMessage)context.get(CommunicatorConstants.CTX_MIME_MESSAGE);
			templateId = (String)context.get(CommunicatorConstants.CTX_MAIL_TEMPLATE_ID);

			mimeMessage = mailSender.createMimeMessage();
      helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

      if (CommonUtility.isNotEmpty(context.get(CommunicatorConstants.CTX_MAIL_TEMPLATE_DIR))) {
      	this.emailTemplateHelper.setEmailTemplateLoadingDir((String)context.get(CommunicatorConstants.CTX_MAIL_TEMPLATE_DIR));
      }
      messageText = this.emailTemplateHelper.getEmailMessageText(templateId, corpMimeMessage.getLocale(), corpMimeMessage.getDefinitions());

			helper.setSubject(corpMimeMessage.getSubject());
			helper.setTo(corpMimeMessage.getRecipients());
			helper.setText(messageText, true);

			mailSender.send(mimeMessage);
		} catch (Exception e) {
			throw new CerberusException(e);
		}
	}

	public boolean sendEmail(MailMessage mailMessage, String templateId, String mailTemplateLoadingDir) throws CerberusException {
		boolean response = false;
		MimeMessage mimeMessage = null;
    MimeMessageHelper helper = null;
    String messageText = null;
		try {
			mimeMessage = this.mailSender.createMimeMessage();
      helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

      if (CommonUtility.isNotEmpty(mailTemplateLoadingDir)) {
      	this.emailTemplateHelper.setEmailTemplateLoadingDir(mailTemplateLoadingDir);
      }
      messageText = this.emailTemplateHelper.getEmailMessageText(templateId, mailMessage.getLocale(), mailMessage.getDefinitions());

			helper.setSubject(mailMessage.getSubject());
			helper.setTo(mailMessage.getRecipients());
			helper.setText(messageText, true);

			this.mailSender.send(mimeMessage);
			response = true;
		} catch (Exception e) {
			throw new CerberusException(e);
		}
		return response;
	}

	public void sendEmail(MailMessage mail, String templateId) throws CerberusException {
		try {
			MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message,
          MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
          StandardCharsets.UTF_8.name());

			// Using a sub-folder such as /templates here
			String emailTemplateDir = this.getEmailTemplateLoadingDir();
			if (CommonUtility.isEmpty(emailTemplateDir)) {
				emailTemplateDir = CommunicatorConstants.DEFAULT_LOADING_TEMPLATE_DIRECTORY;
				logger.info("The loading email template directory is empty, using the default email loading template: " + CommunicatorConstants.DEFAULT_LOADING_TEMPLATE_DIRECTORY);
			}

			freemarkerConfig.setClassForTemplateLoading(this.getClass(), emailTemplateDir);
			Template template = freemarkerConfig.getTemplate(templateId, mail.getLocale());

			helper.setSubject(mail.getSubject());
			helper.setTo(mail.getRecipients());
			String messageText = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getDefinitions());
			helper.setText(messageText, true);

			/*StringWriter textWriter = new StringWriter();
			template.process(mail.getModel(), textWriter);
			String text = textWriter.toString();*/

			/*helper.addAttachment("pathToPortalImage", (File)mail.getModel().get("imageSpec"));
			
			if (CommonUtility.isNotEmpty(mail.getAttachments())) {
				for (Object attachment :mail.getAttachments()) {
					if (!(attachment instanceof EmailAttachment))
						continue;

					helper.addAttachment(((EmailAttachment)attachment).getName(), ((EmailAttachment)attachment).getInputStreamSource(), ((EmailAttachment)attachment).getContentType());
				}
			}*/
			mailSender.send(message);
		} catch (Exception e) {
			throw new CerberusException(e);
		}
	}
}
