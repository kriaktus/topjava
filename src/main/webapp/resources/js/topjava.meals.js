const mealsAjaxUrl = "ui/meals/";
const mealsBetweenAjaxUrl = mealsAjaxUrl + "filtered";
let filterForm;

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealsAjaxUrl
};

// $(document).ready(function () {
$(function () {
    filterForm = $('#filterForm');
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
    ctx.updateTable = (function getBetween() {
        $.get(mealsBetweenAjaxUrl, filterForm.serialize(), ctx.updateTableByData)
    });
});

function clearFilter() {
    filterForm[0].reset();
    $.get(mealsAjaxUrl, ctx.updateTableByData);
}