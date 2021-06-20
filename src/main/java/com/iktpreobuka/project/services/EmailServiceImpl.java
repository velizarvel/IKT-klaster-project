package com.iktpreobuka.project.services;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.iktpreobuka.project.entities.VoucherEntity;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender emailSender;

	@Override
	public void sendTemplateMessage(VoucherEntity voucher) throws Exception {
		MimeMessage mail = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true, "UTF-8");
		helper.setFrom("email@gmail.com");
		helper.setTo(voucher.getBuyer().getEmail());
		helper.setSubject(voucher.getOffer().getOfferName());
		String text = "<table style=\"border-collapse: collapse; width: 100%;\" border=\"2\">\r\n" + "<tbody>\r\n"
				+ "<tr>\r\n" + "<td style=\"width: 25%;\"><strong>Buyer</strong></td>\r\n"
				+ "<td style=\"width: 25%;\"><strong>Offer</strong></td>\r\n"
				+ "<td style=\"width: 25%;\"><strong>Price</strong></td>\r\n"
				+ "<td style=\"width: 25%;\"><strong>ExpiresDate</strong></td>\r\n" + "</tr>\r\n" + "<tr>\r\n"
				+ "<td style=\"width: 25%;\">" + voucher.getBuyer().getFirstName() + " "
				+ voucher.getBuyer().getLastName() + "</td>\r\n" + "<td style=\"width: 25%;\">"
				+ voucher.getOffer().getOfferName() + "</td>\r\n" + "<td style=\"width: 25%;\">"
				+ voucher.getOffer().getRegularPrice() + "</td>\r\n" + "<td style=\"width: 25%;\"> "
				+ voucher.getExpirationDate() + "</td>\r\n" + "</tr>\r\n" + "</tbody>\r\n" + "</table>";
		helper.setText(text, true);
		emailSender.send(mail);

	}

}
