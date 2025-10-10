// Movie Detail Page functionality

let selectedShow = null;

document.addEventListener('DOMContentLoaded', function() {
    initializeShowtimes();
    initializeDatePicker();
});

function initializeDatePicker() {
    const datePicker = document.getElementById('show-date');
    if (datePicker) {
        // Set today as default or first available date
        const today = new Date().toISOString().split('T')[0];
        const minDate = datePicker.getAttribute('min');
        const maxDate = datePicker.getAttribute('max');
        
        // Check if today is within range
        if (today >= minDate && today <= maxDate) {
            datePicker.value = today;
        } else if (today < minDate) {
            datePicker.value = minDate;
        } else {
            datePicker.value = maxDate;
        }
        
        // Show showtimes for the initial date
        filterShowsByDate(datePicker.value);
    }
}

function filterShowsByDate(selectedDate) {
    // Hide all date groups
    const allDateGroups = document.querySelectorAll('.date-group');
    const noShowtimesMessage = document.getElementById('no-showtimes-message');
    let hasShowtimes = false;
    
    allDateGroups.forEach(group => {
        const groupDate = group.getAttribute('data-date');
        if (groupDate === selectedDate) {
            group.style.display = 'block';
            hasShowtimes = true;
        } else {
            group.style.display = 'none';
        }
    });
    
    // Show/hide no showtimes message
    if (noShowtimesMessage) {
        noShowtimesMessage.style.display = hasShowtimes ? 'none' : 'block';
    }
}

function initializeShowtimes() {
    // Add click handlers to showtime buttons
    document.querySelectorAll('.showtime-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            // Remove selected class from all buttons
            document.querySelectorAll('.showtime-btn').forEach(b => {
                b.classList.remove('selected');
            });
            
            // Add selected class to clicked button
            this.classList.add('selected');
            
            // Get theater name and date from parent elements
            const theaterGroup = this.closest('.theater-group');
            const dateGroup = this.closest('.date-group');
            
            // Store selected showtime
            selectedShow = {
                id: this.getAttribute('data-id'),
                time: this.querySelector('.showtime-time').textContent,
                endTime: this.querySelector('.showtime-info') ? 
                    this.querySelector('.showtime-info').textContent.replace('~ ', '') : '',
                theater: theaterGroup ? theaterGroup.querySelector('.theater-name span').textContent : 'N/A',
                date: dateGroup ? dateGroup.getAttribute('data-date') : 'N/A'
            };
            
            console.log('Selected showtime:', selectedShow);
            
            // Show booking confirmation or redirect to seat selection
            showBookingOptions();
        });
    });
}

function selectShowtime(showId) {
    // This function is called from the onclick attribute in HTML
    console.log('Showtime selected with ID:', showId);
    
    // Store showtime ID in sessionStorage
    sessionStorage.setItem('showId', showId);
    
    // Redirect to seat selection page with showtime ID
    window.location.href = '/reservation?showId=' + showId;
}

function showBookingOptions() {
    if (!selectedShow) {
        alert('Vui lòng chọn suất chiếu');
        return;
    }
    
    const movieTitle = document.querySelector('.movie-title') ? 
        document.querySelector('.movie-title').textContent : 'N/A';
    
    // Format date to dd/MM/yyyy
    const dateObj = new Date(selectedShow.date);
    const formattedDate = dateObj.toLocaleDateString('vi-VN');
    
    const confirmMessage = `
Bạn đã chọn:
🎬 Phim: ${movieTitle}
🏢 Rạp: ${selectedShow.theater}
📅 Ngày: ${formattedDate}
🕐 Giờ chiếu: ${selectedShow.time}
${selectedShow.endTime ? '⏱️ Kết thúc: ' + selectedShow.endTime : ''}

Tiếp tục đặt vé?
    `;
    
    if (confirm(confirmMessage)) {
        // Redirect to seat selection page
        proceedToSeatSelection();
    }
}

function proceedToSeatSelection() {
    if (!selectedShow) {
        alert('Vui lòng chọn suất chiếu trước');
        return;
    }
    
    // Store booking info in sessionStorage for the next page
    const movieTitle = document.querySelector('.movie-title');
    const moviePoster = document.querySelector('.movie-poster-large');
    
    const bookingInfo = {
        movie: {
            title: movieTitle ? movieTitle.textContent : 'N/A',
            poster: moviePoster ? moviePoster.src : '',
        },
        showtime: selectedShow
    };
    
    sessionStorage.setItem('currentBooking', JSON.stringify(bookingInfo));
    
    // Redirect to seat selection page with showtime ID
    window.location.href = '/reservation?showId=' + selectedShow.id;
    
    console.log('Booking info saved:', bookingInfo);
}

// Export functions for global use
window.selectShowtime = selectShowtime;
window.showBookingOptions = showBookingOptions;
window.filterShowsByDate = filterShowsByDate;
