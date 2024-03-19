$(document).ready(function () {
    $('#nav-content').find('.view-score').addClass('active');
    loadData(pageNumber, pageSize, fromDate, toDate, viewScoreAdding, viewScoreUsing);
    changePageSize();
    changeSearByDate();
})

let lsdRing = $('.lsd-ring-container');
let pageSize = 1;
let pageNumber = 0;
let fromDate = '1000-01-01';
let toDate = '3000-12-31';
let viewScoreAdding = true;
let viewScoreUsing = true;

function loadData(pageNumber, pageSize, fromDate, toDate, viewScoreAdding, viewScoreUsing) {
    $.ajax({
        url: '/get-history-of-score',
        type: 'GET',
        data: {
            pageNumber: pageNumber,
            pageSize: pageSize,
            fromDate: fromDate,
            toDate: toDate,
            viewScoreAdding: viewScoreAdding,
            viewScoreUsing: viewScoreUsing,
        },
        beforeSend: function () {
            lsdRing.removeClass('d-none');
        },
        success: function (response) {
            const table = $('#table-data');
            const data = response.data;
            if (data == null) {
                Swal.fire({
                    title: "No Data",
                    icon: "error",
                    text: "Please try later.",
                    confirmButtonText: "Oke Bro :)",
                });
                return;
            }
            const lstScoreHisDTO = data.lstScoreHisDTO;
            if (lstScoreHisDTO === null || lstScoreHisDTO.length === 0 || data.pageNumber === null || data.pageSize === null || data.totalPage === null) {    //No Data
                table.empty();
                table.append(nullRowTemplate());
                renderPagination(1, 1);
            } else {                          //Has Data

                table.empty();
                $.each(lstScoreHisDTO, function (index, value) {
                    table.append(rowTemplate(index + 1, value));
                });
                viewInvoiceDetail();
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
        }
    });
}

// Render Data
function nullRowTemplate() {
    return `
    <tr>
        <td colspan="6">
            <div class="main__table-text">
                <span >No Data</span>
            </div>
        </td>
    </tr>    
    `
}

function rowTemplate(index, rowData) {
    return `
    <tr>
        <td>
            <div class="main__table-text">
                <span class="index">` + index + `</span>
            </div>
        </td>
        <td>
            <div class="main__table-text">
                <span class="movie-name main__table-text--green status">` + formatDateString(rowData.bookingDate) + `</span>
            </div>
        </td>
        <td>
            <div class="main__table-text">
                <span class="addedScore">` + rowData.addScore + `</span>
            </div>
        </td>
        <td>
            <div class="main__table-text">
                <span class="usedScore">` + rowData.usedScore + `</span>
            </div>
        </td>
        <td>
            <div class="main__table-text">
                <span class="usedScore">` + Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(rowData.totalMoney) + `</span>
            </div>
        </td>
        <td>
            <div class="main__table-text">
                <span class="usedScore">` + rowData.discount + ` %</span>
            </div>
        </td>
        <td>
            <div class="main__table-text">
                <span class="usedScore">` + rowData.status + `</span>
            </div>
        </td>
        <td>
            <div class="main__table-text" style="padding: 11px" >
               <button class="btn btn-dark view-invoice-detail" type="button" data-invoice-id="` + rowData.invoiceId + `" data-bs-toggle="modal" data-bs-target="#detail-invoice">
                    <i class="bi bi-eye me-0" style="font-size: inherit"></i>
               </button>
            </div>
        </td>
    </tr>`
}

//View Invoice Detail
function viewInvoiceDetail() {
    $('.view-invoice-detail').off('click');
    $('.view-invoice-detail').on('click', function () {
        let invoiceId = $(this).data('invoice-id');
        $.ajax({
            url: '/get-invoice-detail',
            type: 'GET',
            data: {
                invoiceId: invoiceId
            },
            beforeSend: function () {
                lsdRing.removeClass('d-none');
            },
            success: function (response) {
                let table = $('#invoice-detail-data');
                let foodTable = $('#table-food-data');

                if (response.data === null) {
                    Swal.fire({
                        title: "Load Data Fail",
                        icon: "error",
                        text: "Please try later.",
                        confirmButtonText: "Oke Bro :)",
                    });
                } else {
                    table.empty();
                    foodTable.empty();

                    let tableHasData = false;
                    let tableFoodHasData = false;
                    $.each(response.data, function (index, value) {
                        if(value.movieName !== null && value.price !== null && value.scheduleTime && value.seat != null && value.seatType !== null ){
                            tableHasData = true;
                            table.append(`
                            <tr>
                                <td>
                                    <div class="main__table-text"  style="border-radius: 0">
                                        <span>` + index + `</span>
                                    </div>
                                </td>
                                <td>
                                    <div class="main__table-text">
                                        <span class="movie-name main__table-text--green">` + value.movieName + `</span>
                                    </div>
                                </td>
                                <td>
                                    <div class="main__table-text">
                                        <span>` + Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(value.price) + `</span>
                                    </div>
                                </td>
                                <td>
                                    <div class="main__table-text">
                                        <span>` + formatDateTimeString(value.scheduleTime) + `</span>
                                    </div>
                                </td>                                
                                <td>
                                    <div class="main__table-text">
                                        <span class="main__table-text--green ">` + value.seat + `</span>
                                    </div>
                                </td>
                                <td>
                                    <div class="main__table-text"  style="border-radius: 0">
                                        <span class="main__table-text--green status">` + value.seatType + `</span>
                                    </div>
                                </td>
                            </tr>
                        `)
                        }

                        if(value.foodName !== null && value.foodPrice !== null && value.foodSize !== null){
                            tableFoodHasData = true;
                            foodTable.append(`
                            <tr>
                                <td>
                                    <div className="main__table-text" style="border-radius: 0">
                                        <span>` + index + `</span>
                                    </div>
                                </td>
                                <td>
                                    <div className="main__table-text">
                                        <span
                                            className="movie-name main__table-text--green">` + value.foodName + `</span>
                                    </div>
                                </td>
                                <td>
                                    <div className="main__table-text">
                                        <span>` + Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(value.foodPrice) + `</span>
                                    </div>
                                </td>
                                <td>
                                    <div className="main__table-text">
                                        <span>` + value.foodSize + `</span>
                                    </div>
                                </td>
                            </tr>
                        `)
                        }

                    })
                    if(!tableHasData){
                        table.append(`
                            <tr>
                                <td colSpan="6">
                                    <div class="main__table-text">
                                        <span>Not buy</span>
                                    </div>
                                </td>
                            </tr>
                        `)
                    }
                    if(!tableFoodHasData){
                        foodTable.append(`
                            <tr id="table-food-data-defalut">
                                <td colSpan="4">
                                    <div class="main__table-text">
                                        <span>Not Buy</span>
                                    </div>
                                </td>
                            </tr>
                        `)
                    }

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
            }
        });
    })
}

function formatDateString(dateString) {
    // Parse the string into a Date object
    var date = new Date(dateString);

    // Extract the parts of the date
    var day = date.getDate().toString().padStart(2, '0'); // Day (with leading zeros)
    var month = (date.getMonth() + 1).toString().padStart(2, '0'); // Month (with leading zeros, January is 0!)
    var year = date.getFullYear(); // Year

    // Format the date as "dd/mm/yyyy hh:mm"
    return day + '/' + month + '/' + year;
}

function formatDateTimeString(dateString) {
    // Parse the string into a Date object
    var date = new Date(dateString);

    // Extract the parts of the date
    var day = date.getDate().toString().padStart(2, '0'); // Day (with leading zeros)
    var month = (date.getMonth() + 1).toString().padStart(2, '0'); // Month (with leading zeros, January is 0!)
    var year = date.getFullYear(); // Year
    var hour = date.getHours().toString().padStart(2, '0'); // Hour (with leading zeros)
    var minute = date.getMinutes().toString().padStart(2, '0'); // Minute (with leading zeros)

    // Format the date as "dd/mm/yyyy hh:mm"
    return day + '/' + month + '/' + year + ' - ' + hour + ':' + minute;
}

//Render Số trang
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
        loadData(pageNumber, pageSize, fromDate, toDate, viewScoreAdding, viewScoreUsing);
    })
}

//Change Page Size
function changePageSize() {
    let pageSizeButton = $('#pageSize');
    pageSizeButton.off('change');
    pageSizeButton.on('change', function () {
        pageSize = $(this).val();
        pageNumber = 0;
        loadData(pageNumber, pageSize, fromDate, toDate, viewScoreAdding, viewScoreUsing);
    })
}

//Change Search
function changeSearByDate() {


    $('#from-date, #to-date').on('blur', function () {
        // Lấy giá trị của từng trường
        let fromDateVal = $('#from-date').val();
        let toDateVal = $('#to-date').val();

        // Chuyển đổi giá trị thành đối tượng Date
        let fromDate = new Date(fromDateVal);
        let toDate = new Date(toDateVal);

        // Kiểm tra xem cả hai trường có giá trị hợp lệ không
        if (!isNaN(fromDate.getTime()) && !isNaN(toDate.getTime()) && checkDate(fromDate, toDate)) {
            $('#from-date, #to-date').addClass('border-danger');
            Swal.fire({
                title: "From date must be greater than To date.",
                icon: "error",
                confirmButtonText: "Ok",
            });
            return false;
        } else {
            $('#from-date, #to-date').removeClass('border-danger');
        }
    });


    let searchViewScoreButton = $('#search-view-score')
    searchViewScoreButton.off('click');
    searchViewScoreButton.on('click', function () {
        if ($('#from-date').val() !== '') {
            fromDate = $('#from-date').val();
        }
        if ($('#to-date').val() !== '') {
            toDate = $('#to-date').val();
        }

        viewScoreAdding = $('#view-score-adding').prop('checked');
        viewScoreUsing = $('#view-score-using').prop('checked');

        if (checkDate(fromDate, toDate)) {
            Swal.fire({
                title: "From date must be greater than To date.",
                icon: "error",
                confirmButtonText: "Ok",
            });
            return;
        }
        pageNumber = 0;
        loadData(pageNumber, pageSize, fromDate, toDate, viewScoreAdding, viewScoreUsing)
    })
}

function checkDate(fromDate, toDate) {
    return fromDate > toDate;
}

