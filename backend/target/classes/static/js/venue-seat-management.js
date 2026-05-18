/**
 * venue-seat-management.js
 * Shared utility functions used across all frontend pages.
 * Connects to Spring Boot backend at http://localhost:8080
 */

const API_BASE = 'http://localhost:8080/api';

// =====================================================
// TOAST NOTIFICATIONS
// =====================================================
function showToast(message, type = 'info') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    container.className = 'toast-container';
    document.body.appendChild(container);
  }
  const icons = { success: '', error: '', warning: '', info: '' };
  const toast = document.createElement('div');
  toast.className = `toast ${type}`;
  toast.innerHTML = `<span>${icons[type] || ''}</span><span>${message}</span><button class="toast-close" onclick="this.parentElement.remove()">X</button>`;
  container.appendChild(toast);
  setTimeout(() => toast.remove(), 4500);
}

// =====================================================
// CONFIRM MODAL
// =====================================================
function showConfirm(title, message, onConfirm) {
  let overlay = document.getElementById('confirm-modal');
  if (!overlay) {
    overlay = document.createElement('div');
    overlay.id = 'confirm-modal';
    overlay.className = 'modal-overlay';
    overlay.innerHTML = `
      <div class="modal">
        <div class="modal-icon"></div>
        <h3 id="confirm-title"></h3>
        <p id="confirm-message"></p>
        <div class="modal-actions">
          <button class="btn btn-ghost" id="confirm-cancel">Cancel</button>
          <button class="btn btn-danger" id="confirm-ok">Confirm</button>
        </div>
      </div>`;
    document.body.appendChild(overlay);
    document.getElementById('confirm-cancel').onclick = () => overlay.classList.remove('open');
  }
  document.getElementById('confirm-title').textContent = title;
  document.getElementById('confirm-message').textContent = message;
  overlay.classList.add('open');
  document.getElementById('confirm-ok').onclick = () => { overlay.classList.remove('open'); onConfirm(); };
}

// =====================================================
// LOADING STATE HELPERS
// =====================================================
function setButtonLoading(btn, loading, originalText) {
  if (loading) {
    btn.disabled = true;
    btn.innerHTML = `<span class="loading-spinner"></span> Loading...`;
  } else {
    btn.disabled = false;
    btn.innerHTML = originalText;
  }
}

function showLoading(containerId) {
  const el = document.getElementById(containerId);
  if (el) el.innerHTML = `<div class="loading-overlay"><div class="loading-spinner" style="width:32px;height:32px;border-width:3px"></div><p>Loading...</p></div>`;
}

function showEmpty(containerId, message = 'No data found') {
  const el = document.getElementById(containerId);
  if (el) el.innerHTML = `<div class="empty-state"><div class="empty-icon"></div><h3>${message}</h3><p>Try adjusting your filters or add new items.</p></div>`;
}

// =====================================================
// BADGE HELPERS
// =====================================================
function venueBadge(status) {
  const map = { ACTIVE: 'active', INACTIVE: 'inactive', MAINTENANCE: 'maintenance' };
  return `<span class="badge badge-${map[status] || 'inactive'}">${status}</span>`;
}

function seatStatusBadge(status) {
  const map = { AVAILABLE: 'available', RESERVED: 'reserved', BOOKED: 'booked', BLOCKED: 'blocked', MAINTENANCE: 'maintenance' };
  return `<span class="badge badge-${map[status] || 'blocked'}">${status}</span>`;
}

function seatTypeBadge(type) {
  return type === 'VIP'
    ? `<span class="badge badge-vip">⭐ VIP</span>`
    : `<span class="badge badge-regular">REGULAR</span>`;
}

function venueTypeBadge(type) {
  return type === 'INDOOR'
    ? `<span class="badge badge-indoor">Indoor</span>`
    : `<span class="badge badge-outdoor">Outdoor</span>`;
}

// =====================================================
// FORMAT HELPERS
// =====================================================
function formatCurrency(amount) {
  if (amount == null) return 'LKR 0.00';
  return 'LKR ' + parseFloat(amount).toLocaleString('en-LK', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function formatDate(dateStr) {
  if (!dateStr) return 'N/A';
  return new Date(dateStr).toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' });
}

function getVenueIcon(type) {
  return type === 'INDOOR' ? '' : '';
}

// =====================================================
// URL PARAM HELPERS
// =====================================================
function getParam(name) {
  return new URLSearchParams(window.location.search).get(name);
}

// =====================================================
// API FUNCTIONS — VENUES
// =====================================================
async function apiGetAllVenues() {
  const res = await fetch(`${API_BASE}/venues`);
  if (!res.ok) throw new Error('Failed to fetch venues');
  return res.json();
}

async function apiGetVenue(id) {
  const res = await fetch(`${API_BASE}/venues/${id}`);
  if (!res.ok) throw new Error('Venue not found');
  return res.json();
}

async function apiSearchVenues(params) {
  const query = new URLSearchParams();
  if (params.keyword) query.set('keyword', params.keyword);
  if (params.city) query.set('city', params.city);
  if (params.status) query.set('status', params.status);
  if (params.venueType) query.set('venueType', params.venueType);
  const res = await fetch(`${API_BASE}/venues/search?${query}`);
  if (!res.ok) throw new Error('Search failed');
  return res.json();
}

async function apiCreateVenue(data) {
  const res = await fetch(`${API_BASE}/venues`, {
    method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data)
  });
  const json = await res.json();
  if (!res.ok) throw json;
  return json;
}

async function apiUpdateVenue(id, data) {
  const res = await fetch(`${API_BASE}/venues/${id}`, {
    method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data)
  });
  const json = await res.json();
  if (!res.ok) throw json;
  return json;
}

async function apiDeactivateVenue(id) {
  const res = await fetch(`${API_BASE}/venues/${id}/deactivate`, { method: 'PATCH' });
  const json = await res.json();
  if (!res.ok) throw json;
  return json;
}

async function apiActivateVenue(id) {
  const res = await fetch(`${API_BASE}/venues/${id}/activate`, { method: 'PATCH' });
  const json = await res.json();
  if (!res.ok) throw json;
  return json;
}

async function apiDeleteVenue(id) {
  const res = await fetch(`${API_BASE}/venues/${id}`, { method: 'DELETE' });
  if (!res.ok && res.status !== 204) throw new Error('Delete failed');
}

// =====================================================
// API FUNCTIONS — SEATS
// =====================================================
async function apiGetSeatLayout(venueId) {
  const res = await fetch(`${API_BASE}/venues/${venueId}/seats/layout`);
  if (!res.ok) throw new Error('Failed to load seat layout');
  return res.json();
}

async function apiGetSeat(seatId) {
  const res = await fetch(`${API_BASE}/seats/${seatId}`);
  if (!res.ok) throw new Error('Seat not found');
  return res.json();
}

async function apiSearchSeats(params) {
  const query = new URLSearchParams();
  if (params.venueId) query.set('venueId', params.venueId);
  if (params.section) query.set('section', params.section);
  if (params.seatType) query.set('seatType', params.seatType);
  if (params.seatStatus) query.set('seatStatus', params.seatStatus);
  const res = await fetch(`${API_BASE}/seats/search?${query}`);
  if (!res.ok) throw new Error('Seat search failed');
  return res.json();
}

async function apiAddSeat(venueId, data) {
  const res = await fetch(`${API_BASE}/venues/${venueId}/seats`, {
    method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data)
  });
  const json = await res.json();
  if (!res.ok) throw json;
  return json;
}

async function apiAddSeatBlock(venueId, data) {
  const res = await fetch(`${API_BASE}/venues/${venueId}/seats/bulk`, {
    method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data)
  });
  const json = await res.json();
  if (!res.ok) throw json;
  return json;
}

async function apiUpdateSeat(seatId, data) {
  const res = await fetch(`${API_BASE}/seats/${seatId}`, {
    method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data)
  });
  const json = await res.json();
  if (!res.ok) throw json;
  return json;
}

async function apiUpdateSeatStatus(seatId, status) {
  const res = await fetch(`${API_BASE}/seats/${seatId}/status`, {
    method: 'PATCH', headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ status })
  });
  const json = await res.json();
  if (!res.ok) throw json;
  return json;
}

async function apiDeleteSeat(seatId) {
  const res = await fetch(`${API_BASE}/seats/${seatId}`, { method: 'DELETE' });
  if (!res.ok && res.status !== 204) throw new Error('Delete failed');
}

// =====================================================
// FORM VALIDATION HELPERS
// =====================================================
function showFieldError(fieldId, message) {
  const errorEl = document.getElementById(fieldId + '-error');
  if (errorEl) { errorEl.textContent = message; errorEl.style.display = 'block'; }
  const input = document.getElementById(fieldId);
  if (input) input.style.borderColor = 'var(--danger)';
}

function clearFieldError(fieldId) {
  const errorEl = document.getElementById(fieldId + '-error');
  if (errorEl) { errorEl.textContent = ''; errorEl.style.display = 'none'; }
  const input = document.getElementById(fieldId);
  if (input) input.style.borderColor = '';
}

function clearAllErrors(formId) {
  const form = document.getElementById(formId);
  if (!form) return;
  form.querySelectorAll('.form-error').forEach(el => { el.textContent = ''; el.style.display = 'none'; });
  form.querySelectorAll('input, select, textarea').forEach(el => { el.style.borderColor = ''; });
}

function getFormErrors(json) {
  if (json && json.fieldErrors) {
    Object.entries(json.fieldErrors).forEach(([field, msg]) => showFieldError(field, msg));
  } else if (json && json.message) {
    showToast(json.message, 'error');
  } else {
    showToast('An error occurred. Please try again.', 'error');
  }
}

// =====================================================
// SIDEBAR ACTIVE LINK
// =====================================================
function setActiveNav(href) {
  document.querySelectorAll('.nav-item').forEach(a => {
    if (a.getAttribute('href') === href) a.classList.add('active');
    else a.classList.remove('active');
  });
}

// =====================================================
// SHARED SIDEBAR HTML
// =====================================================
function renderSidebar(activePage) {
  const nav = [
    { href: 'venue-dashboard.html', icon: '', label: 'Dashboard' },
    { href: 'add-venue.html',       icon: '', label: 'Add Venue' },
    { href: 'add-seat.html',        icon: '', label: 'Add Seat' },
    { href: 'add-seat-block.html',  icon: '', label: 'Add Seat Block' },
  ];
  const items = nav.map(n =>
    `<a class="nav-item ${activePage === n.href ? 'active' : ''}" href="${n.href}">
       <span class="icon">${n.icon}</span>${n.label}
     </a>`
  ).join('');
  return `
    <div class="sidebar-logo">
      <h2>EventBook</h2>
      <p>Venue & Seat Manager</p>
    </div>
    <nav class="sidebar-nav">
      <div class="nav-section-title">Navigation</div>
      ${items}
    </nav>`;
}
