// Seat Selection Page JavaScript

let selectedSeats = [];
let allTicketsData = window.allTicketsData || [];
let availableTicketsData = window.availableTicketsData || [];
let countdownInterval;
let timeRemaining = 600; // 10 minutes in seconds

// Create a Set of available ticket IDs for quick lookup
let availableTicketIds = new Set(availableTicketsData.map(ticket => ticket.id));

document.addEventListener('DOMContentLoaded', function() {
    console.log('All tickets data:', allTicketsData);
    console.log('Available tickets data:', availableTicketsData);
    console.log('Available ticket IDs:', availableTicketIds);
    initializeSeatMap();
    startCountdown();
});

function initializeSeatMap() {
    if (!allTicketsData || allTicketsData.length === 0) {
        document.getElementById('seat-map').innerHTML = '<p style="text-align: center; color: #999;">Không có dữ liệu ghế</p>';
        return;
    }
    
    // Group tickets by position (row)
    const seatsByRow = {};
    allTicketsData.forEach(ticket => {
        const position = ticket.position; // e.g., "A1", "B2", "C3"
        const row = position.charAt(0); // Get first character as row
        
        if (!seatsByRow[row]) {
            seatsByRow[row] = [];
        }
        seatsByRow[row].push(ticket);
    });
    
    // Sort rows alphabetically
    const sortedRows = Object.keys(seatsByRow).sort();
    
    // Create seat map HTML
    const seatMapContainer = document.getElementById('seat-map');
    seatMapContainer.innerHTML = '';
    
    sortedRows.forEach(row => {
        const rowDiv = document.createElement('div');
        rowDiv.className = 'seat-row';
        
        // Add row label
        const rowLabel = document.createElement('div');
        rowLabel.className = 'row-label';
        rowLabel.textContent = row;
        rowDiv.appendChild(rowLabel);
        
        // Sort seats in the row by position number
        const seats = seatsByRow[row].sort((a, b) => {
            const numA = parseInt(a.position.substring(1));
            const numB = parseInt(b.position.substring(1));
            return numA - numB;
        });
        
        // Add seats
        seats.forEach(ticket => {
            const seatDiv = createSeatElement(ticket);
            rowDiv.appendChild(seatDiv);
        });
        
        seatMapContainer.appendChild(rowDiv);
    });
}

function createSeatElement(ticket) {
    const seatDiv = document.createElement('div');
    seatDiv.className = 'seat';
    
    // Add seat type class (NORMAL, VIP, COUPLE)
    if (ticket.seatType) {
        switch(ticket.seatType) {
            case 'VIP':
                seatDiv.classList.add('seat-vip');
                break;
            case 'COUPLE':
                seatDiv.classList.add('seat-couple');
                break;
            case 'NORMAL':
            default:
                seatDiv.classList.add('seat-normal');
                break;
        }
    }
    
    // Check if seat is available (ticket is in availableTickets list)
    const isAvailable = availableTicketIds.has(ticket.id);
    
    if (!isAvailable) {
        // Seat is occupied (not in available list)
        seatDiv.classList.add('seat-occupied');
        seatDiv.title = 'Ghế đã được đặt';
    } else {
        // Seat is available for booking
        seatDiv.classList.add('seat-available');
        seatDiv.setAttribute('data-ticket-id', ticket.id);
        seatDiv.setAttribute('data-position', ticket.position);
        seatDiv.setAttribute('data-price', ticket.price);
        seatDiv.setAttribute('data-seat-type', ticket.seatType);
        
        // Update title with seat type
        let seatTypeLabel = 'Ghế thường';
        if (ticket.seatType === 'VIP') seatTypeLabel = 'Ghế VIP';
        if (ticket.seatType === 'COUPLE') seatTypeLabel = 'Ghế đôi';
        
        seatDiv.title = `${seatTypeLabel} ${ticket.position} - ${formatPrice(ticket.price)}`;
        
        // Add click event
        seatDiv.addEventListener('click', function() {
            toggleSeatSelection(this, ticket);
        });
    }
    
    // Display seat position number only
    const seatNumber = ticket.position.substring(1);
    seatDiv.textContent = seatNumber;
    
    return seatDiv;
}

function toggleSeatSelection(seatElement, ticket) {
    if (seatElement.classList.contains('seat-occupied')) {
        return;
    }
    
    const ticketId = ticket.id;
    const position = ticket.position;
    const price = ticket.price;
    
    if (seatElement.classList.contains('seat-selected')) {
        // Deselect seat
        seatElement.classList.remove('seat-selected');
        seatElement.classList.add('seat-available');
        
        // Remove from selected seats array
        selectedSeats = selectedSeats.filter(seat => seat.id !== ticketId);
    } else {
        // Select seat
        seatElement.classList.remove('seat-available');
        seatElement.classList.add('seat-selected');
        
        // Add to selected seats array
        selectedSeats.push({
            id: ticketId,
            position: position,
            price: price
        });
    }
    
    updateBookingSummary();
}


function toggleSeat(seatElement) {
    const seatId = seatElement.dataset.seat;
    
    if (seatElement.disabled || seatElement.classList.contains('occupied')) {
        return;
    }
    
    if (seatElement.classList.contains('selected')) {
        // Deselect seat
        seatElement.classList.remove('selected');
        const index = selectedSeats.indexOf(seatId);
        if (index > -1) {
            selectedSeats.splice(index, 1);
        }
    } else {
        // Select seat (limit to 8 seats maximum)
        if (selectedSeats.length >= 8) {
            alert('Bạn chỉ có thể chọn tối đa 8 ghế');
            return;
        }
        
        seatElement.classList.add('selected');
        selectedSeats.push(seatId);
    }
    
    updateBookingSummary();
}

function updateBookingSummary() {
    const selectedSeatsDisplay = document.getElementById('selected-seats-display');
    const totalPriceElement = document.getElementById('total-price');
    const btnContinue = document.getElementById('btn-continue');
    
    if (!selectedSeatsDisplay || !totalPriceElement || !btnContinue) return;
    
    if (selectedSeats.length === 0) {
        selectedSeatsDisplay.innerHTML = '<p class="no-seats-selected">Chưa chọn ghế nào</p>';
        totalPriceElement.textContent = '0 vnđ';
        btnContinue.disabled = true;
    } else {
        // Display selected seats
        selectedSeatsDisplay.innerHTML = selectedSeats
            .map(seat => `<span class="selected-seat-item">${seat.position}</span>`)
            .join('');
        
        // Calculate total price
        const totalPrice = selectedSeats.reduce((sum, seat) => sum + seat.price, 0);
        totalPriceElement.textContent = formatPrice(totalPrice);
        
        btnContinue.disabled = false;
    }
}

function formatPrice(price) {
    return new Intl.NumberFormat('vi-VN', { 
        style: 'currency', 
        currency: 'VND' 
    }).format(price);
}

function startCountdown() {
    const countdownElement = document.getElementById('countdown-timer');
    if (!countdownElement) return;
    
    countdownInterval = setInterval(() => {
        timeRemaining--;
        
        const minutes = Math.floor(timeRemaining / 60);
        const seconds = timeRemaining % 60;
        
        countdownElement.textContent = `${minutes}:${seconds.toString().padStart(2, '0')}`;
        
        // Change color when time is running out
        if (timeRemaining <= 60) {
            countdownElement.style.color = '#d32f2f';
            countdownElement.style.animation = 'pulse 1s infinite';
        } else if (timeRemaining <= 180) {
            countdownElement.style.color = '#f57c00';
        }
        
        // Time's up
        if (timeRemaining <= 0) {
            clearInterval(countdownInterval);
            alert('Hết thời gian giữ ghế! Vui lòng chọn lại.');
            window.location.reload();
        }
    }, 1000);
}

function proceedToCheckout() {
    if (selectedSeats.length === 0) {
        alert('Vui lòng chọn ít nhất một ghế');
        return;
    }
    
    // Ưu tiên lấy userId từ template; fallback gọi /api/v1/auth/me để lấy id nếu cần
    const ensureUserId = async () => {
        if (window.userId) return window.userId;
        try {
            const res = await fetch('/api/v1/auth/me', {
                headers: { 'X-Requested-With': 'XMLHttpRequest' },
                credentials: 'same-origin'
            });
            if (res.ok) {
                const me = await res.json();
                if (me && (me.id || me.userId)) {
                    const id = me.id || me.userId;
                    window.userId = id;
                    return id;
                }
            }
        } catch (e) {
            // ignore, sẽ xử lý tiếp bên dưới
        }
        return null;
    };

    (async () => {
        const userId = await ensureUserId();
        if (!userId) {
            alert('Vui lòng đăng nhập để tiếp tục đặt vé');
            const redirectUrl = encodeURIComponent(window.location.href);
            window.location.href = `/login?redirect=${redirectUrl}`;
            return;
        }

        // Get selected payment method
        const paymentMethodElement = document.querySelector('input[name="payment-method"]:checked');
        if (!paymentMethodElement) {
            alert('Vui lòng chọn phương thức thanh toán');
            return;
        }
        const paymentMethod = paymentMethodElement.value;

        // Get ticket IDs
        const ticketIds = selectedSeats.map(seat => seat.id);

        // Create OrderRequestDTO đúng cấu trúc backend: UserId, ShowId, TicketIds, Method
        const orderRequest = {
            UserId: userId,
            ShowId: window.showId,
            TicketIds: ticketIds,
            Method: paymentMethod
        };

        console.log('Sending order request:', orderRequest);

        // Show loading
        const btnContinue = document.getElementById('btn-continue');
        const originalText = btnContinue.textContent;
        btnContinue.disabled = true;
        btnContinue.textContent = 'Đang xử lý...';

        // Send POST request to REST API (returns JSON)
        fetch('/api/v1/orders/booking', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'same-origin',
            body: JSON.stringify(orderRequest)
        })
        .then(response => {
            if (!response.ok) throw new Error('Đặt vé thất bại');
            return response.json();
        })
        .then(data => {
            if (!data || !data.orderCode) throw new Error('Thiếu orderCode trong phản hồi');
            // Redirect to order detail page (GET)
            window.location.href = `/checkout?orderId=${data.orderCode}`;
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Có lỗi xảy ra khi đặt vé. Vui lòng thử lại!');
            btnContinue.disabled = false;
            btnContinue.textContent = originalText;
        });
    })();
}

// Add pulse animation for countdown
const style = document.createElement('style');
style.textContent = `
    @keyframes pulse {
        0%, 100% { transform: scale(1); }
        50% { transform: scale(1.05); }
    }
`;
document.head.appendChild(style);

// Export functions for global use
window.proceedToCheckout = proceedToCheckout;