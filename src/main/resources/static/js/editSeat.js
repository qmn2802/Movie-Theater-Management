$(document).ready(function () {
    // updateStatusSeat();

});
var lsdRing = $('.lsd-ring-container');

function changeBtnSaveId(btnId) {
    const button = document.querySelector('.save-btn-modal');
    button.id = btnId;
}

function reverseDisableInput() {
    var inputs = document.querySelectorAll('.seat-row-list input[type="checkbox"]');
    inputs.forEach(function (input) {
        input.disabled = !input.disabled;
    });
}

function changTitleModal(title) {
    var modalTitle = document.querySelector('.modal-title');
    modalTitle.innerHTML = title;
}

function disableCheckedBookedSeats() {
    $('.seat-row-list input[type="checkbox"]:checked').each(function () {
        if (!$(this).hasClass('disabled-seat')) {
            $(this).prop('disabled', true);
        }
    });
}
function disableNormalSeats() {
    $('.seat-row-list input[type="checkbox"]:checked').each(function () {
        if (!$(this).hasClass('vip-to-normal')) {
            $(this).prop('disabled', true);
        }
    });
}

function enableDisabledInputs() {
    $('.seat-row-list input[type="checkbox"]').each(function () {
        if ($(this).is(':disabled')) {
            $(this).prop('disabled', false);
        }
    });
}

function disableAllSeats() {
    $('.seat-row-list input[type="checkbox"]').each(function () {
        $(this).prop('disabled', true);
    });
}

function updateUncheckedCheckboxClasses() {
    $('.seat-row-list .tgl.tgl-flip').each(function () {
        if (!$(this).is(':checked') && !$(this).hasClass('disabled-seat')) {
            $(this).addClass('disabled-seat').prop('checked', true);
        }
    });
}

function addClassForNormalToVipSeat() {
    $('.seat-row-list .tgl.tgl-flip').each(function () {
        if (!$(this).is(':checked') &&
            !$(this).hasClass('disabled-seat') &&
            !$(this).hasClass('vip-seat')) {
            $(this).addClass('normal-to-vip');
        }
    });

    $('.seat-row-list .tgl.tgl-flip.vip-seat').each(function () {
        $(this).addClass('vip-to-normal').prop('checked', true);
    });
}

function addOrDeleteSeat() {
    updateUncheckedCheckboxClasses();
    enableDisabledInputs();
    disableCheckedBookedSeats();
    changTitleModal('Add More Or Delete Seat');
    changeBtnSaveId('add-or-delete-seat-submit');
    createAddSeatButton();
    createAddRowButton();
    var cinemaRoomId = $("#menu-edit-seat").data("id");

    var initialState = {};
    $('.seat-row-list input[type="checkbox"]').each(function () {
        initialState[this.id] = $(this).is(':checked');
    });

    $(".modal .btn-close, .modal").on("click", function (event) {
        if ($(event.target).is(".btn-close, .modal")) {
            const seatRowList = document.querySelector('.seat-row-list');
            seatRowList.innerHTML = '';
            initialState = {};
            $("#menu-edit-seat").removeData("id");
        }
    });

    $(".save-btn-modal").on("click", function () {
        updateDeletedSeat(initialState, cinemaRoomId);
    });

}

function viewSeatDetail() {
    disableAllSeats();
    changTitleModal('View Seat Detail');

    const button = document.querySelector('.save-btn-modal');
        button.style.display = 'none';

    $(".modal .btn-close, .modal").on("click", function (event) {
        if ($(event.target).is(".btn-close, .modal")) {
            button.style.display = '';
        }
    });
}

function bookingSeat() {
    var initialState = {};
    $('.seat-row-list input[type="checkbox"]').each(function () {
        initialState[this.id] = $(this).is(':checked');
    });
    // console.log('initialState:', initialState);
    updateStatusSeat(initialState);
    // updateDeletedSeat(initialState)
    changTitleModal('Booking Seat');
    changeBtnSaveId('booking-seat-submit');
}

function setVipSeat() {
    addClassForNormalToVipSeat();
    disableNormalSeats();
    changeBtnSaveId('set-vip-seat-submit');
    changTitleModal('Set VIP Seat');

    var initialState = {};
    $('.seat-row-list input[type="checkbox"]').each(function () {
        initialState[this.id] = $(this).is(':checked');
    });

    updateTypeSeat(initialState);
}

//api
function updateStatusSeat(initialState) {
    $(document).on('click', '#booking-seat-submit', function (e) {

        var formData = {
            seatIdsBooked: getChangedCheckedCheckboxDataIds(initialState),
            seatIdsCancelled: getChangedUncheckedCheckboxDataIds(initialState)
        };

        console.log(formData);

        $.ajax({
            url: '/admin/update-booked-seat',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {
                // Code to run on successful response
                console.log('Success:', response);
                location.reload();
            },
            error: function (xhr, status, error) {
                // Code to run if the request fails
                // console.log('Error:', error);
            }
        });

    });
}

function updateTypeSeat(initialState) {
    $(document).on('click', '#set-vip-seat-submit', function (e) {

        var formData = {
            seatIdsBookedVip: getChangedCheckedCheckboxDataIds(initialState),
            seatIdsCancelledVip: getChangedUncheckedCheckboxDataIds(initialState)
        };

        console.log(formData);

        $.ajax({
            url: '/admin/update-type-seat',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {
                // Code to run on successful response
                console.log('Success:', response);
                location.reload();
            },
            error: function (xhr, status, error) {
                // Code to run if the request fails
                // console.log('Error:', error);
            }
        });

    });
}

function updateDeletedSeat(initialState, cinemaRoomId) {
    $(document).on('click', '#add-or-delete-seat-submit', function (e) {
        var formData = {
            cinemaRoomId: cinemaRoomId,
            seatNamesAdded: getChangedCheckedCheckboxIds(initialState),
            seatNamesDeleted: getChangedUncheckedCheckboxIds(initialState)
        };

        console.log(formData);
        if (!formData.cinemaRoomId || (!formData.seatNamesAdded && !formData.seatNamesDeleted)) {
            formData = {};
        } else {
            $.ajax({
                url: '/admin/update-deleted-seat',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                beforeSend: function () {
                    lsdRing.removeClass('d-none')
                },
                success: function (response) {
                    // Code to run on successful response
                    console.log('Update deleted success:', response);
                    location.reload();
                },
                error: function (xhr, status, error) {
                    // Code to run if the request fails
                    console.log('Error:', error);
                },
                complete: function () {
                    lsdRing.addClass('d-none');
                }
            });
        }
    });
}

// get data-id
function getChangedCheckedCheckboxDataIds(initialState) {
    var changedDataIds = [];
    $('.seat-row-list input[type="checkbox"]').each(function () {
        if (initialState[this.id] === false && $(this).is(':checked')) {
            changedDataIds.push($(this).data('id'));
        }
    });
    return changedDataIds;
}

function getChangedUncheckedCheckboxDataIds(initialState) {
    var changedDataIds = [];
    $('.seat-row-list input[type="checkbox"]').each(function () {
        if (initialState[this.id] === true && !$(this).is(':checked')) {
            changedDataIds.push($(this).data('id'));
        }
    });
    return changedDataIds;
}

// get id input
function getChangedCheckedCheckboxIds(initialState) {
    var changedIds = [];
    $('.seat-row-list input[type="checkbox"]').each(function () {
        if (initialState[this.id] === false && $(this).is(':checked')) {
            changedIds.push(this.id);
        } else if (!$(this).data('id') && $(this).is(':checked')) {
            changedIds.push(this.id);
        }
    });

    return changedIds;
}


function getChangedUncheckedCheckboxIds(initialState) {
    var changedIds = [];
    $('.seat-row-list input[type="checkbox"]').each(function () {
        if (initialState[this.id] === true && !$(this).is(':checked')) {
            changedIds.push(this.id); // Lấy ID thay vì data-id
        } else if (!$(this).data('id') && !$(this).is(':checked')) {
            changedIds.push(this.id);
        }

    });
    return changedIds;
}
