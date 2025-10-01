// Global variables
let cart = JSON.parse(localStorage.getItem('cart')) || [];
let promoCode = '';
let discountAmount = 0;

// DOM elements
const cartCountElement = document.querySelector('.cart-count');
const hamburger = document.querySelector('.hamburger');
const navMenu = document.querySelector('.nav-menu');

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    updateCartCount();
    initializeEventListeners();
});

// Event listeners
function initializeEventListeners() {
    // Mobile menu toggle
    if (hamburger && navMenu) {
        hamburger.addEventListener('click', function() {
            hamburger.classList.toggle('active');
            navMenu.classList.toggle('active');
        });
    }

    // Smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });

    // Add to cart buttons
    document.querySelectorAll('.book-btn').forEach(button => {
        button.addEventListener('click', function() {
            const movieTitle = this.closest('.movie-card').querySelector('h3').textContent;
            const price = parseInt(this.textContent.match(/\d+/)[0]) * 1000;
            bookMovie(movieTitle, price);
        });
    });
}

// Cart functions
function bookMovie(title, price) {
    const existingItem = cart.find(item => item.title === title);
    
    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.push({
            id: Date.now(),
            title: title,
            price: price,
            quantity: 1,
            theater: 'CinemaHub Vincom Đồng Khởi',
            date: getCurrentDate(),
            time: '19:30',
            room: 'Phòng 1'
        });
    }
    
    saveCart();
    updateCartCount();
    showNotification(`Đã thêm "${title}" vào giỏ hàng!`, 'success');
}

function removeFromCart(itemId) {
    cart = cart.filter(item => item.id !== itemId);
    saveCart();
    updateCartCount();
    displayCartItems();
    showNotification('Đã xóa sản phẩm khỏi giỏ hàng!', 'info');
}

function updateQuantity(itemId, newQuantity) {
    if (newQuantity <= 0) {
        removeFromCart(itemId);
        return;
    }
    
    const item = cart.find(item => item.id === itemId);
    if (item) {
        item.quantity = newQuantity;
        saveCart();
        updateCartCount();
        displayCartItems();
    }
}

function saveCart() {
    localStorage.setItem('cart', JSON.stringify(cart));
}

function updateCartCount() {
    const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
    if (cartCountElement) {
        cartCountElement.textContent = totalItems;
    }
}

function getCartTotal() {
    return cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
}

function getCartSubtotal() {
    return getCartTotal() - discountAmount;
}

// Display cart items
function displayCartItems() {
    const cartList = document.getElementById('cart-list');
    const emptyCart = document.getElementById('empty-cart');
    const cartSummary = document.getElementById('cart-summary');
    
    if (!cartList) return;
    
    if (cart.length === 0) {
        emptyCart.style.display = 'block';
        cartList.style.display = 'none';
        cartSummary.style.display = 'none';
        return;
    }
    
    emptyCart.style.display = 'none';
    cartList.style.display = 'block';
    cartSummary.style.display = 'block';
    
    cartList.innerHTML = cart.map(item => `
        <div class="cart-item">
            <div class="item-image">🎬</div>
            <div class="item-details">
                <div class="item-title">${item.title}</div>
                <div class="item-info">${item.theater}</div>
                <div class="item-info">${item.date} - ${item.time}</div>
                <div class="item-info">${item.room}</div>
                <div class="quantity-controls">
                    <button class="quantity-btn" onclick="updateQuantity(${item.id}, ${item.quantity - 1})">-</button>
                    <input type="number" class="quantity" value="${item.quantity}" min="1" 
                           onchange="updateQuantity(${item.id}, parseInt(this.value))" readonly>
                    <button class="quantity-btn" onclick="updateQuantity(${item.id}, ${item.quantity + 1})">+</button>
                </div>
                <div class="item-price">${formatCurrency(item.price)}</div>
            </div>
            <button class="remove-btn" onclick="removeFromCart(${item.id})">
                <i class="fas fa-trash"></i> Xóa
            </button>
        </div>
    `).join('');
    
    updateCartSummary();
}

function updateCartSummary() {
    const totalTickets = cart.reduce((sum, item) => sum + item.quantity, 0);
    const subtotal = getCartTotal();
    const total = subtotal - discountAmount;
    
    document.getElementById('total-tickets').textContent = totalTickets;
    document.getElementById('subtotal').textContent = formatCurrency(subtotal);
    document.getElementById('total-amount').textContent = formatCurrency(total);
    
    if (discountAmount > 0) {
        document.getElementById('discount-row').style.display = 'flex';
        document.getElementById('discount-amount').textContent = formatCurrency(discountAmount);
    } else {
        document.getElementById('discount-row').style.display = 'none';
    }
}

// Promo code functions
function applyPromo() {
    const promoInput = document.getElementById('promo-code');
    const promoMessage = document.getElementById('promo-message');
    const code = promoInput.value.toUpperCase();
    
    const promoCodes = {
        'WEEKEND20': { discount: 0.2, description: 'Giảm 20% vé cuối tuần' },
        'FAMILY4': { discount: 0.15, description: 'Giảm 15% combo gia đình' },
        'HAPPY30': { discount: 0.3, description: 'Giảm 30% Happy Hour' },
        'NEWUSER': { discount: 0.25, description: 'Giảm 25% cho khách hàng mới' }
    };
    
    if (promoCodes[code]) {
        const discount = promoCodes[code];
        discountAmount = Math.floor(getCartTotal() * discount.discount);
        promoCode = code;
        
        promoMessage.textContent = `✓ Áp dụng thành công: ${discount.description}`;
        promoMessage.className = 'promo-message success';
        
        updateCartSummary();
        showNotification('Mã khuyến mãi đã được áp dụng!', 'success');
    } else {
        promoMessage.textContent = '✗ Mã khuyến mãi không hợp lệ';
        promoMessage.className = 'promo-message error';
        showNotification('Mã khuyến mãi không hợp lệ!', 'error');
    }
}

// Checkout functions
function proceedToCheckout() {
    if (cart.length === 0) {
        showNotification('Giỏ hàng của bạn đang trống!', 'error');
        return;
    }
    
    // Store checkout data
    const checkoutData = {
        items: cart,
        promoCode: promoCode,
        discountAmount: discountAmount,
        subtotal: getCartTotal(),
        total: getCartTotal() - discountAmount
    };
    
    localStorage.setItem('checkoutData', JSON.stringify(checkoutData));
    window.location.href = 'checkout.html';
}

// Utility functions
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}

function getCurrentDate() {
    const today = new Date();
    return today.toLocaleDateString('vi-VN');
}

function showNotification(message, type = 'info') {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.innerHTML = `
        <div class="notification-content">
            <i class="fas ${getNotificationIcon(type)}"></i>
            <span>${message}</span>
        </div>
    `;
    
    // Add styles if not already added
    if (!document.querySelector('#notification-styles')) {
        const styles = document.createElement('style');
        styles.id = 'notification-styles';
        styles.textContent = `
            .notification {
                position: fixed;
                top: 100px;
                right: 20px;
                background: white;
                padding: 1rem 1.5rem;
                border-radius: 8px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                z-index: 1001;
                animation: slideInRight 0.3s ease;
                border-left: 4px solid #667eea;
                max-width: 350px;
            }
            .notification.success {
                border-left-color: #27ae60;
            }
            .notification.error {
                border-left-color: #e74c3c;
            }
            .notification.info {
                border-left-color: #3498db;
            }
            .notification-content {
                display: flex;
                align-items: center;
                gap: 0.5rem;
            }
            .notification-content i {
                font-size: 1.2rem;
            }
            .notification.success i {
                color: #27ae60;
            }
            .notification.error i {
                color: #e74c3c;
            }
            .notification.info i {
                color: #3498db;
            }
            @keyframes slideInRight {
                from {
                    transform: translateX(100%);
                    opacity: 0;
                }
                to {
                    transform: translateX(0);
                    opacity: 1;
                }
            }
        `;
        document.head.appendChild(styles);
    }
    
    // Add to page
    document.body.appendChild(notification);
    
    // Remove after 3 seconds
    setTimeout(() => {
        notification.style.animation = 'slideInRight 0.3s ease reverse';
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }, 3000);
}

function getNotificationIcon(type) {
    switch (type) {
        case 'success': return 'fa-check-circle';
        case 'error': return 'fa-exclamation-circle';
        case 'info': return 'fa-info-circle';
        default: return 'fa-info-circle';
    }
}

// Initialize cart display if on cart page
if (window.location.pathname.includes('cart.html')) {
    document.addEventListener('DOMContentLoaded', displayCartItems);
}

// Smooth scrolling for all internal links
document.addEventListener('DOMContentLoaded', function() {
    // Add loading animation to buttons
    document.querySelectorAll('button').forEach(button => {
        button.addEventListener('click', function() {
            if (!this.classList.contains('no-loading')) {
                this.style.position = 'relative';
                this.classList.add('loading');
                
                setTimeout(() => {
                    this.classList.remove('loading');
                }, 1000);
            }
        });
    });
});

// Export functions for global use
window.bookMovie = bookMovie;
window.removeFromCart = removeFromCart;
window.updateQuantity = updateQuantity;
window.applyPromo = applyPromo;
window.proceedToCheckout = proceedToCheckout;