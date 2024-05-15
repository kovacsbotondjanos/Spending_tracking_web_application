package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers;

import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.Outgoing;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.OutgoingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataBaseWriteAndDeleteHandler {
    @Autowired
    private final OutgoingService outgoingService;

    public DataBaseWriteAndDeleteHandler(OutgoingService outgoingService) {
        this.outgoingService = outgoingService;
    }

    public void dataBaseWrite(Outgoing outgoing){
        outgoingService.insertExpenseRecord(outgoing);
    }

    public void dataBaseDelete(Outgoing outgoing){
        outgoingService.deleteExpenseRecord(outgoing);
    }
}
