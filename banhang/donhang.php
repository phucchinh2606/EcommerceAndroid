<?php 
include "connect.php";

$sdt = $_POST['sdt']; 
$email = $_POST['email'];
$tongtien = $_POST['tongtien'];
$iduser = $_POST['iduser'];
$diachi = $_POST['diachi'];
$soluong = $_POST['soluong'];
$chitiet = $_POST['chitiet'];

// Xác thực và làm sạch dữ liệu đầu vào ở đây

// Sử dụng câu lệnh đã chuẩn bị
$stmt = $conn->prepare('INSERT INTO `donhang`(`iduser`, `diachi`, `sodienthoai`, `email`, `soluong`, `tongtien`) VALUES (?, ?, ?, ?, ?, ?)');
$stmt->bind_param("isssis", $iduser, $diachi, $sdt, $email, $soluong, $tongtien);

if ($stmt->execute()) {
    $stmt->close();

    $stmt = $conn->prepare('SELECT id AS iddonhang FROM `donhang` WHERE `iduser` = ? ORDER BY id DESC LIMIT 1');
    $stmt->bind_param("i", $iduser);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($row = $result->fetch_assoc()) {
        $iddonhang = $row['iddonhang'];

        if (!empty($chitiet)) {
            $chitiet = json_decode($chitiet, true);
            
            foreach ($chitiet as $item) {
                // Sử dụng câu lệnh đã chuẩn bị cho truy vấn này
                $stmt = $conn->prepare('INSERT INTO `chitietdonhang`(`iddonhang`, `idsp`, `soluong`, `gia`) VALUES (?, ?, ?, ?)');
                $stmt->bind_param("iiis", $iddonhang, $item['idsp'], $item['soluong'], $item['giasp']);
                
                if (!$stmt->execute()) {
                    $arr = ['success' => false, 'message' => "Thêm chi tiết không thành công"];
                    break;
                }
            }

            $arr = ['success' => true, 'message' => "Thành công"];
        } else {
            $arr = ['success' => false, 'message' => "Không có chi tiết nào"];
        }
    } else {
        $arr = ['success' => false, 'message' => "Không tìm thấy đơn hàng"];
    }
} else {
    $arr = ['success' => false, 'message' => "Không thể tạo đơn hàng"];
}

$stmt->close();
$conn->close();
echo json_encode($arr);
?>