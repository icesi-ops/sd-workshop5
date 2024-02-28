package com.aforo.kafka.consumer;

import com.aforo.dao.InvoiceDao;
import com.aforo.model.Invoice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionEvents {

    @Autowired
    private InvoiceDao _dao;

    @Autowired
    private ObjectMapper objectMapper;

    private Logger log = LoggerFactory.getLogger(TransactionEvents.class);

    public void processTransactionEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        Invoice event = objectMapper.readValue(consumerRecord.value(), Invoice.class);
        Invoice invoice = _dao.findById(event.getIdInvoice()).get();
        log.info("Actualizando Invoice ***" + event.getIdInvoice());
   		log.info("Se ha pagado la factura # " + event.getIdInvoice());
        invoice.setAmount(invoice.getAmount()-event.getAmount());
        if (invoice.getAmount()<=0){
            invoice.setState(1);
            if (invoice.getAmount()<0){
                invoice.setAmount(0.00);
            }
        }
        _dao.save(invoice);
    }
}
