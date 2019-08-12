function googleSignInSuccess(googleUser) {
    // Extract and POST the id_token
    let id_token = googleUser.getAuthResponse().id_token;
    $.ajax({
        type: "POST",
        url: "/login",
        data: JSON.stringify({
            'token':id_token
        }),
        contentType : "application/json",
        dataType : 'json',
        success: (data, textStatus, jqXHR) => {
            console.log(data, textStatus, jqXHR);
        }
    });
}

function signOut() {
    let auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(() => {
        console.log('User signed out.');
    });
}