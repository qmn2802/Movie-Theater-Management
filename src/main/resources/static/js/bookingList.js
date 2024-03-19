$(document).ready(function () {
    loadData(pageNumber, pageSize, searchByInvoiceItemId);
    searchByInvoiceItemIdFunction();
    changePageSize();
});
let lsdRing = $('.lsd-ring-container');

let pageNumber = 0;
let pageSize = 1;
let searchByInvoiceItemId = '';
function attachCheckboxClickHandlers() {
    let statusLabel = $('.status-label');
    statusLabel.off('click');
    statusLabel.on('click',function (event){
        Swal.fire({
            title: "Are you sure want to update status for this ticket",
            icon: "warning",
            confirmButtonText: "Continue Update",
            showCancelButton: true,
            cancelButtonText: "Cancel"
        }).then((result) => {
            if (result.isConfirmed) {
                const invoiceItemId = $(this).data('invoice-item-id');
                let inputId = $(this).attr('for');
                let inputElement = $('#' + inputId);
                const isChecked = inputElement.is(':checked');
                console.log(isChecked)

                $.ajax({
                    url: '/admin/update-status',
                    type: 'POST',
                    // Bỏ contentType nếu bạn không cần thiết lập nó một cách rõ ràng
                    data: {
                        invoiceItemId: invoiceItemId,
                        status: !inputElement.is(':checked')
                    },
                    beforeSend: function () {
                        lsdRing.removeClass('d-none');
                    },
                    success: function(response) {
                        Swal.fire({
                            title: "Update Success",
                            icon: "success",
                            confirmButtonText: "Oke",
                        }).then((result2) => {
                            if (result2.isConfirmed) {
                                inputElement.prop('disabled', false)
                                inputElement.prop('checked', !isChecked);
                                inputElement.prop('disabled', true)
                            }
                        });
                    },
                    error: function(xhr, status, error) {
                        console.error('Failed to update status for item', invoiceItemId, ':', error);
                        // Có thể thêm mã xử lý lỗi ở đây
                    },
                    complete: function (xhr, status) {
                        lsdRing.addClass('d-none');


                    }
                });
            }
        });



    })
}
let lstBookingTicket = [];
function loadData(pageNumber, pageSize, searchByInvoiceItemId) {
    $.ajax({
        url: '/admin/get-booking-ticket',
        data: {
            pageNumber: pageNumber,
            pageSize: pageSize,
            searchByInvoiceItemId: searchByInvoiceItemId
        },
        type: 'GET',
        beforeSend: function () {
            lsdRing.removeClass('d-none');
        },
        success: function (response) {
            let table = $('#table-data');
            const data = response.data;
            if(data == null){
                Swal.fire({
                    title: "Load Data Fail",
                    icon: "error",
                    text: "Please try later.",
                    confirmButtonText: "Oke Bro :)",
                });
                return;
            }
            lstBookingTicket = data.lstBookingTicket;

            if (lstBookingTicket === null || lstBookingTicket.length === 0 || data.pageNumber === null || data.pageSize === null || data.totalPage === null) {    //No Data
                table.empty();
                table.append(nullRowTemplate());
                renderPagination(1, 1);
            } else {                          //Has Data

                table.empty();

                $.each(lstBookingTicket, function (index, value) {
                    table.append(rowTemplate(index + 1, value));
                });

                renderPagination(data.pageNumber, data.totalPage);
            }
        },
        error: function (xhr, status, error) {
            console.log('Error:', error);
            Swal.fire({
                title: "Load Data Fail",
                icon: "error",
                text: "Please try later.",
                confirmButtonText: "Oke Bro :)",
            });
        },
        complete: function (xhr, status) {
            lsdRing.addClass('d-none');
            updateCheckboxStatus(lstBookingTicket);
            attachCheckboxClickHandlers();
        }
    });
}

function nullRowTemplate() {
    return `
    <tr>
        <td colspan="10">
            <div class="main__table-text">
                <span >No Data</span>
            </div>
        </td>
    </tr>    
    `
}
function rowTemplate(index, value) {
    return `
    <tr id="bookingListContainer">
                            <!-- id -->
                            <td>
                                <div class="main__table-text">
                                    <span class="id1" >` + index + `</span>
                                </div>
                            </td>
                             <td>
                                <div class="main__table-text">
                                    <span class="id1" >` + value.invoiceItemId + `</span>
                                </div>
                            </td>
                            <!-- basic info -->
                            <td>
                                <div class="main__user">
                                    <div class="main__meta">
                                        <span>` + value.movieName + `</span>
                                    </div>
                                </div>
                            </td>
                            <!-- === -->
                            <td>
                                <div class="main__user">
                                    <div class="main__meta">
                                        <span>` + formatDateStringToDate(value.scheduleTime) + `</span>
                                    </div>
                                </div>
                            </td>
                             <td>
                                <div class="main__user">
                                    <div class="main__meta">
                                        <span>` + formatDateStringToTime(value.scheduleTime) + `</span>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="main__table-text">
                                    <span >` + value.cinemaRoomName + `</span>
                                </div>
                            </td>
                            <td>
                                <div class="main__table-text">
                                    <span >` + value.seat + `</span>
                                </div>
                            </td>
                            <!-- Action -->
                            <td style="background-color: #3b3b3b !important;" class="pe-5">
                                <div class="checkbox-wrapper-10">
                                    <input type="checkbox" id="id-${value.invoiceItemId}" class="tgl tgl-flip status-input "  disabled>
                                    <label for="id-${value.invoiceItemId}" data-tg-on="USED" data-tg-off="NOT USED" class="tgl-btn status-label" data-invoice-item-id="${value.invoiceItemId}"></label>
                                </div>

                            </td>
                        </tr>
    `
}

function updateCheckboxStatus(lstBookingTicket) {
    $.each(lstBookingTicket, function (index, value){
        const checkbox = $("#id-" + value.invoiceItemId);

        if (value.status) {
            checkbox.prop('disabled', false)
            checkbox.prop('checked', true)
            checkbox.prop('disabled', true)
        } else {
            checkbox.prop('disabled', false)
            checkbox.prop('checked', false)
            checkbox.prop('disabled', true)
        }
    });
}

function formatDateStringToDate(dateString){
    // Parse the string into a Date object
    var date = new Date(dateString);

    // Extract the parts of the date
    var day = date.getDate().toString().padStart(2, '0'); // Day (with leading zeros)
    var month = (date.getMonth() + 1).toString().padStart(2, '0'); // Month (with leading zeros, January is 0!)
    var year = date.getFullYear(); // Year
    var hour = date.getHours().toString().padStart(2, '0'); // Hour (with leading zeros)
    var minute = date.getMinutes().toString().padStart(2, '0'); // Minute (with leading zeros)

    // Format the date as "dd/mm/yyyy hh:mm"
    return day + '/' + month + '/' + year ;
}
function formatDateStringToTime(dateString){
    // Parse the string into a Date object
    var date = new Date(dateString);

    // Extract the parts of the date
    var day = date.getDate().toString().padStart(2, '0'); // Day (with leading zeros)
    var month = (date.getMonth() + 1).toString().padStart(2, '0'); // Month (with leading zeros, January is 0!)
    var year = date.getFullYear(); // Year
    var hour = date.getHours().toString().padStart(2, '0'); // Hour (with leading zeros)
    var minute = date.getMinutes().toString().padStart(2, '0'); // Minute (with leading zeros)

    // Format the date as "dd/mm/yyyy hh:mm"
    return  hour + ':' + minute;
}

function renderPagination(pageNumber, totalPage) {
    let paginationRoot = $('.pagination');
    paginationRoot.empty();
    let html = '<li class="page-item" data-page-number="' + (pageNumber === 0 ? pageNumber : pageNumber - 1) + '"><span class="page-link">Previous</span></li>';

    for (var i = 0; i < totalPage; i++) {
        if (i === pageNumber) {
            html += '<li class="page-item" data-page-number="' + i + '"><span class="page-link active">' + (i + 1) + '</span></li>';
        } else {
            html += '<li class="page-item" data-page-number="' + i + '"><span class="page-link">' + (i + 1) + '</span></li>';
        }
    }

    html += '<li class="page-item" data-page-number="' + (pageNumber === totalPage - 1 ? pageNumber : pageNumber + 1) + '"><span class="page-link">Next</span></li>';

    paginationRoot.html(html);
    changePagination();
}

function changePagination() {
    let paginationRoot = $('.pagination');
    paginationRoot.off('click', '.page-item');
    paginationRoot.on('click', '.page-item', function () {
        pageNumber = $(this).data('page-number');
        loadData(pageNumber, pageSize,searchByInvoiceItemId );
    })
}

//Change Page Size
function changePageSize(){
    let pageSizeButton = $('#pageSize');
    pageSizeButton.off('change');
    pageSizeButton.on('change',function (){
        pageSize = $(this).val();
        pageNumber = 0;
        loadData(pageNumber,pageSize,searchByInvoiceItemId);
    })
}

function searchByInvoiceItemIdFunction(){
    let searchMovieButton = $('#search-button');
    searchMovieButton.off('click');
    searchMovieButton.on('click',function (){
        searchByInvoiceItemId = $('#search-by-invoice-item-id').val();
        pageNumber = 0;
        loadData(pageNumber,pageSize,searchByInvoiceItemId);
    })


}