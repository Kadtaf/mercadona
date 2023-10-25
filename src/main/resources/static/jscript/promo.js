function showPromotionForm(productId) {
    console.log("totototo");

    // Afficher le formulaire de la promotion
   // document.getElementById('promotionForm-' + productId).style.display = 'block';
}

function savePromotion() {
    // Récupérer les valeurs du formulaire
    let startDate = document.getElementById('startDateModal').value;
    let endDate = document.getElementById('endDateModal').value;
    let discountPercentage = document.getElementById('discountPercentageModal').value;
    let productId = document.getElementById('productIdModal').value;
    console.log(startDate);
    console.log(endDate);
    console.log(discountPercentage);
    console.log(productId);
    /*
    // Effectuer le calcul du nouveau prix
    let currentPrice = parseFloat(document.getElementById('prix-' + productId).innerText.split(':')[1].trim());
    let newPrice = currentPrice - (currentPrice * discountPercentage / 100);

    // Effectuer une requête POST pour enregistrer la promotion
    let xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/products/savePromotion', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // Mettre à jour l'affichage avec les détails de la promotion
            document.getElementById('newPrice-' + productId).innerText = 'Nouveau prix : ' + newPrice.toFixed(2);
            document.getElementById('startDate-' + productId).innerText = 'Date de début de promotion : ' + startDate;
            document.getElementById('endDate-' + productId).innerText = 'Date de fin de promotion : ' + endDate;

            //Vider et Cacher le formulaire après l'enregistrement
            document.getElementById('promotionForm-' + productId).style.display = 'none';

        } else if (xhr.readyState === 4 && xhr.status !== 200) {
            alert('Une erreur s\'est produite lors de l\'enregistrement de la promotion.');
        }
    };

    // Construire les données POST
    let data = 'productId=' + encodeURIComponent(productId)
        + '&startDate=' + encodeURIComponent(startDate)
        + '&endDate=' + encodeURIComponent(endDate)
        + '&discountPercentage=' + encodeURIComponent(discountPercentage);

    // Envoyer la requête POST avec les données
    xhr.send(data);
}*/
    var form = document.getElementById('promotionForm');
    var data = new FormData(form);
    console.log(data);
    for (var [key, value] of data) {

        console.log(key, value)

    }
}