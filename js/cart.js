// Cart JavaScript

// Initialize cart page
document.addEventListener('DOMContentLoaded', function() {
    displayCartItems();
    initializeCartEventListeners();
});

// Event listeners specific to cart page
function initializeCartEventListeners() {
    // Promo code input enter key
    const promoInput = document.getElementById('promo-code');
    if (promoInput) {
        promoInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                applyPromo();
            }
        });
    }
}

// Display cart items (enhanced version for cart page)
function displayCartItems() {
    const cartList = document.getElementById('cart-list');
    const emptyCart = document.getElementById('empty-cart');
    const cartSummary = document.getElementById('cart-summary');
    
    if (!cartList) return;
    
    // Get cart from localStorage
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    
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
        <div class="cart-item" data-item-id="${item.id}">
            <div class="item-image">🎬</div>
            <div class="item-details">
                <div class="item-title">${item.title}</div>
                <div class="item-info">
                    <i class="fas fa-map-marker-alt"></i> ${item.theater}
                </div>
                <div class="item-info">
                    <i class="fas fa-calendar"></i> ${item.date} - ${item.time}
                </div>
                <div class="item-info">
                    <i class="fas fa-door-open"></i> ${item.room}
                </div>
                <div class="quantity-controls">
                    <button class="quantity-btn" onclick="updateQuantity(${item.id}, ${item.quantity - 1})">
                        <i class="fas fa-minus"></i>
                    </button>
                    <input type="number" class="quantity" value="${item.quantity}" min="1" max="10"
                           onchange="updateQuantity(${item.id}, parseInt(this.value) || 1)" readonly>
                    <button class="quantity-btn" onclick="updateQuantity(${item.id}, ${item.quantity + 1})">
                        <i class="fas fa-plus"></i>
                    </button>
                </div>
                <div class="item-price">${formatCurrency(item.price * item.quantity)}</div>
                <div class="item-unit-price">Đơn giá: ${formatCurrency(item.price)}</div>
            </div>
            <button class="remove-btn" onclick="removeFromCart(${item.id})" title="Xóa khỏi giỏ hàng">
                <i class="fas fa-trash"></i>
            </button>
        </div>
    `).join('');
    
    updateCartSummary();
    addCartItemAnimations();
}

// Add animations to cart items
function addCartItemAnimations() {
    const cartItems = document.querySelectorAll('.cart-item');
    cartItems.forEach((item, index) => {
        item.style.opacity = '0';
        item.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            item.style.transition = 'all 0.3s ease';
            item.style.opacity = '1';
            item.style.transform = 'translateY(0)';
        }, index * 100);
    });
}

// Enhanced update quantity function
function updateQuantity(itemId, newQuantity) {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    
    // Validate quantity
    if (newQuantity <= 0) {
        removeFromCart(itemId);
        return;
    }
    
    if (newQuantity > 10) {
        showNotification('Số lượng vé tối đa là 10!', 'error');
        return;
    }
    
    const item = cart.find(item => item.id === itemId);
    if (item) {
        const oldQuantity = item.quantity;
        item.quantity = newQuantity;
        
        // Save updated cart
        localStorage.setItem('cart', JSON.stringify(cart));
        
        // Update UI with animation
        const cartItem = document.querySelector(`[data-item-id="${itemId}"]`);
        if (cartItem) {
            // Animate quantity change
            const quantityInput = cartItem.querySelector('.quantity');
            const priceElement = cartItem.querySelector('.item-price');
            
            quantityInput.value = newQuantity;
            priceElement.textContent = formatCurrency(item.price * newQuantity);
            
            // Flash animation for price
            priceElement.style.background = '#fff3cd';
            setTimeout(() => {
                priceElement.style.background = 'transparent';
            }, 500);
        }
        
        updateCartCount();
        updateCartSummary();
        
        if (newQuantity > oldQuantity) {
            showNotification(`Đã tăng số lượng vé "${item.title}"!`, 'success');
        } else {
            showNotification(`Đã giảm số lượng vé "${item.title}"!`, 'info');
        }
    }
}

// Enhanced remove from cart function
function removeFromCart(itemId) {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    const item = cart.find(item => item.id === itemId);
    
    if (!item) return;
    
    // Show confirmation
    if (confirm(`Bạn có chắc muốn xóa vé "${item.title}" khỏi giỏ hàng?`)) {
        // Remove with animation
        const cartItem = document.querySelector(`[data-item-id="${itemId}"]`);
        if (cartItem) {
            cartItem.style.transform = 'translateX(-100%)';
            cartItem.style.opacity = '0';
            
            setTimeout(() => {
                cart = cart.filter(cartItem => cartItem.id !== itemId);
                localStorage.setItem('cart', JSON.stringify(cart));
                
                updateCartCount();
                displayCartItems();
                showNotification(`Đã xóa vé "${item.title}" khỏi giỏ hàng!`, 'info');
            }, 300);
        }
    }
}

// Enhanced cart summary update
function updateCartSummary() {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const totalTickets = cart.reduce((sum, item) => sum + item.quantity, 0);
    const subtotal = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    
    // Get discount info
    const discountAmount = parseInt(localStorage.getItem('discountAmount')) || 0;
    const total = subtotal - discountAmount;
    
    // Update summary elements
    const elements = {
        'total-tickets': totalTickets,
        'subtotal': formatCurrency(subtotal),
        'total-amount': formatCurrency(total)
    };
    
    Object.entries(elements).forEach(([id, value]) => {
        const element = document.getElementById(id);
        if (element) {
            element.textContent = value;
        }
    });
    
    // Show/hide discount row
    const discountRow = document.getElementById('discount-row');
    const discountAmountElement = document.getElementById('discount-amount');
    
    if (discountAmount > 0 && discountRow && discountAmountElement) {
        discountRow.style.display = 'flex';
        discountAmountElement.textContent = formatCurrency(discountAmount);
    } else if (discountRow) {
        discountRow.style.display = 'none';
    }
    
    // Update checkout button state
    const checkoutBtn = document.querySelector('.checkout-btn');
    if (checkoutBtn) {
        if (cart.length === 0) {
            checkoutBtn.disabled = true;
            checkoutBtn.textContent = 'Giỏ hàng trống';
            checkoutBtn.style.opacity = '0.6';
        } else {
            checkoutBtn.disabled = false;
            checkoutBtn.innerHTML = '<i class="fas fa-credit-card"></i> Tiến hành thanh toán';
            checkoutBtn.style.opacity = '1';
        }
    }
}

// Enhanced promo code application
function applyPromo() {
    const promoInput = document.getElementById('promo-code');
    const promoMessage = document.getElementById('promo-message');
    
    if (!promoInput || !promoMessage) return;
    
    const code = promoInput.value.trim().toUpperCase();
    
    if (!code) {
        showNotification('Vui lòng nhập mã khuyến mãi!', 'error');
        return;
    }
    
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const subtotal = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    
    if (subtotal === 0) {
        showNotification('Giỏ hàng trống, không thể áp dụng mã khuyến mãi!', 'error');
        return;
    }
    
    const promoCodes = {
        'WEEKEND20': { 
            discount: 0.2, 
            description: 'Giảm 20% vé cuối tuần',
            minAmount: 100000,
            maxDiscount: 100000
        },
        'FAMILY4': { 
            discount: 0.15, 
            description: 'Giảm 15% combo gia đình',
            minAmount: 200000,
            maxDiscount: 150000
        },
        'HAPPY30': { 
            discount: 0.3, 
            description: 'Giảm 30% Happy Hour',
            minAmount: 150000,
            maxDiscount: 200000
        },
        'NEWUSER': { 
            discount: 0.25, 
            description: 'Giảm 25% cho khách hàng mới',
            minAmount: 120000,
            maxDiscount: 120000
        },
        'STUDENT10': { 
            discount: 0.1, 
            description: 'Giảm 10% cho sinh viên',
            minAmount: 50000,
            maxDiscount: 50000
        }
    };
    
    if (promoCodes[code]) {
        const promo = promoCodes[code];
        
        // Check minimum amount
        if (subtotal < promo.minAmount) {
            promoMessage.textContent = `✗ Đơn hàng tối thiểu ${formatCurrency(promo.minAmount)} để sử dụng mã này`;
            promoMessage.className = 'promo-message error';
            showNotification(`Đơn hàng tối thiểu ${formatCurrency(promo.minAmount)}!`, 'error');
            return;
        }
        
        // Calculate discount
        let discountAmount = Math.floor(subtotal * promo.discount);
        if (discountAmount > promo.maxDiscount) {
            discountAmount = promo.maxDiscount;
        }
        
        // Save discount info
        localStorage.setItem('discountAmount', discountAmount.toString());
        localStorage.setItem('promoCode', code);
        
        // Update UI
        promoMessage.textContent = `✓ ${promo.description} - Giảm ${formatCurrency(discountAmount)}`;
        promoMessage.className = 'promo-message success';
        
        // Disable input and button after successful application
        promoInput.disabled = true;
        const applyBtn = promoInput.nextElementSibling;
        if (applyBtn) {
            applyBtn.textContent = 'Đã áp dụng';
            applyBtn.disabled = true;
        }
        
        updateCartSummary();
        showNotification(`Đã áp dụng mã ${code}! Giảm ${formatCurrency(discountAmount)}`, 'success');
        
        // Add remove promo button
        const removePromoBtn = document.createElement('button');
        removePromoBtn.textContent = 'Bỏ mã';
        removePromoBtn.className = 'remove-promo-btn';
        removePromoBtn.onclick = removePromo;
        removePromoBtn.style.cssText = `
            margin-left: 0.5rem;
            padding: 0.25rem 0.5rem;
            background: #e74c3c;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 0.8rem;
        `;
        promoMessage.appendChild(removePromoBtn);
        
    } else {
        promoMessage.textContent = '✗ Mã khuyến mãi không hợp lệ hoặc đã hết hạn';
        promoMessage.className = 'promo-message error';
        showNotification('Mã khuyến mãi không hợp lệ!', 'error');
        
        // Shake animation for input
        promoInput.style.animation = 'shake 0.5s ease-in-out';
        setTimeout(() => {
            promoInput.style.animation = '';
        }, 500);
    }
}

// Remove promo code
function removePromo() {
    // Clear discount info
    localStorage.removeItem('discountAmount');
    localStorage.removeItem('promoCode');
    
    // Reset UI
    const promoInput = document.getElementById('promo-code');
    const promoMessage = document.getElementById('promo-message');
    const applyBtn = promoInput?.nextElementSibling;
    
    if (promoInput) {
        promoInput.disabled = false;
        promoInput.value = '';
    }
    
    if (applyBtn) {
        applyBtn.textContent = 'Áp dụng';
        applyBtn.disabled = false;
    }
    
    if (promoMessage) {
        promoMessage.textContent = '';
        promoMessage.className = 'promo-message';
    }
    
    updateCartSummary();
    showNotification('Đã bỏ mã khuyến mãi!', 'info');
}

// Enhanced proceed to checkout
function proceedToCheckout() {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    
    if (cart.length === 0) {
        showNotification('Giỏ hàng của bạn đang trống!', 'error');
        return;
    }
    
    // Check if user is logged in
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    if (!currentUser) {
        if (confirm('Bạn cần đăng nhập để tiếp tục. Chuyển đến trang đăng nhập?')) {
            window.location.href = 'login.html';
            return;
        }
    }
    
    // Calculate totals
    const subtotal = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const discountAmount = parseInt(localStorage.getItem('discountAmount')) || 0;
    const total = subtotal - discountAmount;
    
    // Store checkout data
    const checkoutData = {
        items: cart,
        promoCode: localStorage.getItem('promoCode') || '',
        discountAmount: discountAmount,
        subtotal: subtotal,
        total: total,
        timestamp: new Date().toISOString()
    };
    
    localStorage.setItem('checkoutData', JSON.stringify(checkoutData));
    
    // Show loading and redirect
    const checkoutBtn = document.querySelector('.checkout-btn');
    if (checkoutBtn) {
        checkoutBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';
        checkoutBtn.disabled = true;
    }
    
    setTimeout(() => {
        window.location.href = 'checkout.html';
    }, 1000);
}

// Add shake animation CSS
const shakeStyles = document.createElement('style');
shakeStyles.textContent = `
    @keyframes shake {
        0%, 100% { transform: translateX(0); }
        25% { transform: translateX(-5px); }
        75% { transform: translateX(5px); }
    }
    
    .cart-item {
        transition: all 0.3s ease;
    }
    
    .quantity-controls {
        gap: 0.5rem;
        align-items: center;
        margin: 0.5rem 0;
    }
    
    .quantity-btn {
        width: 32px;
        height: 32px;
        border: 1px solid #ddd;
        background: white;
        border-radius: 6px;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s ease;
        font-size: 0.8rem;
    }
    
    .quantity-btn:hover {
        background: #f0f0f0;
        border-color: #667eea;
        color: #667eea;
    }
    
    .quantity-btn:active {
        transform: scale(0.95);
    }
    
    .item-unit-price {
        font-size: 0.9rem;
        color: #888;
        margin-top: 0.25rem;
    }
    
    .remove-promo-btn:hover {
        background: #c0392b !important;
    }
`;

document.head.appendChild(shakeStyles);

// Export functions for global use
window.updateQuantity = updateQuantity;
window.removeFromCart = removeFromCart;
window.applyPromo = applyPromo;
window.removePromo = removePromo;
window.proceedToCheckout = proceedToCheckout;