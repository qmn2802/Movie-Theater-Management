$(document).ready(function () {
    loadAllPromotion();
    openModalAddNewPromotion();
    AddNewPromotion();
    SaveEditPromotion();

})
let lsdRing = $('.lsd-ring-container');

function loadAllPromotion() {
    $.ajax({
        url: '/get-promotion',
        type: 'GET',
        beforeSend: function () {
            lsdRing.removeClass('d-none');
        },
        success: function (response) {
            var listPromotion = response.data;
            $.each(listPromotion, function (index, value) {
                var html = `
            <div class="row">
                <div class="border border-dark-subtle py-4 px-3 m-4" style="border-radius: 16px">
                    <div class="row m-3 bg-body-secondary d-flex justify-content-around">
                        <div class="col-2 mt-3 mb-3">
                            <div><img src="/img/logoNe.png" alt="bác sĩ lạ" class="img-fluid mt-5 me-2"
                                      style="max-width: 200%;"></div>
                        </div>
                        <div class="col-8 mt-3 mb-3">
                            <div class="text-decoration-none mb-5">
                                <h1 class="text-secondary-emphasis">` + value.title + `</h1>
                            </div>
                            <div class="row border-top m-2">
                                <div class="col-5"><b>Start time: </b></div>
                                <div class="col-7">` + new Date(value.startTime).toLocaleString() + `</div>
                            </div>
                            <div class="row border-top m-2">
                                <div class="col-5"><b>End time: </b></div>
                                <div class="col-7">` + new Date(value.endTime).toLocaleString() + `</div>
                            </div>
                            <div class="row border-top m-2">
                                <div class="col-5"><b>Discount level: </b></div>
                                <div class="col-7">` + value.discountLevel + `</div>
                            </div>
                            <div class="row border-top m-2">
                                <div class="col-5"><b>Details: </b></div>
                                <div class="col-7">` + value.detail + `</div>
                            </div>
                            <div class="row border-top m-2">
                                <div class="col-5"><b>Code: </b></div>
                                <div class="col-7 text-danger">` + value.code + `</div>
                                <div class="col-7 d-none">` + value.promotionId + `</div>
                            </div>
                            <div class="col-5 d-flex justify-content-end mt-5">
                                   <button id="deletePromotionBtn" class="btn btn-secondary me-3 delete-promo" data-promo-id="` + value.promotionId + `">Delete Promotion</button>

                                <button type="button" class="btn btn-danger edit" data-promo-id="` + value.promotionId + `" data-bs-toggle="modal"
                            data-bs-target="#modalAddPromotion"><i class="bi bi-ticket-detailed"></i> Edit
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            `
                $('.main-content').append(html);
            });
            editPromotion();
        }, error: function (xhr, status, error) {
            console.log('Error:', error);
        }, complete: function (xhr, status) {
            lsdRing.addClass('d-none');
            deletePromo();
        }
    });
}

function editPromotion() {
    $('.edit').on('click', function () {
        var formData = {
            id : $(this).data('promo-id'),
        }
        $.ajax({
            url: '/edit-promotion',
            type: 'POST',
            contentType: 'application/json',
            dataType: "json",
            data: JSON.stringify(formData),
            beforeSend: function () {
                lsdRing.removeClass('d-none');
            },
            success: function (response) {
                let promotion = response.data;
                $('#staticBackdropLabel').text('Edit Promotion');
                $('#inputDetails').val(promotion.detail);
                $('#inputDiscountLevel').val(promotion.discountLevel);
                $('#inputEndTime').val(new Date(promotion.endTime).toISOString().slice(0, 16));
                $('#inputStartTime').val(new Date(promotion.startTime).toISOString().slice(0, 16));
                $('#inputTitle').val(promotion.title);
                $('#inputId').val(promotion.promotionId);
                $('#add-new-promotion').attr('id', 'save-Edit');
            }, error: function (xhr, status, error) {
                console.log('Error:', error);
            }, complete: function (xhr, status) {
                lsdRing.addClass('d-none');
            }
        });
    })
}

function openModalAddNewPromotion() {
    $('.add-new-promotion').on('click', function () {
        $('#staticBackdropLabel').text('Add A New Promotion');
        $('#inputDetails').val('');
        $('#inputDiscountLevel').val('');
        $('#inputEndTime').val('');
        $('#inputStartTime').val('');
        $('#inputTitle').val('');
    })
}
function generateRandomCode() {
    let chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    let nums = '0123456789';
    let code = '';

    for (let i = 0; i < 5; i++) {
        code += chars.charAt(Math.floor(Math.random() * chars.length));
    }

    for (let i = 0; i < 5; i++) {
        code += nums.charAt(Math.floor(Math.random() * nums.length));
    }

    code = code.split('').sort(() => 0.5 - Math.random()).join('');

    return code;
}

function AddNewPromotion() {
    $('#add-new-promotion').on('click', function () {
        var promotion = {
            detail : $('#inputDetails').val(),
            discountLevel : $('#inputDiscountLevel').val(),
            endTime : $('#inputEndTime').val(),
            startTime : $('#inputStartTime').val(),
            title : $('#inputTitle').val(),
            image : $('#inputImage').val(),
            code : generateRandomCode(),
        }

        $.ajax({
            url: '/save-new-promotion',
            type: 'POST',
            contentType: 'application/json',
            dataType: "json",
            data: JSON.stringify(promotion),
            beforeSend: function () {
                lsdRing.removeClass('d-none');
            },
            success: function (response) {
                console.log(response.message)
            }, error: function (xhr, status, error) {
                console.log('Error:', error);
            }, complete: function (xhr, status) {
                lsdRing.addClass('d-none');
            }
        });
    })
}

function SaveEditPromotion() {
    $('#edit-promotion').on('click', function () {
        var promotion = {
            id : $('#inputId').val(),
            detail : $('#inputDetails').val(),
            discountLevel : $('#inputDiscountLevel').val(),
            endTime : $('#inputEndTime').val(),
            startTime : $('#inputStartTime').val(),
            title : $('#inputTitle').val(),
            image : $('#inputImage').val(),
            code : generateRandomCode(),
        }

        $.ajax({
            url: '/save-edit-promotion',
            type: 'POST',
            contentType: 'application/json',
            dataType: "json",
            data: JSON.stringify(promotion),
            beforeSend: function () {
                lsdRing.removeClass('d-none');
            },
            success: function (response) {
                console.log("Promo ID:", $(this).data('promo-id'));

            }, error: function (xhr, status, error) {
                console.log("Promo ID:", promotion);
            }, complete: function (xhr, status) {
                lsdRing.addClass('d-none');
            }
        });
    })
}

function deletePromo(){
    $('.delete-promo').off('click');
    $('.delete-promo').on('click',function () {
        var promotionId = $(this).data('promo-id');
        Swal.fire({
            title: "Notification",
            icon: "warning",
            text: 'Do you want to delete Promo has id: ' + promotionId,
            showCancelButton:true
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: '/delete-promotion',
                    type: 'POST',
                    data: {id : promotionId},
                    beforeSend: function () {
                        lsdRing.removeClass('d-none');
                    },
                    success: function (response) {
                        // Code to run on successful response
                        Swal.fire({
                            title: "Add Success",
                            icon: "success",
                            text: response.message,
                            confirmButtonText: "Oke",
                        }).then((result) => {
                            if (result.isConfirmed) {
                                window.location.reload();
                            }
                        });
                    },
                    error: function (xhr, status, errorThrown) {
                        // Lấy thông tin lỗi từ phản hồi
                        let errorMessage = xhr.responseText;

                        // Phân tích JSON nếu phản hồi là JSON
                        try {
                            let responseJson = JSON.parse(xhr.responseText);
                            errorMessage = responseJson.message || "Internal Error. Please try later";
                        } catch(e) {
                            console.error("Error parsing JSON response:", e);
                        }

                        // Hiển thị thông báo lỗi
                        Swal.fire({
                            title: "Add Fail",
                            icon: "error",
                            text: errorMessage,
                            confirmButtonText: "Let's Go",
                        });

                        // Log lỗi ra console
                        console.log('Error:', errorThrown);
                    },
                    complete: function () {
                        lsdRing.addClass('d-none');
                    }
                });
            }
        });
    });
}