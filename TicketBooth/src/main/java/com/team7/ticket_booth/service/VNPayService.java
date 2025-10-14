package com.team7.ticket_booth.service;

import com.team7.ticket_booth.model.Payment;
import com.team7.ticket_booth.model.Ticket;
import com.team7.ticket_booth.model.enums.Status;
import com.team7.ticket_booth.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@RequiredArgsConstructor
public class VNPayService {

    // Lấy các giá trị cấu hình từ file application.properties
    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.payUrl}")
    private String vnp_PayUrl;

    @Value("${vnpay.returnUrl}")
    private String vnp_ReturnUrl;

    private final PaymentService paymentService;
    private final OrderService orderService;

    public String createPaymentUrl(HttpServletRequest req, int amount, String orderId, String orderInfo) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other"; // Hoặc lấy từ request nếu cần
        // Sử dụng orderId (UUID) làm TxnRef để tiện đối soát (loại bỏ dấu '-')
        String vnp_TxnRef = orderId != null ? orderId.replace("-", "") : VNPayUtil.getRandomNumber(8);
    String vnp_IpAddr = VNPayUtil.getIpAddress(req);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
    vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Sắp xếp các tham số và tạo chuỗi dữ liệu để hash
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                try {
                    // Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                } catch (UnsupportedEncodingException e) {
                    // Handle exception
                    e.printStackTrace();
                }
            }
        }

        String queryUrl = query.toString();
    String vnp_SecureHash = VNPayUtil.hmacSHA512(vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        String url = vnp_PayUrl + "?" + queryUrl;
        try {
            // Lưu tạm URL vào payment để FE có thể hiển thị/ghi log
            if (orderId != null && !orderId.isEmpty()) {
                UUID uuid = parseOrderId(vnp_TxnRef);
                if (uuid != null) {
                    paymentService.updatePaymentUrl(uuid, url);
                }
            }
        } catch (Exception ignored) {}
        return url;
    }

    public Map<String, String> processIpnResponse(Map<String, String> allParams) {
        Map<String, String> response = new HashMap<>();

        try {
            String vnp_SecureHash = allParams.get("vnp_SecureHash");

            // Xóa các tham số hash ra khỏi map để chuẩn bị cho việc kiểm tra checksum
            allParams.remove("vnp_SecureHash");
            allParams.remove("vnp_SecureHashType");

            // Sắp xếp và tạo chuỗi hash
            String signValue = VNPayUtil.hashAllFields(allParams, vnp_HashSecret);

            if (signValue.equals(vnp_SecureHash)) {
                String txnRef = allParams.get("vnp_TxnRef");
                UUID orderId = parseOrderId(txnRef);
                long amount = Long.parseLong(allParams.get("vnp_Amount"));
                String responseCode = allParams.get("vnp_ResponseCode");
                String providerTxnNo = allParams.get("vnp_TransactionNo");
                String bankCode = allParams.get("vnp_BankCode"); // VNPay bank or card scheme

                // TODO: 1. Tìm đơn hàng trong DB theo orderId (checkOrderId)
                // TODO: 2. Kiểm tra số tiền có khớp không (checkAmount)
                // TODO: 3. Kiểm tra trạng thái đơn hàng có phải là "chờ thanh toán" không (checkOrderStatus)

                boolean isOrderValid = orderId != null; // Kết quả của bước 1
                boolean isAmountValid = true; // Kết quả của bước 2
                boolean isOrderStatusPending = true; // Kết quả của bước 3

                if (isOrderValid) {
                    if (isAmountValid) {
                        if (isOrderStatusPending) {
                            if ("00".equals(responseCode)) {
                                // Cập nhật trạng thái thanh toán thành công
                                paymentService.updatePaymentStatus(orderId, Status.PAID);
                                if (providerTxnNo != null) {
                                    paymentService.updateProviderTxnId(orderId, providerTxnNo);
                                }
                                // Xác định phương thức từ VNPay: thẻ nội địa (ATM) coi là BANK, quốc tế (VISA/MASTERCARD/...) coi là CARD
                                if (bankCode != null) {
                                    String derived = bankCode;
                                    Payment payment = paymentService.getByOdrerId(orderId);
                                    if (derived != null) {
                                        paymentService.updatePaymentMethod(payment, derived);
                                    }
                                }
                            } else {
                                // Cập nhật trạng thái thất bại
                                List <Ticket> tickets = orderService.getTicketsByOrderId(orderId);
                                for (Ticket ticket : tickets) {
                                    ticket.setOrder(null);
                                }
                                orderService.deleteOrder(orderId);
                            }
                            response.put("RspCode", "00");
                            response.put("Message", "Confirm Success");
                        } else {
                            response.put("RspCode", "02");
                            response.put("Message", "Order already confirmed");
                        }
                    } else {
                        response.put("RspCode", "04");
                        response.put("Message", "Invalid Amount");
                    }
                } else {
                    response.put("RspCode", "01");
                    response.put("Message", "Order not Found");
                }
            } else {
                response.put("RspCode", "97");
                response.put("Message", "Invalid Checksum");
            }
        } catch (Exception e) {
            response.put("RspCode", "99");
            response.put("Message", "Unknown error");
        }

        return response;
    }

    public boolean validateSignature(Map<String, String> allParams) {
        try {
            String vnp_SecureHash = allParams.get("vnp_SecureHash");
            Map<String, String> params = new HashMap<>(allParams);
            params.remove("vnp_SecureHash");
            params.remove("vnp_SecureHashType");
            String signValue = VNPayUtil.hashAllFields(params, vnp_HashSecret);
            return signValue.equals(vnp_SecureHash);
        } catch (Exception e) {
            return false;
        }
    }

    public UUID parseOrderId(String txnRef) {
        if (txnRef == null) return null;
        String raw = txnRef.trim();
        if (raw.length() == 32) {
            // Chèn dấu '-' theo định dạng UUID 8-4-4-4-12
            StringBuilder sb = new StringBuilder();
            sb.append(raw, 0, 8).append('-')
              .append(raw, 8, 12).append('-')
              .append(raw, 12, 16).append('-')
              .append(raw, 16, 20).append('-')
              .append(raw, 20, 32);
            raw = sb.toString();
        }
        try {
            return UUID.fromString(raw);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}