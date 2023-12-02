<?php
// Koneksi ke database
$koneksi = new mysqli("localhost", "root", "", "affogatoaffaircafe");

// Cek koneksi
if ($koneksi->connect_error) {
    die("Koneksi gagal: " . $koneksi->connect_error);
}

// Check if phoneNumber is set in the GET request
if (isset($_GET['phoneNumber'])) {
    $phoneNumber = $_GET['phoneNumber'];

    $sql = "SELECT name, email, birthdate, gender FROM user WHERE phone_number = ?";
    $stmt = $koneksi->prepare($sql);
    $stmt->bind_param("s", $phoneNumber);
    $stmt->execute();

    $result = $stmt->get_result();
    if ($row = $result->fetch_assoc()) {
        echo json_encode($row);
    } else {
        echo json_encode(["message" => "No user found"]);
    }

    $stmt->close();
} else {
    echo json_encode(["message" => "phoneNumber parameter is missing"]);
}

$koneksi->close();
?>
