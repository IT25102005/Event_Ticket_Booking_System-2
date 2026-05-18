// js/user-management.js — Live API version
// Connects to GET /api/users, GET /api/users/{id}, DELETE /api/users/{id}
// PATCH /api/users/{id}/activate | /deactivate

document.addEventListener('DOMContentLoaded', () => {
    const tableBody    = document.getElementById('userTableBody');
    const searchInput  = document.getElementById('searchInput');
    const roleFilter   = document.getElementById('roleFilter');
    const statusFilter = document.getElementById('statusFilter');

    let allUsers = [];

    // ---- Fetch from live API ----
    async function fetchUsers() {
        try {
            const response = await fetch(`${API_BASE_URL}/users`);
            if (!response.ok) throw new Error('Server error: ' + response.status);
            allUsers = await response.json();
            renderUsers(allUsers);
        } catch (error) {
            console.error('Error fetching users:', error);
            if (tableBody) {
                tableBody.innerHTML = `<tr><td colspan="4" style="text-align:center;padding:2rem;color:#ef4444;">
                    Error: Could not load users — make sure the backend is running on port 8080.
                </td></tr>`;
            }
        }
    }

    // ---- Render users into the table ----
    function renderUsers(users) {
        if (!tableBody) return;
        tableBody.innerHTML = '';

        if (!users || users.length === 0) {
            tableBody.innerHTML = `<tr><td colspan="4" style="text-align:center;padding:2rem;">No users found.</td></tr>`;
            updateSummaryCards([]);
            return;
        }

        users.forEach(user => {
            const firstName = user.firstName || '';
            const lastName  = user.lastName  || '';
            const initials  = `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase() || '?';
            const role   = user.role   || 'CUSTOMER';
            const status = user.status || 'ACTIVE';
            const id     = user.userId || user.id;

            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>
                    <div class="user-info">
                        <div class="avatar">${initials}</div>
                        <div class="user-details-text">
                            <span class="user-name">${firstName} ${lastName}</span>
                            <span class="user-email">${user.email || ''}</span>
                        </div>
                    </div>
                </td>
                <td>
                    <span class="badge role-${role.toLowerCase()}">${role}</span>
                </td>
                <td>
                    <span class="badge status-${status.toLowerCase()}">${status}</span>
                </td>
                <td>
                    <div class="action-btns">
                        <button class="btn btn-view"   onclick="viewUser(${id})">View</button>
                        <button class="btn btn-edit"   onclick="editUser(${id})">Edit</button>
                        <button class="btn btn-delete" onclick="deleteUser(${id}, this)">Delete</button>
                    </div>
                </td>
            `;
            tableBody.appendChild(tr);
        });

        updateSummaryCards(users);
    }

    // ---- Summary cards ----
    function updateSummaryCards(users) {
        const total    = document.getElementById('totalUsers');
        const active   = document.getElementById('activeUsers');
        const admins   = document.getElementById('adminUsers');
        const customers = document.getElementById('customerUsers');
        if (total)     total.innerText     = users.length;
        if (active)    active.innerText    = users.filter(u => u.status === 'ACTIVE').length;
        if (admins)    admins.innerText    = users.filter(u => u.role === 'ADMIN').length;
        if (customers) customers.innerText = users.filter(u => u.role === 'CUSTOMER').length;
    }

    // ---- Filter ----
    function filterData() {
        const query  = (searchInput  ? searchInput.value  : '').toLowerCase();
        const role   = (roleFilter   ? roleFilter.value   : 'ALL');
        const status = (statusFilter ? statusFilter.value : 'ALL');

        const filtered = allUsers.filter(user => {
            const name  = `${user.firstName || ''} ${user.lastName || ''}`.toLowerCase();
            const email = (user.email || '').toLowerCase();
            const matchesSearch = name.includes(query) || email.includes(query);
            const matchesRole   = role   === 'ALL' || user.role   === role;
            const matchesStatus = status === 'ALL' || user.status === status;
            return matchesSearch && matchesRole && matchesStatus;
        });

        renderUsers(filtered);
    }

    if (searchInput)  searchInput.addEventListener('input',  filterData);
    if (roleFilter)   roleFilter.addEventListener('change',  filterData);
    if (statusFilter) statusFilter.addEventListener('change', filterData);

    // Initial load
    fetchUsers();
});

// ---- Global action functions ----
function viewUser(id) {
    window.location.href = `user-details.html?id=${id}`;
}

function editUser(id) {
    window.location.href = `edit-profile.html?id=${id}`;
}

async function deleteUser(id, btn) {
    if (!confirm('Are you sure you want to delete this user?')) return;
    try {
        btn.disabled = true;
        btn.textContent = '...';
        const response = await fetch(`${API_BASE_URL}/users/${id}`, { method: 'DELETE' });
        if (response.ok) {
            alert('User deleted successfully!');
            location.reload();
        } else {
            alert('Failed to delete user. They may have associated data.');
            btn.disabled = false;
            btn.textContent = 'Delete';
        }
    } catch (error) {
        console.error('Delete error:', error);
        alert('Cannot connect to server.');
        btn.disabled = false;
        btn.textContent = 'Delete';
    }
}
