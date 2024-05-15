package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers;

import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.Outgoing;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.OutgoingService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataBaseWriteAndDeleteHandler {
    @Autowired
    private final OutgoingService outgoingService;

    public DataBaseWriteAndDeleteHandler(OutgoingService outgoingService) {
        this.outgoingService = outgoingService;
    }

    public void DataBaseWrite(CustomUser user, Outgoing outgoing){
        outgoingService.insertExpenseRecord(outgoing);
    }

    public void DataBaseDelete(CustomUser user, Outgoing outgoing){
        outgoingService.deleteExpenseRecord(outgoing);
    }
}
