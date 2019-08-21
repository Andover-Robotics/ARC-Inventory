let gapiLoaded = false;

gapi.load('auth2', initSigninV2);

function initSigninV2() {
    let clientId = getMetaByName('google-signin-client_id');
    gapi.auth2.init({
        client_id: (clientId !== null) ? clientId : '946689851710-1c8g9b9cce9ltiijc1mjfsvjf8f0u8vm.apps.googleusercontent.com'
    }).then(function (authInstance) {
        // now auth2 is fully initialized
        gapiLoaded = true;
    });
}

function getMetaByName(name) {
    let metas = document.getElementsByTagName('meta');

    for (let i = 0; i < metas.length; i++) {
        if (metas[i].getAttribute('name') === name) {
            return metas[i].getAttribute('content');
        }
    }

    return null;
}

function googleSignInSuccess(googleUser) {
    // Extract and POST the token
    let token = googleUser.getAuthResponse().id_token;
    let email = googleUser.getBasicProfile().getEmail();
    $.ajax({
        type: "POST",
        url: "/login",
        data: JSON.stringify({
            'token': token,
            'email': email
        }),
        contentType: "application/json",
        dataType: 'text',
        success: () => {
            let redirectLocation = findGetParameter('redirect');

            if (redirectLocation != null) {
                window.location.href = redirectLocation;
            } else
                window.location.href = '/';
        },
        error: (jqXHR) => {
            signOut(null);

            if (jqXHR.status === 401)
                $('#err-msg').html('Sorry, but that Google account is not authorized to use ARC Inventory! Please speak with a board member to ensure you are on the whitelist.');
            else
                $('#err-msg').html('Sorry, but an unknown error has occurred! Please try again at another time or with a different Google account.<br>If the problem persists, please speak with a CSO.');
        }
    });
}

function signOut(redirect) {
    let auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(() => {
        console.log('User signed out');
        if (typeof redirect === 'string' || redirect instanceof String) {
            window.location.href = redirect;
        }
    });
}