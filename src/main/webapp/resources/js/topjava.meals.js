const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    $('.datetimepicker').datetimepicker({
        format: 'Y-m-d H:i'
    });

    $('#startDate').datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        onShow: function(){
            let endDateVal = jQuery('#endDate').val();
            this.setOptions({
                maxDate: endDateVal ? endDateVal : false
            })
        }
    });

    $('#endDate').datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        onShow: function(){
            let startDateVal = jQuery('#startDate').val();
            this.setOptions({
                minDate: startDateVal ? startDateVal : false
            })
        }
    });

    $('#startTime').datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function (){
            let endTimeVal = jQuery('#endTime').val();
            this.setOptions({
                maxTime: endTimeVal ? endTimeVal : false
            })
        }
    });

    $('#endTime').datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function (){
            let startTimeVal = jQuery('#startTime').val();
            this.setOptions({
                minTime: startTimeVal ? startTimeVal : false
            })
        }
    });


    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        return date.replace('T', ' ');
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-meal-excess", data.excess);
            }
        })
    );
});