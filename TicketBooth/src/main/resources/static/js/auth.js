// Authentication JavaScript

// Global variables
let currentUser = JSON.parse(localStorage.getItem('currentUser')) || null;

// Initialize authentication
document.addEventListener('DOMContentLoaded', function() {
    checkAuthStatus();
    initializeAuthEventListeners();
});

// Event listeners
function initializeAuthEventListeners() {
    // Password visibility toggle
    document.querySelectorAll('.toggle-password').forEach(toggle => {
        toggle.addEventListener('click', function() {
            const passwordInput = document.getElementById(this.previousElementSibling?.id || this.parentNode.querySelector('input').id);
            togglePasswordVisibility(passwordInput, this);
        });
    });

    // Form validation
    const passwordInput = document.getElementById('register-password');
    const confirmPasswordInput = document.getElementById('confirm-password');
    
    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('input', function() {
            validatePasswordMatch(passwordInput, confirmPasswordInput);
        });
    }

    // Social login buttons
    document.querySelectorAll('.social-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const provider = this.classList.contains('facebook') ? 'Facebook' : 'Google';
            handleSocialLogin(provider);
        });
    });
}

// Password visibility toggle
function togglePassword(inputId, toggleIcon) {
    const input = document.getElementById(inputId);
    const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
    input.setAttribute('type', type);
    
    // Toggle icon
    toggleIcon.classList.toggle('fa-eye');
    toggleIcon.classList.toggle('fa-eye-slash');
}

function togglePasswordVisibility(input, icon) {
    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}

// Password validation
function validatePasswordMatch(passwordInput, confirmPasswordInput) {
    const password = passwordInput.value;
    const confirmPassword = confirmPasswordInput.value;
    
    if (confirmPassword && password !== confirmPassword) {
        confirmPasswordInput.setCustomValidity('Mật khẩu không khớp');
        confirmPasswordInput.style.borderColor = '#e74c3c';
    } else {
        confirmPasswordInput.setCustomValidity('');
        confirmPasswordInput.style.borderColor = '#e0e0e0';
    }
}

// Handle login
function handleLogin(event) {
    event.preventDefault();
    
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    const rememberMe = document.getElementById('remember-me').checked;
    
    // Simulate authentication
    if (email && password) {
        const userData = {
            id: Date.now(),
            email: email,
            name: email.split('@')[0],
            loginTime: new Date().toISOString()
        };
        
        // Save user data
        localStorage.setItem('currentUser', JSON.stringify(userData));
        
        if (rememberMe) {
            localStorage.setItem('rememberUser', 'true');
        }
        
        showNotification('Đăng nhập thành công!', 'success');
        
        // Redirect after delay
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 1500);
    } else {
        showNotification('Vui lòng nhập đầy đủ thông tin!', 'error');
    }
}

// Handle registration
function handleRegister(event) {
    event.preventDefault();
    
    const name = document.getElementById('register-name').value;
    const username = document.getElementById('register-username').value;
    const email = document.getElementById('register-email').value;
    const password = document.getElementById('register-password').value;
    const confirmPassword = document.getElementById('confirm-password').value;
    const agreeTerms = document.getElementById('agree-terms').checked;
    
    // Validate form
    if (!validateRegistrationForm(name, username, email, password, confirmPassword, agreeTerms)) {
        return;
    }
    
    // Create user object for backend API
    const userData = {
        username: username,
        password: password,
        email: email,
        fullName: name
    };
    
    // Show loading state
    const submitBtn = event.target.querySelector('button[type="submit"]');
    const originalText = submitBtn ? submitBtn.textContent : '';
    if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.textContent = 'Đang xử lý...';
    }

    // Call REST API to register
    fetch('/api/v1/auth/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
    .then(response => {
        if (!response.ok) {
            return response.json()
                .catch(() => ({message: 'Đăng ký thất bại'}))
                .then(err => {
                    throw new Error(err.message || 'Đăng ký thất bại!');
                });
        }
        return response.json();
    })
    .then(data => {
        // Success - show message from backend or default
        showNotification(data.message || 'Đăng ký thành công!', 'success');
        setTimeout(() => {
            window.location.href = '/login';
        }, 1200);
    })
    .catch(error => {
        // Error - restore button and show error message
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }
        showNotification(error.message || 'Có lỗi xảy ra, vui lòng thử lại!', 'error');
    });
}

// Validate registration form
function validateRegistrationForm(name, username, email, password, confirmPassword, agreeTerms) {
    // Check required fields
    if (!name || !username || !email || !password || !confirmPassword) {
        showNotification('Vui lòng điền đầy đủ thông tin!', 'error');
        return false;
    }
    
    // Check username format (only letters, numbers, and underscore)
    const usernameRegex = /^[a-zA-Z0-9_]{3,}$/;
    if (!usernameRegex.test(username)) {
        showNotification('Tên đăng nhập không hợp lệ! Chỉ chứa chữ cái, số và dấu gạch dưới, ít nhất 3 ký tự', 'error');
        return false;
    }
    
    // Check email format
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        showNotification('Email không hợp lệ!', 'error');
        return false;
    }
    
    // Check password length
    if (password.length < 6) {
        showNotification('Mật khẩu phải có ít nhất 6 ký tự!', 'error');
        return false;
    }
    
    // Check password match
    if (password !== confirmPassword) {
        showNotification('Mật khẩu không khớp!', 'error');
        return false;
    }

    // Check terms agreement
    if (!agreeTerms) {
        showNotification('Bạn phải đồng ý với điều khoản sử dụng!', 'error');
        return false;
    }
    
    // Check if email already exists
    const existingUsers = getRegisteredUsers();
    if (existingUsers.some(user => user.email === email)) {
        showNotification('Email này đã được đăng ký!', 'error');
        return false;
    }
    
    return true;
}

// Handle social login
function handleSocialLogin(provider) {
    // Simulate social login
    showNotification(`Đăng nhập bằng ${provider} đang được phát triển!`, 'info');
    
    // In real implementation, this would integrate with OAuth providers
    setTimeout(() => {
        const userData = {
            id: Date.now(),
            name: `User from ${provider}`,
            email: `user@${provider.toLowerCase()}.com`,
            provider: provider,
            loginTime: new Date().toISOString()
        };
        
        localStorage.setItem('currentUser', JSON.stringify(userData));
        showNotification(`Đăng nhập ${provider} thành công!`, 'success');
        
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 1500);
    }, 2000);
}

// Utility functions
function getRegisteredUsers() {
    return JSON.parse(localStorage.getItem('registeredUsers')) || [];
}

function checkAuthStatus() {
    if (currentUser) {
        updateAuthUI();
    }
}

function updateAuthUI() {
    const loginBtn = document.querySelector('.login-btn');
    if (loginBtn && currentUser) {
        loginBtn.textContent = currentUser.name;
        loginBtn.href = '#';
        loginBtn.addEventListener('click', showUserMenu);
    }
}

function showUserMenu(event) {
    event.preventDefault();
    
    // Create dropdown menu
    const existingMenu = document.querySelector('.user-menu');
    if (existingMenu) {
        existingMenu.remove();
        return;
    }
    
    const userMenu = document.createElement('div');
    userMenu.className = 'user-menu';
    userMenu.innerHTML = `
        <div class="user-menu-item" onclick="showProfile()">
            <i class="fas fa-user"></i> Thông tin cá nhân
        </div>
        <div class="user-menu-item" onclick="showBookingHistory()">
            <i class="fas fa-history"></i> Lịch sử đặt vé
        </div>
        <div class="user-menu-item" onclick="logout()">
            <i class="fas fa-sign-out-alt"></i> Đăng xuất
        </div>
    `;
    
    // Add styles for dropdown
    const styles = document.createElement('style');
    styles.textContent = `
        .user-menu {
            position: absolute;
            top: 100%;
            right: 0;
            background: white;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            z-index: 1000;
            min-width: 200px;
            padding: 0.5rem 0;
        }
        .user-menu-item {
            padding: 0.75rem 1rem;
            cursor: pointer;
            color: #333;
            transition: background 0.3s ease;
        }
        .user-menu-item:hover {
            background: #f8f9fa;
        }
        .user-menu-item i {
            margin-right: 0.5rem;
            color: #667eea;
        }
    `;
    
    if (!document.querySelector('#user-menu-styles')) {
        styles.id = 'user-menu-styles';
        document.head.appendChild(styles);
    }
    
    const loginBtnContainer = event.target.parentNode;
    loginBtnContainer.style.position = 'relative';
    loginBtnContainer.appendChild(userMenu);
    
    // Close menu when clicking outside
    setTimeout(() => {
        document.addEventListener('click', function closeMenu(e) {
            if (!userMenu.contains(e.target) && e.target !== event.target) {
                userMenu.remove();
                document.removeEventListener('click', closeMenu);
            }
        });
    }, 0);
}

function showProfile() {
    showNotification('Trang thông tin cá nhân đang được phát triển!', 'info');
}

function showBookingHistory() {
    showNotification('Trang lịch sử đặt vé đang được phát triển!', 'info');
}

function logout() {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('rememberUser');
    currentUser = null;
    
    showNotification('Đăng xuất thành công!', 'success');
    
    setTimeout(() => {
        window.location.href = 'index.html';
    }, 1500);
}

// Show notification function (if not already defined)
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

// Export functions for global use
window.showLogin = showLogin;
window.showRegister = showRegister;
window.togglePassword = togglePassword;
window.handleLogin = handleLogin;
window.handleRegister = handleRegister;
window.logout = logout;