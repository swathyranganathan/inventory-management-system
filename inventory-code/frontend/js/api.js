// ============================================================
// api.js — Central API Client
// All HTTP calls go through here. Change BASE_URL once for all pages.
// ============================================================

const BASE_URL = 'http://localhost:8080/api';

// ── Generic fetch wrapper ────────────────────────────────────
async function apiFetch(path, options = {}) {
    const url = `${BASE_URL}${path}`;
    const defaults = {
        headers: { 'Content-Type': 'application/json' }
    };
    const config = { ...defaults, ...options };
    if (options.body && typeof options.body === 'object') {
        config.body = JSON.stringify(options.body);
    }
    const res = await fetch(url, config);
    if (!res.ok) {
        const err = await res.json().catch(() => ({ message: res.statusText }));
        throw { status: res.status, message: err.message || 'Request failed', fieldErrors: err.fieldErrors };
    }
    if (res.status === 204) return null;
    return res.json();
}

// ── Products ─────────────────────────────────────────────────
export const ProductAPI = {
    getAll: (page = 0, size = 20, sort = 'name,asc') =>
        apiFetch(`/products?page=${page}&size=${size}&sort=${sort}`),

    getById: (id) =>
        apiFetch(`/products/${id}`),

    create: (data) =>
        apiFetch('/products', { method: 'POST', body: data }),

    update: (id, data) =>
        apiFetch(`/products/${id}`, { method: 'PUT', body: data }),

    updateQuantity: (id, quantity) =>
        apiFetch(`/products/${id}/quantity`, { method: 'PATCH', body: { quantity } }),

    delete: (id) =>
        apiFetch(`/products/${id}`, { method: 'DELETE' }),

    getLowStock: () =>
        apiFetch('/products/low-stock'),

    getByCategory: (category) =>
        apiFetch(`/products/category/${encodeURIComponent(category)}`),

    search: (keyword) =>
        apiFetch(`/products/search?q=${encodeURIComponent(keyword)}`),

    getCategories: () =>
        apiFetch('/products/categories'),
};

// ── Analytics ────────────────────────────────────────────────
export const AnalyticsAPI = {
    getSummary: () =>
        apiFetch('/analytics/summary'),

    getCategoryBreakdown: () =>
        apiFetch('/analytics/categories'),

    getStockDistribution: () =>
        apiFetch('/analytics/stock-distribution'),

    getTopValued: (limit = 10) =>
        apiFetch(`/analytics/top-valued?limit=${limit}`),

    getRecent: (days = 7) =>
        apiFetch(`/analytics/recent?days=${days}`),
};
