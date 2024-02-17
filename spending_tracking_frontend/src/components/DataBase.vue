<script>
export default {
    data() {
        return {
            tableData: [],
            formData: {
                type: "groceries",
                amount: 0,
                date: "",
            },
            today: new Date(),
            formDataForFill: {
                month: "",
            },
            localhost: "192.168.56.1",
        };
    },
    methods: {
        monthFormatCorrection(month) {
            if (month < 10) {
                return "0" + month;
            }
            return String(month)
        },
        getToday() {
            const year = this.today.getFullYear();
            const month = this.monthFormatCorrection(this.today.getMonth() + 1);
            const day = this.today.getDate();
            if (day < 10) {
                return year + "-" + month + "-0" + day;
            }
            return year + "-" + month + "-" + day;
        },
        sendPostRequestToInsert() {
            if (this.formData !== undefined && this.formData.amount > 0 && this.formData.date != "") {
                const dateSplit = this.formData.date.split('-');
                if (dateSplit.length != 3) {
                    return;
                }
                const data = {
                    year: dateSplit[0],
                    month: dateSplit[1],
                    day: dateSplit[2],
                    dataBaseName: this.formData.type,
                    amount: this.formData.amount,
                };
                fetch("http://" + this.localhost + ":8080/enterIntoDataBase/v1", {
                    method: 'POST',
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(data),
                })
                    .then(() => this.fillDays(this.today.getFullYear(), this.today.getMonth() + 1))
                    .catch((error) => {
                        console.error("Error:", error);
                    });
            }
        },
        sendPostRequestToDelete() {
            if (this.formData !== undefined && this.formData.amount > 0 && this.formData.date != "") {
                const dateSplit = this.formData.date.split('-');
                if (dateSplit.length != 3) {
                    return;
                }
                const data = {
                    year: dateSplit[0],
                    month: dateSplit[1],
                    day: dateSplit[2],
                    dataBaseName: this.formData.type,
                    amount: this.formData.amount,
                };
                fetch("http://" + this.localhost + ":8080/deleteFromDataBase/v1", {
                    method: 'DELETE',
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(data),
                })
                    .then(() => this.fillDays(this.today.getFullYear(), this.today.getMonth() + 1))
                    .catch((error) => {
                        console.error("Error:", error);
                    });
            }
        },
        fillDays() {
            let year = "";
            let month = "";
            if (this.formDataForFill.month === "") {
                year = this.today.getFullYear();
                month = this.today.getMonth() + 1;
            }
            else {
                let datas = this.formDataForFill.month.split("-");
                year = datas[0];
                month = datas[1];
            }
            fetch("http://" + this.localhost + ":8080/monthlyStatistics/v1/" + year + "/" + month, {
                method: 'GET',
                headers: {
                    "Content-Type": "application/json",
                },
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("HTTP error! Status: ${response.status}");
                    }
                    else {
                        return response.json();
                    }
                })
                .then(data => {
                    this.tableData = [];
                    for (const dateKey in data) {
                        this.tableData.push({
                            id: dateKey,
                            values: data[dateKey],
                        });
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                });
        },
    },
    created() {
        this.formData.date = this.getToday();
        console.log(this.formData.date)
        let datas = this.getToday().split("-");
        this.formDataForFill.month = datas[0] + "-" + datas[1];
        this.fillDays();
    },
}
</script>


<template>
    <div class="container container-fluid">
        <div class="row">
            <div class="col-2">
                <div class="row">
                    <div class="col">
                        <form onsubmit="return false;">
                            <div class="row">
                                <div class="col">
                                    <label for="type">Type of the expense:</label><br>
                                    <select v-model="this.formData.type" id="type">
                                        <option value="groceries" default>groceries</option>
                                        <option value="commute">commute</option>
                                        <option value="extra">extra</option>
                                        <option value="rent">rent</option>
                                        <option value="income">income</option>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <label for="amount">Amount:</label><br>
                                    <input v-model="this.formData.amount" type="number" id="amount" min=0>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <label for="date">Date of the expense:</label><br>
                                    <input v-model="this.formData.date" type="date" id="date">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <button v-on:click="sendPostRequestToInsert">Submit</button>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <button v-on:click="sendPostRequestToDelete">Delete</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="col-10">
                <div class="row">
                    <div class="col">
                        <h1>{{ this.today.getFullYear() }}-{{ monthFormatCorrection(this.today.getMonth() + 1) }}</h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <form @submit.prevent="fillDays">
                            <label for="month">Month to list out</label><br>
                            <input type="month" v-model="this.formDataForFill.month" id="month">
                            <button class="fillButton" type="submit">Fill in</button>
                        </form>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <div class="table-container">
                            <table class="days">
                                <tr>
                                    <th>Day</th>
                                    <th>Groceries</th>
                                    <th>Extra</th>
                                    <th>Rent</th>
                                    <th>Commute</th>
                                    <th>Income</th>
                                    <th>Bank Balance</th>
                                </tr>
                                <tr v-for="row in tableData.slice(0, 15)">
                                    <td>{{ row.id }}.</td>
                                    <td>{{ row.values.groceries.toLocaleString("hun") }} Ft</td>
                                    <td>{{ row.values.extra.toLocaleString("hun") }} Ft</td>
                                    <td>{{ row.values.rent.toLocaleString("hun") }} Ft</td>
                                    <td>{{ row.values.commute.toLocaleString("hun") }} Ft</td>
                                    <td>{{ row.values.income.toLocaleString("hun") }} Ft</td>
                                    <td>{{ row.values.bankBalance.toLocaleString("hun") }} Ft</td>
                                </tr>
                            </table>
                            <table class="days">
                                <tr>
                                    <th>Day</th>
                                    <th>Groceries</th>
                                    <th>Extra</th>
                                    <th>Rent</th>
                                    <th>Commute</th>
                                    <th>Income</th>
                                    <th>Bank Balance</th>
                                </tr>
                                <tr v-for="row in tableData.slice(15)">
                                    <td>{{ row.id }}.</td>
                                    <td>{{ row.values.groceries.toLocaleString("hun") }} Ft</td>
                                    <td>{{ row.values.extra.toLocaleString("hun") }} Ft</td>
                                    <td>{{ row.values.rent.toLocaleString("hun") }} Ft</td>
                                    <td>{{ row.values.commute.toLocaleString("hun") }} Ft</td>
                                    <td>{{ row.values.income.toLocaleString("hun") }} Ft</td>
                                    <td>{{ row.values.bankBalance.toLocaleString("hun") }} Ft</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>