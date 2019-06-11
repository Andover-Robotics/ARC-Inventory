alert(JSON.stringify((function() {
    const strategies = {
        pitsco: function() {
            return {
                name: document.querySelector('*[itemprop="name"]').innerText,
                partNumber: document.querySelector('*[itemprop="sku"]').innerText,
                brand: 'Tetrix',
                url: document.URL,
                imageUrl: document.querySelector('img[itemprop="image"]').src
            };
        }
    };

    return function() {
        for (let key in strategies) {
            if (document.URL.includes(key)) {
                return strategies[key]();
            }
        }
    }
})()()));