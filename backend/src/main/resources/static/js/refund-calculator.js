// Refund Calculator JavaScript

function calculateRefundAmount(originalAmount, refundType) {
    if (refundType === 'FULL') {
        return originalAmount;
    } else if (refundType === 'PARTIAL') {
        // Default partial refund is 50%
        return originalAmount * 0.5;
    }
    return 0;
}

function getRefundProcessingFee(amount) {
    // Processing fee for refunds (typically less than payment fee)
    return amount * 0.01; // 1% processing fee
}

function getRefundTimeline(refundType) {
    // Different refund types may have different processing times
    const timelines = {
        'FULL': '3-5 business days',
        'PARTIAL': '2-3 business days'
    };
    
    return timelines[refundType] || 'Processing';
}

function validateRefundForm() {
    const paymentId = document.getElementById('paymentId').value;
    const amount = document.getElementById('refund-amount').value;
    const refundType = document.querySelector('input[name="refundType"]:checked');
    const reason = document.getElementById('reason').value;
    
    if (!paymentId || !amount || !refundType || !reason) {
        return {
            valid: false,
            message: 'Please fill in all required fields'
        };
    }
    
    if (parseFloat(amount) <= 0) {
        return {
            valid: false,
            message: 'Refund amount must be greater than 0'
        };
    }
    
    return {
        valid: true,
        message: 'Form is valid'
    };
}

function getRefundDetails() {
    return {
        paymentId: parseInt(document.getElementById('paymentId').value),
        amount: parseFloat(document.getElementById('refund-amount').value),
        refundType: document.querySelector('input[name="refundType"]:checked')?.value,
        reason: document.getElementById('reason').value,
        notes: document.getElementById('notes').value
    };
}

function calculateRefundSummary(amount, refundType) {
    const processingFee = getRefundProcessingFee(amount);
    const timeline = getRefundTimeline(refundType);
    
    return {
        originalAmount: amount,
        processingFee: processingFee,
        netRefund: amount - processingFee,
        estimatedTimeline: timeline,
        refundType: refundType
    };
}

// Validate refund reason
function validateRefundReason(reason) {
    const validReasons = [
        'Event Cancelled',
        'Customer Request',
        'Duplicate Payment',
        'Payment Error',
        'Event Postponed',
        'Other'
    ];
    
    return validReasons.includes(reason);
}
