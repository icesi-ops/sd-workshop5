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

import java.util.Objects;

@Service
public class TransactionEvents {

    @Autowired
    private InvoiceDao _dao;

    @Autowired
    private ObjectMapper objectMapper;

    private Logger log = LoggerFactory.getLogger(TransactionEvents.class);

    public void processTransactionEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        Invoice event = objectMapper.readValue(consumerRecord.value(), Invoice.class);
        log.info("Actulizando Invoice ***" + event.getIdInvoice());
        event.setState(1);
   		log.info("Se ha pagado la factura # " + event.getIdInvoice());

        _dao.findByIdInvoice(event.getIdInvoice())
                .ifPresentOrElse(
                        i -> processExistingInvoice(i, event),
                        () -> {
                            log.info("Invoice not found : " + event.getIdInvoice());
                            _dao.save(event);
                        }
                );
    }

    private void processExistingInvoice(Invoice existingInvoice, Invoice event) {
        log.info("Invoice found: " + existingInvoice.getIdInvoice());

        if (Objects.equals(existingInvoice.getAmount(), event.getAmount())) {
            log.info("Invoice already paid");
            existingInvoice.setState(1);
            existingInvoice.setAmount(0.0);
        } else {
            log.info("Invoice not paid");
            existingInvoice.setAmount(existingInvoice.getAmount() - event.getAmount());
        }

        _dao.save(existingInvoice);
    }
}
