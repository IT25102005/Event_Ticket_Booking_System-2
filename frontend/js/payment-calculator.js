// Payment Calculator JavaScript

function calculateTotalAmount() {
    const amount = parseFloat(document.getElementById('amount').value) || 0;
    const currency = document.getElementById('currency').value;
    
    // In real scenario, you might fetch exchange rates
    const exchangeRates = {
        'USD': 1,
        'EUR': 0.85,
        'GBP': 0.73,
        'INR': 82.50
    };
    
    const rate = exchangeRates[currency] || 1;
    const convertedAmount = amount * rate;
    
    return {
        originalAmount: amount,
        currency: currency,
        convertedAmount: convertedAmount,
        exchangeRate: rate
    };
}

function validatePaymentForm() {
    const eventId = document.getElementById('eventId').value;
    const userId = document.getElementById('userId').value;
    const amount = document.getElementById('amount').value;
    const paymentType = document.querySelector('input[name="paymentType"]:checked');
    
    if (!eventId || !userId || !amount || !paymentType) {
        return {
            valid: false,
            message: 'Please fill in all required fields'
        };
    }
    
    if (parseFloat(amount) <= 0) {
        return {
            valid: false,
            message: 'Amount must be greater than 0'
        };
    }
    
    return {
        valid: true,
        message: 'Form is valid'
    };
}

function getPaymentFee(amount, paymentType) {
    // Different payment methods may have different fees
    const fees = {
        'CARD': 0.029, // 2.9%
        'BANK_TRANSFER': 0.001, // 0.1%
        'WALLET': 0, // No fee
        'CASH': 0 // No fee
    };
    
    const feePercentage = fees[paymentType] || 0;
    return amount * feePercentage;
}

function calculateTotal(amount, paymentType) {
    const fee = getPaymentFee(amount, paymentType);
    return parseFloat(amount) + fee;
}

// Export functions for use in forms
function getPaymentDetails() {
    return {
        eventId: parseInt(document.getElementById('eventId').value),
        userId: parseInt(document.getElementById('userId').value),
        amount: parseFloat(document.getElementById('amount').value),
        currency: document.getElementById('currency').value,
        paymentType: document.querySelector('input[name="paymentType"]:checked')?.value,
        description: document.getElementById('description').value
    };
}
