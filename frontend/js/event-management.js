/**
 * Event Management Module — Shared JavaScript
 * SE1020 OOP Group Project
 *
 * This file contains:
 *  - API base URL configuration
 *  - Reusable fetch functions for all CRUD operations
 *  - Toast notification system
 *  - Confirmation modal
 *  - Utility helpers (badge colors, emoji icons, date formatting)
 */

// =====================================================
// 1. API CONFIGURATION
// =====================================================
const API_BASE = 'http://localhost:8080/api/events';

// =====================================================
// 2. CORE API FUNCTIONS
// =====================================================

/** Fetch all events */
async function getAllEvents() {
    const res = await fetch(API_BASE);
    if (!res.ok) throw new Error('Failed to fetch events');
    return res.json();
}

/** Fetch single event by ID */
async function getEventById(id) {
    const res = await fetch(`${API_BASE}/${id}`);
    if (!res.ok) throw new Error(`Event with ID ${id} not found`);
    return res.json();
}

/** Search events with optional filters */
async function searchEvents(keyword = '', category = '', status = '') {
    const params = new URLSearchParams();
    if (keyword) params.append('keyword', keyword);
    if (category) params.append('category', category);
    if (status)   params.append('status', status);
    const res = await fetch(`${API_BASE}/search?${params.toString()}`);
    if (!res.ok) throw new Error('Search failed');
    return res.json();
}

/** Create a new event */
async function createEvent(eventData) {
    const res = await fetch(API_BASE, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(eventData)
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.message || JSON.stringify(data));
    return data;
}

/** Update an existing event by ID */
async function updateEvent(id, eventData) {
    const res = await fetch(`${API_BASE}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(eventData)
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.message || JSON.stringify(data));
    return data;
}

/** Cancel an event (PATCH status to CANCELLED) */
async function cancelEvent(id) {
    const res = await fetch(`${API_BASE}/${id}/cancel`, { method: 'PATCH' });
    const data = await res.json();
    if (!res.ok) throw new Error(data.message || 'Cancel failed');
    return data;
}

/** Delete an event permanently */
async function deleteEvent(id) {
    const res = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
    if (!res.ok) {
        const data = await res.json();
        throw new Error(data.message || 'Delete failed');
    }
}

// =====================================================
// 3. TOAST NOTIFICATIONS
// =====================================================

function showToast(message, type = 'success') {
    // Create container if it doesn't exist
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

    // Auto-remove after 3.5 seconds
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

/**
 * Shows a confirmation popup before a destructive action.
 * Returns a Promise that resolves true (confirm) or false (cancel).
 */
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

/** Returns a CSS badge class based on status */
function getStatusBadge(status) {
    const map = {
        UPCOMING: 'badge-upcoming',
        ONGOING: 'badge-ongoing',
        COMPLETED: 'badge-completed',
        CANCELLED: 'badge-cancelled'
    };
    return `badge ${map[status] || 'badge-upcoming'}`;
}

/** Returns a CSS badge class based on category */
function getCategoryBadge(category) {
    const map = {
        CONCERT: 'badge-concert',
        SPORT: 'badge-sport',
        DRAMA: 'badge-drama',
        MOVIE: 'badge-movie',
        FESTIVAL: 'badge-festival',
        CONFERENCE: 'badge-conference',
        WORKSHOP: 'badge-workshop'
    };
    return `badge ${map[category] || ''}`;
}

/** Returns an emoji icon for each event type */
function getEventIcon(eventType) {
    const map = {
        CONCERT:    '',
        SPORT:      '',
        DRAMA:      '',
        MOVIE:      '',
        FESTIVAL:   '',
        CONFERENCE: '',
        WORKSHOP:   ''
    };
    return map[eventType] || '';
}

/** Format a LocalDate string (YYYY-MM-DD) to a readable format */
function formatDate(dateStr) {
    if (!dateStr) return '—';
    const d = new Date(dateStr + 'T00:00:00');
    return d.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });
}

/** Format a LocalTime string (HH:mm:ss) to 12-hour format */
function formatTime(timeStr) {
    if (!timeStr) return '—';
    const [h, m] = timeStr.split(':');
    const hour = parseInt(h);
    const ampm = hour >= 12 ? 'PM' : 'AM';
    const hour12 = hour % 12 || 12;
    return `${hour12}:${m} ${ampm}`;
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
// 6. SIDEBAR ACTIVE STATE & THEME
// =====================================================
function setActiveNav() {
    const currentPage = window.location.pathname.split('/').pop();
    document.querySelectorAll('.nav-item[data-page]').forEach(item => {
        item.classList.toggle('active', item.dataset.page === currentPage);
    });
}

function toggleTheme() {
    const root = document.documentElement;
    const currentTheme = root.getAttribute('data-theme') || 'dark';
    const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
    root.setAttribute('data-theme', newTheme);
    localStorage.setItem('theme', newTheme);
    
    // Update switch icon
    const themeIcon = document.getElementById('theme-icon-emoji');
    if (themeIcon) {
        themeIcon.textContent = newTheme === 'dark' ? 'Dark' : 'Light';
    }
}

function initTheme() {
    const savedTheme = localStorage.getItem('theme') || 'dark';
    document.documentElement.setAttribute('data-theme', savedTheme);
    
    const themeIcon = document.getElementById('theme-icon');
    if (themeIcon) {
        themeIcon.textContent = savedTheme === 'dark' ? 'Light' : 'Dark';
    }
}

// Run on all pages
document.addEventListener('DOMContentLoaded', () => {
    setActiveNav();
    initTheme();
});
