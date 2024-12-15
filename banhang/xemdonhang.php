<?php
include "connect.php";

$iduser = $_POST['iduser'];



// Main query to fetch orders
$query = 'SELECT * FROM `donhang` WHERE `iduser` = ?';
$stmt = $conn->prepare($query);
$stmt->bind_param('i', $iduser);
$stmt->execute();
$data = $stmt->get_result();

$result = [];
while ($row = $data->fetch_assoc()) {
    // Subquery to fetch order details
    $truyvan = 'SELECT * FROM `chitietdonhang` INNER JOIN `sanphammoi` ON `chitietdonhang`.`idsp` = `sanphammoi`.`id` WHERE `chitietdonhang`.`iddonhang` = ?';
    $stmt2 = $conn->prepare($truyvan);
    $stmt2->bind_param('i', $row['id']);
    $stmt2->execute();
    $data1 = $stmt2->get_result();

    $item = [];
    while ($row1 = $data1->fetch_assoc()) {
        $item[] = $row1;
    }
    $row['item'] = $item;
    $result[] = $row;
}

if (!empty($result)) {
    $arr = [
        'success' => true,
        'message' => 'thanh cong',
        'result' => $result
    ];
} else {
    $arr = [
        'success' => false,
        'message' => 'khong thanh cong',
        'result' => []
    ];
}

echo json_encode($arr);
?>
