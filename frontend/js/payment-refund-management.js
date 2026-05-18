// ==========================================
// Payment & Refund Management JavaScript
// ==========================================

const API_BASE_URL = 'http://localhost:8080/api';
let currentPaymentPage = 1;
let currentRefundPage = 1;
let allPayments = [];
let allRefunds = [];
let selectedPaymentId = null;
let selectedRefundId = null;

// ==========================================
// Payment Management Functions
// ==========================================

async function loadPayments() {
    try {
        const response = await fetch(`${API_BASE_URL}/payments`);
        const payments = await response.json();
        allPayments = payments;
        
        // Apply filters
        const statusFilter = document.getElementById('status-filter')?.value;
        const typeFilter = document.getElementById('payment-type-filter')?.value;
        
        let filtered = payments;
        if (statusFilter) {
            filtered = filtered.filter(p => p.status === statusFilter);
        }
        if (typeFilter) {
            filtered = filtered.filter(p => p.paymentType === typeFilter);
        }
        
        displayPayments(filtered);
    } catch (error) {
        console.error('Error loading payments:', error);
        showTableError('Error loading payments');
    }
}

function displayPayments(payments) {
    const tbody = document.getElementById('payments-table-body');
    if (!tbody) return;
    
    if (payments.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; padding: 2rem;">No payments found</td></tr>';
        return;
    }
    
    tbody.innerHTML = payments.map(payment => `
        <tr>
            <td><strong>${payment.paymentId}</strong></td>
            <td>${payment.eventId}</td>
            <td>${payment.userId}</td>
            <td>$${parseFloat(payment.amount).toFixed(2)}</td>
            <td>${payment.paymentType}</td>
            <td><span class="badge ${payment.status.toLowerCase()}">${payment.status}</span></td>
            <td>${formatDate(payment.paymentDate || payment.createdAt)}</td>
            <td>
                <button class="btn-primary" onclick="viewPaymentDetails(${payment.id})" style="padding: 0.4rem 0.8rem; font-size: 0.85rem;">View</button>
                <button class="btn-secondary" onclick="deletePaymentConfirm(${payment.id})" style="padding: 0.4rem 0.8rem; font-size: 0.85rem;">Delete</button>
            </td>
        </tr>
    `).join('');
}

async function updateStatistics() {
    try {
        const response = await fetch(`${API_BASE_URL}/payments`);
        const payments = await response.json();
        
        const completed = payments.filter(p => p.status === 'COMPLETED').length;
        const pending = payments.filter(p => p.status === 'PENDING').length;
        const failed = payments.filter(p => p.status === 'FAILED').length;
        const total = payments.reduce((sum, p) => sum + p.amount, 0);
        
        document.getElementById('completed-count').textContent = completed;
        document.getElementById('pending-count').textContent = pending;
        document.getElementById('failed-count').textContent = failed;
        document.getElementById('total-amount').textContent = '$' + total.toFixed(2);
    } catch (error) {
        console.error('Error updating statistics:', error);
    }
}

function viewPaymentDetails(paymentId) {
    window.location.href = `payment-details.html?id=${paymentId}`;
}

async function loadPaymentDetails(paymentId) {
    try {
        const response = await fetch(`${API_BASE_URL}/payments/${paymentId}`);
        const payment = await response.json();
        
        document.getElementById('detail-payment-id').textContent = payment.paymentId;
        document.getElementById('detail-transaction-id').textContent = payment.transactionId;
        document.getElementById('detail-event-id').textContent = payment.eventId;
        document.getElementById('detail-user-id').textContent = payment.userId;
        document.getElementById('detail-amount').textContent = '$' + parseFloat(payment.amount).toFixed(2);
        document.getElementById('detail-currency').textContent = payment.currency;
        document.getElementById('detail-type').textContent = payment.paymentType;
        document.getElementById('detail-type').className = 'badge';
        document.getElementById('detail-type').classList.add(payment.paymentType.toLowerCase());
        document.getElementById('detail-status').textContent = payment.status;
        document.getElementById('detail-status').className = 'badge';
        document.getElementById('detail-status').classList.add(payment.status.toLowerCase());
        document.getElementById('detail-description').textContent = payment.description || '-';
        document.getElementById('detail-payment-date').textContent = formatDate(payment.paymentDate) || '-';
        document.getElementById('detail-created-at').textContent = formatDate(payment.createdAt);
        document.getElementById('detail-updated-at').textContent = formatDate(payment.updatedAt);
        
        // Update receipt section
        document.getElementById('receipt-payment-id').textContent = payment.paymentId;
        document.getElementById('receipt-amount').textContent = '$' + parseFloat(payment.amount).toFixed(2);
        document.getElementById('receipt-type').textContent = payment.paymentType;
        document.getElementById('receipt-date').textContent = formatDate(payment.createdAt);
        document.getElementById('receipt-status').textContent = payment.status;
        
        selectedPaymentId = paymentId;
    } catch (error) {
        console.error('Error loading payment details:', error);
    }
}

function resetFilters() {
    document.getElementById('status-filter').value = '';
    document.getElementById('payment-type-filter').value = '';
    loadPayments();
}

async function deletePaymentConfirm(paymentId) {
    if (confirm('Are you sure you want to delete this payment?')) {
        try {
            const response = await fetch(`${API_BASE_URL}/payments/${paymentId}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                alert('Payment deleted successfully');
                loadPayments();
            }
        } catch (error) {
            console.error('Error deleting payment:', error);
        }
    }
}

// ==========================================
// Refund Management Functions
// ==========================================

async function loadRefunds() {
    try {
        const response = await fetch(`${API_BASE_URL}/refunds`);
        const refunds = await response.json();
        allRefunds = refunds;
        
        // Apply filters
        const statusFilter = document.getElementById('refund-status-filter')?.value;
        const typeFilter = document.getElementById('refund-type-filter')?.value;
        
        let filtered = refunds;
        if (statusFilter) {
            filtered = filtered.filter(r => r.status === statusFilter);
        }
        if (typeFilter) {
            filtered = filtered.filter(r => r.refundType === typeFilter);
        }
        
        displayRefunds(filtered);
    } catch (error) {
        console.error('Error loading refunds:', error);
        showTableError('Error loading refunds');
    }
}

function displayRefunds(refunds) {
    const tbody = document.getElementById('refunds-table-body');
    if (!tbody) return;
    
    if (refunds.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; padding: 2rem;">No refunds found</td></tr>';
        return;
    }
    
    tbody.innerHTML = refunds.map(refund => `
        <tr>
            <td><strong>${refund.refundId}</strong></td>
            <td>${refund.paymentId}</td>
            <td>$${parseFloat(refund.amount).toFixed(2)}</td>
            <td>${refund.refundType}</td>
            <td>${refund.reason}</td>
            <td><span class="badge ${refund.status.toLowerCase()}">${refund.status}</span></td>
            <td>${refund.approvedBy || '-'}</td>
            <td>
                <button class="btn-primary" onclick="viewRefundDetails(${refund.id})" style="padding: 0.4rem 0.8rem; font-size: 0.85rem;">View</button>
                <button class="btn-secondary" onclick="deleteRefundConfirm(${refund.id})" style="padding: 0.4rem 0.8rem; font-size: 0.85rem;">Delete</button>
            </td>
        </tr>
    `).join('');
}

async function updateRefundStatistics() {
    try {
        const response = await fetch(`${API_BASE_URL}/refunds`);
        const refunds = await response.json();
        
        const pending = refunds.filter(r => r.status === 'PENDING').length;
        const completed = refunds.filter(r => r.status === 'COMPLETED').length;
        const approved = refunds.filter(r => r.status === 'APPROVED').length;
        const total = refunds.reduce((sum, r) => sum + r.amount, 0);
        
        document.getElementById('pending-refunds').textContent = pending;
        document.getElementById('completed-refunds').textContent = completed;
        document.getElementById('approved-refunds').textContent = approved;
        document.getElementById('total-refund-amount').textContent = '$' + total.toFixed(2);
    } catch (error) {
        console.error('Error updating refund statistics:', error);
    }
}

function viewRefundDetails(refundId) {
    window.location.href = `refund-details.html?id=${refundId}`;
}

async function loadRefundDetails(refundId) {
    try {
        const response = await fetch(`${API_BASE_URL}/refunds/${refundId}`);
        const refund = await response.json();
        
        document.getElementById('detail-refund-id').textContent = refund.refundId;
        document.getElementById('detail-payment-id').textContent = refund.paymentId;
        document.getElementById('detail-refund-amount').textContent = '$' + parseFloat(refund.amount).toFixed(2);
        document.getElementById('detail-refund-type').textContent = refund.refundType;
        document.getElementById('detail-refund-status').textContent = refund.status;
        document.getElementById('detail-refund-status').className = 'badge';
        document.getElementById('detail-refund-status').classList.add(refund.status.toLowerCase());
        document.getElementById('detail-refund-reason').textContent = refund.reason;
        document.getElementById('detail-approved-by').textContent = refund.approvedBy || '-';
        document.getElementById('detail-approval-date').textContent = formatDate(refund.approvalDate) || '-';
        document.getElementById('detail-refund-date').textContent = formatDate(refund.refundDate) || '-';
        document.getElementById('detail-refund-notes').textContent = refund.notes || '-';
        document.getElementById('detail-created-at').textContent = formatDate(refund.createdAt);
        document.getElementById('detail-updated-at').textContent = formatDate(refund.updatedAt);
        
        // Update timeline
        document.getElementById('timeline-created').textContent = formatDate(refund.createdAt);
        document.getElementById('timeline-updated').textContent = formatDate(refund.updatedAt);
        document.getElementById('timeline-approved').textContent = refund.approvalDate ? formatDate(refund.approvalDate) : 'Pending...';
        document.getElementById('timeline-refund').textContent = refund.refundDate ? formatDate(refund.refundDate) : 'Pending...';
        
        // Show/hide action buttons based on status
        if (refund.status === 'PENDING') {
            document.getElementById('approve-action-btn').style.display = 'inline-block';
            document.getElementById('reject-action-btn').style.display = 'inline-block';
        } else if (refund.status === 'APPROVED') {
            document.getElementById('complete-action-btn').style.display = 'inline-block';
        }
        
        selectedRefundId = refundId;
    } catch (error) {
        console.error('Error loading refund details:', error);
    }
}

function resetRefundFilters() {
    document.getElementById('refund-status-filter').value = '';
    document.getElementById('refund-type-filter').value = '';
    loadRefunds();
}

async function deleteRefundConfirm(refundId) {
    if (confirm('Are you sure you want to delete this refund?')) {
        try {
            const response = await fetch(`${API_BASE_URL}/refunds/${refundId}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                alert('Refund deleted successfully');
                loadRefunds();
            }
        } catch (error) {
            console.error('Error deleting refund:', error);
        }
    }
}

// ==========================================
// Receipt Functions
// ==========================================

async function loadReceiptDetails(paymentId) {
    try {
        const response = await fetch(`${API_BASE_URL}/payments/${paymentId}`);
        const payment = await response.json();
        
        document.getElementById('receipt-number').textContent = payment.paymentId;
        document.getElementById('receipt-date').textContent = formatDate(payment.createdAt);
        document.getElementById('receipt-txn-id').textContent = payment.transactionId;
        document.getElementById('receipt-status-badge').textContent = payment.status;
        document.getElementById('receipt-status-badge').className = `badge ${payment.status.toLowerCase()}`;
        document.getElementById('receipt-event-id').textContent = payment.eventId;
        document.getElementById('receipt-user-id').textContent = payment.userId;
        document.getElementById('receipt-method').textContent = payment.paymentType;
        document.getElementById('receipt-currency').textContent = payment.currency;
        document.getElementById('receipt-description').textContent = payment.description || 'Event Ticket Purchase';
        document.getElementById('receipt-amount').textContent = '$' + parseFloat(payment.amount).toFixed(2);
        document.getElementById('receipt-total').textContent = '$' + parseFloat(payment.amount).toFixed(2);
    } catch (error) {
        console.error('Error loading receipt details:', error);
    }
}

// ==========================================
// Utility Functions
// ==========================================

function formatDate(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { 
        year: 'numeric', 
        month: 'short', 
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function showTableError(message) {
    const tbody = document.getElementById('payments-table-body') || document.getElementById('refunds-table-body');
    if (tbody) {
        tbody.innerHTML = `<tr><td colspan="8" style="text-align: center; color: red; padding: 2rem;">${message}</td></tr>`;
    }
}

function openPaymentModal(paymentId) {
    selectedPaymentId = paymentId;
    const modal = document.getElementById('payment-modal');
    modal.style.display = 'block';
}

function closePaymentModal() {
    document.getElementById('payment-modal').style.display = 'none';
}

function openStatusUpdate() {
    document.getElementById('status-modal').style.display = 'block';
}

function closeStatusModal() {
    document.getElementById('status-modal').style.display = 'none';
}

async function updatePaymentStatus() {
    const status = document.getElementById('new-status').value;
    try {
        const response = await fetch(`${API_BASE_URL}/payments/${selectedPaymentId}/status?status=${status}`, {
            method: 'PUT'
        });
        if (response.ok) {
            alert('Payment status updated successfully');
            closeStatusModal();
            loadPaymentDetails(selectedPaymentId);
        }
    } catch (error) {
        console.error('Error updating payment status:', error);
    }
}

function openRefundModal(refundId) {
    selectedRefundId = refundId;
    const modal = document.getElementById('refund-modal');
    modal.style.display = 'block';
}

function closeRefundModal() {
    document.getElementById('refund-modal').style.display = 'none';
}

async function approveRefund() {
    const approvedBy = prompt('Enter approver name:');
    if (approvedBy) {
        try {
            const response = await fetch(`${API_BASE_URL}/refunds/${selectedRefundId}/approve?approvedBy=${approvedBy}`, {
                method: 'PUT'
            });
            if (response.ok) {
                alert('Refund approved successfully');
                closeRefundModal();
                loadRefunds();
            }
        } catch (error) {
            console.error('Error approving refund:', error);
        }
    }
}

async function rejectRefund() {
    if (confirm('Are you sure you want to reject this refund?')) {
        try {
            const response = await fetch(`${API_BASE_URL}/refunds/${selectedRefundId}/reject`, {
                method: 'PUT'
            });
            if (response.ok) {
                alert('Refund rejected successfully');
                closeRefundModal();
                loadRefunds();
            }
        } catch (error) {
            console.error('Error rejecting refund:', error);
        }
    }
}

// Pagination
function nextPage() {
    currentPaymentPage++;
    loadPayments();
    document.getElementById('page-info').textContent = `Page ${currentPaymentPage}`;
}

function previousPage() {
    if (currentPaymentPage > 1) {
        currentPaymentPage--;
        loadPayments();
        document.getElementById('page-info').textContent = `Page ${currentPaymentPage}`;
    }
}

function nextRefundPage() {
    currentRefundPage++;
    loadRefunds();
    document.getElementById('refund-page-info').textContent = `Page ${currentRefundPage}`;
}

function previousRefundPage() {
    if (currentRefundPage > 1) {
        currentRefundPage--;
        loadRefunds();
        document.getElementById('refund-page-info').textContent = `Page ${currentRefundPage}`;
    }
}

// Close modal when clicking outside
window.onclick = function(event) {
    const paymentModal = document.getElementById('payment-modal');
    const statusModal = document.getElementById('status-modal');
    const refundModal = document.getElementById('refund-modal');
    
    if (event.target === paymentModal) {
        paymentModal.style.display = 'none';
    }
    if (event.target === statusModal) {
        statusModal.style.display = 'none';
    }
    if (event.target === refundModal) {
        refundModal.style.display = 'none';
    }
}
