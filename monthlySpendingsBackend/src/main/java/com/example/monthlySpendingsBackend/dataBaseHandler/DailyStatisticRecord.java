package com.example.monthlySpendingsBackend.dataBaseHandler;

import java.util.List;

public record DailyStatisticRecord(
       int groceries,
       List<Integer> groceriesList,
       int commute,
       List<Integer> commuteList,
       int extra,
       List<Integer> extraList,
       int rent,
       List<Integer> rentList,
       int income,
       List<Integer> incomeList,
       int bankBalance) { }
