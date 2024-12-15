<?php
include "connect.php";

if (isset($_GET['key']) && isset($_GET['reset'])) {
    $email = $_GET['key'];
    $pass = $_GET['reset'];

    // Sử dụng câu lệnh đã chuẩn bị để tránh SQL injection
    $stmt = $conn->prepare("SELECT email, pass FROM user WHERE email = ? AND pass = ?");
    $stmt->bind_param("ss", $email, $pass);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        ?>
<form method="post" action="submit_new.php">
    <input type="hidden" name="email" value="<?php echo htmlspecialchars($email); ?>">
    <p>Nhập mật khẩu mới</p>
    <input type="password" name="password" required>
    <input type="submit" name="submit_password" value="Đặt lại mật khẩu">
</form>
<?php
    } else {
        echo "Thông tin không hợp lệ.";
    }

    $stmt->close();
} else {
    echo "Yêu cầu không hợp lệ.";
}

$conn->close();
?>