const API_CALCULATE_URL = 'http://localhost:8080/api/pricing/calculate';

async function calculatePrice() {
    const category = document.getElementById('calc-category').value;
    const basePrice = parseFloat(document.getElementById('calc-base-price').value) || 0;
    const discount = parseFloat(document.getElementById('calc-discount').value) || 0;
    const tax = parseFloat(document.getElementById('calc-tax').value) || 0;
    const serviceFee = parseFloat(document.getElementById('calc-service-fee').value) || 0;
    const extraFee = parseFloat(document.getElementById('calc-extra-fee').value) || 0;

    const requestData = {
        ticketCategory: category,
        basePrice: basePrice,
        discountPercentage: discount,
        taxPercentage: tax,
        serviceFee: serviceFee,
        extraFee: extraFee
    };

    try {
        const response = await fetch(API_CALCULATE_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestData)
        });
        const result = await response.json();

        document.getElementById('res-discount').innerText = `$${result.discountAmount.toFixed(2)}`;
        document.getElementById('res-tax').innerText = `$${result.taxAmount.toFixed(2)}`;
        document.getElementById('res-final').innerText = `$${result.finalPrice.toFixed(2)}`;
        
        // Visual Glow Effect on update
        const resultDisplay = document.querySelector('.result-display');
        resultDisplay.style.boxShadow = '0 0 30px rgba(0, 240, 255, 0.6)';
        setTimeout(() => {
            resultDisplay.style.boxShadow = 'var(--accent-glow)';
        }, 500);

    } catch (error) {
        console.error('Calculation error:', error);
    }
}

// Add event listeners for instant calculation
document.querySelectorAll('.calc-input').forEach(input => {
    input.addEventListener('input', calculatePrice);
});
