// Tickets page functionality

document.addEventListener('DOMContentLoaded', function() {
    loadTickets();
});

// Sample tickets data (in real app, this would come from backend)
const sampleTickets = [
    {
        id: 'ticket1',
        movieTitle: 'Avengers: Endgame',
        theater: 'TicketBooth Vincom Đồng Khởi',
        showtime: '15/10/2024 - 19:30',
        seats: 'Phòng 5 - Ghế A5, A6',
        price: '180.000 VNĐ',
        status: 'upcoming',
        poster: 'https://via.placeholder.com/120x180?text=Avengers'
    },
    {
        id: 'ticket2',
        movieTitle: 'Spider-Man: No Way Home',
        theater: 'TicketBooth Crescent Mall',
        showtime: '10/10/2024 - 21:00',
        seats: 'Phòng 3 - Ghế B3, B4',
        price: '160.000 VNĐ',
        status: 'completed',
        poster: 'https://via.placeholder.com/120x180?text=SpiderMan'
    }
];

function loadTickets() {
    const ticketsList = document.querySelector('.tickets-list');
    const emptyState = document.querySelector('.empty-tickets');
    
    // Get tickets from localStorage or use sample data
    const tickets = JSON.parse(localStorage.getItem('userTickets')) || sampleTickets;
    
    if (tickets.length === 0) {
        ticketsList.innerHTML = '';
        emptyState.style.display = 'block';
        return;
    }
    
    emptyState.style.display = 'none';
    displayTickets(tickets);
}

function displayTickets(tickets) {
    const ticketsList = document.querySelector('.tickets-list');
    
    ticketsList.innerHTML = tickets.map(ticket => `
        <div class="ticket-card ${ticket.status}" data-status="${ticket.status}">
            <div class="ticket-poster">
                <img src="${ticket.poster}" alt="${ticket.movieTitle}">
            </div>
            <div class="ticket-details">
                <h3 class="movie-title">${ticket.movieTitle}</h3>
                <div class="ticket-info">
                    <div class="info-row">
                        <i class="fas fa-map-marker-alt"></i>
                        <span>${ticket.theater}</span>
                    </div>
                    <div class="info-row">
                        <i class="fas fa-calendar-alt"></i>
                        <span>${ticket.showtime}</span>
                    </div>
                    <div class="info-row">
                        <i class="fas fa-couch"></i>
                        <span>${ticket.seats}</span>
                    </div>
                    <div class="info-row">
                        <i class="fas fa-money-bill"></i>
                        <span>${ticket.price}</span>
                    </div>
                </div>
            </div>
            <div class="ticket-actions">
                <span class="ticket-status ${ticket.status}">${getStatusText(ticket.status)}</span>
                <button class="btn-detail" onclick="showTicketDetail('${ticket.id}')">Chi tiết</button>
                ${ticket.status === 'upcoming' ? `<button class="btn-cancel" onclick="cancelTicket('${ticket.id}')">Hủy vé</button>` : ''}
            </div>
        </div>
    `).join('');
}

function getStatusText(status) {
    const statusTexts = {
        'upcoming': 'Sắp tới',
        'completed': 'Đã xem',
        'cancelled': 'Đã hủy'
    };
    return statusTexts[status] || status;
}

function filterTickets(status) {
    // Update active filter button
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.classList.add('active');
    
    // Show/hide tickets based on filter
    const ticketCards = document.querySelectorAll('.ticket-card');
    
    ticketCards.forEach(card => {
        if (status === 'all' || card.dataset.status === status) {
            card.style.display = 'grid';
        } else {
            card.style.display = 'none';
        }
    });
    
    // Check if any tickets are visible
    const visibleTickets = Array.from(ticketCards).filter(card => 
        card.style.display !== 'none'
    );
    
    const emptyState = document.querySelector('.empty-tickets');
    if (visibleTickets.length === 0) {
        emptyState.style.display = 'block';
        emptyState.querySelector('h3').textContent = `Không có vé ${getStatusText(status).toLowerCase()}`;
    } else {
        emptyState.style.display = 'none';
    }
}

function showTicketDetail(ticketId) {
    // Find ticket by ID
    const tickets = JSON.parse(localStorage.getItem('userTickets')) || sampleTickets;
    const ticket = tickets.find(t => t.id === ticketId);
    
    if (ticket) {
        // Show ticket detail modal or navigate to detail page
        alert(`Chi tiết vé:\n\nPhim: ${ticket.movieTitle}\nRạp: ${ticket.theater}\nSuất chiếu: ${ticket.showtime}\nGhế: ${ticket.seats}\nGiá: ${ticket.price}\nTrạng thái: ${getStatusText(ticket.status)}`);
        
        // In real app, you would:
        // - Open a modal with full ticket details
        // - Show QR code for entry
        // - Display booking reference
    }
}

function cancelTicket(ticketId) {
    if (confirm('Bạn có chắc chắn muốn hủy vé này không?\nLưu ý: Phí hủy vé có thể được áp dụng theo chính sách của rạp.')) {
        // Update ticket status
        let tickets = JSON.parse(localStorage.getItem('userTickets')) || sampleTickets;
        const ticketIndex = tickets.findIndex(t => t.id === ticketId);
        
        if (ticketIndex !== -1) {
            tickets[ticketIndex].status = 'cancelled';
            localStorage.setItem('userTickets', JSON.stringify(tickets));
            
            // Reload tickets display
            loadTickets();
            
            // Show success message
            alert('Vé đã được hủy thành công!\nBạn sẽ nhận được email xác nhận và thông tin hoàn tiền (nếu có).');
        }
    }
}

// Export functions for global use
window.filterTickets = filterTickets;
window.showTicketDetail = showTicketDetail;
window.cancelTicket = cancelTicket;