// =====================================================
// 1. API CONFIGURATION
// =====================================================
const API_BASE = 'http://localhost:8080/api/bookings';

// =====================================================
// 2. CORE API FUNCTIONS
// =====================================================

/** Fetch all bookings */
async function getAllBookings() {
    const res = await fetch(API_BASE);
    if (!res.ok) throw new Error('Failed to fetch bookings');
    return res.json();
}

/** Fetch single booking by ID */
async function getBookingById(id) {
    const res = await fetch(`${API_BASE}/${id}`);
    if (!res.ok) throw new Error(`Booking with ID ${id} not found`);
    return res.json();
}

/** Create a new booking */
async function createBooking(bookingData) {
    const res = await fetch(API_BASE, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(bookingData)
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.message || JSON.stringify(data));
    return data;
}

/** Update an existing booking by ID */
async function updateBooking(id, bookingData) {
    const res = await fetch(`${API_BASE}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(bookingData)
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.message || JSON.stringify(data));
    return data;
}

/** Cancel a booking (PATCH status to CANCELLED) */
async function cancelBooking(id) {
    const res = await fetch(`${API_BASE}/${id}/cancel`, { method: 'PATCH' });
    const data = await res.json();
    if (!res.ok) throw new Error(data.message || 'Cancel failed');
    return data;
}

/** Delete a booking permanently */
async function deleteBooking(id) {
    const res = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
    if (!res.ok) {
        const data = await res.json().catch(() => ({}));
        throw new Error(data.message || 'Delete failed');
    }
}

// =====================================================
// 3. TOAST NOTIFICATIONS
// =====================================================
function showToast(message, type = 'success') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }
    const icons = { success: '', error: '', warning: '', info: '' };
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `<span>${icons[type] || ''}</span><span>${message}</span>`;
    container.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(30px)';
        toast.style.transition = 'all 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 3500);
}

// =====================================================
// 4. CONFIRMATION MODAL
// =====================================================
function showConfirm(title, message, confirmText = 'Confirm', type = 'danger') {
    return new Promise((resolve) => {
        const overlay = document.getElementById('confirm-modal');
        document.getElementById('confirm-title').textContent = title;
        document.getElementById('confirm-message').textContent = message;
        const confirmBtn = document.getElementById('confirm-btn');
        confirmBtn.textContent = confirmText;
        confirmBtn.className = `btn btn-${type}`;
        overlay.classList.add('active');
        const onConfirm = () => { cleanup(); resolve(true); };
        const onCancel  = () => { cleanup(); resolve(false); };
        function cleanup() {
            overlay.classList.remove('active');
            confirmBtn.removeEventListener('click', onConfirm);
            document.getElementById('cancel-btn').removeEventListener('click', onCancel);
        }
        confirmBtn.addEventListener('click', onConfirm);
        document.getElementById('cancel-btn').addEventListener('click', onCancel);
    });
}

// =====================================================
// 5. UTILITY HELPERS
// =====================================================

/** Returns a CSS badge class based on booking status */
function getStatusBadge(status) {
    const map = {
        PENDING:   'badge-pending',
        CONFIRMED: 'badge-confirmed',
        CANCELLED: 'badge-cancelled'
    };
    return `badge ${map[status] || 'badge-pending'}`;
}

/** Returns a CSS badge class based on booking type */
function getTypeBadge(type) {
    const map = {
        ONLINE:  'badge-online',
        COUNTER: 'badge-counter'
    };
    return `badge ${map[type] || ''}`;
}

/** Returns an emoji icon for booking type */
function getTypeIcon(type) {
    const map = { ONLINE: '', COUNTER: '' };
    return map[type] || '';
}

/** Format a LocalDateTime string to readable format */
function formatDateTime(dateStr) {
    if (!dateStr) return '—';
    const d = new Date(dateStr);
    return d.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' }) +
        ' ' + d.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });
}

/** Format currency */
function formatPrice(price) {
    if (price == null) return '—';
    return `Rs. ${parseFloat(price).toFixed(2)}`;
}

/** Get query param from URL */
function getQueryParam(name) {
    return new URLSearchParams(window.location.search).get(name);
}

/** Redirect to a page */
function navigateTo(url) {
    window.location.href = url;
}

// =====================================================
// 6. SIDEBAR ACTIVE STATE
// =====================================================
function setActiveNav() {
    const currentPage = window.location.pathname.split('/').pop();
    document.querySelectorAll('.nav-item[data-page]').forEach(item => {
        item.classList.toggle('active', item.dataset.page === currentPage);
    });
}

document.addEventListener('DOMContentLoaded', setActiveNav);