package com.devsuperior.dsmeta.services;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsService {

	@Value("${twilio.sid}")
	private String twilioSid;

	@Value("${twilio.key}")
	private String twilioKey;

	@Value("${twilio.phone.from}")
	private String twilioPhoneFrom;

	@Value("${twilio.phone.to}")
	private String twilioPhoneTo;

	@Autowired
	private SaleRepository repository;

	public void sendSms(Long saleid) {

		Sale sale = repository.findById(saleid).get();
		
//		String date = sale.getDate().getMonthValue() + "/" + sale.getDate().getYear();
//		String msg = "Vendedor " + sale.getSellerName() + " foi destaque em " + date
//				+ " com um total de R$ " + String.format("%.2f", sale.getAmount());

		String date = sale.getDate().getMonthValue() + "/" + sale.getDate().getYear();
		String msg = "Vendedor " + sale.getSellerName() + " foi destaque em " + date
				+ " com um total de R$ " + new DecimalFormat("#,##0.00").format(sale.getAmount());
		
		String teste = msg.replace(".",",").replace(",", ".");
		
		System.out.println(teste);
		
		Twilio.init(twilioSid, twilioKey);

		PhoneNumber to = new PhoneNumber(twilioPhoneTo);
		PhoneNumber from = new PhoneNumber(twilioPhoneFrom);

		Message message = Message.creator(to, from, teste).create();

		System.out.println(message.getSid());
	}
}