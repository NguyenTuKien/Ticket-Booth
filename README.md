# CinemaHub - Website Bán Vé Xem Phim

Một website hoàn chỉnh cho việc đặt vé xem phim online với giao diện đẹp và tính năng đầy đủ.

## 🎬 Tính năng

### Trang Chủ (index.html)
- Hero section với call-to-action
- Danh sách phim đang chiếu với thông tin chi tiết
- Hệ thống rạp chiếu
- Khuyến mãi và ưu đãi
- Navigation responsive

### Trang Đăng nhập/Đăng ký (login.html)
- Form đăng nhập với validation
- Form đăng ký với đầy đủ thông tin
- Đăng nhập bằng Facebook/Google (UI)
- Bảo mật mật khẩu
- Responsive design

### Trang Giỏ hàng (cart.html)
- Hiển thị danh sách vé đã chọn
- Chỉnh sửa số lượng vé
- Áp dụng mã khuyến mãi
- Tính toán tổng tiền tự động
- Xóa sản phẩm khỏi giỏ hàng

### Trang Thanh toán (checkout.html)
- Form thông tin khách hàng
- Chọn ghế ngồi trực quan
- Nhiều phương thức thanh toán
- Xác nhận đơn hàng
- Modal thành công

## 🛠️ Công nghệ sử dụng

- **HTML5**: Cấu trúc trang web semantic
- **CSS3**: Styling với Flexbox, Grid, Animations
- **JavaScript ES6+**: Logic tương tác và xử lý dữ liệu
- **Font Awesome**: Icons
- **Local Storage**: Lưu trữ dữ liệu phía client

## 📁 Cấu trúc thư mục

```
movie-ticket-website/
├── index.html              # Trang chủ
├── login.html              # Trang đăng nhập/đăng ký
├── cart.html               # Trang giỏ hàng
├── checkout.html           # Trang thanh toán
├── css/
│   └── style.css          # File CSS chính
├── js/
│   ├── main.js            # JavaScript chính
│   ├── auth.js            # Xử lý authentication
│   ├── cart.js            # Xử lý giỏ hàng
│   └── checkout.js        # Xử lý thanh toán
├── images/                 # Thư mục hình ảnh
└── README.md              # File hướng dẫn
```

## 🚀 Hướng dẫn sử dụng

### 1. Cài đặt
```bash
# Clone hoặc download project
# Không cần cài đặt dependencies
```

### 2. Chạy website
- Mở file `index.html` trong trình duyệt web
- Hoặc sử dụng Live Server trong VS Code

### 3. Tính năng chính

#### Đặt vé phim:
1. Chọn phim từ trang chủ
2. Nhấn "Đặt vé" để thêm vào giỏ hàng
3. Xem giỏ hàng và chỉnh sửa
4. Tiến hành thanh toán

#### Mã khuyến mãi có sẵn:
- `WEEKEND20`: Giảm 20% vé cuối tuần
- `FAMILY4`: Giảm 15% combo gia đình  
- `HAPPY30`: Giảm 30% Happy Hour
- `NEWUSER`: Giảm 25% cho khách hàng mới
- `STUDENT10`: Giảm 10% cho sinh viên

#### Thanh toán:
- Điền thông tin khách hàng
- Chọn ghế ngồi
- Chọn phương thức thanh toán
- Xác nhận và hoàn tất

## 🎨 Thiết kế

### Color Scheme
- Primary: `#1e3c72` (Navy Blue)
- Secondary: `#667eea` (Blue)
- Accent: `#ffd700` (Gold)
- Success: `#27ae60` (Green)
- Error: `#e74c3c` (Red)

### Typography
- Font chính: Segoe UI, system fonts
- Headings: Bold weights
- Body text: Regular weight

### Layout
- Responsive design với mobile-first approach
- Grid và Flexbox layout
- Sticky navigation
- Card-based design

## 📱 Responsive Design

Website được thiết kế responsive cho:
- Desktop (1200px+)
- Tablet (768px - 1199px)
- Mobile (< 768px)

## 🔧 Customization

### Thay đổi màu sắc:
Chỉnh sửa CSS variables trong file `style.css`

### Thêm phim mới:
Chỉnh sửa section `.movies-grid` trong `index.html`

### Thêm mã khuyến mãi:
Chỉnh sửa object `promoCodes` trong `cart.js`

## 📊 Local Storage Data

Website sử dụng Local Storage để lưu:
- `cart`: Giỏ hàng
- `currentUser`: Thông tin user hiện tại
- `checkoutData`: Dữ liệu checkout
- `discountAmount`: Số tiền giảm giá
- `promoCode`: Mã khuyến mãi đã áp dụng
- `bookings`: Lịch sử đặt vé

## 🔒 Security Features

- Input validation và sanitization
- Password strength requirements
- Form validation
- XSS protection (cơ bản)

## 🌟 Tính năng nâng cao

### Animations & Effects:
- Smooth scrolling
- Hover effects
- Loading animations
- Modal transitions
- Notification system

### User Experience:
- Auto-fill form data
- Real-time validation
- Shopping cart persistence
- Seat selection visualization
- Payment processing simulation

## 🐛 Bug Reports & Features

Để báo cáo lỗi hoặc đề xuất tính năng mới, vui lòng tạo issue mới.

## 📄 License

Dự án này được phát hành dưới MIT License.

## 👥 Contributors

- Developer: [Tên của bạn]
- Designer: [Tên designer]

## 📞 Support

Nếu cần hỗ trợ:
- Email: support@cinemahub.vn
- Phone: 1900 1234

---

**CinemaHub** - Trải nghiệm điện ảnh tuyệt vời nhất! 🎬✨