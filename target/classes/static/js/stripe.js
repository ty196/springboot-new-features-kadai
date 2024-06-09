const stripe = Stripe('pk_test_51PK8doAfbYk8jEcWPOajxiN9ewr6R8dvUs0NZosUe1D6t3AjBPvpHWpEfKV6VdS9r663gDCpnqvcpht7Xvni0TZ100prwYkE8j');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
	stripe.redirectToCheckout({
		sessionId: sessionId
	})
});