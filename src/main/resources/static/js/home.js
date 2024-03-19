$(document).ready(function () {
    loadData();
})
let lsdRing = $('.lsd-ring-container');
let movieDate = new Date();
function loadData() {
    $.ajax({
        url: '/get-movie-by-date',
        type: 'GET',
        data: {
            // date: formatDate(movieDate)
            date: '2024-03-18'
        },
        beforeSend: function () {
            lsdRing.removeClass('d-none');
        },
        success: function (response) {
            const container = $('#movie-items');
            const data = response.data;
            if (data == null) {
                Swal.fire({
                    title: "No Movie Playing Today",
                    icon: "error",
                    text: "See you tomorrow!",
                    confirmButtonText: "Oke Bro :(",
                });
                return;
            }

            //Has Data
            container.empty();
            $.each(data, function (index, value) {
                container.append(rowTemplate(index, value));
            });
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

function rowTemplate(index, rowData) {
    // Tạo một biến để giữ chuỗi HTML của các nút loại phim
    var movieTypesHtml = '';

    // Duyệt qua mảng typeMovies và thêm mỗi loại vào chuỗi HTML
    $.each(rowData.typeMoviesString, function (i, value) {
        movieTypesHtml += `<button type="button" class="btn btn-outline-success me-2 mb-2 fw-bold">${value}</button>`;
    });

    // Trả về chuỗi template của bạn với movieTypesHtml đã được nối vào
    return `
    <div class="col-xl-3 col-lg-4 col-6 p-3">
        <div class="card border-dark rounded bg-dark" style="width: 100%;">
            <img src="${rowData.smallImage}" class="rounded bg-dark movie-small-img" style="width: 100%;" alt="">
            <div class="card-body bg-dark text-light px-0">
                <h3 class="card-title  fw-bold movie-name">${rowData.movieName}</h3>
                <div class="pb-3 d-flex flex-wrap movie-attribute">
                    ${movieTypesHtml}                     
                </div>
                <button class="button-63 rounded fw-bold text-light w-75 buy-ticket" role="button" data-movie-id="${rowData.movieId}" data-bs-target="#select-movie" data-bs-toggle="modal">Buy Ticket</button>
            </div>
        </div>
    </div>
    `;
}

function formatDate(date){
    var day = date.getDate();
    var month = date.getMonth() + 1; // getMonth() returns 0-11
    var year = date.getFullYear();

// Pad the month and day with zeros if they are single digits
    month = month.toString().padStart(2, '0');
    day = day.toString().padStart(2, '0');

    return year + '-' + month + '-' + day;

}
