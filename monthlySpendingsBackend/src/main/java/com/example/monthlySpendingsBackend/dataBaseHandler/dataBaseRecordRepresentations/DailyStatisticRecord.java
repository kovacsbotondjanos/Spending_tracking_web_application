package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations;

import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.Outgoing;

import java.util.List;

public record DailyStatisticRecord(
       int groceries,
       List<Outgoing> groceriesList,
       int commute,
       List<Outgoing> commuteList,
       int extra,
       List<Outgoing> extraList,
       int rent,
       List<Outgoing> rentList,
       int income,
       List<Outgoing> incomeList,
       int bankBalance) { }
