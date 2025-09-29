// API Base URL
const API_BASE = '/api';

// Global variables
let currentBooks = [];
let cartItems = [];

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    loadBooks();
    loadCartItems();
    updateCartCount();
});

// Page navigation
function showPage(pageId) {
    // Hide all pages
    document.querySelectorAll('.page').forEach(page => {
        page.classList.remove('active');
    });

    // Show selected page
    document.getElementById(pageId + '-page').classList.add('active');

    // Load data for specific pages
    if (pageId === 'cart') {
        loadCartItems();
    }
}

// Book Management
async function loadBooks() {
    try {
        showLoading('books-grid');
        const response = await fetch(`${API_BASE}/books`);
        const books = await response.json();
        currentBooks = books;
        displayBooks(books);
    } catch (error) {
        showError('Failed to load books');
        console.error('Error loading books:', error);
    }
}

function displayBooks(books) {
    const booksGrid = document.getElementById('books-grid');

    if (books.length === 0) {
        booksGrid.innerHTML = `
            <div class="empty-state">
                <h3>No books found</h3>
                <p>Try adjusting your search criteria.</p>
            </div>
        `;
        return;
    }

    booksGrid.innerHTML = books.map(book => `
        <div class="book-card">
            <div class="book-title">${book.title}</div>
            <div class="book-author">by ${book.author}</div>
            <div class="book-price">$${book.price.toFixed(2)}</div>
            <div class="book-description">${book.description || 'No description available'}</div>
            <div class="book-stock">Stock: ${book.stockQuantity} available</div>
            <button
                class="btn-primary"
                onclick="addToCart(${book.id})"
                ${book.stockQuantity === 0 ? 'disabled' : ''}
            >
                ${book.stockQuantity === 0 ? 'Out of Stock' : 'Add to Cart'}
            </button>
        </div>
    `).join('');
}

async function searchBooks() {
    const query = document.getElementById('search-bar').value.trim();
    try {
        showLoading('books-grid');
        const url = query ? `${API_BASE}/books/search?query=${encodeURIComponent(query)}` : `${API_BASE}/books`;
        const response = await fetch(url);
        const books = await response.json();
        displayBooks(books);
    } catch (error) {
        showError('Failed to search books');
        console.error('Error searching books:', error);
    }
}

// Cart Management
async function addToCart(bookId) {
    try {
        const response = await fetch(`${API_BASE}/cart`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                bookId: bookId,
                quantity: 1
            })
        });

        if (response.ok) {
            showSuccess('Book added to cart');
            updateCartCount();
        } else {
            showError('Failed to add book to cart');
        }
    } catch (error) {
        showError('Failed to add book to cart');
        console.error('Error adding to cart:', error);
    }
}

async function loadCartItems() {
    try {
        const response = await fetch(`${API_BASE}/cart`);
        cartItems = await response.json();
        displayCartItems();
        updateCartTotal();
        updateCartCount();
    } catch (error) {
        showError('Failed to load cart items');
        console.error('Error loading cart:', error);
    }
}

async function displayCartItems() {
    const cartItemsContainer = document.getElementById('cart-items');

    if (cartItems.length === 0) {
        cartItemsContainer.innerHTML = `
            <div class="empty-state">
                <h3>Your cart is empty</h3>
                <p>Add some books to get started!</p>
            </div>
        `;
        return;
    }

    // Get book details for each cart item
    const cartWithBooks = await Promise.all(cartItems.map(async (item) => {
        try {
            const response = await fetch(`${API_BASE}/books/${item.bookId}`);
            const book = await response.json();
            return { ...item, book };
        } catch (error) {
            console.error('Error loading book details:', error);
            return { ...item, book: null };
        }
    }));

    cartItemsContainer.innerHTML = cartWithBooks.map(item => {
        if (!item.book) return '';

        return `
            <div class="cart-item">
                <div class="cart-item-info">
                    <div class="cart-item-title">${item.book.title}</div>
                    <div class="cart-item-author">by ${item.book.author}</div>
                    <div class="cart-item-price">$${item.book.price.toFixed(2)} each</div>
                </div>
                <div class="cart-item-controls">
                    <input
                        type="number"
                        class="quantity-input"
                        value="${item.quantity}"
                        min="1"
                        onchange="updateCartItemQuantity(${item.id}, this.value)"
                    >
                    <button class="btn-danger" onclick="removeCartItem(${item.id})">Remove</button>
                </div>
            </div>
        `;
    }).join('');
}

async function updateCartItemQuantity(itemId, quantity) {
    try {
        const response = await fetch(`${API_BASE}/cart/${itemId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                quantity: parseInt(quantity)
            })
        });

        if (response.ok) {
            loadCartItems();
        } else {
            showError('Failed to update quantity');
        }
    } catch (error) {
        showError('Failed to update quantity');
        console.error('Error updating quantity:', error);
    }
}

async function removeCartItem(itemId) {
    try {
        const response = await fetch(`${API_BASE}/cart/${itemId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            loadCartItems();
            showSuccess('Item removed from cart');
        } else {
            showError('Failed to remove item');
        }
    } catch (error) {
        showError('Failed to remove item');
        console.error('Error removing item:', error);
    }
}

async function updateCartTotal() {
    try {
        const response = await fetch(`${API_BASE}/cart/total`);
        const total = await response.json();
        document.getElementById('cart-total').textContent = total.toFixed(2);
    } catch (error) {
        console.error('Error updating cart total:', error);
    }
}

function updateCartCount() {
    const count = cartItems.reduce((total, item) => total + item.quantity, 0);
    document.getElementById('cart-count').textContent = count;
}

// Checkout
function showCheckout() {
    if (cartItems.length === 0) {
        showError('Your cart is empty');
        return;
    }
    showPage('checkout');
}

async function placeOrder(event) {
    event.preventDefault();

    const customerName = document.getElementById('customer-name').value;
    const customerEmail = document.getElementById('customer-email').value;
    const customerAddress = document.getElementById('customer-address').value;

    try {
        const response = await fetch(`${API_BASE}/orders`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                customerName,
                customerEmail,
                customerAddress
            })
        });

        if (response.ok) {
            const order = await response.json();
            showSuccess(`Order placed successfully! Order ID: ${order.id}`);
            document.getElementById('checkout-form').reset();
            cartItems = [];
            updateCartCount();
            showPage('home');
        } else {
            showError('Failed to place order');
        }
    } catch (error) {
        showError('Failed to place order');
        console.error('Error placing order:', error);
    }
}

// Admin Functions
async function addBook(event) {
    event.preventDefault();

    const book = {
        title: document.getElementById('book-title').value,
        author: document.getElementById('book-author').value,
        isbn: document.getElementById('book-isbn').value,
        price: parseFloat(document.getElementById('book-price').value),
        description: document.getElementById('book-description').value,
        stockQuantity: parseInt(document.getElementById('book-stock').value)
    };

    try {
        const response = await fetch(`${API_BASE}/books`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(book)
        });

        if (response.ok) {
            showSuccess('Book added successfully');
            document.getElementById('add-book-form').reset();
            loadBooks();
        } else {
            showError('Failed to add book');
        }
    } catch (error) {
        showError('Failed to add book');
        console.error('Error adding book:', error);
    }
}

// Utility Functions
function showLoading(elementId) {
    document.getElementById(elementId).innerHTML = `
        <div class="loading">
            <p>Loading...</p>
        </div>
    `;
}

function showMessage(message, type) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type}`;
    messageDiv.textContent = message;

    document.body.appendChild(messageDiv);

    setTimeout(() => {
        messageDiv.remove();
    }, 3000);
}

function showSuccess(message) {
    showMessage(message, 'success');
}

function showError(message) {
    showMessage(message, 'error');
}

// Event Listeners
document.getElementById('search-input').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        searchBooks();
    }
});
