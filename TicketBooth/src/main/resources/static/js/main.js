// Global variables
let cart = JSON.parse(localStorage.getItem('cart')) || [];
let promoCode = '';
let discountAmount = 0;
let currentSlideIndex = 0;
let slideInterval;

// DOM elements
const cartCountElement = document.querySelector('.cart-count');
const hamburger = document.querySelector('.hamburger');
const navMenu = document.querySelector('.nav-menu');

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    updateCartCount();
    initializeEventListeners();
    initializeSlider();
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

// Slider functionality
function initializeSlider() {
    const slides = document.querySelectorAll('.slide');
    if (slides.length > 0) {
        startSlideShow();
    }
}

function startSlideShow() {
    slideInterval = setInterval(nextSlide, 5000); // Auto slide every 5 seconds
}

function nextSlide() {
    const slides = document.querySelectorAll('.slide');
    const dots = document.querySelectorAll('.dot');
    
    if (slides.length === 0) return;
    
    slides[currentSlideIndex].classList.remove('active');
    dots[currentSlideIndex].classList.remove('active');
    
    currentSlideIndex = (currentSlideIndex + 1) % slides.length;
    
    slides[currentSlideIndex].classList.add('active');
    dots[currentSlideIndex].classList.add('active');
}

function previousSlide() {
    const slides = document.querySelectorAll('.slide');
    const dots = document.querySelectorAll('.dot');
    
    if (slides.length === 0) return;
    
    clearInterval(slideInterval);
    
    slides[currentSlideIndex].classList.remove('active');
    dots[currentSlideIndex].classList.remove('active');
    
    currentSlideIndex = (currentSlideIndex - 1 + slides.length) % slides.length;
    
    slides[currentSlideIndex].classList.add('active');
    dots[currentSlideIndex].classList.add('active');
    
    startSlideShow();
}

function currentSlide(index) {
    const slides = document.querySelectorAll('.slide');
    const dots = document.querySelectorAll('.dot');
    
    if (slides.length === 0) return;
    
    clearInterval(slideInterval);
    
    slides[currentSlideIndex].classList.remove('active');
    dots[currentSlideIndex].classList.remove('active');
    
    currentSlideIndex = index - 1;
    
    slides[currentSlideIndex].classList.add('active');
    dots[currentSlideIndex].classList.add('active');
    
    startSlideShow();
}

// Movie categories functionality
function showMovieCategory(categoryId) {
    // Hide all categories
    document.querySelectorAll('.movie-category').forEach(category => {
        category.classList.remove('active');
    });
    
    // Remove active class from all tabs
    document.querySelectorAll('.tab-btn').forEach(tab => {
        tab.classList.remove('active');
    });
    
    // Show selected category
    const selectedCategory = document.getElementById(categoryId);
    if (selectedCategory) {
        selectedCategory.classList.add('active');
    }
    
    // Add active class to clicked tab
    event.target.classList.add('active');
}

// Movie interaction functions
function showTrailer(trailerUrl) {
    if (!trailerUrl) {
        alert('Trailer không có sẵn');
        return;
    }
    
    // Create modal for trailer
    const modal = document.createElement('div');
    modal.className = 'trailer-modal';
    modal.innerHTML = `
        <div class="trailer-modal-content">
            <span class="trailer-close" onclick="closeTrailer()">&times;</span>
            <div class="trailer-container">
                <iframe src="${trailerUrl}" 
                        frameborder="0" 
                        allowfullscreen
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture">
                </iframe>
            </div>
        </div>
    `;
    
    // Add styles
    const style = document.createElement('style');
    style.textContent = `
        .trailer-modal {
            display: block;
            position: fixed;
            z-index: 9999;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.8);
        }
        .trailer-modal-content {
            position: relative;
            margin: 5% auto;
            width: 80%;
            max-width: 800px;
        }
        .trailer-close {
            color: white;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
            position: absolute;
            right: -40px;
            top: -40px;
        }
        .trailer-container {
            position: relative;
            width: 100%;
            height: 0;
            padding-bottom: 56.25%; /* 16:9 aspect ratio */
        }
        .trailer-container iframe {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
        }
    `;
    
    document.head.appendChild(style);
    document.body.appendChild(modal);
    
    // Close on outside click
    modal.addEventListener('click', function(e) {
        if (e.target === modal) {
            closeTrailer();
        }
    });
}

function closeTrailer() {
    const modal = document.querySelector('.trailer-modal');
    if (modal) {
        modal.remove();
    }
}

function notifyMovie(movieId) {
    // Implement notification functionality
    console.log('Set notification for movie ID:', movieId);
    // You can implement notification signup here
    alert('Bạn sẽ được thông báo khi phim ra mắt!');
}

function viewMovieDetail(movieId) {
    // Encode the movie title to handle special characters in URL
    window.location.href = `/showtime?movieId=${movieId}`;
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

// Search functionality
function toggleSearch() {
    const searchContainer = document.getElementById('search-container');
    const searchInput = document.getElementById('search-input');
    
    if (searchContainer.classList.contains('active')) {
        searchContainer.classList.remove('active');
    } else {
        searchContainer.classList.add('active');
        setTimeout(() => {
            searchInput.focus();
        }, 300);
    }
}

function performSearch() {
    const searchInput = document.getElementById('search-input');
    const query = searchInput.value.trim();
    
    if (query === '') {
        alert('Vui lòng nhập từ khóa tìm kiếm');
        return;
    }
    
    // Implement search logic here
    console.log('Searching for:', query);
    
    // For now, we'll show an alert
    alert(`Tìm kiếm: "${query}"\nTính năng này sẽ được phát triển trong phiên bản tiếp theo.`);
    
    // Clear search and close
    searchInput.value = '';
    toggleSearch();
}

// Handle Enter key in search input
document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('search-input');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                performSearch();
            }
        });
    }
    
    // Close search when clicking outside
    document.addEventListener('click', function(e) {
        const searchContainer = document.getElementById('search-container');
        const searchBtn = document.querySelector('.search-btn');
        
        if (searchContainer && searchContainer.classList.contains('active')) {
            if (!searchContainer.contains(e.target) && !searchBtn.contains(e.target)) {
                searchContainer.classList.remove('active');
            }
        }
    });
});

// Export functions for global use
window.bookMovie = bookMovie;
window.removeFromCart = removeFromCart;
window.updateQuantity = updateQuantity;
window.applyPromo = applyPromo;
window.proceedToCheckout = proceedToCheckout;
window.toggleSearch = toggleSearch;
window.performSearch = performSearch;
window.viewMovieDetail = viewMovieDetail;