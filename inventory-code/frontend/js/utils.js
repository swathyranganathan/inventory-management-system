// ============================================================
// utils.js — Shared UI helpers used by all pages
// ============================================================

export function formatCurrency(value) {
    if (value == null) return '$0.00';
    return '$' + parseFloat(value).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

export function badgeHTML(status) {
    const map = {
        LOW:    ['badge-low',    '● Low Stock'],
        MEDIUM: ['badge-medium', '● Medium'],
        NORMAL: ['badge-normal', '● Normal'],
    };
    const [cls, label] = map[status] || ['badge-normal', status];
    return `<span class="badge ${cls}">${label}</span>`;
}

export function showToast(message, type = 'info') {
    const container = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.textContent = message;
    container.appendChild(toast);
    setTimeout(() => toast.remove(), 3500);
}

export function setActivePage() {
    const path = window.location.pathname.split('/').pop() || 'index.html';
    document.querySelectorAll('.navbar-links a').forEach(a => {
        a.classList.toggle('active', a.getAttribute('href') === path);
    });
}

export function formatDate(dateStr) {
    if (!dateStr) return '-';
    return new Date(dateStr).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
}

export function debounce(fn, delay = 350) {
    let t;
    return (...args) => { clearTimeout(t); t = setTimeout(() => fn(...args), delay); };
}
