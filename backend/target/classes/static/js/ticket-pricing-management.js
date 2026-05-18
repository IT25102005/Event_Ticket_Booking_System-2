const API_BASE_URL = 'http://localhost:8080/api';

// --- Utility Functions ---
async function fetchData(endpoint) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`);
        if (!response.ok) throw new Error('Network response was not ok');
        return await response.json();
    } catch (error) {
        console.error('Fetch error:', error);
        showToast('Error fetching data', 'error');
    }
}

async function postData(endpoint, data) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (!response.ok) throw new Error('Error in submission');
        return await response.json();
    } catch (error) {
        console.error('Post error:', error);
        showToast('Submission failed', 'error');
    }
}

function showToast(message, type = 'success') {
    // Simple toast logic (can be enhanced)
    console.log(`[${type.toUpperCase()}] ${message}`);
    alert(message); // Temporary simple alert
}

// --- Dashboard Logic ---
async function loadDashboard() {
    const tickets = await fetchData('/tickets');
    if (!tickets) return;

    const tableBody = document.getElementById('ticket-table-body');
    if (tableBody) {
        tableBody.innerHTML = tickets.map(ticket => `
            <tr>
                <td>${ticket.ticketName}</td>
                <td><span class="badge ${ticket.ticketCategory.toLowerCase()}-badge">${ticket.ticketCategory}</span></td>
                <td>$${ticket.finalPrice.toFixed(2)}</td>
                <td>${ticket.availableQuantity} / ${ticket.totalQuantity}</td>
                <td><span class="badge ${ticket.status.toLowerCase()}-badge">${ticket.status}</span></td>
                <td>
                    <button class="btn btn-primary" onclick="viewTicket(${ticket.ticketTypeId})">View</button>
                    <button class="btn" onclick="editTicket(${ticket.ticketTypeId})">Edit</button>
                </td>
            </tr>
        `).join('');
    }

    // Update Summary Cards
    document.getElementById('total-types').innerText = tickets.length;
    document.getElementById('active-count').innerText = tickets.filter(t => t.status === 'ACTIVE').length;
    document.getElementById('sold-out-count').innerText = tickets.filter(t => t.status === 'SOLD_OUT').length;
}

// --- Navigation ---
function viewTicket(id) { window.location.href = `ticket-details.html?id=${id}`; }
function editTicket(id) { window.location.href = `edit-ticket.html?id=${id}`; }

// --- Form Handling ---
function handleTicketTypeChange() {
    const category = document.getElementById('ticketCategory').value;
    const vipFields = document.getElementById('vip-fields');
    const standardFields = document.getElementById('standard-fields');
    const earlyBirdFields = document.getElementById('earlybird-fields');

    if (vipFields) vipFields.style.display = category === 'VIP' ? 'block' : 'none';
    if (standardFields) standardFields.style.display = category === 'STANDARD' ? 'block' : 'none';
    if (earlyBirdFields) earlyBirdFields.style.display = category === 'EARLY_BIRD' ? 'block' : 'none';
}

async function submitTicketForm(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const data = Object.fromEntries(formData.entries());
    
    // Add logic to convert numeric strings to numbers
    data.basePrice = parseFloat(data.basePrice);
    data.totalQuantity = parseInt(data.totalQuantity);
    // ... handle other fields ...

    const result = await postData('/tickets', data);
    if (result) {
        showToast('Ticket created successfully!');
        window.location.href = 'ticket-dashboard.html';
    }
}
