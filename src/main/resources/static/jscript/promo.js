function showPromotionForm(productId, productPrice) {
    console.log(productId);
    $('#productIdModal').val(productId)
    console.log(productPrice);
    console.log( $('#priceId-' + productId).text());
    $('#priceModal').val(productPrice);

    // $('#addPromotionModal').style.display = 'block';

}
//document.getElementById('addPromotionModal').addEventListener('submit', savePromotion());
function savePromotion() {
    console.log("tototo");
    // Récupérer les valeurs du formulaire
    let startDate = $('#startDateModal').val();
    let endDate = $('#endDateModal').val();
    let discountPercentage = $('#discountPercentageModal').val();
    let productId = $('#productIdModal').val();
    let productPrice = $('#priceModal').val();
    let form = document.getElementById('promotionForm');
    let datas = new FormData(form);




    /*for (var [key, value] of data) {

        console.log(key, value)

    }*/
    // Effectuer le calcul du nouveau prix

    /* let newPrice = productPrice * (1 - discountPercentage / 100);
      console.log(newPrice);
      console.log( document.getElementById('priceId-' + productId));
      $('#priceId-' + productId).text('Prix : ' + newPrice);*/
    $('#promotionForm')[0].reset();
    $('#addPromotionModal').hide();
    $('.modal-backdrop').remove();
    $('body').css("overflow", "unset");

    let token = $("meta[name='_csrf']").attr("content");
    console.log(token);
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
        alert("Votre promotion a été ajouté");
        $('#promotionId-' + productId).text(response.promotion.startDate + ' ' + response.promotion.endDate + ': ' + response.promotion.discountPercentage + '%');
        $('#priceId-' + productId).text('Prix : ' + response.prix);
    });
    request.fail(function (http_error) {
        //Code à jouer en cas d'éxécution en erreur du script du PHP

        let server_msg = http_error.responseText;
        let code = http_error.status;
        let code_label = http_error.statusText;
        alert("Erreur "+code+" ("+code_label+") : "  + server_msg);
    });

    request.always(function () {
       getProductsByCategory(globalCategory, globalPage);
    });
}
let xhr = new XMLHttpRequest();
xhr.open('GET', '/api/categories/allCategories', true);

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







