const mealsAjaxUrl = "ui/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealsAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

const mealsBetweenAjaxUrl = "ui/meals/filtered";
let filterForm;

$(document).ready(function () {
    filterForm = $('#filterForm');
});

function clearFilter() {
    filterForm.find(":input").val("");
    updateTable();
}

function getBetween() {
    $.get(mealsBetweenAjaxUrl, filterForm.serialize(), function(data){
        ctx.datatableApi.clear().rows.add(data).draw();
    })
}