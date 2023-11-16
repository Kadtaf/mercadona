
function showPromotionForm(productId, productPrice) {

    $('#productIdModal').val(productId)

    $('#priceModal').val(productPrice.toFixed(2));

}


function savePromotion() {
    let globalCategory;
    let selectCategory = $('#selectCategory').val();

    if(selectCategory === undefined || selectCategory === null || selectCategory === "") {
        globalCategory = 0;
    } else {
        globalCategory = selectCategory;
    }

    let globalPage = $('input.btn-info').val() - 1;


    let startDate = $('#startDateModal').val();
    let endDate = $('#endDateModal').val();
    let discountPercentage = $('#discountPercentageModal').val();
    let productId = $('#productIdModal').val();
    let productPrice = $('#priceModal').val();
    let form = document.getElementById('promotionForm');
    let datas = new FormData(form);


    let newPrice = productPrice * (1 - discountPercentage / 100);
    $('#priceId-' + productId).text('Prix : ' + newPrice.toFixed(2));



    $('#promotionForm')[0].reset();
    $('#addPromotionModal').hide();
    $('.modal-backdrop').remove();
    $('body').css("overflow", "unset");


    let token = $("meta[name='_csrf']").attr("content");

    let request =
        $.ajax({
            type: "POST",
            url: "/api/products/savePromotion",
            data:datas,
            dataType: 'json',
            timeout: 120000, //2 Minutes
            cache: false,
            contentType: false,
            processData: false,

        });

    request.done(function (response) {

        let selectorPromo = "#promotionId-" + response.id;
        let selectorPrice = "#newPriceId-" + response.id;


        $(selectorPromo).text(response.promotion.startDate + ' ' + response.promotion.endDate + ' ' + 'Promotion: ' + response.promotion.discountPercentage + '%');
        $(selectorPrice).text('Prix : ' + response.prix.toFixed(2)+  " €");

        const successAlert = $('<div>').addClass('alert alert-success alert-dismissible fade show').attr('role', 'alert')
            .html('Votre promotion a été ajoutée avec succès' +
                '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>');


        $('#alerts-container').append(successAlert);


        setTimeout(function () {
            successAlert.alert('close');
        }, 5000);


        getProductsByCategory(globalCategory, globalPage);


    });
    request.fail(function (http_error) {

        let server_msg = http_error.responseText;
        let code = http_error.status;
        let code_label = http_error.statusText;
        alert("Erreur "+code+" ("+code_label+") : "  + server_msg);
    });
    /*
        request.always(function () {

            getProductsByCategory(globalCategory, globalPage);
        });
        */

}

function getProductsByCategory(category, page=0) {
    console.log('Category: ' + category + ', Page: ' + page);
    let XHR = new XMLHttpRequest();
    XHR.open('get', '/api/products/listProducts?category='+ category + '&page=' + page );
    XHR.send();
    XHR.addEventListener('readystatechange', function() {
        console.log('Category: ' + category + ', Page: ' + page);
        console.log(XHR);
        if (XHR.readyState === XMLHttpRequest.DONE && XHR.status === 200) {

            document.getElementById("listProducts").innerHTML = XHR.responseText;
        }
    });
}