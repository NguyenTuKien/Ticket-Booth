// Checkout JavaScript

// Global variables
let selectedSeats = [];
let checkoutData = {};

// Initialize checkout page
document.addEventListener('DOMContentLoaded', function() {
    loadCheckoutData();
    initializeCheckoutEventListeners();
    displayOrderSummary();
    initializePaymentMethods();
});

// Load checkout data from localStorage
function loadCheckoutData() {
    checkoutData = JSON.parse(localStorage.getItem('checkoutData')) || {};
    
    if (!checkoutData.items || checkoutData.items.length === 0) {
        // Redirect to cart if no items
        showNotification('Không có sản phẩm nào để thanh toán!', 'error');
        setTimeout(() => {
            window.location.href = 'cart.html';
        }, 2000);
        return;
    }
}

// Event listeners specific to checkout page
function initializeCheckoutEventListeners() {
    // Form validation
    const form = document.getElementById('checkout-form');
    if (form) {
        form.addEventListener('input', validateForm);
    }
    
    // Payment method selection
    const paymentRadios = document.querySelectorAll('input[name="payment"]');
    paymentRadios.forEach(radio => {
        radio.addEventListener('change', function() {
            updatePaymentForm(this.value);
        });
    });
    
    // Card number formatting
    const cardNumberInput = document.getElementById('card-number');
    if (cardNumberInput) {
        cardNumberInput.addEventListener('input', formatCardNumber);
    }
    
    // Card expiry formatting
    const cardExpiryInput = document.getElementById('card-expiry');
    if (cardExpiryInput) {
        cardExpiryInput.addEventListener('input', formatCardExpiry);
    }
    
    // CVV validation
    const cardCvvInput = document.getElementById('card-cvv');
    if (cardCvvInput) {
        cardCvvInput.addEventListener('input', function() {
            this.value = this.value.replace(/\D/g, '').slice(0, 3);
        });
    }
    
    // Auto-fill user info if logged in
    fillUserInfo();
}

// Fill user information if logged in
function fillUserInfo() {
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    if (currentUser) {
        const nameInput = document.getElementById('customer-name');
        const emailInput = document.getElementById('customer-email');
        const phoneInput = document.getElementById('customer-phone');
        
        if (nameInput && currentUser.name) nameInput.value = currentUser.name;
        if (emailInput && currentUser.email) emailInput.value = currentUser.email;
        if (phoneInput && currentUser.phone) phoneInput.value = currentUser.phone;
    }
}

// Display order summary
function displayOrderSummary() {
    const orderItems = document.getElementById('order-items');
    const checkoutSubtotal = document.getElementById('checkout-subtotal');
    const checkoutDiscount = document.getElementById('checkout-discount');
    const checkoutDiscountAmount = document.getElementById('checkout-discount-amount');
    const checkoutTotal = document.getElementById('checkout-total');
    
    if (!orderItems || !checkoutData.items) return;
    
    // Display items
    orderItems.innerHTML = checkoutData.items.map(item => `
        <div class="order-item">
            <div class="item-info-small">
                <div class="item-name">${item.title}</div>
                <div class="item-details-small">
                    ${item.quantity} vé × ${formatCurrency(item.price)}
                </div>
            </div>
            <div class="item-price-small">${formatCurrency(item.price * item.quantity)}</div>
        </div>
    `).join('');
    
    // Update totals
    if (checkoutSubtotal) checkoutSubtotal.textContent = formatCurrency(checkoutData.subtotal);
    if (checkoutTotal) checkoutTotal.textContent = formatCurrency(checkoutData.total);
    
    // Show/hide discount
    if (checkoutData.discountAmount > 0 && checkoutDiscount && checkoutDiscountAmount) {
        checkoutDiscount.style.display = 'flex';
        checkoutDiscountAmount.textContent = formatCurrency(checkoutData.discountAmount);
    } else if (checkoutDiscount) {
        checkoutDiscount.style.display = 'none';
    }
    
    // Set show date and time
    const showDate = document.getElementById('show-date');
    const showTime = document.getElementById('show-time');
    if (showDate) showDate.textContent = getCurrentDate();
    if (showTime) showTime.textContent = getCurrentTime();
}

// Seat selection
function selectSeat(seatElement) {
    const seatId = seatElement.getAttribute('data-seat');
    
    if (seatElement.classList.contains('occupied')) {
        showNotification('Ghế này đã được đặt!', 'error');
        return;
    }
    
    if (seatElement.classList.contains('selected')) {
        // Deselect seat
        seatElement.classList.remove('selected');
        selectedSeats = selectedSeats.filter(seat => seat !== seatId);
    } else {
        // Check maximum seats (based on total tickets)
        const totalTickets = checkoutData.items.reduce((sum, item) => sum + item.quantity, 0);
        
        if (selectedSeats.length >= totalTickets) {
            showNotification(`Bạn chỉ có thể chọn tối đa ${totalTickets} ghế!`, 'error');
            return;
        }
        
        // Select seat
        seatElement.classList.add('selected');
        selectedSeats.push(seatId);
    }
    
    updateSelectedSeatsDisplay();
}

// Update selected seats display
function updateSelectedSeatsDisplay() {
    const selectedSeatsDisplay = document.getElementById('selected-seats-list');
    
    if (selectedSeatsDisplay) {
        if (selectedSeats.length === 0) {
            selectedSeatsDisplay.textContent = 'Chưa chọn ghế nào';
            selectedSeatsDisplay.style.color = '#999';
        } else {
            selectedSeatsDisplay.textContent = selectedSeats.join(', ');
            selectedSeatsDisplay.style.color = '#667eea';
            selectedSeatsDisplay.style.fontWeight = 'bold';
        }
    }
}

// Payment methods
function initializePaymentMethods() {
    // Show credit card form by default
    updatePaymentForm('credit-card');
}

function updatePaymentForm(paymentMethod) {
    // Hide all payment forms
    document.querySelectorAll('.payment-form').forEach(form => {
        form.classList.remove('active');
    });
    
    // Show selected payment form
    const selectedForm = document.getElementById(`${paymentMethod}-form`);
    if (selectedForm) {
        selectedForm.classList.add('active');
    }
    
    // Update pay button text
    const payBtn = document.querySelector('.pay-btn');
    if (payBtn) {
        const paymentTexts = {
            'credit-card': 'Thanh toán bằng thẻ',
            'momo': 'Thanh toán bằng MoMo',
            'vnpay': 'Thanh toán bằng VNPay',
            'banking': 'Chuyển khoản ngân hàng'
        };
        
        payBtn.innerHTML = `<i class="fas fa-lock"></i> ${paymentTexts[paymentMethod] || 'Thanh toán'}`;
    }
}

// Form validation
function validateForm() {
    const requiredFields = [
        'customer-name',
        'customer-phone',
        'customer-email'
    ];
    
    let isValid = true;
    
    requiredFields.forEach(fieldId => {
        const field = document.getElementById(fieldId);
        if (field && !field.value.trim()) {
            isValid = false;
            field.style.borderColor = '#e74c3c';
        } else if (field) {
            field.style.borderColor = '#e0e0e0';
        }
    });
    
    // Email validation
    const emailField = document.getElementById('customer-email');
    if (emailField && emailField.value) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(emailField.value)) {
            isValid = false;
            emailField.style.borderColor = '#e74c3c';
        }
    }
    
    // Phone validation
    const phoneField = document.getElementById('customer-phone');
    if (phoneField && phoneField.value) {
        const phoneRegex = /^[0-9]{10,11}$/;
        if (!phoneRegex.test(phoneField.value)) {
            isValid = false;
            phoneField.style.borderColor = '#e74c3c';
        }
    }
    
    return isValid;
}

// Card number formatting
function formatCardNumber(e) {
    let value = e.target.value.replace(/\s/g, '').replace(/\D/g, '');
    let formattedValue = value.replace(/(.{4})/g, '$1 ').trim();
    
    if (formattedValue.length > 19) {
        formattedValue = formattedValue.slice(0, 19);
    }
    
    e.target.value = formattedValue;
}

// Card expiry formatting
function formatCardExpiry(e) {
    let value = e.target.value.replace(/\D/g, '');
    
    if (value.length >= 2) {
        value = value.slice(0, 2) + '/' + value.slice(2, 4);
    }
    
    e.target.value = value;
}

// Process payment
function processPayment() {
    // Validate form
    if (!validateForm()) {
        showNotification('Vui lòng điền đầy đủ thông tin!', 'error');
        return;
    }
    
    // Check seat selection
    const totalTickets = checkoutData.items.reduce((sum, item) => sum + item.quantity, 0);
    if (selectedSeats.length !== totalTickets) {
        showNotification(`Vui lòng chọn ${totalTickets} ghế!`, 'error');
        return;
    }
    
    // Get selected payment method
    const selectedPaymentMethod = document.querySelector('input[name="payment"]:checked');
    if (!selectedPaymentMethod) {
        showNotification('Vui lòng chọn phương thức thanh toán!', 'error');
        return;
    }
    
    // Validate payment method specific fields
    if (selectedPaymentMethod.value === 'credit-card') {
        if (!validateCreditCard()) {
            return;
        }
    }
    
    // Show loading
    const payBtn = document.querySelector('.pay-btn');
    payBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý thanh toán...';
    payBtn.disabled = true;
    
    // Simulate payment processing
    setTimeout(() => {
        processPaymentSuccess();
    }, 2000);
}

// Validate credit card
function validateCreditCard() {
    const cardNumber = document.getElementById('card-number').value.replace(/\s/g, '');
    const cardName = document.getElementById('card-name').value;
    const cardExpiry = document.getElementById('card-expiry').value;
    const cardCvv = document.getElementById('card-cvv').value;
    
    if (!cardNumber || cardNumber.length < 16) {
        showNotification('Số thẻ không hợp lệ!', 'error');
        return false;
    }
    
    if (!cardName) {
        showNotification('Vui lòng nhập tên chủ thẻ!', 'error');
        return false;
    }
    
    if (!cardExpiry || cardExpiry.length < 5) {
        showNotification('Ngày hết hạn không hợp lệ!', 'error');
        return false;
    }
    
    if (!cardCvv || cardCvv.length < 3) {
        showNotification('CVV không hợp lệ!', 'error');
        return false;
    }
    
    // Check expiry date
    const [month, year] = cardExpiry.split('/');
    const expiryDate = new Date(2000 + parseInt(year), parseInt(month) - 1);
    const now = new Date();
    
    if (expiryDate < now) {
        showNotification('Thẻ đã hết hạn!', 'error');
        return false;
    }
    
    return true;
}

// Payment success
function processPaymentSuccess() {
    // Create booking data
    const bookingData = {
        id: 'CH' + Date.now(),
        items: checkoutData.items,
        customerInfo: {
            name: document.getElementById('customer-name').value,
            phone: document.getElementById('customer-phone').value,
            email: document.getElementById('customer-email').value
        },
        seats: selectedSeats,
        theater: 'CinemaHub Vincom Đồng Khởi',
        date: getCurrentDate(),
        time: getCurrentTime(),
        room: 'Phòng 1',
        paymentMethod: document.querySelector('input[name="payment"]:checked').value,
        promoCode: checkoutData.promoCode,
        discountAmount: checkoutData.discountAmount,
        subtotal: checkoutData.subtotal,
        total: checkoutData.total,
        bookingTime: new Date().toISOString(),
        status: 'confirmed'
    };
    
    // Save booking
    const bookings = JSON.parse(localStorage.getItem('bookings')) || [];
    bookings.push(bookingData);
    localStorage.setItem('bookings', JSON.stringify(bookings));
    
    // Clear cart and checkout data
    localStorage.removeItem('cart');
    localStorage.removeItem('checkoutData');
    localStorage.removeItem('discountAmount');
    localStorage.removeItem('promoCode');
    
    // Show success modal
    showSuccessModal(bookingData);
}

// Show success modal
function showSuccessModal(bookingData) {
    const modal = document.getElementById('success-modal');
    const ticketDetails = document.getElementById('ticket-details');
    
    if (modal && ticketDetails) {
        // Populate ticket details
        ticketDetails.innerHTML = `
            <div style="text-align: left; background: #f8f9fa; padding: 1rem; border-radius: 8px;">
                <p><strong>Mã đặt vé:</strong> ${bookingData.id}</p>
                <p><strong>Phim:</strong> ${bookingData.items.map(item => `${item.title} (${item.quantity} vé)`).join(', ')}</p>
                <p><strong>Rạp:</strong> ${bookingData.theater}</p>
                <p><strong>Phòng:</strong> ${bookingData.room}</p>
                <p><strong>Ngày giờ:</strong> ${bookingData.date} - ${bookingData.time}</p>
                <p><strong>Ghế:</strong> ${bookingData.seats.join(', ')}</p>
                <p><strong>Tổng tiền:</strong> ${formatCurrency(bookingData.total)}</p>
            </div>
        `;
        
        modal.style.display = 'block';
        
        // Update checkout steps
        updateCheckoutSteps(3);
        
        // Send confirmation email (simulated)
        setTimeout(() => {
            showNotification('Email xác nhận đã được gửi!', 'success');
        }, 3000);
    }
}

// Update checkout steps
function updateCheckoutSteps(activeStep) {
    const steps = document.querySelectorAll('.step');
    steps.forEach((step, index) => {
        if (index + 1 <= activeStep) {
            step.classList.add('active');
        } else {
            step.classList.remove('active');
        }
    });
}

// Go to home
function goToHome() {
    window.location.href = 'index.html';
}

// Utility functions
function getCurrentDate() {
    const today = new Date();
    return today.toLocaleDateString('vi-VN');
}

function getCurrentTime() {
    const now = new Date();
    const times = ['14:00', '16:30', '19:00', '21:30'];
    return times[Math.floor(Math.random() * times.length)];
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}

// Show notification function
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.innerHTML = `
        <div class="notification-content">
            <i class="fas ${getNotificationIcon(type)}"></i>
            <span>${message}</span>
        </div>
    `;
    
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
    
    document.body.appendChild(notification);
    
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

// Export functions for global use
window.selectSeat = selectSeat;
window.processPayment = processPayment;
window.goToHome = goToHome;