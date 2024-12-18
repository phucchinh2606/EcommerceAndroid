<?php
include "connect.php";
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require 'PHPMailer/src/Exception.php';
require 'PHPMailer/src/PHPMailer.php';
require 'PHPMailer/src/SMTP.php';

$email = $_POST['email'];

// Sử dụng câu lệnh đã chuẩn bị để tránh SQL injection
$stmt = $conn->prepare('SELECT * FROM `user` WHERE `email` = ?');
$stmt->bind_param('s', $email);
$stmt->execute();
$result = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);

if (empty($result)) {
    $arr = [ 
        'success' => false, 
        'message' => "Email không chính xác", 
        'result' => [] 
    ]; 
} else {
    // Gửi email
    $email = md5($result[0]['email']);
    $pass = md5($result[0]['pass']);
    $link = "<a href='http://192.168.1.6/banhang/reset_pass.php?key=".$email."&reset=".$pass."'>Click để đặt lại mật khẩu</a>";

    $mail = new PHPMailer();
    $mail->CharSet = "utf-8";
    $mail->isSMTP();
    $mail->SMTPAuth = true;
    $mail->Username = "phucchinh2606@gmail.com"; // Thay đổi bằng cách sử dụng biến môi trường
    $mail->Password = "qwe"; // Thay đổi bằng cách sử dụng biến môi trường
    $mail->SMTPSecure = "ssl";  
    $mail->Host = "smtp.gmail.com";
    $mail->Port = "465";
    $mail->From="chinhdo266@gmail.com"
    $mail->FromName="App Bán Hàng"
    $mail->addAddress($email,'receiver_name');
    $mail->Subject = 'Đặt lại mật khẩu';
    $mail->isHTML(true);
    $mail->Body = $link;

    if ($mail->send()) {
        $arr = [ 
            'success' => true, 
            'message' => "Vui lòng kiểm tra email của bạn", 
            'result' => $result 
        ]; 
    } else {
        $arr = [ 
            'success' => false, 
            'message' => "Không thành công: " . $mail->ErrorInfo, 
            'result' => [] 
        ]; 
    }
}

$stmt->close();
$conn->close();
echo json_encode($arr);
?>