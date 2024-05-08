package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations;

import java.time.LocalDate;

public record InteractionRecord(
        LocalDate date,
        String dataBaseName,
        int amount) { }
